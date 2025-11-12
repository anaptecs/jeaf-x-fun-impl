/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import com.anaptecs.jeaf.tools.api.Tools;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to convert a String value into a class object.
 * 
 * @author JEAF Development Team
 */
@SuppressWarnings("rawtypes")
public class StringToClassDatatypeConverter implements DatatypeConverter<String, Class> {
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
  public final Class<Class> getOutputType( ) {
    return Class.class;
  }

  /**
   * Method converts the passed String object to a Short.
   * 
   * @param pInput is the value which will be converted into the Short datatype.
   * @return {@link Short} Short object to which the passed String was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Class convert( String pInput ) {
    Class<?> lClass;
    if (pInput != null) {
      lClass = Tools.getReflectionTools().loadClass(pInput);
    }
    else {
      lClass = null;
    }
    return lClass;
  }
}
