/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Set;

import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;

/**
 * Class provides access to data that is stored in resource bundles. All other methods to simplify the access to
 * resource bundles are already implemented in super class ResourceAccessProvider.
 * 
 * @author JEAF Development Team
 * @version 1.1
 */
public final class PropertiesConfigurationResource implements ConfigurationResource {
  /**
   * Reference to the ResourceBundle object that contains all the values of the property file. The reference is never
   * null, since the resource bundle will be loaded when the object is initialized by its constructor.
   */
  private final Properties properties;

  /**
   * Name of the resource bundle that can be access through this class. The attribute is never null since it has to be
   * passed to the class constructor as a not null parameter.
   */
  private final String resourceName;

  /**
   * Location of the resource that can be accessed by this class since the resource will be loaded when the object is
   * initialized by its constructor.
   */
  private final String resourceLocation;

  /**
   * Initialize object. Therefore the name of the resource bundle that should be read has to be passed.
   * 
   * @param pResourceName Name of the resource bundle whose data should be accessed through this class. The parameter
   * must not be null and the resource bundle with the passed name must be located within the application's classpath.
   * @throws java.util.MissingResourceException if no property file with the passed name exists.
   */
  public PropertiesConfigurationResource( String pResourceName ) throws MissingResourceException {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pResourceName, "pResourceName");

    // Try to locate resource
    URL lResourceURL = this.getClass().getClassLoader().getResource(pResourceName);

    // Try to locate resource as system resource if we could not find it directly.
    if (lResourceURL == null) {
      lResourceURL = ClassLoader.getSystemResource(pResourceName);
    }
    if (lResourceURL != null) {
      resourceName = pResourceName;
      resourceLocation = lResourceURL.toExternalForm();
      properties = new Properties();

      // Try to load properties.
      try (InputStream lInputStream = lResourceURL.openStream()) {
        properties.load(lInputStream);
      }
      catch (IOException e) {
        String lMessage = "Unable to read configuration from resource " + pResourceName + ". " + e.getMessage();
        throw new MissingResourceException(lMessage, pResourceName, null);
      }
    }
    else {
      String lMessage =
          "Unable to read configuration from resource " + pResourceName + ". Resource could not be found.";
      throw new MissingResourceException(lMessage, pResourceName, null);
    }
  }

  /**
   * Method returns the name of the resource.
   * 
   * @return String Name of the resource. The method never returns null.
   */
  @Override
  public String getResourceName( ) {
    return resourceName;
  }

  /**
   * Method returns the location of the resource.
   * 
   * @return URL Location of the resource. The method never returns null.
   */
  @Override
  public String getResourceLocation( ) {
    return resourceLocation;
  }

  @Override
  public boolean hasConfigurationValue( String pConfigurationKey ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");

    return properties.containsKey(pConfigurationKey);
  }

  @Override
  public List<String> getAllConfigurationKeys( ) {
    Set<Object> lKeys = properties.keySet();
    List<String> lKeysAsString = new ArrayList<>(lKeys.size());
    for (Object lNextKey : lKeys) {
      lKeysAsString.add(lNextKey.toString());
    }
    return lKeysAsString;
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
      throw new MissingResourceException(lMessage, resourceName, pConfigurationKey);
    }
  }
}
