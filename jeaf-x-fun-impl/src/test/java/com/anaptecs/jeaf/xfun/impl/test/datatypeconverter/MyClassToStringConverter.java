/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.datatypeconverter;

import com.anaptecs.jeaf.xfun.annotations.DatatypeConverterImpl;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a String Value into a class object.
 * 
 * @author JEAF Development Team
 */
@SuppressWarnings("rawtypes")
@DatatypeConverterImpl
public class MyClassToStringConverter implements DatatypeConverter<Class, String> {
  /**
   * Initialize object.
   */
  public MyClassToStringConverter( ) {
  }

  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<Class> getInputType( ) {
    return Class.class;
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
   * Method converts the passed String object to a Short.
   * 
   * @param pInput is the value which will be converted into the Short datatype.
   * @return {@link Short} Short object to which the passed String was converted. The method returns null in the case
   * the pInput is null.
   */
  public final String convert( Class pInput ) {
    String lString;
    if (pInput != null) {
      lString = pInput.getName();
    }
    else {
      lString = null;
    }
    return lString;
  }
}
