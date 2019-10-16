package com.opentext.otmm.sc.modules.profanitydetector;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class ProfanityDetectorTest {

	@Test
	void testFindSwearwords() {
		ProfanityDetector detector = ProfanityDetector.getInstance();

		
		List<String> words = detector.findSwearwords("Hola, buenos días.");
		assertNull(words);
				
		words = detector.findSwearwords("Eres un hijo de puta. Pedazo de cabrón");
		assertNotNull(words);
		assertEquals(3, words.size());
		assertEquals("hijo de puta", words.get(0));
		assertEquals("puta", words.get(1));
		assertEquals("cabrón", words.get(2).toLowerCase());
		
		words = detector.findSwearwords("Calla, marica. ¿Qué es eso de 'hermano', capullo? ¿Vas de negrata, imbecil? Estos no quieren ser tus hermanos, y no me extraña. Vete ya con esa cara de irlandes lechoso a otra parte");
		assertNotNull(words);
		assertEquals(4, words.size());
		assertEquals("marica", words.get(0));
		assertEquals("capullo", words.get(1));
		assertEquals("negrata", words.get(2));	
		assertEquals("imbecil", words.get(3));	
	}

}
