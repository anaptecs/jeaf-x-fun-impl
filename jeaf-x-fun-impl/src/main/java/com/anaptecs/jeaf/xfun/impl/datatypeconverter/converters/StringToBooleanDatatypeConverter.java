/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a String Value into the Boolean Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class StringToBooleanDatatypeConverter implements DatatypeConverter<String, Boolean> {
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
  public final Class<Boolean> getOutputType( ) {
    return Boolean.class;
  }

  /**
   * Method converts the passed String object to a Boolean.
   * 
   * @param pInput is the value which will be converted into the Boolean datatype.
   * @return {@link Boolean} Boolean object to which the passed String was converted. The method returns null in the
   * case the pInput is null.
   */
  public final Boolean convert( String pInput ) {
    // No checks of parameter required.
    Boolean lOutput;
    if (pInput != null) {
      lOutput = Boolean.parseBoolean(pInput);
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
