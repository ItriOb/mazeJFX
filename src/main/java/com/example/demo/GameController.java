package com.example.demo;

import com.example.demo.model.Labyrinthe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {

    @FXML
    private StackPane stackPane;

    @FXML
    private ImageView topLeftImage;

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
//        mazeCanvas.setOnKeyReleased(this::handleKeyPress);


    }

    @FXML
    private void handleStartGameButtonClick() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("labyrinthe.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Generate Labyrinth");
        stage.setScene(new Scene(root));
        stage.show();


    }


}
