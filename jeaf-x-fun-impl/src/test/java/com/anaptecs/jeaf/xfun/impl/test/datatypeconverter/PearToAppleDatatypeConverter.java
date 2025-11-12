/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.datatypeconverter;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;

public class PearToAppleDatatypeConverter implements DatatypeConverter<Pear, Apple> {

  @Override
  public Class<Pear> getInputType( ) {
    return Pear.class;
  }

  @Override
  public Class<Apple> getOutputType( ) {
    return Apple.class;
  }

  @Override
  public Apple convert( Pear pInput ) {
    return null;
  }

}
