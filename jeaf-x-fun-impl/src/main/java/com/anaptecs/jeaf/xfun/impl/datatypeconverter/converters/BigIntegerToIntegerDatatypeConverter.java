/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.math.BigInteger;

import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

/**
 * This DataTypeConverter is used to Convert a BigInteger Value into the Integer Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class BigIntegerToIntegerDatatypeConverter implements DatatypeConverter<BigInteger, Integer> {
  /**
   * This Constant is a BigInteger Value which represents the maximum Value of the Integer DataType.
   */
  public static final BigInteger MAX_INTEGER = BigInteger.valueOf(Integer.MAX_VALUE);

  /**
   * This Constant is a BigInteger Value which represents the minimum Value of the Integer DataType.
   */
  public static final BigInteger MIN_INTEGER = BigInteger.valueOf(Integer.MIN_VALUE);

  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<BigInteger> getInputType( ) {
    return BigInteger.class;
  }

  /**
   * Method returns the class object of the output type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported output type. The method never returns null.
   */
  public final Class<Integer> getOutputType( ) {
    return Integer.class;
  }

  /**
   * Method converts the passed BigInteger object to a Integer.
   * 
   * @param pInput is the value which will be converted into the Integer Datatype.
   * @return {@link Integer} Integer object to which the passed BigInteger was converted. The method returns null in the
   * case the pInput is null.
   */
  public final Integer convert( BigInteger pInput ) {
    Integer lOutput;
    if (pInput != null) {
      if (pInput.compareTo(MAX_INTEGER) > 0 || pInput.compareTo(MIN_INTEGER) < 0) {

        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, BigInteger.class.getSimpleName(),
            Integer.class.getSimpleName(), pInput.toString());
      }
      else {
        lOutput = pInput.intValue();
      }
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
