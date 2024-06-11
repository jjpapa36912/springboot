package com.exercise.springboot.hwinformation.service;

import java.util.List;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;

public class HardwareAbstractionLayerImpl implements HardwareAbstractionLayer {

  @Override
  public ComputerSystem getComputerSystem() {
    return null;
  }

  @Override
  public CentralProcessor getProcessor() {
    return null;
  }

  @Override
  public GlobalMemory getMemory() {
    return null;
  }

  @Override
  public List<PowerSource> getPowerSources() {
    return null;
  }

  @Override
  public List<HWDiskStore> getDiskStores() {
    return null;
  }

  @Override
  public List<NetworkIF> getNetworkIFs() {
    return null;
  }

  @Override
  public List<NetworkIF> getNetworkIFs(boolean includeLocalInterfaces) {
    return null;
  }

  @Override
  public List<Display> getDisplays() {
    return null;
  }

  @Override
  public Sensors getSensors() {
    return null;
  }

  @Override
  public List<UsbDevice> getUsbDevices(boolean tree) {
    return null;
  }

  @Override
  public List<SoundCard> getSoundCards() {
    return null;
  }

  @Override
  public List<GraphicsCard> getGraphicsCards() {
    return null;
  }
}
