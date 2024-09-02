import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * Class handles interaction between view and model classes.
 * Assigns key and action listeners for keyboard input and button presses
 * Validates guesses and determines tile colors based on letter correctness
 * Restart and Quit game functionality  
 */
public class GameController {
    private final GameModel model;
    private final GameView view;
    private KeyAdapter keyAdapter;
    private Timer timer;
    private int guessCount = 0;
    private int currentTileIdx = 0;
    private int wordStartIdx = 0;
    private int wordEndIdx = 5;

    public GameController(GameModel model, GameView view) {
        this.view = view;
        this.model = model;
        setEmptyGuessedLetters();
        setBlackTiles();
        refreshBoard();
        addActionListeners();
        addKeyAdapter();
    }

    private void setEmptyGuessedLetters() {
        model.clearGuessedLetters();
        for (int x = 0; x < 30; x++) {
            model.addGuessedLetter(" ");
        }
    }

    private void setBlackTiles() {
        if (!view.getTileColors().isEmpty()) {
            view.clearTileColor();
        }
        for (int x = 0; x < 30; x++) {
            view.addTileColor(Color.BLACK);
        }
    }

    private void refreshBoard(){
        view.refreshBoard(model.getGuessedLetters());
    }

    /**
     * Sets action listeners for game buttons
     */
    private void addActionListeners(){
        view.setListenerQuitButton(e -> view.closeFrame());
        view.setListenerRestartButton(e -> restartGame());
    }

    /**
     * Creates keyAdapator to listen for key presses.
     * Passes alphabetic inputs to model repository to display on the board.
     * Sends input for validation when enter key is pressed.
     *
     */
    private void addKeyAdapter() {
        view.setFocusToBoard();
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyPressed = e.getKeyChar();
                if (Character.isAlphabetic(keyPressed) && currentTileIdx < wordEndIdx) {
                    model.setGuessedLetter(currentTileIdx, String.valueOf(keyPressed));
                    refreshBoard();
                    currentTileIdx++;
                }
                if (keyPressed == KeyEvent.VK_BACK_SPACE && currentTileIdx >= wordStartIdx) {
                    if (currentTileIdx != wordStartIdx) {
                        currentTileIdx--;
                    }
                    model.setGuessedLetter(currentTileIdx," ");
                    refreshBoard();
                }
                if (keyPressed == KeyEvent.VK_ENTER) {
                    String guessWord = getGuessWord();
                    validateGuess(guessWord);
                }
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W) {
                    System.out.println(model.getMysteryWord());
                }
            }
        };
        view.setKeyAdapter(keyAdapter);
    }

    /**
     * Gets the current guess letters and arranges into a 5-letter word
     *
     * @return The 5-letter word as a String
     */
    private String getGuessWord() {
        StringBuilder guess = new StringBuilder();
        for (int i = wordStartIdx; i < wordEndIdx; i++) {
            guess.append(model.getGuessedLetters().get(i));
        }
        return guess.toString().toLowerCase();
    }

    /**
     * Validates word guesses and determines appropriate actions.
     *
     * @param guessWord The 5-letter word to be validated
     */
    private void validateGuess(String guessWord) {
        boolean guessed = false;
        if (Objects.equals(guessWord, model.getMysteryWord())) {
            success(model.getMysteryWord());
            endGame();
            guessed = true;
        }
        if (model.getDictionary().contains(guessWord) &&
                !model.getGuessedWords().contains(guessWord)) {
            commitValidGuess(guessWord);
        } else {
            invalidWord();
        }
        if (guessCount == 6 & !guessed) {
            failure(model.getMysteryWord());
            endGame();
        }
    }

    private void commitValidGuess(String guess){
        for (int idx = 0; idx < guess.length(); idx++) {
            String letter = String.valueOf(guess.charAt(idx));
            model.setGuessedLetter(wordStartIdx + idx, letter);
        }
        model.addGuessedWord(guess);
        guessCount++;
        wordStartIdx = wordStartIdx + 5;
        wordEndIdx = wordEndIdx + 5;
        model.initNumLettersRemaining();
        setGuessTileColors();
        refreshBoard();
    }

    /**
     * Sets tile colors for the last guess based on correctness and position.
     */
    public void setGuessTileColors() {
        int tileIdx = wordStartIdx-5;
        int letterIdx = 0;

        for (String letterStr : model.getGuessedLetters().subList(wordStartIdx-5, wordEndIdx-5)) {
            //Letter contained in mystery word AND in correct position = green
            if (Objects.equals(letterStr, String.valueOf(model.getMysteryWord().charAt(letterIdx)))) {
                view.setTileColor(tileIdx, new Color(117, 173, 107));
            }
            //Letter contained in mystery word but in wrong position = yellow
            else if (model.getMysteryWord().contains(letterStr) & guessLetterRemaining(letterStr)) {
                view.setTileColor(tileIdx, new Color(224, 191, 81));
            } else {
                view.setTileColor(tileIdx, Color.DARK_GRAY);    //Guessed letters not in mystery word = dark grey.
            }

            tileIdx++;
            letterIdx++;
        }
    }

    /**
     * Checks if any occurrences of a non-green guess letter remain in the mystery word after
     * any green occurrences have been accounted for.
     *
     * @param letterStr Guess letter to check against the remaining instances
     * @return True if the remaining instances are above 0, else False.
     */
    private boolean guessLetterRemaining(String letterStr) {
        String guessWord = model.getGuessedWord(guessCount-1);

        //Deduct green letters from the number of letters remaining
        for (int idx = 0; idx < guessWord.length(); idx++) {
            boolean isGreenLetter = guessWord.charAt(idx) == model.getMysteryWord().charAt(idx);
            if (isGreenLetter) {
                model.setNumLettersRemaining(String.valueOf(guessWord.charAt(idx)),
                       model.getNumLettersRemaining(letterStr)-1);
            }
        }
        return model.getNumLettersRemaining(letterStr) > 0;
    }

    private void restartGame() {
        currentTileIdx = 0;
        guessCount = 0;
        wordStartIdx = 0;
        wordEndIdx = 5;
        view.removeKeyAdapter(keyAdapter);
        model.clearGuessedWords();
        model.clearGuessedLetters();
        model.setRandomMysteryWord();
        model.initNumLettersRemaining();
        setEmptyGuessedLetters();
        setBlackTiles();
        refreshBoard();
        addKeyAdapter();
    }

    /**
     * Timer to delay closure of the dialogue box
     *
     * @param delay Delay duration in milliseconds
     */
    public void delayHideDialog(int delay){
        timer = new Timer(delay, e -> {
            view.hideDialogBox();
            timer.stop();
        });
        timer.start();
    }

    private void invalidWord() {
        view.showDialogBox("Invalid word");
        delayHideDialog(650);
    }

    private void endGame(){
        view.removeKeyAdapter(keyAdapter);
    }

    public void failure(String mysteryWord) {
        view.showDialogBox("Sorry, the word was " + mysteryWord);
        delayHideDialog(2500);
    }

    public void success(String mysteryWord) {
        view.showDialogBox("Correct! The word was " + mysteryWord);
        delayHideDialog(2500);
    }

}
