/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters;

import java.math.BigDecimal;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

/**
 * This DataTypeConverter is used to Convert a Double Value into the BigDecimal Datatype by normally using the convert
 * method.
 * 
 * @author JEAF Development Team
 * 
 */
public class DoubleToBigDecimalDatatypeConverter implements DatatypeConverter<Double, BigDecimal> {
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
  public final Class<BigDecimal> getOutputType( ) {
    return BigDecimal.class;
  }

  /**
   * Method converts the passed Double object to a BigDecimal.
   * 
   * @param pInput is the value which will be converted into the BigDecimal datatype.
   * @return {@link BigDecimal} BigDecimal object to which the passed Double was converted. The method returns null in
   * the case the pInput is null.
   */
  public final BigDecimal convert( Double pInput ) {
    // No checks of parameter required.
    BigDecimal lOutput;
    if (pInput != null) {
      lOutput = BigDecimal.valueOf(pInput);
    }
    else {
      lOutput = null;
    }

    // Return result of conversion.
    return lOutput;
  }
}
