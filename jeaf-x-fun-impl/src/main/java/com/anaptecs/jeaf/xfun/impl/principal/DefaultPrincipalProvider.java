/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.principal;

import java.security.Principal;

import com.anaptecs.jeaf.xfun.api.principal.PrincipalProvider;

/**
 * Default implementation of a principal provider. This implementation will be used, if no other principal provider is
 * configured within the JEAF properties.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public class DefaultPrincipalProvider implements PrincipalProvider {
  /**
   * The default principal represents the user under which this JVM is run.
   */
  private static final Principal DEFAULT_PRINCIPAL = new SimplePrincipal();

  /**
   * Method returns the current principal object. This means for this implementation that a principal object will be
   * returned that represents the user under which this JVM is run.
   * 
   * @return {@link Principal} Current principal object. The method never returns null.
   */
  public Principal getCurrentPrincipal( ) {
    return DEFAULT_PRINCIPAL;
  }
}
