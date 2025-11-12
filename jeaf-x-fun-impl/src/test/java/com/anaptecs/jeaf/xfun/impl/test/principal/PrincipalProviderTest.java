/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.principal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.principal.PrincipalProvider;
import com.anaptecs.jeaf.xfun.impl.principal.DefaultPrincipalProvider;
import com.anaptecs.jeaf.xfun.impl.principal.PrinciaplProviderFactoryImpl;

public class PrincipalProviderTest {
  @Test
  public void testPrincipalProviderConfiguration( ) {
    PrincipalProvider lPrincipalProvider = XFun.getPrincipalProvider();
    assertEquals(TestPrincipalProvider.class, lPrincipalProvider.getClass());
  }

  @Test
  public void testDefaultPrincipalProvider( ) {
    PrinciaplProviderFactoryImpl lFactory = new PrinciaplProviderFactoryImpl();
    DefaultPrincipalProvider lDefaultPrincipalProvider = (DefaultPrincipalProvider) lFactory.getPrincipalProvider();

    Principal lCurrentPrincipal = lDefaultPrincipalProvider.getCurrentPrincipal();
    assertEquals(System.getProperty("user.name"), lCurrentPrincipal.getName());
  }
}
