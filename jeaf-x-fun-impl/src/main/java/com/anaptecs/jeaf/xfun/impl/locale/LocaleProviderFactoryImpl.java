/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.locale;

import com.anaptecs.jeaf.xfun.api.locale.LocaleProvider;
import com.anaptecs.jeaf.xfun.api.locale.LocaleProviderFactory;

public class LocaleProviderFactoryImpl implements LocaleProviderFactory {
  private final LocaleProvider localeProvider = new DefaultLocaleProvider();

  @Override
  public LocaleProvider getLocaleProvider( ) {
    return localeProvider;
  }
}
