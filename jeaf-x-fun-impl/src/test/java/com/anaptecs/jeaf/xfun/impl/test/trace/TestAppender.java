/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.trace;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

/**
 * Class implements special Log4J-Appender
 * 
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
@Plugin(name = "TestAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public final class TestAppender extends AbstractAppender {
  /**
   * Constant for the name of the test appender.
   */
  public static final String TEST_APPENDER_NAME = "TestAppender";

  private int messageCounter = 0;

  @PluginFactory
  public static TestAppender createAppender( @PluginAttribute("name") String pName,
      @PluginElement("Filter") Filter pFilter ) {
    return new TestAppender(pName, pFilter);
  }

  /**
   * Method returns the instance of this class that is used by Log4J.
   * 
   * @return TestAppender TestAppender object that is used by Log4J. The method never returns null.
   */
  public static TestAppender getInstance( ) {
    org.apache.logging.log4j.core.Logger lRootLogger =
        (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
    return (TestAppender) lRootLogger.getAppenders().get(TEST_APPENDER_NAME);
  }

  /**
   * Last message that was logged through this appender.
   */
  private Object lastMessage;

  /**
   * Log level of the last message
   */
  private Level lastLevel;

  /**
   * Last throwable that was logged through this appender.
   */
  private Throwable lastThrowable;

  /**
   * Initialize new object. Thereby no actions are performed.
   */
  public TestAppender( String pName, Filter pFilter ) {
    super(TEST_APPENDER_NAME, null, null, false, null);
  }

  /**
   * Method returns the last message that was logged through this appender.
   * 
   * @return Object Last message that was logged through this appender. The method may return null.
   */
  public Object getLastMessage( ) {
    return lastMessage;
  }

  public Level getLastLevel( ) {
    return lastLevel;
  }

  /**
   * Method returns the last Throwable object that was logged through this appender.
   * 
   * @return Throwable Last Throwable object that was logged through this appender. The method may return null.
   */
  public Throwable getLastThrowable( ) {
    return lastThrowable;
  }

  public int getMessageCounter( ) {
    return messageCounter;
  }

  /**
   * Method resets the last message and last Throwable object to null.
   */
  public void reset( ) {
    lastMessage = null;
    lastLevel = null;
    lastThrowable = null;
    messageCounter = 0;
  }

  @Override
  public void append( LogEvent pLogEvent ) {
    Message lMessage = pLogEvent.getMessage();
    lastMessage = lMessage.toString();
    lastLevel = pLogEvent.getLevel();
    lastThrowable = pLogEvent.getThrown();
    messageCounter++;
  }
}
