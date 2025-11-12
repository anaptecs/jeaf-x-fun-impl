/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.locale;

import java.util.Locale;

import com.anaptecs.jeaf.xfun.api.locale.LocaleProvider;

/**
 * Class implements a locale provider that uses the JVMs default mechanism to determine the current locale. The class
 * does not determine the current locale in a context sensitive way as it is required for multi user environments where
 * every user has its own locale.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public class DefaultLocaleProvider implements LocaleProvider {
  /**
   * Initialize object. Thereby no actions are performed.
   */
  public DefaultLocaleProvider( ) {
    // Nothing to do.
  }

  /**
   * Method returns the default locale of the JVM.
   * 
   * @return Locale Default locale of the JVM. The method never returns null.
   * 
   * @see LocaleProvider#getCurrentLocale()
   */
  public Locale getCurrentLocale( ) {
    return Locale.getDefault();
  }
}