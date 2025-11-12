/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a Float Value into the Double Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class FloatToDoubleDatatypeConverter implements DatatypeConverter<Float, Double> {
  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<Float> getInputType( ) {
    return Float.class;
  }

  /**
   * Method returns the class object of the output type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported output type. The method never returns null.
   */
  public final Class<Double> getOutputType( ) {
    return Double.class;
  }

  /**
   * Method converts the passed Float object to a Double.
   * 
   * @param pInput is the value which will be converted into the Double datatype.
   * @return {@link Double} Double object to which the passed Float was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Double convert( Float pInput ) {
    // No checks of parameter required.
    Double lOutput;
    if (pInput != null) {
      String lString = Float.toString(pInput);
      lOutput = Double.parseDouble(lString);
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
