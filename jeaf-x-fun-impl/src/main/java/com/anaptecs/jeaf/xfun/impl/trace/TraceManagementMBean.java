/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.trace;

import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

/**
 * Interface defines possibilities that can be used to control JEAF X-Fun tracing via JMX.
 * 
 * @author JEAF Development Team
 */
public interface TraceManagementMBean {
  /**
   * Method returns the name of the trace instance.
   * 
   * @return String Name of the trace instance. The method never returns null.
   */
  String getName( );

  /**
   * Method returns the name of the current trace level.
   * 
   * @return {@link String} Name of the current trace level. The method never returns null and the returned names match
   * with the enumeration literals of {@link TraceLevel}
   */
  String getTraceLevel( );

  /**
   * Methods sets the trace level of the represented trace instance.
   * 
   * @param pTraceLevel Name of the level that should be set on the trace instance. The parameter must not be null and
   * must match with the names of the literals of enumeration {@link TraceLevel}.
   */
  void setTraceLevel( String pTraceLevel );

  /**
   * Method resets the current trace level settings for this trace instance. This leads to the situation the current
   * definitions from higher levels will be used for this instance as well.
   */
  void resetTraceLevel( );
}
