package com.exercise.springboot.hwinformation.service;

import com.exercise.springboot.filemanager.service.FileWriterService;
import com.exercise.springboot.hwinformation.domain.HwInformation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("HwInformationService")
@Slf4j
@RequiredArgsConstructor
public class HwInformationService {

  private final HwInformationCreatorService hwInformationCreatorService;
  private final FileWriterService fileWriterService;


  public void startProducingHwInfo() {
    ScheduledExecutorService scheduledExecutorService =
        Executors.newSingleThreadScheduledExecutor();
//    ScheduledFuture<?> scheduledFuture =
    scheduledExecutorService.scheduleAtFixedRate(() -> {
      try {
        HwInformation hwInformation = hwInformationCreatorService.collectHwInfo();
        fileWriterService.startWrite(hwInformation);
      } catch (Exception e) {
        log.info("HwInformationService - createStart(), {}", e);
      }
    }, 1000, 1, TimeUnit.MILLISECONDS);
  }



  public Map<String, HwInformation> ggeet() {
    return new ConcurrentHashMap<>();
  }

}
