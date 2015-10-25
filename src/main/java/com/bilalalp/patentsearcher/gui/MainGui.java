package com.bilalalp.patentsearcher.gui;

import com.bilalalp.patentsearcher.gui.main.AnalysingPage;
import com.bilalalp.patentsearcher.gui.main.ParsingPage;
import com.bilalalp.patentsearcher.gui.main.SearchingPage;
import com.bilalalp.patentsearcher.util.GuiUtil;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Patent Searcher");
        primaryStage.setOnCloseRequest(event -> System.exit(1));
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 600, Color.WHITE);

        TabPane tabPane = new TabPane();
        BorderPane borderPane = new BorderPane();

        Tab tab = new SearchingPage().getTab();
        Tab tab2 = new ParsingPage().getTab();
        Tab tab3 = new AnalysingPage().getTab();

        tabPane.getTabs().addAll(tab, tab2, tab3);

        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        borderPane.setCenter(tabPane);
        borderPane.setTop(GuiUtil.getMenuBar(borderPane.widthProperty()));

        root.getChildren().add(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}