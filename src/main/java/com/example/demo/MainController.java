package com.example.demo;

import com.example.demo.model.Cell;
import com.example.demo.model.Labyrinthe;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainController {

    @FXML
    private TextField sizeInput;

    @FXML
    private Button generateButton;

    @FXML
    private VBox container;

    @FXML
    private Canvas mazeCanvas;

    @FXML
    private void initialize() {
        // You can perform additional initialization here
    }

    @FXML
    private void handleGenerateButtonClick() {
        try {
            int mazeSize = Integer.parseInt(sizeInput.getText());
            Labyrinthe maze = new Labyrinthe(mazeSize);
            drawMaze(maze);
            this.container.setMargin(mazeCanvas, new Insets(0, 0, 0, 50));
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid integer for maze size.");
        }
    }

    private void drawMaze(Labyrinthe maze) {
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
        double cellSize = mazeCanvas.getWidth() / maze.getSize();

        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        for (int i = 0; i < maze.getSize(); i++) {
            for (int j = 0; j < maze.getSize(); j++) {
                Cell cell = maze.getCell(i, j);

                if (cell.getWall(0)) {
                    gc.strokeLine(i * cellSize, j * cellSize, (i + 1) * cellSize, j * cellSize);
                }
                if (cell.getWall(1)) {
                    gc.strokeLine((i + 1) * cellSize, j * cellSize, (i + 1) * cellSize, (j + 1) * cellSize);
                }
                if (cell.getWall(2)) {
                    gc.strokeLine(i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, (j + 1) * cellSize);
                }
                if (cell.getWall(3)) {
                    gc.strokeLine(i * cellSize, j * cellSize, i* cellSize, (j + 1) * cellSize);
                }
            }
        }
    }
}
