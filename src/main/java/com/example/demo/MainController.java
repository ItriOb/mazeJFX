package com.example.demo;

import com.example.demo.model.Cell;
import com.example.demo.model.Labyrinthe;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class MainController {

    @FXML
    private ScrollPane scrollPane;

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

    private int playerRow;
    private int playerColumn;

    private Labyrinthe maze;

    @FXML
    private void initialize() {
        // You can perform additional initialization here
//        mazeCanvas.setOnScroll(this::handleScroll);

        mazeCanvas.setOnKeyReleased(event -> handleKeyPress(event));
    }



    @FXML
    private void handleGenerateButtonClick() {
        try {
            int mazeSize = Integer.parseInt(sizeInput.getText());
            this.maze = new Labyrinthe(mazeSize);
            this.container.setMargin(mazeCanvas, new Insets(50, 0, 0, 50));
            playerRow=  this.maze.getEntry().getX();
            System.out.println("This is player row : "+this.maze.getEntry().getRow());
            playerColumn = this.maze.getEntry().getY();
            System.out.println("This is player column : "+this.maze.getEntry().getColumn());
//            maze.bfs();
            drawMaze(this.maze);
            mazeCanvas.requestFocus();
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid integer for maze size.");
        }
    }



    @FXML
    private void handleKeyPress(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED){

            //D left bottom wall
            //W up left wall
            //S down right wall
            //A right top wall


        KeyCode keyCode = event.getCode();
        switch (keyCode) {
            case W:
                System.out.println("up");
                movePlayer(0, -1);  // Move up, wall index for top wall
                break;
            case S:
                System.out.println("down");
                movePlayer(0, 1);   // Move down, wall index for bottom wall
                break;
            case A:
                System.out.println("left");
                movePlayer(-1, 0);  // Move left, wall index for left wall
                break;
            case D:
                System.out.println("right");
                movePlayer(1, 0);   // Move right, wall index for right wall
                break;
        }
        }
    }

    private void movePlayer(int rowChange, int columnChange) {
        System.out.println("initial row : "+playerRow+" initial column : "+playerColumn);
        int newRow = playerRow + rowChange;
        int newColumn = playerColumn + columnChange;



        int wallIndex =0;
        if(rowChange==-1 && columnChange==0){
            wallIndex = 0;
        }else if (rowChange==0&&columnChange==1){
            wallIndex = 3;
        }else if(rowChange ==1&& columnChange==0) {
            wallIndex = 1;
        }else if(rowChange==0&&columnChange==-1){
            wallIndex = 2;
        }

        System.out.println("this is new Row : "+newRow+" this is new Column : "+newColumn+" and this is wall index : " + wallIndex);


        if (isValidMove(newRow, newColumn,wallIndex)) {
            // Update player position
            playerRow = newRow;
            playerColumn = newColumn;
            System.out.println("This is row : "+playerRow+" This is column : "+playerColumn);



            // Redraw the maze
            drawMaze(maze);
        }
    }

    private boolean isValidMove(int row, int column, int wallIndex) {


        System.out.println("is ValidMove row : "+row+" is ValidMove column : "+column);

        if(row >=0 && column>=0){
            System.out.println("this cell is row : "+maze.getCell(column,row).getRow()+" this is column : "+maze.getCell(column,row).getColumn()+ " and wall index : "+wallIndex);

        }

        if(row >= 0 && row < maze.getSize() &&
                column >= 0 && column < maze.getSize() &&
                !maze.getCell(column, row).getWall(wallIndex)){
            System.out.println("this wall : "+wallIndex);
            return true;
        }else return false;
    }


    private void drawMaze(Labyrinthe maze) {
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
        double cellSize = calculateCellSize(maze);
        if(cellSize<=0){
            return;
        }

        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        double canvasSize = maze.getSize() * cellSize;
        mazeCanvas.setWidth(canvasSize);
        mazeCanvas.setHeight(canvasSize);

        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());


//        mazeCanvas.getTransforms().add(new Scale(scaleValue, scaleValue));



        for (int i = 0; i < maze.getSize(); i++) {
            for (int j = 0; j < maze.getSize(); j++) {
                Cell cell = maze.getCell(i, j);

                gc.setLineWidth(1);

                double x = cell.getX() * cellSize;
                double y = cell.getY() * cellSize;


                if (cell.hasTopWall()) {
                    gc.strokeLine(x, y, x + cellSize, y);
                }

                if (cell.hasRightWall()) {
                    gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize);
                }

                if (cell.hasBottomWall()) {
                    gc.strokeLine(x, y + cellSize, x + cellSize, y + cellSize);
                }

                if (cell.hasLeftWall()) {
                    gc.strokeLine(x, y, x, y + cellSize);
                }

//                if (cell.isPath()) {
//                    gc.setFill(javafx.scene.paint.Color.BLUE);
//                    gc.fillRect(x, y, cellSize, cellSize);
//                }
//
//
//                if (cell.getWall(0)) {
//                    gc.strokeLine(i * cellSize, j * cellSize, (i + 1) * cellSize, j * cellSize);
//                }
//
//                if (cell.getWall(1)) {
//                    gc.strokeLine((i + 1) * cellSize, j * cellSize, (i + 1) * cellSize, (j + 1) * cellSize);
//                }
//
//                if (cell.getWall(2)) {
//                    gc.strokeLine(i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, (j + 1) * cellSize);
//                }
//
//
//                if (cell.getWall(3)) {
//                    gc.strokeLine(i * cellSize, j * cellSize, i* cellSize, (j + 1) * cellSize);
//                }

                if (i == playerRow&& j == playerColumn) {
                    gc.setFill(Color.RED);  // You can choose a color for the player marker
                    gc.fillOval(i* cellSize, j * cellSize, cellSize, cellSize);
                    gc.setFill(Color.BLACK);  // Reset the fill color
                }

                if ( playerRow == maze.getExit().getRow() && playerColumn==maze.getExit().getColumn() && i == playerRow && j == playerColumn) {
                    gc.setFill(Color.GREEN);  // You can choose a color for the player marker
                    gc.fillOval(i * cellSize, j * cellSize, cellSize, cellSize);
                    gc.setFill(Color.BLACK);  // Reset the fill color
                }


//                if (cell.isPathBfs()){
//                    gc.setFill(Color.LIGHTBLUE);  // You can choose a color for the path
//                    gc.fillOval(i * cellSize, j * cellSize, cellSize, cellSize);
//                    gc.setFill(Color.BLACK);  // Reset the fill color
//                }



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

    private void solveRightHand(){
        Cell currentCell = this.maze.getEntry();
        String direction = "";
        String[] directions= {"north","est","south","west"};
        while(currentCell!=this.maze.getExit()){
            if(direction=="north"){

            }
        }
    }




    //give me a method that solves this algorithm using right hand rule





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
