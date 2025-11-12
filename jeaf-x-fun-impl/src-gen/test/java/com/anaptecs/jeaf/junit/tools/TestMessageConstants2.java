package com.anaptecs.jeaf.junit.tools;

import com.anaptecs.jeaf.xfun.annotations.MessageResource;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;

/**
 * Test class for generation of constants.
 *
 * @author JEAF Development Team
 * @version 1.1
 */
@MessageResource(path = "RepoMergeTestData.xml")
public final class TestMessageConstants2 {
  /**
   * Constant for XML file that contains all messages that are defined within this class.
   */
  private static final String MESSAGE_RESOURCE = "RepoMergeTestData.xml";

  /**
   * Info message for ID 20015.
   */
  public static final MessageID INFO_MESSAGE_20015;
  /**
   * Static initializer contains initialization for all generated constants.
   */
  static {
    MessageRepository lRepository = XFun.getMessageRepository();
    lRepository.loadResource(MESSAGE_RESOURCE);
    // Handle all info messages.
    INFO_MESSAGE_20015 = lRepository.getMessageID(20015);
    // Handle all messages for errors.
    // Handle all localized strings.
  }

  /**
   * Constructor is private to ensure that no instances of this class will be created.
   */
  private TestMessageConstants2( ) {
    // Nothing to do.
  }
}