package com.codecool.dungeoncrawl.logic.items.door;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.items.Item;

public abstract class Door  extends Item implements Drawable {
    public Door(Cell cell) {
        super(cell);
    }
}
