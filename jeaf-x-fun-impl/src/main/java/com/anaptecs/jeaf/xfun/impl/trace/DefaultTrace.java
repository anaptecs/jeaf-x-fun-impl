/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.trace;

import java.util.Arrays;

import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.ContextStackElement;

/**
 * Class provides a default trace implementation that is used by JEAF X-Fun during startup until real tracing is
 * initialized.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public class DefaultTrace extends AbstractCommonsLoggingTraceImpl {
  public static final String JEAF_DEFAULT_LOGGER_NAME = "JEAF_DEFAULT_LOGGER";

  /**
   * Initialize a new Trace-Object for the passed component.
   * 
   * @param pLoggerName Name of the logger for which the new Trace instance will be created. The parameter must not be
   * null.
   */
  public DefaultTrace( ) {
    super(JEAF_DEFAULT_LOGGER_NAME);
  }

  /**
   * Method creates a new message based on the locale to use for tracing and the passed parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no message will
   * be created.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   * @return String Created message. The method never returns null.
   */
  @Override
  protected String getMessage( MessageID pMessageID, String[] pMessageParameters ) {
    StringBuilder lBuilder = new StringBuilder();
    lBuilder.append("Error-Code: ");
    if (pMessageID != null) {
      lBuilder.append(pMessageID.getLocalizationID());
    }
    else {
      lBuilder.append("?");
    }

    if (pMessageParameters != null && pMessageParameters.length > 0) {
      lBuilder.append(" Details: ");
      lBuilder.append(Arrays.toString(pMessageParameters));
    }
    return lBuilder.toString();
  }

  /**
   * In order to support to know the current context JEAF's trace mechanism supports a so called context stack. Based on
   * this context stack we are able to know the current context and can provide the current trace object.
   * 
   * Method creates a new context stack with the passed object as first element on it.
   * 
   * @param pContextStackElement First element for new context stack. The parameter must not be null.
   */
  @Override
  public void newContextStack( ContextStackElement pContextStackElement ) {
    // Nothing to do.
  }

  /**
   * In order to support to know the current context JEAF's trace mechanism supports a so called context stack. Based on
   * this context stack we are able to know the current context and can provide the current trace object.
   * 
   * Method pushes the passed object to the existing context stack.
   * 
   * @param pContextStackElement New element for the context stack. The parameter must not be null.
   */
  @Override
  public void pushContextStackElement( ContextStackElement pContextStackElement ) {
    // Nothing to do.
  }

  /**
   * In order to support to know the current context JEAF's trace mechanism supports a so called context stack. Based on
   * this context stack we are able to know the current context and can provide the current trace object.
   * 
   * Method pops the top element of the context stack.
   * 
   * @return {@link ContextStackElement} Object that was the top element on the context stack or null if the stack is
   * empty.
   */
  @Override
  public ContextStackElement popContextStackElement( ) {
    return null;
  }
}
