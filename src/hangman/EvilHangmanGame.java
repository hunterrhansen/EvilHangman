package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
  private Set<String> possibleWords;
  private TreeSet<Character> guessedLetters;
  private String wordPattern;
  private int letterOccurrences;

  public EvilHangmanGame() {
    possibleWords = null;
    guessedLetters = null;
    wordPattern = null;
    letterOccurrences = 0;
  }

  @Override
  public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
    try (Scanner sc = new Scanner(dictionary)) {
      if (!sc.hasNext()) {
        throw new EmptyDictionaryException("The provided dictionary file is empty.");
      }

      possibleWords = new TreeSet<>();
      guessedLetters = new TreeSet<>();
      wordPattern = "-".repeat(wordLength);

      while (sc.hasNext()) {
        String nextWord = sc.next();
        if (nextWord.length() == wordLength) {
          possibleWords.add(nextWord);
        }
      }
    } catch (FileNotFoundException ex) {
      System.out.println("Could not find file " + dictionary);
      ex.printStackTrace();
    }


    if (possibleWords.isEmpty()) {
      throw new EmptyDictionaryException("The provided dictionary file did not contain any words of length " + wordLength);
    }
  }

  @Override
  public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
    // check if guess is valid and add it to the guessed letters array
    guess = Character.toLowerCase(guess);
    if (guessedLetters.contains(guess)) {
      throw new GuessAlreadyMadeException("Guess already made!");
    }
    if (!Character.isLetter(guess)) {
      System.out.println("Invalid input!");
    } else {
      guessedLetters.add(guess);
    }

    // initialize variables
    Map<String, Set<String>> partitions = new HashMap<>(); // keeps track of the various partitions and size of each
    int maxPartitionSize = 0; // keeps track of the biggest partition
    int minLetterOccurrences = wordPattern.length(); // keeps track of the smallest number of times a letter occurs in the word

    // iterate through each word in current set of possible words and add them to the proper partition
    for (String word : possibleWords) {
      int currOccurrences = 0;
      StringBuilder pattern = new StringBuilder();

      // generating key
      for (int i = 0; i < word.length(); i++) {
        if (word.charAt(i) == guess) {
          pattern.append(guess);
          currOccurrences++;
        } else {
          pattern.append(wordPattern.charAt(i));
        }
      }
      String key = pattern.toString();

      // generate partitons and add word into respective partition
      if (!partitions.containsKey(key)) {
        Set<String> keySet = new HashSet<>();
        keySet.add(word);
        partitions.put(key, keySet);
      } else {
        partitions.get(key).add(word);
      }

      if (partitions.get(key).size() > maxPartitionSize) {
        maxPartitionSize = partitions.get(key).size();
      }
      if (currOccurrences < minLetterOccurrences) {
        minLetterOccurrences = currOccurrences;
      }
    }

    prioritizeLargestSet(partitions, maxPartitionSize);
    if (partitions.size() > 1) {
      for (String pattern : partitions.keySet()) {
        if (!pattern.contains(Character.toString(guess))) {
          letterOccurrences = 0;
          possibleWords = partitions.get(pattern);
          return possibleWords;
        }
      }
    }

    prioritizeFewestOccurrences(partitions, minLetterOccurrences, guess);
    if (partitions.size() > 1) {
      prioritizeRightMost(partitions, wordPattern.length() - 1, guess);
    }

    String finalPattern = "";
    int occurrences = 0;
    for (String pattern : partitions.keySet()) {
      for (int i = 0; i < pattern.length(); i++) {
        if (pattern.charAt(i) == guess) {
          occurrences++;
        }
      }
      finalPattern = pattern;
    }

    wordPattern = finalPattern;
    letterOccurrences = occurrences;
    possibleWords = partitions.get(finalPattern);
    return possibleWords;
  }

  private void prioritizeLargestSet(Map<String, Set<String>> partitions, int maxSize) {
    partitions.values().removeIf(set -> set.size() < maxSize);
  }

  private void prioritizeFewestOccurrences(Map<String, Set<String>> partitions, int minOccurrences, char guess) {
    for (Iterator<Map.Entry<String, Set<String>>> itr = partitions.entrySet().iterator(); itr.hasNext();) {
      int occurrences = 0;
      String pattern = itr.next().getKey();
      for (int i = 0; i < pattern.length(); i++) {
        if (pattern.charAt(i) == guess) {
          occurrences++;
        }
      }
      if (occurrences > minOccurrences) {
        itr.remove();
      }
    }
  }

  private void prioritizeRightMost(Map<String, Set<String>> partitions, int startIndex, char guess) {
    int ind = 0;

    for (String pattern : partitions.keySet()) {
      int i = startIndex;
      while (i > ind) {
        if (pattern.charAt(i) == guess) {
          ind = i;
        }
        i--;
      }
    }

    int rightmostInd = ind;
    partitions.keySet().removeIf(key -> key.charAt(rightmostInd) != guess);

    if (partitions.size() > 1) {
      prioritizeRightMost(partitions, rightmostInd - 1, guess);
    }
  }

  @Override
  public SortedSet<Character> getGuessedLetters() {
    return guessedLetters;
  }

  public String getWordPattern() {
    return wordPattern;
  }

  public int getLetterOccurrences() { return letterOccurrences; }

  public String getFirstWord() { return "busses"; }
}
