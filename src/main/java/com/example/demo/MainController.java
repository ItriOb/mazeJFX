package com.example.demo;

import com.example.demo.model.Cell;
import com.example.demo.model.Labyrinthe;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainController {

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField sizeInput;

    @FXML
    private Button generateButton;

    @FXML
    private VBox container;

    @FXML
    private Canvas mazeCanvas;

    private int playerRow;
    private int playerColumn;

    private Labyrinthe maze;

    @FXML
    private void initialize() {
        mazeCanvas.setOnKeyReleased(this::handleKeyPress);

    }

    @FXML
    private void handleGenerateButtonClick() {
        try {
            int mazeSize = Integer.parseInt(sizeInput.getText());
            this.maze = new Labyrinthe(mazeSize);
            VBox.setMargin(mazeCanvas, new Insets(50, 0, 0, 50));
            playerRow = this.maze.getEntry().getRow();
            playerColumn = this.maze.getEntry().getColumn();
            drawMaze(this.maze);
            mazeCanvas.requestFocus();
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid integer for maze size.");
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED){
            switch (event.getCode()) {
                case W:
                    movePlayer(-1, 0);
                    break;
                case S:
                    movePlayer(1, 0);
                    break;
                case A:
                    movePlayer(0, -1);
                    break;
                case D:
                    movePlayer(0, 1);
                    break;
            }
        }
    }

    private void movePlayer(int rowChange, int columnChange) {
        int newRow = playerRow + rowChange;
        int newColumn = playerColumn + columnChange;
        if (isValidMove(newRow, newColumn, rowChange, columnChange)) {
            playerRow = newRow;
            playerColumn = newColumn;
            drawMaze(maze);
        }
    }

    private boolean isValidMove(int newRow, int newColumn, int rowChange, int columnChange) {
        if (newRow < 0 || newRow >= maze.getSize() || newColumn < 0 || newColumn >= maze.getSize()) {
            return false;
        }

        Cell currentCell = maze.getCell(playerRow, playerColumn);
        Cell newCell = maze.getCell(newRow, newColumn);

        // Check for walls based on movement direction
        if (rowChange == -1 && currentCell.hasTopWall() || rowChange == 1 && newCell.hasTopWall()) {
            return false;
        }
        return (columnChange != 1 || !currentCell.hasRightWall()) && (columnChange != -1 || !newCell.hasRightWall());
    }

    private void drawMaze(Labyrinthe maze) {
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
        double cellSize = calculateCellSize(maze);
        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        for (int i = 0; i < maze.getSize(); i++) {
            for (int j = 0; j < maze.getSize(); j++) {
                Cell cell = maze.getCell(i, j);
                drawCell(gc, cell, cellSize);

                if (i == playerRow && j == playerColumn) {
                    drawPlayer(gc, cellSize, i, j);
                }
            }
        }

        // Draw entry
        Cell entryCell = maze.getEntry();
        gc.setFill(Color.GREEN);
        gc.fillRect(entryCell.getColumn() * cellSize, entryCell.getRow() * cellSize, cellSize, cellSize);

        // Draw exit
        Cell exitCell = maze.getExit();
        gc.setFill(Color.RED);
        gc.fillRect(exitCell.getColumn() * cellSize, exitCell.getRow() * cellSize, cellSize, cellSize);
    }


    private void drawCell(GraphicsContext gc, Cell cell, double cellSize) {
        double x = cell.getColumn() * cellSize;
        double y = cell.getRow() * cellSize;
        gc.setLineWidth(2);

        if (cell.hasTopWall()) gc.strokeLine(x, y, x + cellSize, y);
        if (cell.hasRightWall()) gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize);
        if (cell.hasBottomWall()) gc.strokeLine(x, y + cellSize, x + cellSize, y + cellSize);
        if (cell.hasLeftWall()) gc.strokeLine(x, y, x, y + cellSize);
    }

    private void drawPlayer(GraphicsContext gc, double cellSize, int row, int col) {
        double x = col * cellSize;
        double y = row * cellSize;
        gc.setFill(Color.RED);
        gc.fillOval(x, y, cellSize, cellSize);
    }

    private double calculateCellSize(Labyrinthe maze) {
        double canvasSize = Math.min(mazeCanvas.getWidth(), mazeCanvas.getHeight());
        return canvasSize / maze.getSize();
    }
}
