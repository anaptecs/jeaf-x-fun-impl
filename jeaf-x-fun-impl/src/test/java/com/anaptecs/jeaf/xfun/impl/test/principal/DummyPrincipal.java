/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.principal;

import java.security.Principal;

/**
 * This is a simple implementation of a principal. The class uses the name of the current user as provided by the JVM to
 * form a principal.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
class DummyPrincipal implements Principal {
  public static final String DUMMY_NAME = "Crashtest Dummy";

  /**
   * Initialize object.
   */
  DummyPrincipal( ) {
    // Nothing to do.
  }

  /**
   * Method returns the name of the current user as provided by the JVM system property "user.name".
   * 
   * @return String Login name of the current user. The method never returns null.
   * 
   * @see java.security.Principal#getName()
   */
  public String getName( ) {
    return DUMMY_NAME;
  }
}
