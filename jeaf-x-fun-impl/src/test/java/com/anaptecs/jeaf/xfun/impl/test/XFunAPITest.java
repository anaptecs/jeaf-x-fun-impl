/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.anaptecs.jeaf.tools.api.Tools;
import com.anaptecs.jeaf.tools.api.performance.Stopwatch;
import com.anaptecs.jeaf.tools.api.performance.TimePrecision;
import com.anaptecs.jeaf.tools.api.validation.ValidationTools;
import com.anaptecs.jeaf.tools.impl.performance.StopwatchImpl;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.impl.trace.TraceImpl;
import org.junit.jupiter.api.Test;

public class XFunAPITest {
  /**
   * Method tests the X-Fun entry point for accessing providers.
   */
  @Test
  public void testXFunEntryPoint( ) {
    Stopwatch lStopwatch = new StopwatchImpl("Stopwatch for X-Fun initialization", TimePrecision.MILLIS);
    lStopwatch.start();
    Trace lCurrentTrace = XFun.getTrace();
    lStopwatch.stopAndTrace(1);
    assertNotNull(lCurrentTrace, "Trace not available");
    assertEquals(TraceImpl.class, lCurrentTrace.getClass());

    lCurrentTrace.info("Hello world");
  }

  /**
   * Method tests the X-Fun entry point for accessing providers.
   */
  @Test
  public void testApplicationInfo( ) {
    ApplicationInfo lApplicationInfo = XFun.getInfoProvider().getApplicationInfo();
    assertNotNull(lApplicationInfo);
  }

  /**
   * Method tests the X-Fun entry point for accessing providers.
   */
  @Test
  public void testToolsCustomization( ) {
    ValidationTools lValidationTools = Tools.getValidationTools();
    assertEquals(FakeValidationTools.class, lValidationTools.getClass(), "Wrong validation tools implementation");
  }
}
