package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface PlayerDao {
    void add(PlayerModel player);
    void update(PlayerModel player);
    PlayerModel get(int id);
    List<PlayerModel> getAll();
    Integer getPlayerIdByName(String name);
    ArrayList<String> getAllNames();
    HashMap getPlayerByName(String name);
}
