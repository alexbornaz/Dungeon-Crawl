package com.codecool.dungeoncrawl.logic.items.door;

import com.codecool.dungeoncrawl.logic.Cell;

public class OpenDoor extends Door {
    public OpenDoor(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "openDoor";
    }
}
