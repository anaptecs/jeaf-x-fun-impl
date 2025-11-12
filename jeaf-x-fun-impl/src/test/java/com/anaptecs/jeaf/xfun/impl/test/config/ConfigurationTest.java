/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;

import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.config.Configuration;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.impl.config.ConfigurationImpl;
import org.junit.jupiter.api.Test;

public class ConfigurationTest {
  @Test
  public void testBooleanConfiguration( ) {
    TestConfigurationResource lResource = new TestConfigurationResource();
    lResource.resourceLocation = "commons-impl-test";
    lResource.resourceName = "Test-Configuration";
    lResource.properties.put("TrueValue", Boolean.TRUE.toString());
    lResource.properties.put("FalseValue", Boolean.FALSE.toString());
    lResource.properties.put("NullValue", null);
    lResource.properties.put("EmptyValue", "");
    lResource.properties.put("TrimmedEmptyValue", " \n");
    lResource.properties.put("InvalidBoolean", "ABC");
    lResource.properties.put("ValueWithTrueSystemProperty", "${system.property.true}");
    lResource.properties.put("ValueWithFalseSystemProperty", "${system.property.false}");
    lResource.properties.put("ValueWithNotSetSystemProperty", "${system.property.not.set}");
    lResource.properties.put("InvalidSystemProperty", "${system.property.invalid");
    System.setProperty("system.property.true", "TrUE");
    System.setProperty("system.property.false", "false");

    ConfigurationImpl lReader = new ConfigurationImpl(lResource);

    // Test good cases for default access
    assertEquals(Boolean.TRUE, lReader.getConfigurationValue("TrueValue", Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("FalseValue", Boolean.class));
    assertEquals(null, lReader.getConfigurationValue("NullValue", Boolean.class));
    assertEquals(null, lReader.getConfigurationValue("EmptyValue", Boolean.class));
    assertEquals(null, lReader.getConfigurationValue("TrimmedEmptyValue", Boolean.class));
    assertEquals(null, lReader.getConfigurationValue("MissingValue", Boolean.class));
    assertEquals(Boolean.TRUE, lReader.getConfigurationValue("ValueWithTrueSystemProperty", Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("ValueWithFalseSystemProperty", Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("ValueWithNotSetSystemProperty", Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("InvalidSystemProperty", Boolean.class));

    // Test good cases in combination with a provided default value
    assertEquals(Boolean.TRUE, lReader.getConfigurationValue("TrueValue", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.TRUE, lReader.getConfigurationValue("TrueValue", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("FalseValue", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("FalseValue", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.TRUE, lReader.getConfigurationValue("NullValue", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("NullValue", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.TRUE, lReader.getConfigurationValue("EmptyValue", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("EmptyValue", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.TRUE, lReader.getConfigurationValue("TrimmedEmptyValue", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("TrimmedEmptyValue", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.TRUE, lReader.getConfigurationValue("MissingValue", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("MissingValue", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("InvalidBoolean", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("InvalidBoolean", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.TRUE,
        lReader.getConfigurationValue("ValueWithTrueSystemProperty", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.TRUE,
        lReader.getConfigurationValue("ValueWithTrueSystemProperty", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.FALSE,
        lReader.getConfigurationValue("ValueWithFalseSystemProperty", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.FALSE,
        lReader.getConfigurationValue("ValueWithFalseSystemProperty", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.FALSE,
        lReader.getConfigurationValue("ValueWithNotSetSystemProperty", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.FALSE,
        lReader.getConfigurationValue("ValueWithNotSetSystemProperty", Boolean.FALSE, Boolean.class));

    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("InvalidSystemProperty", Boolean.TRUE, Boolean.class));
    assertEquals(Boolean.FALSE, lReader.getConfigurationValue("InvalidSystemProperty", Boolean.FALSE, Boolean.class));

    assertNull(lReader.getConfigurationValue("NotExistingKey", false, Boolean.class));

    try {
      lReader.getConfigurationValue("NotExistingKey", true, Boolean.class);
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("NotExistingKey", e.getKey());
    }

    try {
      lReader.getConfigurationValue("NullValue", true, Boolean.class);
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("NullValue", e.getKey());
    }
  }

  @Test
  public void testStringConfiguration( ) {
    TestConfigurationResource lResource = new TestConfigurationResource();
    lResource.resourceLocation = "x-fun-tests";
    lResource.resourceName = "Test-Configuration";
    lResource.properties.put("Key", "value");
    lResource.properties.put("Null", null);

    Configuration lConfiguration = new ConfigurationImpl(lResource);

    // Test access to existing value
    assertEquals("value", lConfiguration.getConfigurationValue("Key", String.class));
    assertEquals("value", lConfiguration.getConfigurationValue("Key", true, String.class));
    assertEquals("value", lConfiguration.getConfigurationValue("Key", "Default", String.class));

    // Test access to existing but not set value.
    assertNull(lConfiguration.getConfigurationValue("Null", String.class));
    assertEquals("Default", lConfiguration.getConfigurationValue("Null", "Default", String.class));

    try {
      lConfiguration.getConfigurationValue("Null", true, String.class);
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("Null", e.getKey());
    }

    // Test access to missing configuration entry.
    assertNull(lConfiguration.getConfigurationValue("Missing", String.class));
    assertEquals("Default", lConfiguration.getConfigurationValue("Missing", "Default", String.class));

    try {
      lConfiguration.getConfigurationValue("Missing", true, String.class);
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("Missing", e.getKey());
    }
  }

  @Test
  public void testCharacterProperties( ) {
    TestConfigurationResource lResource = new TestConfigurationResource();
    lResource.resourceName = "TestResource";
    lResource.resourceName = "TestResourceLocation";
    lResource.properties.put("key", "x");
    lResource.properties.put("invalidEntry", "xyz");

    ConfigurationImpl lConfiguration = new ConfigurationImpl(lResource);
    assertEquals(Character.valueOf('x'), lConfiguration.getConfigurationValue("key", Character.class));
    assertNull(lConfiguration.getConfigurationValue("missing", Character.class));

    lConfiguration.writeTrace();

    try {
      lConfiguration.getConfigurationValue(null, Character.class);
      fail("Exception expected.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }

    try {
      lConfiguration.getConfigurationValue("invalidEntry", Character.class);
      fail("Exception expected.");
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, e.getErrorCode());
      assertEquals("String", e.getMessageParameters()[0]);
      assertEquals("Character", e.getMessageParameters()[1]);
      assertEquals("xyz", e.getMessageParameters()[2]);
      // Nothing to do.
    }
  }

  @Test
  public void testStringValueListConfiguration( ) {
    TestConfigurationResource lResource = new TestConfigurationResource();
    lResource.resourceName = "TestResource";
    lResource.resourceName = "TestResourceLocation";
    lResource.properties.put("StringValues", "value1; value2;value3");
    lResource.properties.put("DoubleValues", "1;2.456;   1.345   ; -2.4878");
    lResource.properties.put("EmptyValues", null);
    lResource.properties.put("NoneValues", " ");

    ConfigurationImpl lConfiguration = new ConfigurationImpl(lResource);
    List<String> lStringValues = lConfiguration.getConfigurationValueList("StringValues", String.class);
    assertEquals(3, lStringValues.size());
    assertEquals("value1", lStringValues.get(0));
    assertEquals("value2", lStringValues.get(1));
    assertEquals("value3", lStringValues.get(2));

    lStringValues = lConfiguration.getConfigurationValueList("StringValues", false, String.class);
    assertEquals(3, lStringValues.size());
    assertEquals("value1", lStringValues.get(0));
    assertEquals("value2", lStringValues.get(1));
    assertEquals("value3", lStringValues.get(2));

    lStringValues = lConfiguration.getConfigurationValueList("StringValues", true, String.class);
    assertEquals(3, lStringValues.size());
    assertEquals("value1", lStringValues.get(0));
    assertEquals("value2", lStringValues.get(1));
    assertEquals("value3", lStringValues.get(2));

    // Test default value handling
    lStringValues =
        lConfiguration.getConfigurationValueList("MissingValues", Arrays.asList("Default", "Default2"), String.class);
    assertEquals(2, lStringValues.size());
    assertEquals("Default", lStringValues.get(0));
    assertEquals("Default2", lStringValues.get(1));

    lStringValues = lConfiguration.getConfigurationValueList("MissingValues", "Default", String.class);
    assertEquals(1, lStringValues.size());
    assertEquals("Default", lStringValues.get(0));

    lStringValues = lConfiguration.getConfigurationValueList("EmptyValues", String.class);
    assertEquals(0, lStringValues.size());
    lStringValues = lConfiguration.getConfigurationValueList("NoneValues", String.class);
    assertEquals(0, lStringValues.size());

    // Test exception handling
    try {
      lConfiguration.getConfigurationValueList("MissingValues", true, String.class);
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("MissingValues", e.getKey());
    }

  }

  @Test
  public void testDoubleValueListConfiguration( ) {
    TestConfigurationResource lResource = new TestConfigurationResource();
    lResource.resourceName = "TestResource";
    lResource.resourceName = "TestResourceLocation";
    lResource.properties.put("StringValues", "value1; value2;value3");
    lResource.properties.put("DoubleValues", "1;2.456;   1.345   ; -2.4878");
    lResource.properties.put("EmptyValues", null);
    lResource.properties.put("NoneValues", " ");

    ConfigurationImpl lConfiguration = new ConfigurationImpl(lResource);
    List<Double> lDoubleValues = lConfiguration.getConfigurationValueList("DoubleValues", Double.class);
    assertEquals(4, lDoubleValues.size());
    assertEquals(1, lDoubleValues.get(0));
    assertEquals(2.456, lDoubleValues.get(1));
    assertEquals(1.345, lDoubleValues.get(2));
    assertEquals(-2.4878, lDoubleValues.get(3));

    // Test invalid conversions
    try {
      lConfiguration.getConfigurationValueList("StringValues", Double.class);
      fail("Exception expected.");
    }
    catch (NumberFormatException e) {
      // Nothing to do.
    }

  }

  @Test
  public void testEnumValueListConfiguration( ) {
    TestConfigurationResource lResource = new TestConfigurationResource();
    lResource.resourceName = "TestResource";
    lResource.resourceName = "TestResourceLocation";
    lResource.properties.put("EnumValues", "APPLE; ORANGE");
    lResource.properties.put("EmptyValues", null);

    ConfigurationImpl lConfiguration = new ConfigurationImpl(lResource);
    List<Fruit> lFruits = lConfiguration.getConfigurationValueList("EnumValues", Fruit.class);
    assertEquals(2, lFruits.size());
    assertEquals(Fruit.APPLE, lFruits.get(0));
    assertEquals(Fruit.ORANGE, lFruits.get(1));

    try {
      lConfiguration.getConfigurationValueList("EmptyValues", true, Fruit.class);
      fail("Exception expected.");
    }
    catch (MissingResourceException e) {
      assertEquals("EmptyValues", e.getKey());
    }

  }

}
