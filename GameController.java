import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class GameController {
    private GameModel model;
    private GameView view;
    private int guessCount;
    private int currentTileIdx;
    private int wordStartIdx;
    private int wordEndIdx;
    private KeyAdapter keyAdapter;

    public GameController(GameModel model, GameView view) {
        this.view = view;
        this.model = model;
        wordStartIdx = 0;
        wordEndIdx = 5;
        guessCount = 0;
        currentTileIdx = 0;
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
                if (keyPressed == KeyEvent.VK_ENTER && currentTileIdx == wordEndIdx) {
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
            }
            else if (model.getMysteryWord().contains(letterStr)) {
                view.setTileColor(tileIdx, new Color(224, 191, 81));
            } else {
                view.setTileColor(tileIdx, Color.DARK_GRAY);
            }
            tileIdx++;
            letterIdx++;
            if (letterIdx == 5) {
                letterIdx = 0;
            }
        }
    }

    private void printValues(){
        System.out.println("currentTileIdx" + " " + currentTileIdx);
        System.out.println("wordStartIndx" + " " + wordStartIdx);
    }

//    private boolean isColored(String letterStr) {
//        if (numCharsUsed.containsKey(letterStr)) {
//            System.out.println(numCharsUsed);
//            if (numCharsUsed.get(letterStr) >= 0){
//                numCharsUsed.replace(letterStr, numCharsUsed.get(letterStr) - 1);
//                return true;
//            }
//        }
//        return false;
//    }

    private void validateGuess(String guessWord){
        boolean guessed = false;
        if (Objects.equals(guessWord, model.getMysteryWord())) {
            view.success(model.getMysteryWord());
            endGame();
            guessed = true;
        }
        if (model.getDictionary().contains(guessWord) &&
                !model.getGuessedWords().contains(guessWord)) {
            validGuess(guessWord);
        } else {
            System.out.println("invalid word");
        }
        if (guessCount == 6 & !guessed) {
            view.failure(model.getMysteryWord());
            endGame();
        }
    }

    private void endGame(){
        view.removeKeyAdapter(keyAdapter);
        view.gameOver();
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
        setEmptyGuessedLetters();
        refreshBoard();
        addKeyAdapter();
    }
}
