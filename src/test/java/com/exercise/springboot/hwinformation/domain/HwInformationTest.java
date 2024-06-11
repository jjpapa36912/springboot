package com.exercise.springboot.hwinformation.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.exercise.springboot.hwinformation.service.HwInformationService;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Test;

class HwInformationTest {

  @Test
  void gget() {

    HwInformationService mock = mock(HwInformationService.class);
    when(mock.ggeet()).thenReturn(new ConcurrentHashMap<>());

  }


}