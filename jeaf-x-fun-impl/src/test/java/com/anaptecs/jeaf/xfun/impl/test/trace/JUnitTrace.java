/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.trace;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.trace.Trace;

/**
 * Trace implementation for test purposes. The class is realized as singleton.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public class JUnitTrace {
  /**
   * Reference to only instance of this class.
   */
  private static final Trace instance =
      XFun.getTraceProvider().getTrace((new ComponentID("Core JUnits", "com.anaptecs.jeaf.junit.core")));

  /**
   * Method returns only instance of this class.
   * 
   * @return JUnitTrace Only instance of this class. The method never returns null.
   */
  public static Trace getInstance( ) {
    return instance;
  }
}