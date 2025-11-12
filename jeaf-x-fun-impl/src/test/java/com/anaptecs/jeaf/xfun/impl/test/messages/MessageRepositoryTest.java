/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import com.anaptecs.jeaf.junit.tools.MessageConstantTest;
import com.anaptecs.jeaf.tools.api.ToolsMessages;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.errorhandling.ApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.ExceptionInfo;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFApplicationException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedObject;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import com.anaptecs.jeaf.xfun.api.messages.MessageDefinition;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.impl.messages.MessageRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * JUnit test class for MessageRepositoryImpl implementation.
 *
 * @author JEAF Development Team
 * @version 1.0
 */
public class MessageRepositoryTest {
  /**
   * String constants defines the message resource that is used for the test cases within this class.
   */
  private static final String MESSAGE_RESOURCE = "TestMessageData.xml";

  /**
   * String constant defines a message resource that references a xml file that causes an exception during the parsing
   * process.
   */
  private static final String BROKEN_MESSAGE_RESOURCE = "BrokenTestMessageData.xml";

  /**
   * Test of method getInstance() of message repository.
   */
  @Test
  public void testGetInstance( ) {
    // getInstance() method must not return null.
    String lMessage = "Method getInstance() must not return null.";
    assertNotNull(XFun.getMessageRepository(), lMessage);
  }

  /**
   * Test of method loadMessageResource(...) of message repository.
   *
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  @Test
  public void testLoadMessageResource( ) throws SAXException, IOException, ParserConfigurationException {
    // Get message repository.
    MessageRepository lRepository = XFun.getMessageRepository();

    // Load test resource the first time.
    lRepository.loadResource(MESSAGE_RESOURCE);

    // Test settings for message id 4711
    ErrorCode lID_20011 = lRepository.getErrorCode(20011);
    assertNotNull(lID_20011);

    ApplicationException lApplicationException = new JEAFApplicationException(lID_20011);
    assertEquals(lID_20011, lApplicationException.getErrorCode(), "ExceptionID stimmt nicht ueberein.");
    assertEquals(null, lApplicationException.getMessageParameters(), "Message-Parameter sind nicht null.");
    assertEquals(null, lApplicationException.getCause(), "Exception-Ursache ist nicht null.");
    String lMessage = lApplicationException.getMessage();
    String lExpectedMessage = "[" + ExceptionInfo.ERROR + " " + lID_20011.getErrorCodeValue() + "] Test 1";
    assertEquals(lExpectedMessage, lMessage, "Fehlermeldung stimmt nicht");

    // Test settings for message id 4712.
    ErrorCode lID_20012 = lRepository.getErrorCode(20012);
    assertNotNull(lID_20012);

    String[] lParams = new String[] { "pObject" };
    SystemException lException = new JEAFSystemException(lID_20012, lApplicationException, lParams);
    assertEquals(lID_20012, lException.getErrorCode(), "ExceptionID stimmt nicht ueberein.");
    assertTrue(Arrays.equals(lParams, lException.getMessageParameters()), "Message-Parameter stimmen nicht ueberein.");
    assertEquals(lApplicationException, lException.getCause(), "Exception-Ursache stimmt nicht ueberein.");
    String lString =
        "[" + ExceptionInfo.ERROR + " " + lID_20012.getErrorCodeValue() + "] Parameter 'pObject' must not be null.";
    assertEquals(lString, lException.getMessage(), "Fehlermeldung stimmt nicht.");

    // Load the same message resource the second time. Nothing must happen.
    lRepository.loadResource(MESSAGE_RESOURCE);

    // Try to load message resource that does not exist.
    try {
      lRepository.loadResource(MESSAGE_RESOURCE + "unknown");
      fail("When trying to load unknown message resource exception must occur.");
    }
    catch (SystemException e) {
      // Check if the right exception occurred.
      assertEquals(MessageRepositoryImpl.UNABLE_TO_PARSE_XML_FILE, e.getErrorCode(),
          "Exception has wrong exception id.");
      assertNotNull(e.getCause());
    }

    try {
      lRepository.loadResource(BROKEN_MESSAGE_RESOURCE);
      fail("When trying to load unknown message resource exception must occur.");
    }
    catch (JEAFSystemException e) {
      // Check if the right exception occurred.
      assertEquals(ToolsMessages.UNABLE_TO_PARSE_XML_FILE, e.getErrorCode(), "Exception has wrong exception id.");
      assertNotNull(e.getCause());
    }

    // Test handling of duplicate message ids. This must cause an exception.
    try {
      lRepository.loadResource("DuplicateTestMessageData.xml");
      fail("Loading a message resource with an anlready used message code must cause an exception.");
    }
    catch (SystemException e) {
      // Check error code
      assertEquals(MessageRepositoryImpl.LOCALIZATION_ID_ALREADY_IN_USE, e.getErrorCode(), "Wrong error code");
    }

    // Test handling of same locale multiple times within one message
    try {
      lRepository.loadResource("DuplicateLocaleMessageData.xml");
      fail("Exception expected.");
    }
    catch (JEAFSystemException e) {
      assertEquals(MessageRepositoryImpl.MESSAGE_ID_ALREADY_USED_TO_IDENTIFY_MESSAGE, e.getErrorCode());
    }

    try {
      lRepository.loadResource("InvalidDefaultMessageFormat.xml");
      fail("Exception expected.");
    }
    catch (JEAFSystemException e) {
      assertEquals(MessageRepositoryImpl.INVALID_MESSAGE_FORMAT, e.getErrorCode());
    }

    try {
      lRepository.loadResource("InvalidLocalizedMessageFormat.xml");
      fail("Exception expected.");
    }
    catch (JEAFSystemException e) {
      assertEquals(MessageRepositoryImpl.INVALID_MESSAGE_FORMAT, e.getErrorCode());
    }
  }

  /**
   * Method tests the support for localized messages.
   */
  @Test
  public void testLocalizedMessages( ) {
    // Test settings for message id 1212
    MessageRepository lMessageRepository = XFun.getMessageRepository();
    ErrorCode lID_1212 = lMessageRepository.getErrorCode(1212);
    assertNotNull(lID_1212);

    // Set default locale to United Kingdom during this test case.
    Locale lOldLocale = Locale.getDefault();
    try {
      Locale lDefaultLocale = Locale.UK;
      Locale.setDefault(lDefaultLocale);

      // Create Locale objects that will be used by the test.
      String lLocaleSuffix_de = "(de)";

      Locale lLocale_de_DE_JEAF = new Locale("de", "DE", "JEAF");
      String lLocaleSuffix_de_DE_JEAF = "(de_DE_JEAF)";

      Locale lLocale_de_CH = new Locale("de", "CH");
      String lLocaleSuffix_de_CH = "(de_CH)";

      String lLocaleSuffix_en_GB = "(en_GB)";

      // Get locale specific message text. Since the message repository and its messages are not directly accessible
      // using
      // an ErrorCode object an indirection via an application exception has to be used for testing.
      ApplicationException lApplicationException = new JEAFApplicationException(lID_1212);
      final String lAssertMessage = "Message text does not end with ";
      String lMessageText;

      // Get message text for Switzerland.
      Locale.setDefault(lLocale_de_CH);
      lMessageText = lApplicationException.getMessage();
      assertNotNull(lMessageText, "Message must not be null.");
      assertTrue(lMessageText.endsWith(lLocaleSuffix_de_CH), lAssertMessage + lLocaleSuffix_de_CH);

      // Get message text for unknown variant in Switzerland.
      Locale.setDefault(new Locale("de", "CH", "unknown"));
      lMessageText = lApplicationException.getMessage();
      assertNotNull("Message must not be null.", lMessageText);
      assertTrue(lMessageText.endsWith(lLocaleSuffix_de_CH), lAssertMessage + lLocaleSuffix_de_CH);

      // Get German message text for unknown variant and country.
      Locale.setDefault(new Locale("de", "AU", "unknown"));
      lMessageText = lApplicationException.getMessage();
      assertNotNull(lMessageText, "Message must not be null.");
      assertTrue(lMessageText.endsWith(lLocaleSuffix_de), lAssertMessage + lLocaleSuffix_de);

      // Get Chinese message text. Since no message for the Chinese locale are available, the messages for the default
      // locale have to be used.
      Locale.setDefault(Locale.CHINA);
      lMessageText = lApplicationException.getMessage();
      assertNotNull(lMessageText, "Message must not be null.");
      assertTrue(lMessageText.endsWith("(default)"), lAssertMessage + lLocaleSuffix_en_GB);

      // Get German message for variant JEAF.
      Locale.setDefault(lLocale_de_DE_JEAF);
      lMessageText = lApplicationException.getMessage();
      assertNotNull(lMessageText, "Message must not be null.");
      assertTrue(lMessageText.endsWith(lLocaleSuffix_de_DE_JEAF), lAssertMessage + lLocaleSuffix_de_DE_JEAF);

      // Get message for locale with a specific variant but no country set.
      Locale.setDefault(new Locale("de", "", "JEAF"));
      lMessageText = lApplicationException.getMessage();
      assertNotNull(lMessageText, "Message must not be null.");
      assertTrue(lMessageText.endsWith(lLocaleSuffix_de), lAssertMessage + lLocaleSuffix_de);

      // Change current default locale to France and perform further tests.
      lDefaultLocale = Locale.FRANCE;
      Locale.setDefault(lDefaultLocale);

      // Get message text for China again. Since the default locale changed to France and no messages for France are
      // available the default message text has to be returned.
      String lDefaultTextSuffix = "(default)";
      Locale.setDefault(Locale.CHINA);
      lMessageText = lApplicationException.getMessage();
      assertNotNull(lMessageText, "Message must not be null.");
      assertTrue(lMessageText.endsWith(lDefaultTextSuffix), lAssertMessage + lDefaultTextSuffix);
    }
    // Restore previous default locale.
    finally {
      Locale.setDefault(lOldLocale);
    }
  }

  /**
   * Method test the merging of one message repository into another.
   */
  @Test
  public void testMergeMessageRepositories( ) {
    Locale lCurrentLocale = Locale.getDefault();

    // Get message repository.
    MessageRepository lRepository = XFun.getMessageRepository();

    // Load test resource the first time.
    lRepository.loadResource(MESSAGE_RESOURCE);

    // Test settings for message id 4711
    ErrorCode lID_1212 = lRepository.getErrorCode(1212);
    assertNotNull(lID_1212);

    ApplicationException lApplicationException = new JEAFApplicationException(lID_1212);
    assertEquals(lID_1212, lApplicationException.getErrorCode(), "ExceptionID stimmt nicht ueberein.");
    assertEquals(null, lApplicationException.getMessageParameters(), "Message-Parameter sind nicht null.");
    assertEquals(null, lApplicationException.getCause(), "Exception-Ursache ist nicht null.");
    String lExpectedMessage_1212 = "[" + ExceptionInfo.ERROR + " " + lID_1212.getErrorCodeValue()
        + "] Error message for ID 1212 (" + lCurrentLocale + ")";
    assertEquals(lExpectedMessage_1212, lApplicationException.getMessage(), "Fehlermeldung stimmt nicht");

    // Test settings for message id 20012.
    ErrorCode lID_20012 = lRepository.getErrorCode(20012);
    assertNotNull(lID_20012);

    // Create new message repository that will be merged with the existing one.
    MessageRepositoryImpl lNewMessageRepository = new MessageRepositoryImpl(new ArrayList<Element>(0));
    lNewMessageRepository.loadResource("RepoMergeTestData.xml");
    MessageID lMessageID_20015 = lNewMessageRepository.getMessageID(20015);
    String lExpectedMessage_20015 = "Info message for ID 20015 (" + lCurrentLocale + ")";
    assertEquals(lExpectedMessage_20015, lNewMessageRepository.getMessage(lMessageID_20015), "Wrong message");

    // Merge new message repository with existing one.
    lRepository.addAllMessages(lNewMessageRepository.getAllMessages());
    assertEquals(lExpectedMessage_20015, lRepository.getMessage(lMessageID_20015), "Wrong message");
    assertEquals(lExpectedMessage_1212, lApplicationException.getMessage(), "Fehlermeldung stimmt nicht");

    // Test adding additional messages
    Map<Locale, MessageFormat> lLocalizedMessages = new HashMap<>();
    lLocalizedMessages.put(Locale.ITALIAN, new MessageFormat("Ciao"));

    ErrorCode lLocalizedObject = new ErrorCode(123456789, TraceLevel.INFO);
    MessageDefinition lMessageDefinition =
        new MessageDefinition(lLocalizedObject, new MessageFormat("Hello"), lLocalizedMessages);
    List<MessageDefinition> lMessageDefinitions = new ArrayList<>();
    lMessageDefinitions.add(lMessageDefinition);
    lRepository.addAllMessages(lMessageDefinitions);
    assertEquals("Hello", lRepository.getMessage(lLocalizedObject, Locale.ENGLISH));
    assertEquals("Ciao", lRepository.getMessage(lLocalizedObject, Locale.ITALIAN));
  }

  /**
   * Method tests JEAF's implementation of class LocalizedString.
   *
   * @throws Exception if the test case fails.
   */
  @Test
  public void testLocalizedString( ) throws Exception {
    // All tests are run with locale for UK.
    Locale.setDefault(Locale.US);

    ErrorCode lErrorCode = MessageConstantTest.ERROR_MESSAGE_1212;
    MessageRepository lMessageRepository = XFun.getMessageRepository();
    String lErrorMessage = lMessageRepository.getMessage(lErrorCode, (String[]) null);
    assertEquals(lErrorMessage, "Error message for ID 1212 (en_US)");
    LocalizedString lLocalizedText = MessageConstantTest.LOCALIZED_TEXT;
    assertEquals("Localized text (en)", lLocalizedText.toString());
  }

  @Test
  public void testMessageAccess( ) {
    // Get message repository.
    MessageRepository lRepository = XFun.getMessageRepository();

    List<MessageDefinition> lAllMessages = lRepository.getAllMessages();
    Map<Integer, MessageDefinition> lMessages = new HashMap<>();
    for (MessageDefinition lMessageDefinition : lAllMessages) {
      lMessages.put(lMessageDefinition.getLocalizedObject().getLocalizationID(), lMessageDefinition);
    }

    MessageDefinition lMessageDefinition = lMessages.get(MessageConstantTest.ERROR_MESSAGE_1212.getLocalizationID());
    assertNotNull(lMessageDefinition);

    assertTrue(lRepository.existsMessage(MessageConstantTest.ERROR_MESSAGE_1212.getLocalizationID()));
    assertFalse(lRepository.existsMessage(4711));

    LocalizedObject lLocalizedObject =
        lRepository.getLocalizedObject(MessageConstantTest.ERROR_MESSAGE_1212.getErrorCodeValue());
    assertNotNull(lLocalizedObject);

    // Test error handling
    try {
      lRepository.getLocalizedObject(4711);
      fail("Exception expected.");
    }
    catch (JEAFSystemException e) {
      assertEquals(MessageRepositoryImpl.UNKNOWN_MESSAGE_CODE, e.getErrorCode());
    }
  }

  @Test
  public void testMessageTraces( ) {
    MessageRepositoryImpl lRepository = (MessageRepositoryImpl) XFun.getMessageRepository();

    assertFalse(lRepository.showCurrentUserInTraces());
    lRepository.setTraceLocale(Locale.ENGLISH);
    assertEquals(Locale.ENGLISH, lRepository.getTraceLocale());
    assertEquals("[ 1212]                      Error message for ID 1212 (en)",
        lRepository.getTraceMessage(MessageConstantTest.ERROR_MESSAGE_1212));

    lRepository.setShowCurrentUserInTraces(true);
    assertTrue(lRepository.showCurrentUserInTraces());
    assertEquals("[ 1212] Crashtest Dummy      Error message for ID 1212 (en)",
        lRepository.getTraceMessage(MessageConstantTest.ERROR_MESSAGE_1212));

    ErrorCode lFakeErrorCode = new ErrorCode(9999999, TraceLevel.INFO);
    assertEquals("[9999999] Crashtest Dummy      [ID-9999999] Unable to return real message. ID is unknown",
        lRepository.getTraceMessage(lFakeErrorCode));

  }
}
