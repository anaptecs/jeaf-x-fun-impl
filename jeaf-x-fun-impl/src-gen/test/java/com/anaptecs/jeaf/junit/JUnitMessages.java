package com.anaptecs.jeaf.junit;

import com.anaptecs.jeaf.xfun.annotations.MessageResource;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;

/**
 * Class contains all message ids and error codes that are used within the JEAF tests.
 *
 * @author JEAF Development Team
 * @version 1.0
 */
@MessageResource(path = "JUnitMessages.xml")
public final class JUnitMessages {
  /**
   * Constant for XML file that contains all messages that are defined within this class.
   */
  private static final String MESSAGE_RESOURCE = "JUnitMessages.xml";

  /**
   * Message id is used to test class VerificationFailure
   */
  public static final MessageID VERIFICATION_FAILURE_TEST_MESSAGE_ID;

  /**
   * Error code is used to test class VerificationFailure
   */
  public static final ErrorCode VERIFICATION_FAILURE_TEST_ERROR_CODE;

  /**
   * Message id is used to indicate an unfulfilled constraint warning.
   */
  public static final MessageID CHECK_CONSTRAINTS_WARNING_1;

  /**
   * Message id is used to indicate an unfulfilled constraint warning.
   */
  public static final MessageID CHECK_CONSTRAINTS_WARNING_2;

  /**
   * Message id is used to indicate an unfulfilled constraint warning.
   */
  public static final MessageID CHECK_CONSTRAINTS_WARNING_3;

  /**
   * Message id is used to indicate an unfulfilled constraint warning.
   */
  public static final MessageID CHECK_CONSTRAINTS_WARNING_4;

  /**
   * Message id is used to indicate an unfulfilled constraint warning.
   */
  public static final MessageID CHECK_CONSTRAINTS_WARNING_5;

  /**
   * Message id is used to indicate an unfulfilled constraint warning.
   */
  public static final MessageID CHECK_CONSTRAINTS_WARNING_6;

  /**
   * Message id is used to indicate an unfulfilled constraint warning.
   */
  public static final MessageID CHECK_CONSTRAINTS_WARNING_7;

  /**
   * Message id is used to indicate an unfulfilled constraint warning.
   */
  public static final MessageID CHECK_CONSTRAINTS_WARNING_8;

  /**
   * Message id is used to indicate an unfulfilled constraint warning.
   */
  public static final MessageID CHECK_CONSTRAINTS_WARNING_9;

  /**
   * Error code is used to indicate an unfulfilled constraint error.
   */
  public static final ErrorCode CHECK_CONSTRAINTS_ERROR_1;

  /**
   * Error code is used to indicate an unfulfilled constraint error.
   */
  public static final ErrorCode CHECK_CONSTRAINTS_ERROR_2;

  /**
   * Error code is used to indicate an unfulfilled constraint error.
   */
  public static final ErrorCode CHECK_CONSTRAINTS_ERROR_3;

  /**
   * Error code is used to indicate an unfulfilled constraint error.
   */
  public static final ErrorCode CHECK_CONSTRAINTS_ERROR_4;

  /**
   * Error code is used to indicate an unfulfilled constraint error.
   */
  public static final ErrorCode CHECK_CONSTRAINTS_ERROR_5;

  /**
   * Error code is used to indicate an unfulfilled constraint error.
   */
  public static final ErrorCode CHECK_CONSTRAINTS_ERROR_6;

  /**
   * Error code is used to indicate an unfulfilled constraint error.
   */
  public static final ErrorCode CHECK_CONSTRAINTS_ERROR_7;

  /**
   * Error code is used to indicate an unfulfilled constraint error.
   */
  public static final ErrorCode CHECK_CONSTRAINTS_ERROR_8;

  /**
   * Error code is used to indicate an unfulfilled constraint error.
   */
  public static final ErrorCode CHECK_CONSTRAINTS_ERROR_9;

  /**
   * Message-Id will be used within tests for traces with trace level TRACE.
   */
  public static final MessageID TRACE_MESSAGE;

  /**
   * Message-Id will be used within tests for traces with trace level DEBUG.
   */
  public static final MessageID DEBUG_MESSAGE;

  /**
   * Message-Id will be used within tests for traces with trace level INFO.
   */
  public static final MessageID INFO_MESSAGE;

  /**
   * Message-Id will be used within tests for traces with trace level WARN.
   */
  public static final MessageID WARN_MESSAGE;

  /**
   * Message-Id will be used within tests for traces with trace level ERROR.
   */
  public static final MessageID ERROR_MESSAGE;

  /**
   * Message-Id will be used within tests for traces with trace level FATAL.
   */
  public static final MessageID FATAL_MESSAGE;

  /**
   * Message-Id will be used within tests for traces with a parameterized trace level.
   */
  public static final MessageID GENERIC_MESSAGE;

  /**
   * Message-Id will be used within tests for traces when trying to call a service with transaction behvior NEVER from
   * within a transaction.
   */
  public static final MessageID TX_NEVER_CALL_FROM_TX;

  /**
   * Message-Id will be used to tests JEAFs interceptor implementation.
   */
  public static final ErrorCode APE;

  /**
   * Message-Id will be used to tests JEAFs interceptor implementation.
   */
  public static final ErrorCode SYE;
  /**
   * Static initializer contains initialization for all generated constants.
   */
  static {
    MessageRepository lRepository = XFun.getMessageRepository();
    lRepository.loadResource(MESSAGE_RESOURCE);
    // Handle all info messages.
    VERIFICATION_FAILURE_TEST_MESSAGE_ID = lRepository.getMessageID(10000);
    CHECK_CONSTRAINTS_WARNING_1 = lRepository.getMessageID(10002);
    CHECK_CONSTRAINTS_WARNING_2 = lRepository.getMessageID(10003);
    CHECK_CONSTRAINTS_WARNING_3 = lRepository.getMessageID(10004);
    CHECK_CONSTRAINTS_WARNING_4 = lRepository.getMessageID(10005);
    CHECK_CONSTRAINTS_WARNING_5 = lRepository.getMessageID(10006);
    CHECK_CONSTRAINTS_WARNING_6 = lRepository.getMessageID(10007);
    CHECK_CONSTRAINTS_WARNING_7 = lRepository.getMessageID(10008);
    CHECK_CONSTRAINTS_WARNING_8 = lRepository.getMessageID(10009);
    CHECK_CONSTRAINTS_WARNING_9 = lRepository.getMessageID(10010);
    TRACE_MESSAGE = lRepository.getMessageID(10020);
    DEBUG_MESSAGE = lRepository.getMessageID(10021);
    INFO_MESSAGE = lRepository.getMessageID(10022);
    WARN_MESSAGE = lRepository.getMessageID(10023);
    ERROR_MESSAGE = lRepository.getMessageID(10024);
    FATAL_MESSAGE = lRepository.getMessageID(10025);
    GENERIC_MESSAGE = lRepository.getMessageID(10026);
    TX_NEVER_CALL_FROM_TX = lRepository.getMessageID(10027);
    // Handle all messages for errors.
    VERIFICATION_FAILURE_TEST_ERROR_CODE = lRepository.getErrorCode(10001);
    CHECK_CONSTRAINTS_ERROR_1 = lRepository.getErrorCode(10011);
    CHECK_CONSTRAINTS_ERROR_2 = lRepository.getErrorCode(10012);
    CHECK_CONSTRAINTS_ERROR_3 = lRepository.getErrorCode(10013);
    CHECK_CONSTRAINTS_ERROR_4 = lRepository.getErrorCode(10014);
    CHECK_CONSTRAINTS_ERROR_5 = lRepository.getErrorCode(10015);
    CHECK_CONSTRAINTS_ERROR_6 = lRepository.getErrorCode(10016);
    CHECK_CONSTRAINTS_ERROR_7 = lRepository.getErrorCode(10017);
    CHECK_CONSTRAINTS_ERROR_8 = lRepository.getErrorCode(10018);
    CHECK_CONSTRAINTS_ERROR_9 = lRepository.getErrorCode(10019);
    APE = lRepository.getErrorCode(10501);
    SYE = lRepository.getErrorCode(10502);
    // Handle all localized strings.
  }

  /**
   * Constructor is private to ensure that no instances of this class will be created.
   */
  private JUnitMessages( ) {
    // Nothing to do.
  }
}