/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This datatype converter is used to convert a String value into the String. As in general this kind of conversion does
 * not make sense its still meaningful as this way generic implementations of features are possible.
 * 
 * @author JEAF Development Team
 */
public class StringToStringDatatypeConverter implements DatatypeConverter<String, String> {
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
  public final String convert( String pInput ) {
    // As this class does not do any conversion we can just return the input value.
    return pInput;
  }
}
