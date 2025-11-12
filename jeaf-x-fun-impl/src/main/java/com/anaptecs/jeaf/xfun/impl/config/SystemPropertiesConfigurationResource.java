/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;

import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;

/**
 * Class provides access to data that is stored in JVM's system properties. All other methods to simplify the access to
 * system properties are already implemented in super class ResourceAccessProvider. System properties are defined using
 * the "-D" JVM argument.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public final class SystemPropertiesConfigurationResource implements ConfigurationResource {
  /**
   * String constant that is always used as resource name.
   */
  public static final String SYSTEM_PROPERTIES = "JVM system properties";

  /**
   * Initialize object. Thereby no actions are performed.
   */
  public SystemPropertiesConfigurationResource( ) {
    // Nothing to do.
  }

  /**
   * Method returns the name of the resource. Since system properties are not a named resource always the same name will
   * be returned.
   * 
   * @return String Name of the system properties resource. The method never returns null.
   */
  @Override
  public String getResourceName( ) {
    return SYSTEM_PROPERTIES;
  }

  /**
   * Method returns the location of the resource.
   * 
   * @return URL Location of the resource. The method never returns null.
   */
  @Override
  public String getResourceLocation( ) {
    return SYSTEM_PROPERTIES;
  }

  @Override
  public boolean hasConfigurationValue( String pConfigurationKey ) {
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");
    return System.getProperties().containsKey(pConfigurationKey);
  }

  @Override
  public List<String> getAllConfigurationKeys( ) {
    // This is a rather ugly way to convert the keys from the system properties to a typed list of strings.
    List<Object> lKeys = Collections.list(System.getProperties().keys());
    List<String> lNames = new ArrayList<>();
    for (Object lNextObject : lKeys) {
      lNames.add(lNextObject.toString());
    }
    return lNames;
  }

  @Override
  public String getConfigurationValue( String pConfigurationKey ) throws MissingResourceException {
    // Check parameter.
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");

    // Get current system properties.
    Properties lSystemProperties = System.getProperties();

    // Check if property with the passed name exists.
    if (lSystemProperties.containsKey(pConfigurationKey) == true) {
      // Return property value.
      return lSystemProperties.getProperty(pConfigurationKey);
    }
    // System property with the passed name is not defined -> MissingResourceException.
    else {
      String lMessage = "System property with the name '" + pConfigurationKey + "' is not defined.";
      throw new MissingResourceException(lMessage, SYSTEM_PROPERTIES, pConfigurationKey);
    }
  }
}
