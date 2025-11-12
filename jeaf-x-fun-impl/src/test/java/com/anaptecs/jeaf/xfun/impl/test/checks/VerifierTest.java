/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.anaptecs.jeaf.junit.JUnitMessages;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.checks.VerificationResult;
import com.anaptecs.jeaf.xfun.api.checks.Verifier;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.impl.checks.VerificationResultFormatter;
import org.junit.jupiter.api.Test;

/**
 * Class tests verify methods of class AbstractVerifier and Verifier from package com.anaptecs.jeaf.fwk.core.
 *
 * @author JEAF Development Team
 * @version 1.0
 */
public class VerifierTest {
  /**
   * Constant for prefix of error message that is used whenever no message text is defined in VerifierMessage.properties
   * for a specific verification.
   */
  public static final String NO_ERROR_MESSAGE = "No error message";

  /**
   * Method tests class <code>com.anaptecs.jeaf.fwk.core.VerificationFailure</code>. Since the methods to create new
   * VerificationFailure objects are only accessible to sub classes a special implementation of a verifier, the class
   * MyTestVerifier, is used to test these methods.
   */
  @Test
  public void testVerificationFailure( ) {
    final MyTestVerifier lTestVerifier = MyTestVerifier.getMyTestVerifier();
    // Create new VerificationFailure object.
    MessageID lMessageID = JUnitMessages.VERIFICATION_FAILURE_TEST_MESSAGE_ID;
    ErrorCode lErrorCode = JUnitMessages.VERIFICATION_FAILURE_TEST_ERROR_CODE;
    FailureMessage lVerificationFailure = lTestVerifier.createVerificationFailureObject(lMessageID, null, null);

    // Test get methods
    assertEquals(lMessageID, lVerificationFailure.getMessageID());
    assertNull(lVerificationFailure.getMessageParameters());
    assertNull(lVerificationFailure.getCause());
    String lMessage = lVerificationFailure.getMessage();
    assertNotNull(lMessage);

    // Create another VerificationFailure object.
    String[] lParams = new String[] { "Create another VerificationFailure object." };
    lVerificationFailure = lTestVerifier.createVerificationFailureObject(lMessageID, lParams, null);
    assertEquals(lMessageID, lVerificationFailure.getMessageID());
    assertEquals(lParams.length, lVerificationFailure.getMessageParameters().length);
    assertEquals(lParams[0], lVerificationFailure.getMessageParameters()[0]);
    assertNull(lVerificationFailure.getCause());

    // Create another VerificationFailure object.
    Throwable lThrowable = new RuntimeException();
    lVerificationFailure = lTestVerifier.createVerificationFailureObject(lErrorCode, lParams, lThrowable);
    assertEquals(lErrorCode.getErrorCodeValue(), lVerificationFailure.getMessageID().getLocalizationID());
    assertEquals(lParams.length, lVerificationFailure.getMessageParameters().length);
    assertEquals(lParams[0], lVerificationFailure.getMessageParameters()[0]);
    assertEquals(lThrowable, lVerificationFailure.getCause());

    // Try to create VerificationFailure with null as message id.
    try {
      lTestVerifier.createVerificationFailureObject((MessageID) null, lParams, lThrowable);
      fail("Expected InvalidParameterException to be thrown");
    }
    // Catch IllegalArgumentException. The thrown error must be instance of InvalidParameterException which is a sub
    // class of IllegalArgumentException.
    catch (IllegalArgumentException e) {
      assertEquals("com.anaptecs.jeaf.xfun.api.checks.InvalidParameterException", e.getClass().getName());
    }

    // Try to create VerificationFailure with null as error code.
    try {
      lTestVerifier.createVerificationFailureObject((ErrorCode) null, lParams, lThrowable);
      fail("Expected InvalidParameterException to be thrown");
    }
    // Catch IllegalArgumentException. The thrown error must be instance of InvalidParameterException which is a sub
    // class of IllegalArgumentException.
    catch (IllegalArgumentException e) {
      assertEquals("com.anaptecs.jeaf.xfun.api.checks.InvalidParameterException", e.getClass().getName());
    }
  }

  /**
   * Method test class <code>com.anaptecs.jeaf.fwk.base.VerificationResult</code>.
   */
  @Test
  public void testVerificationResult( ) {
    // Create new VerificationResult.
    VerificationResult lVerificationResult = new VerificationResult();
    assertFalse(lVerificationResult.containsErrors());
    assertFalse(lVerificationResult.containsWarnings());
    assertFalse(lVerificationResult.containsFailures());
    assertNotNull(lVerificationResult.getErrors());
    assertNotNull(lVerificationResult.getWarnings());

    String lMessage = lVerificationResult.getMessage();
    assertNotNull("Method VerificationReult.getMessage() must not return null.", lMessage);
    // System.out.println(lMessage);

    // Create verification failures.
    final MyTestVerifier lTestVerifier = MyTestVerifier.getMyTestVerifier();
    FailureMessage lVerificationError_1 =
        lTestVerifier.createVerificationFailureObject(JUnitMessages.CHECK_CONSTRAINTS_ERROR_1, null, null);
    FailureMessage lVerificationError_2 =
        lTestVerifier.createVerificationFailureObject(JUnitMessages.CHECK_CONSTRAINTS_ERROR_2, null, null);
    FailureMessage lVerificationError_3 =
        lTestVerifier.createVerificationFailureObject(JUnitMessages.CHECK_CONSTRAINTS_ERROR_3, null, null);
    FailureMessage lVerificationWarning_1 =
        lTestVerifier.createVerificationFailureObject(JUnitMessages.CHECK_CONSTRAINTS_WARNING_9, null, null);
    FailureMessage lVerificationWarning_2 =
        lTestVerifier.createVerificationFailureObject(JUnitMessages.CHECK_CONSTRAINTS_WARNING_8, null, null);
    FailureMessage lVerificationWarning_3 =
        lTestVerifier.createVerificationFailureObject(JUnitMessages.CHECK_CONSTRAINTS_WARNING_7, null, null);

    // Add warnings to new object.
    VerificationResult lVerificationResultWithWarnings = new VerificationResult();
    lVerificationResultWithWarnings.addWarning(lVerificationWarning_1);
    lVerificationResultWithWarnings.addWarning(lVerificationWarning_3);

    Collection<FailureMessage> lWarnings = lVerificationResultWithWarnings.getWarnings();
    assertEquals(2, lWarnings.size(), "Number of warnings incorrect.");
    assertEquals(0, lVerificationResultWithWarnings.getErrors().size(), "Number of errors incorrect.");
    assertTrue(lWarnings.contains(lVerificationWarning_3), "VerificationFailure 3 is missing");
    assertTrue(lWarnings.contains(lVerificationWarning_1), "VerificationFailure 1 is missing");
    lMessage = lVerificationResultWithWarnings.getMessage();
    assertNotNull("Method VerificationReult.getMessage() must not return null.", lMessage);
    // System.out.println(lMessage);

    // Add errors to new object.
    VerificationResult lVerificationResultWithErrors = new VerificationResult();
    lVerificationResultWithErrors.addError(lVerificationError_2);

    Collection<FailureMessage> lErrors = lVerificationResultWithErrors.getErrors();
    assertEquals(1, lErrors.size(), "Number of errors incorrect.");
    assertEquals(0, lVerificationResultWithErrors.getWarnings().size(), "Number of warnings incorrect.");
    assertTrue(lErrors.contains(lVerificationError_2), "VerificationError 2 is missing.");
    lMessage = lVerificationResultWithErrors.getMessage();
    assertNotNull("Method VerificationReult.getMessage() must not return null.", lMessage);
    // System.out.println(lMessage);

    // Merge verification results.
    VerificationResult lMergedVerificationResult = new VerificationResult();
    lMergedVerificationResult.addVerificationResult(lVerificationResultWithWarnings);
    lMergedVerificationResult.addVerificationResult(lVerificationResultWithErrors);
    lWarnings = lMergedVerificationResult.getWarnings();
    lErrors = lMergedVerificationResult.getErrors();
    assertEquals(2, lWarnings.size(), "Number of warnings incorrect.");
    assertEquals(1, lErrors.size(), "Number of errors incorrect.");
    assertTrue(lWarnings.contains(lVerificationWarning_3), "VerificationFailure 3 is missing");
    assertTrue(lWarnings.contains(lVerificationWarning_1), "VerificationFailure 1 is missing");
    assertTrue(lErrors.contains(lVerificationError_2), "VerificationError 2 is missing.");
    lMessage = lMergedVerificationResult.getMessage();
    assertNotNull("Method VerificationReult.getMessage() must not return null.", lMessage);

    // VerificationResultFormatter
    VerificationResultFormatter lFormatter = new VerificationResultFormatter();
    assertEquals(lMergedVerificationResult.getMessage(),
        lFormatter.formatObject(lMergedVerificationResult, TraceLevel.INFO));
    assertEquals(lMergedVerificationResult.getMessage(), lFormatter.formatObject(lMergedVerificationResult, null));

    try {
      lFormatter.formatObject(null, null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }

    // Create another verification result with errors and warnings.
    VerificationResult lAnotherVerificationResult = new VerificationResult();
    lAnotherVerificationResult.addError(lVerificationError_3);
    lAnotherVerificationResult.addError(lVerificationError_1);
    lAnotherVerificationResult.addWarning(lVerificationWarning_2);
    assertTrue(lAnotherVerificationResult.containsFailures());
    lMergedVerificationResult.addVerificationResult(lAnotherVerificationResult);

    // Test merged object again.
    lWarnings = lMergedVerificationResult.getWarnings();
    lErrors = lMergedVerificationResult.getErrors();
    assertEquals(3, lWarnings.size(), "Number of warnings incorrect.");
    assertEquals(3, lErrors.size(), "Number of errors incorrect.");
    assertTrue(lWarnings.contains(lVerificationWarning_1), "VerificationFailure 1 is missing");
    assertTrue(lWarnings.contains(lVerificationWarning_2), "VerificationFailure 2 is missing");
    assertTrue(lWarnings.contains(lVerificationWarning_3), "VerificationFailure 3 is missing");
    assertTrue(lErrors.contains(lVerificationError_1), "VerificationError 1 is missing.");
    assertTrue(lErrors.contains(lVerificationError_2), "VerificationError 2 is missing.");
    assertTrue(lErrors.contains(lVerificationError_3), "VerificationError 3 is missing.");
    lMessage = lMergedVerificationResult.getMessage();
    assertNotNull("Method VerificationReult.getMessage() must not return null.", lMessage);
    // System.out.println(lMessage);
  }

  /**
   * Method tests method <code>Verifier.getVerifier()</code>.
   */
  @Test
  public void testGetVerifier( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNotNull(lVerifier, "Method getVerifier() must not return null.");
  }

  /**
   * Method tests method <code>Verifier.verifyIsNotNull()</code>.
   */
  @Test
  public void testVerifyIsNotNull( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.isNotNull(this, "lVerifier"));
    FailureMessage lVerificationFailure = lVerifier.isNotNull(null, "lNull");
    assertNotNull(lVerificationFailure);
    assertFalse(lVerificationFailure.getMessage().startsWith(NO_ERROR_MESSAGE));
    assertNotNull(lVerifier.isNotNull(null, null));
  }

  /**
   * Method tests method <code>Verifier.verifyIsZeroOrGreater()</code>.
   */
  @Test
  public void testVerifyIsZeroOrGreater( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.isZeroOrGreater(0, "pIntParam"));
    assertNull(lVerifier.isZeroOrGreater(1, "pIntParam"));
    assertNull(lVerifier.isZeroOrGreater(5, "pIntParam"));
    assertNull(lVerifier.isZeroOrGreater(100, null));

    FailureMessage lVerificationFailure = lVerifier.isZeroOrGreater(-1, "pIntParam");
    assertNotNull(lVerificationFailure);
    assertFalse(lVerificationFailure.getMessage().startsWith(NO_ERROR_MESSAGE));
    assertEquals(2, lVerificationFailure.getMessageID().getLocalizationID());
    assertNotNull(lVerifier.isZeroOrGreater(-2, "pIntParam"));
    assertNotNull(lVerifier.isZeroOrGreater(-5, null));
    assertNotNull(lVerifier.isZeroOrGreater(-100, "pIntParam"));
  }

  /**
   * Method tests method <code>Verifier.verifyIsValidSet()</code>.
   */
  @Test
  public void testVerifyIsValidSet( ) {
    // Test with valid sets.
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.isValidSet(0, 1));
    assertNull(lVerifier.isValidSet(0, 10));
    assertNull(lVerifier.isValidSet(-1, 0));
    assertNull(lVerifier.isValidSet(0, 0));
    assertNull(lVerifier.isValidSet(1, 1));
    assertNull(lVerifier.isValidSet(4711, 4711));

    // Test with invalid sets.
    FailureMessage lVerificationFailure = lVerifier.isValidSet(0, -1);
    assertNotNull(lVerificationFailure);
    assertFalse(lVerificationFailure.getMessage().startsWith(NO_ERROR_MESSAGE));
    lVerificationFailure = lVerifier.isValidSet(-4711, -4712);
    assertNotNull(lVerificationFailure);
    lVerificationFailure = lVerifier.isValidSet(6, 5);
    assertNotNull(lVerificationFailure);
    lVerificationFailure = lVerifier.isValidSet(5, 1);
    String lMessage = lVerificationFailure.getMessage();
    assertNotNull(lMessage);
  }

  /**
   * Method tests method <code>Verifier.verifyIsPartOfSet()</code>.
   */
  @Test
  public void testVerifyIsPartOfSet( ) {
    // Test valid variations.
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.isPartOfSet(0, 9, 2));
    assertNull(lVerifier.isPartOfSet(123, 125, 124));
    assertNull(lVerifier.isPartOfSet(123, 129, 129));
    assertNull(lVerifier.isPartOfSet(-199, 9, 0));
    assertNull(lVerifier.isPartOfSet(57, 57, 57));
    assertNull(lVerifier.isPartOfSet(0, 9, 2));
    assertNull(lVerifier.isPartOfSet(-30, -9, -20));

    // Test invalid variations
    FailureMessage lVerificationFailure = lVerifier.isPartOfSet(0, 5, 6);
    assertNotNull(lVerificationFailure);
    assertFalse(lVerificationFailure.getMessage().startsWith(NO_ERROR_MESSAGE));
    lVerificationFailure = lVerifier.isPartOfSet(0, -5, 6);
    assertNotNull(lVerificationFailure);
    lVerificationFailure = lVerifier.isPartOfSet(0, 5, -1);
    assertNotNull(lVerificationFailure);
    lVerificationFailure = lVerifier.isPartOfSet(-30, -6, -5);
    assertNotNull(lVerificationFailure);
  }

  /**
   * Method tests method <code>Verifier.verifyIsRealString()</code>.
   */
  @Test
  public void testVerifyIsRealString( ) {
    // Test valid variations.
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.isRealString(" not an empty string \t\n   ", "Dummy string"));
    assertNull(lVerifier.isRealString(" n ", null));
    assertNull(lVerifier.isRealString("abc", null));
    assertNull(lVerifier.isRealString("1 ", "pName"));

    // Test invalid variations.
    int lErrorCodeValue = XFunMessages.STRING_IS_EMPTY.getErrorCodeValue();
    assertEquals(lErrorCodeValue, lVerifier.isRealString(" \t\n   ", null).getMessageID().getLocalizationID());
    assertEquals(lErrorCodeValue, lVerifier.isRealString(" ", "pName").getMessageID().getLocalizationID());
    assertEquals(lErrorCodeValue, lVerifier.isRealString("", "1234").getMessageID().getLocalizationID());
    lErrorCodeValue = XFunMessages.NULL_IS_NOT_A_REAL_STRING.getErrorCodeValue();
    assertEquals(lErrorCodeValue, lVerifier.isRealString(null, null).getMessageID().getLocalizationID());
    assertEquals(lErrorCodeValue, lVerifier.isRealString(null, "471-0815").getMessageID().getLocalizationID());
  }

  /**
   * Method tests method <code>Verifier.verifyMaxStringLength()</code>.
   */
  @Test
  public void testVerifyMaxStringLength( ) {
    // Get Verifier
    Verifier lVerifier = XFun.getVerifier();

    // Test correct variants.
    assertNull(lVerifier.verifyMaxStringLength("ABC", 4, null));
    assertNull(lVerifier.verifyMaxStringLength("", 1, null));
    assertNull(lVerifier.verifyMaxStringLength("A  ", 457, null));
    assertNull(lVerifier.verifyMaxStringLength("This is a very long string", 100, null));
    assertNull(lVerifier.verifyMaxStringLength("ABC", 3, "pString"));

    // Test incorrect variants.
    assertNotNull(lVerifier.verifyMaxStringLength("ABC", 2, null));
    assertNotNull(lVerifier.verifyMaxStringLength("ABCDEFGHIJK", 5, null));
    assertNotNull(lVerifier.verifyMaxStringLength("ABC", 0, null));
    assertNotNull(lVerifier.verifyMaxStringLength("", -1, null));
  }

  /**
   * Method test method <code>Verifier.verifyPattern(...)</code>.
   */
  @Test
  public void testVerifyPattern( ) {
    // Get Verifier
    Verifier lVerifier = XFun.getVerifier();

    // Create pattern
    String lPattern = "[#]*[0-9A-Z]+";

    assertNull(lVerifier.verifyPattern("##ABC", lPattern));
    assertNull(lVerifier.verifyPattern("ABC", lPattern));
    assertNull(lVerifier.verifyPattern("##ABC234", lPattern));
    assertNull(lVerifier.verifyPattern("232", lPattern));
    assertNull(lVerifier.verifyPattern("ABC22", lPattern));
    assertNotNull(lVerifier.verifyPattern("A#", lPattern));
    assertNotNull(lVerifier.verifyPattern("##AsC", lPattern));
    assertNotNull(lVerifier.verifyPattern("#22?2", lPattern));
    assertNotNull(lVerifier.verifyPattern("", lPattern));
  }

  @Test
  public void testVerfifyPeriod( ) {
    Date lNow = new Date();
    Date lBefore = new Date(lNow.getTime() - 1);
    Date lAfter = new Date(lNow.getTime() + 1);
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.verifyValidPeriod(lBefore, lNow));
    assertNull(lVerifier.verifyValidPeriod(lBefore, lAfter));
    assertNull(lVerifier.verifyValidPeriod(null, lNow));
    assertNull(lVerifier.verifyValidPeriod(lBefore, null));
    assertNull(lVerifier.verifyValidPeriod(null, lNow));

    // Test failure cases.
    assertNotNull(lVerifier.verifyValidPeriod(lBefore, lBefore));
    assertNotNull(lVerifier.verifyValidPeriod(lAfter, lNow));
    FailureMessage lFailure = lVerifier.verifyValidPeriod(lBefore, lBefore);
    assertEquals(XFunMessages.INVALID_PERIOD, lFailure.getMessageID());
  }

  @Test
  public void testVerifyEMailAddress( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.verifyEMailAddress("jeaf@anaptecs.de"));
    assertNull(lVerifier.verifyEMailAddress("a@b"));
    assertNull(lVerifier.verifyEMailAddress("jeaf@änäptecs.dü"));

    assertEquals(XFunMessages.INVALID_EMAIL_ADDRESS, lVerifier.verifyEMailAddress("a").getMessageID());
    assertEquals(XFunMessages.INVALID_EMAIL_ADDRESS, lVerifier.verifyEMailAddress("").getMessageID());
    assertEquals(XFunMessages.INVALID_EMAIL_ADDRESS, lVerifier.verifyEMailAddress(null).getMessageID());
  }

  @Test
  public void testVerifyMaxCollectionSize( ) {
    List<String> lList = new ArrayList<>();
    lList.add("Hello");
    lList.add("World");
    lList.add("!");

    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.verifyMaximumCollectionSize(lList, 3));
    assertNull(lVerifier.verifyMaximumCollectionSize(lList, 4));

    // Test negative cases
    assertEquals(XFunMessages.COLLECTION_EXCEEDS_MAX_SIZE,
        lVerifier.verifyMaximumCollectionSize(lList, 0).getMessageID());
    assertEquals(XFunMessages.COLLECTION_EXCEEDS_MAX_SIZE,
        lVerifier.verifyMaximumCollectionSize(lList, 2).getMessageID());
    assertEquals(XFunMessages.COLLECTION_EXCEEDS_MAX_SIZE,
        lVerifier.verifyMaximumCollectionSize(lList, -1).getMessageID());

    // Test exception handling
    try {
      lVerifier.verifyMaximumCollectionSize(null, 4);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testVerifyMinCollectionSize( ) {
    List<String> lList = new ArrayList<>();
    lList.add("Hello");
    lList.add("World");
    lList.add("!");

    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.verifyMinimumCollectionSize(lList, 3));
    assertNull(lVerifier.verifyMinimumCollectionSize(lList, 2));
    assertNull(lVerifier.verifyMinimumCollectionSize(lList, -1));

    // Test negative cases
    assertEquals(XFunMessages.COLLECTION_DOES_NOT_HAVE_MIN_SIZE,
        lVerifier.verifyMinimumCollectionSize(lList, 4).getMessageID());
    assertEquals(XFunMessages.COLLECTION_DOES_NOT_HAVE_MIN_SIZE,
        lVerifier.verifyMinimumCollectionSize(lList, 5).getMessageID());

    // Test exception handling
    try {
      lVerifier.verifyMinimumCollectionSize(null, 4);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testIsTrueFalse( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.isTrue(true, "My Message"));
    assertNull(lVerifier.isTrue(true, null));

    FailureMessage lFailure = lVerifier.isTrue(false, "My Message");
    assertEquals(XFunMessages.NOT_TRUE, lFailure.getMessageID());
    assertEquals("My Message", lFailure.getMessageParameters()[0]);
    assertEquals("My Message", lFailure.getMessage());

    lFailure = lVerifier.isTrue(false, null);
    assertEquals("null", lFailure.getMessage());

    assertNull(lVerifier.isFalse(false, "My Message"));
    assertNull(lVerifier.isFalse(false, null));

    lFailure = lVerifier.isFalse(true, "My Message");
    assertEquals(XFunMessages.NOT_FALSE, lFailure.getMessageID());
    assertEquals("My Message", lFailure.getMessageParameters()[0]);
    assertEquals("My Message", lFailure.getMessage());

    lFailure = lVerifier.isFalse(true, null);
    assertEquals("null", lFailure.getMessage());
  }

  @Test
  public void testIsSubset( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.isSubset(2, 10, 2, 8));
    assertNull(lVerifier.isSubset(2, 10, 2, 10));
    assertNull(lVerifier.isSubset(2, 10, 3, 4));

    // Test negative cases
    assertEquals(XFunMessages.INVALID_SUBSET, lVerifier.isSubset(0, 5, 3, 6).getMessageID());
    assertEquals(XFunMessages.INVALID_SUBSET, lVerifier.isSubset(1, 5, 0, 4).getMessageID());
    assertEquals(XFunMessages.INVALID_SUBSET, lVerifier.isSubset(1, 5, 0, 6).getMessageID());
    assertEquals(XFunMessages.SET_IS_INVALID, lVerifier.isSubset(1, 5, 3, 2).getMessageID());
    assertEquals(XFunMessages.SET_IS_INVALID, lVerifier.isSubset(1, -1, 1, 2).getMessageID());
  }

  @Test
  public void testIsNull( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.isNull(null, "Oooops"));
    FailureMessage lFailureMessage = lVerifier.isNull(lVerifier, "Oooops");
    assertEquals("Object 'Oooops' must be NULL.", lFailureMessage.getMessage());
    assertEquals(XFunMessages.IS_NULL, lFailureMessage.getMessageID());
  }

  @Test
  public void testIsNotPartOfSet( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.isNotPartOfSet(1, 10, 0));
    assertNull(lVerifier.isNotPartOfSet(1, 10, 11));
    assertNull(lVerifier.isNotPartOfSet(1, 10, -2));

    // Test negative cases.
    assertEquals(XFunMessages.VALUE_IS_PART_OF_SET, lVerifier.isNotPartOfSet(-1, 5, -1).getMessageID());
    assertEquals(XFunMessages.VALUE_IS_PART_OF_SET, lVerifier.isNotPartOfSet(-1, 5, 5).getMessageID());
  }

  @Test
  public void testHasIntersection( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.hasIntersection(0, 2, 1, 4));
    assertNull(lVerifier.hasIntersection(0, 2, 2, 4));
    assertNull(lVerifier.hasIntersection(0, 5, 2, 4));

    // Test negativ cases.
    assertEquals(XFunMessages.SETS_HAVE_NO_INTERSECTION, lVerifier.hasIntersection(-1, 5, 6, 7).getMessageID());
    assertEquals(XFunMessages.SETS_HAVE_NO_INTERSECTION, lVerifier.hasIntersection(1, 5, 8, 777).getMessageID());
    assertEquals(XFunMessages.SETS_HAVE_NO_INTERSECTION, lVerifier.hasIntersection(3, 5, 1, 2).getMessageID());
    assertEquals(XFunMessages.SET_IS_INVALID, lVerifier.hasIntersection(0, -10, 10, 11).getMessageID());
    assertEquals(XFunMessages.SET_IS_INVALID, lVerifier.hasIntersection(0, 5, 10, 7).getMessageID());
  }

  @Test
  public void testHasEmptyIntersection( ) {
    Verifier lVerifier = XFun.getVerifier();
    assertNull(lVerifier.hasEmptyIntersection(0, 1, 2, 3));
    assertNull(lVerifier.hasEmptyIntersection(0, 1, -2, -1));

    // Test negative cases
    assertEquals(XFunMessages.SETS_HAVE_NO_INTERSECTION, lVerifier.hasEmptyIntersection(0, 10, 10, 11).getMessageID());
    assertEquals(XFunMessages.SET_IS_INVALID, lVerifier.hasEmptyIntersection(0, -10, 10, 11).getMessageID());
    assertEquals(XFunMessages.SET_IS_INVALID, lVerifier.hasEmptyIntersection(0, 10, 3, 1).getMessageID());
  }
}