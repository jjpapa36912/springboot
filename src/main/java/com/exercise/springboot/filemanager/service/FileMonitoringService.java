package com.exercise.springboot.filemanager.service;

import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileMonitoringService {

  private final FileListenerService fileListenerService;

  public void startMonitoringFile() {
    // 디렉토리 경로 지정
    String directoryPath = "/Users/djk/Documents/MSS";
    try {
      // FileAlterationObserver를 사용하여 디렉토리를 감시
      FileAlterationObserver observer = new FileAlterationObserver(
          new File(directoryPath));
      observer.addListener(fileListenerService);

      // FileAlterationMonitor를 사용하여 모니터링 시작 (주기: 5000ms = 5초)
      FileAlterationMonitor monitor = new FileAlterationMonitor(2000, observer);
      monitor.start();
//    // 모니터링을 중지하려면 아래 코드를 호출
//    // monitor.stop();
    } catch (Exception e) {
      log.info(":::::Failed to Monitoring:::::");
    }


  }

}
