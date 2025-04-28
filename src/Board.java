import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private boolean isOpponent;
    private Cell[][] cells = new Cell[Constants.SIZE][Constants.SIZE];
    private Battleship battleship;

    public Board(boolean isOpponent, Battleship battleship) {
        this.isOpponent = isOpponent;
        this.battleship = battleship;
        setLayout(new GridLayout(Constants.SIZE, Constants.SIZE));

        initialize();
        placeShipsRandomly();
    }

    private void initialize() {
        for (int i =0; i< Constants.SIZE; i++){
            for (int j=0; j<Constants.SIZE; j++){
                cells[i][j] = new Cell(i, j, this);
                add(cells[i][j]);
            }
        }
        revalidate();
        repaint();
    }

    public boolean isOpponent(){
        return isOpponent;
    }

    public Cell getCell (int x, int y){
        return cells[x][y];
    }

    public Battleship getBattleship() {
        return battleship;
    }



    private void placeShipsRandomly() {

    }

    public void shoot(Cell cell){
        if (cell.isWasShot()) return;

        cell.markShot();

        if (cell.isHasShip()){
            if (isOpponent){
                if (allShipsSunk()){
                    battleship.endGame("You won!");
                }
            } else {
                if (allShipsSunk()){
                    battleship.endGame("You loose!");
                }
            }
        } else {
            battleship.setPlayerTurn(!isOpponent);
        }

    }

    private boolean allShipsSunk() {
        for (int i =0; i< Constants.SIZE; i++){
            for (int j=0; j<Constants.SIZE; j++){
                if (cells[i][j].isHasShip() && !cells[i][j].isWasShot()){
                    return false;
                }
            }
        }
        return true;
    }
}
