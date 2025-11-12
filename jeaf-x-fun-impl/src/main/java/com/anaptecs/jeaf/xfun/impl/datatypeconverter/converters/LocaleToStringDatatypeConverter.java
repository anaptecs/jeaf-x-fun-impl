/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.util.Locale;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

public class LocaleToStringDatatypeConverter implements DatatypeConverter<Locale, String> {

  @Override
  public Class<Locale> getInputType( ) {
    return Locale.class;
  }

  @Override
  public Class<String> getOutputType( ) {
    return String.class;
  }

  @Override
  public String convert( Locale pInput ) {
    String lLocaleAsString;
    if (pInput != null) {
      lLocaleAsString = pInput.toString();
    }
    else {
      lLocaleAsString = null;
    }
    return lLocaleAsString;
  }
}
