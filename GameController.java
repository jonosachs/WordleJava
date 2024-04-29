import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

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
        setTileColors();
        refreshBoard();
        addActionListeners();
        addKeyAdapter();
    }

    private void refreshBoard(){
        setTileColors();
        view.refreshBoard(model.getGuessedLetters());
    }

    private void setEmptyGuessedLetters() {
        model.clearGuessedLetters();
        for (int x = 0; x < 30; x++) {
            model.addGuessedLetter(" ");
        }
    }

    private void setBlackTiles() {
        for (int x = 0; x < 30; x++) {
            view.addTileColor(Color.BLACK);
        }
    }

    private void addActionListeners(){
        view.setListenerQuitButton(e -> view.closeFrame());
        view.setListenerRestartButton(e -> restartGame());
    }

    private void addKeyAdapter() {
        view.setFocusBoard();
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
            }
        };
        view.setKeyAdapter(keyAdapter);
    }

    private void validGuess(String guess){
        for (int idx = 0; idx < guess.length(); idx++) {
            String letter = String.valueOf(guess.charAt(idx));
            model.setGuessedLetter(wordStartIdx + idx, letter);
        }
        model.addGuessedWord(guess);
        guessCount++;
        wordStartIdx = wordStartIdx + 5;
        wordEndIdx = wordEndIdx + 5;
        model.initNumLettersToDict();
        refreshBoard();
    }

    public void setTileColors() {
        if (view.getTileColors().isEmpty()) {
            setBlackTiles();
        }
        int tileIdx = 0;
        int letterIdx = 0;
        for (String letterStr : model.getGuessedLetters()) {
            if (letterStr.equals(" ") | tileIdx >= wordStartIdx) {
                view.setTileColor(tileIdx, Color.BLACK);
            }
            else if (Objects.equals(letterStr, String.valueOf(model.getMysteryWord().charAt(letterIdx)))) {
                view.setTileColor(tileIdx, new Color(117, 173, 107));
                model.reduceNumLettersRemaining(letterStr);
            }
            else if (model.getMysteryWord().contains(letterStr) & letterCountNotExceedsMax(letterStr)) {
                view.setTileColor(tileIdx, new Color(224, 191, 81));
                model.reduceNumLettersRemaining(letterStr);
            } else {
                view.setTileColor(tileIdx, Color.DARK_GRAY);
            }

            tileIdx++;
            letterIdx++;

            if (letterIdx == 5) {
                letterIdx = 0;
                model.initNumLettersToDict();
            }
        }
    }

    private boolean letterCountNotExceedsMax(String letterStr) {
        return model.getNumLettersRemaining(letterStr) > 0;
    }

    private void validateGuess(String guessWord) {
        boolean guessed = false;
        if (Objects.equals(guessWord, model.getMysteryWord())) {
            success(model.getMysteryWord());
            endGame();
            guessed = true;
        }
        if (model.getDictionary().contains(guessWord) &&
                !model.getGuessedWords().contains(guessWord)) {
            validGuess(guessWord);
        } else {
            invalidWord();
        }
        if (guessCount == 6 & !guessed) {
            failure(model.getMysteryWord());
            endGame();
        }
    }

    private String getGuessWord() {
        StringBuilder guess = new StringBuilder();
        for (int i = wordStartIdx; i < wordEndIdx; i++) {
            guess.append(model.getGuessedLetter(i));
        }
        return guess.toString().toLowerCase();
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
        model.initNumLettersToDict();
        setEmptyGuessedLetters();
        refreshBoard();
        addKeyAdapter();
    }

    private void invalidWord() {
        view.showDialogBox("Invalid word");
        delayedHideDialog(650);
    }

    public void delayedHideDialog(int delay){
        timer = new Timer(delay, e -> {
            view.hideDialogBox();
            timer.stop();
        });
        timer.start();
    }

    private void endGame(){
        view.removeKeyAdapter(keyAdapter);
    }

    public void failure(String mysteryWord) {
        view.showDialogBox("Sorry, the word was " + mysteryWord);
        delayedHideDialog(2500);
    }

    public void success(String mysteryWord) {
        view.showDialogBox("Correct! The word was " + mysteryWord);
        delayedHideDialog(2500);
    }

    public void gameOver() {
        view.showDialogBox("""
                        Game over
                        Press 'Restart' to play again
                        """);
    }
}
