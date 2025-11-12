/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.math.BigInteger;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a String Value into the Long Datatype by normally using the convert method.
 * 
 * @author JEAF Development Team
 * 
 */
public class StringToLongDatatypeConverter implements DatatypeConverter<String, Long> {
  /**
   * This Constant is a Long Value which represents the maximum Value of the Long DataType.
   */
  public static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

  /**
   * This Constant is a Long Value which represents the minimum Value of the Integer DataType.
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
  public final Class<Long> getOutputType( ) {
    return Long.class;
  }

  /**
   * Method converts the passed String object to a Long.
   * 
   * @param pInput is the value which will be converted into the Long datatype.
   * @return {@link Long} Long object to which the passed String was converted. The method returns null in the case the
   * pInput is null.
   */
  public final Long convert( String pInput ) {
    // No checks of parameter required.
    Long lOutput;
    if (pInput != null) {
      lOutput = Long.parseLong(pInput);
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
