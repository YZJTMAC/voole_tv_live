package com.vad.sdk.core;

import com.vad.sdk.core.base.interfaces.IAdController;
import com.vad.sdk.core.controller.v30.AdController;

public class VAdControllerFactory {
  public static IAdController getAdController() {
    // if("1".equals(apkId)){
    // return new NoAdController();
    // }
    return new AdController();
  }
}
