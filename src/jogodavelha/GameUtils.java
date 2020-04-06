/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogodavelha;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Felipe Azevedo (milhouse)
 */

public class GameUtils implements Serializable {
    private ArrayList<Player> players;
    private int countMove;
    private int lastPlayer;
    private String actualGameLoaded;
    private String defaultSaveGameDirectory;
    private String[][] velhaStrList;
    
    public GameUtils() {
        this.players = new ArrayList<Player>();
        this.actualGameLoaded = "";
        this.defaultSaveGameDirectory = "./SavedGames";
        
        File file = new File(this.defaultSaveGameDirectory);
        
        if (!file.exists()) {
            file.mkdir();
        }
    }
    
    public String[][] getVelhaStrList() {
        return velhaStrList;
    }

    public void setVelhaStrList(String[][] velhaBtnList) {
        this.velhaStrList = velhaBtnList;
    }

    public int getLastPlayer() {
        return lastPlayer;
    }

    public int getCountMove() {
        return countMove;
    }

    public void setCountMove(int countMove) {
        this.countMove = countMove;
    }

    public void setLastPlayer(int lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public String getActualGameLoaded() {
        return actualGameLoaded;
    }

    public void setActualGameLoaded(String actualGameLoaded) {
        this.actualGameLoaded = actualGameLoaded;
    }

    public String getDefaultSaveGameDirectory() {
        return defaultSaveGameDirectory;
    }

    public void setDefaultSaveGameDirectory(String defaultSaveGameDirectory) {
        this.defaultSaveGameDirectory = defaultSaveGameDirectory;
    }
    
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "VariablesToSave{" + "players=" + players + '}';
    }
    
    public ArrayList<String> listGames() {
        
        ArrayList<String> savedGames = new ArrayList<String>();
        
        try {
            File directory = new File(this.defaultSaveGameDirectory);
            
            if (directory.exists()) {
                File[] files = directory.listFiles();
                if (null != files) {
                    for (File file : files) {
                        if (!file.isDirectory()) {
                            savedGames.add(file.getName());
                        }
                    }
                }
            }
        } catch (final Exception err) {
            System.out.println(err);
        }
        
        return savedGames;
    }
    
    public GameUtils loadGame(String fileName) {
        GameUtils mGameUtils = null;
        
        // Passivel de Path Travessal
        String filePath = this.defaultSaveGameDirectory + "/" + fileName;
        
        try {
            File file = new File(filePath);
            
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objInputStream = new ObjectInputStream(fileInputStream);

                while (true) {
                    mGameUtils = (GameUtils) objInputStream.readObject();
                    this.players = mGameUtils.getPlayers();
                    this.countMove = mGameUtils.getCountMove();
                    this.lastPlayer = mGameUtils.getLastPlayer();
                    this.velhaStrList = mGameUtils.getVelhaStrList();
                }
            }

        } catch (EOFException eoferr) {
            System.out.println("OK");
        }
        catch (IOException | ClassNotFoundException err) {
            GameView.showDialog("Erro ao tentar carregar o jogo salvo.");
        }
        
        return mGameUtils;
    }
    
    private String simplePreventPathTraversal(String fileName) {
        if (fileName.contains("/")) {
            String[] tmp = fileName.split("/");
            fileName = tmp[tmp.length - 1];
        }
        return fileName;
    }
    
    public short saveGame(String fileName, boolean forces) {
        ArrayList<String> savedGames;
        
        fileName = simplePreventPathTraversal(fileName);
        
        savedGames = listGames();
        
        if (savedGames.contains(fileName) && false == forces) {
            return 1;
        } else {
            String filePath = this.defaultSaveGameDirectory + "/" + fileName;

            try {
                File saveFile = new File(filePath);

                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                }

                FileOutputStream fileOutStream = new FileOutputStream(saveFile);
                ObjectOutputStream objOutStream = new ObjectOutputStream(fileOutStream);
                objOutStream.writeObject(this);

                objOutStream.close();
                fileOutStream.close();

                return 0;
            } catch (Exception err) {
                GameView.showDialog("Erro ao tentar salvar o jogo");

                return 2;
            }
        }
    }
}