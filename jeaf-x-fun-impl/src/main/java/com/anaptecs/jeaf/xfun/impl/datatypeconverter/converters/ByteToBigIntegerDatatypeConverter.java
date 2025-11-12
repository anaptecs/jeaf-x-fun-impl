/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.math.BigInteger;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a Byte Value into the BigInteger Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class ByteToBigIntegerDatatypeConverter implements DatatypeConverter<Byte, BigInteger> {
  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<Byte> getInputType( ) {
    return Byte.class;
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
   * Method converts the passed Byte object to a BigInteger.
   * 
   * @param pInput is the value which will be converted into the BigInteger datatype.
   * @return {@link BigInteger} BigInteger object to which the passed Byte was converted. The method returns null in the
   * case the pInput is null.
   */
  public final BigInteger convert( Byte pInput ) {
    // No checks of parameter required.
    BigInteger lOutput;
    if (pInput != null) {
      Long lInput = pInput.longValue();
      lOutput = BigInteger.valueOf(lInput);
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
