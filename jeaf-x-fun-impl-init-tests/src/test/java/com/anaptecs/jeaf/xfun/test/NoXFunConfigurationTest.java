/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.impl.trace.TraceImpl;
import org.junit.jupiter.api.Test;

public class NoXFunConfigurationTest {
  @Test
  public void useXFunWithoutConfig( ) {
    Trace lTrace = XFun.getTrace();
    assertNotNull(lTrace);
    assertEquals(TraceImpl.class, lTrace.getClass());
    // Test tracing of startup info.
    TestHandler lHandler = new TestHandler();
    LogManager lLogManager = LogManager.getLogManager();
    lLogManager.reset();
    Logger lRootLogger = lLogManager.getLogger("");
    lRootLogger.setLevel(Level.ALL);
    lRootLogger.addHandler(lHandler);

    Check.checkInvalidParameterNull("", "pParam");
    assertNull(lHandler.getLastLogRecord());
    lTrace.info("Hello X-Fun");
  }
}
