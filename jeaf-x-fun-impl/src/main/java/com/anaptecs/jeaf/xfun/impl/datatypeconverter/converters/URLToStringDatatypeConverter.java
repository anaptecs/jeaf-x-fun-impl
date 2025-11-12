/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.net.URL;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

public class URLToStringDatatypeConverter implements DatatypeConverter<URL, String> {

  @Override
  public Class<URL> getInputType( ) {
    return URL.class;
  }

  @Override
  public Class<String> getOutputType( ) {
    return String.class;
  }

  @Override
  public String convert( URL pInput ) {
    String lString;
    if (pInput != null) {
      lString = pInput.toExternalForm();
    }
    else {
      lString = null;
    }
    return lString;
  }
}
