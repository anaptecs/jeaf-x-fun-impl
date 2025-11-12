/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test;

import com.anaptecs.jeaf.xfun.annotations.AppInfo;
import com.anaptecs.jeaf.xfun.annotations.XFunConfig;
import com.anaptecs.jeaf.xfun.impl.test.principal.TestPrinciaplProviderFactoryImpl;

@AppInfo(
    applicationID = "JEAF_XFUN_IMPL_TEST",
    applicationName = "JEAF X-Fun Impl JUnit Tests",
    applicationCreator = "anaptecs GmbH",
    applicationCreatorURL = "http://www.anaptecs.de",
    applicationWebsiteURL = "http://www.jeaf.de",
    applicationDescription = "JUnit tests for JEAF X-Fun Implementation")

@XFunConfig(principalProviderFactory = TestPrinciaplProviderFactoryImpl.class)
public interface MyTestConfiguration {
}
