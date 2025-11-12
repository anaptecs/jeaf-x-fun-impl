/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.config;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;

/**
 * Class provides access to data that is stored in resource bundles. All other methods to simplify the access to
 * resource bundles are already implemented in super class ResourceAccessProvider.
 * 
 * @author JEAF Development Team
 * @version 1.1
 */
public final class ResourceBundleConfigurationResource implements ConfigurationResource {
  /**
   * Reference to the ResourceBundle object that contains all the values of the property file. The reference is never
   * null, since the resource bundle will be loaded when the object is initialized by its constructor.
   */
  private final ResourceBundle resourceBundle;

  /**
   * Name of the resource bundle that can be access through this class. The attribute is never null since it has to be
   * passed to the class constructor as a not null parameter.
   */
  private final String resourceBundelName;

  /**
   * Location of the resource that can be accessed by this class since the resource will be loaded when the object is
   * initialized by its constructor.
   */
  private final String resourceLocation;

  /**
   * Initialize object. Therefore the name of the resource bundle that should be read has to be passed.
   * 
   * @param pResourceBundelName Name of the resource bundle whose data should be accessed through this class. The
   * parameter must not be null and the resource bundle with the passed name must be located within the application's
   * classpath.
   * @throws java.util.MissingResourceException if no property file with the passed name exists.
   */
  public ResourceBundleConfigurationResource( String pResourceBundelName ) throws MissingResourceException {
    // Check parameter for null.
    Check.checkInvalidParameterNull(pResourceBundelName, "pResourceBundelName");

    // Create new ResourceBundle for the passed bundle.
    resourceBundle = ResourceBundle.getBundle(pResourceBundelName);
    resourceBundelName = pResourceBundelName;

    // In order to find the property file with the passed name the extension ".properties" has to be appended and all
    // the path has to be separated by slashes.
    String lResourceName = pResourceBundelName.replace('.', '/') + ".properties";
    URL lResourceURL = this.getClass().getResource(lResourceName);
    // Try to locate resource as system resource.
    if (lResourceURL == null) {
      lResourceURL = ClassLoader.getSystemResource(lResourceName);
    }
    if (lResourceURL != null) {
      resourceLocation = lResourceURL.toExternalForm();
    }
    else {
      resourceLocation = "unknown";
    }
  }

  /**
   * Method returns the name of the resource.
   * 
   * @return String Name of the resource. The method never returns null.
   */
  @Override
  public String getResourceName( ) {
    return resourceBundelName;
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
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");
    return resourceBundle.containsKey(pConfigurationKey);
  }

  @Override
  public List<String> getAllConfigurationKeys( ) {
    return Collections.list(resourceBundle.getKeys());
  }

  @Override
  public String getConfigurationValue( String pConfigurationKey ) throws MissingResourceException {
    // Check parameter.
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");

    // Get value of property with the passed name and trim it.
    String lValue = resourceBundle.getString(pConfigurationKey);

    // Return trimmed property value.
    return lValue.trim();
  }
}
