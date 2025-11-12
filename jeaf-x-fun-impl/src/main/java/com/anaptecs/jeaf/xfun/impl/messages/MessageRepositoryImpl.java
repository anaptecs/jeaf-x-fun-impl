/*
 * anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 * 
 * Copyright 2004 - 2013 All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.messages;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.anaptecs.jeaf.tools.api.Tools;
import com.anaptecs.jeaf.xfun.annotations.MessageResource;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedObject;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import com.anaptecs.jeaf.xfun.api.messages.MessageDataDTD;
import com.anaptecs.jeaf.xfun.api.messages.MessageDefinition;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceConfiguration;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

/**
 * Class implements a repository that contains all available messages. To make sure that only one instance of this class
 * exists, the class is realized as singleton.
 * 
 * @author JEAF Development Team
 * @version 1.0
 */
public final class MessageRepositoryImpl implements MessageRepository {
  /**
   * Default serial version uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * System's line separator.
   */
  private static final String LINE_SEPERATOR = System.getProperty("line.separator");

  /**
   * Exceptions with this error code are thrown if somebody tries to load a message resource the second time.
   */
  public static final ErrorCode RESOURCE_ALREADY_LOADED;

  /**
   * Exceptions with this error code are thrown if somebody tries to create an ErrorCode object with an error code that
   * was used before.
   */
  public static final ErrorCode LOCALIZATION_ID_ALREADY_IN_USE;

  /**
   * Exceptions with this error code are thrown if during the load process of a message resource an exception occurs.
   */
  public static final ErrorCode UNABLE_TO_PARSE_XML_FILE;

  /**
   * Exceptions with this error code are thrown if somebody tries to add a message to the message repository and the
   * used ErrorCode object was already used to store a MessageFormat object.
   */
  public static final ErrorCode MESSAGE_ID_ALREADY_USED_TO_IDENTIFY_MESSAGE;

  public static final ErrorCode VERSION_DETAIL_NOT_DEFINED;

  public static final ErrorCode INVALID_VERSION_NUMBER_FORMAT;

  public static final ErrorCode INVALID_CREATION_DATE_FORMAT;

  public static final ErrorCode INVALID_MESSAGE_FORMAT;

  /**
   * Exceptions with this error code are thrown if somebody tries to create a new MessageID object with an unknown
   * message code. This usually happens if the corresponding message resource was not loaded yet.
   */
  public static final ErrorCode UNKNOWN_MESSAGE_CODE;

  /**
   * Constant for name of resource that contains the messages that are used by this class.
   */
  private static final String MESSAGE_RESOURCE = "BasicMessages.xml";

  /**
   * Static initializer is used to initialize ErrorCode objects that are used within this class, since these constants
   * can not be defined in a special class with constant definitions in order to avoid circular loops.
   */
  static {
    try {
      // Load resource file that contains the messages of this class.
      Collection<Element> lMessageElements = MessageRepositoryImpl.internalLoadResource(MESSAGE_RESOURCE);

      // Create only instance of the message repository with the loaded elements as initial message content.
      MessageRepositoryImpl lMessageRepository = new MessageRepositoryImpl(lMessageElements);
      INSTANCE = lMessageRepository;

      // Create ErrorCode objects for constants defined in this class.
      RESOURCE_ALREADY_LOADED = lMessageRepository.getErrorCode(5);
      LOCALIZATION_ID_ALREADY_IN_USE = lMessageRepository.getErrorCode(6);
      UNABLE_TO_PARSE_XML_FILE = lMessageRepository.getErrorCode(7);
      MESSAGE_ID_ALREADY_USED_TO_IDENTIFY_MESSAGE = lMessageRepository.getErrorCode(8);
      UNKNOWN_MESSAGE_CODE = lMessageRepository.getErrorCode(9);
      VERSION_DETAIL_NOT_DEFINED = lMessageRepository.getErrorCode(10);
      INVALID_VERSION_NUMBER_FORMAT = lMessageRepository.getErrorCode(11);
      INVALID_CREATION_DATE_FORMAT = lMessageRepository.getErrorCode(12);
      INVALID_MESSAGE_FORMAT = lMessageRepository.getErrorCode(13);

      // Load all other message resources from classpath.
      lMessageRepository.loadResourcesFromClasspath();
    }
    catch (IOException e) {
      throw new RuntimeException(e.getLocalizedMessage(), e);
    }
  }

  /**
   * Map contains all message IDs that are loaded within this message repository.
   */
  private final Map<Integer, LocalizedObject> localizationIDs;

  /**
   * Map contains all default messages that were added to the repository. Within the map the MessageID object is used as
   * key and a MessageFormat object as value. Messages are taken from this map whenever no localized message for a
   * specific locale and the default locale can be found.
   */
  private final Map<LocalizedObject, MessageFormat> defaultMessages;

  /**
   * Map contains maps that contain localized messages that were added to the repository. The map contains a map for
   * every supported locale. Within the map a Locale object is used as key and a Map object as value.
   */
  private final Map<Locale, Map<LocalizedObject, MessageFormat>> localizedMessageMaps;

  /**
   * Set contains the names of all resource that have already been loaded, in order to avoid loading a message resource
   * twice.
   */
  private final Set<String> loadedResources;

  /**
   * Set contains all localization IDs that were already used to create a localized object. All localization IDs are
   * stored within this object as instances of java.lang.Integer.
   */
  private final Set<Integer> usedLocalizationIDs = new HashSet<>();

  /**
   * In order to avoid useless requests to JEAF properties we cache the trace message format.
   */
  private final String traceMessageFormat;

  /**
   * Attribute defines if name of the current user should be shown in traces or not.
   */
  private boolean showCurrentUserInTraces;

  /**
   * In order to avoid useless requests to JEAF properties we cache the trace locale.
   */
  private Locale traceLocale;

  /**
   * Reference to only instance of this class.
   */
  private static final MessageRepositoryImpl INSTANCE;

  /**
   * Initialize MessageRepositoryImpl. The constructor is private to realize the singleton pattern.
   * 
   * @param pInitialMessageElements Collection containing all DOM elements that are used initial message content of the
   * repository. The parameter must not be null.
   */
  public MessageRepositoryImpl( Collection<Element> pInitialMessageElements ) {
    // Check parameter.
    Assert.assertNotNull(pInitialMessageElements, "pInitialMessageElements");

    // Initialize attributes.
    localizationIDs = new HashMap<>();
    defaultMessages = new HashMap<>();
    localizedMessageMaps = new HashMap<>();
    loadedResources = new HashSet<>();
    TraceConfiguration lTraceConfiguration = TraceConfiguration.getInstance();
    traceMessageFormat = lTraceConfiguration.getTraceMessageFormat();
    showCurrentUserInTraces = lTraceConfiguration.showCurrentUserInTraces();
    traceLocale = this.resolveTraceLocale();

    // Add initial message content.
    this.addMessageElements(pInitialMessageElements);
  }

  /**
   * Method returns only instance of this class.
   * 
   * @return MessageRepositoryImpl Only instance of this class. The method never returns null.
   */
  public static MessageRepositoryImpl getInstance( ) {
    return INSTANCE;
  }

  /**
   * Method loads the message data into the repository that is contained in the resource file with the passed name.
   * 
   * @param pMessageResource Name of the resource file that should be loaded. The parameter must point to a file
   * containing the message data. The parameter must not be null. A message resource must not be loaded twice.
   * @throws SystemException if the message resource pMessageResource was already loaded or an error occurs during the
   * parsing process of the message resource.
   */
  public void loadResource( String pMessageResource ) throws SystemException {
    // Check pMessageResource for null.
    Check.checkInvalidParameterNull(pMessageResource, "pMessageResource");

    // Check if resource is already loaded.
    Trace lTrace = XFun.getTrace();
    lTrace.debug("Loading message resource " + pMessageResource);
    if (loadedResources.contains(pMessageResource) == false) {
      // Parse XML file containing the messages.
      try {
        // Call internal method to load message resource and
        Collection<Element> lReadMessageElements = MessageRepositoryImpl.internalLoadResource(pMessageResource);
        this.addMessageElements(lReadMessageElements);

        // Mark resource as loaded.
        loadedResources.add(pMessageResource);
      }
      // No XML parser available.
      // Error during file access.
      catch (IOException e) {
        lTrace.error(UNABLE_TO_PARSE_XML_FILE, e, pMessageResource);
        throw new JEAFSystemException(UNABLE_TO_PARSE_XML_FILE, e, pMessageResource);
      }
    }
    // Resource has already been loaded. This can happen e.g. in JEE environments were the messages are transfered from
    // the server to the client. Since this is an ordinary case nothing has to happen exception for ignoring the load
    // command.
    else {
      // Nothing to do.
    }
  }

  /**
   * Method loads all message resources from classpath. It therefore checks for annotation {@link MessageResource}
   */
  private void loadResourcesFromClasspath( ) {
    // Resolve additional message resources that were configured. By accessing these classes they will be loaded.
    XFun.getConfiguration().getMessageResourceClasses();
  }

  /**
   * Method loads the message resource with the passed resource name. Since this is the internal part of the message
   * loading process no exception handling is done within this method. Exceptions have to be handled by the caller.
   * 
   * @param pMessageResource Name of the resource file that should be loaded. The parameter must point to a file
   * containing the message data. The parameter must not be null. A message resource must not be loaded twice.
   * @return Collection All DOM elements that contain information about a message. The method never returns null.
   * @throws ParserConfigurationException if no XML parser is available.
   * @throws SAXException if an error occurs during the parsing process.
   * @throws IOException if an error occurs during the file access.
   */
  private static Collection<Element> internalLoadResource( String pMessageResource ) throws IOException {
    // Load resource with the passed name from application class path.
    ClassLoader lClassLoader = MessageRepositoryImpl.class.getClassLoader();
    InputStream lResourceStream = lClassLoader.getResourceAsStream(pMessageResource);

    // Check if passed resource could be loaded.
    if (lResourceStream != null) {
      // Load passed resource as XML file.
      Document lDocument =
          Tools.getXMLTools().parseInputStream(lResourceStream, true, MessageDataDTD.SYSTEM_ID, pMessageResource);

      // Get all elements with name MESSAGE and add them to the repository.
      NodeList lMessageNodeList = lDocument.getElementsByTagName(MessageDataDTD.MESSAGE);
      Assert.assertNotNull(lMessageNodeList, "lMessageNodeList");

      Collection<Element> lMessageElements = new ArrayList<>(lMessageNodeList.getLength());
      for (int i = 0; i < lMessageNodeList.getLength(); i++) {
        // Get next element and add it to the repository.
        Element lElement = (Element) lMessageNodeList.item(i);
        lMessageElements.add(lElement);
      }
      // Return Collection with message elements.
      return lMessageElements;
    }
    // Resource could not be found within the application class path.
    else {
      String lMessage = "Resource '" + pMessageResource + "' could not be found within the application class path.";
      throw new IOException(lMessage);
    }
  }

  /**
   * Method adds all passed DOM elements as message elements to the repository.
   * 
   * @param pMessageElements Collection with all message elements that should be added to the message repository. The
   * parameter must not be null.
   */
  private void addMessageElements( Collection<Element> pMessageElements ) {
    // Add all DOM elements as message elements.
    Iterator<Element> lIterator = pMessageElements.iterator();
    while (lIterator.hasNext()) {
      // Get next message element and add it to repository.
      Element lMessageElement = lIterator.next();
      LocalizedObject lLocalizedObject = this.createLocalizedObject(lMessageElement);
      localizationIDs.put(lLocalizedObject.getLocalizationID(), lLocalizedObject);
    }
  }

  /**
   * Method adds the passed MessageFormat object to the list of available messages for the passed message id and locale.
   * 
   * @param pLocalizedObject Message id that is used to identify the passed MessageFormat object. The parameter must not
   * be null and no other MessageFormat object must be configured using pMessageID as message id and pLocale as locale.
   * @param pMessageFormat MessageFormat object that is used to create parameterized and localized messages. The
   * parameter must not be null.
   * @param pLocale Locale for which the passed message should be used. The parameter must not be null.
   * @throws SystemException if a MessageFormat object for the passed message id and locale is already available.
   */
  private void addLocalizedMessage( LocalizedObject pLocalizedObject, MessageFormat pMessageFormat, Locale pLocale )
    throws SystemException {
    // Check parameters for null.
    Assert.assertNotNull(pLocalizedObject, "pMessageID");
    Assert.assertNotNull(pMessageFormat, "pMessageFormat");
    Assert.assertNotNull(pLocale, "pLocale");

    // Get get message map for used locale. If no appropriate map is created
    // yet then create it now.
    Map<LocalizedObject, MessageFormat> lLocalizedMap =
        localizedMessageMaps.computeIfAbsent(pLocale, f -> new HashMap<>());

    // Check if message id is already used for this locale.
    if (lLocalizedMap.containsKey(pLocalizedObject) == false) {
      // Add message id and MessageFormat object.
      lLocalizedMap.put(pLocalizedObject, pMessageFormat);
    }
    // Message id must not be used more than once per locale.
    else {
      String[] lParams = new String[] { Integer.toString(pLocalizedObject.getLocalizationID()) };
      throw new JEAFSystemException(MESSAGE_ID_ALREADY_USED_TO_IDENTIFY_MESSAGE, lParams);
    }
  }

  /**
   * Returns the TraceLevel of a MessageID.
   * 
   * @param pTraceLevel contains the String of the TraceLevel which will be converted to a enumeration value. The passed
   * string must match with one of the names of enumeration TraceLevel. The parameter must not be null.
   * @return The method returns a enumeration TraceLevel object which can be [DEBUG, FATAL, ERROR, WARN, INFO, TRACE].
   * The method never returns null. If the parameter is an empty string then the method returns the default value ERROR.
   */
  private TraceLevel getTraceLevel( String pTraceLevel ) {
    // Check parameter.
    Assert.assertNotNull(pTraceLevel, "pTraceLevel");

    // Parameter may be null.
    TraceLevel lTraceLevel;
    if (pTraceLevel.trim().length() > 0) {
      lTraceLevel = TraceLevel.valueOf(pTraceLevel);
    }
    // Value not set within the XML file thus we are using the default trace level ERROR.
    else {
      lTraceLevel = TraceLevel.ERROR;
    }
    return lTraceLevel;
  }

  /**
   * Method returns the LocalizedObject for the passed localization ID.
   * 
   * @param pLocalizationID Localization ID of the object that should be returned.
   * @return {@link LocalizedObject} LocalizedObject for the passed localization ID. The method never returns null.
   * @throws SystemException if no LocalizedObject exists for the passed localization ID.
   */
  public LocalizedObject getLocalizedObject( int pLocalizationID ) throws SystemException {
    final LocalizedObject lLocalizedObject = localizationIDs.get(pLocalizationID);
    if (lLocalizedObject != null) {
      return lLocalizedObject;
    }
    // No LocalizedObject for the passed localization ID available. This means that probably the required resource was
    // not yet loaded -> SystemException
    else {
      throw new JEAFSystemException(UNKNOWN_MESSAGE_CODE, Integer.toString(pLocalizationID));
    }
  }

  /**
   * Method returns the MessageID object for the passed message code.
   * 
   * @param pMessageCode Message code whose message id should be returned.
   * @return {@link MessageID} MessageID object for the passed message code. The method never returns null.
   * @throws SystemException if no MessageID object exists for the passed message code.
   */
  public MessageID getMessageID( int pMessageCode ) throws SystemException {
    return (MessageID) this.getLocalizedObject(pMessageCode);
  }

  /**
   * Method checks if a message with the passed message code is known inside inside the message repository.
   * 
   * @param pMessageCode Message code that should be checked.
   * @return boolean Method returns true if a message with the passed message code is known and false otherwise.
   */
  @Override
  public boolean existsMessage( int pMessageCode ) {
    return localizationIDs.containsKey(pMessageCode);
  }

  /**
   * Method returns the ErrorCode object for the passed error code.
   * 
   * @param pErrorCode Error code whose ErrorCode object should be returned.
   * @return {@link ErrorCode} ErrorCode object for the passed error code. The method never returns null.
   * @throws SystemException if no ErrorCode object exists for the passed error code.
   */
  public ErrorCode getErrorCode( int pErrorCode ) throws SystemException {
    return (ErrorCode) this.getLocalizedObject(pErrorCode);
  }

  /**
   * Method returns the LocalizedObject for the passed localization ID.
   * 
   * @param pLocalizationID Localization ID of the object that should be returned.
   * @return {@link LocalizedObject} LocalizedObject for the passed localization ID. The method never returns null.
   * @throws SystemException if no LocalizedObject exists for the passed localization ID.
   */
  public LocalizedString getLocalizedString( int pLocalizationID ) throws SystemException {
    return (LocalizedString) this.getLocalizedObject(pLocalizationID);
  }

  /**
   * Method creates new message from the passed DOM element.
   * 
   * @param pMessageElement DOM element describing the message object that should be created. The parameter must not be
   * null.
   * @return MessageID ID of the created message object. The method never returns null.
   * @throws SystemException if a message with the same message code was already created.
   */
  private LocalizedObject createLocalizedObject( Element pMessageElement ) throws SystemException {
    // The passed DOM element contains all information that are required to create an MessageFormat object that can be
    // used to create an message.
    String lMessageCodeString = pMessageElement.getAttribute(MessageDataDTD.MESSAGE_ID);
    Integer lLocalizationID = Integer.valueOf(lMessageCodeString);

    // Check if localization id is already in use.
    if (this.isLocalizationIDUsed(lLocalizationID) == false) {
      // Get value of attribute MESSAGE_DEFAULT_TEXT which is used as message pattern for the MessageFormat object.
      MessageFormat lMessageFormat;
      String lMessagePattern = null;
      try {
        lMessagePattern = pMessageElement.getAttribute(MessageDataDTD.MESSAGE_DEFAULT_TEXT);
        lMessagePattern = lMessagePattern.replace("\\\\n", LINE_SEPERATOR);
        lMessageFormat = new MessageFormat(lMessagePattern);
      }
      catch (IllegalArgumentException e) {
        throw new JEAFSystemException(INVALID_MESSAGE_FORMAT, e, lLocalizationID.toString(), lMessagePattern);
      }

      // Get trace level from XML element and convert it the used enumeration.
      String lTraceLevelString = pMessageElement.getAttribute(MessageDataDTD.MESSAGE_TRACE_LEVEL);
      TraceLevel lTraceLevel = this.getTraceLevel(lTraceLevelString);

      // Get type of localized object.
      String lType = pMessageElement.getAttribute(MessageDataDTD.TYPE);

      // Create new LocalizedObject subclass depending on the defined type.
      LocalizedObject lLocalizedObject;
      if (MessageDataDTD.TYPE_INFO.equals(lType)) {
        // Create new MessageID with the passed message code. If the message code was already used to create an
        // MessageID
        // object a SystemException will be thrown.
        lLocalizedObject = new MessageID(lLocalizationID, lTraceLevel);
      }
      else if (MessageDataDTD.TYPE_ERROR.equals(lType)) {
        // Create new ErrorCode.
        lLocalizedObject = new ErrorCode(lLocalizationID, lTraceLevel);
      }
      else {
        lLocalizedObject = new LocalizedString(lLocalizationID);
      }

      // Add default message to repository.
      defaultMessages.put(lLocalizedObject, lMessageFormat);

      // Get all localized message texts.
      NodeList lLocalizedMessageElements = pMessageElement.getElementsByTagName(MessageDataDTD.LOCALIZED_MESSAGE);
      for (int i = 0; i < lLocalizedMessageElements.getLength(); i++) {
        // Get next element and create localized message.
        Element lNextLocalizedMessageElement = (Element) lLocalizedMessageElements.item(i);

        // Create Locale object for defined locale.
        String lNextLanguage = lNextLocalizedMessageElement.getAttribute(MessageDataDTD.LANGUAGE);
        String lNextCountry = lNextLocalizedMessageElement.getAttribute(MessageDataDTD.COUNTRY);
        String lNextVariant = lNextLocalizedMessageElement.getAttribute(MessageDataDTD.VARIANT);
        Locale lNextLocale = new Locale(lNextLanguage, lNextCountry, lNextVariant);

        // Create message format object.
        String lNextMessagePattern = lNextLocalizedMessageElement.getAttribute(MessageDataDTD.LOCALIZED_TEXT);
        lNextMessagePattern = lNextMessagePattern.replace("\\n", LINE_SEPERATOR);
        MessageFormat lNextMessageFormat;
        try {
          lNextMessageFormat = new MessageFormat(lNextMessagePattern);
        }
        catch (IllegalArgumentException e) {
          throw new JEAFSystemException(INVALID_MESSAGE_FORMAT, e, lLocalizationID.toString(), lMessagePattern);
        }

        // Add localized message for current locale.
        this.addLocalizedMessage(lLocalizedObject, lNextMessageFormat, lNextLocale);
      }
      // Register localization ID and assign it to attribute localizationID
      usedLocalizationIDs.add(lLocalizationID);

      // Return created message id.
      return lLocalizedObject;
    }
    // Localization ID was already used -> SystemException
    else {
      XFun.getTrace().write(MessageRepositoryImpl.LOCALIZATION_ID_ALREADY_IN_USE, lLocalizationID.toString());
      throw new JEAFSystemException(MessageRepositoryImpl.LOCALIZATION_ID_ALREADY_IN_USE, lLocalizationID.toString());
    }
  }

  /**
   * Method returns a parameterized message for the passed message id. The method uses the current default locale to
   * localize the message text.
   * 
   * @param pLocalizedObject Localized object to identify the parameterized message that should be returned. The
   * parameter must not be null.
   * @param pMessageParameters Parameter values that should be used to create the parameterized message. If the message
   * does not need any parameters pMessageParameters may also be null.
   * @return String Message that was created using the passed message id and the message parameters and the current
   * default locale. The method never returns null.
   * @see #getMessage(MessageID, Locale, String[])
   */
  public String getMessage( LocalizedObject pLocalizedObject, String... pMessageParameters ) {
    return this.getMessage(pLocalizedObject, XFun.getLocaleProvider().getCurrentLocale(), pMessageParameters);
  }

  /**
   * Method returns a parameterized trace message for the passed message id. The difference between a "normal" message
   * and a trace message is that in case of the trace message the locale that is defined for tracing will be used. A
   * trace message locale is especially helpful for application with several languages.
   * 
   * @param pLocalizedObject Localized object to identify the parameterized message that should be returned. The
   * parameter must not be null.
   * @param pMessageParameters Parameter values that should be used to create the parameterized message. If the message
   * does not need any parameters pMessageParameters may also be null.
   * @return String Message that was created using the passed message id and the message parameters and the current
   * default locale. The method never returns null.
   * 
   * @see #getMessage(MessageID, Locale, String[])
   */
  public String getTraceMessage( LocalizedObject pLocalizedObject, String... pMessageParameters ) {
    // Get default message.
    String lMessage = this.getMessage(pLocalizedObject, traceLocale, pMessageParameters);

    // Determine current user if possible.
    String lCurrentUser;
    if (showCurrentUserInTraces == true) {
      Principal lCurrentPrincipal = XFun.getPrincipalProvider().getCurrentPrincipal();
      if (lCurrentPrincipal != null) {
        lCurrentUser = lCurrentPrincipal.getName();
      }
      else {
        lCurrentUser = " ";
      }
    }
    else {
      lCurrentUser = " ";
    }

    // The default message can be adapted by using an optional format string defined in JEAF configuration.
    return String.format(traceMessageFormat, pLocalizedObject.getLocalizationID(), lMessage, lCurrentUser);
  }

  /**
   * Method returns a parameterized and localized message for the passed message id. The method also returns a message
   * even though no localized message text for the passed locale is available. In this case the following algorithm is
   * used to find the closest message:
   * <ol>
   * <li>Find message for passed locale.</li>
   * <li>Find message for passed locale with reduced restrictions. Thereby the restrictions are widened from the variant
   * to the language.</li>
   * <li>Find message for current default locale.</li>
   * <li>Find message for current default locale with reduced restrictions. Thereby the restrictions are widened from
   * the variant to the language.</li>
   * <li>Use default message for passed message id. A default message is always available.</li>
   * </ol>
   * 
   * @param pLocalizedObject Localized object to identify the parameterized message that should be returned. The
   * parameter must not be null.
   * @param pLocale Information about the locale for which the message should be created. The parameter must not be
   * null.
   * @param pMessageParameters Parameter values that should be used to create the parameterized message. If the message
   * does not need any parameters pMessageParameters can also be null.
   * @return String Message that was created using the passed message id, message parameters and locale. The method
   * never returns null.
   */
  public String getMessage( LocalizedObject pLocalizedObject, Locale pLocale, String... pMessageParameters ) {
    // Check pMessageID and pLocale for null.
    Check.checkInvalidParameterNull(pLocalizedObject, "pLocalizedObject");
    Check.checkInvalidParameterNull(pLocale, "pLocale");

    // Get MessageFormat object for the passed message id and create message string using the passed parameters.
    String lMessage;
    MessageFormat lMessageFormat = this.getMessageFormat(pLocalizedObject, pLocale);
    if (lMessageFormat != null) {
      lMessage = lMessageFormat.format(pMessageParameters);
    }
    // No message could be found. In order to ease to location of an problem, we provide as much information as possible
    else {
      lMessage = "[ID-" + pLocalizedObject.getLocalizationID() + "] Unable to return real message. ID is unknown";
      RuntimeException lRuntimeException = new RuntimeException(lMessage);
      XFun.getTrace().error(lMessage, lRuntimeException);
    }
    return lMessage;
  }

  /**
   * Method returns the best fitting message format object for the passed localized object and locale. If there is no
   * message format object in the passed locale then the locale will be returned until a message format is found.
   * 
   * @param pLocalizedObject Localized object for which a message format object should be returned.
   * @param pLocale Locale of the returned message format object.
   * @return {@link MessageFormat} Message format object for the passed localized object. The method returns null if no
   * matching message format exists in any locale.
   */
  private MessageFormat getMessageFormat( LocalizedObject pLocalizedObject, Locale pLocale ) {
    // Get message for passed locale.
    MessageFormat lMessageFormat = null;

    // Try to find message map for the passed locale.
    Map<LocalizedObject, MessageFormat> lMessageMap = localizedMessageMaps.get(pLocale);
    if (lMessageMap != null) {
      lMessageFormat = lMessageMap.get(pLocalizedObject);
    }

    // If no message format object exits in the found message map or we did not find a appropriate message map than we
    // have to reduce the locale and try again.
    if (lMessageFormat == null) {
      // Reduce locale.
      Locale lReducedLocale = this.reduceLocale(pLocale);

      if (lReducedLocale != null) {
        // Make recursive call with reduced locale.
        lMessageFormat = this.getMessageFormat(pLocalizedObject, lReducedLocale);
      }
      // Locale can not be reduced any more thus we use the default message format object.
      else {
        lMessageFormat = defaultMessages.get(pLocalizedObject);
      }
    }

    // Return found message format object.
    return lMessageFormat;
  }

  /**
   * Method reduces the passed locale so that it get less specific. Reduction means that a locale "en_GB" is reduced to
   * "en". The method starts with removing the variant then the country and at last the language. Locales will be
   * reduced by one level.
   * 
   * @param pLocale Locale that should be reduced. The parameter must not be null.
   * @return {@link Locale} Result of the reduction of the locale. The method returns null if the passed locale only
   * consists of a language as it can not be reduced any more.
   */
  private Locale reduceLocale( Locale pLocale ) {
    // Check parameter.
    Assert.assertNotNull(pLocale, "pLocale");

    // Reduce variant if set.
    Locale lReducedLocale;
    if (pLocale.getVariant().length() > 0) {
      lReducedLocale = new Locale(pLocale.getLanguage(), pLocale.getCountry());
    }
    // Reduce country if set
    else if (pLocale.getCountry().length() > 0) {
      lReducedLocale = new Locale(pLocale.getLanguage());
    }
    // Locale only consists of a language. Thus we can not reduce it any more and return null.
    else {
      lReducedLocale = null;
    }
    return lReducedLocale;
  }

  /**
   * Method adds all messages of the passed message repository to this message repository. May be existing messages with
   * the same message id will be overwritten.
   * 
   * @param pMessages List with all message that should be added to the repository. The parameter must not be null.
   */
  @Override
  public void addAllMessages( List<MessageDefinition> pMessages ) {
    // Check parameter
    Check.checkInvalidParameterNull(pMessages, "pMessages");

    // Process all messages.
    for (MessageDefinition lNextMessage : pMessages) {
      // Add new message
      LocalizedObject lLocalizedObject = lNextMessage.getLocalizedObject();
      localizationIDs.put(lLocalizedObject.getLocalizationID(), lLocalizedObject);
      defaultMessages.put(lLocalizedObject, lNextMessage.getDefaultMessage());

      // Merge localizations of the message with existing ones.
      Set<Entry<Locale, MessageFormat>> lEntrySet = lNextMessage.getLocalizedMessages().entrySet();
      for (Entry<Locale, MessageFormat> lNextEntry : lEntrySet) {

        // Check if message map for the next locale exists.
        Locale lLocale = lNextEntry.getKey();
        Map<LocalizedObject, MessageFormat> lExistingMap = localizedMessageMaps.get(lLocale);
        if (lExistingMap != null) {
          // Merge existing messages with the new ones for the current locale
          lExistingMap.put(lLocalizedObject, lNextEntry.getValue());
        }
        // No message map exists thus we will use the new one.
        else {
          Map<LocalizedObject, MessageFormat> lNewMap = new HashMap<>();
          lNewMap.put(lLocalizedObject, lNextEntry.getValue());
          localizedMessageMaps.put(lLocale, lNewMap);
        }
      }
    }
  }

  /**
   * Method returns all messages of the repository.
   * 
   * @return {@link List} List with all messages of the repository. The method never returns null.
   */
  @Override
  public List<MessageDefinition> getAllMessages( ) {
    // Create new list for result.
    List<MessageDefinition> lAllMessage = new ArrayList<>(defaultMessages.size());

    // Process all messages.
    for (Entry<LocalizedObject, MessageFormat> lNextEntry : defaultMessages.entrySet()) {
      LocalizedObject lKey = lNextEntry.getKey();
      MessageFormat lDefaultMessage = lNextEntry.getValue();

      // Resolve all localize messages.
      Map<Locale, MessageFormat> lLocalizations = new HashMap<>();
      for (Entry<Locale, Map<LocalizedObject, MessageFormat>> lEntry : localizedMessageMaps.entrySet()) {
        // Resolve localized message
        Locale lLocale = lEntry.getKey();
        Map<LocalizedObject, MessageFormat> lValues = lEntry.getValue();
        MessageFormat lMessageFormat = lValues.get(lKey);

        if (lMessageFormat != null) {
          lLocalizations.put(lLocale, lMessageFormat);
        }
      }

      // Create new message
      MessageDefinition lMessage = new MessageDefinition(lKey, lDefaultMessage, lLocalizations);
      lAllMessage.add(lMessage);
    }

    // Return all messages
    return lAllMessage;
  }

  /**
   * Method returns the locale that is currently used for tracing.
   * 
   * @return {@link Locale} Local that is currently used for tracing. The method never returns null.
   */
  public Locale getTraceLocale( ) {
    return traceLocale;
  }

  /**
   * Method sets the locale that will be used for tracing.
   * 
   * @param pLocale Locale that should be used fro tracing. The parameter must not be null.
   */
  public void setTraceLocale( Locale pLocale ) {
    Check.checkInvalidParameterNull(pLocale, "pLocale");

    traceLocale = pLocale;
  }

  /**
   * Method returns if the current user is shown in trace. Due to data protection policies it's recommended that on
   * production environments this feature is turned off.
   * 
   * @return boolean Method returns true if the current user should be shown in traces and otherwise false.
   */
  public boolean showCurrentUserInTraces( ) {
    return showCurrentUserInTraces;
  }

  /**
   * Method controls feature about showing current user in traces.
   * 
   * @param pShowCurrentUser If the parameter is set to true then the current user will be shown in traces and otherwise
   * not.
   */
  public void setShowCurrentUserInTraces( boolean pShowCurrentUser ) {
    showCurrentUserInTraces = pShowCurrentUser;
  }

  /**
   * Method returns the locale that should be used for tracing.
   * 
   * @return {@link Locale} Locale that should be used for tracing. The method never returns null.
   */
  private Locale resolveTraceLocale( ) {
    Locale lTraceLocale;
    // Get tracing locale from JEAF properties.
    TraceConfiguration lTraceConfiguration = TraceConfiguration.getInstance();
    if (lTraceConfiguration.isTraceWithSystemLocaleEnabled() == true) {
      lTraceLocale = Locale.getDefault(Category.DISPLAY);
    }
    // Get custom locale for tracing.
    else {
      lTraceLocale = lTraceConfiguration.getCustomTraceLocale();
    }
    return lTraceLocale;
  }

  /**
   * Method checks whether the passed localization ID was already used to create a localized object.
   * 
   * @param pLocalizationID Integer value of the localization ID, for which this method should check if it is already
   * used.
   * @return boolean The method returns true if the localization ID is not used yet. Otherwise the method returns false.
   */
  private synchronized boolean isLocalizationIDUsed( Integer pLocalizationID ) {
    // Check if localization ID is already used.
    return usedLocalizationIDs.contains(pLocalizationID);
  }

}
