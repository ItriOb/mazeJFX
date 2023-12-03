package com.example.demo;

import com.example.demo.model.Cell;
import com.example.demo.model.Labyrinthe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField sizeInput;

    @FXML
    private Button generateButton;

    @FXML
    private VBox container;

    @FXML private VBox wrapper;

    @FXML
    private Canvas mazeCanvas;

    private int playerRow;
    private int playerColumn;

    private Labyrinthe maze;
    private boolean showPlayer = false;

    @FXML
    private void initialize() {
        mazeCanvas.setOnKeyReleased(this::handleKeyPress);

    }

    @FXML
    private void handleGenerateButtonClick() throws IOException {


        showPlayer=false;
        try {
            int mazeSize = Integer.parseInt(sizeInput.getText());
            this.maze = new Labyrinthe(mazeSize);
            VBox.setMargin(mazeCanvas, new Insets(50, 0, 0, 50));
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
    private void handleDrawBFSPath() {
        showPlayer=false;


        if (maze != null) {

            List<Cell> path = maze.performBFS(); // Assuming performBFS returns the path from entry to exit
            drawPath(path);
        }
    }
    @FXML
    private void handleJouer() {


        showPlayer=true;
        drawMaze(this.maze);
    }

    private void drawPath(List<Cell> path) {
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
        double cellSize = calculateCellSize(maze);
        double pathWidth = cellSize / 3; // Set the path to be one-third the cell size
        double offset = (cellSize - pathWidth) / 2; // Offset to center the path within the cell

        for (Cell cell : path) {
            double x = cell.getColumn() * cellSize + offset;
            double y = cell.getRow() * cellSize + offset;
            gc.setFill(Color.BLUE); // Color for the BFS path
            gc.fillRect(x, y, pathWidth, pathWidth); // Draw a smaller square for the path
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

                if (i == playerRow && j == playerColumn && showPlayer){
                    drawPlayer(gc, cellSize, i, j);
                }
            }
        }

        // Draw entry
        Cell entryCell = maze.getEntry();
//        gc.setFill(Color.GREEN);
//        gc.fillRect(entryCell.getColumn() * cellSize, entryCell.getRow() * cellSize, cellSize, cellSize);

        // Draw exit
        Cell exitCell = maze.getExit();
//        gc.setFill(Color.RED);
//        gc.fillRect(exitCell.getColumn() * cellSize, exitCell.getRow() * cellSize, cellSize, cellSize);
    }


    private void drawCell(GraphicsContext gc, Cell cell, double cellSize) {
        double x = cell.getColumn() * cellSize;
        double y = cell.getRow() * cellSize;
        gc.setLineWidth(2);

        if (cell.hasTopWall()) gc.strokeLine(x, y, x + cellSize, y);
        if (cell.hasRightWall() && cell!=maze.getExit()){
            gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize);
        }else if(cell==maze.getExit()){
            gc.setStroke(Color.RED);
            gc.setLineWidth(5);
            gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize);
            gc.setLineWidth(2);
            gc.setStroke(Color.BLACK);
        }
        if (cell.hasBottomWall()) gc.strokeLine(x, y + cellSize, x + cellSize, y + cellSize);
        if (cell.hasLeftWall() && cell!=maze.getEntry()){
            gc.strokeLine(x, y, x, y + cellSize);
        }else if(cell==maze.getEntry()){
            gc.setStroke(Color.GREEN);
            gc.setLineWidth(5);
            gc.strokeLine(x, y, x, y + cellSize);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
        }
    }

    private void drawPlayer(GraphicsContext gc, double cellSize, int row, int col) {
        double x = col * cellSize;
        double y = row * cellSize;
        if (row == maze.getExit().getRow() && col == maze.getExit().getColumn()){
            gc.setFill(Color.GREEN);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Félicitations");
            alert.setHeaderText(null); // No header
            alert.setContentText("Vous avez trouvé le chemin de sortie !");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/com/example/demo/style/css/style.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");

            alert.showAndWait();
        }else gc.setFill(Color.RED);

        gc.fillOval(x, y, cellSize, cellSize);
    }

    private double calculateCellSize(Labyrinthe maze) {
        double canvasSize = Math.min(mazeCanvas.getWidth(), mazeCanvas.getHeight());
        return canvasSize / maze.getSize();

    }
}
