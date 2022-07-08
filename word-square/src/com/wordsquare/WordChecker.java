/*
This is a word Square solver, takes input from user for letters and size of word square and returns valid word squares
*/


//How To Guide:
// Press run and have letters and size of word square at the ready. Enter when console prompts. Results will follow.


//Current bug - unable to get word square of size 5 to run, programme does grab all valid words but terminates itself after this stage. Can see words for split second but console wipes due to <terminated>


//Development process
//FIRST STEP - get all valid words through counting char from input and reading in words from URL
		
//SECOND STEP - create methods wordSquares(create a list of results once passed backTrack), backTrack(recursion through the prefixes to identify if words are valid in word square), prefixMap(tracks prefixes of words)

//THIRD STEP - convert wordArrayList to an Array of words. Call method wordSquares on words and print result.

package com.wordsquare;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



public class WordChecker {
	
	private static HashMap<String, List<String>> map = new HashMap<>();
	
	
	public static void main(String[] args) throws Exception {
		
		
		// Scanner for taking input from user of letters in the pool & length of word
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter letters: ");
		String letters = scanner.nextLine().toLowerCase();
		System.out.println("Enter Word Length: ");
		int wordLength = scanner.nextInt();
		
		
		Map<Character, Integer> lettersCountMap = getCharCountMap(letters);
		
		//stores all valid words in this ArrayList
		List<String> wordArrayList = new ArrayList<String>();

		// Grabs valid words from //http://norvig.com/ngrams/enable1.txt
		URL url = new URL("http://norvig.com/ngrams/enable1.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		
		//for loop to read each word from url
		for (String currentWord = reader.readLine(); currentWord != null; currentWord = reader.readLine()) {
			Map<Character, Integer> currentWordMap = getCharCountMap(currentWord);
			
			
			//this stays as true unless can not make word
			boolean canMakeCurrentWord = true;
			
			//loop to count how many characters from input and from word from URL
			for (Character character : currentWordMap.keySet()) {
				int currentWordCharCount = currentWordMap.get(character);
				int lettersCharCount = lettersCountMap.containsKey(character) ? lettersCountMap.get(character) : 0;
				if (currentWordCharCount > lettersCharCount) {
					canMakeCurrentWord = false;
					break;
				}
			}
			
			//condition if can make word is true and word equals specified word length to add current word to arrayList
			if (canMakeCurrentWord && currentWord.length() == wordLength) {
//				System.out.println(currentWord); used to test if valid words are found
				wordArrayList.add(currentWord);	
			}
		scanner.close();
//		reader.close(); //Need to close this to stop memory leaks but causes error if done
	}
		
		try {
			String [] words = wordArrayList.toArray(new String[0]);
			
			System.out.println(wordSquares(words));
			
			}
			catch(Exception e) {
				System.out.println("Something went wrong.");
			}
		
		
	
		
	}
	
	
	//Method which uses word
	//@param words valid words found in dictionary that are of size wordLength
	//@return words which can be used in a word square
	public static List<List<String>> wordSquares(String[] words){
		List<List<String>> result = new ArrayList<>();
		createPrefixMap(words);
		for(int i = 0; i < words.length; i++) {
			LinkedList<String> list = new LinkedList<>();
			list.add(words[i]);
			backTrack(1, list, result, words[i].length());
		}
		return result;
	}
	
	
	
	
	//method to backtrack through 
	//@param step current step in the backtrack algorithm
	//@param list uses prefixMap to add words to list
	//@param result uses result from wordSquares
	//@param n takes input as how long word is
	private static void backTrack(int step, LinkedList<String> list, List<List<String>> result, int n) {
		if(list.size() == n) {
			result.add(new ArrayList<>(list));
			return;
		}
		StringBuilder sb = new StringBuilder();
		
		for(String word : list) {
			sb.append(word.charAt(step));
		}
		String prefix = sb.toString();
		List<String> wordList = map.getOrDefault(prefix, new ArrayList<>());
		for(String word: wordList) {
			list.add(word);
			backTrack(step + 1, list, result, n);
			list.removeLast();
		}
	}

	
	
	
	//method to get prefix of word
	//@param words uses valid words 
	// adds prefix to list
	private static void createPrefixMap(String[] words) {
		for(String word : words) {
			for(int i = 0; i< word.length(); i++) {
				String prefix = word.substring(0, i);
				map.putIfAbsent(prefix, new ArrayList<>());
				List<String> list = map.get(prefix);
				list.add(word);
			}
		}
	}
	
	
	
	
	
	//method to count characters 
	//@param letters counts number of characters in word
	//@return returns a HashMap of characters 
	private static Map<Character, Integer> getCharCountMap(String letters) {
		// Map to store how many Chars in scanner
		Map<Character, Integer> lettersCountMap = new HashMap<>();

		// Looping through pool of Chars
		for (int i = 0; i < letters.length(); i++) {
			char currentChar = letters.charAt(i);
			int count = lettersCountMap.containsKey(currentChar) ? lettersCountMap.get(currentChar) : 0;
			lettersCountMap.put(currentChar, count + 1);
		}
		return lettersCountMap;
	}
}
