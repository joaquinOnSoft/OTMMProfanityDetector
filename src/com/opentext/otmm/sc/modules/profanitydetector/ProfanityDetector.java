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
package com.opentext.otmm.sc.modules.profanitydetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Profanity detection
 * 
 * <strong>NOTE</strong>: Code based on 
 * <a href="https://github.com/souwoxi/Profanity">souwoxi/Profanity</a> project, available in github.
 * 
 * <strong>NOTE</strong>: Spanish list of bad words based on information published
 * <a href="https://listas.20minutos.es/lista/insultos-en-castellano-que-deberias-conocer-y-su-significado-393340/">Insultos en castellano que deberías conocer y su significado</a> 
 */
public class ProfanityDetector {
	private static final Log log = LogFactory.getLog(ProfanityDetector.class);

	
	private static int largestWordLength = 0;

	private static Map<String, Swearword> allBadWords = new HashMap<String, Swearword>();

	private static ProfanityDetector instance = null;
	
	private ProfanityDetector() {
		loadBadWords();
		log.debug(allBadWords);
	}
	
	public static ProfanityDetector getInstance() {
		if(instance == null) {
			instance = new ProfanityDetector();
		}
		
		return instance;
	}
	
	/**
	 * Iterates over a String input and checks whether any cuss word was found - and for any/all cuss word found, 
	 * as long as the cuss word should not be ignored (i.e. check for false positives - e.g. even though "bass" 
	 * contains the word *ss, bass should not be censored) then (in the String returned) replace the cuss word with asterisks.
	 */
	public List<String> findSwearwords(final String input) {
		
		if (input == null) {
			return null;
		}

		String modifiedInput = input;

		// remove leetspeak
		modifiedInput = modifiedInput.replaceAll("1", "i").replaceAll("!", "i").replaceAll("3", "e").replaceAll("4", "a")
				.replaceAll("@", "a").replaceAll("5", "s").replaceAll("7", "t").replaceAll("0", "o").replaceAll("9", "g");

		// ignore any character that is not a letter
		modifiedInput = modifiedInput.toLowerCase().replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]", "");

		log.debug("Modified input: " + modifiedInput);
		
		List<String> badWordsFound = new ArrayList<String>();

		boolean ignore = false;
		int size = 0;
		// iterate over each letter in the word
		for (int start = 0; start < modifiedInput.length(); start++) {
			// from each letter, keep going to find bad words until either the end of
			// the sentence is reached, or the max word length is reached.
			for (int offset = 1; offset < (modifiedInput.length() + 1 - start) && offset < largestWordLength; offset++) {
				String wordToCheck = modifiedInput.substring(start, start + offset);
				
				log.debug("Word to check: " + wordToCheck + " " + toASCII(wordToCheck));
				
				if (allBadWords.containsKey(wordToCheck)) {
					Swearword swearwod = allBadWords.get(wordToCheck);
					ignore = false;
					size = swearwod.getIgnoreInCombinationWithWords().length;
					for (int stringIndex = 0; stringIndex < size; stringIndex++) {
						if (modifiedInput.contains(swearwod.getIgnoreInCombinationWithWords()[stringIndex])) {
							ignore = true;
							break;
						}
					}

					if (!ignore) {
						badWordsFound.add(swearwod.getWord());
					}
				}
			}
		}

		return badWordsFound.size() > 0 ? badWordsFound :  null;
	}
	

	private void loadBadWords() {
		int readCounter = 0;
		BufferedReader reader = null;
		
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream input = classLoader.getResourceAsStream("bad-words-es.csv");
			
			reader = new BufferedReader(new InputStreamReader(input));
			

			String currentLine = "";
			while ((currentLine = reader.readLine()) != null) {
				readCounter++;
				String[] content = null;
				
					if (1 == readCounter) {
						continue;
					}

					content = currentLine.split(",");
					if (content.length == 0) {
						continue;
					}

					final String word = content[0];

					if (word.startsWith("-----")) {
						continue;
					}

					if (word.length() > largestWordLength) {
						largestWordLength = word.length();
					}

					String[] ignoreInCombinationWithWords = new String[] {};
					if (content.length > 1) {
						ignoreInCombinationWithWords = content[1].split("_");
					}

					// Make sure there are no capital letters in the spreadsheet
					Swearword swearword = new Swearword(word, ignoreInCombinationWithWords);
					allBadWords.put(swearword.normalize(), swearword);
			
			} // end while
			
			reader.close();
		} catch (IOException e) {
			log.error("Error loading bad words list", e);
		}
	} // end loadBadWords
	
	private String toASCII(String txt) {
		StringBuilder str = new StringBuilder();
		
		if(txt != null) {
			int size = txt.length();
			for(int i=0; i<size; i++) {
				str.append("[")
					.append((int) txt.charAt(i))
					.append("]");
			}			
		}
		
		return str.toString();
	}
}
