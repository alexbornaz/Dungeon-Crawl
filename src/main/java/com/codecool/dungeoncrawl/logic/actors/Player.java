package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.items.door.Door;
import com.codecool.dungeoncrawl.logic.items.door.OpenDoor;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Actor {
    private ArrayList<Item> inventory;

    private int onMap= 1;

    private boolean changeMap = false;

    public Player(Cell cell) {
        super(cell);
        inventory = new ArrayList<>();
        this.setHealth(15);
        this.setStr(3);
    }

    public boolean isDead(){
        return this.getHealth() <= 0;
    }


    public String getTileName() {
        return "player";
    }

    public void addToInventory(Item item) {
        inventory.add(item);
    }

    public void removeFromInventory(Item item) {
        inventory.remove(item);
    }

    public ArrayList getInventory() {
        return inventory;
    }

    public void pickUpItem() {
        if (this.getCell().getItem() != null && !(this.getCell().getItem() instanceof Door)) {
            addToInventory(this.getCell().getItem());
            if (this.getCell().getItem() instanceof Sword){
                this.setStr(this.getStr()+5);
            }
            this.getCell().setItem(null);
        }

    }

    public String showInventory() {
        StringBuilder display = new StringBuilder();
        int keyCount = 0;
        int swordCount = 0;
        HashMap<String, Integer> inventory_dict = new HashMap<String, Integer>();
        for(Item item : inventory){
            if(item instanceof Key){
                keyCount+=1;
                if(keyCount <= 1){
                    inventory_dict.put(item.getTileName(), keyCount);
                }else{
                    inventory_dict.put("key", keyCount);
                }

            } else if (item instanceof Sword){
                inventory_dict.put(item.getTileName(), swordCount+=1);
            }
        }
        for(HashMap.Entry<String, Integer> element: inventory_dict.entrySet()){
            display.append(element.getKey()+": "+ element.getValue());
            display.append("\n");
        }


        return display.toString();
    }

    public void move(int dx, int dy) {
        Cell cell = getCell();
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if (nextCell.getType() == CellType.STAIRS) {
            setChangeMap(true);
        }
        if (nextCell.getType() == CellType.FLOOR ) {
            if (nextCell.getActor() == null) {
                cell.setActor(null);
                nextCell.setActor(this);
                setCell(nextCell);
            }else {
                attack(nextCell);
            }
        } else if (nextCell.getType() == CellType.CLOSED_DOOR && nextCell.getActor() == null) {
            int counter = 0;
            for (Item item : inventory) {
                counter += 1;
                if (item instanceof Key) {
                    removeFromInventory(item);
                    cell.setActor(null);
                    nextCell.setType(CellType.FLOOR);
                    nextCell.setActor(this);
                    nextCell.setItem(new OpenDoor(nextCell));
                    setCell(nextCell);
                    break;
                } else if (inventory.size() == counter) {
                    System.out.println("you don't have a key!");
                }
            }
        }
    }

    private void attack(Cell nextCell) {
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

    public boolean getChangeMap() {
        return changeMap;
    }

    public void setChangeMap(boolean changeMap) {
        this.changeMap = changeMap;
    }

    public int getOnMap() {
        return onMap;
    }

    public void setOnMap(int onMap) {
        this.onMap = onMap;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }
}
