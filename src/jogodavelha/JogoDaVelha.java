/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogodavelha;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Felipe Azevedo (milhouse)
 */

public class JogoDaVelha extends Application {
    private int actualPlayer = 0; // 0 == player1(X) || 1 == player2(O)

    public Player player1;
    public Player player2;
    
    public GameView gameView;
    public GameUtils gameUtils;
    public Button[][] velhaBtnList;
    public String[][] velhaStrList;
    
    public boolean resetPlayers = false;
    public boolean keyboardStatus = false;
    public boolean loadedGameFlag = false;
    public boolean continueGameFlag = false;
    public int countMove = 0;
    
    @Override
    public void start(Stage stage) {
        player1 = new Player(null, null);
        player2 = new Player(null, null);
        
        gameView = new GameView();
        gameUtils = new GameUtils();
        
        velhaBtnList = new Button[3][3];
        velhaStrList = null;
        
        gameView.getRootPane().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                saveGameEvent(event);
            }
        });
        
        addGameButtons();

        Scene scene = new Scene(gameView.getRootPane());
        stage.setScene(scene);
        stage.setTitle("Jogo da Velha");
        stage.show();
    }
    
    public void addGameButtons() {
        Button btnNewGame = new Button();
        Button btnExitGame = new Button();
        Button btnLoadGame = new Button();

        btnNewGame.setText("Novo Jogo");

        btnNewGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                countMove = 0;
                gameView.clearGrid();
                if (false == continueGameFlag) {
                    if (false == loadedGameFlag) {
                        if (false == startPlayers()) {
                            return;
                        }
                    }
                }

                updateScoreboard();
                selectFirstPlayer();
                
                addVelhaButtons();
                loadedGameFlag = false;
            }
        });

        btnLoadGame.setText("Carregar Jogo");
        btnLoadGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameView.clearGrid();
                loadGameEvent();
                addVelhaButtons();
            }
        });

        btnExitGame.setText("Fechar Jogo");
        btnExitGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        btnNewGame.setPadding(new Insets(5, 5, 5, 5));
        gameView.addButtonOnTopPane(btnNewGame, "left");
        
        btnLoadGame.setPadding(new Insets(5, 5, 5, 5));
        gameView.addButtonOnTopPane(btnLoadGame, "center");

        btnExitGame.setPadding(new Insets(5, 5, 5, 5));
        gameView.addButtonOnTopPane(btnExitGame, "right");
    }
    
    public void addVelhaButtons() {
        int x;
        int y;

        for (x = 0; x < 3; x++) { // linha
            for (y = 0; y < 3; y++) { // coluna
                Button btn = new Button();
                velhaBtnList[x][y] = btn;
                btn.setFont(new Font("Calibri", 20));
                if (null != velhaStrList) {
                    btn.setText(velhaStrList[x][y]);
                    if (velhaStrList[x][y].equals("X") || velhaStrList[x][y].equals("O")) {
                        btn.setDisable(true);
                    }
                } else {
                    btn.setText("   ");
                }
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Player actualPlayer = getActualPlayer();
                        btn.setText(actualPlayer.getGameVariable());
                        btn.setDisable(true);
                        checkWinner(velhaBtnList, actualPlayer);
                        countMove++;
                        changePlayer();
                    }
                });
                
                btn.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        saveGameEvent(event);
                    }
                });
                
                gameView.addButtonOnGrid(btn, x, y);
                
            }
        }
        velhaStrList = null;
    }
    
    // Checks
    public void checkWinner(Button[][] velhaBtnList, Player actualPlayer) {
        String playerGameVariable = actualPlayer.getGameVariable();

        if ((velhaBtnList[0][0].getText().equals(playerGameVariable) &&
            velhaBtnList[1][0].getText().equals(playerGameVariable) &&
            velhaBtnList[2][0].getText().equals(playerGameVariable)) ||

            (velhaBtnList[0][1].getText().equals(playerGameVariable) &&
            velhaBtnList[1][1].getText().equals(playerGameVariable) &&
            velhaBtnList[2][1].getText().equals(playerGameVariable)) ||

            (velhaBtnList[0][2].getText().equals(playerGameVariable) &&
            velhaBtnList[1][2].getText().equals(playerGameVariable) &&
            velhaBtnList[2][2].getText().equals(playerGameVariable)) ||

            (velhaBtnList[1][0].getText().equals(playerGameVariable) &&
            velhaBtnList[1][1].getText().equals(playerGameVariable) &&
            velhaBtnList[1][2].getText().equals(playerGameVariable)) ||

            (velhaBtnList[2][0].getText().equals(playerGameVariable) &&
            velhaBtnList[2][1].getText().equals(playerGameVariable) &&
            velhaBtnList[2][2].getText().equals(playerGameVariable)) ||

            (velhaBtnList[0][0].getText().equals(playerGameVariable) &&
            velhaBtnList[0][1].getText().equals(playerGameVariable) &&
            velhaBtnList[0][2].getText().equals(playerGameVariable)) ||

            // Validar \
            (velhaBtnList[0][0].getText().equals(playerGameVariable) &&
            velhaBtnList[1][1].getText().equals(playerGameVariable) &&
            velhaBtnList[2][2].getText().equals(playerGameVariable)) ||

            // Validar /
            (velhaBtnList[2][0].getText().equals(playerGameVariable) &&
            velhaBtnList[1][1].getText().equals(playerGameVariable) &&
            velhaBtnList[0][2].getText().equals(playerGameVariable))) {
                actualPlayer.addPoint();
                gameView.writeOutput("===== O JOGADOR '" + actualPlayer.getName() + "' VENCEU! =====");
                gameView.showDialog("===== O JOGADOR '" + actualPlayer.getName() + "' VENCEU! =====");
                
                updateScoreboard();
                disableVelhaBtnList(velhaBtnList);
                checkContinue();
        } else {
            if (8 == countMove) {
                gameView.writeOutput("===== DEU VELHA =====");
                gameView.showDialog("===== DEU VELHA =====");
                
                checkContinue();
            }
        }
    }
    
    public void checkContinue() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Jogo da Velha");
        alert.setContentText("Deseja continuar jogando com esses players?");

        ButtonType btnNo = new ButtonType("Não", ButtonData.NO);
        ButtonType btnYes = new ButtonType("Sim", ButtonData.YES);

        alert.getButtonTypes().setAll(btnYes, btnNo);

        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == btnYes) {
            continueGameFlag = true;
        } else {
            resetPlayers = true;
            continueGameFlag = false;
        }
    }
    
    // Players
    public void selectFirstPlayer() {
        double random = (int)(Math.random()*((0-10)+1))+0;
        actualPlayer = (0 == random % 2) ? 1 : 0;
        gameView.showDialog("O jogador'" + getActualPlayer().getName() + "' iniciará a partida!");
        gameView.writeOutput("O jogador '" + getActualPlayer().getName() + "' iniciará a partida!");
    }
    
    public void changePlayer() {
        if (0 == this.actualPlayer) { this.actualPlayer = 1; }
        else { this.actualPlayer = 0; }
    }

    public Player getActualPlayer() {
        if (0 == this.actualPlayer) { return player1; }
        else { return player2; }
    }
    
    public boolean startPlayers() {
        String playerName;
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Jogo da Velha");
        dialog.setContentText("Insira o nome do Player 1:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            playerName = (result.get().isEmpty()) ? "Player 1" : result.get();

            player1.setPoints(0);
            player1.setName(playerName);
            player1.setGameVariable("X");
            gameView.writeOutput("O jogador '" + player1.getName() +
                    "' usará o simbolo '" + player1.getGameVariable() + "';");
        } else {
            return false;
        }

        dialog.setContentText("Insira o nome do Player 2:");

        result = dialog.showAndWait();

        if (result.isPresent()) {
            playerName = (result.get().isEmpty()) ? "Player 2" : result.get();

            player2.setPoints(0);
            player2.setName(playerName);
            player2.setGameVariable("O");
            gameView.writeOutput("O jogador '" + player2.getName() +
                    "' usará o simbolo '" + player2.getGameVariable() + "';");
        } else {
            return false;
        }

        return true;
    }

    public String getFileNameToSave() {
        String fileName;
        
        LocalDateTime now = LocalDateTime.now();   
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");  
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Jogo da Velha");
        dialog.setContentText("Insira o nome do arquivo para salvar:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            fileName = (result.get().isEmpty()) ? dtf.format(now) + ".jv" : result.get() + ".jv";
        } else {
            return null;
        }
        
        return fileName;
    }
    
    
    // Keyboard Events
    public void saveGameEvent(KeyEvent event) {
        switch (event.getCode()) {
            case CONTROL:
                gameView.writeOutput("Key 'control' pressed. Press 'S' to save.");
                keyboardStatus = true;
                break;

            case S:
                if (true == keyboardStatus) {
                    gameView.writeOutput("Key 'S' pressed");
                    String fileName = getFileNameToSave();
                    saveGame(fileName, false);
                    keyboardStatus = false;
                }
                break;

            default:
                keyboardStatus = false;
        }
    }
    
    public void loadGameEvent() {
        ArrayList<String> savedGames = new ArrayList<>();
        
        savedGames = gameUtils.listGames();
        
        if (0 < savedGames.size()) {
        
            ChoiceDialog<String> dialog = new ChoiceDialog<>("", savedGames);

            dialog.setTitle("Jogo da Velha");
            dialog.setContentText("Selecione um jogo salvo:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (!result.get().isEmpty()) {
                    gameUtils = gameUtils.loadGame(result.get());
                    gameUtils.setActualGameLoaded(result.get());

                    ArrayList<Player> players = gameUtils.getPlayers();
                    player1 = players.get(0);
                    player2 = players.get(1);

                    updateScoreboard();
                    loadedGameFlag = true;
                    countMove = gameUtils.getCountMove();
                    actualPlayer = gameUtils.getLastPlayer();
                    velhaStrList = gameUtils.getVelhaStrList();

                    gameView.writeOutput("Jogo carregado: " + result.get());
                }
            } else {
                gameView.writeOutput("Carregar jogo cancelado.");
            }
        } else { 
            gameView.writeOutput("Nenhum jogo salvo para ser carregado.");
        }
    }
    
    
    // Others
    public void updateScoreboard() {
        String text = player1.getName() + ": " + player1.getPoints() + "\n" +
                        player2.getName() + ": " + player2.getPoints();
        gameView.updateScoreboard(text);
    }
    
    public void saveGame(String fileName, boolean forceFlag) {
        if (null != fileName) {
            gameUtils.addPlayer(player1);
            gameUtils.addPlayer(player2);
            gameUtils.setLastPlayer(actualPlayer);
            gameUtils.setVelhaStrList(changeVelhaBtnListToVelhaStrList());

            short status = gameUtils.saveGame(fileName, forceFlag);
            switch (status) {
                case 0: 
                    gameView.showDialog("Jogo salvo com sucesso!");
                    gameView.writeOutput("Jogo salvo com sucesso!");
                    break;
                case 1:
                    confirmFileName(fileName);
                    break;

                case 2:
                    gameView.showDialog("Ocorreu um erro ao salvar o arquivo.");
                    gameView.writeOutput("Ocorreu um erro ao salvar o arquivo.");
                    break;
                default:
                    break;
            }
        } else {
            gameView.writeOutput("Salvar jogo cancelado.");
        }
    }
    
    public void verifySavedGames() {
        ArrayList<String> savedGames = gameUtils.listGames();
        if (0 != savedGames.size()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Jogo da Velha");
            alert.setContentText("Existem jogos salvos. Deseja carregar algum?");

            ButtonType btnNo = new ButtonType("Não", ButtonData.NO);
            ButtonType btnYes = new ButtonType("Sim", ButtonData.YES);

            alert.getButtonTypes().setAll(btnYes, btnNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == btnYes) {
                loadGameEvent();
            } else {
                return;
            }
        }
    }
    
    public void confirmFileName(String fileName) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Jogo da Velha");
        alert.setContentText("O arquivo com nome '" + fileName + "'já existe. Deseja sobrepor?");

        ButtonType btnNo = new ButtonType("Não", ButtonData.NO);
        ButtonType btnYes = new ButtonType("Sim", ButtonData.YES);

        alert.getButtonTypes().setAll(btnYes, btnNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            saveGame(fileName, true);
        } else {
            saveGame(fileName, false);
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }  
    
    public void disableVelhaBtnList(Button[][] velhaBtnList) {
        short x;
        short y;
        for (x = 0; x < 3; x++) { // linha
            for (y = 0; y < 3; y++) { // coluna
                velhaBtnList[x][y].setDisable(true);
            }
        }
    }
    
    public String[][] changeVelhaBtnListToVelhaStrList() {
        short x;
        short y;
        String[][] velhaStrList = new String[3][3];
        for (x = 0; x < 3; x++) { // linha
            for (y = 0; y < 3; y++) { // coluna
                velhaStrList[x][y] = velhaBtnList[x][y].getText();
            }
        }
        
        return velhaStrList;
    }   
}
