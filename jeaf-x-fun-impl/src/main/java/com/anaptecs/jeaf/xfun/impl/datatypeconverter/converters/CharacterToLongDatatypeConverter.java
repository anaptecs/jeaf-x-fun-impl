/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a Character Value into the Long Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class CharacterToLongDatatypeConverter implements DatatypeConverter<Character, Long> {
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
  public final Class<Long> getOutputType( ) {
    return Long.class;
  }

  /**
   * Method converts the passed Character object to a Long.
   * 
   * @param pInput is the value which will be converted into the Long datatype.
   * @return {@link Long} Long object to which the passed Character was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Long convert( Character pInput ) {
    // No checks of parameter required.
    Long lOutput;
    if (pInput != null) {
      lOutput = (long) pInput.charValue();
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
