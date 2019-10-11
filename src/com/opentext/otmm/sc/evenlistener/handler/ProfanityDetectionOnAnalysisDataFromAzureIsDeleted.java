/*
 * (C) Copyright 2019 Joaqu�n Garz�n (http://opentext.com) and others.
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
 *   Joaqu�n Garz�n - initial implementation
 */
package com.opentext.otmm.sc.evenlistener.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.artesia.asset.AssetIdentifier;
import com.artesia.asset.metadata.services.AssetMetadataServices;
import com.artesia.common.exception.BaseTeamsException;
import com.artesia.entity.TeamsIdentifier;
import com.artesia.event.Event;
import com.artesia.metadata.MetadataCollection;
import com.artesia.metadata.MetadataTableField;
import com.artesia.security.SecuritySession;
import com.opentext.otmm.sc.evenlistener.OTMMField;
import com.opentext.otmm.sc.evenlistener.helper.SecurityHelper;

/**
 * @see com.artesia.examples.client.AssetMetadataExample from Media_Management_Programmer_Guide_16.5 
 **/
public class ProfanityDetectionOnAnalysisDataFromAzureIsDeleted implements OTMMEventHandler {

	private static final Log log = LogFactory.getLog(ProfanityDetectionOnAnalysisDataFromAzureIsDeleted.class);

	@Override
	public boolean handle(Event event) {
		boolean handled = false;
		
		AssetIdentifier assetId = new AssetIdentifier(event.getObjectId());

		// Retrieve tabular metadata fields for the asset
		TeamsIdentifier[] fieldIds = new TeamsIdentifier[] {
				new TeamsIdentifier(OTMMField.MEDIA_ANALYSIS_VIDEO_SPEECH_TEXT),
				new TeamsIdentifier(OTMMField.MEDIA_ANALYSIS_VIDEO_SPEECH_START_TIME)};

		SecuritySession session = SecurityHelper.getAdminSession();

		MetadataCollection assetMetadata = null;
		try {
			assetMetadata = AssetMetadataServices.getInstance().retrieveMetadataForAsset(assetId, fieldIds, session);
		} catch (BaseTeamsException e) {
			log.error("Error retrieving metadata", e);
		}

		if(assetMetadata != null) {
			MetadataTableField textField = (MetadataTableField) assetMetadata.findElementByName(OTMMField.MEDIA_ANALYSIS_VIDEO_SPEECH_TEXT);
			MetadataTableField startTimeField = (MetadataTableField) assetMetadata.findElementByName(OTMMField.MEDIA_ANALYSIS_VIDEO_SPEECH_START_TIME);

			int rows = textField.getRowCount();

			log.info(rows + " transcriptions found");

			if (rows > 0) {

				for (int i = 0; i < rows; i++)
				{					
					log.debug("[" + i + "] TXT: " + textField.getValueAt(i).getIntValue());
					log.debug("[" + i + "] START TIME: " + startTimeField.getValueAt(i).getIntValue());
					
					
				}
				
				//The event has been properly handled.
				handled = true;
			}   
		}

		return handled;
	}

}
