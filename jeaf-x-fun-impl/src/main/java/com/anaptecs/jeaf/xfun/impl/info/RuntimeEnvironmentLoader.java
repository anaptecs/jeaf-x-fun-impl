/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.info;

import com.anaptecs.jeaf.xfun.annotations.RuntimeInfo;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.info.RuntimeEnvironment;

public class RuntimeEnvironmentLoader {
  public RuntimeEnvironment loadRuntimeEnvironment( Class<?> pRuntimeInfoClass ) {
    // Runtime infos are available
    RuntimeEnvironment lRuntimeEnvironment;
    if (pRuntimeInfoClass != null) {
      RuntimeInfo lAnnotation = pRuntimeInfoClass.getAnnotation(RuntimeInfo.class);
      if (lAnnotation != null) {
        lRuntimeEnvironment = lAnnotation.runtimeEnvironment();
      }
      else {
        lRuntimeEnvironment = RuntimeEnvironment.UNKNOWN;
        XFun.getTrace().error("Unable to resolve runtime information. Class " + pRuntimeInfoClass.getName()
            + "is not properly configured. Please make use annotation @" + RuntimeInfo.class.getSimpleName());
      }
    }
    else {
      lRuntimeEnvironment = RuntimeEnvironment.UNKNOWN;
      XFun.getTrace().error(
          "Runtime environment is not configured. Please make use annotation @" + RuntimeInfo.class.getSimpleName());
    }
    return lRuntimeEnvironment;
  }
}
