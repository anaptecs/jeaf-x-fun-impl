/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.anaptecs.jeaf.tools.api.Tools;
import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.tools.api.regexp.RegExpTools;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.info.VersionCompatibilityMode;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.anaptecs.jeaf.xfun.impl.info.VersionInfoHelper;
import org.junit.jupiter.api.Test;

/**
 * Test class to test the functionality of class com.anaptecs.jeaf.common.util.VersionInfo. All version information are
 * read from property file test_version.proeprties.
 *
 * @author JEAF Development Team
 * @version 1.0
 */
public class VersionInfoTest {
  /**
   * Constant for property file that contains all version information.
   */
  private static final String TEST_VERSION_BUNDLE = "test_version.properties";

  /**
   * Method tests all methods of the class version info including exception handling.
   *
   * @throws Exception if an error occurs during the execution of the test case.
   */
  @Test
  public void testVersionInfo( ) throws Exception {
    // Load version information of TEST_VERSION_BUNDEL.
    VersionInfo lVersionInfo = VersionInfoHelper.loadVersionInfo(TEST_VERSION_BUNDLE);

    // Test version information.
    assertEquals(1, lVersionInfo.getMajorVersion());
    assertEquals(2, lVersionInfo.getMinorVersion());
    assertEquals(3, (int) lVersionInfo.getBugfixLevel());
    assertEquals(4, (int) lVersionInfo.getHotfixLevel());

    // Test build information
    assertNull(lVersionInfo.getBuildNumber());
    SimpleDateFormat lFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date lCreationDate = lFormat.parse("2004-11-24  22:05:22.123");
    assertEquals(DateTools.getDateTools().toTimestampString(lCreationDate),
        DateTools.getDateTools().toTimestampString(lVersionInfo.getCreationDate()));
    assertEquals(lCreationDate, lVersionInfo.getCreationDate());

    //
    // Test exception handling.
    //

    // Test bundle name null.
    lVersionInfo = VersionInfoHelper.loadVersionInfo(null);
    assertEquals(VersionInfo.UNKNOWN_VERSION, lVersionInfo, "Dummy version expected");

    // Test property file not found.
    lVersionInfo = VersionInfoHelper.loadVersionInfo("missing_version_info");
    assertEquals(VersionInfo.UNKNOWN_VERSION, lVersionInfo);

    // Test error handling in case of not existing resource.
    VersionInfoHelper.loadVersionInfo("brokentest_version.properties");
  }

  @Test
  public void testVersionInfoCreation( ) {
    Date lDate = new GregorianCalendar(2018, 0, 27).getTime();
    VersionInfo lVersionInfo = new VersionInfo("1.2-SNAPSHOT", lDate);
    assertEquals(1, lVersionInfo.getMajorVersion(), "Wrong major version.");
    assertEquals(2, lVersionInfo.getMinorVersion(), "Wrong minor version.");
    assertNull(lVersionInfo.getBugfixLevel(), "Wrong bugfix level.");
    assertNull(lVersionInfo.getHotfixLevel(), "Wrong hotfix level.");
    assertNull(lVersionInfo.getBuildNumber(), "Wrong build number.");
    assertEquals(lDate, lVersionInfo.getCreationDate(), "Wrong release date");
    assertTrue(lVersionInfo.isSnapshotRelease(), "Wrong snapshot info");
    assertFalse(lVersionInfo.isUnknownVersion(), "Wrong unknown version info");
    assertNotNull(lVersionInfo.getCreationDate(), "Version date must not be null.");
  }

  /**
   * Method tests JEAF's version info implementation for compatibility
   */
  @Test
  public void testVersionInfoCompatibility( ) {
    VersionInfo lVersionInfo = new VersionInfo("1.2.3.4", new Date());
    VersionInfo lSameVersion = new VersionInfo("1.2.3.4", new Date());
    VersionInfo lPreviousBugfixVersion = new VersionInfo("1.2.3.3", new Date());
    VersionInfo lPreviousMinorVersion = new VersionInfo("1.1.4.18", new Date());
    VersionInfo lGrannieVersion = new VersionInfo("1.1.8.9", new Date());
    VersionInfo lOutDatedVersion = new VersionInfo("1.0.2.7", new Date());
    VersionInfo lNewerVersion = new VersionInfo("1.3.0.0", new Date());
    VersionInfo lBrandNewVersion = new VersionInfo("2.0.0.0", new Date());

    // Test strict comparison
    VersionCompatibilityMode lStrict = VersionCompatibilityMode.STRICT;
    assertTrue(lVersionInfo.isCompatible(lSameVersion, lStrict), "Same versions must be compatible");
    assertFalse(lVersionInfo.isCompatible(lPreviousBugfixVersion, lStrict), "Previous version not detected.");
    assertFalse(lVersionInfo.isCompatible(lOutDatedVersion, lStrict), "Out-dated version not detected.");
    assertFalse(lVersionInfo.isCompatible(lGrannieVersion, lStrict), "Grannie version not detected.");
    assertFalse(lVersionInfo.isCompatible(lNewerVersion, lStrict), "Newer version not detected.");
    assertFalse(lVersionInfo.isCompatible(lBrandNewVersion, lStrict), "Brand new version not detected.");

    // Test tolerant comparison
    VersionCompatibilityMode lSemVer = VersionCompatibilityMode.SEMANTIC_VERSIONING;
    assertTrue(lVersionInfo.isCompatible(lSameVersion, lSemVer), "Same versions must be compatible");
    assertTrue(lVersionInfo.isCompatible(lPreviousBugfixVersion, lSemVer),
        "Previous bugfix version must be comaptible.");
    assertFalse(lVersionInfo.isCompatible(lPreviousMinorVersion, lSemVer), "Previous version not detected.");
    assertFalse(lVersionInfo.isCompatible(lOutDatedVersion, lSemVer), "Out-dated version not detected.");
    assertFalse(lVersionInfo.isCompatible(lGrannieVersion, lSemVer), "Grannie version not detected.");
    assertTrue(lVersionInfo.isCompatible(lNewerVersion, lSemVer), "Newer version not detected.");
    assertFalse(lVersionInfo.isCompatible(lBrandNewVersion, lSemVer), "Brand new version not detected.");
  }

  /**
   * Method tests the access to all version information about the JEAF framework.
   *
   * @throws Exception if an error occurs during the execution of the test case.
   */
  @Test
  public void testJEAFVersionInfo( ) throws Exception {
    String lRegexp;
    lRegexp = VersionInfo.VERSION_STRING_PATTERN;

    RegExpTools lRegExpTools = Tools.getRegExpTools();
    String lVersionString = "1.3.0.44.123-SNAPSHOT";
    assertTrue(lRegExpTools.matchesPattern(lVersionString, lRegexp), lVersionString);
    lVersionString = "1.3.0-SNAPSHOT";
    assertTrue(lRegExpTools.matchesPattern(lVersionString, lRegexp), lVersionString);
    lVersionString = "1.3.0.1.2";
    assertTrue(lRegExpTools.matchesPattern(lVersionString, lRegexp), lVersionString);
    lVersionString = "1.1.456";
    assertTrue(lRegExpTools.matchesPattern(lVersionString, lRegexp), lVersionString);
    lVersionString = "1.ewer.3.3";
    assertFalse(lRegExpTools.matchesPattern(lVersionString, lRegexp), lVersionString);
    lVersionString = "1.3";
    assertTrue(lRegExpTools.matchesPattern(lVersionString, lRegexp), lVersionString);

    // Get version information about JEAF as object.
    VersionInfo lVersionInfo = XFun.getVersionInfo();
    assertNotNull(lVersionInfo, "No version information about JEAF available.");

    // Get version information about JEAF as String
    String lVersionInfoString = XFun.getVersionInfo().getVersionString();
    assertNotNull("No version information about JEAF available as String.", lVersionInfoString);
    System.out.println("JEAF version: " + lVersionInfoString);

    // Try to access all attributes.
    System.out.println("major version: " + lVersionInfo.getMajorVersion());
    System.out.println("minor version: " + lVersionInfo.getMinorVersion());
    System.out.println("bugfix level: " + lVersionInfo.getBugfixLevel());
    System.out.println("hotfix level: " + lVersionInfo.getHotfixLevel());
    System.out.println("build number: " + lVersionInfo.getBuildNumber());
  }
}
