package com.exercise.springboot.hwinformation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
public class HwInformation {

  @Id
  private long ramPercent; // %
  private long totalRam; // MB
  private long usingRam; // MB
  private long availableRam; // MB


  public HwInformation() {

  }
}
