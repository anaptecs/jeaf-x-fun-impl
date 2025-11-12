/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.info;

import java.net.MalformedURLException;
import java.net.URL;

import com.anaptecs.jeaf.xfun.annotations.AppInfo;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationReader;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.ApplicationProvider;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

public class ApplicationInfoLoader {
  public ApplicationInfo readApplicationInfoFromConfiguredClass( String pConfigurationFileName,
      String pBasePackagePath ) {

    ApplicationInfo lApplicationInfo;
    try {
      // Read app info via annotation.
      ConfigurationReader lReader = new ConfigurationReader();
      Class<?> lAppInfoClass = lReader.readClassFromConfigFile(pConfigurationFileName, pBasePackagePath);

      ApplicationInfoLoader lLoader = new ApplicationInfoLoader();
      lApplicationInfo = lLoader.loadApplicationInfo(lAppInfoClass);
    }
    // No matter what happens we do not want to kill the startup process due to an only missing application and / or
    // version info. Thus we use ApplicationInfo.UNKNOWN_APPLICATION as fallback
    catch (RuntimeException e) {
      lApplicationInfo = ApplicationInfo.UNKNOWN_APPLICATION;
      XFun.getTrace().writeEmergencyTrace("Runtime exception when trying to load application info.", e,
          TraceLevel.WARN);
    }
    return lApplicationInfo;
  }

  /**
   * Method loads JEAF X-Fun Application Info from the passed class.
   * 
   * @param pAppInfoClass Class from which the application info should be read. The parameter may be null.
   * @return {@link ApplicationInfo} Application info that was read from the passed class. If parameter is null or does
   * not provide application info then {@link ApplicationInfo#UNKNOWN_APPLICATION} will be returned. The method never
   * returns null.
   */
  public ApplicationInfo loadApplicationInfo( Class<?> pAppInfoClass ) {
    // App infos are available
    ApplicationInfo lApplicationInfo;
    if (pAppInfoClass != null) {
      AppInfo lAnnotation = pAppInfoClass.getAnnotation(AppInfo.class);

      // Create application info based on the found annotation
      if (lAnnotation != null) {
        // Create application provider
        String lCreator = lAnnotation.applicationCreator();
        URL lCreatorURL = this.toURL(lAnnotation.applicationCreatorURL());
        String lURLString;
        if (lCreatorURL != null) {
          lURLString = lCreatorURL.toExternalForm();
        }
        else {
          lURLString = null;
        }
        ApplicationProvider lApplicationProvider = new ApplicationProvider(lCreator, lURLString);

        // Get information about application from JEAF properties.
        String lApplicationID = lAnnotation.applicationID();
        String lApplicationName = lAnnotation.applicationName();
        URL lApplicationWebsiteURL = this.toURL(lAnnotation.applicationWebsiteURL());
        if (lApplicationWebsiteURL != null) {
          lURLString = lApplicationWebsiteURL.toExternalForm();
        }
        else {
          lURLString = null;
        }
        String lDescription = lAnnotation.applicationDescription();
        VersionInfo lVersionInfo = VersionInfoHelper.loadVersionInfo(lAnnotation.versionInfoPropertyFileName());

        // Create ApplicationInfo object.
        lApplicationInfo = new ApplicationInfo(lApplicationID, lApplicationName, lURLString, lDescription,
            lApplicationProvider, lVersionInfo);
      }
      else {
        lApplicationInfo = ApplicationInfo.UNKNOWN_APPLICATION;
        XFun.getTrace().writeInitInfo(
            "Configured class " + pAppInfoClass.getName()
                + " does not have required annotation @AppInfo. Using fallback to generic application info.",
            TraceLevel.WARN);
      }
    }
    // No app info available so we work with a default
    else {
      lApplicationInfo = ApplicationInfo.UNKNOWN_APPLICATION;
      XFun.getTrace().writeInitInfo(
          "Application info is not available. Please make use of annotation @AppInfo. Using fallback to generic application info.",
          TraceLevel.WARN);
    }
    return lApplicationInfo;
  }

  /**
   * Method tries to convert the passed string into an URL object if possible
   * 
   * @param pURLString String that should be converted into an URL. The parameter may be null.
   * @return {@link URL} URL object that was created from the passed string. If parameter is not a valid URL or null
   * then the method will return null.
   */
  private URL toURL( String pURLString ) {
    URL lURL;
    try {
      return new URL(pURLString);
    }
    catch (MalformedURLException e) {
      lURL = null;
    }
    return lURL;
  }
}
