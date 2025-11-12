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
 * This Datatype Converter can only be used for StringValues with under StringLength.
 * 
 * @author JEAF Development Team
 * 
 */
public class StringToBigIntegerDatatypeConverter implements DatatypeConverter<String, BigInteger> {
  /**
   * represents the maximum length which the input value can be to ensure that the output value is not corrupted.
   */
  public static final int MAX_INPUT_LENGTH = 18;

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
  public final Class<String> getInputType( ) {
    return String.class;
  }

  /**
   * Method returns the class object of the output type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported output type. The method never returns null.
   */
  public final Class<BigInteger> getOutputType( ) {
    return BigInteger.class;
  }

  /**
   * Method converts the passed String object to a BigInteger.
   * 
   * @param pInput is the value which will be converted into the BigInteger datatype.
   * @return {@link BigInteger} BigInteger object to which the passed String was converted. The method returns null in
   * the case the pInput is null.
   */
  public final BigInteger convert( String pInput ) {
    // No checks of parameter required.
    BigInteger lOutput;
    if (pInput != null) {
      if (pInput.length() > MAX_INPUT_LENGTH) {

        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, String.class.getSimpleName(),
            BigInteger.class.getSimpleName(), pInput);

      }
      lOutput = BigInteger.valueOf(Long.parseLong(pInput));
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
