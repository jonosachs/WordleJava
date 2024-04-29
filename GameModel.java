import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class GameModel {
    private ArrayList<String> dictionary;
    private ArrayList<String> guessedWords;
    private ArrayList<String> guessedLetters;
    private HashMap<String, Long> numLettersInWord;
    private String mysteryWord;

    public GameModel(){
        initDictionary("csv/5_letter_common_words.csv");
        guessedWords = new ArrayList<>();
        guessedLetters = new ArrayList<>();
        numLettersInWord = new HashMap<>();
        setRandomMysteryWord();
        initNumLettersToDict();
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

    public long getNumLettersInWord(String letter) {
        if (mysteryWord.contains(letter)) {
            return mysteryWord.chars().filter(e -> e == letter.charAt(0)).count();
        }
        return 0;
    }

    public void initNumLettersToDict() {
        if (numLettersInWord == null) {
            numLettersInWord = new HashMap<>();
        }
        else numLettersInWord.clear();
        for (int idx = 0; idx < mysteryWord.length(); idx++) {
            String letter = String.valueOf(mysteryWord.charAt(idx));
            numLettersInWord.put(letter, getNumLettersInWord(letter));
        }
    }

    public long getNumLettersRemaining(String letter){
        if (mysteryWord.contains(letter)) {
            return numLettersInWord.get(letter);
        }
        return 0;
    }

    public void reduceNumLettersRemaining(String letter) {
        if (numLettersInWord.containsKey(letter)){
            numLettersInWord.put(letter, getNumLettersRemaining(letter)-1);
        }
    }

    public String getMysteryWord(){
        return mysteryWord;
    }

}

