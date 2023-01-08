package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.ArrayList;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;
    Cell centerCell;

    private Player player;
    private ArrayList<Actor> monsters = new ArrayList<>();

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public void addMonsterToMap(Actor monster) {
        monsters.add(monster);
    }
    public void removeDeadMonsters(){
        for (Actor monster: monsters) {
            if (monster.getHealth()<=0) {
                monsters.remove(monster);
            }
        }
    }
    public void repositionCenter(){
        int centerX;
        int centerY;

        if (player.getCell().getX() <= 10) {
            centerX = 10;
        } else {
            centerX = Math.min(player.getCell().getX(), width - 10);
        }

        if (player.getCell().getY() <= 10) {
            centerY = 10;
        } else {
            centerY = Math.min(player.getCell().getY(), height - 10);
        }

        centerCell = cells[centerX][centerY];
    }

    public Cell getCenterCell() {
        return centerCell;
    }
    public ArrayList<Actor> getMonsters() {
        return monsters;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
        repositionCenter();
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
