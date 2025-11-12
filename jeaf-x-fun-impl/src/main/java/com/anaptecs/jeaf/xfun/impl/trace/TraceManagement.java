/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.trace;

import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

/**
 * Class implements JXM MBean to control levels of JEAF X-Fun trace instances.
 * 
 * Therefore this class wraps the existing trace implementations and adds JMX capabilities.
 * 
 * @author JEAF Development Team
 */
public class TraceManagement implements TraceManagementMBean {
  /**
   * Reference to trace instance that is wrapped by this class.
   */
  private final AbstractCommonsLoggingTraceImpl trace;

  /**
   * Initialize object.
   * 
   * @param pTrace Trace instance tat is made accessibly via JMX by this class. The parameter must not be null.
   */
  public TraceManagement( AbstractCommonsLoggingTraceImpl pTrace ) {
    // Check parameter
    Check.checkInvalidParameterNull(pTrace, "pTrace");

    trace = pTrace;
  }

  /**
   * Method returns the name of the trace instance.
   * 
   * @return String Name of the trace instance. The method never returns null.
   */
  @Override
  public String getName( ) {
    return trace.getName();
  }

  /**
   * Method returns the name of the current trace level.
   * 
   * @return {@link String} Name of the current trace level. The method never returns null and the returned names match
   * with the enumeration literals of {@link TraceLevel}
   */
  @Override
  public String getTraceLevel( ) {
    return trace.getTraceLevel();
  }

  /**
   * Methods sets the trace level of the represented trace instance.
   * 
   * @param pTraceLevel Name of the level that should be set on the trace instance. The parameter must not be null and
   * must match with the names of the literals of enumeration {@link TraceLevel}.
   */
  @Override
  public void setTraceLevel( String pLogLevel ) {
    trace.setTraceLevel(pLogLevel);
  }

  /**
   * Method resets the current trace level settings for this trace instance. This leads to the situation the current
   * definitions from higher levels will be used for this instance as well.
   */
  @Override
  public void resetTraceLevel( ) {
    trace.resetTraceLevel();
  }
}
