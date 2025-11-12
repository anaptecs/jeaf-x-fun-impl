/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.checks;

import java.util.Collection;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.anaptecs.jeaf.tools.api.Tools;
import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.checks.Verifier;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;

/**
 * Class implements all checks that can be performed through assert and check methods. The implementation of these
 * verifications is extracted to this class in order to avoid implementing the same behavior twice. Due to this fact the
 * methods of this class only implement the verification but the reaction on an occurred problem has to be implemented
 * by the caller. To indicate that a check failed all methods return a not null object of class FailureMessage
 * containing an error message describing why the verification failed.
 * 
 * Besides the provided verification methods it is also possible to provide additional verifications by creating a
 * subclass of this class that offers additional verify methods.
 * 
 * @author JEAF Development Team
 * @version 1.0
 * 
 * @see com.anaptecs.jeaf.xfun.api.checks.Assert
 * @see com.anaptecs.jeaf.xfun.api.checks.Check
 */
public class VerifierImpl implements Verifier {
  /**
   * Error code whose message id is used as message id for verification failures of method
   * <code>verifyIsNotNull(..)</code>. The attribute must not be accessed directly, but by using the corresponding
   * method <code>getErrorCodeForVerificationIsNotNull()</code>.
   * 
   * <br/>
   * In order to avoid circular references this class can not use regular constants for JEAF's message repository since
   * the message repository itself uses check and assert methods which indirectly call the methods of this class.
   * 
   * @see #getErrorCodeForVerificationIsNotNull()
   */
  private static ErrorCode verificationIsNotNullErrorCode;

  /**
   * Error code whose message id is used as message id for verification failures of method
   * <code>verifyIsZeroOrGreater(..)</code>. The attribute must not be accessed directly, but by using the corresponding
   * method <code>getErrorCodeForVerificationIsZeroOrGreater()</code>.
   * 
   * <br/>
   * In order to avoid circular references a generated constant for JEAF's message repository can not be used for this
   * verification since the message repository itself indirectly uses this verification by calling the corresponding
   * check and / or assert methods.
   * 
   * @see #getErrorCodeForVerificationIsZeroOrGreater()
   */
  private static ErrorCode verificationIsZeroOrGreaterErrorCode;

  /**
   * Method returns error code which is used for verification failures of method <code>verifyIsNotNull(..)</code>.
   * 
   * @return ErrorCode Error code that is used for failed verifications of method <code>verifyIsNotNull(..)</code>. The
   * method never returns null.
   */
  private static synchronized ErrorCode getErrorCodeForVerificationIsNotNull( ) {
    // Create message id if it does not exist.
    if (verificationIsNotNullErrorCode == null) {
      verificationIsNotNullErrorCode = XFun.getMessageRepository().getErrorCode(1);
    }
    return verificationIsNotNullErrorCode;
  }

  /**
   * Method returns error code whih is used for verification failures of method <code>verifyIsZeroOrGreater(..)</code>.
   * 
   * @return ErrorCode Error code that is used for failed verifications of method <code>verifyIsZeroOrGreater(..)</code>
   * . The method never returns null.
   */
  private static synchronized ErrorCode getErrorCodeForVerificationIsZeroOrGreater( ) {
    // Create message id if it does not exist.
    if (verificationIsZeroOrGreaterErrorCode == null) {
      verificationIsZeroOrGreaterErrorCode = XFun.getMessageRepository().getErrorCode(2);
    }
    return verificationIsZeroOrGreaterErrorCode;
  }

  /**
   * Initialize object. Thereby no actions are performed.
   */
  public VerifierImpl( ) {
    // Nothing to do.
  }

  /**
   * Method creates a new VerificationFailure object that describes which verification failed and why.
   * 
   * @param pMessageID ID of the message that describes the failed verification. The parameter must not be null.
   * @param pMessageParameters Message parameters that are used to parameterized a message string that describes the
   * failed verification. The parameter may be null.
   * @return VerificationFailure Created object that describes the failed verification. The method never returns null.
   */
  protected final FailureMessage createVerificationFailure( MessageID pMessageID, String[] pMessageParameters ) {
    return new FailureMessage(pMessageID, pMessageParameters, null);
  }

  /**
   * Method creates a new VerificationFailure object that describes which verification failed and why.
   * 
   * @param pMessageID ID of the message that describes the failed verification. The parameter must not be null.
   * @param pMessageParameters Message parameters that are used to parameterized a message string that describes the
   * failed verification. The parameter may be null.
   * @param pCause Throwable object that caused the verification to fail. The parameter may be null.
   * @return VerificationFailure Created object that describes the failed verification. The method never returns null.
   */
  protected final FailureMessage createFailureMessage( MessageID pMessageID, String[] pMessageParameters,
      Throwable pCause ) {
    // Check message id for null.
    Check.checkInvalidParameterNull(pMessageID, "pMessageID");

    return new FailureMessage(pMessageID, pMessageParameters, pCause);
  }

  /**
   * Method verifies whether the passed object is null or not.
   * 
   * @param pObject Object that should be checked for null. If pObject is null an error is indicated.
   * @param pObjectName Name of the object that is checked for null. The parameter may be null.
   * @return VerificationFailure Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all error cases the method returns a VerificationFailure object.
   */
  @Override
  public final FailureMessage isNotNull( Object pObject, String pObjectName ) {
    // Check pObject for null.
    FailureMessage lVerificationFailure;
    if (pObject != null) {
      lVerificationFailure = null;
    }
    // An error message is returned if pObject is null.
    else {
      String[] lParams = new String[] { pObjectName };
      ErrorCode lErrorCode = VerifierImpl.getErrorCodeForVerificationIsNotNull();
      lVerificationFailure = this.createFailureMessage(lErrorCode, lParams, null);
    }
    // Return result of verification.
    return lVerificationFailure;
  }

  /**
   * Method verifies whether the passed parameter is is zero or greater or not.
   * 
   * @param pValue Value that should be check if it is zero or greater.
   * @param pValueName Name of the value that is checked. The parameter may be null.
   * @return VerificationFailure Verification failure describing the reason why the verification failed. If no error
   * occurred the method returns null. In all other cases the method returns a VerificationFailure object.
   */
  @Override
  public final FailureMessage isZeroOrGreater( int pValue, String pValueName ) {
    // Check if pValue is positive including zero.
    FailureMessage lVerificationFailure;
    if (pValue >= 0) {
      lVerificationFailure = null;
    }
    // An error message is returned if pValue is negative.
    else {
      String[] lParams = new String[] { pValueName, Integer.toString(pValue) };
      ErrorCode lErrorCode = VerifierImpl.getErrorCodeForVerificationIsZeroOrGreater();
      lVerificationFailure = this.createFailureMessage(lErrorCode, lParams, null);
    }
    // Return result of verification.
    return lVerificationFailure;
  }

  /**
   * Method verifies that the passed object is null.
   * 
   * @param pObject Object that should be checked for null. If pObject is not null an error is indicated.
   * @param pObjectName Name of the object that is checked for null. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all error cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isNull( Object pObject, String pObjectName ) {
    // Check pObject for null.
    FailureMessage lFailureMessage;
    if (pObject == null) {
      lFailureMessage = null;
    }
    // An error message is returned if pObject is null.
    else {
      String[] lParams = new String[] { pObjectName };
      lFailureMessage = this.createFailureMessage(XFunMessages.IS_NULL, lParams, null);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies if the passed condition is true.
   * 
   * @param pCondition Condition that should be check if it is true.
   * @param pDescription Description of the condition. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all error cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isTrue( boolean pCondition, String pDescription ) {
    // Check pObject for null.
    FailureMessage lFailureMessage;
    if (pCondition == true) {
      lFailureMessage = null;
    }
    // An error message is returned if pObject is null.
    else {
      String[] lParams = new String[] { pDescription };
      lFailureMessage = this.createFailureMessage(XFunMessages.NOT_TRUE, lParams, null);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies if the passed condition is false.
   * 
   * @param pCondition Condition that should be check if it is false.
   * @param pDescription Description of the condition. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all error cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isFalse( boolean pCondition, String pDescription ) {
    // Check pObject for null.
    FailureMessage lFailureMessage;
    if (pCondition == false) {
      lFailureMessage = null;
    }
    // An error message is returned if pObject is null.
    else {
      String[] lParams = new String[] { pDescription };
      lFailureMessage = this.createFailureMessage(XFunMessages.NOT_FALSE, lParams, null);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed string is a real string. A String object is a "real" string whenever it is not
   * null and its trimmed version consists of at least one character.
   * 
   * @param pString String object that should be checked if it is a real string. If pString is null an error is
   * indicated.
   * @param pStringName Name of the string that is checked. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isRealString( String pString, String pStringName ) {
    // Do verification.
    FailureMessage lFailureMessage = null;
    if (pString != null) {
      // Trim string to see if it is empty.
      String lTrimmedString = pString.trim();
      if (lTrimmedString.length() == 0) {
        String[] lParams = new String[] { pStringName };
        lFailureMessage = this.createVerificationFailure(XFunMessages.STRING_IS_EMPTY, lParams);
      }
    }
    // String is null.
    else {
      String[] lParams = new String[] { pStringName };
      lFailureMessage = this.createVerificationFailure(XFunMessages.NULL_IS_NOT_A_REAL_STRING, lParams);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed string is not longer than the passed maximum length.
   * 
   * @param pString String object that should be checked for its length. The parameter must not be null.
   * @param pMaxLenght Maximum length the string may have.
   * @param pStringName Name of the string that is checked. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyMaxStringLength( String pString, int pMaxLenght, String pStringName ) {
    // Check parameters.
    Check.checkInvalidParameterNull(pString, "pString");

    // Check length of string.
    FailureMessage lFailureMessage = null;
    if (pString.length() > pMaxLenght) {
      String[] lParams = new String[] { pString, Integer.toString(pMaxLenght) };
      lFailureMessage = this.createVerificationFailure(XFunMessages.STRING_TOO_LONG, lParams);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed parameters define a valid set. A set is valid if the lower bound is less or
   * equal to the upper bound. In all other cases the verification fails.
   * 
   * @param pLowerBound Lower bound of the set.
   * @param pUpperBound Upper bound of the set.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isValidSet( int pLowerBound, int pUpperBound ) {
    // Set is valid if lower bound is less or equal to upper bound.
    FailureMessage lFailureMessage;
    if (pLowerBound <= pUpperBound) {
      lFailureMessage = null;
    }
    // Set is invalid.
    else {
      String[] lParams = new String[] { Integer.toString(pLowerBound), Integer.toString(pUpperBound) };
      ErrorCode lErrorCode = XFunMessages.SET_IS_INVALID;
      lFailureMessage = this.createVerificationFailure(lErrorCode, lParams);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed value is part of the passed set. The verification fails if the passed value is
   * not within the passed set or if the lower and upper bound do not define a valid set.
   * 
   * @param pLowerBound Lower bound of the set. The parameter must define a valid set together with pUpperBound.
   * @param pUpperBound Upper bound of the set. The parameter must define a valid set together with pLowerBound.
   * @param pValue Value for which is verified if it is part of the defined set.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isPartOfSet( int pLowerBound, int pUpperBound, int pValue ) {
    // Check if lower and upper bound define a valid set.
    FailureMessage lFailureMessage = this.isValidSet(pLowerBound, pUpperBound);

    // Defined set is valid.
    if (lFailureMessage == null) {
      // pValue must be greater or equal to the lower bound and less or equal to the upper bound.
      if (pLowerBound <= pValue && pValue <= pUpperBound) {
        lFailureMessage = null;
      }
      // pValue is not within the set.
      else {
        String[] lParams;
        lParams =
            new String[] { Integer.toString(pValue), Integer.toString(pLowerBound), Integer.toString(pUpperBound) };
        lFailureMessage = this.createVerificationFailure(XFunMessages.VALUE_IS_NOT_PART_OF_SET, lParams);
      }
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed value is not part of the passed set. The verification fails if the passed value
   * is within the passed set or if the lower and upper bound do not define a valid set.
   * 
   * @param pLowerBound Lower bound of the set. The parameter must define a valid set together with pUpperBound.
   * @param pUpperBound Upper bound of the set. The parameter must define a valid set together with pLowerBound.
   * @param pValue Value for which is verified if it is not part of the defined set.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isNotPartOfSet( int pLowerBound, int pUpperBound, int pValue ) {
    // Check if lower and upper bound define a valid set.
    FailureMessage lFailureMessage = this.isValidSet(pLowerBound, pUpperBound);

    // Defined set is valid.
    if (lFailureMessage == null) {
      // pValue must be less than the lower bound or greater than the upper bound.
      if (pValue < pLowerBound || pValue > pUpperBound) {
        lFailureMessage = null;
      }
      // pValue is not within the set.
      else {
        String[] lParams;
        lParams =
            new String[] { Integer.toString(pValue), Integer.toString(pLowerBound), Integer.toString(pUpperBound) };
        lFailureMessage = this.createVerificationFailure(XFunMessages.VALUE_IS_PART_OF_SET, lParams);
      }
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed set is a subset. The verification fails if the passed values are not a subset of
   * the passed set or if the the passed values do not define valid sets.
   * 
   * @param pLowerBoundSet Lower bound of the set. The parameter must define a valid set together with pUpperBoundSet.
   * @param pUpperBoundSet Upper bound of the set. The parameter must define a valid set together with pLowerBoundSet.
   * @param pLowerBoundSubset Lower bound of the subset. The parameter must define a valid set together with
   * pUpperBoundSubset.
   * @param pUpperBoundSubset Upper bound of the subset. The parameter must define a valid set together with
   * pLowerBoundSubset.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage isSubset( int pLowerBoundSet, int pUpperBoundSet, int pLowerBoundSubset,
      int pUpperBoundSubset ) {
    // Check if the passed parameters define valid outer set.
    FailureMessage lFailureMessage = this.isValidSet(pLowerBoundSet, pUpperBoundSet);

    // Outer set is valid.
    if (lFailureMessage == null) {
      // Check if passed parameters define valid subset.
      lFailureMessage = this.isValidSet(pLowerBoundSubset, pUpperBoundSubset);

      // Subset is also valid.
      if (lFailureMessage == null) {
        // To be a real subset of the outer set the lower and the upper bound must be part of the outer set. Since we
        // already know the both sets are valid it is sufficient to compare only the lower and upper bounds with each
        // other.
        if (pLowerBoundSubset < pLowerBoundSet || pUpperBoundSubset > pUpperBoundSet) {
          String[] lParams;
          lParams = new String[] { Integer.toString(pLowerBoundSet), Integer.toString(pUpperBoundSet),
            Integer.toString(pLowerBoundSubset), Integer.toString(pUpperBoundSubset) };
          lFailureMessage = this.createVerificationFailure(XFunMessages.INVALID_SUBSET, lParams);
        }
      }
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the first and the second set have a not null intersection. The verification fails if the
   * two sets do not have a not null intersection or if one of the sets is not a valid set.
   * 
   * @param pLowerBoundFirstSet Lower bound of the first set. The parameter must define a valid set together with
   * pUpperBoundFirstSet.
   * @param pUpperBoundFirstSet Upper bound of the first set. The parameter must define a valid set together with
   * pLowerBoundFirstSet.
   * @param pLowerBoundSecondSet Lower bound of the second set. The parameter must define a valid set together with
   * pUpperBoundSecondSet.
   * @param pUpperBoundSecondSet Upper bound of the second set. The parameter must define a valid set together with
   * pLowerBoundSecondSet.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage hasIntersection( int pLowerBoundFirstSet, int pUpperBoundFirstSet,
      int pLowerBoundSecondSet, int pUpperBoundSecondSet ) {
    // Check if the parameters for the first set define a valid set.
    FailureMessage lFailureMessage = this.isValidSet(pLowerBoundFirstSet, pUpperBoundFirstSet);
    if (lFailureMessage == null) {
      // Check if the parameters for the second set define a valid set.
      lFailureMessage = this.isValidSet(pLowerBoundSecondSet, pUpperBoundSecondSet);
      if (lFailureMessage == null) {
        // Since we already know that both sets are valid we only have to check if the upper bound of the first set is
        // less than the lower bound of the second set and vice versa.
        if (pUpperBoundFirstSet < pLowerBoundSecondSet || pUpperBoundSecondSet < pLowerBoundFirstSet) {
          String[] lParams;
          lParams = new String[] { Integer.toString(pLowerBoundFirstSet), Integer.toString(pUpperBoundFirstSet),
            Integer.toString(pLowerBoundSecondSet), Integer.toString(pUpperBoundSecondSet) };
          lFailureMessage = this.createVerificationFailure(XFunMessages.SETS_HAVE_NO_INTERSECTION, lParams);
        }
      }
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the first and the second set have a empty intersection. The verification fails if the two
   * sets do not have a empty intersection or if one of the sets is not a valid set.
   * 
   * @param pLowerBoundFirstSet Lower bound of the first set. The parameter must define a valid set together with
   * pUpperBoundFirstSet.
   * @param pUpperBoundFirstSet Upper bound of the first set. The parameter must define a valid set together with
   * pLowerBoundFirstSet.
   * @param pLowerBoundSecondSet Lower bound of the second set. The parameter must define a valid set together with
   * pUpperBoundSecondSet.
   * @param pUpperBoundSecondSet Upper bound of the second set. The parameter must define a valid set together with
   * pLowerBoundSecondSet.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage hasEmptyIntersection( int pLowerBoundFirstSet, int pUpperBoundFirstSet,
      int pLowerBoundSecondSet, int pUpperBoundSecondSet ) {
    // Check if the parameters for the first set define a valid set.
    FailureMessage lFailureMessage = this.isValidSet(pLowerBoundFirstSet, pUpperBoundFirstSet);
    if (lFailureMessage == null) {
      // Check if the parameters for the second set define a valid set.
      lFailureMessage = this.isValidSet(pLowerBoundSecondSet, pUpperBoundSecondSet);
      if (lFailureMessage == null) {
        // Since we already know that both sets are valid we only have to check
        if (pUpperBoundFirstSet < pLowerBoundSecondSet || pUpperBoundSecondSet < pLowerBoundFirstSet) {
          lFailureMessage = null;
        }
        // Set do overlap
        else {
          String[] lParams;
          lParams = new String[] { Integer.toString(pLowerBoundFirstSet), Integer.toString(pUpperBoundFirstSet),
            Integer.toString(pLowerBoundSecondSet), Integer.toString(pUpperBoundSecondSet) };
          lFailureMessage = this.createVerificationFailure(XFunMessages.SETS_HAVE_NO_INTERSECTION, lParams);
        }
      }
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed character sequence matches with the passed pattern.
   * 
   * @param pCharacters Character sequence to verify. The parameter must not be null.
   * @param pPattern Regular expression pattern which will be used to verify the passed character sequence. The
   * parameter must not be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyPattern( String pCharacters, String pPattern ) {
    // Check parameters
    Check.checkInvalidParameterNull(pCharacters, "pCharacters");
    Check.checkInvalidParameterNull(pPattern, "pPattern");

    // Perform pattern matching.
    final boolean lPatternFullfilled = Tools.getRegExpTools().matchesPattern(pCharacters, pPattern);

    FailureMessage lFailureMessage;
    if (lPatternFullfilled == true) {
      lFailureMessage = null;
    }
    else {
      String[] lParams = new String[] { pCharacters, pPattern };
      lFailureMessage = this.createFailureMessage(XFunMessages.PATTERN_NOT_MATCHED, lParams, null);
    }
    // Return result of verification.
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed collection has the passed minimum size.
   * 
   * @param pCollection Collection that should be verified for its minimum size. The parameter must not be null.
   * @param pMinimumSize Minimum size the passed collection must have. The amount of elements within the collection must
   * be equal or greater than this value.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyMinimumCollectionSize( Collection<?> pCollection, int pMinimumSize ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pCollection, "pCollection");

    FailureMessage lFailureMessage;
    if (pCollection.size() >= pMinimumSize) {
      lFailureMessage = null;
    }
    else {
      String[] lParams = new String[] { Integer.toString(pMinimumSize) };
      lFailureMessage = this.createVerificationFailure(XFunMessages.COLLECTION_DOES_NOT_HAVE_MIN_SIZE, lParams);
    }
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed collection has the passed minimum size.
   * 
   * @param pCollection Collection that should be verified for its maximum size. The parameter must not be null.
   * @param pMaximumSize Maximum size the passed collection must have. The amount of elements within the collection must
   * be less or equal than this value.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyMaximumCollectionSize( Collection<?> pCollection, int pMaximumSize ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pCollection, "pCollection");

    FailureMessage lFailureMessage;
    if (pCollection.size() <= pMaximumSize) {
      lFailureMessage = null;
    }
    else {
      String[] lParams = new String[] { Integer.toString(pMaximumSize) };
      lFailureMessage = this.createVerificationFailure(XFunMessages.COLLECTION_EXCEEDS_MAX_SIZE, lParams);
    }
    return lFailureMessage;
  }

  /**
   * Method verifies whether the passed start and end dates define a valid period. The verification is fulfilled if the
   * start date is before the end date.
   * 
   * @param pStart Start of the period. The parameter may be null.
   * @param pEnd End of the period. The parameter may be null.
   * @return FailureMessage Verification failure describing the reason why the verification failed. If no error occurred
   * the method returns null. In all other cases the method returns a FailureMessage object.
   */
  @Override
  public final FailureMessage verifyValidPeriod( Date pStart, Date pEnd ) {
    FailureMessage lFailureMessage;
    if (pStart == null || pEnd == null) {
      lFailureMessage = null;
    }
    else {
      if (pStart.before(pEnd) == true) {
        lFailureMessage = null;
      }
      else {
        DateTools lDateTools = DateTools.getDateTools();
        String[] lParams = new String[] { lDateTools.toTimestampString(pStart), lDateTools.toDateString(pEnd) };
        lFailureMessage = this.createFailureMessage(XFunMessages.INVALID_PERIOD, lParams, null);
      }
    }
    return lFailureMessage;
  }

  @Override
  public final FailureMessage verifyEMailAddress( String pEMailAddress ) {
    // E-Mail is not null
    FailureMessage lFailureMessage;
    if (pEMailAddress != null) {
      try {
        InternetAddress lEmailAddress = new InternetAddress(pEMailAddress);
        lEmailAddress.validate();
        lFailureMessage = null;
      }
      catch (AddressException e) {
        lFailureMessage = this.createFailureMessage(XFunMessages.INVALID_EMAIL_ADDRESS, null, e);
      }
    }
    // null is not a valid E-Mail address
    else {
      lFailureMessage = this.createFailureMessage(XFunMessages.INVALID_EMAIL_ADDRESS, null, null);
    }

    return lFailureMessage;
  }
}
