package com.exercise.springboot.controller;

import com.exercise.springboot.display.service.DisplayService;
import com.exercise.springboot.kafka.service.KafkaConsumerService;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.Display;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class MSSController {

  private final KafkaConsumerService kafkaConsumerService;
  private final DisplayService displayService;


  @GetMapping(value = "/sse/{type}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Map<String, Object>> showHwInfo(@PathVariable int type) {
    return kafkaConsumerService.getMessages().map(c->displayService.parsedMessage(c))
        .filter(map -> {
          // map의 키 집합(key set)을 가져옵니다.
          Set<String> keys = map.keySet();

          // 키 집합에서 필요한 키를 가져오거나 조작할 수 있습니다.
          // 여기서는 간단히 첫 번째 키를 가져와서 사용합니다.
          String firstKey = keys.iterator().next();

          // 첫 번째 키를 반환하거나 다른 방식으로 키를 사용할 수 있습니다.
          return type == Integer.parseInt(firstKey); // 예를 들어 "type" 키와 비교하여
          // 필터링합니다.
        });
//        .filter(map -> type == Integer.parseInt(map.get(String.valueOf(type)).toString()));
//        .filter(map ->{
//          System.out.println("+=+++++++++++++++++++++++++++++++"+map.get("1"));
//          return true;
//        } );


  }

}
