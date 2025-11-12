/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.datatypeconverter;

import com.anaptecs.jeaf.xfun.annotations.DatatypeConverterImpl;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

@DatatypeConverterImpl
public class AppleToPearDatatypeConverter implements DatatypeConverter<Apple, Pear> {

  @Override
  public Class<Apple> getInputType( ) {
    return Apple.class;
  }

  @Override
  public Class<Pear> getOutputType( ) {
    return Pear.class;
  }

  @Override
  public Pear convert( Apple pInput ) {
    return new Pear();
  }

}
