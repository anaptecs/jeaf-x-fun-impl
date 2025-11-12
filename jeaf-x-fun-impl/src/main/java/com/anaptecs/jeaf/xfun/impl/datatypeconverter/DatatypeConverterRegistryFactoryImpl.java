/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.datatypeconverter;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistry;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistryFactory;

public class DatatypeConverterRegistryFactoryImpl implements DatatypeConverterRegistryFactory {
  private final DatatypeConverterRegistry registry = new DatatypeConverterRegistryImpl();

  @Override
  public DatatypeConverterRegistry getDatatypeConverterRegistry( ) {
    return registry;
  }
}
