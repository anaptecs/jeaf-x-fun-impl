/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.checks;

import com.anaptecs.jeaf.xfun.api.checks.Verifier;
import com.anaptecs.jeaf.xfun.api.checks.VerifierFactory;

public class VerifierFactoryImpl implements VerifierFactory {
  private final Verifier verifier = new VerifierImpl();

  @Override
  public Verifier getVerifier( ) {
    return verifier;
  }
}
