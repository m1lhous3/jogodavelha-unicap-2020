/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogodavelha;

import java.io.Serializable;

/**
 *
 * @author Felipe Azevedo (milhouse)
 */

class Player implements Serializable {

    private int points;
    private String name;
    private String gameVariable;

    public Player(String name, String gameVariable) {
        this.points = 0;

        this.name = name;
        this.gameVariable = gameVariable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameVariable() {
        return gameVariable;
    }

    public void setGameVariable(String gameVariable) {
        this.gameVariable = gameVariable;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoint() {
        this.points++;
    }

    @Override
    public String toString() {
        return "Player{" + "points=" + points + ", name=" + name + ", gameVariable=" + gameVariable + '}';
    }
}