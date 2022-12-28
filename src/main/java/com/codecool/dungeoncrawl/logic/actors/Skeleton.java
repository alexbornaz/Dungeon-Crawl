package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Actor {
    public Skeleton(Cell cell) {
        super(cell);
        this.setHealth(5);
        this.setStr(1);
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
