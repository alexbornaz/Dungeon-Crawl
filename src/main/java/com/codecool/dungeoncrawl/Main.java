package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Nazgul;
import com.codecool.dungeoncrawl.logic.actors.Ork;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ConcurrentModificationException;

public class Main extends Application {
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label playerInventory = new Label("Inventory-> ");
    Button pickUpItems = new Button("Pick up");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        ui.add(pickUpItems,0,2);
        pickUpItems.setOnAction(click -> {
            map.getPlayer().pickUpItem();
            refresh();
        });
        pickUpItems.setFocusTraversable(false);
        ui.add(new Label("Inventory-> "),0,3);
        ui.add(playerInventory,0,4);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                monstersMovement(map);
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                monstersMovement(map);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                monstersMovement(map);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                monstersMovement(map);
                refresh();
                break;
        }
        if (map.getPlayer().isDead()){
            System.exit(0);
        }
    }

    private void monstersMovement(GameMap map) {
        for (Actor monster: map.getMonsters()){
            if (monster instanceof Ork){
                ((Ork)monster).move();
            }else if (monster instanceof Nazgul){
                ((Nazgul)monster).move();
            }
        }
    }

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
            healthLabel.setText("" + map.getPlayer().getHealth());
            if (map.getPlayer().isDead()){
                healthLabel.setText("DEAD");
            }
            playerInventory.setText(""+map.getPlayer().showInventory());
        }
    }
}
