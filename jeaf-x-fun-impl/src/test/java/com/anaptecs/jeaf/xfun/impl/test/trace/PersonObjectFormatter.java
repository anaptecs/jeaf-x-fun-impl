/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.test.trace;

import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;
import com.anaptecs.jeaf.xfun.api.trace.ObjectFormatter;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

@TraceObjectFormatter(supportedClasses = Person.class)
public class PersonObjectFormatter implements ObjectFormatter<Person> {

  @Override
  public String formatObject( Person pPerson, TraceLevel pTraceLevel ) {
    return pTraceLevel.name() + " " + pPerson.name;
  }

}
