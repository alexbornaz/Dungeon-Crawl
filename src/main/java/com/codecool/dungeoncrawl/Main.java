package com.codecool.dungeoncrawl;


import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Nazgul;
import com.codecool.dungeoncrawl.logic.actors.Ork;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.import_Export.Export;
import com.codecool.dungeoncrawl.logic.import_Export.Import;
import com.codecool.dungeoncrawl.logic.items.Item;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;

import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.*;


import javafx.scene.input.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Optional;

public class Main extends Application {
    int CANVAS_SIZE = 20;
    GameMap map = MapLoader.loadMap(1);
    GameMap savedMap1 = map;
    GameMap savedMap2 = MapLoader.loadMap(2);

    Canvas canvas = new Canvas(
            CANVAS_SIZE * Tiles.TILE_WIDTH,
            CANVAS_SIZE * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label strengthLabel = new Label();
    Label mapLabel = new Label();

    Label playerInventory = new Label("Inventory-> ");
    Button pickUpItems = new Button("Pick up");

    GameDatabaseManager dbManager;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        setupDbManager();

        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(new Label("Strength: "), 0,1);
        ui.add(healthLabel, 1, 0);
        ui.add(strengthLabel,1,1);
        ui.add(mapLabel,1,14);

        ui.add(pickUpItems,0,2);
        pickUpItems.setOnAction(click -> {
            map.getPlayer().pickUpItem();
            refresh();
        });
        pickUpItems.setFocusTraversable(false);
        ui.add(new Label("Inventory-> "),0,3);
        ui.add(playerInventory,0,4);
        Button restartButton = new Button("Restart Game");
        Button quitGameButton = new Button("Quit Game");
        restartButton.setOnAction(click -> {
            map = MapLoader.loadMap(1);
            map.getPlayer().setChangeMap(false);
            map.getPlayer().setHealth(15);
            map.getPlayer().setStr(3);
            map.getPlayer().setInventory(new ArrayList<Item>());
            refresh();
        });
        quitGameButton.setOnAction(click -> {
            System.exit(0);
        });
        Button loadButton = new Button("Load Game");
        ui.add(loadButton, 0, 20);
        loadButton.setOnAction(mousedown -> {
            showGetNameModalForGameLoad();
            refresh();
        });

        loadButton.setFocusTraversable(false);
        Button importButton = new Button("Import");
        ui.add(importButton, 0, 22);
        importButton.setOnAction(click -> {
            importGamestate(primaryStage);
        });
        importButton.setFocusTraversable(false);
        Button exportButton = new Button("Export");
        ui.add(exportButton, 0, 23);
        exportButton.setOnAction(click -> {
            exportModal();
        });
        exportButton.setFocusTraversable(false);
        ui.add(new Label("  "), 0, 12);
        ui.add(new Label("  "), 0, 13);
        ui.add(new Label("  "), 0, 14);
        ui.add(new Label("  "), 0, 15);
        ui.add(new Label("  "), 0, 16);
        ui.add(new Label("  "), 0, 17);
        ui.add(restartButton, 0, 18);
        ui.add(quitGameButton, 0, 19);
        restartButton.setFocusTraversable(false);
        quitGameButton.setFocusTraversable(false);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        scene.setOnKeyReleased(this::onKeyReleased);


        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void importGamestate(Stage stage){
        GameState game = Import.importGame(stage);
        if (game != null) {
            String gameMapToLoad = game.getCurrentMap();
            PlayerModel playerToLoad = game.getPlayer();
            loadGame(gameMapToLoad,playerToLoad);
        }
    }

    private void exportModal() {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Export Game");
        Label saveGameName = new Label("Name:");
        TextField textField = new TextField();
        GridPane gridPane = new GridPane();
        Button cancelButton = new Button("Cancel");
        Button saveButton = new Button("Export");
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> stage.close());
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->{
            GameState gameToExport = new GameState(MapSaver.saveMap(map),null,new PlayerModel(map.getPlayer()));
            savePlayerInDb(saveButton,textField.getText());
            try {
                Export.exportGame(gameToExport,stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        gridPane.setHgap(60);
        gridPane.setVgap(30);
        gridPane.add(saveGameName, 2, 2);
        gridPane.add(textField, 3, 2);
        gridPane.add(cancelButton, 4, 4);
        gridPane.add(saveButton, 2, 4);
        stage.setWidth(600);
        stage.setHeight(300);
        stage.setScene(new Scene(gridPane));
        stage.show();
    }


    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
    }


    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                refresh();
                break;
            case S:
                Player player = map.getPlayer();
                showSaveModal();
                break;
        }
        map.repositionCenter();
        monstersMovement(map);
        if (map.getPlayer().isDead()){
            System.exit(0);
        }
        changeMap();
    }

    private void showSaveModal() {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Save Game");
        Label saveGameName = new Label("Name:");
        TextField textField = new TextField();
        GridPane gridPane = new GridPane();
        Button cancelButton = new Button("Cancel");
        Button saveButton = new Button("Save");
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> stage.close());
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->{
                savePlayerInDb(saveButton, textField.getText());
                stage.close();
        });
        gridPane.setHgap(60);
        gridPane.setVgap(30);
        gridPane.add(saveGameName, 2, 2);
        gridPane.add(textField, 3, 2);
        gridPane.add(cancelButton, 4, 4);
        gridPane.add(saveButton, 2, 4);
        stage.setWidth(600);
        stage.setHeight(300);
        stage.setScene(new Scene(gridPane));
        stage.show();
    }

    private void savePlayerInDb(Button saveButton, String name) {
        Integer playerId = dbManager.getPlayerIdByNameManager(name);
        Player player = map.getPlayer();
        player.setName(name);
        if (playerId == null) {
            dbManager.savePlayer(player,MapSaver.saveMap(map));
        } else {
            confirmDialog(player);
        }
    }

    private void confirmDialog(Player player) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Name already exists");
        alert.setContentText("Would you like to overwrite?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dbManager.updatePlayer(player,MapSaver.saveMap(map));
            alert.close();
        } else {
            alert.close();
        }
    }


    private void monstersMovement(GameMap map) {
        try {
            map.removeDeadMonsters();
        } catch (ConcurrentModificationException e) {
            System.out.println("No monsters on map.");
        }
        for (Actor monster: map.getMonsters()){
            if (monster instanceof Ork){
                ((Ork)monster).move();
            }else if (monster instanceof Nazgul){
                ((Nazgul)monster).move();
            }
        }
    }

    private void refresh(){
        map.repositionCenter();
        int minX = map.getCenterCell().getX() - CANVAS_SIZE/2;
        int minY = map.getCenterCell().getY() - CANVAS_SIZE/2;
        int maxX = map.getCenterCell().getX() + CANVAS_SIZE/2;
        int maxY = map.getCenterCell().getY() + CANVAS_SIZE/2;
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x-minX, y-minY);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x-minX, y-minY);
                } else {
                    Tiles.drawTile(context, cell, x-minX, y-minY);
                }
            }
            healthLabel.setText("" + map.getPlayer().getHealth());
            strengthLabel.setText("" + map.getPlayer().getStr());
            if (map.getPlayer().isDead()){
                healthLabel.setText("DEAD");
            }
            playerInventory.setText(""+map.getPlayer().showInventory());
        }

    }
    public void changeMap() {
        int previousHealth = map.getPlayer().getHealth();
        int previousAttackStrength = map.getPlayer().getStr();
        ArrayList previousInventory = map.getPlayer().getInventory();
        if (map.getPlayer().getChangeMap() && map.getPlayer().getOnMap() == 1) {
            savedMap1 = map;
            map = savedMap2;
            map.getPlayer().setOnMap(2);
        } else if (map.getPlayer().getChangeMap() && map.getPlayer().getOnMap() == 2) {
            savedMap2 = map;
            map = savedMap1;
            map.getPlayer().setOnMap(1);
        }
        map.getPlayer().setChangeMap(false);
        map.getPlayer().setHealth(previousHealth);
        map.getPlayer().setStr(previousAttackStrength);
        map.getPlayer().setInventory(previousInventory);

        }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);

    }
    private void showGetNameModalForGameLoad() {
        ArrayList<String> names = dbManager.getAllNames();
        ListView<String> nameList = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList (names);
        nameList.setItems(items);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Load Game");
        Label loadGameName = new Label("Saves:");
        TextField textField = new TextField();
        GridPane gridPane = new GridPane();
        Button cancelButton = new Button("Cancel");
        Button loadButton = new Button("Load Game");
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> stage.close());

        loadButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            String chosenName = nameList.getSelectionModel().getSelectedItem();;
            loadGame(chosenName);
            refresh();
            stage.close();
        });
        gridPane.setHgap(60);
        gridPane.setVgap(30);
        gridPane.add(loadGameName, 2, 2);
        gridPane.add(nameList, 3, 2);
        gridPane.add(cancelButton, 3, 4);
        gridPane.add(loadButton, 2, 4);
        stage.setWidth(600);
        stage.setHeight(300);
        stage.setScene(new Scene(gridPane));
        stage.show();
    }
    public void loadGame(String chosenName){
        String currentMap = dbManager.getMapByPlayerName(chosenName);
        map = MapLoader.loadMap(currentMap);

        HashMap playerDictionary = dbManager.getPlayerByName(chosenName);
        Player player = map.getPlayer();
        player.setHealth((int) playerDictionary.get("hp"));
        player.getCell().setX((int) playerDictionary.get("x"));
        player.getCell().setY((int) playerDictionary.get("y"));
        player.setStr((int) playerDictionary.get("attack_strength"));
        player.setInventory(new ArrayList<Item>());
        for (int i=0; i<(int) playerDictionary.get("sword"); i++) {
            player.addToInventory(new Sword(new Cell(map, 0, 0, CellType.FLOOR)));
        }
        for (int i=0; i<(int) playerDictionary.get("key"); i++) {
            player.addToInventory(new Key(new Cell(map, 0, 0, CellType.FLOOR)));
        }
        player.movePlayerToPosition(player.getCell().getX(), player.getCell().getY());
        refresh();
    }
    public void loadGame(String gameMap,PlayerModel playertoImport){
        map = MapLoader.loadMap(gameMap);
        Player player = map.getPlayer();
        player.setHealth( playertoImport.getHp());
        player.getCell().setX( playertoImport.getX());
        player.getCell().setY( playertoImport.getY());
        player.setStr( playertoImport.getAttack_strength());
        player.setInventory(new ArrayList<Item>());
        for (int i=0; i<(int) playertoImport.getSword(); i++) {
            player.addToInventory(new Sword(new Cell(map, 0, 0, CellType.FLOOR)));
        }
        for (int i=0; i<(int) playertoImport.getKey(); i++) {
            player.addToInventory(new Key(new Cell(map, 0, 0, CellType.FLOOR)));
        }
        player.movePlayerToPosition(player.getCell().getX(), player.getCell().getY());
        refresh();
    }

}
