package com.exercise.springboot.display.service;

import com.exercise.springboot.kafka.service.KafkaConsumerService;
import java.time.chrono.MinguoDate;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisplayService {

  private final KafkaConsumerService kafkaConsumerService;

  public Map<String, Object> parsedMessage(String message) {
    Map<String, Object> map = new HashMap<>();
    System.out.println("========"+message);
    String[] parts = message.split(",");
    String[] threadName = parts[1].trim().split(":");
    int index = threadName[1].indexOf("t");
    String[] threadNum = threadName[1].substring(index).split("-");
    String type = threadNum[1];
    System.out.println("%%%%%%%%%%%type : "+ type);
    map.put(type,  message);
//    String[] parts = message.replace("{", "").replace("}", "").replace("\"", "").split(", ");
//    for (String part : parts) {
//      String[] keyValue = part.split(": ");
//      map.put(keyValue[0], keyValue[1]);
//    }
    return map;
  }

}
