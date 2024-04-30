import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Class handles the user interface.
 * Assembles Swing components including the game message dialogue box.
 *
 */
public class GameView {
    private JFrame frame;
    private JPanel board;
    private JButton restartButton;
    private JButton quitButton;
    private JDialog dialogBox;
    private ArrayList<Color> tileColors;

    public GameView(){
        tileColors = new ArrayList<>();
        initComponents();
    }

    /**
     * Initialises Swing components for the GUI
     */
    private void initComponents(){
        frame = new JFrame("Wordley");
        quitButton = new JButton("Quit");
        restartButton = new JButton("Restart");
        board = new JPanel(new GridLayout(6, 5, 10, 10));

        JLabel title = new JLabel("Wordley");
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JLabel wrapper = new JLabel();

        frame.setSize(500,650);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBackground(Color.BLACK);

        title.setPreferredSize(new Dimension(400,35));
        title.setFont(new Font("Arial", Font.BOLD, 35));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(0);

        board.setPreferredSize(new Dimension(400,450));
        board.setBackground(Color.BLACK);

        buttonPanel.setPreferredSize(new Dimension(400,35));
        buttonPanel.setAlignmentX(0);
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);

        // Components wrapped within JLabel to centre within 'board'
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
        frame.setVisible(true);
    }

    public void setListenerQuitButton(ActionListener listener){
        quitButton.addActionListener(listener);
    }

    public void setListenerRestartButton(ActionListener listener){
        restartButton.addActionListener(listener);
    }

    public void setKeyAdapter(KeyAdapter adapter){
        board.addKeyListener(adapter);
    }

    public void removeKeyAdapter(KeyAdapter adapter){
        board.removeKeyListener(adapter);
    }

    public void setFocusToBoard(){
        board.setFocusable(true);
        board.requestFocusInWindow();
    }

    /**
     * Wraps a JComponents within a JPanel in order to centre
     *
     * @param c JComponent to be wrapped
     * @return a JPanel containing the JComponent
     */
    private JPanel wrap(JComponent c){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        panel.setBackground(Color.BLACK);
        panel.add(c);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        return panel;
    }

    /**
     * Refreshes the board to show updated guessed words and/or letters
     *
     * @param guessedLetters The current list of guessed letters
     */
    public void refreshBoard(ArrayList<String> guessedLetters) {
        board.removeAll();
        drawTiles(guessedLetters);
        board.revalidate();
        board.repaint();
        frame.revalidate();
        frame.repaint();
    }

    public void closeFrame(){
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Displays hovering dialogue box to show game messages
     *
     * @param text The text message to be displayed
     */
    public void showDialogBox(String text) {
        dialogBox = new JDialog();
        dialogBox.setSize(300,75);
        dialogBox.setLocationRelativeTo(frame);
        JLabel msg = new JLabel(text, SwingConstants.CENTER);
        msg.setFont(new Font("Arial", Font.BOLD, 15));
        msg.setBackground(Color.BLACK);
        msg.setForeground(Color.WHITE);
        msg.setOpaque(true);
        dialogBox.add(msg);
        dialogBox.setUndecorated(true);
        dialogBox.setVisible(true);
    }

    public void hideDialogBox() {
        if (dialogBox != null) {
            dialogBox.setVisible(false);
            dialogBox.dispose();
            dialogBox = null;
        }
    }

    /**
     * Draws the board tiles
     *
     * @param guessedLetters The list of current guessed letters
     */
    public void drawTiles(ArrayList<String> guessedLetters) {
        int tileIdx = 0;
        for (String letterStr : guessedLetters) {
            JLabel tile = new JLabel(letterStr.toUpperCase());
            tile.setFont(new Font("Arial", Font.BOLD, 30));
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

    public void clearTileColor() {
        if (!tileColors.isEmpty()) {
            tileColors.clear();
        }
    }

    public void addTileColor(Color color) {
        tileColors.add(color);
    }

}
