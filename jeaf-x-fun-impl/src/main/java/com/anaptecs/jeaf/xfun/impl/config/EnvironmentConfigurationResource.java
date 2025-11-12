/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;

/**
 * Class provides access to configuration values that are defined in operating system environment variables.
 * 
 * @author JEAF Development Team
 * @version 1.3
 */
public final class EnvironmentConfigurationResource implements ConfigurationResource {
  /**
   * String constant that is always used as resource name.
   */
  public static final String OS_ENV_VARIABLES = "OS environment variables";

  /**
   * Map with all environment variables that are available.
   */
  private final Map<String, String> environmentVariables;

  /**
   * Initialize object. Thereby no actions are performed.
   */
  public EnvironmentConfigurationResource( ) {
    environmentVariables = System.getenv();
  }

  /**
   * Method returns the name of the resource. Since system properties are not a named resource always the same name will
   * be returned.
   * 
   * @return String Name of the system properties resource. The method never returns null.
   */
  @Override
  public String getResourceName( ) {
    return OS_ENV_VARIABLES;
  }

  /**
   * Method returns the location of the resource.
   * 
   * @return URL Location of the resource. The method never returns null.
   */
  @Override
  public String getResourceLocation( ) {
    return OS_ENV_VARIABLES;
  }

  @Override
  public boolean hasConfigurationValue( String pConfigurationKey ) {
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");
    return environmentVariables.containsKey(pConfigurationKey);
  }

  @Override
  public List<String> getAllConfigurationKeys( ) {
    Set<String> lKeys = environmentVariables.keySet();
    return new ArrayList<>(lKeys);
  }

  @Override
  public String getConfigurationValue( String pConfigurationKey ) throws MissingResourceException {
    // Check parameter.
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");

    // Check if property with the passed name exists.
    if (environmentVariables.containsKey(pConfigurationKey) == true) {
      // Return property value.
      return environmentVariables.get(pConfigurationKey);
    }
    // System property with the passed name is not defined -> MissingResourceException.
    else {
      String lMessage = "Environment variable with the name '" + pConfigurationKey + "' is not defined.";
      throw new MissingResourceException(lMessage, OS_ENV_VARIABLES, pConfigurationKey);
    }
  }
}
