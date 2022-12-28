package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Actor {
    private ArrayList<Item> inventory;

    public Player(Cell cell) {
        super(cell);
        inventory = new ArrayList<>();
        this.setHealth(15);
        this.setStr(3);
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
        if (this.getCell().getItem() != null) {
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

}
