import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class contains the dictionary and gets the random mystery word.
 * Keeps track of guessed words and letters during play.
 * Stores the quantity of each guess letter occurrence in the mystery word (used for tile coloring)
 */
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
        initNumLettersRemaining();
    }

    public void addGuessedWord(String guessedWord) {
        guessedWords.add(guessedWord);
    }

    public ArrayList<String> getGuessedWords() {
        return guessedWords;
    }

    public String getGuessedWord(int wordIndex) {
        return guessedWords.get(wordIndex);
    }

    public void clearGuessedWords(){
        guessedWords.clear();
    }

    public void addGuessedLetter(String guessedLetter) {
        guessedLetters.add(guessedLetter);
    }

    public ArrayList<String> getGuessedLetters() {
        return guessedLetters;
    }

    public void setGuessedLetter(int index, String guessedChar) {
        guessedLetters.set(index, guessedChar);
    }

    public void clearGuessedLetters(){
        guessedLetters.clear();
    }

    /**
     * Gets the dictionary from local csv file and stores in an ArrayList
     *
     * @param csvPath Path for the csv file
     */
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

    /**
     * Selects a word from the dictionary using randomly generated index
     */
    public void setRandomMysteryWord() {
        Random rand = new Random();
        int randomIndexNum = rand.nextInt(0, dictionary.size() - 1);
        mysteryWord = dictionary.get(randomIndexNum);
//        mysteryWord = "balmy";
//        System.out.println(mysteryWord);
    }

    /**
     * Determines the number of occurrences of a particular letter in a word
     *
     * @param word The target word
     * @param letter The target letter to count occurrences
     * @return The number of letter occurrences
     */
    public long getNumLettersInWord(String word, String letter) {
        if (word.contains(letter)) {
            return word.chars().filter(e -> e == letter.charAt(0)).count();
        }
        return 0;
    }

    /**
     * Initialises the number of occurrences of each letter in the mystery word remaining
     * before any are 'consumed' in a guess.
     */
    public void initNumLettersRemaining() {
        if (numLettersInWord == null) {
            numLettersInWord = new HashMap<>();
        }
        else numLettersInWord.clear();
        for (int idx = 0; idx < mysteryWord.length(); idx++) {
            String letter = String.valueOf(mysteryWord.charAt(idx));
            numLettersInWord.put(letter, getNumLettersInWord(getMysteryWord(), letter));
        }
    }

    /**
     * Gets the number of occurrences of a guess letter remaining, less those used in the guess already
     *
     * @param letter The guess letter to check remaining quantity
     * @return The quantity remaining
     */
    public long getNumLettersRemaining(String letter){
        if (mysteryWord.contains(letter)) {
            return numLettersInWord.get(letter);
        }
        return 0;
    }

    public void setNumLettersRemaining(String letter, long numRemaining) {
        if (numLettersInWord.containsKey(letter)){
            numLettersInWord.put(letter, numRemaining);
        }
    }

    public String getMysteryWord(){
        return mysteryWord;
    }

}

