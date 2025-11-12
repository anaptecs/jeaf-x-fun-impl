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
 * This DataTypeConverter is used to Convert a BigDecimal Value into the DoubleDataType by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class BigDecimalToDoubleDatatypeConverter implements DatatypeConverter<BigDecimal, Double> {
  /**
   * This Constant is a BigDecimal Value which represents the maximum Value of the Double DataType.
   */
  public static final BigDecimal MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);

  /**
   * This Constant is a BigDecimal Value which represents the minimum Value of the Double DataType.
   */
  public static final BigDecimal MIN_DOUBLE = BigDecimal.valueOf(Double.MIN_VALUE);

  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<BigDecimal> getInputType( ) {
    return BigDecimal.class;
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
   * Method converts the passed BigDecimal object to a Double Value.
   * 
   * @return {@link Double} Double object to which the passed BigDecimal was converted. The method returns null in the
   * case the pInput is null.
   * @throws Exception
   * @param pInput is the Value which have to be converted into Double
   */
  public final Double convert( BigDecimal pInput ) {
    Double lOutput;
    if (pInput != null) {
      if (pInput.compareTo(MAX_DOUBLE) > 0 || pInput.compareTo(MIN_DOUBLE) < 0) {

        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, BigDecimal.class.getSimpleName(),
            Double.class.getSimpleName(), pInput.toString());
      }
      else {
        lOutput = pInput.doubleValue();
      }
    }
    else {
      lOutput = null;
    }
    // Return result of conversion.
    return lOutput;
  }
}
