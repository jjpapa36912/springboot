package com.exercise.springboot.filemanager.service;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileReadService {

  //  private final ExecutorService executorService;
  private static final String TOPIC = "hw";
  private final FileWriterService fileWriterService;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private static final ReentrantReadWriteLock.ReadLock readLock =
      new ReentrantReadWriteLock().readLock();


  public FileReadService(
      FileWriterService fileWriterService,
      KafkaTemplate<String, String> kafkaTemplate) {
    this.fileWriterService = fileWriterService;
//    this.executorService = Executors.newFixedThreadPool(10); // config필요;
    this.kafkaTemplate = kafkaTemplate;
  }

//  public void submitTask(Runnable task) {
//    log.info(":::::Start task!:::::");
//    executorService.submit(task);
//  }
//
//  public void shutdown() {
//    executorService.shutdown();
//    try {
//      if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
//        executorService.shutdownNow();
//      }
//    } catch (InterruptedException e) {
//      executorService.shutdownNow();
//      Thread.currentThread().interrupt();
//    }
//  }


  public Runnable getReadFileTask(String filePath,
      Map<String, Integer> startPositionMap) {
    return () -> {
//      System.out.println("Task is running.");
      String threadName = getThreadName(filePath);
      RandomAccessFile file = null;
//      int position =
//          Optional.ofNullable(startPositionMap.get(threadName)).orElse(0);
      int position = startPositionMap.get(threadName);
//      log.info(":::::position:::::{}, :::::{}", position, threadName);
      try {
        file = new RandomAccessFile(filePath, "r");
        file.seek(position); // 지정된 위치로 이동

//        log.info(":::::Check process:::::");
        String line = null;
        while ((line = file.readLine()) != null) {
//          System.out.println(line+";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"
//              + ";;;;;;");
          position = Integer.parseInt(
              String.valueOf(file.getFilePointer())); // 현재 파일 포인터 위치 저장
//          log.info(">>>:::::position:::::{}, :::::{}", position, threadName);
          startPositionMap.put(threadName, position);
          fileWriterService.mapToJsonFile(startPositionMap);// 마지막 읽은 위치 저장

          kafkaTemplate.send(TOPIC, "position : " + file.getFilePointer() + ", " + line);
//          log.info(":::::Send data to Kafka Server:::::");

          // 임의의 조건으로 읽기 중단 (예: 특정 조건이 있는 경우)
          if (line.contains("specific condition")) {
            break;
          }
        } // 작업 수행 (예: 3초 대기)
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (file != null) {
          try {
            file.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    };
  }

  public String getThreadName(String filePath) {
    int startIndex = filePath.lastIndexOf("/");
    int endIndex = filePath.lastIndexOf(".");
    return filePath.substring(startIndex + 1, endIndex);
  }

  public Map<String, Integer> jsonToMap() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);

    Map<String, Integer> map = null;
    readLock.lock();
    synchronized (this) {
      try {
        if (new File("/Users/djk/Documents/position.txt").isFile() & new File(
            "/Users/djk/Documents/position.txt").length() != 0) {
          // JSON 파일을 Map으로 역직렬화
          Thread.sleep(500);
          map = objectMapper.readValue(
              new File("/Users/djk/Documents/position.txt"), Map.class);
        } else {
          map = fileWriterService.getStartPositionMap();
        }

//        System.out.println("Map read from JSON file: " + map);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        readLock.unlock();
      }
    }

    return map;
  }
}
