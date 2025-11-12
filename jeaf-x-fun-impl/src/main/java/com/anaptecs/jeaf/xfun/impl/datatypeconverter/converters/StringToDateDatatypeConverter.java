/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.util.Calendar;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * Class implements a {@link String} to {@link Calendar} datatype converter. It expects the string to contain a somehow
 * valid date in ISO format.
 * 
 * @author JEAF Development Team
 */
public class StringToDateDatatypeConverter implements DatatypeConverter<String, Calendar> {

  @Override
  public Class<String> getInputType( ) {
    return String.class;
  }

  @Override
  public Class<Calendar> getOutputType( ) {
    return Calendar.class;
  }

  @Override
  public Calendar convert( String pInput ) {
    Calendar lCalendar;
    // Let's try to tolerant conversion from any ISO formated date representation to a Calendar object.
    if (pInput != null) {
      lCalendar = DateTools.getDateTools().toCalendar(pInput, false);
    }
    else {
      lCalendar = null;
    }
    return lCalendar;
  }
}
