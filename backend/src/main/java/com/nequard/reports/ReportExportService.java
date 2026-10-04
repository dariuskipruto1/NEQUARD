package com.nequard.reports;
import org.springframework.stereotype.Service;import java.nio.charset.StandardCharsets;import java.time.Instant;
@Service public class ReportExportService{public byte[] csv(String title,String value){return ("title,value,generated_at\n"+title+","+value.replace(","," ") +","+Instant.now()+"\n").getBytes(StandardCharsets.UTF_8);}}