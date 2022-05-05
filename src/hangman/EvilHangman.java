package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {

        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();

        try {
            File dictionary = new File(args[0]);
            int wordLength = Integer.parseInt(args[1]);
            evilHangmanGame.startGame(dictionary, wordLength);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EmptyDictionaryException e) {
            System.out.println(e.getMessage());
        }

        Scanner userInput = new Scanner(System.in);
        int numGuesses = Integer.parseInt(args[2]);

        while (numGuesses > 0) {
            try {
                System.out.println("You have " + numGuesses + " guesses left");
                System.out.println("Used letters: " + evilHangmanGame.getGuessedLetters());
                System.out.println("Word: " + evilHangmanGame.getWordPattern());

                System.out.println("Enter guess: ");
                String userGuess = userInput.next();

                if (userGuess.isBlank()) {
                    System.out.println("You must enter a letter as a guess");
                    continue;
                }
                if (!Character.isLetter(userGuess.charAt(0))) {
                    System.out.println(userGuess.charAt(0) + " is not valid input. Please guess a letter.");
                    continue;
                }
                evilHangmanGame.makeGuess(userGuess.charAt(0));
                int letterOccurrences = evilHangmanGame.getLetterOccurrences();
                if (letterOccurrences == 0) {
                    System.out.println("Sorry, there are no " + userGuess.charAt(0));
                    numGuesses -= 1;
                } else {
                    System.out.println("Yes, there are " + letterOccurrences + " " + userGuess.charAt(0));
                }

            } catch (GuessAlreadyMadeException | IllegalArgumentException ex) {
                System.out.print(ex.getMessage());
            }
        }

        if (evilHangmanGame.getWordPattern().contains("-")) {
            System.out.print("Sorry, you lost! ");
        } else {
            System.out.print("You win! ");
        }
        System.out.println("The word was: " + evilHangmanGame.getFirstWord());
    }

}
