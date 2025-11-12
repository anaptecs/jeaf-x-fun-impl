/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.net.URL;

import com.anaptecs.jeaf.tools.api.Tools;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

public class StringToURLDatatypeConverter implements DatatypeConverter<String, URL> {

  @Override
  public Class<String> getInputType( ) {
    return String.class;
  }

  @Override
  public Class<URL> getOutputType( ) {
    return URL.class;
  }

  @Override
  public URL convert( String pInput ) {
    URL lURL;
    if (pInput != null) {
      lURL = Tools.getNetworkingTools().toURL(pInput);
    }
    else {
      lURL = null;
    }
    return lURL;
  }
}
