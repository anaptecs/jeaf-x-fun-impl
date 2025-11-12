package com.anaptecs.jeaf.junit.tools;

import com.anaptecs.jeaf.xfun.annotations.MessageResource;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;

/**
 * Test class for generation of constants.
 *
 * @author JEAF Development Team
 * @version 1.1
 */
@MessageResource(path = "TestMessageData.xml")
public final class MessageConstantTest {
  /**
   * Constant for XML file that contains all messages that are defined within this class.
   */
  private static final String MESSAGE_RESOURCE = "TestMessageData.xml";

  /**
   * Test 1
   */
  public static final ErrorCode TEST_ERROR_001;

  /**
   * Exception id is used whenever a parameter with the invalid value null is passed to an method.
   */
  public static final ErrorCode INVALID_PARAMETER_NULL;

  /**
   * Erster Info-Test
   */
  public static final MessageID TEST_01;

  /**
   * Error message for ID 1212.
   */
  public static final ErrorCode ERROR_MESSAGE_1212;

  /**
   * Error message for ID 1212.
   */
  public static final LocalizedString LOCALIZED_TEXT;

  /**
   * Parameterisierte Fehlermeldung
   */
  public static final ErrorCode PARAMETERIZED_MESSAGE;
  /**
   * Static initializer contains initialization for all generated constants.
   */
  static {
    MessageRepository lRepository = XFun.getMessageRepository();
    lRepository.loadResource(MESSAGE_RESOURCE);
    // Handle all info messages.
    TEST_01 = lRepository.getMessageID(20013);
    // Handle all messages for errors.
    TEST_ERROR_001 = lRepository.getErrorCode(20011);
    INVALID_PARAMETER_NULL = lRepository.getErrorCode(20012);
    ERROR_MESSAGE_1212 = lRepository.getErrorCode(1212);
    PARAMETERIZED_MESSAGE = lRepository.getErrorCode(1234567890);
    // Handle all localized strings.
    LOCALIZED_TEXT = lRepository.getLocalizedString(1213);
  }

  /**
   * Constructor is private to ensure that no instances of this class will be created.
   */
  private MessageConstantTest( ) {
    // Nothing to do.
  }
}