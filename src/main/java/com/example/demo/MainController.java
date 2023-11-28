package com.example.demo;

import com.example.demo.model.Cell;
import com.example.demo.model.Labyrinthe;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

public class MainController {

    @FXML
    private TextField sizeInput;

    @FXML
    private Button generateButton;

    @FXML
    private VBox container;

    @FXML
    private Canvas mazeCanvas;

    private double scaleValue = 1.0;
    private double scaleFactor = 0.1;

    private Labyrinthe maze;

    @FXML
    private void initialize() {
        // You can perform additional initialization here
//        mazeCanvas.setOnScroll(this::handleScroll);
    }



    @FXML
    private void handleGenerateButtonClick() {
        try {
            int mazeSize = Integer.parseInt(sizeInput.getText());
            this.maze = new Labyrinthe(mazeSize);
            this.container.setMargin(mazeCanvas, new Insets(50, 0, 0, 50));
            drawMaze(this.maze);
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid integer for maze size.");
        }
    }

    private void drawMaze(Labyrinthe maze) {
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
        double cellSize = calculateCellSize(maze);
        if(cellSize<=0){
            return;
        }

        double canvasSize = maze.getSize() * cellSize;
        mazeCanvas.setWidth(canvasSize);
        mazeCanvas.setHeight(canvasSize);

        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

//        mazeCanvas.getTransforms().add(new Scale(scaleValue, scaleValue));

        for (int i = 0; i < maze.getSize(); i++) {
            for (int j = 0; j < maze.getSize(); j++) {
                Cell cell = maze.getCell(i, j);
                gc.setLineWidth(1);


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


//                if (cell.getIsPath() && cell!=maze.getEntry() && cell!=maze.getExit()) {
//                    gc.setFill(Color.PALEGREEN);  // You can choose a color for the path
//                    gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
//                    gc.setFill(Color.BLACK);  // Reset the fill color
//                }




            }
        }
    }

    private double calculateCellSize(Labyrinthe maze) {
        double canvasWidth = mazeCanvas.getWidth();
        double canvasHeight = mazeCanvas.getHeight();
        int mazeSize = maze.getSize();

        // Ensure non-zero maze size to avoid division by zero
        if (mazeSize > 0) {
            // Choose the minimum of canvas width and height to ensure the maze fits within the canvas
            double minDimension = Math.min(canvasWidth, canvasHeight);

            // Calculate the cell size based on the chosen dimension
            return minDimension / mazeSize;
        } else {
            return 0.0;  // Default size if maze size is zero (or negative, though that should not happen)
        }
    }


//    private void handleScroll(ScrollEvent event) {
//        double delta = event.getDeltaY();
//        if(delta>0){
//            scaleValue+=scaleFactor;
//        }else scaleValue = Math.max(scaleValue - scaleFactor,0.1);
//
//        if (delta < 0 && scaleValue == 0.1) {
//            scaleValue = 1.0;
//        }
//        drawMaze(this.maze);
//    }
}
