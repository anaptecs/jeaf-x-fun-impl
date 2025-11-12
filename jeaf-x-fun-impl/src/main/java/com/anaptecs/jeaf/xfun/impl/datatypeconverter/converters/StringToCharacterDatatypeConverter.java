/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

/**
 * This DataTypeConverter is used to Convert a String Value into the Character Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class StringToCharacterDatatypeConverter implements DatatypeConverter<String, Character> {
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
  public final Class<Character> getOutputType( ) {
    return Character.class;
  }

  /**
   * Method converts the passed String object to a Character.
   * 
   * @param pInput is the value which will be converted into the Character datatype.
   * @return {@link Character} Character object to which the passed String was converted. The method returns null in the
   * case the pInput is null.
   */
  public final Character convert( String pInput ) {
    // No checks of parameter required.
    Character lOutput;
    if (pInput != null) {
      if (pInput.length() == 1) {
        lOutput = pInput.charAt(0);
      }
      else {
        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, String.class.getSimpleName(),
            Character.class.getSimpleName(), pInput);
      }
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
