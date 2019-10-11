package com.opentext.otmm.sc.modules.profanitydetector;

import java.util.Arrays;

public class Swearword {
	private String word;
	private String normalized;
	private String[] ignoreInCombinationWithWords = new String[] {};
	
	public Swearword(String word, String[] ignoreInCombinationWithWords) {
		setWord(word);
		this.ignoreInCombinationWithWords = ignoreInCombinationWithWords;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
		this.normalized = normalize(word);
	}

	public String[] getIgnoreInCombinationWithWords() {
		return ignoreInCombinationWithWords;
	}

	public void setIgnoreInCombinationWithWords(String[] ignoreInCombinationWithWords) {
		this.ignoreInCombinationWithWords = ignoreInCombinationWithWords;
	}

	private String normalize(String txt) {
		return txt == null ? null : txt.replaceAll(" ", "").toLowerCase();
	}	
	
	public String normalize() {
		return normalized;
	}

	@Override
	public String toString() {
		return "Swearword [word=" + word + ", ignoreInCombinationWithWords="
				+ Arrays.toString(ignoreInCombinationWithWords) + "]";
	}
	
	
}
