package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
    }

    public void savePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model);
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=new Date(ts.getTime());
        GameState gameState = new GameState(Integer.toString(player.getOnMap()), date, model);
        gameStateDao.add(gameState);
    }
    public void updatePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        playerDao.update(model);
        Integer playerId = getPlayerIdByNameManager(player.getName());
        model.setId(playerId);
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=new Date(ts.getTime());
        GameState gameState = new GameState(Integer.toString(player.getOnMap()), date, model);
        gameStateDao.update(gameState);
    }
    public Integer getPlayerIdByNameManager(String name) {
        Integer playerId = playerDao.getPlayerIdByName(name);
        return playerId;
    }

    public ArrayList<String> getAllNames() {
        ArrayList<String> names = playerDao.getAllNames();
        return names;
    }

    public HashMap getPlayerByName(String name) {
        HashMap playerDictionary = playerDao.getPlayerByName(name);
        return playerDictionary;
    }

    public int getMapByPlayerName(String name) {
        Integer playerId = playerDao.getPlayerIdByName(name);
        Integer gameMap = gameStateDao.getMapByPlayerId(playerId);
        return gameMap;
    }
    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = "dungeonCrawl";
        String user = "alex";
        String password = "7empest";

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
