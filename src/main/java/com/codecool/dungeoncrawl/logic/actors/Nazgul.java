package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Nazgul extends Actor{
    private String moveDirection = "right";
    public Nazgul(Cell cell) {
        super(cell);
        this.setHealth(25);
        this.setStr(8);
    }
    public void move(){
        int width = this.getCell().getGameMap().getWidth();
        int currentX = this.getCell().getX();
        if (currentX == width-1) {
            moveDirection="left";
        } else if (currentX == 0) {
            moveDirection = "right";
        }
        Cell nextCell;
        while (true) {
            if (moveDirection == "left"){
                nextCell = this.getCell().getNeighbor(-2, 0);
            }
            else {
                nextCell = this.getCell().getNeighbor(2, 0);
            }
            if (nextCell.getActor() == null) {
                this.getCell().setActor(null);
                nextCell.setActor(this);
                setCell(nextCell);
                break;
            }
        }
    }

    @Override
    public String getTileName() {
        return "nazgul";
    }
}
