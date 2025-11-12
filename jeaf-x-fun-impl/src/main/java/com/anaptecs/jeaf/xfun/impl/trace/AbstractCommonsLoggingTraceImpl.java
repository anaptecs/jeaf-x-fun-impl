/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.trace;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import com.anaptecs.jeaf.xfun.api.errorhandling.ExceptionInfoProvider;
import com.anaptecs.jeaf.xfun.api.trace.AbstractTraceImpl;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.bootstrap.Assert;
import com.anaptecs.jeaf.xfun.bootstrap.Check;

/**
 * Class provides tracing in combination with Apache Commons Logging and can be used as base class for trace
 * implementations.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
abstract class AbstractCommonsLoggingTraceImpl extends AbstractTraceImpl {
  /**
   * List of trace levels as they need to be order to find out the current level.
   */
  private static final TraceLevel[] TRACE_LEVELS = new TraceLevel[] { TraceLevel.FATAL, TraceLevel.ERROR,
    TraceLevel.WARN, TraceLevel.INFO, TraceLevel.DEBUG, TraceLevel.TRACE };

  /**
   * Array contains all Log4J2 configuration files in the order as it is checked by Log4J2 at startup.
   */
  private static final String[] LOG4J2_CONFIG_FILES = { "log4j2-test.properties", "log4j2-test.yaml", "log4j2-test.yml",
    "log4j2-test.json", "log4j2-test.jsn", "log4j2-test.xml", "log4j2.properties", "log4j2.yaml", "log4j2.yml",
    "log4j2.json", "log4j2.jsn", "log4j2.xml" };

  /**
   * During class loading we try to fix Log4J configuration if necessary.
   */
  static {
    AbstractCommonsLoggingTraceImpl.checkAndFixLog4JConfiguration();
  }

  /**
   * Name of the logger
   */
  private final String name;

  /**
   * Reference to the used Log implementation. The reference is never null since it is set in the class' constructor.
   */
  private final Log logger;

  /**
   * Static field says if Log4J configuration was already checked and may be fixed.
   */
  private static boolean log4JConfigChecked = false;

  /**
   * Initialize a new Trace-Object for the passed component.
   * 
   * @param pLoggerName Name of the logger for which the new Trace instance will be created. The parameter must not be
   * null.
   */
  AbstractCommonsLoggingTraceImpl( String pLoggerName ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pLoggerName, "pLoggerName");

    // In order to avoid problems with Log4J due to missing Log4J configuration we try to fix it if needed.
    AbstractCommonsLoggingTraceImpl.checkAndFixLog4JConfiguration();

    // Get logger implementation.
    name = pLoggerName;
    logger = LogFactory.getLog(name);
  }

  /**
   * Method checks and fixes the current Log4J configuration if required.
   */
  static void checkAndFixLog4JConfiguration( ) {
    if (log4JConfigChecked == false) {
      // Check Log4J configuration
      boolean lConfigurationFixRequired = isLog4JConfigurationFixRequired();

      // Log4J is not set up properly, so we better fix it.
      if (lConfigurationFixRequired == true) {
        System.setProperty("log4j2.configurationFile", "jeaf-log4j2-fallback-configuration.xml");
        Log lLogger = LogFactory.getLog("DEFAULT_TRACE");
        lLogger.warn("Using JEAF fallback Log4J2 configuration as no Log4J2 configuration is available.");
      }
    }
    log4JConfigChecked = true;
  }

  /**
   * Method checks if Log4J configuration needs to be fixed.
   * 
   * @return boolean Method returns true if the Log4J configuration needs to be fixed.
   */
  private static boolean isLog4JConfigurationFixRequired( ) {
    // Check if system property for Log4J2 configuration is set
    String lProperty = System.getProperty("log4j2.configurationFile");

    // No custom configuration file is defined. So let's check if one of the default files is available.
    boolean lFixRequired = true;
    if (lProperty == null) {
      ClassLoader lClassLoader = AbstractCommonsLoggingTraceImpl.class.getClassLoader();
      for (String lNextFileName : LOG4J2_CONFIG_FILES) {
        // First we try to load the resource using the class loader of this class.
        URL lResource = lClassLoader.getResource(lNextFileName);

        // Second we try our best using system class loader.
        if (lResource == null) {
          lResource = ClassLoader.getSystemResource(lNextFileName);
        }
        if (lResource != null) {
          lFixRequired = false;
          break;
        }
      }
    }
    // Custom configuration is provided.
    else {
      lFixRequired = false;
    }
    return lFixRequired;
  }

  @Override
  public void log( TraceLevel pTraceLevel, String pMessage, Throwable pThrowable ) {
    if (pTraceLevel == null) {
      pTraceLevel = TraceLevel.ERROR;
    }

    this.internalLog(logger, pTraceLevel, pMessage, pThrowable);
  }

  /**
   * Method writes an so called emergency trace. Emergency traces are needed in case that we run into fatal problems
   * during initialization. Thus this method should be implemented in a way that it requires no environment be be set
   * up.
   * 
   * @param pMessage Message that should be written. The parameter may be null.
   * @param pThrowable Exception that occurred. The parameter may be null.
   * @param pTraceLevel Level with which the message should be traced. The parameter may be null. In this case
   * {@link TraceLevel#FATAL} will be used.
   */
  @Override
  public void writeEmergencyTrace( String pMessage, Throwable pThrowable, TraceLevel pTraceLevel ) {
    Log lLogger = LogFactory.getLog("JEAF_EMERGENCY");

    if (pTraceLevel == null) {
      pTraceLevel = TraceLevel.FATAL;
    }

    this.internalLog(lLogger, pTraceLevel, pMessage, pThrowable);

  }

  private void internalLog( Log pLogger, TraceLevel pTraceLevel, String pMessage, Throwable pThrowable ) {
    String lTechnicalDetails;
    if (pThrowable instanceof ExceptionInfoProvider) {
      lTechnicalDetails = ((ExceptionInfoProvider) pThrowable).getTechnicalDetails();
    }
    else {
      lTechnicalDetails = null;
    }

    if (lTechnicalDetails == null) {
      switch (pTraceLevel) {
        case TRACE:
          pLogger.trace(pMessage, pThrowable);
          break;
        case DEBUG:
          pLogger.debug(pMessage, pThrowable);
          break;
        case INFO:
          pLogger.info(pMessage, pThrowable);
          break;
        case WARN:
          pLogger.warn(pMessage, pThrowable);
          break;
        case ERROR:
          pLogger.error(pMessage, pThrowable);
          break;
        // FATAL is also our default case.
        default:
          pLogger.fatal(pMessage, pThrowable);
      }
    }
    else {
      switch (pTraceLevel) {
        case TRACE:
          pLogger.trace(pMessage);
          pLogger.trace(lTechnicalDetails, pThrowable);
          break;
        case DEBUG:
          pLogger.debug(pMessage);
          pLogger.debug(lTechnicalDetails, pThrowable);
          break;
        case INFO:
          pLogger.info(pMessage);
          pLogger.info(lTechnicalDetails, pThrowable);
          break;
        case WARN:
          pLogger.warn(pMessage);
          pLogger.warn(lTechnicalDetails, pThrowable);
          break;
        case ERROR:
          pLogger.error(pMessage);
          pLogger.error(lTechnicalDetails, pThrowable);
          break;
        // FATAL is also our default case.
        default:
          pLogger.fatal(pMessage);
          pLogger.fatal(lTechnicalDetails, pThrowable);
      }
    }
  }

  /**
   * Method check if the passed trace level is enabled on this trace object.
   * 
   * @param pTraceLevel Trace level that should be checked. The parameter must not be null.
   * @return boolean The method returns true if the passed trace level is enabled on this trace object and false in all
   * other cases.
   */
  @Override
  public boolean isLevelEnabled( TraceLevel pTraceLevel ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pTraceLevel, "pTraceLevel");
    boolean lTraceEnabled = false;

    switch (pTraceLevel) {
      case TRACE:
        lTraceEnabled = logger.isTraceEnabled();
        break;

      case DEBUG:
        lTraceEnabled = logger.isDebugEnabled();
        break;

      case INFO:
        lTraceEnabled = logger.isInfoEnabled();
        break;

      case WARN:
        lTraceEnabled = logger.isWarnEnabled();
        break;

      case ERROR:
        lTraceEnabled = logger.isErrorEnabled();
        break;

      case FATAL:
        lTraceEnabled = logger.isFatalEnabled();
        break;

      default:
        Assert.unexpectedEnumLiteral(pTraceLevel);
        break;
    }
    return lTraceEnabled;
  }

  /**
   * Method returns the trace level that is currently enabled.
   * 
   * @return {@link TraceLevel} Trace level that is currently enabled. If tracing is completely disabled then this
   * method returns null.
   */
  @Override
  public TraceLevel getLevel( ) {
    // Test all trace levels until we find one that is enabled.
    TraceLevel lCurrentLevel = null;
    for (TraceLevel lNext : TRACE_LEVELS) {
      if (this.isLevelEnabled(lNext) == true) {
        lCurrentLevel = lNext;
      }
    }

    // Method returns null if tracing is disabled completely.
    return lCurrentLevel;
  }

  /**
   * Method sets the trace level of this instance to the passed level.
   * 
   * @param pLevel Trace level for this instance. The parameter must not be null.
   */
  private void setLevel( TraceLevel pLevel ) {
    Configurator.setLevel(name, this.toLevel(pLevel));
  }

  /**
   * Method returns the name of the trace instance.
   * 
   * @return String Name of the trace instance. The method never returns null.
   */
  public String getName( ) {
    return name;
  }

  /**
   * Method returns the name of the current trace level.
   * 
   * @return {@link String} Name of the current trace level. The method never returns null and the returned names match
   * with the enumeration literals of {@link TraceLevel}
   */
  public String getTraceLevel( ) {
    return this.getLevel().name();
  }

  /**
   * Methods sets the trace level of the represented trace instance.
   * 
   * @param pTraceLevel Name of the level that should be set on the trace instance. The parameter must not be null and
   * must match with the names of the literals of enumeration {@link TraceLevel}.
   */
  public void setTraceLevel( String pTraceLevel ) {
    // Check parameter
    Check.checkInvalidParameterNull(pTraceLevel, "pTraceLevel");

    TraceLevel lLevel = TraceLevel.valueOf(pTraceLevel);
    this.setLevel(lLevel);
  }

  /**
   * Method resets the current trace level settings for this trace instance. This leads to the situation the current
   * definitions from higher levels will be used for this instance as well.
   */
  public void resetTraceLevel( ) {
    Configurator.setLevel(name, (Level) null);
  }

  /**
   * Method converts the passed trace level into the matching representation in Apache Commons Logging style.
   * 
   * @param pTraceLevel Trace level that should be converted into a {@link Level}. The parameter may be null. In this
   * case {@link TraceLevel#FATAL} will be used.
   * @return {@link Level} Trace level representation of Apache Commons Logging. The method never returns null.
   */
  public Level toLevel( TraceLevel pTraceLevel ) {
    Level lLevel;
    if (pTraceLevel == null) {
      pTraceLevel = TraceLevel.FATAL;
    }

    // Convert X-Fun TraceLevel into Apache Commons Logging representation.
    switch (pTraceLevel) {
      case TRACE:
        lLevel = Level.TRACE;
        break;

      case DEBUG:
        lLevel = Level.DEBUG;
        break;

      case INFO:
        lLevel = Level.INFO;
        break;

      case WARN:
        lLevel = Level.WARN;
        break;

      case ERROR:
        lLevel = Level.ERROR;
        break;

      case FATAL:
        lLevel = Level.FATAL;
        break;

      default:
        com.anaptecs.jeaf.xfun.api.checks.Assert.unexpectedEnumLiteral(pTraceLevel);
        lLevel = null;
    }
    return lLevel;
  }

}
