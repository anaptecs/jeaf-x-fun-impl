/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.info;

import java.util.Date;

import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.config.Configuration;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;

/**
 * Class can be used to load version information from a property file and create a version info object out of it.
 * 
 * @author JEAF Development Team
 * @version JEAF Release 1.2
 */
public final class VersionInfoHelper {
  /**
   * Constant for property that contains the version string.
   */
  public static final String VERSION = "VERSION";

  /**
   * Constant for property that contains the build date.
   */
  public static final String CREATION_DATE = "CREATION_DATE";

  /**
   * Initialize objects.
   */
  private VersionInfoHelper( ) {
    // Nothing to do, as there won't exist any instances of this class.
  }

  /**
   * Initialize object with version information that is stored in the property file with the passed name. This
   * constructor is intended to be used by subclasses, to provided access to version information that is stored in a
   * user specific property file e.g. "version.properties".
   * 
   * @param pVersionBundleName Name of the property file that contains the values for all attributes of this object. The
   * Parameter must not be null and the name of a property file that can be found within the applications classpath.
   */
  public static VersionInfo loadVersionInfo( String pVersionBundleName ) {
    // Check parameter for null
    VersionInfo lVersionInfo;
    if (pVersionBundleName != null) {
      // Load property file with version info.
      Configuration lConfiguration = null;
      try {
        // Lookup configuration.
        lConfiguration = XFun.getConfigurationProvider().getResourceConfiguration(pVersionBundleName);

        // Get timestamp of build.
        Date lCreationDate = lConfiguration.getConfigurationValue(CREATION_DATE, true, Date.class);

        // Get Maven version string
        String lMavenVersionString = lConfiguration.getConfigurationValue(VERSION, null, String.class);
        // Create new version info based on Maven version string.
        lVersionInfo = new VersionInfo(lMavenVersionString, lCreationDate);
      }
      // Property file with version information not found
      catch (RuntimeException e) {
        // Unfortunately we can not make a real trace statement here as the version info that is missing is required for
        // this. Thus we will write the error message to system error.
        String lMessage = "Unable to load version info from resource bundle '" + pVersionBundleName
            + "'. Resource was not found in classpath. Please correct your configuration. Using generic version info '"
            + VersionInfo.UNKNOWN_VERSION.toString() + "' as fallback.";
        XFun.getTrace().writeEmergencyTrace(lMessage, e);
        lVersionInfo = VersionInfo.UNKNOWN_VERSION;
      }
    }
    else {
      lVersionInfo = VersionInfo.UNKNOWN_VERSION;
    }
    return lVersionInfo;
  }
}
