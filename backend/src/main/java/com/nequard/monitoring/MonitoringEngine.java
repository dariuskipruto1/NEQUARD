package com.nequard.monitoring;

import com.nequard.network.Device;
import com.nequard.alerts.Alert;
import com.nequard.alerts.AlertRepository;
import com.nequard.network.DeviceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.*;
import java.time.Duration;
import java.time.Instant;

@Service
public class MonitoringEngine {
    private final DeviceRepository devices;
    private final MetricSnapshotRepository metrics;
    private final AlertRepository alerts;
    private final int timeoutMs;

    public MonitoringEngine(DeviceRepository devices, MetricSnapshotRepository metrics, AlertRepository alerts,
                            @Value("${nequard.monitoring.timeout-ms:2000}") int timeoutMs) {
        this.devices = devices;
        this.alerts = alerts;
        this.metrics = metrics;
        this.timeoutMs = timeoutMs;
    }

    @Scheduled(fixedDelayString = "${nequard.monitoring.interval-ms:30000}")
    public void collect() {
        for (Device device : devices.findAll()) {
            try { collectDevice(device); }
            catch (Exception ignored) { /* isolate failed targets */ }
        }
    }

    void collectDevice(Device device) {
        Instant started = Instant.now();
        boolean reachable = false;
        Double latency = null;
        String ip = device.getManagementIp();

        if (ip != null && !ip.isBlank()) {
            try {
                String protocol = device.getManagementProtocol() == null ? "ICMP"
                        : device.getManagementProtocol().trim().toUpperCase();
                reachable = switch (protocol) {
                    case "HTTP", "HTTPS" -> httpCheck(ip, protocol);
                    case "TCP", "SSH", "SNMP", "SNMPV2C", "SNMPV3", "DNS" -> tcpCheck(ip, defaultPort(protocol));
                    default -> InetAddress.getByName(ip.trim()).isReachable(timeoutMs);
                };
                if (reachable) latency = Duration.between(started, Instant.now()).toNanos() / 1_000_000.0;
            } catch (Exception ignored) { }
        }

        Instant now = Instant.now();
        device.setMonitoringStatus(reachable ? "UP" : "DOWN");
        device.setOperationalStatus(reachable ? "ONLINE" : "OFFLINE");
        if (reachable) device.setLastSeenAt(now);
        devices.save(device);

        if (!reachable && device.getOrganizationId() != null && !alerts.existsByDeviceIdAndStatus(device.getId(), "OPEN")) {
            Alert alert = new Alert();
            alert.setTitle("Device unreachable: " + device.getHostname());
            alert.setSeverity("CRITICAL");
            alert.setDeviceId(device.getId());
            alert.setOrganizationId(device.getOrganizationId());
            alert.setMessage("Automated monitoring could not reach the management endpoint. Verify upstream connectivity and device status.");
            alerts.save(alert);
        }

        MetricSnapshot snapshot = new MetricSnapshot();
        snapshot.setDeviceId(device.getId());
        snapshot.setRecordedAt(now);
        snapshot.setLatencyMs(latency);
        snapshot.setPacketLossPercent(reachable ? 0.0 : 100.0);
        snapshot.setCpuPercent(device.getCpuPercent());
        snapshot.setMemoryPercent(device.getMemoryPercent());
        snapshot.setStatus(device.getOperationalStatus());
        metrics.save(snapshot);
    }

    private boolean tcpCheck(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host.trim(), port), timeoutMs);
            return true;
        } catch (Exception e) { return false; }
    }

    private boolean httpCheck(String host, String protocol) {
        try {
            URL url = URI.create(protocol.toLowerCase() + "://" + host.trim()).toURL();
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setConnectTimeout(timeoutMs);
            c.setReadTimeout(timeoutMs);
            c.setRequestMethod("HEAD");
            c.setInstanceFollowRedirects(false);
            int code = c.getResponseCode();
            c.disconnect();
            return code > 0 && code < 500;
        } catch (Exception e) { return false; }
    }

    private int defaultPort(String protocol) {
        return switch (protocol) {
            case "SSH" -> 22;
            case "SNMP", "SNMPV2C", "SNMPV3" -> 161;
            case "DNS" -> 53;
            default -> 443;
        };
    }
}