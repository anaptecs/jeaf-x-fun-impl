/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a Boolean Value into the String Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class BooleanToStringDatatypeConverter implements DatatypeConverter<Boolean, String> {
  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<Boolean> getInputType( ) {
    return Boolean.class;
  }

  /**
   * Method returns the class object of the output type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported output type. The method never returns null.
   */
  public final Class<String> getOutputType( ) {
    return String.class;
  }

  /**
   * Method converts the passed Boolean object to a String.
   * 
   * @param pInput is the value which will be converted into the String Datatype.
   * @return {@link String} String object to which the passed Boolean was converted. The method returns null in the case
   * the pInput is null.
   */
  public final String convert( Boolean pInput ) {
    // No checks of parameter required.
    String lOutput;
    if (pInput != null) {
      lOutput = pInput.toString();
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
