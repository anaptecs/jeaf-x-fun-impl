/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.impl.trace.formatter;

import com.anaptecs.jeaf.tools.api.date.DateTools;
import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.info.ApplicationInfo;
import com.anaptecs.jeaf.xfun.api.info.VersionInfo;
import com.anaptecs.jeaf.xfun.api.trace.ObjectFormatter;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

@TraceObjectFormatter(supportedClasses = ApplicationInfo.class)
public class ApplicationInfoFormatter implements ObjectFormatter<ApplicationInfo> {

  @Override
  public String formatObject( ApplicationInfo pApplicationInfo, TraceLevel pTraceLevel ) {
    VersionInfo lVersion = pApplicationInfo.getVersion();
    String lBuildDate = DateTools.getDateTools().toDateTimeString(lVersion.getCreationDate());
    return XFun.getMessageRepository().getMessage(XFunMessages.APPLICATION_VERSION_INFO, pApplicationInfo.getName(),
        lVersion.toString(), lBuildDate);
  }
}
