/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.checks;

import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.checks.VerificationResult;
import com.anaptecs.jeaf.xfun.api.trace.ObjectFormatter;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

@TraceObjectFormatter(supportedClasses = VerificationResult.class)
public class VerificationResultFormatter implements ObjectFormatter<VerificationResult> {

  @Override
  public String formatObject( VerificationResult pVerificationResultObject, TraceLevel pTraceLevel ) {
    // Check parameter
    Check.checkInvalidParameterNull(pVerificationResultObject, "pVerificationResultObject");

    return pVerificationResultObject.getMessage();
  }
}
