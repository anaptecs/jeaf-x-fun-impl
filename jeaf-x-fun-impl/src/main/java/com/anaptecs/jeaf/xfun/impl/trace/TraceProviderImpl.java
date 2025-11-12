/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.trace;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.trace.ContextStackElement;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceConfiguration;
import com.anaptecs.jeaf.xfun.api.trace.TraceProvider;

public class TraceProviderImpl implements TraceProvider {
  public static final String LOGGER_PREFIX = "com.anaptecs.jeaf.xfun.trace:type=Logger, name=";

  /**
   * Map contains all existing trace instances.
   */
  private final Map<String, TraceImpl> traces = new HashMap<>();

  /**
   * Map contains all trace objects for the matching components.
   */
  private final Map<ComponentID, Trace> tracesByComponent = new HashMap<>();

  /**
   * Reference to trace instance for JEAF.
   */
  private final Trace defaultTrace;

  /**
   * Reference to tracing configuration.
   */
  private final TraceConfiguration traceConfiguration;

  /**
   * Initialize object.
   */
  public TraceProviderImpl( ) {
    this(TraceConfiguration.getInstance());
  }

  /**
   * Initialize object.
   */
  public TraceProviderImpl( TraceConfiguration pTraceConfiguration ) {
    traceConfiguration = pTraceConfiguration;
    String lDefaultLoggerName = traceConfiguration.getDefaultLoggerName();
    defaultTrace = new TraceImpl(lDefaultLoggerName);

    // Register trace instance as MBean.
    if (traceConfiguration.exposeLoggersViaJMX()) {
      try {
        MBeanServer lMBeanServer = ManagementFactory.getPlatformMBeanServer();
        lMBeanServer.registerMBean(new TraceManagement((TraceImpl) defaultTrace),
            new ObjectName(LOGGER_PREFIX + lDefaultLoggerName));
      }
      catch (JMException e) {
        defaultTrace.writeEmergencyTrace(e.getMessage(), e);
      }
    }
  }

  @Override
  public Trace getTrace( String pLoggerName ) {
    // Check parameter
    Check.checkInvalidParameterNull(pLoggerName, "pLoggerName");

    // Try to uses existing trace object.
    TraceImpl lTrace = traces.get(pLoggerName);

    // Trace object does not exist yet.
    if (lTrace == null) {
      // Create new trace instance
      lTrace = new TraceImpl(pLoggerName);
      traces.put(pLoggerName, lTrace);

      // If enabled then trace instances are manageable via JMX
      if (traceConfiguration.exposeLoggersViaJMX()) {
        try {
          MBeanServer lMBeanServer = ManagementFactory.getPlatformMBeanServer();
          lMBeanServer.registerMBean(new TraceManagement(lTrace), new ObjectName(LOGGER_PREFIX + pLoggerName));
        }
        catch (JMException e) {
          XFun.getTrace().error(e.getMessage(), e);
        }
      }
    }
    return lTrace;
  }

  @Override
  public Trace getTrace( Class<?> pClass ) {
    // Check parameter
    Check.checkInvalidParameterNull(pClass, "pClass");

    String lPackageName = pClass.getPackage().getName();
    return this.getTrace(lPackageName);
  }

  @Override
  public Trace getTrace( ComponentID pComponentID ) {
    // Check parameter
    Check.checkInvalidParameterNull(pComponentID, "pComponentID");

    // Try to uses existing trace object.
    Trace lTrace = tracesByComponent.get(pComponentID);

    // No trace object exists yet.
    if (lTrace == null) {
      String lLoggerName = this.getLoggerName(pComponentID, traceConfiguration);
      lTrace = this.getTrace(lLoggerName);
      tracesByComponent.put(pComponentID, lTrace);
    }

    // Return trace
    return lTrace;
  }

  @Override
  public Trace getCurrentTrace( ) {
    ContextStackElement lCurrentContextStackElement = TraceImpl.getCurrentContextStackElement();
    Trace lTrace;
    if (lCurrentContextStackElement != null) {
      ComponentID lComponentID = lCurrentContextStackElement.getComponentID();
      lTrace = this.getTrace(lComponentID);
    }
    // There is no current context thus we use default trace. Better then nothing.
    else {
      lTrace = defaultTrace;
    }
    return lTrace;
  }

  /**
   * Method returns default trace object.
   * 
   * @return {@link Trace} Default trace instance. The method never returns null.
   */
  public Trace getDefaultTrace( ) {
    return defaultTrace;
  }

  /**
   * Method transforms the passed component id into a logger name.
   * 
   * @param pComponentID ComponentID for which a logger name should be created. The parameter must not be null.
   * @return {@link String} Name for a logger for the passed component.
   */
  public String getLoggerName( ComponentID pComponentID, TraceConfiguration pTraceConfiguration ) {
    // Application-ID is only added to the name of the logger in case when it is enabled.
    StringBuilder lBuffer = new StringBuilder();
    if (pTraceConfiguration.useApplicationIDAsPrefix() == true) {
      String lApplicationID = XFun.getInfoProvider().getApplicationInfo().getApplicationID();
      lBuffer.append(lApplicationID);
      lBuffer.append(".");
    }
    lBuffer.append(pComponentID.getComponentName());
    return lBuffer.toString();
  }
}
