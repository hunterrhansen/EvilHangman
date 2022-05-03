package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {
        File dictionary = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);

        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();

        try {
            evilHangmanGame.startGame(dictionary, wordLength);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EmptyDictionaryException e) {
            System.out.println(e.getMessage());
        }

        Scanner userInput = new Scanner(System.in);

        while (numGuesses > 0) {
            System.out.println("You have " + numGuesses + " guesses left");
            System.out.println("Used letters: " + evilHangmanGame.getGuessedLetters());
            System.out.println("Word: " + "-----");

            System.out.println("Enter guess: ");
            char userGuess = userInput.nextLine().charAt(0);

            try {
                evilHangmanGame.makeGuess(userGuess);
            } catch (GuessAlreadyMadeException ex) {
                System.out.print(ex.getMessage());
            }

            numGuesses -= 1;
        }

        System.out.println("Sorry you lost! The word was: busses");

    }

}
