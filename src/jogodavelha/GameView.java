/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogodavelha;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 *
 * @author Felipe Azevedo (milhouse)
 */

public class GameView {
    private GridPane grid;
    private TextArea outputArea;
    private TextArea scoreboardArea;
    
    private BorderPane topPane;
    private BorderPane rootPane;
    

    public GameView() {
        this.grid = new GridPane();
        this.topPane = new BorderPane();
        this.rootPane = new BorderPane();

        this.outputArea = new TextArea();
        this.scoreboardArea = new TextArea();
        
        this.outputArea.setDisable(true);
        
        configureGrid();
        configureRootPane();
        configureScoreBoardArea();
    }
    
    public GridPane getGrid() {
        return grid;
    }

    public void setGrid(GridPane grid) {
        this.grid = grid;
    }

    public BorderPane getTopPane() {
        return topPane;
    }

    public void setTopPane(BorderPane topPane) {
        this.topPane = topPane;
    }

    public BorderPane getRootPane() {
        return rootPane;
    }

    public void setRootPane(BorderPane rootPane) {
        this.rootPane = rootPane;
    }

    public TextArea getOutputArea() {
        return outputArea;
    }

    public void setOutputArea(TextArea outputArea) {
        this.outputArea = outputArea;
    }

    public TextArea getScoreboardArea() {
        return scoreboardArea;
    }

    public void setScoreboardArea(TextArea scoreboardArea) {
        this.scoreboardArea = scoreboardArea;
    }
    
    private void configureGrid() {
        this.grid.setAlignment(Pos.CENTER);
        this.grid.setHgap(10);
        this.grid.setVgap(10);
        
        this.grid.setPadding(new Insets(25, 25, 25, 25));
    }
    
    private void configureScoreBoardArea() {
        this.scoreboardArea.setPrefWidth(80);
        this.scoreboardArea.setMaxSize(70, 80);
        this.scoreboardArea.setFont(new Font("Calibri", 11));
        
        this.scoreboardArea.setDisable(true);
    }
    
    private void configureRootPane() {
        rootPane.setTop(this.topPane);
        rootPane.setCenter(this.grid);
        rootPane.setBottom(getOutputArea());
        rootPane.setRight(getScoreboardArea());

        rootPane.setPrefSize(400, 400);
        
        rootPane.setStyle("-fx-padding: 10;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;");
    }
    
    public void clearGrid() {
        this.grid.getChildren().clear();
    }
    
    public void addButtonOnTopPane(Button btn, String alignment) {
        switch (alignment.toLowerCase()) {
            case "center":
                this.topPane.setCenter(btn);
                this.topPane.setAlignment(btn, Pos.TOP_CENTER);
                break;
                    
            case "left":
                this.topPane.setLeft(btn);
                this.topPane.setAlignment(btn, Pos.TOP_LEFT);
                break;                
                
            case "right":
                this.topPane.setRight(btn);
                this.topPane.setAlignment(btn, Pos.TOP_RIGHT);
                break;
                
            default:
                break;
        }
    }
    
    public void addButtonOnGrid(Button btn, int posX, int posY) {
        this.grid.add(btn, posX, posY);
    }
    
    public void updateScoreboard(String text) {
        this.scoreboardArea.clear();
        this.scoreboardArea.appendText(text);
    }
    
    public void writeOutput(String outputText) {
        this.outputArea.appendText(outputText + "\n");
    }
    
    public static void showDialog(String outputText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Jogo da Velha");
        alert.setHeaderText(null);
        alert.setContentText(outputText);

        alert.showAndWait();
    }

}