/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import java.util.MissingResourceException;

import com.anaptecs.jeaf.xfun.annotations.ConfigurationProviderConfig;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.config.Configuration;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.info.OperatingSystem;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;
import com.anaptecs.jeaf.xfun.impl.config.ComponentConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.ConfigurationProviderConfiguration;
import com.anaptecs.jeaf.xfun.impl.config.ConfigurationProviderImpl;
import com.anaptecs.jeaf.xfun.impl.config.EnvironmentConfigurationResource;
import com.anaptecs.jeaf.xfun.impl.config.EnvironmentConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.FileConfigurationResource;
import com.anaptecs.jeaf.xfun.impl.config.FileConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.PropertiesConfigurationResource;
import com.anaptecs.jeaf.xfun.impl.config.ResourceBundleConfigurationResource;
import com.anaptecs.jeaf.xfun.impl.config.ResourceBundleConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.ResourceConfigurationResourceFactoryImpl;
import com.anaptecs.jeaf.xfun.impl.config.SystemPropertiesConfigurationResource;
import com.anaptecs.jeaf.xfun.impl.config.SystemPropertiesConfigurationResourceFactoryImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigurationProviderTest {
  @Test
  @Order(10)
  public void testConfigurationProviderConfiguration( ) {
    ConfigurationProviderConfiguration lConfiguration = ConfigurationProviderConfiguration.getInstance();

    // Check if expected factories are present
    assertEquals(ComponentConfigurationResourceFactoryImpl.class,
        lConfiguration.getComponentConfigurationResourceFactory().getClass(), "Wrong factory found.");

    assertEquals(EnvironmentConfigurationResourceFactoryImpl.class,
        lConfiguration.getEnvironmentConfigurationResourceFactory().getClass(), "Wrong factory found.");
    assertEquals(FileConfigurationResourceFactoryImpl.class,
        lConfiguration.getFileConfigurationResourceFactory().getClass(), "Wrong factory found.");
    assertEquals(ResourceBundleConfigurationResourceFactoryImpl.class,
        lConfiguration.getResourceBundleConfigurationResourceFactory().getClass(), "Wrong factory found.");
    assertEquals(ResourceConfigurationResourceFactoryImpl.class,
        lConfiguration.getResourceConfigurationResourceFactory().getClass(), "Wrong factory found.");
    assertEquals(SystemPropertiesConfigurationResourceFactoryImpl.class,
        lConfiguration.getSystemPropertiesConfigurationResourceFactory().getClass(), "Wrong factory found.");

    ConfigurationProviderConfig lEmptyConfiguration = lConfiguration.getEmptyConfiguration();
    assertEquals(ConfigurationProviderConfig.class, lEmptyConfiguration.annotationType());
    assertNull(lEmptyConfiguration.componentConfigurationResourceFactory());
    assertNull(lEmptyConfiguration.environmentConfigurationResourceFactory());
    assertNull(lEmptyConfiguration.fileConfigurationResourceFactory());
    assertNull(lEmptyConfiguration.resourceBundleConfigurationResourceFactory());
    assertNull(lEmptyConfiguration.resourceConfigurationResourceFactory());
    assertNull(lEmptyConfiguration.systemPropertiesConfigurationResourceFactory());

    List<String> lErrors = lConfiguration.checkCustomConfiguration(lEmptyConfiguration);
    assertEquals("Unable to create new instance of configured class. Configured class is 'null'.", lErrors.get(0));
    assertEquals(5, lErrors.size());

  }

  @Test
  @Order(30)
  public void testStartupTrace( ) {
    ConfigurationProviderImpl lConfigurationProvider = new ConfigurationProviderImpl();
    lConfigurationProvider.traceStartupInfo(XFun.getTrace(), TraceLevel.INFO);
    lConfigurationProvider.traceStartupInfo(XFun.getTrace(), null);
  }

  @Test
  @Order(40)
  public void testSystemPropertyReplacement( ) {
    ConfigurationProviderImpl lConfigurationProvider = new ConfigurationProviderImpl();

    System.setProperty("key1", "value1");
    System.setProperty("key2", "value2");

    assertEquals("value1", lConfigurationProvider.replaceSystemProperties("${key1}"));
    assertEquals("value2", lConfigurationProvider.replaceSystemProperties("${key2}"));
    assertEquals("${key3}", lConfigurationProvider.replaceSystemProperties("${key3}"));
    assertEquals("Hello", lConfigurationProvider.replaceSystemProperties("Hello"));
    assertEquals("abc", lConfigurationProvider.replaceSystemProperties("abc"));
    assertEquals("${}", lConfigurationProvider.replaceSystemProperties("${}"));
    assertEquals("}${", lConfigurationProvider.replaceSystemProperties("}${"));
    assertEquals("abcdefg${}xyz", lConfigurationProvider.replaceSystemProperties("abcdefg${}xyz"));
    assertNull(lConfigurationProvider.replaceSystemProperties(null));

    System.setProperty("key1", "Hello");
    System.setProperty("key2", "World!");
    assertEquals("fdnsfn Hello World! dgadg Hello",
        lConfigurationProvider.replaceSystemProperties("fdnsfn ${key1} ${key2} dgadg ${key1}"));
  }

  @Test
  public void testResourceBundleConfiguration( ) {
    ResourceBundleConfigurationResource lResource =
        new ResourceBundleConfigurationResource("BooleanPropertyAccessTest");
    assertNotNull(lResource);
    String lResourceLocation = lResource.getResourceLocation();
    assertTrue(lResourceLocation.endsWith("/jeaf-x-fun-impl/target/test-classes/BooleanPropertyAccessTest.properties"));

    assertEquals("BooleanPropertyAccessTest", lResource.getResourceName());

    List<String> lAllConfigurationKeys = lResource.getAllConfigurationKeys();
    assertTrue(lAllConfigurationKeys.contains("BooleanPropertyKey_2"));
    assertTrue(lAllConfigurationKeys.contains("BooleanPropertyKey_1"));
    assertTrue(lAllConfigurationKeys.contains("BooleanPropertyKey_4"));
    assertTrue(lAllConfigurationKeys.contains("BooleanPropertyKey_3"));
    assertTrue(lAllConfigurationKeys.contains("UnsetBooleanPropertyKey"));
    assertEquals(5, lAllConfigurationKeys.size());

    assertTrue(lResource.hasConfigurationValue("BooleanPropertyKey_2"));
    assertTrue(lResource.hasConfigurationValue("BooleanPropertyKey_1"));
    assertTrue(lResource.hasConfigurationValue("BooleanPropertyKey_4"));
    assertTrue(lResource.hasConfigurationValue("BooleanPropertyKey_3"));
    assertTrue(lResource.hasConfigurationValue("UnsetBooleanPropertyKey"));
    assertFalse(lResource.hasConfigurationValue("MissingKey"));

    String lValue = lResource.getConfigurationValue("UnsetBooleanPropertyKey");
    assertEquals("", lValue);

    try {
      lResource.getConfigurationValue("NotExistingEntry");
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("NotExistingEntry", e.getKey());
    }

    try {
      lResource.getConfigurationValue(null);
      fail("Exception expected");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testSystemPropertiesConfiguration( ) {
    SystemPropertiesConfigurationResource lResource = new SystemPropertiesConfigurationResource();
    assertEquals("JVM system properties", lResource.getResourceLocation());
    assertEquals("JVM system properties", lResource.getResourceName());

    List<String> lAllConfigurationKeys = lResource.getAllConfigurationKeys();
    assertTrue(lAllConfigurationKeys.contains("os.version"));
    assertFalse(lAllConfigurationKeys.contains("jeaf.version"));
    assertFalse(lAllConfigurationKeys.contains("test.key"));
    assertFalse(lResource.hasConfigurationValue("test.key"));

    System.setProperty("test.key", "xyz");
    assertTrue(lResource.hasConfigurationValue("test.key"));
    assertTrue(lResource.hasConfigurationValue("test.key"));

    try {
      lResource.getConfigurationValue("NotExistingEntry");
      fail("Exception expected");
    }
    catch (MissingResourceException e) {
      // Nothing to do.
    }

    try {
      lResource.getConfigurationValue(null);
      fail("Exception expected");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testEnvironmentConfigurationResource( ) {
    EnvironmentConfigurationResource lResource = new EnvironmentConfigurationResource();
    Configuration lEnvironmentConfiguration = XFun.getConfigurationProvider().getEnvironmentConfiguration();

    assertEquals("OS environment variables", lResource.getResourceLocation());
    assertEquals("OS environment variables", lResource.getResourceName());
    List<String> lAllConfigurationKeys = lResource.getAllConfigurationKeys();
    System.out.println("Environment Variables:" + lAllConfigurationKeys);

    OperatingSystem lOperatingSystem = XFun.getInfoProvider().getOperatingSystem();
    if (lOperatingSystem == OperatingSystem.WINDOWS) {
      assertTrue(lAllConfigurationKeys.contains("ALLUSERSPROFILE"));
      assertTrue(lResource.hasConfigurationValue("ALLUSERSPROFILE"));

      assertEquals("C:\\ProgramData", lResource.getConfigurationValue("ALLUSERSPROFILE"));
      assertEquals("C:\\ProgramData", lEnvironmentConfiguration.getConfigurationValue("ALLUSERSPROFILE", String.class));

      assertFalse(lAllConfigurationKeys.contains("HOME"));
      assertFalse(lResource.hasConfigurationValue("HOME"));
    }
    else if (lOperatingSystem == OperatingSystem.LINUX) {
      assertTrue(lAllConfigurationKeys.contains("HOME"));
      assertTrue(lResource.hasConfigurationValue("HOME"));

      assertEquals("/home/runner", lResource.getConfigurationValue("HOME"));

      assertFalse(lAllConfigurationKeys.contains("ALLUSERSPROFILE"));
      assertFalse(lResource.hasConfigurationValue("ALLUSERSPROFILE"));
    }
    else if (lOperatingSystem == OperatingSystem.MAC) {
      assertTrue(lAllConfigurationKeys.contains("HOME"));
      assertTrue(lResource.hasConfigurationValue("HOME"));

      assertTrue(lResource.getConfigurationValue("HOME").startsWith("/Users/"));

      assertFalse(lAllConfigurationKeys.contains("ALLUSERSPROFILE"));
      assertFalse(lResource.hasConfigurationValue("ALLUSERSPROFILE"));
    }
    else {
      fail("Tests are executed on unexpected plattform.");
    }

    // Try to access environment variable that does not exist.
    try {
      lResource.getConfigurationValue("NotExistingEntry");
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("NotExistingEntry", e.getKey());
    }

    try {
      lResource.getConfigurationValue(null);
      fail("Exception expected");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testPropertiesConfiguration( ) {
    PropertiesConfigurationResource lResource = new PropertiesConfigurationResource("test_version.properties");

    assertTrue(lResource.getResourceLocation().endsWith("jeaf-x-fun-impl/target/test-classes/test_version.properties"));
    assertEquals("test_version.properties", lResource.getResourceName());

    List<String> lAllConfigurationKeys = lResource.getAllConfigurationKeys();
    assertTrue(lAllConfigurationKeys.contains("VERSION"));
    assertTrue(lAllConfigurationKeys.contains("CREATION_DATE"));
    assertTrue(lResource.hasConfigurationValue("CREATION_DATE"));
    assertFalse(lResource.hasConfigurationValue("version"));
    assertEquals(2, lAllConfigurationKeys.size());

    assertEquals("1.2.3.4", lResource.getConfigurationValue("VERSION"));

    // Try to access property that does not exist.
    try {
      lResource.getConfigurationValue("NotExistingEntry");
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("NotExistingEntry", e.getKey());
    }

    try {
      lResource.getConfigurationValue(null);
      fail("Exception expected");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testComponentConfiguration( ) {
    ComponentID lComponentID = new ComponentID("Component1", "com.anaptecs.jeaf.xfun.test.component1");
    Configuration lConfiguration = XFun.getConfigurationProvider().getComponentConfiguration(lComponentID);

    // Test exception handling
    try {
      XFun.getConfigurationProvider()
          .getComponentConfiguration(new ComponentID("Component2", "com.anaptecs.jeaf.xfun.test.component2"));
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
    }
    // Try to access property that does not exist.
    try {
      lConfiguration.getConfigurationValue("NotExistingEntry", true, String.class);
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("NotExistingEntry", e.getKey());
    }

    try {
      lConfiguration.getConfigurationValue(null, false, String.class);
      fail("Exception expected");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testFileConfigurationResource( ) throws IOException {
    FileConfigurationResource lConfiguration = new FileConfigurationResource("./testdata/test_version.file");

    OperatingSystem lOperatingSystem = XFun.getInfoProvider().getOperatingSystem();
    if (lOperatingSystem == OperatingSystem.WINDOWS) {
      assertTrue(lConfiguration.getResourceLocation().endsWith("\\testdata\\test_version.file"));
    }
    else if (lOperatingSystem == OperatingSystem.LINUX || lOperatingSystem == OperatingSystem.MAC) {
      assertTrue(lConfiguration.getResourceLocation().endsWith("/testdata/test_version.file"));
    }
    else {
      fail("Unexpected operatin system.");
    }
    assertEquals("test_version.file", lConfiguration.getResourceName());

    List<String> lAllConfigurationKeys = lConfiguration.getAllConfigurationKeys();
    assertTrue(lAllConfigurationKeys.contains("VERSION"));
    assertTrue(lAllConfigurationKeys.contains("CREATION_DATE"));
    assertTrue(lConfiguration.hasConfigurationValue("CREATION_DATE"));
    assertFalse(lConfiguration.hasConfigurationValue("version"));
    assertEquals(2, lAllConfigurationKeys.size());

    assertEquals("1.2.3.4", lConfiguration.getConfigurationValue("VERSION"));

    // Try to access property that does not exist.
    try {
      lConfiguration.getConfigurationValue("NotExistingEntry");
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("NotExistingEntry", e.getKey());
    }

    try {
      lConfiguration.getConfigurationValue(null);
      fail("Exception expected");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }
  }

  @Test
  public void testFileConfiguration( ) {
    Configuration lConfiguration = XFun.getConfigurationProvider().getFileConfiguration("./testdata/test_version.file");

    assertEquals("1.2.3.4", lConfiguration.getConfigurationValue("VERSION", true, String.class));

    // Test error handling

    // Test exception handling with invalid files.
    try {
      XFun.getConfigurationProvider().getFileConfiguration(null);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // nothing to do.
    }

    try {
      XFun.getConfigurationProvider().getFileConfiguration("./testdata/not_existing_file.file");
      fail("Exception expected.");
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.FILE_NOT_FOUND, e.getErrorCode());
    }
  }
}
