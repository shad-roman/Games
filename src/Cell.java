import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Cell extends JButton {
    private int x;
    private int y;
    private Board board;
    private boolean hasShip = false;
    private boolean wasShot = false;
    private boolean marked = false;

    public Cell(int y, int x, Board board) {
        this.y = y;
        this.x = x;
        this.board = board;

        setPreferredSize(new Dimension(30, 30));
        setBackground(Color.CYAN);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (board.isOpponent() && board.getBattleship().isPlayerTurn()) {
                    board.shoot(Cell.this);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (wasShot) {
            if (hasShip) {
                setBackground(Color.RED);
                g.setColor(Color.BLACK);
                g.drawString("X", getWidth() / 2 - 4, getHeight() / 2 + 5);
            } else {
                setBackground(Color.WHITE);
            }
        } else {
            if (!board.isOpponent() && hasShip) {
                setBackground(Color.GRAY);
            } else {
                setBackground(Color.CYAN);
            }
        }
    }

    public void markShot() {
        wasShot = true;
        repaint();
    }
    public boolean isHasShip(){
        return hasShip;
    }

    public boolean isWasShot(){
        return wasShot;
    }

    public boolean isMarked(){
        return marked;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    public void setWasShot(boolean wasShot) {
        this.wasShot = wasShot;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}
