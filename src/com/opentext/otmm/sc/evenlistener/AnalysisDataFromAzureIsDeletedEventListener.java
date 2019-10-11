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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.artesia.entity.TeamsIdentifier;
import com.artesia.event.Event;
import com.artesia.event.EventListener;
import com.opentext.otmm.sc.evenlistener.handler.OTMMEventHandler;
import com.opentext.otmm.sc.evenlistener.handler.ProfanityDetectionOnAnalysisDataFromAzureIsDeleted;

public class AnalysisDataFromAzureIsDeletedEventListener implements EventListener {

	private String eventsToRegister = "";
	private static final Log log = LogFactory.getLog(AnalysisDataFromAzureIsDeletedEventListener.class);

	public AnalysisDataFromAzureIsDeletedEventListener() {
	}

	public AnalysisDataFromAzureIsDeletedEventListener(String events) {
		this.eventsToRegister = events;
		log.debug("Registering listener to events: " + this.eventsToRegister);		
	}


	private void displayEventObject(Event theEvent) {
		log.debug("== Event Init == ");
		log.debug("Event ID : " + theEvent.getEventId().getId());
		log.debug("user ID : " + theEvent.getUserId().getId());
		log.debug("Event Description : " + theEvent.getDescription());
		log.debug("Event XMLContent : " + theEvent.getXmlContent());
		log.debug("== Event End ==");
	}

	public void onEvent(Event event) {
		displayEventObject(event);
		
		if (event.getEventId().equals(new TeamsIdentifier(OTMMEvent.ANALYSIS_DATA_FROM_AZURE_IS_DELETED))) {
			log.info("Ids match for Reveiw Job Task completed event. Event ID: " + event.getObjectId());

			OTMMEventHandler handler = new ProfanityDetectionOnAnalysisDataFromAzureIsDeleted();
			handler.handle(event);
		}
	}

}
