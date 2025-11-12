/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.info;

import com.anaptecs.jeaf.xfun.annotations.AppInfo;
import com.anaptecs.jeaf.xfun.annotations.RuntimeInfo;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationReader;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.InfoProvider;
import com.anaptecs.jeaf.xfun.api.info.JavaRuntimeEnvironment;
import com.anaptecs.jeaf.xfun.api.info.OperatingSystem;
import com.anaptecs.jeaf.xfun.api.info.RuntimeEnvironment;

/**
 * Class implements a so called info provider. Info provides can be used to find out information about the application
 * and runtime environment we are running in.
 * 
 * @author JEAF Development Team
 */
public class InfoProviderImpl implements InfoProvider {
  /**
   * Information about this application we are currently running inside
   */
  private ApplicationInfo applicationInfo;

  /**
   * Runtime environment in which we are currently executed.
   */
  private RuntimeEnvironment runtimeEnvironment;

  /**
   * Information about current Java Runtime Environment. As this information is valid until the JVM exits we can resolve
   * it once and then keep it.
   */
  private JavaRuntimeEnvironment javaRuntimeEnvironment;

  /**
   * Method returns information about this application.
   * 
   * @return {@link ApplicationInfo} Information about this application. The method never returns null.
   */
  @Override
  public ApplicationInfo getApplicationInfo( ) {
    // In order to avoid problems to initialize JEAF due to configuration problems of the business application the
    // application info is loaded on its first access.
    if (applicationInfo == null) {
      ApplicationInfoLoader lLoader = new ApplicationInfoLoader();
      applicationInfo =
          lLoader.readApplicationInfoFromConfiguredClass(AppInfo.APP_INFO_RESOURCE_NAME, XFun.X_FUN_BASE_PATH);
    }
    // Return application info
    return applicationInfo;
  }

  /**
   * Method returns the operating system under which the application is currently executed.
   * 
   * @return {@link OperatingSystem} Operating system and which the application is currently executed. The method never
   * returns null.
   */
  @Override
  public OperatingSystem getOperatingSystem( ) {
    // Get current operating system name from system properties.
    String lOSName = System.getProperty("os.name").toLowerCase();

    // Any kind of Windows operating systems
    OperatingSystem lOperatingSystem;
    if (lOSName.indexOf("win") >= 0) {
      lOperatingSystem = OperatingSystem.WINDOWS;
    }
    // Mac OS
    else if (lOSName.indexOf("mac") >= 0) {
      lOperatingSystem = OperatingSystem.MAC;
    }
    // Any UNIX
    else if (lOSName.indexOf("nix") >= 0 || lOSName.indexOf("aix") >= 0 || lOSName.indexOf("sunos") >= 0
        || lOSName.indexOf("solaris") >= 0 || lOSName.indexOf("hp-ux") >= 0) {
      lOperatingSystem = OperatingSystem.UNIX;
    }
    // Linux
    else if (lOSName.indexOf("nux") >= 0) {
      lOperatingSystem = OperatingSystem.LINUX;
    }
    // Any other operating system.
    else {
      lOperatingSystem = OperatingSystem.OTHER;

    }
    return lOperatingSystem;
  }

  /**
   * Method returns the runtime environment as defined in the JEAF properties in that the application is currently
   * executed.
   * 
   * @return {@link RuntimeEnvironment} Object describing the defined runtime environment. The method never returns
   * null.
   */
  @Override
  public RuntimeEnvironment getRuntimeEnvironment( ) {
    // In order to avoid problems to initialize JEAF due to configuration problems of the business application the
    // runtime info is loaded on its first access.
    if (runtimeEnvironment == null) {
      // Read app info via annotation.
      ConfigurationReader lReader = new ConfigurationReader();
      Class<?> lRuntimeInfoClass =
          lReader.readClassFromConfigFile(RuntimeInfo.RUNTIME_INFO_RESORUCE_NAME, XFun.X_FUN_BASE_PATH);
      RuntimeEnvironmentLoader lLoader = new RuntimeEnvironmentLoader();
      runtimeEnvironment = lLoader.loadRuntimeEnvironment(lRuntimeInfoClass);
    }

    return runtimeEnvironment;
  }

  /**
   * Method returns information about the current Java Runtime environment such as its major release. These information
   * are derived from the system properties that are provided.
   * 
   * @return {@link JavaRuntimeEnvironment} Information about current Java Runtime Environment.
   */
  @Override
  public JavaRuntimeEnvironment getJavaRuntimeEnvironment( ) {
    // Java Runtime was not yet analyzed.
    if (javaRuntimeEnvironment == null) {
      String lRuntimeName = System.getProperty("java.runtime.name");
      String lVersion = System.getProperty("java.version");
      String lVendor = System.getProperty("java.vm.vendor");
      javaRuntimeEnvironment = new JavaRuntimeEnvironment(lRuntimeName, lVendor, lVersion);
    }
    return javaRuntimeEnvironment;
  }

}
