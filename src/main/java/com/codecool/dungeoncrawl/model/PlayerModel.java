package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;

import java.util.ArrayList;

public class PlayerModel extends BaseModel {
    private String playerName;
    private int hp;
    private int x;
    private int y;
    private int attack_strength;
    private int sword;
    private int key;

    public PlayerModel(String playerName, int x, int y) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
    }

    public PlayerModel(Player player) {
        this.playerName = player.getName();
        this.x = player.getX();
        this.y = player.getY();

        this.hp = player.getHealth();
        this.attack_strength = player.getStr();
        ArrayList<Item> inventory = player.getInventory();
        this.sword = (int) inventory.stream().filter(item -> item instanceof Sword).count();
        this.key = (int) inventory.stream().filter(item -> item instanceof Key).count();

    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getAttack_strength() {
        return attack_strength;
    }

    public void setAttack_strength(int attack_strength) {
        this.attack_strength = attack_strength;
    }

    public int getSword() {
        return sword;
    }

    public void setSword(int sword) {
        this.sword = sword;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}

