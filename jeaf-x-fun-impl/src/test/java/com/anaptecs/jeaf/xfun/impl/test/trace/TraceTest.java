/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.anaptecs.jeaf.junit.JUnitMessages;
import com.anaptecs.jeaf.xfun.annotations.TraceConfig;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.errorhandling.ApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.api.trace.ContextStackElement;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceConfiguration;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.api.trace.TraceProvider;
import com.anaptecs.jeaf.xfun.impl.trace.DefaultTrace;
import com.anaptecs.jeaf.xfun.impl.trace.TraceImpl;
import com.anaptecs.jeaf.xfun.impl.trace.TraceManagement;
import com.anaptecs.jeaf.xfun.impl.trace.TraceProviderImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.Test;

/**
 * Method test the tracing facilities of JEAF.
 *
 * @author JEAF Development Team
 * @version 1.0
 */
@TraceConfig(defaultLoggerName = "JEAF_XFUN_IMPL_TEST", useApplicationIDAsPrefix = true)
public class TraceTest {

  /**
   * Method tests the creation of a new Tracing implementation based on JEAF tracing facilities.
   */
  @SuppressWarnings("unused")
  @Test
  public void testSimpleTraces( ) {
    Trace lTrace = JUnitTrace.getInstance();
    assertNotNull(lTrace, "Method 'JUnitTrace.getInstance()' must not return null.");
    TestAppender lAppender = TestAppender.getInstance();
    assertNotNull(lAppender, "TestAppender not configured.");

    // Trace message on level "FATAL"
    String lPrefix;
    if (TraceConfig.SHOW_CURRENT_USER_IN_TRACES == true) {
      String lUserName = XFun.getPrincipalProvider().getCurrentPrincipal().getName();
      lPrefix = lUserName;
    }
    else {
      lPrefix = "";
    }
    String lMessage = "Hello Trace!";

    lTrace.fatal(lMessage);
    String lLastMessage = lAppender.getLastMessage().toString();
    assertTrue(lLastMessage.startsWith(lPrefix), "Wrong prefix. Expected: " + lPrefix + " Actual: " + lLastMessage);
    lLastMessage = lLastMessage.substring(lPrefix.length(), lLastMessage.length()).trim();
    assertEquals(lMessage, lLastMessage, "Wrong message content.");
    lAppender.reset();

    // Trace message on level "INFO"
    lTrace.info(lMessage);
    lLastMessage = lAppender.getLastMessage().toString();
    lLastMessage = lLastMessage.substring(lPrefix.length(), lLastMessage.length()).trim();
    assertEquals(lMessage, lLastMessage, "Wrong message content.");
    lAppender.reset();

    // Trace message on level "TRACE"
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.DEBUG);
    lTrace.trace(lMessage);
    assertNull(lAppender.getLastMessage(), "Message was traced.");
  }

  /**
   * Method tests tracing using JEAF's localization and parameterizing capabilities.
   */
  @Test
  public void testParameterizableTraces( ) {
    Trace lTrace = JUnitTrace.getInstance();
    assertNotNull(lTrace, "Method 'JUnitTrace.getInstance()' must not return null.");
    TestAppender lAppender = TestAppender.getInstance();
    assertNotNull(lAppender, "TestAppender not configured.");
    MessageRepository lMessageRepository = XFun.getMessageRepository();

    // Trace message on level "TRACE"
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.TRACE);
    assertEquals(TraceLevel.TRACE, lTrace.getLevel());
    MessageID lMessageID = JUnitMessages.TRACE_MESSAGE;
    lTrace.trace(lMessageID, (String[]) null);
    String lExpectedMessage = lMessageRepository.getTraceMessage(lMessageID);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Trace message on level "TRACE"
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.TRACE);
    lMessageID = JUnitMessages.TRACE_MESSAGE;
    lTrace.trace(lMessageID);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessageID);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test handling of null messages
    lTrace.trace((MessageID) null, new String[] { "abc" });
    assertEquals("null", lAppender.getLastMessage());
    lAppender.reset();
    lTrace.trace((MessageID) null, new Object[] { "abc" });
    assertEquals("null", lAppender.getLastMessage());
    lAppender.reset();

    // Test filter. Since version 1.0.4 of Apache Commons-Logging does not support Log4J's log level TRACE messsages
    // with level TRACE are traced through Log4J with level DEBUG.
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.INFO);
    assertEquals(TraceLevel.INFO, lTrace.getLevel());
    lTrace.trace(lMessageID, (String[]) null);
    assertNull(lAppender.getLastMessage(), "Message was traced.");

    // Trace message on level "DEBUG"
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.DEBUG);
    assertEquals(TraceLevel.DEBUG, lTrace.getLevel());
    lMessageID = JUnitMessages.DEBUG_MESSAGE;
    lTrace.debug(lMessageID, (String[]) null);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessageID);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test filter.
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.INFO);
    assertEquals(TraceLevel.INFO, lTrace.getLevel());
    lTrace.debug(lMessageID, (String[]) null);
    assertNull(lAppender.getLastMessage(), "Message was traced.");

    // Trace message on level "INFO"
    lMessageID = JUnitMessages.INFO_MESSAGE;
    lTrace.info(lMessageID);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessageID);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test filter.
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.WARN);
    assertEquals(TraceLevel.WARN, lTrace.getLevel());
    lTrace.info(lMessageID, (String[]) null);
    assertNull(lAppender.getLastMessage(), "Message was traced.");

    // Trace message on level "WARN"
    lMessageID = JUnitMessages.WARN_MESSAGE;
    lTrace.warn(lMessageID, (String[]) null);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessageID);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test filter.
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.ERROR);
    assertEquals(TraceLevel.ERROR, lTrace.getLevel());
    lTrace.warn(lMessageID);
    assertNull(lAppender.getLastMessage(), "Message was traced.");

    // Trace message on level "ERROR"
    lMessageID = JUnitMessages.ERROR_MESSAGE;
    lTrace.error(lMessageID, (String[]) null);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessageID);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test filter.
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.FATAL);
    assertEquals(TraceLevel.FATAL, lTrace.getLevel());
    lTrace.error(lMessageID, (String[]) null);
    assertNull(lAppender.getLastMessage(), "Message was traced.");

    // Trace message on level "FATAL"
    lMessageID = JUnitMessages.FATAL_MESSAGE;
    lTrace.fatal(lMessageID, (String[]) null);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessageID);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test filter.
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.OFF);
    lTrace.fatal(lMessageID, (String[]) null);
    assertNull(lAppender.getLastMessage(), "Message was traced.");

    // Test generic message.
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.ALL);
    lMessageID = JUnitMessages.GENERIC_MESSAGE;
    String[] lParams = new String[] { "WARN" };
    lTrace.error(lMessageID, lParams);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessageID, lParams);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test generic message.
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.ALL);
    lMessageID = JUnitMessages.GENERIC_MESSAGE;
    lParams = new String[] { "WARN" };
    lTrace.error(lMessageID, lParams[0]);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessageID, lParams);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();
  }

  /**
   * Methods tests tracing methods for exceptions and exception hierarchies.
   */
  @Test
  public void testExceptionTracing( ) {
    // Create simple exception and trace it.
    Trace lTrace = JUnitTrace.getInstance();
    TestAppender lAppender = TestAppender.getInstance();
    assertNotNull(lAppender, "Wrong Log4J settings. Appender " + TestAppender.TEST_APPENDER_NAME + " is not defined.");

    ApplicationException lApplicationException;
    lApplicationException = new JEAFApplicationException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_1);
    MessageID lMessageID = JUnitMessages.GENERIC_MESSAGE;
    String[] lParams = new String[] { "WARN" };

    lTrace.info(lMessageID, lApplicationException, lParams);
    // TestCase.assertEquals("Application exception was not traced.", lApplicationException,
    // lAppender.getLastThrowable());
    lAppender.reset();
    lTrace.error(lMessageID, lApplicationException, lParams);
    // TestCase.assertEquals("Application exception was not traced.", lApplicationException,
    // lAppender.getLastThrowable());
    lAppender.reset();
    lTrace.fatal(lMessageID, lApplicationException, lParams);
    // TestCase.assertEquals("Application exception was not traced.", lApplicationException,
    // lAppender.getLastThrowable());
    lAppender.reset();

    // Create SystemException with nested exceptions and also trace it.
    SystemException lSystemException =
        new JEAFSystemException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_2, lApplicationException);
    lSystemException = new JEAFSystemException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_2, lSystemException);
    lSystemException = new JEAFSystemException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_2, lSystemException);
    lSystemException = new JEAFSystemException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_2, lSystemException);

    lTrace.trace(lMessageID, lSystemException, lParams);
    // TestCase.assertEquals("System exception was not traced.", lAppender.getLastThrowable(), lSystemException);
    lAppender.reset();
    lTrace.debug(lMessageID, lSystemException, lParams);
    // TestCase.assertEquals("System exception was not traced.", lAppender.getLastThrowable(), lSystemException);
    lAppender.reset();
    lTrace.info(lMessageID, lSystemException, lParams);
    // TestCase.assertEquals("System exception was not traced.", lAppender.getLastThrowable(), lSystemException);
    lAppender.reset();
    lTrace.warn(lMessageID, lSystemException, lParams);
    // TestCase.assertEquals("System exception was not traced.", lAppender.getLastThrowable(), lSystemException);
    lAppender.reset();
    lTrace.error(lMessageID, lSystemException, lParams);
    // TestCase.assertEquals("Application exception was not traced.", lAppender.getLastThrowable(), lSystemException);
    lAppender.reset();
    lTrace.fatal(lMessageID, lSystemException, lParams);
    // TestCase.assertEquals("System exception was not traced.", lAppender.getLastThrowable(), lSystemException);
    lAppender.reset();
  }

  @Test
  public void testInternalLogMethod( ) {
    // Create simple exception and trace it.
    TraceImpl lTrace = (TraceImpl) JUnitTrace.getInstance();
    TestAppender lAppender = TestAppender.getInstance();

    lTrace.log(TraceLevel.WARN, "Warn Message", null);
    assertEquals("Warn Message", lAppender.getLastMessage());
    assertEquals(Level.WARN, lAppender.getLastLevel());

    lTrace.log(null, "Warn Message", null);
    assertEquals("Warn Message", lAppender.getLastMessage());
    assertEquals(Level.ERROR, lAppender.getLastLevel());

    lAppender.reset();
    lTrace.log(null, null, null);
    assertEquals("null", lAppender.getLastMessage());
    assertEquals(Level.ERROR, lAppender.getLastLevel());
    assertEquals(1, lAppender.getMessageCounter());
  }

  @Test
  public void testTechnicalDetailsInLogs( ) {
    // Create simple exception and trace it.
    TraceImpl lTrace = (TraceImpl) JUnitTrace.getInstance();
    TestAppender lAppender = TestAppender.getInstance();

    // Test technical details in logs
    ApplicationException lApplicationException = new JEAFApplicationException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_1,
        "Ugly but useful detail.", (Throwable) null);

    // Level TRACE
    lAppender.reset();
    lTrace.log(TraceLevel.TRACE, "User Message", lApplicationException);
    assertEquals(2, lAppender.getMessageCounter());
    assertEquals("Ugly but useful detail.", lAppender.getLastMessage());

    // Level DEBUG
    lAppender.reset();
    lTrace.log(TraceLevel.DEBUG, "User Message", lApplicationException);
    assertEquals(2, lAppender.getMessageCounter());
    assertEquals("Ugly but useful detail.", lAppender.getLastMessage());

    // Level INFO
    lAppender.reset();
    lTrace.log(TraceLevel.INFO, "User Message", lApplicationException);
    assertEquals(2, lAppender.getMessageCounter());
    assertEquals("Ugly but useful detail.", lAppender.getLastMessage());

    // Level WARN
    lAppender.reset();
    lTrace.log(TraceLevel.WARN, "User Message", lApplicationException);
    assertEquals(2, lAppender.getMessageCounter());
    assertEquals("Ugly but useful detail.", lAppender.getLastMessage());

    // Level ERROR
    lAppender.reset();
    lTrace.log(TraceLevel.ERROR, "User Message", lApplicationException);
    assertEquals(2, lAppender.getMessageCounter());
    assertEquals("Ugly but useful detail.", lAppender.getLastMessage());

    // Level FATAL
    lAppender.reset();
    lTrace.log(TraceLevel.FATAL, "User Message", lApplicationException);
    assertEquals(2, lAppender.getMessageCounter());
    assertEquals("Ugly but useful detail.", lAppender.getLastMessage());
  }

  /**
   * Method tests the deprecated methods for tracing of exceptions and exception hierarchies.
   *
   * @deprecated Method is deprecated to suppress compiler warnings for deprecation.
   */
  @Deprecated
  @Test
  public void testDeprecatedExceptionTracing( ) {
    // Create simple exception and trace it.
    Trace lTrace = JUnitTrace.getInstance();
    ApplicationException lApplicationException = new JEAFApplicationException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_1);
    lTrace.info(lApplicationException.getMessage());
    lTrace.error("Trace of simple exception", lApplicationException);
    lTrace.fatal(null, lApplicationException);

    // Create SystemException with nested exceptions and also trace it.
    SystemException lSystemException =
        new JEAFSystemException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_2, lApplicationException);
    lSystemException = new JEAFSystemException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_2, lSystemException);
    lSystemException = new JEAFSystemException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_2, lSystemException);
    lSystemException = new JEAFSystemException(JUnitMessages.CHECK_CONSTRAINTS_ERROR_2, lSystemException);
    lTrace.warn("Trace of exception hierarchy", lSystemException);
  }

  /**
   * JUnit Test for the write() Methods in Trace.java. Method tests tracing using JEAF's localization and parameterizing
   * capabilities. Test uses the different RootLogger Level to Test the Reaction of the Logger at the Messages For
   * Example : Error don't know Warn , Info know Warn: Hierarchy: INFO -> WARN->ERROR
   */
  @Test
  public void testWriteMethod( ) {
    // Declaration and Initialization of the needed Objects
    Trace lTrace = JUnitTrace.getInstance();
    TestAppender lAppender = TestAppender.getInstance();
    MessageRepository lMessageRepository = XFun.getMessageRepository();

    // Test DEBUG Object
    MessageID lMessage = JUnitMessages.DEBUG_MESSAGE;
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.DEBUG);
    lTrace.write(lMessage);
    String lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test DEBUG Object Bad Case
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.FATAL);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getMessage(lMessage);
    assertNotSame(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced");
    lAppender.reset();

    // Test DEBUG on Hierarchy
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.TRACE);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced");
    lAppender.reset();

    // Test ERROR Object
    lMessage = JUnitMessages.ERROR_MESSAGE;
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.ERROR);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test ERROR Object Bad Case
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.FATAL);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertNotSame(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced");
    lAppender.reset();

    // Test ERROR Object on Hierarchy
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.WARN);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertNotNull(lAppender.getLastMessage(), "Message was not traced");
    lAppender.reset();

    // Test FATAL Object
    lMessage = JUnitMessages.FATAL_MESSAGE;
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.FATAL);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test FATAL on Hierarchy
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.FATAL);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test INFO Object
    lMessage = JUnitMessages.INFO_MESSAGE;
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.INFO);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test INFO Object Bad Case
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.WARN);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertNotSame(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced");
    lAppender.reset();

    // Test INFO on Hierarchy
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.DEBUG);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced");
    lAppender.reset();

    // Test TRACE Object
    lMessage = JUnitMessages.TRACE_MESSAGE;
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.TRACE);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test Trace Object Bad Case
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.DEBUG);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertNotSame(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced");
    lAppender.reset();

    // Test WARN Object
    lMessage = JUnitMessages.WARN_MESSAGE;
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.WARN);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced.");
    lAppender.reset();

    // Test WARN Object Bad Case
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.ERROR);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertNotSame(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced");
    lAppender.reset();

    // Test WARN on Hierarchy
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.INFO);
    lTrace.write(lMessage);
    lExpectedMessage = lMessageRepository.getTraceMessage(lMessage);
    assertEquals(lExpectedMessage, lAppender.getLastMessage(), "Message was not traced");
    lAppender.reset();
  }

  @Test
  public void testTraceObject( ) {
    // Declaration and Initialization of the needed Objects
    Trace lTrace = JUnitTrace.getInstance();
    TestAppender lAppender = TestAppender.getInstance();
    lAppender.reset();
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.TRACE);
    Person lPerson = new Person();
    lPerson.name = "Donald Duck";
    lTrace.traceObject(lPerson);
    assertEquals("TRACE Donald Duck", lAppender.getLastMessage().toString());
  }

  @Test
  public void testDefaultTrace( ) {
    TestAppender lAppender = TestAppender.getInstance();
    lAppender.reset();
    Configurator.setLevel(LogManager.getRootLogger().getName(), Level.TRACE);
    DefaultTrace lDefaultTrace = new DefaultTrace();
    lDefaultTrace.info(JUnitMessages.WARN_MESSAGE);
    assertEquals("Error-Code: 10023", lAppender.getLastMessage());
    lDefaultTrace.info(null, "abc");
    assertEquals("Error-Code: ? Details: [abc]", lAppender.getLastMessage());
    lDefaultTrace.info(null, new String[] {});
    assertEquals("Error-Code: ?", lAppender.getLastMessage());

    lDefaultTrace.info(JUnitMessages.WARN_MESSAGE, new String[] {});
    assertEquals("Error-Code: 10023", lAppender.getLastMessage());

    lDefaultTrace.info(JUnitMessages.WARN_MESSAGE, (String[]) null);
    assertEquals("Error-Code: 10023", lAppender.getLastMessage());

    // Also test empty implementation to achieve test coverage
    lDefaultTrace.newContextStack(null);
    lDefaultTrace.pushContextStackElement(null);
    lDefaultTrace.popContextStackElement();
  }

  @Test
  public void testTraceLookup( ) {
    TraceProvider lTraceProvider = XFun.getTraceProvider();
    assertEquals(TraceProviderImpl.class, lTraceProvider.getClass());

    // Test trace lookup by name.
    Trace lNewTrace = lTraceProvider.getTrace("NewTrace");
    assertNotNull(lNewTrace);
    Trace lTrace = lTraceProvider.getTrace("NewTrace");
    assertEquals(lNewTrace, lTrace);

    // Test exception handling
    try {
      lTraceProvider.getTrace((String) null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }

    // Test trace lookup by class.
    lNewTrace = lTraceProvider.getTrace(this.getClass());
    assertNotNull(lNewTrace);
    lTrace = lTraceProvider.getTrace(this.getClass());
    assertEquals(lNewTrace, lTrace);

    // Test exception handling
    try {
      lTraceProvider.getTrace((Class<?>) null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }

    // Test trace lookup by component.
    ComponentID lComponentID = new ComponentID("YetAnotherTestComponent", "a.b.c");
    lNewTrace = lTraceProvider.getTrace(lComponentID);
    assertNotNull(lNewTrace);
    lTrace = lTraceProvider.getTrace(lComponentID);
    assertEquals(lNewTrace, lTrace);

    // Test exception handling
    try {
      lTraceProvider.getTrace((ComponentID) null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }

    TraceProviderImpl lTraceProviderImpl = (TraceProviderImpl) lTraceProvider;
    assertEquals("JEAF_DEFAULT_LOGGER", ((TraceImpl) lTraceProvider.getCurrentTrace()).getName());
    assertEquals("JEAF_DEFAULT_LOGGER", ((TraceImpl) XFun.getTrace()).getName());
    assertEquals(lTraceProviderImpl.getDefaultTrace(), lTraceProvider.getCurrentTrace());
    assertEquals(lTraceProviderImpl.getDefaultTrace(), XFun.getTrace());
  }

  @Test
  public void testContextStackHandling( ) {
    Trace lDefaultTrace = XFun.getTrace();
    ComponentID lComponent1 = new ComponentID("Component 1", "com.anaptecs.jeaf.component1");
    ContextStackElement lContextStackElement1 = new ContextStackElement("1", lComponent1);
    ContextStackElement lContextStackElement2 = new ContextStackElement("2", lComponent1);
    TestAppender lAppender = TestAppender.getInstance();
    lAppender.reset();
    lDefaultTrace.info("Hello");
    assertEquals("Hello", lAppender.getLastMessage());
    Trace lTrace = XFun.getTraceProvider().getTrace(lComponent1);

    // Level 1
    lDefaultTrace.newContextStack(lContextStackElement1);
    lDefaultTrace.info(JUnitMessages.CHECK_CONSTRAINTS_WARNING_1);
    String lLastMessage = lAppender.getLastMessage().toString();
    assertTrue(lLastMessage.startsWith("    [10002]"));

    // Level 2
    lDefaultTrace.pushContextStackElement(lContextStackElement2);
    lDefaultTrace.info(JUnitMessages.CHECK_CONSTRAINTS_WARNING_1);
    lLastMessage = lAppender.getLastMessage().toString();
    assertTrue(lLastMessage.startsWith("        [10002]"));
    assertEquals(lTrace, XFun.getTrace());

    // Back to level 1
    ContextStackElement lStackElement = lTrace.popContextStackElement();
    assertNotNull(lStackElement);
    lDefaultTrace.info(JUnitMessages.CHECK_CONSTRAINTS_WARNING_1);
    lLastMessage = lAppender.getLastMessage().toString();
    assertTrue(lLastMessage.startsWith("    [10002]"));

    // Back to level 0
    lStackElement = lTrace.popContextStackElement();
    assertNotNull(lStackElement);
    lDefaultTrace.info(JUnitMessages.CHECK_CONSTRAINTS_WARNING_1);
    lLastMessage = lAppender.getLastMessage().toString();
    assertTrue(lLastMessage.startsWith("[10002]"));

    // Test pop if stack is already empty.
    lStackElement = lTrace.popContextStackElement();
    assertNull(lStackElement);
    assertEquals(lDefaultTrace, XFun.getTrace());
  }

  @Test
  public void testTraceConfigurationUsage( ) {
    TraceConfiguration lTraceConfiguration = new TraceConfiguration("test_trace_config", null, true);
    assertNotNull(lTraceConfiguration);
    assertEquals(false, lTraceConfiguration.isTraceIndentationEnabled());
    assertEquals(false, lTraceConfiguration.exposeLoggersViaJMX());
    TraceImpl lTraceImpl = new TraceImpl(TraceTest.class.getName(), lTraceConfiguration);

    TestAppender lAppender = TestAppender.getInstance();
    lAppender.reset();
    lTraceImpl.info(JUnitMessages.CHECK_CONSTRAINTS_WARNING_1);
    String lLastMessage = lAppender.getLastMessage().toString();
    assertTrue(lLastMessage.startsWith("[10002]"));

    // Level 1
    ComponentID lComponent1 = new ComponentID("Component 1", "com.anaptecs.jeaf.component1");
    ContextStackElement lContextStackElement1 = new ContextStackElement("1", lComponent1);
    lTraceImpl.pushContextStackElement(lContextStackElement1);

    ContextStackElement lContextStackElement = lTraceImpl.popContextStackElement();
    lContextStackElement = lTraceImpl.popContextStackElement();
    assertNull(lContextStackElement);
  }

  @Test
  public void testLoggerName( ) {
    TraceProviderImpl lTraceProviderImpl = (TraceProviderImpl) XFun.getTraceProvider();

    ComponentID lComponentID = new ComponentID("Component_1", "com.anaptecs.jeaf.component1");
    TraceConfiguration lCustomTraceConfiguration = new TraceConfiguration("CUSTOM_TRACE", "META-INF", true);

    assertEquals("Component_1", lTraceProviderImpl.getLoggerName(lComponentID, TraceConfiguration.getInstance()));
    assertEquals("JEAF_XFUN_IMPL_TEST.Component_1",
        lTraceProviderImpl.getLoggerName(lComponentID, lCustomTraceConfiguration));
  }

  @Test
  public void testEmergencyTrace( ) {
    TestAppender lAppender = TestAppender.getInstance();
    // lAppender.setThreshold(Level.TRACE);
    lAppender.reset();
    Trace lTrace = XFun.getTrace();
    lTrace.writeEmergencyTrace("This is a simple emergency trace", null);
    assertEquals("This is a simple emergency trace", lAppender.getLastMessage());
    assertEquals(Level.FATAL, lAppender.getLastLevel());

    lTrace.writeEmergencyTrace("This is a simple emergency trace", null, null);
    assertEquals("This is a simple emergency trace", lAppender.getLastMessage());
    assertEquals(Level.FATAL, lAppender.getLastLevel());
    lAppender.reset();

    // Due to the defined threshold nothing will be send to the appender.
    lTrace.writeEmergencyTrace("This is a simple emergency trace", null, TraceLevel.TRACE);
    assertNull(lAppender.getLastMessage());
    assertNull(lAppender.getLastLevel());
    lAppender.reset();

    lTrace.writeEmergencyTrace("This is a simple emergency trace", null, TraceLevel.DEBUG);
    assertNull(lAppender.getLastMessage());
    assertNull(lAppender.getLastLevel());
    lAppender.reset();

    lTrace.writeEmergencyTrace("This is a simple emergency trace", null, TraceLevel.INFO);
    assertEquals("This is a simple emergency trace", lAppender.getLastMessage());
    assertEquals(Level.INFO, lAppender.getLastLevel());
    lAppender.reset();

    lTrace.writeEmergencyTrace("This is a simple emergency trace", null, TraceLevel.WARN);
    assertEquals("This is a simple emergency trace", lAppender.getLastMessage());
    assertEquals(Level.WARN, lAppender.getLastLevel());
    lAppender.reset();

    lTrace.writeEmergencyTrace("This is a simple emergency trace", null, TraceLevel.ERROR);
    assertEquals("This is a simple emergency trace", lAppender.getLastMessage());
    assertEquals(Level.ERROR, lAppender.getLastLevel());
    lAppender.reset();

    lTrace.writeEmergencyTrace("This is a simple emergency trace", null, TraceLevel.FATAL);
    assertEquals("This is a simple emergency trace", lAppender.getLastMessage());
    assertEquals(Level.FATAL, lAppender.getLastLevel());
    lAppender.reset();
  }

  @Test
  public void testManagementMethods( ) {
    TraceImpl lTrace = (TraceImpl) JUnitTrace.getInstance();
    assertEquals(TraceLevel.INFO.name(), lTrace.getTraceLevel());

    // Set trace level via method that is intended for usage by JMX.

    // Trace
    lTrace.setTraceLevel(TraceLevel.TRACE.name());
    assertEquals(TraceLevel.TRACE, lTrace.getLevel());
    assertEquals(TraceLevel.TRACE.name(), lTrace.getTraceLevel());

    // Debug
    lTrace.setTraceLevel(TraceLevel.DEBUG.name());
    assertEquals(TraceLevel.DEBUG, lTrace.getLevel());
    assertEquals(TraceLevel.DEBUG.name(), lTrace.getTraceLevel());

    // Info
    lTrace.setTraceLevel(TraceLevel.INFO.name());
    assertEquals(TraceLevel.INFO, lTrace.getLevel());
    assertEquals(TraceLevel.INFO.name(), lTrace.getTraceLevel());
    assertEquals("Core JUnits", lTrace.getName());

    // Warn
    lTrace.setTraceLevel(TraceLevel.WARN.name());
    assertEquals(TraceLevel.WARN, lTrace.getLevel());
    assertEquals(TraceLevel.WARN.name(), lTrace.getTraceLevel());

    // Error
    lTrace.setTraceLevel(TraceLevel.ERROR.name());
    assertEquals(TraceLevel.ERROR, lTrace.getLevel());
    assertEquals(TraceLevel.ERROR.name(), lTrace.getTraceLevel());

    // Fatal
    lTrace.setTraceLevel(TraceLevel.FATAL.name());
    assertEquals(TraceLevel.FATAL, lTrace.getLevel());
    assertEquals(TraceLevel.FATAL.name(), lTrace.getTraceLevel());

    // Rest trace level.
    lTrace.resetTraceLevel();
    assertEquals(TraceLevel.INFO, lTrace.getLevel());

    try {
      lTrace.setTraceLevel(null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pTraceLevel' must not be null.", e.getMessage());
    }

    try {
      lTrace.setTraceLevel("Hello");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("No enum constant com.anaptecs.jeaf.xfun.api.trace.TraceLevel.Hello", e.getMessage());
    }
  }

  @Test
  public void testLevelConversion( ) {
    TraceImpl lTrace = (TraceImpl) JUnitTrace.getInstance();
    assertEquals(Level.FATAL, lTrace.toLevel(null));
    assertEquals(Level.TRACE, lTrace.toLevel(TraceLevel.TRACE));
    assertEquals(Level.DEBUG, lTrace.toLevel(TraceLevel.DEBUG));
    assertEquals(Level.INFO, lTrace.toLevel(TraceLevel.INFO));
    assertEquals(Level.WARN, lTrace.toLevel(TraceLevel.WARN));
    assertEquals(Level.ERROR, lTrace.toLevel(TraceLevel.ERROR));
    assertEquals(Level.FATAL, lTrace.toLevel(TraceLevel.FATAL));
  }

  @Test
  public void testTraceManagementMBean( ) {
    TraceManagement lTrace = new TraceManagement((TraceImpl) JUnitTrace.getInstance());
    assertEquals(TraceLevel.INFO.name(), lTrace.getTraceLevel());

    // Trace
    lTrace.setTraceLevel(TraceLevel.TRACE.name());
    assertEquals(TraceLevel.TRACE.name(), lTrace.getTraceLevel());

    // Debug
    lTrace.setTraceLevel(TraceLevel.DEBUG.name());
    assertEquals(TraceLevel.DEBUG.name(), lTrace.getTraceLevel());

    // Info
    lTrace.setTraceLevel(TraceLevel.INFO.name());
    assertEquals(TraceLevel.INFO.name(), lTrace.getTraceLevel());
    assertEquals("Core JUnits", lTrace.getName());

    // Warn
    lTrace.setTraceLevel(TraceLevel.WARN.name());
    assertEquals(TraceLevel.WARN.name(), lTrace.getTraceLevel());

    // Error
    lTrace.setTraceLevel(TraceLevel.ERROR.name());
    assertEquals(TraceLevel.ERROR.name(), lTrace.getTraceLevel());

    // Fatal
    lTrace.setTraceLevel(TraceLevel.FATAL.name());
    assertEquals(TraceLevel.FATAL.name(), lTrace.getTraceLevel());

    // Rest trace level.
    lTrace.resetTraceLevel();
    assertEquals(Level.INFO, LogManager.getRootLogger().getLevel());

    try {
      lTrace.setTraceLevel(null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("'pTraceLevel' must not be null.", e.getMessage());
    }

    try {
      lTrace.setTraceLevel("Hello");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("No enum constant com.anaptecs.jeaf.xfun.api.trace.TraceLevel.Hello", e.getMessage());
    }

    try {
      new TraceManagement(null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Check failed. pTrace must not be NULL.", e.getMessage());
    }
  }

  @Test
  public void testJMXInteggration( ) throws JMException {
    ObjectName lRootLoggerName = new ObjectName(TraceProviderImpl.LOGGER_PREFIX + "JEAF_DEFAULT_LOGGER");

    // Try to connect to MBeanServer.
    MBeanServer lMBeanServer = ManagementFactory.getPlatformMBeanServer();

    TraceImpl lTrace = (TraceImpl) XFun.getTrace();

    // Get state of JEAF Fast Lane server via JMX
    String lNameFromJMX = (String) lMBeanServer.getAttribute(lRootLoggerName, "Name");
    assertEquals("JEAF_DEFAULT_LOGGER", lNameFromJMX);
    String lTraceLevelFromJMX = (String) lMBeanServer.getAttribute(lRootLoggerName, "TraceLevel");
    assertEquals(lTrace.getLevel().name(), lTraceLevelFromJMX);

    // Test additional logger.
    String lLoggerName = "MyTestLogger";
    ObjectName lLoggerJMXName = new ObjectName(TraceProviderImpl.LOGGER_PREFIX + lLoggerName);

    // As logger does not yet exist an exception is expected.
    try {
      lMBeanServer.getAttribute(lLoggerJMXName, "Name");
      fail();
    }
    catch (InstanceNotFoundException e) {
      assertEquals("com.anaptecs.jeaf.xfun.trace:type=Logger, name=MyTestLogger", e.getMessage());
    }

    // Now let's get logger.
    lTrace = (TraceImpl) XFun.getTraceProvider().getTrace(lLoggerName);
    assertEquals(TraceLevel.INFO, lTrace.getLevel());
    lNameFromJMX = (String) lMBeanServer.getAttribute(lLoggerJMXName, "Name");
    assertEquals(lLoggerName, lNameFromJMX);
    lTraceLevelFromJMX = (String) lMBeanServer.getAttribute(lLoggerJMXName, "TraceLevel");
    assertEquals(lTrace.getLevel().name(), lTraceLevelFromJMX);

    // Let's change log level and see if information is also visible via JMX.
    lTrace.setTraceLevel(TraceLevel.ERROR.name());
    assertEquals(TraceLevel.ERROR, lTrace.getLevel());
    lTraceLevelFromJMX = (String) lMBeanServer.getAttribute(lLoggerJMXName, "TraceLevel");
    assertEquals(lTrace.getLevel().name(), lTraceLevelFromJMX);

    // Set attribute via JMX
    lMBeanServer.setAttribute(lLoggerJMXName, new Attribute("TraceLevel", TraceLevel.FATAL.name()));
    assertEquals(TraceLevel.FATAL, lTrace.getLevel());

    // Test reset via JMX
    lMBeanServer.invoke(lLoggerJMXName, "resetTraceLevel", null, null);
    assertEquals(TraceLevel.INFO, lTrace.getLevel());

    // Provoke JMX Exception
    String lBlockedLoggerName = "BlockedLogger";
    ObjectName lBlockedLoggerJMXName = new ObjectName(TraceProviderImpl.LOGGER_PREFIX + lBlockedLoggerName);
    TraceManagement lTraceManagement = new TraceManagement(lTrace);
    lMBeanServer.registerMBean(lTraceManagement, lBlockedLoggerJMXName);

    lTrace = (TraceImpl) XFun.getTraceProvider().getTrace(lBlockedLoggerName);
    assertNotNull(lTrace);

    // Try to create a TraceProvider for the second time. This will lead to an JMX Exception but anyways it is expected
    // that it can be created.
    TraceProvider lNewTraceProvider = new TraceProviderImpl();
    assertNotNull(lNewTraceProvider);

    // Test trace provider with disabled JMX
    TraceConfiguration lTraceConfiguration = new TraceConfiguration("test_trace_config", null, true);
    assertEquals(false, lTraceConfiguration.exposeLoggersViaJMX());
    lNewTraceProvider = new TraceProviderImpl(lTraceConfiguration);
    assertNotNull(lNewTraceProvider);
    assertNotNull(lNewTraceProvider.getTrace("NonJMXManagedLogger"));

  }

  @Test
  public void testXFunTraceAccess( ) {
    XFun.getTrace().info("Hello JEAF X-Fun");
  }
}