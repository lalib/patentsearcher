package com.bilalalp.patentsearcher.util;

import com.bilalalp.patentsearcher.gui.KeywordInfoUI;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public final class GuiUtil {

    private GuiUtil() {
        //Util Class
    }

    public static MenuBar getMenuBar(ReadOnlyDoubleProperty readOnlyDoubleProperty) {

        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(readOnlyDoubleProperty);

        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> {
            Platform.exit();
            System.exit(1);
        });

        fileMenu.getItems().add(exitMenuItem);

        Menu operationMenu = new Menu("Operation");
        MenuItem keywordInfoMenuItem = new MenuItem("Keyword Operations");
        keywordInfoMenuItem.setOnAction(actionEvent -> {
            try {
                new KeywordInfoUI().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        operationMenu.getItems().add(keywordInfoMenuItem);

        menuBar.getMenus().addAll(fileMenu, operationMenu);
        return menuBar;
    }
}