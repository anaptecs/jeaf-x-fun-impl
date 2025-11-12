/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.principal;

import com.anaptecs.jeaf.xfun.api.principal.PrincipalProvider;
import com.anaptecs.jeaf.xfun.api.principal.PrincipalProviderFactory;

public class PrinciaplProviderFactoryImpl implements PrincipalProviderFactory {
  private final PrincipalProvider principalProvider = new DefaultPrincipalProvider();

  @Override
  public PrincipalProvider getPrincipalProvider( ) {
    return principalProvider;
  }
}
