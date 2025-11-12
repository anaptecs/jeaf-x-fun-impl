/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;
import java.util.MissingResourceException;
import java.util.Properties;

import com.anaptecs.jeaf.tools.api.ToolsMessages;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.config.Configuration;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.info.RuntimeEnvironment;
import org.junit.jupiter.api.Test;

/**
 * Class implements test cases for class ResourceAccessProvider and its subclasses.
 *
 * @author JEAF Development Team
 * @version 1.0
 */
public class ResourceAccessProviderTest {
  /**
   * Constant for name of property file that contains property values that are used to test the access of boolean
   * properties.
   */
  public static final String ACCESS_TEST_PROPERTIES = "BooleanPropertyAccessTest";

  /**
   * Constant for property that is not defined.
   */
  private static final String UNKNOWN_KEY = "UnknownKey";

  /**
   * Method tests access of string properties using a resource access provider for system properties.
   *
   * @throws Exception if an error occurs during the execution of this test case.
   */
  @Test
  public void testStringPropertyAccess( ) throws Exception {
    // String properties
    String lStringPropertyKey = "StringPropertyKey";
    String lStringProperty = "Some property value";

    String lEmptyStringKey = "EmptyStringKey";
    String lEmptyString = "     ";
    String lDefaultString = "Default";

    // Add key value pairs to system properties.
    Properties lSystemProperties = System.getProperties();
    lSystemProperties.put(lStringPropertyKey, lStringProperty);
    lSystemProperties.put(lEmptyStringKey, lEmptyString);

    // Read property values through ResourceAccessProvider class.
    Configuration lResourceAccessProvider = XFun.getConfigurationProvider().getSystemPropertiesConfiguration();
    String lValue;

    // Get required and set property.
    lValue = lResourceAccessProvider.getConfigurationValue(lStringPropertyKey, true, String.class);
    assertEquals(lStringProperty, lValue);

    // Get optional and set property.
    lValue = lResourceAccessProvider.getConfigurationValue(lStringPropertyKey, false, String.class);
    assertEquals(lStringProperty, lValue);

    // Get optional not set property.
    lValue = lResourceAccessProvider.getConfigurationValue(lEmptyStringKey, false, String.class);
    assertNull(lValue);

    // Get property or default value for set property.
    lValue = lResourceAccessProvider.getConfigurationValue(lStringPropertyKey, lDefaultString, String.class);
    assertEquals(lStringProperty, lValue);

    // Get property or default value for not set property.
    lValue = lResourceAccessProvider.getConfigurationValue(lEmptyStringKey, lDefaultString, String.class);
    assertEquals(lDefaultString, lValue);

    // Get property or default value for not set property and default value null.
    assertNull(lResourceAccessProvider.getConfigurationValue(lEmptyStringKey, null, String.class));

    // Get property or default value for not defined property.
    lValue = lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, lDefaultString, String.class);
    assertEquals(lDefaultString, lValue);

    // Get required property that is not set.
    try {
      lResourceAccessProvider.getConfigurationValue(lEmptyStringKey, true, String.class);
      fail("Expected MissingResourceException when accessing empty required property.");
    }
    catch (MissingResourceException e) {
      // Expected exception occurred.
    }

    // Get optional property that is not defined.
    lValue = lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, false, String.class);
    assertEquals(null, lValue, "Optional property should be null if it not exists");

    // Get required property that is not defined.
    try {
      lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, true, String.class);
      fail("Expected MissingResourceException when accessing missing property.");
    }
    catch (MissingResourceException e) {
      // Expected exception occurred.
    }

    // Test exception handling.

    // Get required property "null".
    try {
      lResourceAccessProvider.getConfigurationValue(null, true, String.class);
    }
    catch (IllegalArgumentException e) {
      // Expected exception occurred.
    }

    // Get optional property "null".
    try {
      lResourceAccessProvider.getConfigurationValue(null, false, String.class);
    }
    catch (IllegalArgumentException e) {
      // Expected exception occurred.
    }

    // Get property "null" with default value.
    try {
      lResourceAccessProvider.getConfigurationValue(null, lDefaultString, String.class);
    }
    catch (IllegalArgumentException e) {
      // Expected exception occurred.
    }

    // Get property "null" with default value.
    try {
      lResourceAccessProvider.getConfigurationValue(null, null);
    }
    catch (IllegalArgumentException e) {
      // Expected exception occurred.
    }

    // Test substitution of system properties
    System.setProperty("jeaf.key1", "1");
    System.setProperty("jeaf.key2", "2");
    System.setProperty("jeaf.key3", "3");
    System.setProperty("jeaf.replace1", "${jeaf.key1}-${jeaf.key2}-${jeaf.key3}");
    System.setProperty("jeaf.replace2", "xxx${jeaf.key1}yyy");
    System.setProperty("jeaf.replace3", "${jeaf.key1}");
    System.setProperty("jeaf.replace4", "${jeaf.key4}");
    String lReplaceValue = lResourceAccessProvider.getConfigurationValue("jeaf.replace1", true, String.class);
    assertEquals("1-2-3", lReplaceValue, "Problem with system properties substitution.");
    lReplaceValue = lResourceAccessProvider.getConfigurationValue("jeaf.replace2", true, String.class);
    assertEquals("xxx1yyy", lReplaceValue, "Problem with system properties substitution.");
    lReplaceValue = lResourceAccessProvider.getConfigurationValue("jeaf.replace3", true, String.class);
    assertEquals("1", lReplaceValue, "Problem with system properties substitution.");
    lReplaceValue = lResourceAccessProvider.getConfigurationValue("jeaf.replace4", true, String.class);
    assertEquals("${jeaf.key4}", lReplaceValue, "Problem with system properties substitution.");
  }

  /**
   * Method tests access of string properties using a resource access provider for resource bundels.
   *
   * @throws Exception if an error occurs during the execution of this test case.
   */
  @Test
  public void testBooleanPropertyAccess( ) throws Exception {
    // Boolean properties
    String lBooleanPropertyKey_1 = "BooleanPropertyKey_1";
    Boolean lBooleanPropertyValue_1 = Boolean.TRUE;
    String lBooleanProperty_1 = " TruE    ";

    String lBooleanPropertyKey_2 = "BooleanPropertyKey_2";
    Boolean lBooleanPropertyValue_2 = Boolean.FALSE;
    String lBooleanProperty_2 = "false    ";

    String lBooleanPropertyKey_3 = "BooleanPropertyKey_3";
    Boolean lBooleanPropertyValue_3 = Boolean.FALSE;
    String lBooleanProperty_3 = " something else    ";

    String lBooleanPropertyKey_4 = "BooleanPropertyKey_4";
    Boolean lBooleanPropertyValue_4 = Boolean.TRUE;
    String lBooleanProperty_4 = "TRUE";

    String lUnsetBooleanPropertyKey = "UnsetBooleanPropertyKey";

    // Load resource bundle.
    Configuration lConfiguration;
    lConfiguration = XFun.getConfigurationProvider().getResourceBundleConfiguration(ACCESS_TEST_PROPERTIES);

    // Check that properties have the expected values.
    String lStringValue;
    lStringValue = lConfiguration.getConfigurationValue(lBooleanPropertyKey_1, true, String.class);
    assertEquals(lBooleanProperty_1.trim(), lStringValue);
    lStringValue = lConfiguration.getConfigurationValue(lBooleanPropertyKey_2, true, String.class);
    assertEquals(lBooleanProperty_2.trim(), lStringValue);
    lStringValue = lConfiguration.getConfigurationValue(lBooleanPropertyKey_3, true, String.class);
    assertEquals(lBooleanProperty_3.trim(), lStringValue);
    lStringValue = lConfiguration.getConfigurationValue(lBooleanPropertyKey_4, true, String.class);
    assertEquals(lBooleanProperty_4.trim(), lStringValue);
    lStringValue = lConfiguration.getConfigurationValue(lUnsetBooleanPropertyKey, false, String.class);
    assertNull(lStringValue);

    // Test access of boolean values.
    Boolean lValue;

    // Get set property " TruE ".
    lValue = lConfiguration.getConfigurationValue(lBooleanPropertyKey_1, Boolean.class);
    assertEquals(lBooleanPropertyValue_1, lValue);

    // Get set property "false ".
    lValue = lConfiguration.getConfigurationValue(lBooleanPropertyKey_2, Boolean.class);
    assertEquals(lBooleanPropertyValue_2, lValue);

    // Get set property " something else "
    lValue = lConfiguration.getConfigurationValue(lBooleanPropertyKey_3, Boolean.class);
    assertEquals(lBooleanPropertyValue_3, lValue);

    // Get set property "TRUE".
    lValue = lConfiguration.getConfigurationValue(lBooleanPropertyKey_4, Boolean.class);
    assertEquals(lBooleanPropertyValue_4, lValue);

    // Get not set property.
    lValue = lConfiguration.getConfigurationValue(lUnsetBooleanPropertyKey, Boolean.class);
    assertEquals(null, lValue);

    // Get not set property with default value true.
    lValue = lConfiguration.getConfigurationValue(lUnsetBooleanPropertyKey, Boolean.TRUE, Boolean.class);
    assertEquals(Boolean.TRUE, lValue);

    // Get not set property with default value false.
    lValue = lConfiguration.getConfigurationValue(lUnsetBooleanPropertyKey, Boolean.FALSE, Boolean.class);
    assertEquals(Boolean.FALSE, lValue);

    // Get not defined property.
    lValue = lConfiguration.getConfigurationValue(UNKNOWN_KEY, Boolean.class);
    assertEquals(null, lValue, "Not set boolean property should be false");

    // Get not defined property with default value true.
    lValue = lConfiguration.getConfigurationValue(UNKNOWN_KEY, Boolean.TRUE, Boolean.class);
    assertEquals(Boolean.TRUE, lValue);

    // Get not defined property with default value true.
    lValue = lConfiguration.getConfigurationValue(UNKNOWN_KEY, Boolean.FALSE, Boolean.class);
    assertEquals(Boolean.FALSE, lValue);

    // Test exception handling.

    // Get property "null".
    try {
      lConfiguration.getConfigurationValue(null, Boolean.class);
    }
    catch (IllegalArgumentException e) {
      // Expected exception occurred.
    }

    // Get property "null" with default value true.
    try {
      lConfiguration.getConfigurationValue(null, Boolean.TRUE, Boolean.class);
    }
    catch (IllegalArgumentException e) {
      // Expected exception occurred.
    }

    // Get property "null" with default value false.
    try {
      lConfiguration.getConfigurationValue(null, Boolean.FALSE, Boolean.class);
    }
    catch (IllegalArgumentException e) {
      // Expected exception occurred.
    }
  }

  /**
   * Method tests access of integer properties using a resource access provider for system properties.
   *
   * @throws Exception if an error occurs during the execution of this test case.
   */
  @Test
  public void testIntegerPropertyAccess( ) throws Exception {
    // Integer properties.
    String lIntegerPropertyKey_1 = "IntegerPropertyKey_1";
    Integer lIntegerPropertyValue_1 = 1;
    String lIntegerProperty_1 = Integer.toString(lIntegerPropertyValue_1);

    String lIntegerPropertyKey_2 = "IntegerPropertyKey_2";
    Integer lIntegerPropertyValue_2 = -1;
    String lIntegerProperty_2 = Integer.toString(lIntegerPropertyValue_2);

    String lIntegerPropertyKey_3 = "IntegerPropertyKey_3";
    Integer lIntegerPropertyValue_3 = Integer.MAX_VALUE;
    String lIntegerProperty_3 = Integer.toString(lIntegerPropertyValue_3);

    String lIntegerPropertyKey_4 = "IntegerPropertyKey_4";
    Integer lIntegerPropertyValue_4 = Integer.MIN_VALUE;
    String lIntegerProperty_4 = Integer.toString(lIntegerPropertyValue_4);

    String lDoublePropertyKey = "IntegerPropertyKey_5";
    Double lIntgerPropertyDoubleValue = 47.11;
    String lIntegerProperty_5 = Double.toString(lIntgerPropertyDoubleValue);

    String lBrokenIntegerValueKey = "BrokenIntegerValueKey";
    String lBrokenIntegerValue = "This is not a int value.";

    String lUnsetIntegerPropertyKey = "UnsetIntegerPropertyKey";

    // Add key value pairs to system properties.
    Properties lSystemProperties = System.getProperties();
    lSystemProperties.put(lIntegerPropertyKey_1, lIntegerProperty_1);
    lSystemProperties.put(lIntegerPropertyKey_2, lIntegerProperty_2);
    lSystemProperties.put(lIntegerPropertyKey_3, lIntegerProperty_3);
    lSystemProperties.put(lIntegerPropertyKey_4, lIntegerProperty_4);
    lSystemProperties.put(lDoublePropertyKey, lIntegerProperty_5);
    lSystemProperties.put(lBrokenIntegerValueKey, lBrokenIntegerValue);
    lSystemProperties.put(lUnsetIntegerPropertyKey, "");

    // Get system properties as resource.
    Configuration lResourceAccessProvider = XFun.getConfigurationProvider().getSystemPropertiesConfiguration();
    Integer lValue;

    //
    // Get required and set integer property.
    //
    lValue = lResourceAccessProvider.getConfigurationValue(lIntegerPropertyKey_1, true, Integer.class);
    assertEquals(lIntegerPropertyValue_1, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lIntegerPropertyKey_2, true, Integer.class);
    assertEquals(lIntegerPropertyValue_2, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lIntegerPropertyKey_3, true, Integer.class);
    assertEquals(lIntegerPropertyValue_3, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lIntegerPropertyKey_4, true, Integer.class);
    assertEquals(lIntegerPropertyValue_4, lValue);

    //
    // Get optional and set integer property.
    //
    lValue = lResourceAccessProvider.getConfigurationValue(lIntegerPropertyKey_1, false, Integer.class);
    assertEquals(lIntegerPropertyValue_1, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lIntegerPropertyKey_2, false, Integer.class);
    assertEquals(lIntegerPropertyValue_2, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lIntegerPropertyKey_3, false, Integer.class);
    assertEquals(lIntegerPropertyValue_3, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lIntegerPropertyKey_4, false, Integer.class);
    assertEquals(lIntegerPropertyValue_4, lValue);

    //
    // Get not set property with default value.
    //
    lValue = lResourceAccessProvider.getConfigurationValue(lUnsetIntegerPropertyKey, 4711, Integer.class);
    assertEquals(4711, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lUnsetIntegerPropertyKey, -815, Integer.class);
    assertEquals(-815, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lUnsetIntegerPropertyKey, 0, Integer.class);
    assertEquals(0, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lUnsetIntegerPropertyKey, 1, Integer.class);
    assertEquals(1, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lUnsetIntegerPropertyKey, -1, Integer.class);
    assertEquals(-1, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lUnsetIntegerPropertyKey, Integer.MAX_VALUE, Integer.class);
    assertEquals(Integer.MAX_VALUE, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(lUnsetIntegerPropertyKey, Integer.MIN_VALUE, Integer.class);
    assertEquals(Integer.MIN_VALUE, lValue);

    //
    // Get not defined property with default value.
    //
    lValue = lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, 1999, Integer.class);
    assertEquals(1999, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, -52, Integer.class);
    assertEquals(-52, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, 0, Integer.class);
    assertEquals(0, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, 1, Integer.class);
    assertEquals(1, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, -1, Integer.class);
    assertEquals(-1, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, Integer.MAX_VALUE, Integer.class);
    assertEquals(Integer.MAX_VALUE, lValue);

    lValue = lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, Integer.MIN_VALUE, Integer.class);
    assertEquals(Integer.MIN_VALUE, lValue);

    //
    // Get required and not set property.
    //
    try {
      lResourceAccessProvider.getConfigurationValue(lUnsetIntegerPropertyKey, true, Integer.class);
      fail("Expecting MissingResourceException when accessing not set property.");
    }
    catch (MissingResourceException e) {
      // Expected exception occurred.
    }

    // Get optional and not set property.
    lValue = lResourceAccessProvider.getConfigurationValue(lUnsetIntegerPropertyKey, false, Integer.class);
    assertEquals(null, lValue);

    // Get required and not defined property.
    try {
      lResourceAccessProvider.getConfigurationValue(UNKNOWN_KEY, true, Integer.class);
      fail("Expecting MissingResourceException when accessing not defined property.");
    }
    catch (MissingResourceException e) {
      // Expected exception occurred.
    }

    // Get required property with double value.
    try {
      lResourceAccessProvider.getConfigurationValue(lDoublePropertyKey, true, Integer.class);
      fail("Expecting NumberFormatException when converting double string to integer.");
    }
    catch (NumberFormatException e) {
      // Expected exception occurred.
    }

    // Get optional property with double value.
    try {
      lResourceAccessProvider.getConfigurationValue(lDoublePropertyKey, false, Integer.class);
      fail("Expecting NumberFormatException when converting double string to integer.");
    }
    catch (NumberFormatException e) {
      // Expected exception occurred.
    }

    // Get double value property with default.
    lValue = lResourceAccessProvider.getConfigurationValue(lDoublePropertyKey, 87, Integer.class);
    assertEquals(87, lValue);

    // Get required property with invalid integer.
    try {
      lResourceAccessProvider.getConfigurationValue(lBrokenIntegerValueKey, true, Integer.class);
      fail("Expected NumberFormatException when converting invalid string to integer.");
    }
    catch (NumberFormatException e) {
      // Expected exception occurred.
    }

    // Get optional property with invalid integer.
    try {
      lResourceAccessProvider.getConfigurationValue(lBrokenIntegerValueKey, false, Integer.class);
      fail("Expected NumberFormatException when converting invalid string to integer.");
    }
    catch (NumberFormatException e) {
      // Expected exception occurred.
    }

    // Get invalid string property with default.
    lValue = lResourceAccessProvider.getConfigurationValue(lBrokenIntegerValueKey, -19999999, Integer.class);
    assertEquals(-19999999, lValue);
  }

  /**
   * Method tests access of date properties using a resource access provider for system properties.
   *
   * @throws Exception if an error occurs during the execution of this test case.
   */
  @Test
  public void testCalendarPropertyAccess( ) throws Exception {
    // Initial values for date and timestamp.
    int lYear = 2001;
    int lMonth = 10;
    int lDay = 31;
    int lHour = 13;
    int lMinute = 43;
    int lSecond = 12;
    int lMillisecond = 456;

    String lDatePropertyKey = "DatePropertyKey";
    String lDatePropertyValue = lYear + "-" + lMonth + "-" + lDay;

    String lTimestampPropertyKey = "TimestampPropertyKey";
    String lTimestampPropertyValue =
        lYear + "-" + lMonth + "-" + lDay + " " + lHour + ":" + lMinute + ":" + lSecond + "." + lMillisecond;

    String lInvalidDatePropertyKey = "InvalidDatePropertyKey";
    String lInvalidDatePropertyValue = lDay + "." + lMonth + "." + lYear;

    String lUnsetCalendarPropertyKey = "UnsetCalendarPropertyKey";

    // Add key value pairs to system properties.
    Properties lSystemProperties = System.getProperties();
    lSystemProperties.put(lDatePropertyKey, lDatePropertyValue);
    lSystemProperties.put(lTimestampPropertyKey, lTimestampPropertyValue);
    lSystemProperties.put(lInvalidDatePropertyKey, lInvalidDatePropertyValue);
    lSystemProperties.put(lUnsetCalendarPropertyKey, "     \t  ");

    // Get ResourceAccessProvider for system properties.
    Configuration lResourceAccessProvider = XFun.getConfigurationProvider().getSystemPropertiesConfiguration();
    Calendar lCalendar;

    // Get required and set properties as date.
    lCalendar = lResourceAccessProvider.getConfigurationValue(lDatePropertyKey, true, Calendar.class);
    assertEquals(lYear, lCalendar.get(Calendar.YEAR), "Calculated invalid year.");
    assertEquals(lMonth - 1, lCalendar.get(Calendar.MONTH), "Calculated invalid month.");
    assertEquals(lDay, lCalendar.get(Calendar.DAY_OF_MONTH), "Calculated invalid day.");
    assertEquals(0, lCalendar.get(Calendar.HOUR_OF_DAY), "Calculated invalid hour.");
    assertEquals(0, lCalendar.get(Calendar.MINUTE), "Calculated invalid minute.");
    assertEquals(0, lCalendar.get(Calendar.SECOND), "Calculated invalid second.");
    assertEquals(0, lCalendar.get(Calendar.MILLISECOND), "Calculated invalid millisecond.");

    lCalendar = lResourceAccessProvider.getConfigurationValue(lTimestampPropertyKey, true, Calendar.class);
    assertEquals(lYear, lCalendar.get(Calendar.YEAR), "Calculated invalid year.");
    assertEquals(lMonth - 1, lCalendar.get(Calendar.MONTH), "Calculated invalid month.");
    assertEquals(lDay, lCalendar.get(Calendar.DAY_OF_MONTH), "Calculated invalid day.");
    assertEquals(13, lCalendar.get(Calendar.HOUR_OF_DAY), "Calculated invalid hour.");
    assertEquals(43, lCalendar.get(Calendar.MINUTE), "Calculated invalid minute.");
    assertEquals(12, lCalendar.get(Calendar.SECOND), "Calculated invalid second.");
    assertEquals(456, lCalendar.get(Calendar.MILLISECOND), "Calculated invalid millisecond.");

    // Get required and set property with invalid value.
    try {
      lResourceAccessProvider.getConfigurationValue(lInvalidDatePropertyKey, true, Calendar.class);

      fail("Expected ParserException when converting invalid string to date (Date: " + lCalendar.getTime() + ".");
    }
    catch (JEAFSystemException e) {
      assertEquals(ToolsMessages.INVALID_TIMESTAMP_FORMAT, e.getErrorCode());
    }

    // Get required and set properties as date.
    lCalendar = lResourceAccessProvider.getConfigurationValue(lTimestampPropertyKey, true, Calendar.class);
    assertEquals(lYear, lCalendar.get(Calendar.YEAR), "Calculated invalid year.");
    assertEquals(lMonth - 1, lCalendar.get(Calendar.MONTH), "Calculated invalid month.");
    assertEquals(lDay, lCalendar.get(Calendar.DAY_OF_MONTH), "Calculated invalid day.");
    assertEquals(lHour, lCalendar.get(Calendar.HOUR_OF_DAY), "Calculated invalid hour.");
    assertEquals(lMinute, lCalendar.get(Calendar.MINUTE), "Calculated invalid minute.");
    assertEquals(lSecond, lCalendar.get(Calendar.SECOND), "Calculated invalid second.");
    assertEquals(lMillisecond, lCalendar.get(Calendar.MILLISECOND), "Calculated invalid millisecond.");
  }

  /**
   * Method tests the access to Enumeration objects by using system properties.
   */
  @Test
  public void testEnumPropertyAccess( ) {
    // Add key value pairs to system properties.
    Properties lSystemProperties = System.getProperties();
    String lRuntimeOK = "RUNTIME_OK";
    lSystemProperties.put(lRuntimeOK, RuntimeEnvironment.JSE.toString());
    String lRuntimeNOK = "RUNTIME_NOK";
    lSystemProperties.put(lRuntimeNOK, "ABC_Unknown");
    String lNotExistingProperty = "NOT_EXISTING_PROPERTY";
    lSystemProperties.put(lNotExistingProperty, "");

    // Get ResourceAccessProvider for system properties.
    Configuration lResourceAccessProvider = XFun.getConfigurationProvider().getSystemPropertiesConfiguration();

    // Try accessing properties as enumeration.
    RuntimeEnvironment lEnvironment =
        lResourceAccessProvider.getConfigurationValue(lRuntimeOK, true, RuntimeEnvironment.class);
    assertEquals(RuntimeEnvironment.JSE, lEnvironment, "Wrong runtime environment.");

    try {
      lEnvironment = lResourceAccessProvider.getConfigurationValue(lRuntimeNOK, true, RuntimeEnvironment.class);
      fail("Access to property with invalid value must cause an exception.");
    }
    catch (IllegalArgumentException e) {
      // Nothing to do.
    }

    lEnvironment = lResourceAccessProvider.getConfigurationValue(lNotExistingProperty, false, RuntimeEnvironment.class);
    assertNull(lEnvironment, "Access to not existiung enum property must return null in this case.");
  }
}
