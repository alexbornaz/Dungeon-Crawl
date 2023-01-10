package com.codecool.dungeoncrawl.logic.import_Export;

import com.codecool.dungeoncrawl.model.GameState;
import com.google.gson.Gson;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Export {

    public static void exportGame(GameState game, Stage primaryStage) throws IOException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fc.showSaveDialog(primaryStage);
        if (file != null) {
            String path = file.getPath();
            try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(game);
                out.write(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
