 /* Copyright 2016 Google Inc.
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
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Arrays;


public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList wordList;
    private HashSet wordSet;
    private HashMap<String, ArrayList<String>> lettersToWords;
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet();
        lettersToWords = new HashMap<>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String sortedWord = sortLetters(word);
            wordSet.add(word);
            if(lettersToWords.containsKey(sortedWord)) {
                ArrayList anagrams;
                anagrams = (ArrayList) lettersToWords.get(sortedWord);
                anagrams.add(word);
                lettersToWords.put(sortedWord, anagrams);
            }
            else {
                ArrayList anagrams = new ArrayList();
                anagrams.add(word);
                lettersToWords.put(sortedWord, anagrams);
            }
            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                sizeToWords.put(word.length(), new ArrayList<>(Arrays.asList(word)));
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word)){
            return false;
        }
        if (word.contains(base)){
            return false;
        }
        return true;
    }

    public String sortLetters(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        String sortedWord = new String(chars);
        return sortedWord;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = sortLetters(targetWord);
//        for (int i = 0; i < wordSet.size(); i++) {
//            if(wordSet.get(i).toString().length() == targetWord.length()) {
//                String sortedWordListWord = sortLetters(wordList.get(i));
//                if (sortedTargetWord.equals(sortedWordListWord)) {
//                    result.add(wordList.get(i));
//                }
//            }
//        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char letter = 'a'; letter <= 'z'; letter++) {
            if(lettersToWords.containsKey(sortLetters(word + letter))) {
                result.addAll(lettersToWords.get(sortLetters(word + letter)));
            }
        }
        //delete the bad words
        for (int i = 0; i < result.size(); i++){
            if (!isGoodWord(result.get(i), word)){
                result.remove(i);
                i--;
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> wordArray = sizeToWords.get(Math.min(MAX_WORD_LENGTH, wordLength));

        int maxLen = wordArray.size();
        int sp = random.nextInt(maxLen), loopIndex;
        String key;
        for (loopIndex=sp; loopIndex < (maxLen+sp+1); loopIndex++){
            key = wordArray.get(loopIndex % maxLen);
            if (getAnagramsWithOneMoreLetter(key).size() >= MIN_NUM_ANAGRAMS){
                break;
            }
        }

        wordLength += 1;
        return wordArray.get(loopIndex % maxLen);
    }
}
