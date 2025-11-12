/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.config;

import com.anaptecs.jeaf.xfun.annotations.StartupInfoWriterImpl;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.config.ComponentConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.Configuration;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProvider;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;
import com.anaptecs.jeaf.xfun.api.config.EnvironmentConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.FileConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ResourceBundleConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ResourceConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.SystemPropertiesConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoWriter;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

/**
 * Class collects all the different types of resource access providers that are required for the different source of
 * configuration parameters.
 */
@StartupInfoWriterImpl
public final class ConfigurationProviderImpl implements ConfigurationProvider, StartupInfoWriter {

  /**
   * Reference to configured ComponentConfigurationProvider.
   */
  private final ComponentConfigurationResourceFactory componentConfigurationResourceFactory;

  /**
   * Reference to configured EnvironmentConfigurationProvider.
   */
  private final EnvironmentConfigurationResourceFactory environmentConfigurationResourceFactory;

  /**
   * Reference to configured FileConfigurationProvider.
   */
  private final FileConfigurationResourceFactory fileConfigurationResourceFactory;

  /**
   * Reference to configured ResourceBundleConfigurationProvider.
   */
  private final ResourceConfigurationResourceFactory resourceConfigurationResourceFactory;

  /**
   * Reference to configured ResourceBundleConfigurationProvider.
   */
  private final ResourceBundleConfigurationResourceFactory resourceBundleConfigurationProvider;

  /**
   * Reference to configured SystemPropertiesConfigurationProvider.
   */
  private final SystemPropertiesConfigurationResourceFactory systemPropertiesConfigurationProvider;

  /**
   * Initialize object. All configured configuration providers will be resolved when the object is created.
   */
  public ConfigurationProviderImpl( ) {
    // Lookup all provider that are configured.
    ConfigurationProviderConfiguration lConfig = ConfigurationProviderConfiguration.getInstance();
    componentConfigurationResourceFactory = lConfig.getComponentConfigurationResourceFactory();
    environmentConfigurationResourceFactory = lConfig.getEnvironmentConfigurationResourceFactory();
    fileConfigurationResourceFactory = lConfig.getFileConfigurationResourceFactory();
    resourceConfigurationResourceFactory = lConfig.getResourceConfigurationResourceFactory();
    resourceBundleConfigurationProvider = lConfig.getResourceBundleConfigurationResourceFactory();
    systemPropertiesConfigurationProvider = lConfig.getSystemPropertiesConfigurationResourceFactory();
  }

  @Override
  public Configuration getComponentConfiguration( ComponentID pComponentID ) throws JEAFSystemException {
    ConfigurationResource lResource =
        componentConfigurationResourceFactory.getComponentConfigurationResource(pComponentID);
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getEnvironmentConfiguration( ) {
    ConfigurationResource lResource = environmentConfigurationResourceFactory.getEnvironmentConfigurationResource();
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getFileConfiguration( String pFileName ) throws JEAFSystemException {
    ConfigurationResource lResource = fileConfigurationResourceFactory.getFileConfigurationResource(pFileName);
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getResourceConfiguration( String pResourceName ) {
    ConfigurationResource lResource =
        resourceConfigurationResourceFactory.getResourceConfigurationResource(pResourceName);
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getResourceBundleConfiguration( String pResourceBundleName ) {
    ConfigurationResource lResource =
        resourceBundleConfigurationProvider.getResourceBundleConfigurationResource(pResourceBundleName);
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getSystemPropertiesConfiguration( ) {
    ConfigurationResource lResource = systemPropertiesConfigurationProvider.getSystemPropertiesConfigurationResource();
    return new ConfigurationImpl(lResource);
  }

  /**
   * Method replaces may be existing place holders for system properties inside the passed string. Place holders are
   * defined by a leading '${' and an ending '}'.
   * 
   * @param pValue String inside which system properties should be replaced. The parameter may be null.
   * @return String with replaced system properties. If no system properties are defined inside the string then a same
   * string will be returned. The method may return null.
   */
  @Override
  public String replaceSystemProperties( String pValue ) {
    return SystemPropertiesHelper.replaceSystemProperties(pValue);
  }

  @Override
  public Class<?> getStartupCompletedEventSource( ) {
    return XFun.class;
  }

  @Override
  public void traceStartupInfo( Trace pTrace, TraceLevel pTraceLevel ) {
    pTrace.writeInitInfo("JEAF X-Fun uses the following configuration providers:", pTraceLevel);
    pTrace.writeInitInfo("Component-Impl:             " + componentConfigurationResourceFactory.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("Environment-Impl:           " + environmentConfigurationResourceFactory.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("File-Configuration:         " + fileConfigurationResourceFactory.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("Resource-Impl:              " + resourceConfigurationResourceFactory.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("Resource-Bundle-Impl:       " + resourceBundleConfigurationProvider.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("System-Properties-Impl:     " + systemPropertiesConfigurationProvider.getClass().getName(),
        pTraceLevel);
  }
}
