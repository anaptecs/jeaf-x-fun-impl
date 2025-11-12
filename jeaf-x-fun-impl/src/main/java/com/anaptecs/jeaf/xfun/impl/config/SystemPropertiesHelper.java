/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.anaptecs.jeaf.xfun.api.XFun;

/**
 * Class offers mechanism to replace system properties inside a string.
 * 
 * @author JEAF Development Team
 */
public class SystemPropertiesHelper {
  /**
   * Constructor is private to prevent that instances of this class will be created.
   */
  private SystemPropertiesHelper( ) {
    // Nothing to do.
  }

  /**
   * Method replaces may be existing place holders for system properties inside the passed string. Place holders are
   * defined by a leading '${' and an ending '}'.
   * 
   * @param pValue String inside which system properties should be replaced. The parameter may be null.
   * @return String with replaced system properties. If no system properties are defined inside the string then a same
   * string will be returned. The method may return null.
   */
  public static String replaceSystemProperties( String pValue ) {
    String lResult;
    if (pValue != null) {
      // Detect system properties that need to be replaced.
      Set<String> lSystemProperties = SystemPropertiesHelper.detectSystemProperties(pValue);
      lResult = pValue;

      for (String lPropertyName : lSystemProperties) {
        // Try to resolve value of system property.
        String lPropertyValue = System.getProperty(lPropertyName);
        if (lPropertyValue != null) {
          // Replace all occurrences of the system property with its value.
          lResult = lResult.replaceAll("\\$\\{" + lPropertyName + "\\}", lPropertyValue);
        }
        // We can not substitute the placeholder. System property is not set.
        else {
          String lMessage =
              "Unable to substitue placeholder for system property " + lPropertyName + ". System-Property is not set.";
          XFun.getTrace().warn(lMessage);
        }
      }
    }
    else {
      lResult = null;
    }
    return lResult;
  }

  /**
   * Method detects an "hidden" reference to a system property inside a String.
   * 
   * @param pValue String that should be checked for contained system property references. The parameter must not be
   * null.
   * @return {@link Set} Set containing the names of all system properties that are inside the passed string.
   */
  private static Set<String> detectSystemProperties( String pValue ) {
    Set<String> lSystemProperties;
    if (pValue.length() > 3) {
      lSystemProperties = new HashSet<>();
      int lCurrentIndex = 0;
      StringBuilder lBuilder = new StringBuilder();

      boolean lDone = false;
      while (lDone == false) {
        int lStartIndex = pValue.indexOf("${", lCurrentIndex);
        if (lStartIndex >= 0) {
          // Look for end token
          int lEndIndex = pValue.indexOf("}", lStartIndex);
          if (lEndIndex - 3 > lStartIndex) {
            // Resolve name of system property that should be replaced.
            String lPropertyName = pValue.substring(lStartIndex + 2, lEndIndex);
            lSystemProperties.add(lPropertyName);
            lCurrentIndex = lEndIndex + 1;
          }
          // Invalid system property definition.
          else {
            lBuilder.append(pValue);
            lDone = true;
          }
        }
        // Nothing to replace (any more).
        else {
          lBuilder.append(pValue.substring(lCurrentIndex, pValue.length()));
          lDone = true;
        }
      }
    }
    // String can not contain system properties
    else {
      lSystemProperties = Collections.emptySet();
    }
    return lSystemProperties;
  }

}
