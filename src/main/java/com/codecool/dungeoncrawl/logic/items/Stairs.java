package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Stairs extends Item{
    public Stairs(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "stairs";
    }
}
