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
 * This DataTypeConverter is used to Convert a BigDecimal Value into the Float Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class BigDecimalToFloatDatatypeConverter implements DatatypeConverter<BigDecimal, Float> {
  /**
   * This Constant is a BigDecimal Value which represents the maximum Value of the Float DataType.
   */
  public static final BigDecimal MAX_FLOAT = BigDecimal.valueOf(Float.MAX_VALUE);

  /**
   * This Constant is a BigDecimal Value which represents the minimum Value of the Float DataType.
   */
  public static final BigDecimal MIN_FLOAT = BigDecimal.valueOf(Float.MIN_VALUE);

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
  public final Class<Float> getOutputType( ) {
    return Float.class;
  }

  /**
   * Method converts the passed BigDecimal object to a Float Value.
   * 
   * @param pInput is the Value which has to be converted into Float.
   * @return {@link Float} Float object to which the passed BigDecimal was converted. The method returns null in the
   * case the pInput is null.
   */
  public final Float convert( BigDecimal pInput ) {
    Float lOutput;
    if (pInput != null) {
      if (pInput.compareTo(MAX_FLOAT) > 0) {
        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, BigDecimal.class.getSimpleName(),
            Float.class.getSimpleName(), pInput.toString());
      }
      else if (pInput.compareTo(MIN_FLOAT) < 0) {
        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, BigDecimal.class.getSimpleName(),
            Float.class.getSimpleName(), pInput.toString());
      }
      else {

        lOutput = pInput.floatValue();
      }
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
