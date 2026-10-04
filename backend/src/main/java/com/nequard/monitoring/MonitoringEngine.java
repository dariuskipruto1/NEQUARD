package com.nequard.monitoring;

import com.nequard.network.Device;
import com.nequard.network.DeviceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;

@Service
public class MonitoringEngine {
    private final DeviceRepository devices;
    private final MetricSnapshotRepository metrics;
    private final int timeoutMs;

    public MonitoringEngine(DeviceRepository devices, MetricSnapshotRepository metrics,
                            @Value("${nequard.monitoring.timeout-ms:2000}") int timeoutMs) {
        this.devices = devices;
        this.metrics = metrics;
        this.timeoutMs = timeoutMs;
    }

    @Scheduled(fixedDelayString = "${nequard.monitoring.interval-ms:30000}")
    public void collect() {
        for (Device device : devices.findAll()) {
            collectDevice(device);
        }
    }

    void collectDevice(Device device) {
        Instant started = Instant.now();
        boolean reachable = false;
        Double latency = null;

        if (device.getManagementIp() != null && !device.getManagementIp().isBlank()) {
            try {
                InetAddress address = InetAddress.getByName(device.getManagementIp().trim());
                reachable = address.isReachable(timeoutMs);
                if (reachable) {
                    latency = Duration.between(started, Instant.now()).toNanos() / 1_000_000.0;
                }
            } catch (Exception ignored) {
                // Monitoring must isolate one failed target from the rest of the collection cycle.
            }
        }

        Instant now = Instant.now();
        device.setMonitoringStatus(reachable ? "UP" : "DOWN");
        device.setOperationalStatus(reachable ? "ONLINE" : "OFFLINE");
        if (reachable) {
            device.setLastSeenAt(now);
        }
        devices.save(device);

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
}
