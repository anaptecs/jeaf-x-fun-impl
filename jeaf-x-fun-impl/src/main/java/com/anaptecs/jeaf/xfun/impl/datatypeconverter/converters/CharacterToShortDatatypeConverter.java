/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a Character Value into the Short Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class CharacterToShortDatatypeConverter implements DatatypeConverter<Character, Short> {
  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<Character> getInputType( ) {
    return Character.class;
  }

  /**
   * Method returns the class object of the output type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported output type. The method never returns null.
   */
  public final Class<Short> getOutputType( ) {
    return Short.class;
  }

  /**
   * Method converts the passed Character object to a Short.
   * 
   * @param pInput is the value which will be converted into the Short datatype.
   * @return {@link Short} Short object to which the passed Character was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Short convert( Character pInput ) {
    // No checks of parameter required.
    Short lOutput;
    if (pInput != null) {
      lOutput = (short) pInput.charValue();
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
