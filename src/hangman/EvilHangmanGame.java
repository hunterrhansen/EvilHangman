package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame {
  private Set<String> possibleWords;

  public EvilHangmanGame() {
    possibleWords = new TreeSet<>();
  }

  @Override
  public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
    try (Scanner sc = new Scanner(dictionary)) {
      if (!sc.hasNext()) {
        throw new EmptyDictionaryException("The provided dictionary file is empty.");
      }

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
    return null;
  }

  @Override
  public SortedSet<Character> getGuessedLetters() {
    return null;
  }
}
