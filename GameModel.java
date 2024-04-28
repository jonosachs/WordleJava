import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameModel {
    private ArrayList<String> dictionary;
    private ArrayList<String> guessedWords;
    private ArrayList<String> guessedLetters;
    private String mysteryWord;

    public GameModel(){
        initDictionary("csv/5_letter_common_words.csv");
        guessedWords = new ArrayList<>();
        guessedLetters = new ArrayList<>();
        setRandomMysteryWord();
    }

    public void addGuessedWord(String guessedWord) {
        guessedWords.add(guessedWord);
    }

    public void addGuessedLetter(String guessedLetter) {
        guessedLetters.add(guessedLetter);
    }

    public void setGuessedLetter(int index, String guessedChar) {
        guessedLetters.set(index, guessedChar);
    }

    public void clearGuessedLetters(){
        guessedLetters.clear();
    }

    public void clearGuessedWords(){
        guessedWords.clear();
    }


    public String getGuessedLetter(int index) {
        return guessedLetters.get(index);
    }

    public ArrayList<String> getGuessedWords() {
        return guessedWords;
    }

    public ArrayList<String> getGuessedLetters() {
        return guessedLetters;
    }

    private void initDictionary(String csvPath) {
        dictionary = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(csvPath))) {
            while (scanner.hasNextLine()) {
                dictionary.add(scanner.nextLine().strip());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getDictionary(){
        return dictionary;
    }

    public void setRandomMysteryWord() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(0, dictionary.size() - 1);
        mysteryWord = dictionary.get(randomNumber);
        System.out.println(mysteryWord);
    }

    public String getMysteryWord(){
        return mysteryWord;
    }

}

