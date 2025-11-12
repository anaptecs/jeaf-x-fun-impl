/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;

import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;

/**
 * Class provides access to data that is stored in a configuration file. All other methods to simplify the access to
 * configuration file are already implemented in super class ResourceAccessProvider.
 * 
 * @author JEAF Development Team
 * @version 1.1
 */
public final class FileConfigurationResource implements ConfigurationResource {
  /**
   * Object contains all the values of the configuration file. The reference is never null, since the configuration file
   * will be loaded when the object is initialized by its constructor.
   */
  private final Properties properties;

  /**
   * Name of the configuration file that can be accessed through this class. The attribute is never null since it has to
   * be passed to the class constructor as a not null parameter.
   */
  private final String fileName;

  /**
   * Location of the configuration file that can be accessed by this class. Since the resource will be loaded when the
   * object is initialized by its constructor.
   */
  private final String fileLocation;

  /**
   * Initialize object. Therefore the name of the configuration file that should be read has to be passed.
   * 
   * @param pFileName Name of the configuration file whose data should be accessed through this class. The parameter
   * must not be null and the passed file name must point to a file.
   * @throws IOException if no configuration file with the passed name exists.
   */
  public FileConfigurationResource( String pFileName ) throws IOException {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pFileName, "pFileName");

    // Resolve file location.
    File lFile = new File(pFileName);
    String lCanonicalPath = lFile.getCanonicalPath();
    fileName = lFile.getName();
    fileLocation = lCanonicalPath;

    // Try to open file and read its properties.
    try (FileInputStream lFileInputStream = new FileInputStream(lCanonicalPath)) {
      properties = new Properties();
      properties.load(lFileInputStream);
    }
  }

  /**
   * Method returns the name of the resource.
   * 
   * @return String Name of the resource. The method never returns null.
   */
  @Override
  public String getResourceName( ) {
    return fileName;
  }

  /**
   * Method returns the location of the resource.
   * 
   * @return URL Location of the resource. The method never returns null.
   */
  @Override
  public String getResourceLocation( ) {
    return fileLocation;
  }

  @Override
  public boolean hasConfigurationValue( String pConfigurationKey ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");

    return properties.containsKey(pConfigurationKey);
  }

  @Override
  public List<String> getAllConfigurationKeys( ) {
    List<String> lKeys = new ArrayList<>();
    for (Object lKey : properties.keySet()) {
      lKeys.add(lKey.toString());
    }
    return lKeys;
  }

  @Override
  public String getConfigurationValue( String pConfigurationKey ) throws MissingResourceException {
    // Check parameter.
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");

    // Get value of property with the passed name and trim it.
    String lValue = properties.getProperty(pConfigurationKey);
    if (lValue != null) {
      return lValue.trim();
    }
    // Property does not exist.
    else {
      String lMessage = "Environment variable with the name '" + pConfigurationKey + "' is not defined.";
      throw new MissingResourceException(lMessage, fileLocation, pConfigurationKey);
    }
  }

}
