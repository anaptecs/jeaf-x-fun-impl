/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a Character Value into the Byte Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class CharacterToByteDatatypeConverter implements DatatypeConverter<Character, Byte> {
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
  public final Class<Byte> getOutputType( ) {
    return Byte.class;
  }

  /**
   * Method converts the passed Character object to a Byte.
   * 
   * @param pInput is the value which will be converted into the Byte datatype.
   * @return {@link Byte} Byte object to which the passed Character was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Byte convert( Character pInput ) {
    // No checks of parameter required.
    Byte lOutput;
    if (pInput != null) {
      lOutput = Byte.valueOf((byte) pInput.charValue());

    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
