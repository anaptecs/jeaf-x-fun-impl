/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl;

import com.anaptecs.jeaf.xfun.annotations.ConfigurationProviderConfig;
import com.anaptecs.jeaf.xfun.annotations.RuntimeInfo;
import com.anaptecs.jeaf.xfun.annotations.TraceConfig;
import com.anaptecs.jeaf.xfun.annotations.XFunConfig;
import com.anaptecs.jeaf.xfun.api.info.RuntimeEnvironment;
import com.anaptecs.jeaf.xfun.impl.checks.VerifierFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.ComponentConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.ConfigurationProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.EnvironmentConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.FileConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.ResourceBundleConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.ResourceConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.SystemPropertiesConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.DatatypeConverterRegistryFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.info.InfoProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.locale.LocaleProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.messages.MessageRepositoryFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.principal.PrinciaplProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.trace.TraceProviderFactoryImpl;

@XFunConfig(
    verifierFactory = VerifierFactoryImpl.class,
    messageRepositoryFactory = MessageRepositoryFactoryImpl.class,
    configurationProviderFactory = ConfigurationProviderFactoryImpl.class,
    localeProviderFactory = LocaleProviderFactoryImpl.class,
    principalProviderFactory = PrinciaplProviderFactoryImpl.class,
    infoProviderFactory = InfoProviderFactoryImpl.class,
    datatypeConverterRegistryFactory = DatatypeConverterRegistryFactoryImpl.class,
    traceProviderFactory = TraceProviderFactoryImpl.class)

@ConfigurationProviderConfig(
    componentConfigurationResourceFactory = ComponentConfigurationResourceFactoryImpl.class,
    environmentConfigurationResourceFactory = EnvironmentConfigurationResourceFactoryImpl.class,
    fileConfigurationResourceFactory = FileConfigurationResourceFactoryImpl.class,
    resourceConfigurationResourceFactory = ResourceConfigurationResourceFactoryImpl.class,
    resourceBundleConfigurationResourceFactory = ResourceBundleConfigurationResourceFactoryImpl.class,
    systemPropertiesConfigurationResourceFactory = SystemPropertiesConfigurationResourceFactoryImpl.class)

@TraceConfig(
    traceMessageFormat = "[%1$5d] %3$-20s %2$s",
    traceWithSystemLocale = true,
    indentSize = 4,
    indentTrace = true)

@RuntimeInfo(runtimeEnvironment = RuntimeEnvironment.JSE)
public interface DefaultXFunConfiguration {
}
