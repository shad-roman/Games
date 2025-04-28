import javax.swing.*;
import java.awt.*;

public class Battleship extends JFrame {
    private Board playerBoard;
    private Board computerBoard;
    private boolean playerTurn = true;
    private AI ai;

    public Battleship() {
        setTitle("Battleship");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500);
        setLayout(new BorderLayout(10, 10));


        JPanel topPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        topPanel.add(new JLabel("Your board", SwingConstants.CENTER));
        topPanel.add(new JLabel("Computer", SwingConstants.CENTER));
        add(topPanel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new GridLayout(1, 2, 40, 0)); // 40 пикселей отступ
        playerBoard = new Board(false, this);
        computerBoard = new Board(true, this);

        boardsPanel.add(playerBoard);
        boardsPanel.add(computerBoard);

        add(boardsPanel, BorderLayout.CENTER);

        ai = new AI(playerBoard, this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setPlayerBoard(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public void setComputerBoard(Board computerBoard) {
        this.computerBoard = computerBoard;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setAi(AI ai) {
        this.ai = ai;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public Board getComputerBoard() {
        return computerBoard;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public AI getAi() {
        return ai;
    }

    public void endGame(String message) {
        JOptionPane.showMessageDialog(this, message);
        System.exit(0);
    }
}
