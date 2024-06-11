package com.exercise.springboot.hwinformation.service;

import com.exercise.springboot.hwinformation.domain.HwInformation;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

@Service
public class HwInformationCreatorService {

  public HwInformation collectHwInfo() {
    SystemInfo systemInfo = new SystemInfo();
    HardwareAbstractionLayer hardware = systemInfo.getHardware();
    long stamp = System.currentTimeMillis();
    GlobalMemory memory = hardware.getMemory();
    long usingRam = (memory.getTotal() - memory.getAvailable()) / 1024 / 1024;
    long ramLoadTime = System.currentTimeMillis() - stamp;
    long ramPercent =
        (long) ((double) usingRam / (memory.getTotal() / 1024 / 1024) * 100);

    return new HwInformation(ramPercent, memory.getTotal(), usingRam,
        memory.getAvailable());
  }
}
