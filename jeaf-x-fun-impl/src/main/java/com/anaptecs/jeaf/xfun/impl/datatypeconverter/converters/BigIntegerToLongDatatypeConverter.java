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
 * This DataTypeConverter is used to Convert a BigInteger Value into the Long Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class BigIntegerToLongDatatypeConverter implements DatatypeConverter<BigInteger, Long> {
  /**
   * This Constant is a BigInteger Value which represents the maximum Value of the Long DataType.
   */
  public static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

  /**
   * This Constant is a BigInteger Value which represents the minimum Value of the Long DataType.
   */
  public static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);

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
  public final Class<Long> getOutputType( ) {
    return Long.class;
  }

  /**
   * Method converts the passed BigInteger object to a Long Value.
   * 
   * @param pInput is the value which will be converted into Long.
   * @return {@link Long} Long object to which the passed BigInteger was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Long convert( BigInteger pInput ) {
    Long lOutput;
    if (pInput != null) {
      if (pInput.compareTo(MAX_LONG) > 0 || pInput.compareTo(MIN_LONG) < 0) {

        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, BigInteger.class.getSimpleName(),
            Long.class.getSimpleName(), pInput.toString());
      }
      else {
        lOutput = pInput.longValue();
      }
    }
    else {
      lOutput = null;
    }
    // Return result of conversion.
    return lOutput;
  }
}
