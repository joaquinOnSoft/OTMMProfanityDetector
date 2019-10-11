/*
 * (C) Copyright 2019 Joaquín Garzón (http://opentext.com) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *   Joaquín Garzón - initial implementation
 */
package com.opentext.otmm.sc.evenlistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.artesia.common.exception.BaseTeamsException;
import com.artesia.event.services.EventServices;
import com.artesia.security.SecuritySession;
import com.opentext.otmm.sc.evenlistener.util.EventListenerUtils;

public class AnalysisDataFromAzureIsDeletedEventListenerRegistration implements ServletContextListener {

	private static final Log log = LogFactory.getLog(AnalysisDataFromAzureIsDeletedEventListenerRegistration.class);
	
	private static final String EVENT_CLIENT_ID = "Profanity-Detection";
	
	private static final String USER_ALIAS_TSUPER = "tsuper";
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log.info(">>> " + getClassName() + " >> contextInitialized() Start >>>");
		
		try {		
			SecuritySession session = com.opentext.otmm.sc.evenlistener.util.EventListenerUtils.getLocalSession(USER_ALIAS_TSUPER);
			AnalysisDataFromAzureIsDeletedEventListener ybsEventListener = new AnalysisDataFromAzureIsDeletedEventListener(OTMMEvent.ANALYSIS_DATA_FROM_AZURE_IS_DELETED);
			EventServices.getInstance().addEventListener(EVENT_CLIENT_ID, ybsEventListener, session);
			
			log.info("<<< " + getClassName() + " >> contextInitialized() End <<<");
		} catch (BaseTeamsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info(">>> " + getClassName() + " >> contextDestroyed() Start >>>");
		
		try {
			SecuritySession session = EventListenerUtils.getLocalSession("tsuper");
			EventServices.getInstance().removeEventListener(EVENT_CLIENT_ID, session);
		} catch (BaseTeamsException e) {
			log.error("An exception occured while destroying the servlet context", e);
		}
		
		log.info("<<< " + getClassName() + " >> contextDestroyed() End <<<");		
	}
	
	private String getClassName() {
		return getClass().getName();
	}

}
