import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class GameView {
    private JFrame frame;
    private JLabel title;
    private JLabel wrapper;
    public JPanel board;
    private JPanel buttonPanel;
    private JButton restartButton;
    private JButton quitButton;
    public ArrayList<Color> tileColors;

    public GameView(){
        initComponents();
        configureTitle();
        configureBoard();
        configureButtonPanel();
        configureFrame();
        assembleComponents();
    }

    private void initComponents(){
        frame = new JFrame("Wordley");
        title = new JLabel("Wordley");
        quitButton = new JButton("Quit");
        restartButton = new JButton("Restart");
        buttonPanel = new JPanel(new GridLayout(1,2));
        board = new JPanel(new GridLayout(6, 5, 10, 10));
        wrapper = new JLabel();
        tileColors = new ArrayList<>();
    }
    private void configureTitle() {
        title.setPreferredSize(new Dimension(400,35));
        title.setFont(new Font("Arial", Font.BOLD, 35));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(0);
    }

    public void setListenerQuitButton(ActionListener listener){
        quitButton.addActionListener(listener);
    }

    public void setListenerRestartButton(ActionListener listener){
        restartButton.addActionListener(listener);
    }
    public void closeFrame(){
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public void setKeyAdapter(KeyAdapter adapter){
        board.addKeyListener(adapter);
    }

    public void removeKeyAdapter(KeyAdapter adapter){
        board.removeKeyListener(adapter);
    }

    public void setFocusBoard(){
        board.setFocusable(true);
        board.requestFocusInWindow();
    }

    private void configureButtonPanel() {
        buttonPanel.setPreferredSize(new Dimension(400,35));
        buttonPanel.setAlignmentX(0);
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);
    }
    private void configureBoard() {
        board.setPreferredSize(new Dimension(400,400));
        board.setBackground(Color.BLACK);
    }

    private JPanel wrap(JComponent c){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        panel.setBackground(Color.BLACK);
        panel.add(c);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        return panel;
    }

    private void configureFrame() {
        frame.setSize(500,600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBackground(Color.BLACK);
        frame.setVisible(true);
    }

    private void assembleComponents() {
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.BLACK);
        wrapper.setOpaque(true);
        wrapper.add(Box.createHorizontalStrut(10));
        wrapper.add(wrap(title));
        wrapper.add(Box.createHorizontalStrut(10));
        wrapper.add(wrap(board));
        wrapper.add(Box.createHorizontalStrut(10));
        wrapper.add(wrap(buttonPanel));
        wrapper.add(Box.createHorizontalStrut(10));
        frame.add(wrapper);
    }

    public void refreshBoard(ArrayList<String> guessedLetters) {
        board.removeAll();
        drawTiles(guessedLetters);
        board.revalidate();
        board.repaint();
        frame.revalidate();
        frame.repaint();
    }

    public void drawTiles(ArrayList<String> guessedLetters) {
        int tileIdx = 0;
        for (String letterStr : guessedLetters) {
            JLabel tile = new JLabel(letterStr.toUpperCase());
            tile.setFont(new Font("Arial", Font.BOLD, 27));
            tile.setBorder(new LineBorder(Color.GRAY, 2));
            tile.setForeground(Color.WHITE);
            tile.setBackground(getTileColor(tileIdx));
            tile.setHorizontalAlignment(0);
            tile.setOpaque(true);
            board.add(tile);
            tileIdx++;
        }
    }

    private Color getTileColor(int index) {
        return tileColors.get(index);
    }

    public ArrayList<Color> getTileColors() {
        return tileColors;
    }

    public void setTileColor(int index, Color color) {
         tileColors.set(index, color);
    }

    public void addTileColor(Color color) {
        tileColors.add(color);
    }

    public void failure(String mysteryWord) {
        System.out.println("Sorry, the word was " + mysteryWord);
    }

    public void success(String mysteryWord) {
        System.out.println("Correct! The word was " + mysteryWord);
    }

    public void gameOver() {
        System.out.println("Game over");
        System.out.println("Press 'Restart' to play again");
    }

}
