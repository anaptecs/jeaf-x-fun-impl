/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.datatypeconverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.anaptecs.jeaf.tools.api.Tools;
import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.DatatypeConverterRegistryImpl;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.BigDecimalToDoubleDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.BigDecimalToFloatDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.BigDecimalToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.BigIntegerToByteDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.BigIntegerToIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.BigIntegerToLongDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.BigIntegerToShortDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.BigIntegerToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.BooleanToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.ByteToBigIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.ByteToCharacterDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.ByteToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CalendarToDateDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToByteDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToLongDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToShortDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.ClassToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.DateToCalendarDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.DoubleToBigDecimalDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.DoubleToFloatDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.DoubleToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.FloatToBigDecimalDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.FloatToDoubleDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.FloatToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.IntegerToBigIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.IntegerToCharacterDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.IntegerToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.LongToBigIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.LongToCharacterDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.LongToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.ShortToBigIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.ShortToCharacterDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.ShortToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToBigDecimalDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToBigIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToBooleanDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToByteDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToCharacterDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToClassDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToDoubleDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToFloatDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToLongDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToShortDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToStringDatatypeConverter;
import org.junit.jupiter.api.Test;

/**
 * This test case tests the different DataTypeConverters from com.anaptecs.jeaf.fwk.core.util.converters.*
 *
 * @author JEAF Development Team
 */
public class DatatypeConverterTest {

  /**
   * Tests the conversion from BigDecimal to Double
   */
  @Test
  public void testBigDecimaltoDoubleDatatypeConverter( ) {
    // Testing normal conversion from BigDecimal to Double

    // Initializing BigDecimal Value
    BigDecimal lBigDecimal = new BigDecimal(20000);
    Double lExpected = 20000.0;

    BigDecimalToDoubleDatatypeConverter lConverter = new BigDecimalToDoubleDatatypeConverter();
    Double lDouble = lConverter.convert(lBigDecimal);
    assertEquals(lExpected, lDouble);

    // Testing conversion from BigDecimal to Double for MAX Value of Double

    // Taking MAX Double Value and put it into BigDecimal Value
    lDouble = Double.MAX_VALUE;
    lBigDecimal = BigDecimal.valueOf(lDouble);

    // Try to Convert and Test Result
    lExpected = Double.MAX_VALUE;
    lDouble = lConverter.convert(lBigDecimal);
    assertEquals(lExpected, lDouble);

    // Testing conversion from BigDecimal to Double for MIN Value of Double

    // Taking MIN Double Value and put it into BigDecimal Value
    lDouble = Double.MIN_VALUE;
    lBigDecimal = BigDecimal.valueOf(lDouble);

    // Try to Convert and Test Result
    lExpected = Double.MIN_VALUE;
    lDouble = lConverter.convert(lBigDecimal);
    assertEquals(lExpected, lDouble);

    // Testing conversion from BigDecimal to Double for MAX Value +x (over Range) of Double

    // Taking MAX Double Value and put it into BigDecimal Value + 100 (over Range of Double)

    lDouble = Double.MAX_VALUE;

    lBigDecimal = BigDecimal.valueOf(lDouble);
    lBigDecimal = lBigDecimal.add(new BigDecimal(100));

    // Try to Convert and Test if right Exception is thrown
    final ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;
    try {
      lDouble = lConverter.convert(lBigDecimal);
      fail("No Exception Thrown");

    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from BigDecimal to Double for MAX Value +x (over Range) of Double

    // Taking MIN Double Value and put it into BigDecimal Value + 100 (over Range of Double)

    lDouble = Double.MIN_VALUE;

    lBigDecimal = BigDecimal.valueOf(lDouble);
    lBigDecimal = lBigDecimal.add(new BigDecimal(-100));

    // Try to Convert and Test if right Exception is thrown
    try {
      lDouble = lConverter.convert(lBigDecimal);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  /**
   * Tests the conversion from BigDecimal to Float
   */
  @Test
  public void testBigDecimalToFloatDatatypeConverter( ) {
    // Testing Normal conversion from BigDecimal to Float

    // Initializing BigDecimal Value
    BigDecimal lBigDecimal = new BigDecimal(20000);
    Float lExpected = Float.parseFloat("20000.0");

    BigDecimalToFloatDatatypeConverter lConverter = new BigDecimalToFloatDatatypeConverter();
    Float lFloat = lConverter.convert(lBigDecimal);
    assertEquals(lExpected, lFloat);

    // Testing conversion from BigDecimal to Float for MAX Value of Float

    // Taking MAX Float Value and put it into BigDecimal Value
    lFloat = Float.MAX_VALUE;
    lBigDecimal = BigDecimal.valueOf(lFloat);

    // Try to Convert and Test Result
    lExpected = Float.MAX_VALUE;
    lFloat = lConverter.convert(lBigDecimal);
    assertEquals(lExpected, lFloat);

    // Testing conversion from BigDecimal to Float for MIN Value of Float

    // Taking MIN Float Value and put it into BigDecimal Value
    lFloat = Float.MIN_VALUE;
    lBigDecimal = BigDecimal.valueOf(lFloat);

    // Try to Convert and Test Result
    lExpected = Float.MIN_VALUE;
    lFloat = lConverter.convert(lBigDecimal);
    assertEquals(lExpected, lFloat);

    // Testing conversion from BigDecimal to Float for MAX Value +x (over Range) of Float

    // Taking MAX Float Value and put it into BigDecimal Value + 100 (over Range of Float)

    lFloat = Float.MAX_VALUE;

    lBigDecimal = BigDecimal.valueOf(lFloat);
    lBigDecimal = lBigDecimal.add(new BigDecimal(100));

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lFloat = lConverter.convert(lBigDecimal);
      fail("No Exception Thrown");

    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from BigDecimal to Float for MIN Value -x (over Range) of Float

    // Taking MIN Float Value and put it into BigDecimal Value - 100 (over Range of Float)

    lFloat = Float.MIN_VALUE;

    lBigDecimal = BigDecimal.valueOf(lFloat);
    lBigDecimal = lBigDecimal.add(new BigDecimal(-100));

    // Try to Convert and Test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lFloat = lConverter.convert(lBigDecimal);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  /**
   * Tests the conversion from BigDecimal To String
   */

  @Test
  public void testBigDecimalToStringDatatypeConverter( ) {
    // Testing Normal conversion from BigDecimal to String

    // Initializing BigDecimal Value
    BigDecimal lBigDecimal = new BigDecimal(20000);
    String lExpected = "20000";

    BigDecimalToStringDatatypeConverter lConverter = new BigDecimalToStringDatatypeConverter();
    String lString = lConverter.convert(lBigDecimal);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  /**
   * Tests the conversion from BigInteger to Byte
   */

  @Test
  public void testBigIntegerToByteDatatypeConverter( ) {

    // Initializing BigInteger Value
    BigInteger lBigInteger = new BigInteger("50");
    Byte lExpected = 50;

    BigIntegerToByteDatatypeConverter lConverter = new BigIntegerToByteDatatypeConverter();
    Byte lByte = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lByte);

    // Testing Convertion from BigInteger to Byte for MAX Value of Byte

    // Taking MAX Byte Value and put it into BigInteger Value
    lByte = Byte.MAX_VALUE;
    lBigInteger = BigInteger.valueOf(lByte);

    // Try to Convert and Test Result
    lExpected = Byte.MAX_VALUE;
    lByte = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lByte);

    // Testing conversion from BigInteger to Byte for MIN Value of Byte

    // Taking MIN Double Value and put it into BigDecimal Value
    lByte = Byte.MIN_VALUE;
    lBigInteger = BigInteger.valueOf(lByte);

    // Try to Convert and Test Result
    lExpected = Byte.MIN_VALUE;
    lByte = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lByte);

    // Testing conversion from BigInteger to Byte for MAX Value +x (over Range) of Byte

    // Taking MAX Byte Value and put it into BigInteger Value + 1 (over Range of Double)

    lByte = Byte.MAX_VALUE;

    lBigInteger = BigInteger.valueOf(lByte);
    lBigInteger = lBigInteger.add(new BigInteger("1"));

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lByte = lConverter.convert(lBigInteger);
      fail("No Exception Thrown");

    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from BigInteger to Byte for MAX Value +x (over Range) of Byte

    // Taking MIN Byte Value and put it into BigInteger Value -1 (over Range of Byte)

    lByte = Byte.MIN_VALUE;

    lBigInteger = BigInteger.valueOf(lByte);
    lBigInteger = lBigInteger.add(new BigInteger("-1"));

    // Try to Convert and Test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lByte = lConverter.convert(lBigInteger);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testBigIntegerToIntegerDatatypeConverter( ) {

    // Testing normal conversion from BigInteger to Integer

    // Initializing BigInteger Value
    BigInteger lBigInteger = new BigInteger("50");
    Integer lExpected = 50;

    BigIntegerToIntegerDatatypeConverter lConverter = new BigIntegerToIntegerDatatypeConverter();
    Integer lInteger = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lInteger);

    // Testing conversion from BigInteger to Integer for MAX Value of Integer

    // Taking MAX Integer Value and put it into BigInteger Value
    lInteger = Integer.MAX_VALUE;
    lBigInteger = BigInteger.valueOf(lInteger);

    // Try to Convert and Test Result
    lExpected = Integer.MAX_VALUE;
    lInteger = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lInteger);

    // Testing conversion from BigInteger to Integer for MIN Value of Integer

    // Taking MIN Integer Value and put it into BigInteger Value
    lInteger = Integer.MIN_VALUE;
    lBigInteger = BigInteger.valueOf(lInteger);
    // Try to Convert and Test Result
    lExpected = Integer.MIN_VALUE;
    lInteger = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lInteger);

    // Testing conversion from BigInteger to Integer for MAX Value +1 (over Range) of Integer

    // Taking MAX Integer Value and put it into BigInteger Value + 1 (over Range of Integer)

    lInteger = Integer.MAX_VALUE;

    lBigInteger = BigInteger.valueOf(lInteger);
    lBigInteger = lBigInteger.add(new BigInteger("1"));

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lInteger = lConverter.convert(lBigInteger);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from BigInteger to Integer for MAX Value -1 (over Range of Integer)

    // Taking MIN Integer Value and put it into BigInteger Value -1 (over Range of Integer)

    lInteger = Integer.MIN_VALUE;

    lBigInteger = BigInteger.valueOf(lInteger);
    lBigInteger = lBigInteger.add(new BigInteger("-1"));

    // Try to Convert and Test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lInteger = lConverter.convert(lBigInteger);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testBigIntegerToLongDatatypeConverter( ) {
    // Testing normal conversion from BigInteger to Long

    // Initializing BigInteger Value
    BigInteger lBigInteger = new BigInteger("50");
    Long lExpected = Long.valueOf("50");

    BigIntegerToLongDatatypeConverter lConverter = new BigIntegerToLongDatatypeConverter();
    Long lLong = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lLong);

    // Testing conversion from BigInteger to Long for MAX Value of Long

    // Taking MAX Long Value and put it into BigInteger Value
    lLong = Long.MAX_VALUE;
    lBigInteger = BigInteger.valueOf(lLong);

    // Try to Convert and Test Result
    lExpected = Long.MAX_VALUE;
    lLong = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lLong);

    // Testing conversion from BigInteger to Long for MIN Value of Long

    // Taking MIN Long Value and put it into BigInteger Value
    lLong = Long.MIN_VALUE;
    lBigInteger = BigInteger.valueOf(lLong);
    // Try to Convert and Test Result
    lExpected = Long.MIN_VALUE;
    lLong = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lLong);

    // Testing conversion from BigInteger to Long for MAX Value +1 (over Range) of Long

    // Taking MAX Long Value and put it into BigInteger Value + 1 (over Range of Long)

    lLong = Long.MAX_VALUE;

    lBigInteger = BigInteger.valueOf(lLong);
    lBigInteger = lBigInteger.add(new BigInteger("1"));

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lLong = lConverter.convert(lBigInteger);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from BigInteger to Long for MAX Value -1 (over Range of Long)

    // Taking MIN Long Value and put it into BigInteger Value -1 (over Range of Long)

    lLong = Long.MIN_VALUE;

    lBigInteger = BigInteger.valueOf(lLong);
    lBigInteger = lBigInteger.add(new BigInteger("-1"));

    // Try to Convert and Test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lLong = lConverter.convert(lBigInteger);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testBigIntegerToShortDatatypeConverter( ) {
    // Testing Normal conversion from BigInteger to Short

    // Initializing BigInteger Value
    BigInteger lBigInteger = new BigInteger("50");
    Short lExpected = Short.valueOf("50");

    BigIntegerToShortDatatypeConverter lConverter = new BigIntegerToShortDatatypeConverter();
    Short lShort = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lShort);

    // Testing conversion from BigInteger to Short for MAX Value of Short

    // Taking MAX Short Value and put it into BigInteger Value
    lShort = Short.MAX_VALUE;
    lBigInteger = BigInteger.valueOf(lShort);

    // Try to Convert and Test Result
    lExpected = Short.MAX_VALUE;
    lShort = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lShort);

    // Testing conversion from BigInteger to Short for MIN Value of Short

    // Taking MIN Short Value and put it into BigInteger Value
    lShort = Short.MIN_VALUE;
    lBigInteger = BigInteger.valueOf(lShort);

    // Try to Convert and Test Result
    lExpected = Short.MIN_VALUE;
    lShort = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lShort);

    // Testing conversion from BigInteger to Short for MAX Value +x (over Range of Short)

    // Taking MAX Short Value and put it into BigInteger Value + 1 (over Range of Short)

    lShort = Short.MAX_VALUE;

    lBigInteger = BigInteger.valueOf(lShort);
    lBigInteger = lBigInteger.add(new BigInteger("1"));

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lShort = lConverter.convert(lBigInteger);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from BigInteger to Short for MIN Value +x (over Range) of Short

    // Taking MIN Short Value and put it into BigInteger Value -1 (over Range of Short)

    lShort = Short.MIN_VALUE;

    lBigInteger = BigInteger.valueOf(lShort);
    lBigInteger = lBigInteger.add(new BigInteger("-1"));

    // Try to Convert and Test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lShort = lConverter.convert(lBigInteger);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testBigIntegerToStringDatatypeConverter( ) {
    // Testing normal conversion from BigInteger to String

    // Initializing BigInteger Value
    BigInteger lBigInteger = new BigInteger("20000");
    String lExpected = "20000";

    BigIntegerToStringDatatypeConverter lConverter = new BigIntegerToStringDatatypeConverter();
    String lString = lConverter.convert(lBigInteger);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testBooleanToStringDatatypeConverter( ) {
    // Testing normal conversion from Boolean TRUE to String

    // Initializing BigInteger Value
    Boolean lBoolean = true;
    String lExpected = "true";

    BooleanToStringDatatypeConverter lConverter = new BooleanToStringDatatypeConverter();
    String lString = lConverter.convert(lBoolean);
    assertEquals(lExpected, lString);

    // Testing normal conversion from Boolean FALSE to String
    lBoolean = false;
    lExpected = "false";
    lString = lConverter.convert(lBoolean);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testByteToBigIntegerDatatypeConverter( ) {
    // Testing normal conversion from Byte to BigInteger

    // Initializing BigInteger Value
    Byte lByte = 50;
    BigInteger lExpected = new BigInteger("50");

    // Test for correct conversion
    ByteToBigIntegerDatatypeConverter lConverter = new ByteToBigIntegerDatatypeConverter();
    BigInteger lBigInteger = lConverter.convert(lByte);
    assertEquals(lExpected, lBigInteger);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testByteToCharacterDatatypeConverter( ) {
    // Testing normal conversion from Byte to Character

    // Initializing Character Value
    Byte lByte = 100;
    Character lExpected = 'd';

    ByteToCharacterDatatypeConverter lConverter = new ByteToCharacterDatatypeConverter();
    Character lCharacter = lConverter.convert(lByte);
    assertEquals(lExpected, lCharacter);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testByteToStringDatatypeConverter( ) {
    // Testing normal conversion from Byte to String

    // Initializing BigInteger Value
    Byte lByte = 100;
    String lExpected = "100";

    ByteToStringDatatypeConverter lConverter = new ByteToStringDatatypeConverter();
    String lString = lConverter.convert(lByte);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testCharacterToByteDatatypeConverter( ) {
    // Testing normal conversion from Character to Byte

    Character lCharacter = Character.valueOf('d');
    Byte lExpected = 100;

    CharacterToByteDatatypeConverter lConverter = new CharacterToByteDatatypeConverter();
    Byte lByte = lConverter.convert(lCharacter);
    assertEquals(lExpected, lByte);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testCharacterToIntegerDatatypeConverter( ) {
    // Testing normal conversion from Character to Integer

    Character lCharacter = Character.valueOf('d');
    Integer lExpected = 100;

    CharacterToIntegerDatatypeConverter lConverter = new CharacterToIntegerDatatypeConverter();
    Integer lInteger = lConverter.convert(lCharacter);
    assertEquals(lExpected, lInteger);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testCharacterToLongDatatypeConverter( ) {
    // Testing normal conversion from Character to Long

    Character lCharacter = Character.valueOf('d');
    Long lExpected = Long.valueOf("100");

    CharacterToLongDatatypeConverter lConverter = new CharacterToLongDatatypeConverter();
    Long lLong = lConverter.convert(lCharacter);
    assertEquals(lExpected, lLong);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testCharacterToShortDatatypeConverter( ) {
    // Testing normal conversion from Character to Short

    Character lCharacter = Character.valueOf('d');
    Short lExpected = Short.valueOf("100");

    CharacterToShortDatatypeConverter lConverter = new CharacterToShortDatatypeConverter();
    Short lShort = lConverter.convert(lCharacter);
    assertEquals(lExpected, lShort);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testCharacterToStringDatatypeConverter( ) {
    // Testing normal conversion from Character to String

    Character lCharacter = Character.valueOf('d');
    String lExpected = "d";

    CharacterToStringDatatypeConverter lConverter = new CharacterToStringDatatypeConverter();
    String lString = lConverter.convert(lCharacter);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testDoubleToBigDecimalDatatypeConverter( ) {
    // Testing normal conversion from Double to BigDecimal

    // Initializing BigDecimal and Double Value
    Double lDouble = Double.valueOf("50");
    BigDecimal lExpected = new BigDecimal("50.0");

    // Test for correct conversion
    DoubleToBigDecimalDatatypeConverter lConverter = new DoubleToBigDecimalDatatypeConverter();
    BigDecimal lBigDecimal = lConverter.convert(lDouble);
    assertEquals(lExpected, lBigDecimal);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testDoubleToFloatDatatypeConverter( ) {
    // Testing normal conversion from Double to Float

    // Initializing Double Value
    Double lDouble = Double.valueOf(20000);
    Float lExpected = Float.parseFloat("20000.0");

    DoubleToFloatDatatypeConverter lConverter = new DoubleToFloatDatatypeConverter();
    Float lFloat = lConverter.convert(lDouble);
    assertEquals(lExpected, lFloat);

    // Testing conversion from Double to Float for MAX Value of Float

    // Taking MAX Float Value and put it into Double Value
    lFloat = Float.MAX_VALUE;
    lDouble = Double.valueOf(lFloat);

    // Try to Convert and Test Result
    lExpected = Float.MAX_VALUE;
    lFloat = lConverter.convert(lDouble);
    assertEquals(lExpected, lFloat);

    // Testing conversion from Double to Float for MIN Value of Float

    // Taking MIN Float Value and put it into Double Value
    lFloat = Float.MIN_VALUE;
    lDouble = Double.valueOf(lFloat);

    // Try to Convert and Test Result
    lExpected = Float.MIN_VALUE;
    lFloat = lConverter.convert(lDouble);
    assertEquals(lExpected, lFloat);

    // Test null handling.
    assertNull(lConverter.convert(null));

    // Test exception handling
    lFloat = Float.MIN_VALUE;
    lDouble = Double.valueOf(lFloat);
    lDouble = lDouble - 1.0;
    try {
      lConverter.convert(lDouble);
      fail("Exception expected.");
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, e.getErrorCode());
    }

    lDouble = Double.valueOf(Double.MAX_VALUE);
    lDouble = lDouble + 10;
    try {
      lConverter.convert(lDouble);
      fail("Exception expected.");
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE, e.getErrorCode());
    }

  }

  @Test
  public void testDoubleToStringDatatypeConverter( ) {
    // Testing normal conversion from Double to String

    Double lDouble = Double.valueOf("800.33");
    String lExpected = "800.33";

    DoubleToStringDatatypeConverter lConverter = new DoubleToStringDatatypeConverter();
    String lString = lConverter.convert(lDouble);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testFloatToBigDecimalDatatypeConverter( ) {
    // Testing normal conversion from Float to BigDecimal

    // Initializing BigDecimal and Float Value
    Float lFloat = Float.valueOf("50.5");
    BigDecimal lExpected = new BigDecimal("50.5");

    // Test for correct conversion
    FloatToBigDecimalDatatypeConverter lConverter = new FloatToBigDecimalDatatypeConverter();
    BigDecimal lBigDecimal = lConverter.convert(lFloat);
    assertEquals(lExpected, lBigDecimal);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testFloatToDoubleDatatypeConverter( ) {
    // Testing normal conversion from Float to Double

    // Initializing Float Value
    Float lFloat = Float.valueOf(20000.33f);
    Double lExpected = Double.parseDouble("20000.33");

    FloatToDoubleDatatypeConverter lConverter = new FloatToDoubleDatatypeConverter();
    Double lDouble = lConverter.convert(lFloat);
    assertEquals(lExpected, lDouble);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testFloatToStringDatatypeConverter( ) {
    // Testing normal conversion from Float to String

    Float lFloat = Float.valueOf("800.33");
    String lExpected = "800.33";

    FloatToStringDatatypeConverter lConverter = new FloatToStringDatatypeConverter();
    String lString = lConverter.convert(lFloat);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testIntegerToBigIntegerDatatypeConverter( ) {
    // Testing normal conversion from Integer to BigInteger

    // Initializing BigInteger Value
    Integer lInteger = 50;
    BigInteger lExpected = new BigInteger("50");

    // Test for correct conversion
    IntegerToBigIntegerDatatypeConverter lConverter = new IntegerToBigIntegerDatatypeConverter();
    BigInteger lBigInteger = lConverter.convert(lInteger);
    assertEquals(lExpected, lBigInteger);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testIntegerToCharacterDatatypeConverter( ) {
    // Testing normal conversion from Integer to Character

    // Initializing Character Value
    Integer lInteger = 100;
    Character lExpected = 'd';

    IntegerToCharacterDatatypeConverter lConverter = new IntegerToCharacterDatatypeConverter();
    Character lCharacter = lConverter.convert(lInteger);
    assertEquals(lExpected, lCharacter);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testIntegerToStringDatatypeConverter( ) {
    // Testing normal conversion from Double to String

    Integer lInteger = 800;
    String lExpected = "800";

    IntegerToStringDatatypeConverter lConverter = new IntegerToStringDatatypeConverter();
    String lString = lConverter.convert(lInteger);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testLongToBigIntegerDatatypeConverter( ) {
    // Testing normal conversion from Long to BigInteger

    // Initializing BigInteger Value
    Long lLong = Long.valueOf("50");
    BigInteger lExpected = new BigInteger("50");

    // Test for correct conversion
    LongToBigIntegerDatatypeConverter lConverter = new LongToBigIntegerDatatypeConverter();
    BigInteger lBigInteger = lConverter.convert(lLong);
    assertEquals(lExpected, lBigInteger);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testLongToCharacterDatatypeConverter( ) {
    // Testing normal conversion from Long to Character

    // Initializing Character Value
    Long lLong = Long.valueOf("100");
    Character lExpected = '1';

    LongToCharacterDatatypeConverter lConverter = new LongToCharacterDatatypeConverter();
    Character lCharacter = lConverter.convert(lLong);
    assertEquals(lExpected, lCharacter);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testLongToStringDatatypeConverter( ) {
    // Testing normal conversion from Long to String

    Long lLong = Long.valueOf("800");
    String lExpected = "800";

    LongToStringDatatypeConverter lConverter = new LongToStringDatatypeConverter();
    String lString = lConverter.convert(lLong);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testShortToBigIntegerDatatypeConverter( ) {
    // Testing normal conversion from Short to BigInteger

    // Initializing BigInteger Value
    Short lShort = 50;
    BigInteger lExpected = new BigInteger("50");

    // Test for correct conversion
    ShortToBigIntegerDatatypeConverter lConverter = new ShortToBigIntegerDatatypeConverter();
    BigInteger lBigInteger = lConverter.convert(lShort);
    assertEquals(lExpected, lBigInteger);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testShortToCharacterDatatypeConverter( ) {
    // Testing normal conversion from Short to Character

    // Initializing Character Value
    Short lShort = 100;
    Character lExpected = '1';

    ShortToCharacterDatatypeConverter lConverter = new ShortToCharacterDatatypeConverter();
    Character lCharacter = lConverter.convert(lShort);
    assertEquals(lExpected, lCharacter);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testShortToStringDatatypeConverter( ) {
    // Testing normal conversion from Short to String

    Short lShort = 800;
    String lExpected = "800";

    ShortToStringDatatypeConverter lConverter = new ShortToStringDatatypeConverter();
    String lString = lConverter.convert(lShort);
    assertEquals(lExpected, lString);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToBigDecimalDatatypeConverter( ) {
    // Testing normal conversion from String to BigDecimal

    String lString = "500";
    BigDecimal lExpected = new BigDecimal("500.0");

    StringToBigDecimalDatatypeConverter lConverter = new StringToBigDecimalDatatypeConverter();
    BigDecimal lBigDecimal = lConverter.convert(lString);
    assertEquals(lExpected, lBigDecimal);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToBigIntegerDatatypeConverter( ) {
    // Testing normal conversion from String to BigInteger

    String lString = "5000000000000000";
    BigInteger lExpected = new BigInteger("5000000000000000");

    StringToBigIntegerDatatypeConverter lConverter = new StringToBigIntegerDatatypeConverter();
    BigInteger lBigInteger = lConverter.convert(lString);
    assertEquals(lExpected, lBigInteger);

    // Testing conversion from String to BigInteger for Limitation of String length

    lString = "5000000000000000000000000";

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lBigInteger = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToBooleanDatatypeConverter( ) {
    // Testing conversion from String to Boolean for value true

    String lString = "true";
    Boolean lExpected = true;

    StringToBooleanDatatypeConverter lConverter = new StringToBooleanDatatypeConverter();
    Boolean lBoolean = lConverter.convert(lString);
    assertEquals(lExpected, lBoolean);

    // Testing conversion from String to Boolean for value false

    lString = "false";
    lExpected = false;

    lBoolean = lConverter.convert(lString);
    assertEquals(lExpected, lBoolean);

    // Testing conversion for any other value

    lString = "hallo";
    lExpected = false;

    lBoolean = lConverter.convert(lString);
    assertEquals(lExpected, lBoolean);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToByteDatatypeConverter( ) {
    // Initializing String and Byte Value
    String lString = "50";
    Byte lExpected = 50;

    StringToByteDatatypeConverter lConverter = new StringToByteDatatypeConverter();
    Byte lByte = lConverter.convert(lString);
    assertEquals(lExpected, lByte);

    // Testing conversion from String to Byte for MAX Value of Byte

    // Taking MAX Byte Value and put it into String Value
    lByte = Byte.MAX_VALUE;
    lString = String.valueOf(lByte);

    // Try to Convert and Test Result
    lExpected = Byte.MAX_VALUE;
    lByte = lConverter.convert(lString);
    assertEquals(lExpected, lByte);

    // Testing conversion from String to Byte for MIN Value of Byte

    // Taking MIN Byte Value and put it into String Value
    lByte = Byte.MIN_VALUE;
    lString = String.valueOf(lByte);

    // Try to Convert and Test Result
    lExpected = Byte.MIN_VALUE;
    lByte = lConverter.convert(lString);
    assertEquals(lExpected, lByte);

    // Testing conversion from String to Byte for MAX Value +x (over Range) of Byte

    // Taking MAX Byte Value and put it into BigInteger Value + 1 (over Range of Byte)

    lByte = Byte.MAX_VALUE;

    int ValueOfMaxByteValue = lByte.intValue();
    ValueOfMaxByteValue = ValueOfMaxByteValue + 1;
    lString = Integer.toString(ValueOfMaxByteValue);

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lByte = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from String to Byte for MIN Value +x (over Range) of Byte

    // Taking MIN Byte Value and put it into String Value - 1 (over Range of Byte)

    lByte = Byte.MIN_VALUE;

    int ValueOfMinByteValue = lByte.intValue();
    ValueOfMinByteValue = ValueOfMinByteValue - 1;
    lString = Integer.toString(ValueOfMinByteValue);

    // Try to convert and test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lByte = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToCharacterDatatypeConverter( ) {
    // Testing normal conversion from String to Character

    // Initializing Character Value
    String lString = "d";
    Character lExpected = 'd';

    StringToCharacterDatatypeConverter lConverter = new StringToCharacterDatatypeConverter();
    Character lCharacter = lConverter.convert(lString);
    assertEquals(lExpected, lCharacter);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToDoubleDatatypeConverter( ) {
    // Initializing String and Double Value
    String lString = "10000";
    Double lExpected = 10000.0;

    StringToDoubleDatatypeConverter lConverter = new StringToDoubleDatatypeConverter();
    Double lDouble = lConverter.convert(lString);
    assertEquals(lExpected, lDouble);

    // Testing conversion from String to Double for MAX Value of Double

    // Taking MAX DOuble Value and put it into String Value

    lString =
        "333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333";

    // Try to Convert and Test Result
    lExpected =
        333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333.0;
    lDouble = lConverter.convert(lString);
    assertEquals(lExpected, lDouble);

    // Testing conversion from String to Double for MIN Value of Double

    // Taking MIN Byte Value and put it into String Value
    lString =
        "-333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333";

    // Try to Convert and Test Result
    lExpected =
        -333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333.0;
    lDouble = lConverter.convert(lString);
    assertEquals(lExpected, lDouble);

    // Testing conversion from String to Double for MAX Value +x (over Range) of Double

    // Taking MAX Double Value and put it into String Value + 1 (over Range of Double)

    lString =
        "333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333";

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lDouble = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from String to Double for MIN Value +x (over Range) of Double

    // Taking MIN Double Value and put it into String Value - 1 (over Range of Double)

    lString =
        "-333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333";

    // Try to Convert and Test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lDouble = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToFloatDatatypeConverter( ) {
    // Initializing String and Float Value
    String lString = "50";
    Float lExpected = Float.parseFloat("50.0");

    StringToFloatDatatypeConverter lConverter = new StringToFloatDatatypeConverter();
    Float lFloat = lConverter.convert(lString);
    assertEquals(lExpected, lFloat);

    // Testing conversion from String to Float for MAX Value of Float

    // Taking MAX Float Value and put it into String Value

    lString = "123456789012345678901234567890";

    // Try to Convert and Test Result
    lExpected = Float.parseFloat("123456789012345678901234567890");
    lFloat = lConverter.convert(lString);
    assertEquals(lExpected, lFloat);

    // Testing conversion from String to Float for MIN Value of Float

    // Taking MIN Float Value and put it into String Value

    lString = "-12345678901234567890123456789";

    // Try to Convert and Test Result
    lExpected = Float.parseFloat("-12345678901234567890123456789");
    lFloat = lConverter.convert(lString);
    assertEquals(lExpected, lFloat);

    // Testing conversion from String to Byte for MAX Value +x (over Range) of Byte

    // Taking MAX Byte Value and put it into BigInteger Value + 1 (over Range of Byte)

    lString = "1234567890123456789012345678901";

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lFloat = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from String to Byte for MIN Value +x (over Range) of Byte

    // Taking MIN Byte Value and put it into String Value - 1 (over Range of Byte)

    lString = "-1234567890123456789012345678901";

    // Try to Convert and Test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lFloat = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToIntegerDatatypeConverter( ) {
    // Initializing String and Integer Value
    String lString = "50";
    Integer lExpected = 50;

    StringToIntegerDatatypeConverter lConverter = new StringToIntegerDatatypeConverter();
    Integer lInteger = lConverter.convert(lString);
    assertEquals(lExpected, lInteger);

    // Testing conversion from String to Integer for MAX Value of Integer

    // Taking MAX Integer Value and put it into String Value
    lInteger = Integer.MAX_VALUE;
    lString = String.valueOf(lInteger);

    // Try to Convert and Test Result
    lExpected = Integer.MAX_VALUE;
    lInteger = lConverter.convert(lString);
    assertEquals(lExpected, lInteger);

    // Testing conversion from String to Integer for MIN Value of Integer

    // Taking MIN Integer Value and put it into String Value
    lInteger = Integer.MIN_VALUE;
    lString = String.valueOf(lInteger);

    // Try to Convert and Test Result
    lExpected = Integer.MIN_VALUE;
    lInteger = lConverter.convert(lString);
    assertEquals(lExpected, lInteger);

    // Testing conversion from String to Integer for MAX Value +x (over Range) of Integer

    // Taking MAX Integer Value and put it into String Value + 1 (over Range of Integer)

    Long lLong = Long.valueOf(Long.parseLong(String.valueOf(Integer.MAX_VALUE)));
    lLong = lLong + 1;
    lString = String.valueOf(lLong);

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lInteger = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from String to Integer for MIN Value +x (over Range) of Integer

    // Taking MIN Integer Value and put it into String Value - 1 (over Range of Integer)

    lLong = Long.parseLong(String.valueOf(Integer.MIN_VALUE));
    lLong = lLong - 1;
    lString = String.valueOf(lLong);

    // Try to Convert and Test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lInteger = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToLongDatatypeConverter( ) {
    // Initializing String and Long Value
    String lString = "50";
    Long lExpected = Long.valueOf("50");

    StringToLongDatatypeConverter lConverter = new StringToLongDatatypeConverter();
    Long lLong = lConverter.convert(lString);
    assertEquals(lExpected, lLong);

    // Testing conversion from String to Long for MAX Value of Long

    // Taking MAX Long Value and put it into String Value
    lLong = Long.MAX_VALUE;
    lString = String.valueOf(lLong);

    // Try to Convert and Test Result
    lExpected = Long.MAX_VALUE;
    lLong = lConverter.convert(lString);
    assertEquals(lExpected, lLong);

    // Testing conversion from String to Long for MIN Value of Long

    // Taking MIN Long Value and put it into String Value
    lLong = Long.MIN_VALUE;
    lString = String.valueOf(lLong);

    // Try to Convert and Test Result
    lExpected = Long.MIN_VALUE;
    lLong = lConverter.convert(lString);
    assertEquals(lExpected, lLong);

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToShortDatatypeConverter( ) {
    // Initializing String and Short Value
    String lString = "50";
    Short lExpected = 50;

    StringToShortDatatypeConverter lConverter = new StringToShortDatatypeConverter();
    Short lShort = lConverter.convert(lString);
    assertEquals(lExpected, lShort);

    // Testing conversion from String to Short for MAX Value of Short

    // Taking MAX Short Value and put it into String Value
    lShort = Short.MAX_VALUE;
    lString = String.valueOf(lShort);

    // Try to Convert and Test Result
    lExpected = Short.MAX_VALUE;
    lShort = lConverter.convert(lString);
    assertEquals(lExpected, lShort);

    // Testing conversion from String to Short for MIN Value of Short

    // Taking MIN Short Value and put it into String Value
    lShort = Short.MIN_VALUE;
    lString = String.valueOf(lShort);

    // Try to Convert and Test Result
    lExpected = Short.MIN_VALUE;
    lShort = lConverter.convert(lString);
    assertEquals(lExpected, lShort);

    // Testing conversion from String to Short for MAX Value +x (over Range) of Short

    // Taking MAX Short Value and put it into String Value + 1 (over Range of Short)

    Integer lInteger = Integer.parseInt(String.valueOf(Integer.MAX_VALUE));
    lInteger = lInteger + 1;
    lString = String.valueOf(lInteger);

    // Try to Convert and Test if right Exception is thrown
    ErrorCode lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lShort = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Testing conversion from String to Integer for MIN Value +x (over Range) of Integer

    // Taking MIN Integer Value and put it into String Value - 1 (over Range of Integer)

    lInteger = Integer.parseInt(String.valueOf(Integer.MIN_VALUE));
    lInteger = lInteger - 1;
    lString = String.valueOf(lInteger);

    // Try to Convert and Test if right Exception is thrown
    lErrorCode = XFunMessages.DATATYPE_CONVERSION_NOT_POSSIBLE;

    try {
      lShort = lConverter.convert(lString);
      fail("No Exception Thrown");
    }
    catch (JEAFSystemException lException) {
      assertTrue(lException instanceof JEAFSystemException);
      assertEquals(lErrorCode, lException.getErrorCode());
    }

    // Test null handling.
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testStringToStringDatatypeConverter( ) {
    StringToStringDatatypeConverter lConverter = new StringToStringDatatypeConverter();
    assertEquals("Hello", lConverter.convert("Hello"));
    String lInput = "Input";
    assertTrue(lInput == lConverter.convert(lInput));
    assertNull(lConverter.convert(null));
  }

  @Test
  public void testClassToStringDatatypeConverters( ) {
    ClassToStringDatatypeConverter lConverter = new ClassToStringDatatypeConverter();

    assertEquals(String.class.getName(), lConverter.convert(String.class));
    assertNull(lConverter.convert(null));

  }

  @Test
  public void testStringToClassDatatypeConverters( ) {
    StringToClassDatatypeConverter lConverter = new StringToClassDatatypeConverter();

    assertEquals(String.class, lConverter.convert(String.class.getName()));
    assertNull(lConverter.convert(null));

    // Test exception handling
    try {
      lConverter.convert("a.b.C");
      fail("Exception expected.");
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.CLASS_NOT_LOADABLE, e.getErrorCode());
    }
  }

  @Test
  public void testDateToCalendarDatatypeConverter( ) {
    DateToCalendarDatatypeConverter lConverter = new DateToCalendarDatatypeConverter();
    Date lDate = new Date();
    assertEquals(DateTools.getDateTools().toCalendar(lDate), lConverter.convert(lDate));

    assertNull(lConverter.convert(null));
  }

  @Test
  public void testCalendarToDateDatatypeConverter( ) {
    CalendarToDateDatatypeConverter lConverter = new CalendarToDateDatatypeConverter();
    Calendar lCalendar = DateTools.getDateTools().newCalendar();
    assertEquals(DateTools.getDateTools().toDate(lCalendar), lConverter.convert(lCalendar));

    assertNull(lConverter.convert(null));
  }

  @Test
  public void testCustomDatatypeConverters( ) {
    // Lookup custom datatype converter
    DatatypeConverter<Apple, Pear> lConverter =
        XFun.getDatatypeConverterRegistry().getConverter(Apple.class, Pear.class);
    assertNotNull(lConverter, "Apple-to-Pear datatype converter is missing.");

    // Try to lookup converter that is not configured.
    try {
      XFun.getDatatypeConverterRegistry().getConverter(Pear.class, Apple.class);
      fail("Expection exception when trying to lookup converter for in- and output thatis not available.");
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.NO_DATATYPE_CONVERTER_DEFINED, e.getErrorCode(), "Wrong error code.");
    }

    // Test if default datatype converter was overwritten with custom one.
    @SuppressWarnings("rawtypes")
    DatatypeConverter<Class, String> lMyConverter =
        XFun.getDatatypeConverterRegistry().getConverter(Class.class, String.class);
    assertEquals(MyClassToStringConverter.class, lMyConverter.getClass());

    // Try to add converter manually
    PearToAppleDatatypeConverter lPearToAppleDatatypeConverter = new PearToAppleDatatypeConverter();
    DatatypeConverterRegistryImpl lRegistry = (DatatypeConverterRegistryImpl) XFun.getDatatypeConverterRegistry();
    lRegistry.addConverter(lPearToAppleDatatypeConverter);
    DatatypeConverter<Pear, Apple> lP2AConverter =
        XFun.getDatatypeConverterRegistry().getConverter(Pear.class, Apple.class);
    assertEquals(lPearToAppleDatatypeConverter, lP2AConverter);

    // Try to overwrite standard converter
    try {
      lRegistry.addConverter(new ClassToStringDatatypeConverter());
      fail("Exception expected");
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.DATATYPE_CONVERTER_ALREADY_EXISTS, e.getErrorCode());
    }

    // Try to overwrite custom converter
    try {
      lRegistry.addConverter(new PearToAppleDatatypeConverter());
      fail("Exception expected");
    }
    catch (JEAFSystemException e) {
      assertEquals(XFunMessages.DATATYPE_CONVERTER_ALREADY_EXISTS, e.getErrorCode());
    }
  }

  @Test
  public void testURLToStringDatatypeConverter( ) {
    DatatypeConverter<URL, String> lConverter =
        XFun.getDatatypeConverterRegistry().getConverter(URL.class, String.class);
    assertNotNull(lConverter);

    String lURLString = "https://www.anaptecs.de";
    URL lURL = Tools.getNetworkingTools().toURL(lURLString);
    assertEquals(lURLString, lConverter.convert(lURL));

    assertEquals(null, lConverter.convert(null));
  }

  @Test
  public void testStringToURLDatatypeConverter( ) {
    DatatypeConverter<String, URL> lConverter =
        XFun.getDatatypeConverterRegistry().getConverter(String.class, URL.class);
    assertNotNull(lConverter);

    String lURLString = "https://www.anaptecs.de";
    URL lURL = Tools.getNetworkingTools().toURL(lURLString);
    assertEquals(lURL, lConverter.convert(lURLString));

    assertEquals(null, lConverter.convert(null));
  }

  @Test
  public void testLocaleToStringDatatypeConverter( ) {
    DatatypeConverter<Locale, String> lConverter =
        XFun.getDatatypeConverterRegistry().getConverter(Locale.class, String.class);
    assertNotNull(lConverter);

    assertEquals("de_DE", lConverter.convert(Locale.GERMANY));
    assertEquals("de", lConverter.convert(Locale.GERMAN));
    assertEquals("it", lConverter.convert(Locale.ITALIAN));

    assertEquals(null, lConverter.convert(null));
  }

  @Test
  public void testStringToLocaleDatatypeConverter( ) {
    DatatypeConverter<String, Locale> lConverter =
        XFun.getDatatypeConverterRegistry().getConverter(String.class, Locale.class);
    assertNotNull(lConverter);

    assertEquals(Locale.GERMANY, lConverter.convert("de_DE"));
    assertEquals(Locale.GERMAN, lConverter.convert("de"));
    assertEquals(Locale.ITALIAN, lConverter.convert("it"));

    assertEquals(null, lConverter.convert(null));
  }

  @Test
  public void testCalendarToStringDatatypeConverter( ) {
    DatatypeConverter<Calendar, String> lConverter =
        XFun.getDatatypeConverterRegistry().getConverter(Calendar.class, String.class);
    assertNotNull(lConverter);

    DateTools lDateTools = DateTools.getDateTools();
    Calendar lCalendar = lDateTools.toCalendar(lDateTools.toDate(2020, 12, 23, 13, 17, 05, 199));

    assertEquals("2020-12-23 13:17:05.199", lConverter.convert(lCalendar));

    assertEquals(null, lConverter.convert(null));
  }

  @Test
  public void testStringToCalendarDatatypeConverter( ) {
    DatatypeConverter<String, Calendar> lConverter =
        XFun.getDatatypeConverterRegistry().getConverter(String.class, Calendar.class);
    assertNotNull(lConverter);

    DateTools lDateTools = DateTools.getDateTools();
    Calendar lCalendar = lDateTools.toCalendar(lDateTools.toDate(2020, 12, 23, 13, 17, 05, 199));

    assertEquals(lCalendar, lConverter.convert("2020-12-23 13:17:05.199"));

    assertEquals(null, lConverter.convert(null));
  }

  @Test
  public void testDateToStringDatatypeConverter( ) {
    DatatypeConverter<Date, String> lConverter =
        XFun.getDatatypeConverterRegistry().getConverter(Date.class, String.class);
    assertNotNull(lConverter);

    DateTools lDateTools = DateTools.getDateTools();
    Calendar lCalendar = lDateTools.toCalendar(lDateTools.toDate(2020, 12, 23, 13, 17, 05, 199));

    assertEquals("2020-12-23 13:17:05.199", lConverter.convert(lDateTools.toDate(lCalendar)));

    assertEquals(null, lConverter.convert(null));
  }

  @Test
  public void testStringToDateDatatypeConverter( ) {
    DatatypeConverter<String, Date> lConverter =
        XFun.getDatatypeConverterRegistry().getConverter(String.class, Date.class);
    assertNotNull(lConverter);

    DateTools lDateTools = DateTools.getDateTools();
    Calendar lCalendar = lDateTools.toCalendar(lDateTools.toDate(2020, 12, 23, 13, 17, 05, 199));

    assertEquals(lDateTools.toDate(lCalendar), lConverter.convert("2020-12-23 13:17:05.199"));

    assertEquals(null, lConverter.convert(null));
  }

}
