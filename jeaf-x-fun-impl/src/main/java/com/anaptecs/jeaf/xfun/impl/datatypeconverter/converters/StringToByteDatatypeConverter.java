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
 * This DataTypeConverter is used to Convert a String Value into the Byte Datatype by normally using the convert method.
 * 
 * @author JEAF Development Team
 * 
 */
public class StringToByteDatatypeConverter implements DatatypeConverter<String, Byte> {
  /**
   * This Constant is a Integer Value which represents the maximum Value of the Byte DataType.
   */
  public static final Integer MAX_BYTE = (int) Byte.MAX_VALUE;

  /**
   * This Constant is a Integer Value which represents the minimum Value of the Byte DataType.
   */
  public static final Integer MIN_BYTE = (int) Byte.MIN_VALUE;

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
  public final Class<Byte> getOutputType( ) {
    return Byte.class;
  }

  /**
   * Method converts the passed String object to a Byte.
   * 
   * @param pInput is the value which will be converted into the Byte datatype.
   * @return {@link Byte} Byte object to which the passed String was converted. The method returns null in the case the
   * pInput is null.
   */
  public final Byte convert( String pInput ) {
    // No checks of parameter required.
    Byte lOutput;
    if (pInput != null) {
      // Integer value is too big for a conversion.
      if (Integer.valueOf(Integer.parseInt(pInput)).compareTo(MAX_BYTE) > 0) {
        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, String.class.getSimpleName(),
            Byte.class.getSimpleName(), pInput);
      }
      // Integer value is too small for a conversion.
      else if (Integer.valueOf(Integer.parseInt(pInput)).compareTo(MIN_BYTE) < 0) {
        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, String.class.getSimpleName(),
            Byte.class.getSimpleName(), pInput);
      }
      // Conversion is possible
      else {
        lOutput = Byte.parseByte(pInput);
      }
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
