
package com.words;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

public class TopTest {

	@Test
	public void testWords() {
		assertEquals(Top.getWords("test all\nwords  here;"),
				Arrays.asList(new String[] { "test", "all", "words", "here" }));

	}

	@Test
	public void testOccurrences() {
		Map occurances = Top.getOccurrences("test all\nwords  here; and here test all\\nwords  here; and here",
				Arrays.asList(new String[] { "test", "all", "words", "here" }));
		assertEquals(occurances.get(4), "here");
		assertEquals(occurances.get(2), "test,words");

	}

	@Test
	public void testNTopOccurancess() {
		Pair<String, List<Entry>> topN = Top.getNTopOccurances("21721040", 5);
		assertEquals(topN.getKey(), "Stack Overflow");
		assertEquals(StringUtils.join(topN.getValue(), ","),
				"29=question,20=Stack,19=questions,18=site,17=Overflow,answer");
	}
}