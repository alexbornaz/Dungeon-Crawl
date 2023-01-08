package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, hp, x, y, attack_strength, sword, key) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.setInt(5, player.getAttack_strength());
            statement.setInt(6, player.getSword());
            statement.setInt(7, player.getKey());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE player SET hp = ?, x = ?, y = ?, attack_strength = ?, sword = ?, key = ? WHERE player_name = ?";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, player.getHp());
            statement.setInt(2, player.getX());
            statement.setInt(3, player.getY());
            statement.setInt(4, player.getAttack_strength());
            statement.setInt(5, player.getSword());
            statement.setInt(6, player.getKey());
            statement.setString(7, player.getPlayerName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public PlayerModel get(int id) {
        return null;
    }

    @Override
    public List<PlayerModel> getAll() {
        return null;
    }

    public Integer getPlayerIdByName(String name) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id FROM player WHERE player_name LIKE ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getAllNames() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT player_name FROM player";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            ArrayList<String> names = new ArrayList<>();
            while (resultSet.next()) { // while result set pointer is positioned before or on last row read authors
                names.add(resultSet.getString(1));
            }
            return names;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all authors", e);
        }
    }

    public HashMap getPlayerByName(String name) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM player WHERE player_name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            HashMap playerDictionary = new HashMap();
            playerDictionary.put("hp", resultSet.getInt(3));
            playerDictionary.put("x", resultSet.getInt(4));
            playerDictionary.put("y", resultSet.getInt(5));
            playerDictionary.put("attack_strength", resultSet.getInt(6));
            playerDictionary.put("sword", resultSet.getInt(7));
            playerDictionary.put("key", resultSet.getInt(8));

            return playerDictionary;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all authors", e);
        }
    }
}

