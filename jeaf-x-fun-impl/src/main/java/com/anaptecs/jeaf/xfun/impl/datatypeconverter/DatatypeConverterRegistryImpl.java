/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * Copyright 2004 - 2013 All rights reserved.
 */

package com.anaptecs.jeaf.xfun.impl.datatypeconverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anaptecs.jeaf.xfun.annotations.DatatypeConverterImpl;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationReader;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistry;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFBootstrapException;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
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
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CalendarToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToByteDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToLongDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToShortDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.CharacterToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.ClassToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.DateToCalendarDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.DateToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.DoubleToBigDecimalDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.DoubleToFloatDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.DoubleToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.FloatToBigDecimalDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.FloatToDoubleDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.FloatToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.IntegerToBigIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.IntegerToCharacterDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.IntegerToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.LocaleToStringDatatypeConverter;
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
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToCalendarDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToCharacterDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToClassDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToDateDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToDoubleDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToFloatDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToIntegerDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToLocaleDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToLongDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToShortDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToStringDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.StringToURLDatatypeConverter;
import com.anaptecs.jeaf.xfun.impl.datatypeconverter.converters.URLToStringDatatypeConverter;

/**
 * This class manages all available datatype converters. In order to avoid that there is more than one registry of
 * datatype converters, this class is realized as singleton.
 *
 * @author JEAF Development Team
 * @version 1.0
 */
public final class DatatypeConverterRegistryImpl implements DatatypeConverterRegistry {
  /**
   * Initialize object. Nothing special to do.
   */
  DatatypeConverterRegistryImpl( ) {
    // Nothing to do.
    this.loadDefaultDatatypeConverters();
    this.loadAdditionalConverters();
  }

  /**
   * Map contains all available datatype converters. Thereby the combination of in- and output type is the key and the
   * appropriate converter is the value.
   */
  private Map<String, DatatypeConverter<?, ?>> converters = new HashMap<>();

  /**
   * Method returns the appropriate converter for the passed combination of in- and output type.
   *
   * @param <I> Input type. The parameter must not be null.
   * @param <O> Output type. The parameter must not be null.
   * @param pInputType InputType The parameter must not be null.
   * @param pOutputType OutputType. The parameter must not be null
   * @return {@link DatatypeConverter} Datatype converter for the passed combination of in- and output type. The method
   * never returns null.
   * @throws JEAFSystemException In the case that there is no converter available for a specific combination an
   * exception will be thrown.
   */
  @SuppressWarnings("unchecked")
  @Override
  public <I, O> DatatypeConverter<I, O> getConverter(Class<I> pInputType, Class<O> pOutputType) {
    // Check parameters for null.
    Check.checkInvalidParameterNull(pInputType, "pInputType");
    Check.checkInvalidParameterNull(pOutputType, "pOutputType");

    // In order to generate as less garbage as possible we use a string buffer.
    String lKey = this.generateConverterKey(pInputType, pOutputType);

    // Get DatatypeConverter from map.
    DatatypeConverter<I, O> lDatatypeConverter = (DatatypeConverter<I, O>) converters.get(lKey);
    if (lDatatypeConverter != null) {
      return lDatatypeConverter;
    }
    // Throw exception.
    else {
      String[] lParams = new String[] { pInputType.getName(), pOutputType.getName() };
      throw new JEAFSystemException(XFunMessages.NO_DATATYPE_CONVERTER_DEFINED, lParams);
    }
  }

  /**
   * Method generates a new key for the data type converter that is responsible for the conversion of the passed input
   * and output types.
   *
   * @param pInputType Input type. The parameter must not be null.
   * @param pOutputType Output type. The parameter must not be null.
   * @return {@link String} Key that can be used for a data type converter for the passed types. The method never
   * returns null
   */
  private String generateConverterKey(Class<?> pInputType, Class<?> pOutputType) {
    // Check parameters for null.
    Assert.assertNotNull(pInputType, "pInputType");
    Assert.assertNotNull(pOutputType, "pOutputType");

    // Generate key.
    String lInputType = pInputType.getName();
    String lOutputType = pOutputType.getName();
    StringBuilder lBuilder = new StringBuilder(lInputType.length() + lOutputType.length() + 1);
    lBuilder.append(lInputType);
    lBuilder.append(';');
    lBuilder.append(lOutputType);
    return lBuilder.toString();
  }

  /**
   * Method adds the passed datatype converter t the registry. For security reasons existing converters can not be
   * overwritten at runtime.
   *
   * @param pConverter Converter that should be added. The parameter must not be null and there must not yet exist a
   * converter for the input and output type of this one.
   */
  public void addConverter(DatatypeConverter<?, ?> pConverter) {
    this.addConverter(pConverter, false);
  }

  /**
   * Method adds the passed converter to the map of available converters.
   *
   * @param pConverter Convert that should be added. The parameter must not be null.
   * @param pOverwrite Parameter defines whether existing converter should be overwritten or not. <code>true</code>
   * means overwrite existing converters.
   */
  private void addConverter(DatatypeConverter<?, ?> pConverter, boolean pOverwrite) {
    // Check parameters for null.
    Assert.assertNotNull(pConverter, "pConverter");

    // Get key of converter.
    Class<?> lInputType = pConverter.getInputType();
    Class<?> lOutputType = pConverter.getOutputType();
    String lKey = this.generateConverterKey(lInputType, lOutputType);

    // Check existence.
    DatatypeConverter<?, ?> lExistingConverter = converters.get(lKey);

    // Add new converter.
    if (lExistingConverter == null) {
      converters.put(lKey, pConverter);
    }
    // Overwriting existing converter.
    else if (pOverwrite == true) {
      converters.put(lKey, pConverter);
    }
    // Existing converter must not be overwritten -> throw Exception
    else {
      String[] lParams = new String[] { lInputType.getName(), lOutputType.getName(),
        lExistingConverter.getClass().getName(), pConverter.getClass().getName() };
      throw new JEAFSystemException(XFunMessages.DATATYPE_CONVERTER_ALREADY_EXISTS, lParams);
    }
  }

  /**
   * Method loads all default datatype converters. These converter implementations are part of X-Fun and thus are always
   * available.
   */
  private void loadDefaultDatatypeConverters( ) {
    // Load all known default data type converters.
    this.addConverter(new IntegerToStringDatatypeConverter(), false);
    this.addConverter(new BigDecimalToDoubleDatatypeConverter(), false);
    this.addConverter(new BigDecimalToFloatDatatypeConverter(), false);
    this.addConverter(new BigDecimalToStringDatatypeConverter(), false);
    this.addConverter(new BigIntegerToByteDatatypeConverter(), false);
    this.addConverter(new BigIntegerToIntegerDatatypeConverter(), false);
    this.addConverter(new BigIntegerToLongDatatypeConverter(), false);
    this.addConverter(new BigIntegerToShortDatatypeConverter(), false);
    this.addConverter(new BigIntegerToStringDatatypeConverter(), false);
    this.addConverter(new BooleanToStringDatatypeConverter(), false);
    this.addConverter(new ByteToBigIntegerDatatypeConverter(), false);
    this.addConverter(new ByteToCharacterDatatypeConverter(), false);
    this.addConverter(new ByteToStringDatatypeConverter(), false);
    this.addConverter(new CharacterToByteDatatypeConverter(), false);
    this.addConverter(new CharacterToIntegerDatatypeConverter(), false);
    this.addConverter(new CharacterToLongDatatypeConverter(), false);
    this.addConverter(new CharacterToShortDatatypeConverter(), false);
    this.addConverter(new CharacterToStringDatatypeConverter(), false);
    this.addConverter(new DoubleToBigDecimalDatatypeConverter(), false);
    this.addConverter(new DoubleToFloatDatatypeConverter(), false);
    this.addConverter(new DoubleToStringDatatypeConverter(), false);
    this.addConverter(new FloatToBigDecimalDatatypeConverter(), false);
    this.addConverter(new FloatToDoubleDatatypeConverter(), false);
    this.addConverter(new FloatToStringDatatypeConverter(), false);
    this.addConverter(new IntegerToBigIntegerDatatypeConverter(), false);
    this.addConverter(new IntegerToCharacterDatatypeConverter(), false);
    this.addConverter(new LongToBigIntegerDatatypeConverter(), false);
    this.addConverter(new LongToCharacterDatatypeConverter(), false);
    this.addConverter(new LongToStringDatatypeConverter(), false);
    this.addConverter(new ShortToBigIntegerDatatypeConverter(), false);
    this.addConverter(new ShortToCharacterDatatypeConverter(), false);
    this.addConverter(new ShortToStringDatatypeConverter(), false);
    this.addConverter(new StringToBigDecimalDatatypeConverter(), false);
    this.addConverter(new StringToBigIntegerDatatypeConverter(), false);
    this.addConverter(new StringToBooleanDatatypeConverter(), false);
    this.addConverter(new StringToByteDatatypeConverter(), false);
    this.addConverter(new StringToCharacterDatatypeConverter(), false);
    this.addConverter(new StringToDoubleDatatypeConverter(), false);
    this.addConverter(new StringToFloatDatatypeConverter(), false);
    this.addConverter(new StringToIntegerDatatypeConverter(), false);
    this.addConverter(new StringToLongDatatypeConverter(), false);
    this.addConverter(new StringToShortDatatypeConverter(), false);
    this.addConverter(new StringToStringDatatypeConverter(), false);

    this.addConverter(new DateToStringDatatypeConverter(), false);
    this.addConverter(new StringToDateDatatypeConverter(), false);

    this.addConverter(new CalendarToStringDatatypeConverter(), false);
    this.addConverter(new StringToCalendarDatatypeConverter(), false);

    this.addConverter(new ClassToStringDatatypeConverter(), false);
    this.addConverter(new StringToClassDatatypeConverter(), false);

    this.addConverter(new CalendarToDateDatatypeConverter(), false);
    this.addConverter(new DateToCalendarDatatypeConverter(), false);

    this.addConverter(new URLToStringDatatypeConverter(), false);
    this.addConverter(new StringToURLDatatypeConverter(), false);

    this.addConverter(new LocaleToStringDatatypeConverter(), false);
    this.addConverter(new StringToLocaleDatatypeConverter(), false);
  }

  /**
   * Method loads the datatype converters that are configured in the JEAF.
   */
  @SuppressWarnings("rawtypes")
  private void loadAdditionalConverters( ) {
    // Get classes of all data type converters that are configured.
    ConfigurationReader lReader = new ConfigurationReader();
    List<Class<? extends DatatypeConverter>> lConverterClasses = lReader.readClassesFromConfigFile(
        DatatypeConverterImpl.DATATYPE_CONVERTERS_RESOURCE_NAME, XFun.X_FUN_BASE_PATH, DatatypeConverter.class);

    // Create converter instances and register them.
    for (Class<? extends DatatypeConverter> lNextClass : lConverterClasses) {
      DatatypeConverter<?, ?> lNewConverter;
      try {
        lNewConverter = lNextClass.newInstance();
      }
      catch (InstantiationException | IllegalAccessException e) {
        throw new JEAFBootstrapException("Unable to create datatype converter " + lNextClass.getName(), e);
      }
      this.addConverter(lNewConverter, true);
    }
  }
}