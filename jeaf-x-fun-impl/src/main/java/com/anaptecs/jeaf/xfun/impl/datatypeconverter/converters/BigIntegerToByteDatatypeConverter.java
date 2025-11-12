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
 * This DataTypeConverter is used to Convert a BigInteger Value into the Byte Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class BigIntegerToByteDatatypeConverter implements DatatypeConverter<BigInteger, Byte> {
  /**
   * This Constant is a BigInteger Value which represents the maximum Value of the Byte DataType.
   */
  public static final BigInteger MAX_BYTE = BigInteger.valueOf(Long.parseLong(Byte.toString(Byte.MAX_VALUE)));

  /**
   * This Constant is a BigInteger Value which represents the minimum Value of the Byte DataType.
   */
  public static final BigInteger MIN_BYTE = BigInteger.valueOf(Long.parseLong(Byte.toString(Byte.MAX_VALUE)));

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
  public final Class<Byte> getOutputType( ) {
    return Byte.class;
  }

  /**
   * Method converts the passed BigInteger object to a Byte Value.
   * 
   * @param pInput is the value which has to be converted into the Byte Datatype
   * @return {@link Byte} Byte object to which the passed BigInteger was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Byte convert( BigInteger pInput ) {
    Byte lOutput;
    if (pInput != null) {
      // Conversion not possible
      if (pInput.compareTo(BigInteger.valueOf(Byte.MAX_VALUE)) > 0
          || pInput.compareTo(BigInteger.valueOf(Byte.MIN_VALUE)) < 0) {

        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, BigInteger.class.getSimpleName(),
            Byte.class.getSimpleName(), pInput.toString());
      }
      // Convert value to byte.
      else {
        lOutput = pInput.byteValue();
      }
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
