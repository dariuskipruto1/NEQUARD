package com.nequard.network;
import jakarta.persistence.*;import java.time.*;import java.util.UUID;
@Entity @Table(name="devices") public class Device {
 @Id @GeneratedValue(strategy=GenerationType.UUID) UUID id;
 @Column(nullable=false) String hostname; @Column(name="management_ip") String managementIp;
 @Enumerated(EnumType.STRING) @Column(nullable=false) DeviceType deviceType;
 String vendor,model,monitoringStatus="PENDING",operationalStatus="UNKNOWN"; Double latitude,longitude; Instant lastSeenAt;
 public UUID getId(){return id;} public String getHostname(){return hostname;} public void setHostname(String v){hostname=v;} public String getManagementIp(){return managementIp;} public void setManagementIp(String v){managementIp=v;} public DeviceType getDeviceType(){return deviceType;} public void setDeviceType(DeviceType v){deviceType=v;} public String getVendor(){return vendor;} public void setVendor(String v){vendor=v;} public String getModel(){return model;} public void setModel(String v){model=v;} public String getMonitoringStatus(){return monitoringStatus;} public void setMonitoringStatus(String v){monitoringStatus=v;} public String getOperationalStatus(){return operationalStatus;} public void setOperationalStatus(String v){operationalStatus=v;} public Instant getLastSeenAt(){return lastSeenAt;} public void setLastSeenAt(Instant v){lastSeenAt=v;}
}