package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Nazgul;
import com.codecool.dungeoncrawl.logic.actors.Ork;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;

public class MapSaver {
    public static String saveMap(GameMap gameMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(gameMap.getWidth()).append(" ").append(gameMap.getHeight()).append("\n");
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                Cell cell = gameMap.getCell(x, y);
                switch (cell.getType()) {
                    case WALL:
                        sb.append("#");
                        break;
                    case FLOOR:
                        if (cell.getActor() instanceof Skeleton) sb.append("s");
                        else if (cell.getActor() instanceof Player) sb.append("@");
                        else if (cell.getItem() instanceof Sword) sb.append("w");
                        else if (cell.getItem() instanceof Key) sb.append("k");
                        else if (cell.getActor() instanceof Ork) sb.append("o");
                        else if (cell.getActor() instanceof Nazgul) sb.append("n");
                        else sb.append(".");
                        break;
                    case CLOSED_DOOR:
                        sb.append("d");
                        break;
                    case STAIRS:
                        sb.append("r");
                    default:
                        sb.append(" ");
                        break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
