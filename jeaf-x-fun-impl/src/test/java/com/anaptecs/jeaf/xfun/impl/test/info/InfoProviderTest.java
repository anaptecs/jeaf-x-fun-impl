/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.annotations.AppInfo;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.InfoProvider;
import com.anaptecs.jeaf.xfun.api.info.JavaRelease;
import com.anaptecs.jeaf.xfun.api.info.JavaRuntimeEnvironment;
import com.anaptecs.jeaf.xfun.api.info.OperatingSystem;
import com.anaptecs.jeaf.xfun.api.info.RuntimeEnvironment;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.anaptecs.jeaf.xfun.impl.info.ApplicationInfoLoader;
import com.anaptecs.jeaf.xfun.impl.info.RuntimeEnvironmentLoader;
import com.anaptecs.jeaf.xfun.impl.test.MyTestConfiguration;
import com.anaptecs.jeaf.xfun.impl.trace.formatter.ApplicationInfoFormatter;
import org.junit.jupiter.api.Test;

public class InfoProviderTest {
  /**
   * Test cases checks the operating system detection implemented by JEAF.
   */
  @Test
  public void testOSDetection( ) {
    // Check OS detection
    String lCurrentOS = System.getProperty("os.name");

    try {
      System.setProperty("os.name", "Linux");
      assertEquals(OperatingSystem.LINUX, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Mac OS");
      assertEquals(OperatingSystem.MAC, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Mac OS X");
      assertEquals(OperatingSystem.MAC, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows 95");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows 98");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows Me");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows NT");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows 2000");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows XP");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows 2003");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows CE");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows 7");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows 8");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Windows Server 2013");
      assertEquals(OperatingSystem.WINDOWS, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "OS/2");
      assertEquals(OperatingSystem.OTHER, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Solaris  2.x");
      assertEquals(OperatingSystem.UNIX, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "SunOS");
      assertEquals(OperatingSystem.UNIX, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "MPE/iX");
      assertEquals(OperatingSystem.OTHER, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "HP-UX");
      assertEquals(OperatingSystem.UNIX, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "AIX");
      assertEquals(OperatingSystem.UNIX, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "OS/390");
      assertEquals(OperatingSystem.OTHER, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "FreeBSD");
      assertEquals(OperatingSystem.OTHER, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Irix");
      assertEquals(OperatingSystem.OTHER, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "Digital Unix");
      assertEquals(OperatingSystem.UNIX, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "NetWare");
      assertEquals(OperatingSystem.OTHER, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "OSF1");
      assertEquals(OperatingSystem.OTHER, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
      System.setProperty("os.name", "OpenVMS");
      assertEquals(OperatingSystem.OTHER, XFun.getInfoProvider().getOperatingSystem(), "Wrong operating system");
    }
    finally {
      System.setProperty("os.name", lCurrentOS);
    }
  }

  @Test
  public void testApplicationInfoLoader( ) {
    ApplicationInfoLoader lLoader = new ApplicationInfoLoader();
    ApplicationInfo lApplicationInfo = lLoader.loadApplicationInfo(MyTestConfiguration.class);
    assertEquals("JEAF_XFUN_IMPL_TEST", lApplicationInfo.getApplicationID());
    assertEquals("JEAF X-Fun Impl JUnit Tests", lApplicationInfo.getName());
    assertEquals("anaptecs GmbH", lApplicationInfo.getApplicationProvider().getCreator());
    assertEquals("JUnit tests for JEAF X-Fun Implementation", lApplicationInfo.getDescription());
    assertNotNull(lApplicationInfo.getVersion());

    // Test exception handling
    lApplicationInfo = lLoader.loadApplicationInfo(null);
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION, lApplicationInfo);

    lApplicationInfo = lLoader.loadApplicationInfo(XAppInfo1.class);
    assertEquals("XFUN_IMPL_TEST", lApplicationInfo.getApplicationID());
    assertEquals("JEAF X-Fun Implementation Tests", lApplicationInfo.getName());
    assertEquals("JEAF Development Team", lApplicationInfo.getApplicationProvider().getCreator());
    assertEquals(null, lApplicationInfo.getApplicationProvider().getCreatorURL());
    assertEquals(null, lApplicationInfo.getWebsiteURL());
    assertEquals("", lApplicationInfo.getDescription());

    lApplicationInfo = lLoader.loadApplicationInfo(String.class);
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION, lApplicationInfo);

    lApplicationInfo =
        lLoader.readApplicationInfoFromConfiguredClass(AppInfo.APP_INFO_RESOURCE_NAME, XFun.X_FUN_BASE_PATH);
    assertEquals("JEAF_XFUN_IMPL_TEST", lApplicationInfo.getApplicationID());
    assertEquals("JEAF X-Fun Impl JUnit Tests", lApplicationInfo.getName());
    assertEquals("anaptecs GmbH", lApplicationInfo.getApplicationProvider().getCreator());
    assertEquals("JUnit tests for JEAF X-Fun Implementation", lApplicationInfo.getDescription());
    assertNotNull(lApplicationInfo.getVersion());

    // Test exception handling
    lApplicationInfo = lLoader.readApplicationInfoFromConfiguredClass("wrong_appinfo", null);
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION, lApplicationInfo);

    lApplicationInfo = lLoader.readApplicationInfoFromConfiguredClass("path_to_nowhere", null);
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION, lApplicationInfo);

    lApplicationInfo = lLoader.readApplicationInfoFromConfiguredClass(null, null);
    assertEquals(ApplicationInfo.UNKNOWN_APPLICATION, lApplicationInfo);
  }

  @Test
  public void testApplicationInfo( ) {
    // Test company and product name.
    ApplicationInfo lApplicationInfo = XFun.getInfoProvider().getApplicationInfo();
    assertEquals("JEAF_XFUN_IMPL_TEST", lApplicationInfo.getApplicationID());
    assertEquals("JEAF X-Fun Impl JUnit Tests", lApplicationInfo.getName());
    assertEquals("anaptecs GmbH", lApplicationInfo.getApplicationProvider().getCreator());
    assertEquals("JUnit tests for JEAF X-Fun Implementation", lApplicationInfo.getDescription());
    assertNotNull(lApplicationInfo.getVersion(), "Application version must be available.");

    // Test toString conversion
    String lExpected = lApplicationInfo.getName() + " (Version: " + lApplicationInfo.getVersion().toString()
        + ", App-ID: " + lApplicationInfo.getApplicationID() + ")";
    assertEquals(lExpected, lApplicationInfo.toString(), "Wrong ApplicationInfo.toString() result");

    ApplicationInfo lNewAppInfo = new ApplicationInfo(lApplicationInfo.getApplicationID(), lApplicationInfo.getName(),
        lApplicationInfo.getWebsiteURL(), lApplicationInfo.getDescription(), lApplicationInfo.getApplicationProvider(),
        VersionInfo.UNKNOWN_VERSION);
    lExpected = lApplicationInfo.getName() + " (App-ID: " + lApplicationInfo.getApplicationID() + ")";
    assertEquals(lExpected, lNewAppInfo.toString(), "Wrong ApplicationInfo.toString() result");
  }

  @Test
  public void testRuntimeEnvironmentLoader( ) {
    RuntimeEnvironmentLoader lLoader = new RuntimeEnvironmentLoader();
    RuntimeEnvironment lRuntimeEnvironment = lLoader.loadRuntimeEnvironment(XRuntimeInfo.class);
    assertEquals(RuntimeEnvironment.EJB_CONTAINER, lRuntimeEnvironment);

    // Test exception handling
    lRuntimeEnvironment = lLoader.loadRuntimeEnvironment(null);
    assertEquals(RuntimeEnvironment.UNKNOWN, lRuntimeEnvironment);

    lRuntimeEnvironment = lLoader.loadRuntimeEnvironment(String.class);
    assertEquals(RuntimeEnvironment.UNKNOWN, lRuntimeEnvironment);
  }

  @Test
  public void testRuntimeDetection( ) {
    RuntimeEnvironment lRuntimeEnvironment = XFun.getInfoProvider().getRuntimeEnvironment();
    assertEquals(RuntimeEnvironment.WEB_CONTAINER, lRuntimeEnvironment);
    lRuntimeEnvironment = XFun.getInfoProvider().getRuntimeEnvironment();
    assertEquals(RuntimeEnvironment.WEB_CONTAINER, lRuntimeEnvironment);
    lRuntimeEnvironment = XFun.getInfoProvider().getRuntimeEnvironment();
    assertEquals(RuntimeEnvironment.WEB_CONTAINER, lRuntimeEnvironment);
  }

  @Test
  public void testApplicationInfoFormatter( ) {
    ApplicationInfoFormatter lFormatter = new ApplicationInfoFormatter();
    ApplicationInfo lApplicationInfo = XFun.getInfoProvider().getApplicationInfo();
    String lOutput = lFormatter.formatObject(lApplicationInfo, null);
    Date lCreationDate = lApplicationInfo.getVersion().getCreationDate();

    String lExpected = lApplicationInfo.getName() + " " + lApplicationInfo.getVersion().getVersionString()
        + " (Build date: " + DateTools.getDateTools().toDateTimeString(lCreationDate) + ")";
    assertEquals(lExpected, lOutput);
  }

  @Test
  void testJREDetection( ) {
    // java.version=1.8.0_172
    final String lRealRuntimeName = System.getProperty("java.runtime.name");
    final String lRealVendor = System.getProperty("java.vm.vendor");
    final String lRealVersion = System.getProperty("java.version");

    try {
      // "My Java Runtime", "Crazy Java Company", "17.1.2.4.5"
      System.setProperty("java.runtime.name", "My Java Runtime");
      System.setProperty("java.vm.vendor", "Crazy Java Company");
      System.setProperty("java.version", "17.1.2.4.5");

      InfoProvider lInfoProvider = XFun.getInfoProvider();
      JavaRuntimeEnvironment lJRE = lInfoProvider.getJavaRuntimeEnvironment();
      assertEquals("My Java Runtime", lJRE.getRuntimeName());
      assertEquals("Crazy Java Company", lJRE.getVendor());
      assertEquals(JavaRelease.JAVA_17, lJRE.getJavaRelease());
      assertEquals("17.1.2.4.5", lJRE.getVersion());

      // Test ensure full test coverage we access JRE information a second time.
      lJRE = lInfoProvider.getJavaRuntimeEnvironment();
      assertEquals("My Java Runtime", lJRE.getRuntimeName());
      assertEquals("Crazy Java Company", lJRE.getVendor());
      assertEquals(JavaRelease.JAVA_17, lJRE.getJavaRelease());
      assertEquals("17.1.2.4.5", lJRE.getVersion());
    }
    finally {
      System.setProperty("java.runtime.name", lRealRuntimeName);
      System.setProperty("java.vm.vendor", lRealVendor);
      System.setProperty("java.version", lRealVersion);
    }

  }
}
