package com.exercise.springboot.filemanager.service;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Service;

@Service
@Slf4j
//@RequiredArgsConstructor
public class FileListenerService implements FileAlterationListener {


  private String filePath = "/Users/djk/Documents/MSS/%s.txt";


  private static final int N_THREADS = 10;
  private final ExecutorService executorService;
  private final ThreadPoolExecutor threadPoolExecutor;

  private final FileWriterService fileWriterService;
  private final FileReadService fileReadService;
  private final Map<String, Integer> startPositionMap;

//  private final ExecutorService executorService; // config필요;

  public FileListenerService(
      FileWriterService fileWriterService,
      FileReadService fileReadService) {
    this.fileWriterService = fileWriterService;
    this.fileReadService = fileReadService;
    this.startPositionMap = fileReadService.jsonToMap();
//    this.executorService = Executors.newFixedThreadPool(10); // config필요;
    this.executorService = Executors.newFixedThreadPool(N_THREADS);
    this.threadPoolExecutor = (ThreadPoolExecutor) executorService;
  }


  @Override
  public void onStart(FileAlterationObserver observer) {

  }

  @Override
  public void onDirectoryCreate(File directory) {

  }

  @Override
  public void onDirectoryChange(File directory) {

  }

  @Override
  public void onDirectoryDelete(File directory) {

  }

  @Override
  public void onFileCreate(File file) {
//    log.info(":::::File Create::::: {}", file.getAbsolutePath());
    Runnable readFileTask = fileReadService.getReadFileTask(
        file.getAbsolutePath(), getStartPosition());
    executorService.submit(readFileTask);


  }

  @Override
  public void onFileChange(File file) {
//    log.info(":::::File Change::::: {}", file.getAbsolutePath());
    Runnable readFileTask = fileReadService.getReadFileTask(
        file.getAbsolutePath(), getStartPosition());
    executorService.submit(readFileTask);
  }

  @Override
  public void onFileDelete(File file) {

  }

  @Override
  public void onStop(FileAlterationObserver observer) {

  }

  public Map<String, Integer> getStartPosition() {

    return fileReadService.jsonToMap();
//    if (startPositionMap.size() == 0) {
//      int numThreads = 10;
//      for (int i = 1; i < numThreads + 1; i++) {
//        startPositionMap.put(threadName, 0l);
//      }
//    }
  }


}
