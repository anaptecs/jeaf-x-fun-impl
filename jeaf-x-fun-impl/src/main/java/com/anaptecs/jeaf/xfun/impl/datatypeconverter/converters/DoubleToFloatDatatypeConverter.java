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
 * This DataTypeConverter is used to Convert a Double Value into the Float Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class DoubleToFloatDatatypeConverter implements DatatypeConverter<Double, Float> {
  /**
   * This Constant is a Double Value which represents the maximum Value of the Float DataType.
   */
  private static final double MAX_FLOAT = Float.MAX_VALUE;

  /**
   * This Constant is a Double Value which represents the minimum Value of the Float DataType.
   */
  private static final double MIN_FLOAT = Float.MIN_VALUE;

  /**
   * Method returns the class object of the input type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported input type. The method never returns null.
   */
  public final Class<Double> getInputType( ) {
    return Double.class;
  }

  /**
   * Method returns the class object of the output type that is supported by this data type converter.
   * 
   * @return {@link Class} Class object of the supported output type. The method never returns null.
   */
  public final Class<Float> getOutputType( ) {
    return Float.class;
  }

  /**
   * Method converts the passed Double object to a Float.
   * 
   * @param pInput is the value which will be converted into the Float datatype.
   * @return {@link Float} Float object to which the passed Double was converted. The method returns null in the case
   * the pInput is null.
   */
  public final Float convert( Double pInput ) {
    Float lOutput;
    if (pInput != null) {
      if (pInput > MAX_FLOAT || pInput < MIN_FLOAT) {

        throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, Double.class.getSimpleName(),
            Float.class.getSimpleName(), pInput.toString());

      }
      else {
        lOutput = Float.parseFloat(pInput.toString());
      }
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
