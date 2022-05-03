package hangman;

import java.io.File;
import java.io.IOException;

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


    }

}
