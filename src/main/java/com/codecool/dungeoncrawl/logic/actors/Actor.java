package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Actor implements Drawable {
    private Cell cell;

    private int health;

    private int str;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getType() == CellType.FLOOR) {
            if (nextCell.getActor() == null){
                cell.setActor(null);
                nextCell.setActor(this);
                cell = nextCell;
            }else {
                attack(nextCell);
            }
        }
    }

    public void attack(Cell nextCell) {
        Actor monster = nextCell.getActor();
        System.out.println(this.getStr());
        monster.setHealth(monster.getHealth()-this.getStr());
        if (monster.getHealth() > 0){
            this.setHealth(this.getHealth()- monster.getStr());
            if (this.getHealth() <= 0 ){
                System.out.println("You died!");
            }
        }else {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
