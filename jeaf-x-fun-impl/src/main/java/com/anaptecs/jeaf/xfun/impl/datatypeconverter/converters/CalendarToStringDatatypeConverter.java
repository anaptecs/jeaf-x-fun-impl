/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.util.Calendar;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

public class CalendarToStringDatatypeConverter implements DatatypeConverter<Calendar, String> {

  @Override
  public Class<Calendar> getInputType( ) {
    return Calendar.class;
  }

  @Override
  public Class<String> getOutputType( ) {
    return String.class;
  }

  @Override
  public String convert( Calendar pInput ) {
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
