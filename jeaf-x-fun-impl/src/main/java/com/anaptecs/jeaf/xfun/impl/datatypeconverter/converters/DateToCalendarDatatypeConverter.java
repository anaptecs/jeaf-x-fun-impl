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
 * This DataTypeConverter is used to convert a Date value into the Calendar datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class DateToCalendarDatatypeConverter implements DatatypeConverter<Date, Calendar> {
  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<Date> getInputType( ) {
    return Date.class;
  }

  /**
   * Method returns the class object of the output type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported output type. The method never returns null.
   */
  public final Class<Calendar> getOutputType( ) {
    return Calendar.class;
  }

  /**
   * Method converts the passed Date object to a Calendar.
   * 
   * @param pInput is the value which will be converted into the Calendar datatype.
   * @return {@link Calendar} Calendar object to which the passed Date was converted. The method returns null in the
   * case the pInput is null.
   */
  public final Calendar convert( Date pInput ) {
    Calendar lOutput;
    if (pInput != null) {
      lOutput = DateTools.getDateTools().toCalendar(pInput);
    }
    else {
      lOutput = null;
    }
    return lOutput;
  }
}
