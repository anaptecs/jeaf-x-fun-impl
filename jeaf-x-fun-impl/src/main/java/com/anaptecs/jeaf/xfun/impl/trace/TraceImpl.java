/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.trace;

import java.util.Stack;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.api.trace.ContextStackElement;
import com.anaptecs.jeaf.xfun.api.trace.TraceConfiguration;

/**
 * Class provides the the JEAF X-Fun standard implementation for tracing. It uses Apache Commons Logging and uses JEAF's
 * Message Repository to build messages. It also supports indentation of traces.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public class TraceImpl extends AbstractCommonsLoggingTraceImpl {
  /**
   * Constant for an empty string.
   */
  private static final String EMPTY_STRING = "";

  /**
   * Context stack is kept static thread local as it is shared among multiple trace instances.
   */
  private static ThreadLocal<Stack<ContextStackElement>> contextStackHolder = new ThreadLocal<>();

  /**
   * Attribute defines whether trace output should be indented.
   */
  private boolean indentTrace;

  /**
   * String contains the indentation that should be used for every level.
   */
  private String indentSize;

  /**
   * Method returns the current context stack. If none exists yet then it will be created.
   * 
   * @return {@link Stack} Current Context stack. The method never returns null.
   */
  private static Stack<ContextStackElement> getContextStack( ) {
    Stack<ContextStackElement> lContextStack = contextStackHolder.get();
    if (lContextStack == null) {
      lContextStack = new Stack<>();
      contextStackHolder.set(lContextStack);
    }
    return lContextStack;
  }

  /**
   * Method returns the current context stack element or null if the stack is empty.
   * 
   * @return {@link ContextStackElement} Current context stack element or null.
   */
  static ContextStackElement getCurrentContextStackElement( ) {
    Stack<ContextStackElement> lContextStack = TraceImpl.getContextStack();
    ContextStackElement lElement;
    if (lContextStack.isEmpty() == false) {
      lElement = lContextStack.peek();
    }
    else {
      lElement = null;
    }
    return lElement;
  }

  /**
   * Initialize a new Trace object for the passed logger.
   * 
   * @param pLoggerName Name of the logger for which the new Trace instance will be created. The parameter must not be
   * null.
   */
  public TraceImpl( String pLoggerName ) {
    this(pLoggerName, TraceConfiguration.getInstance());
  }

  /**
   * Initialize a new Trace object for the passed logger.
   * 
   * @param pLoggerName Name of the logger for which the new Trace instance will be created. The parameter must not be
   * null.
   * @param pTraceConfiguration Trace configuration that should be used to configure tracing. The parameter must not be
   * null.
   */
  public TraceImpl( String pLoggerName, TraceConfiguration pTraceConfiguration ) {
    super(pLoggerName);

    // Configure indentation
    this.configureIndentation(pTraceConfiguration);
  }

  /**
   * Method creates a new message based on the locale to use for tracing and the passed parameters.
   * 
   * @param pMessageID Id of the message that should be traced. The parameter may be null. In this case no message will
   * be created.
   * @param pMessageParameters Values that are used to parameterize the trace message. The parameter may be null.
   * @return String Created message or null.
   */
  @Override
  protected String getMessage( MessageID pMessageID, String[] pMessageParameters ) {
    // Create message.
    String lMessage;
    if (pMessageID != null) {
      MessageRepository lMessageRepository = XFun.getMessageRepository();
      lMessage = this.getCurrentIndentation() + lMessageRepository.getTraceMessage(pMessageID, pMessageParameters);
    }
    else {
      lMessage = null;
    }
    // Return created message.
    return lMessage;
  }

  /**
   * Method configures indentation settings as defined for this logger.
   */
  private void configureIndentation( TraceConfiguration pTraceConfig ) {
    // Get indentation settings.
    if (pTraceConfig != null) {
      indentTrace = pTraceConfig.isTraceIndentationEnabled();
      int lIndentSize = pTraceConfig.getIndentSize();
      StringBuilder lIndentBuffer = new StringBuilder(lIndentSize);
      for (int i = 0; i < lIndentSize; i++) {
        lIndentBuffer.append(' ');
      }
      indentSize = lIndentBuffer.toString();
    }
  }

  /**
   * Method returns the current indentation that should be used. How many blanks are used as indentation depends on the
   * depth of the service invocation stack.
   * 
   * @return String String containing as many blank as should be used as indentation. The method never returns null.
   */
  private String getCurrentIndentation( ) {
    // Check if indentation should be used.
    String lCurrentIndentation;
    if (indentTrace == true) {
      // Get current context to calculate the indentation size.
      // Create new indentation string depending on the current stack size.
      int lCurrentLevel = this.getIndentationLevel();
      StringBuilder lIndent = new StringBuilder(lCurrentLevel * indentSize.length());
      for (int i = 0; i < lCurrentLevel; i++) {
        lIndent.append(indentSize);
      }
      lCurrentIndentation = lIndent.toString();
    }
    else {
      lCurrentIndentation = EMPTY_STRING;
    }
    // Return calculated indentation.
    return lCurrentIndentation;
  }

  private Integer getIndentationLevel( ) {
    return TraceImpl.getContextStack().size();
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
    // Check parameter
    Check.checkInvalidParameterNull(pContextStackElement, "pContextStackElement");

    // Lookup context stack and just empty it. This will lead to the same result.
    Stack<ContextStackElement> lContextStack = TraceImpl.getContextStack();
    lContextStack.clear();

    // Push passed new element to context stack.
    lContextStack.push(pContextStackElement);
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
    // Check parameter
    Check.checkInvalidParameterNull(pContextStackElement, "pContextStackElement");

    // Lookup context stack and push new element
    Stack<ContextStackElement> lContextStack = TraceImpl.getContextStack();
    lContextStack.push(pContextStackElement);
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
    // Lookup context stack
    Stack<ContextStackElement> lContextStack = TraceImpl.getContextStack();

    // Pop latest element from stack, if the stack is not already empty.
    ContextStackElement lElement;
    if (lContextStack.isEmpty() == false) {
      lElement = lContextStack.pop();
    }
    else {
      lElement = null;
    }
    return lElement;
  }
}
