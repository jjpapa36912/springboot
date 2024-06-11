package com.exercise.springboot;

import com.exercise.springboot.filemanager.service.FileListenerService;
import com.exercise.springboot.filemanager.service.FileMonitoringService;
import com.exercise.springboot.hwinformation.service.HwInformationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootApplication implements CommandLineRunner {

  private final HwInformationService hwInformationService;
  private final FileMonitoringService fileMonitoringService;
  private final FileListenerService fileListenerService;

  public SpringbootApplication(
      HwInformationService hwInformationService,
      FileMonitoringService fileMonitoringService,
      FileListenerService fileListenerService) {
    this.hwInformationService = hwInformationService;
    this.fileMonitoringService = fileMonitoringService;
    this.fileListenerService = fileListenerService;
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringbootApplication.class, args);
  }

  @Override
  public void run(String... args) {
    fileMonitoringService.startMonitoringFile();

    hwInformationService.startProducingHwInfo();

  }
}
