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
 * This DataTypeConverter is used to Convert a String Value into the Short Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class StringToShortDatatypeConverter implements DatatypeConverter<String, Short> {
  /**
   * This Constant is a Integer Value which represents the maximum Value of the Short DataType.
   */
  public static final Integer MAX_SHORT = Integer.valueOf(Short.MAX_VALUE);

  /**
   * This Constant is a Integer Value which represents the minimum Value of the Short DataType.
   */
  public static final Integer MIN_SHORT = Integer.valueOf(Short.MIN_VALUE);

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
  public final Class<Short> getOutputType( ) {
    return Short.class;
  }

  /**
   * Method converts the passed String object to a Short.
   * 
   * @param pInput is the value which will be converted into the Short datatype.
   * @return {@link Short} Short object to which the passed String was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Short convert( String pInput ) {
    // No checks of parameter required.
    Short lOutput;
    if (pInput != null) {
      if (Integer.valueOf(Integer.parseInt(pInput)).compareTo(MAX_SHORT) > 0) {
        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, String.class.getSimpleName(),
            Short.class.getSimpleName(), pInput);
      }

      else if (Integer.valueOf(Integer.parseInt(pInput)).compareTo(MIN_SHORT) < 0) {
        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, String.class.getSimpleName(),
            Short.class.getSimpleName(), pInput);
      }
      else {
        lOutput = Short.parseShort(pInput);
      }
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
