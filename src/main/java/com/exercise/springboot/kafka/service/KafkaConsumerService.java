package com.exercise.springboot.kafka.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Service
public class KafkaConsumerService {

  private final List<FluxSink<String>> sinks = new ArrayList<>();

  public Flux<String> getMessages() {
    return Flux.create(sinks::add);
  }

//  @KafkaListener(topics = "hw", groupId = "group_id")
//  public void listen(String message) {
//    System.out.println("Received message: " + message);
//    sinks.forEach(sink -> sink.next(message));
//  }

  @KafkaListener(topics = "hw", groupId = "group_id")
  public void consume(ConsumerRecord<String, String> record) {
//    System.out.println("Received message: " + record.value());
    sinks.forEach(sink -> sink.next(record.value()));
  }
}
