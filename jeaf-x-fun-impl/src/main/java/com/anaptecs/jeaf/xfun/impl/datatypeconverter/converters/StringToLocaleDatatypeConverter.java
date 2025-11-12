/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.util.Locale;

import com.anaptecs.jeaf.tools.api.Tools;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * Class implements a datatype converter that reconstructs a {@link Locale} from its {@link String} representation.
 * 
 * @author JEAF Development Team
 */
public class StringToLocaleDatatypeConverter implements DatatypeConverter<String, Locale> {

  @Override
  public Class<String> getInputType( ) {
    return String.class;
  }

  @Override
  public Class<Locale> getOutputType( ) {
    return Locale.class;
  }

  @Override
  public Locale convert( String pInput ) {
    Locale lLocale;
    if (pInput != null) {
      lLocale = Tools.getLocaleTools().createLocale(pInput);
    }
    else {
      lLocale = null;
    }
    return lLocale;
  }

}
