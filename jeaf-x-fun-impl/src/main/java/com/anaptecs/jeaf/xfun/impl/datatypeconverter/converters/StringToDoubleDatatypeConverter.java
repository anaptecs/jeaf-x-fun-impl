/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.math.BigDecimal;

import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

/**
 * This DataTypeConverter is used to Convert a String Value into the Double Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class StringToDoubleDatatypeConverter implements DatatypeConverter<String, Double> {
  /**
   * represents the maximum length which the input value can be to ensure that the output value is not corrupted.
   */
  public static final int MAX_INPUT_LENGTH = 324;

  /**
   * This Constant is a BigDecimal Value which represents the maximum Value of the Double DataType used for comparison.
   */
  public static final BigDecimal MAX_DOUBLE = (BigDecimal.valueOf(Double.MAX_VALUE));

  /**
   * This Constant is a BigDecimal Value which represents the minimum Value of the Double DataType used for comparison.
   */
  public static final BigDecimal MIN_DOUBLE = (BigDecimal.valueOf(Double.MIN_VALUE));

  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<String> getInputType( ) {
    return String.class;
  }

  /**
   * Method returns the class object of the output type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported output type. The method never returns null.
   */
  public final Class<Double> getOutputType( ) {
    return Double.class;
  }

  /**
   * Method converts the passed String object to a Double.
   * 
   * @param pInput is the value which will be converted into the Double datatype.
   * @return {@link Double} Double object to which the passed String was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Double convert( String pInput ) {
    // No checks of parameter required.
    Double lOutput;
    if (pInput != null) {
      if (pInput.length() > MAX_INPUT_LENGTH) {
        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, String.class.getSimpleName(),
            Double.class.getSimpleName(), pInput);
      }
      else {
        lOutput = Double.parseDouble(pInput);
      }
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
