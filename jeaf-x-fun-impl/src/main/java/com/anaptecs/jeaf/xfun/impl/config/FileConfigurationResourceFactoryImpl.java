/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.config;

import java.io.IOException;

import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;
import com.anaptecs.jeaf.xfun.api.config.FileConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

public class FileConfigurationResourceFactoryImpl implements FileConfigurationResourceFactory {

  @Override
  public ConfigurationResource getFileConfigurationResource( String pFileName ) throws JEAFSystemException {
    // Try to open file.
    try {
      return new FileConfigurationResource(pFileName);
    }
    catch (IOException e) {
      throw new JEAFSystemException(XFunMessages.FILE_NOT_FOUND, e, pFileName);
    }
  }
}
