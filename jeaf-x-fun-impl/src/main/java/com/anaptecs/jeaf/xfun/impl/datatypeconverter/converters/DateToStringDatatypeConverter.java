/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.util.Date;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

public class DateToStringDatatypeConverter implements DatatypeConverter<Date, String> {

  @Override
  public Class<Date> getInputType( ) {
    return Date.class;
  }

  @Override
  public Class<String> getOutputType( ) {
    return String.class;
  }

  @Override
  public String convert( Date pInput ) {
    String lString;
    if (pInput != null) {
      lString = DateTools.getDateTools().toString(pInput);
    }
    else {
      lString = null;
    }
    return lString;
  }

}
