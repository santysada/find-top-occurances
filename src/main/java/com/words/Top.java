package com.words;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.jayway.jsonpath.JsonPath;

public class Top {

	final static Logger logger = Logger.getLogger(Top.class);

	public static void main(String[] args) throws ClientProtocolException, IOException {
		String page_id;
		int top;
		try {
			page_id = args[0];
			top = Integer.valueOf(args[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.warn("No arguments provided");
			return;

		} catch (NumberFormatException e) {
			logger.warn("Provide valid integer value as second argument");
			return;
		}

		logger.info("PAGE_ID: " + page_id);
		logger.info("TOP: " + top);

		Pair<String, List<Entry>> topN = getNTopOccurances(page_id, top);
		System.out.println("Title: " + topN.getKey() + "\nTop " + top + " words:");
		topN.getValue().forEach(s -> System.out.println("- " + s.getKey() + " " + s.getValue()));

	}

	public static Pair<String, List<Entry>> getNTopOccurances(String page_id, int top) {
		// Get request
		HttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&pageids=" + page_id
				+ "&explaintext&format=json");
		String response;
		try {
			response = IOUtils.toString(httpclient.execute(httpget).getEntity().getContent(), "UTF-8");
		} catch (UnsupportedOperationException | IOException e) {
			logger.warn(
					"Page with given id may not exist or the url is not reachable at the moment, please check stack trace for more info");
			e.printStackTrace();
			return null;
		}

		// Extract title & content from json using jsonpath
		String title = JsonPath.read(response, "$..title").toString();
		String content = JsonPath.read(response, "$..extract").toString();

		// Split the content into words
		List<String> words = getWords(content);
		if (words.isEmpty()) {
			logger.warn("No content was extracted for given page_id:" + page_id);
			return null;
		}

		// Find word occurrences
		Map<Integer, String> occurrences = getOccurrences(response, words);
		return Pair.of(getWords(title).stream().collect(Collectors.joining(" ")),
				occurrences.entrySet().stream().limit(Integer.valueOf(top)).collect(Collectors.toList()));

	}

	public static List<String> getWords(String text) {
		List<String> words = new ArrayList<String>();
		BreakIterator breakIterator = BreakIterator.getWordInstance();
		breakIterator.setText(text);
		int lastIndex = breakIterator.first();
		while (BreakIterator.DONE != lastIndex) {
			int firstIndex = lastIndex;
			lastIndex = breakIterator.next();
			if (lastIndex != BreakIterator.DONE && Character.isLetter(text.charAt(firstIndex))) {
				words.add(text.substring(firstIndex, lastIndex));
			}
		}

		return words;
	}

	public static Map<Integer, String> getOccurrences(String content, List<String> words) {
		SortedMap<Integer, String> map = new TreeMap<Integer, String>(Collections.reverseOrder());
		words.stream().distinct().filter(word -> word.length() > 3).forEach(word -> {
			int count = StringUtils.countMatches(content, word);
			if (map.containsKey(count))
				map.put(count, map.get(count) + "," + word);
			else
				map.put(count, word);
		});
		return map;
	}
}
