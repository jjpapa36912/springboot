package com.exercise.springboot.filemanager.service;

import com.exercise.springboot.hwinformation.domain.HwInformation;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileWriterService {

  private final ObjectMapper objectMapper;
  private static final ReentrantReadWriteLock.WriteLock writeLock =
      new ReentrantReadWriteLock().writeLock();


  @Getter
  private final Map<String, Integer> startPositionMap =
      new ConcurrentHashMap<>();

  private static final int N_THREADS = 10;
  private final ExecutorService executorService;
  private final ThreadPoolExecutor threadPoolExecutor;

  public FileWriterService(
      ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.executorService = Executors.newFixedThreadPool(N_THREADS);
    this.threadPoolExecutor = (ThreadPoolExecutor) executorService;
  }

  public void startWrite(HwInformation hwInformation)
      throws InterruptedException {

    for (int i = 0; i < N_THREADS; i++) {
      Runnable runnable = () -> {
        int poolSize = threadPoolExecutor.getPoolSize();
        String threadName = Thread.currentThread().getName();
//        log.info("ThreadName : {}, PoolSize : {}, Message : {}", threadName,
//            poolSize, hwInformation.toString());
        String format = String.format(
            "ThreadName : %s, PoolSize : %s, Message : %s",
            threadName,
            poolSize, hwInformation);
        write(format, threadName);
      };
      executorService.execute(runnable);
//      Thread.sleep(100);
    }
//    executorService.shutdown();
  }

  public void shutdown() {
    executorService.shutdown();
  }

  public void write(String message, String threadName) {
    try {
      int index = threadName.indexOf("t");
      threadName = threadName.substring(index);

      setStartPosition(threadName);

      File file = new File("/Users/djk/Documents/MSS/" + threadName + ".txt");
      // 2. 파일 존재여부 체크 및 생성
      if (!file.exists()) {
        file.createNewFile();
      }

//      // 파일에 데이터를 쓰기 위해 BufferedWriter를 생성
//      BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//
//      // 파일에 쓸 내용
//      String content = "새로운 내용";
//
//      // 파일에 내용 쓰기
//      writer.write(message);
//
//      // 버퍼 비우고 닫기
//      writer.flush();
//      writer.close();

      try {
        // 파일을 쓰는 코드
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(message);
        writer.newLine();
        writer.flush();
        writer.close();
//          System.out.println(Thread.currentThread().getName() + " wrote to file.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  private void setStartPosition(String threadName) {
    if (!startPositionMap.containsKey(threadName)) {
      startPositionMap.put(threadName, 0);
    }
  }

  public void mapToJsonFile(
      Map<String, Integer> startPositionMap) {
//    ObjectMapper objectMapper = new ObjectMapper(); // config bean 등록 필요

    Map<String, Integer> mapCopy = new ConcurrentHashMap<>(startPositionMap);
    writeLock.lock();
    synchronized (this) {
      try {
        // 파일을 쓰기 모드로 열어 기존 내용을 지웁니다.
        FileWriter fileWriter = new FileWriter(
            "/Users/djk/Documents/position.txt", false);
        // Map을 JSON 파일로 직렬화
        objectMapper.writeValue(fileWriter, startPositionMap);
//      objectMapper.writeValue(new File("/Users/djk/Documents/position.txt"),
//          startPositionMap);
//        System.out.println("Map has been written to position.json");
      } catch (IOException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        writeLock.unlock();

      }
    }
  }


}
