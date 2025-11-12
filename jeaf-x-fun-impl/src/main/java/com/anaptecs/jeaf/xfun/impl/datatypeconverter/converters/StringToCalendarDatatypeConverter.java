/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.util.Date;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * Class implements a {@link String} to {@link Date} datatype converter. It expects the string to contain a somehow
 * valid date in ISO format.
 * 
 * @author JEAF Development Team
 */
public class StringToCalendarDatatypeConverter implements DatatypeConverter<String, Date> {

  @Override
  public Class<String> getInputType( ) {
    return String.class;
  }

  @Override
  public Class<Date> getOutputType( ) {
    return Date.class;
  }

  @Override
  public Date convert( String pInput ) {
    Date lDate;
    // Let's try to tolerant conversion from any ISO formated date representation to a date object.
    if (pInput != null) {
      lDate = DateTools.getDateTools().toDate(pInput, false);
    }
    else {
      lDate = null;
    }
    return lDate;
  }

}
