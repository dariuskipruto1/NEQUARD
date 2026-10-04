package com.nequard.network;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.net.*;
import java.time.Instant;

@Service
public class ServiceMonitoringEngine {
    private final ServiceMonitorRepository services;
    private final int timeoutMs;
    public ServiceMonitoringEngine(ServiceMonitorRepository services,
                                   org.springframework.beans.factory.annotation.Value("${nequard.monitoring.timeout-ms:2000}") int timeoutMs){
        this.services=services;this.timeoutMs=timeoutMs;
    }
    @Scheduled(fixedDelayString="${nequard.monitoring.interval-ms:30000}")
    public void collect(){services.findAll().forEach(this::check);}
    void check(ServiceMonitor s){
        long start=System.nanoTime(); boolean ok=false;
        try{
            String protocol=s.getProtocol().toUpperCase();
            if((protocol.equals("HTTP")||protocol.equals("HTTPS"))&&s.getEndpoint()!=null&&!s.getEndpoint().isBlank()){
                HttpURLConnection c=(HttpURLConnection)URI.create(s.getEndpoint()).toURL().openConnection();
                c.setConnectTimeout(timeoutMs);c.setReadTimeout(timeoutMs);c.setRequestMethod("HEAD");int code=c.getResponseCode();ok=code>0&&code<500;c.disconnect();
            } else if(s.getPort()!=null){
                String host=s.getEndpoint();
                if(host!=null&&host.contains("://"))host=URI.create(host).getHost();
                if(host==null||host.isBlank())ok=false;else try(Socket socket=new Socket()){socket.connect(new InetSocketAddress(host,s.getPort()),timeoutMs);ok=true;}
            }
        }catch(Exception ignored){}
        s.setStatus(ok?"UP":"DOWN");s.setResponseTimeMs((System.nanoTime()-start)/1_000_000);s.setLastCheckedAt(Instant.now());services.save(s);
    }
}