/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.checks;

import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.FailureMessage;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.impl.checks.VerifierImpl;

/**
 * Class extends JEAF's basic verifier implementation in order to perform tests of methods that are offered to sub
 * classes.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
class MyTestVerifier extends VerifierImpl {
  /**
   * Reference to only instance of this class.
   */
  private static final MyTestVerifier instance = new MyTestVerifier();

  /**
   * Method returns only instance of this class. Since this is a test class the method is only visible within this
   * class.
   * 
   * @return MyTestVerifier Only instance of this verifier implementation.
   */
  static MyTestVerifier getMyTestVerifier( ) {
    return instance;
  }

  /**
   * Method creates a new VerificationFailure object that describes which verification failed and why. This method uses
   * the method <code>createVerificationFailure(...)</code> from class AbtractVerifier and is only defined to make this
   * method accessable within the test cases of this class.
   * 
   * @param pMessageID ID of the message that describes the failed verification. The parameter must not be null.
   * @param pMessageParameters Message parameters that are used to parameterized a message string that describes the
   * failed verification. The parameter may be null.
   * @param pCause Throwable object that caused the verification to fail. The parameter may be null.
   * @return VerificationFailure Created object that describes the failed verification. The method never returns null.
   */
  FailureMessage createVerificationFailureObject( MessageID pMessageID, String[] pMessageParameters,
      Throwable pCause ) {
    return this.createFailureMessage(pMessageID, pMessageParameters, pCause);
  }

  /**
   * Method creates a new VerificationFailure object that describes which verification failed and why. This method uses
   * the method <code>createVerificationFailure(...)</code> from class AbtractVerifier and is only defined to make this
   * method accessable within the test cases of this class.
   * 
   * @param pErrorCode Error code that describes the failed verification. The parameter must not be null.
   * @param pMessageParameters Message parameters that are used to parameterized a message string that describes the
   * failed verification. The parameter may be null.
   * @param pCause Throwable object that caused the verification to fail. The parameter may be null.
   * @return VerificationFailure Created object that describes the failed verification. The method never returns null.
   */
  FailureMessage createVerificationFailureObject( ErrorCode pErrorCode, String[] pMessageParameters,
      Throwable pCause ) {
    return this.createFailureMessage(pErrorCode, pMessageParameters, pCause);
  }
}