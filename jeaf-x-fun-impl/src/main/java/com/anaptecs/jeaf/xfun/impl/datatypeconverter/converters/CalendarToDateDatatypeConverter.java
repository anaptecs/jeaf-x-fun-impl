/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.util.Calendar;
import java.util.Date;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to convert a Calendar value into the Date datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class CalendarToDateDatatypeConverter implements DatatypeConverter<Calendar, Date> {
  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<Calendar> getInputType( ) {
    return Calendar.class;
  }

  /**
   * Method returns the class object of the output type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported output type. The method never returns null.
   */
  public final Class<Date> getOutputType( ) {
    return Date.class;
  }

  /**
   * Method converts the passed Calendar object to a Calendar.
   * 
   * @param pInput is the value which will be converted into the Date datatype.
   * @return {@link Date} Date object to which the passed Date was converted. The method returns null in the case the
   * pInput is null.
   */
  public final Date convert( Calendar pInput ) {
    Date lOutput;
    if (pInput != null) {
      lOutput = DateTools.getDateTools().toDate(pInput);
    }
    else {
      lOutput = null;
    }
    return lOutput;
  }
}
