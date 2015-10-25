package com.bilalalp.patentsearcher.util;

import com.bilalalp.patentsearcher.dto.*;
import com.bilalalp.patentsearcher.gui.KeywordInfoGui;
import com.bilalalp.patentsearcher.gui.SiteInfoGui;
import com.bilalalp.patentsearcher.gui.StopWordInfoGui;
import com.bilalalp.patentsearcher.gui.table.KeywordCheckBoxCell;
import com.bilalalp.patentsearcher.gui.table.StopWordCheckBoxCell;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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
                new KeywordInfoGui().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        MenuItem siteInfoMenuItem = new MenuItem("Site Operations");
        siteInfoMenuItem.setOnAction(actionEvent -> {
            try {
                new SiteInfoGui().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        MenuItem stopWordInfoMenuItem = new MenuItem("StopWord Operations");
        stopWordInfoMenuItem.setOnAction(actionEvent -> {
            try {
                new StopWordInfoGui().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        operationMenu.getItems().addAll(keywordInfoMenuItem, siteInfoMenuItem, stopWordInfoMenuItem);

        menuBar.getMenus().addAll(fileMenu, operationMenu);
        return menuBar;
    }

    public static TableView<KeywordInfoDto> getKeywordTable() {

        final TableView<KeywordInfoDto> keywordInfoDtoTableView = new TableView<>();

        keywordInfoDtoTableView.setMaxWidth(260);
        TableColumn<KeywordInfoDto, String> keywordColumn = new TableColumn<>("Keyword");
        TableColumn<KeywordInfoDto, Boolean> selectionColumn = new TableColumn<>();
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        keywordColumn.setMinWidth(200);

        selectionColumn.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));
        selectionColumn.setCellFactory(personBooleanTableColumn -> new KeywordCheckBoxCell());

        final List<TableColumn<KeywordInfoDto, ?>> tableColumnList = new ArrayList<>();
        tableColumnList.add(selectionColumn);
        tableColumnList.add(keywordColumn);
        keywordInfoDtoTableView.getColumns().addAll(tableColumnList);
        return keywordInfoDtoTableView;
    }


    public static TableView<StopWordInfoDto> getStopWordTable() {
        final TableView<StopWordInfoDto> keywordInfoDtoTableView = new TableView<>();

        keywordInfoDtoTableView.setMaxWidth(260);
        TableColumn<StopWordInfoDto, String> keywordColumn = new TableColumn<>("Keyword");
        TableColumn<StopWordInfoDto, Boolean> selectionColumn = new TableColumn<>();
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        keywordColumn.setMinWidth(200);

        selectionColumn.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));
        selectionColumn.setCellFactory(personBooleanTableColumn -> new StopWordCheckBoxCell());

        final List<TableColumn<StopWordInfoDto, ?>> tableColumnList = new ArrayList<>();
        tableColumnList.add(selectionColumn);
        tableColumnList.add(keywordColumn);
        keywordInfoDtoTableView.getColumns().addAll(tableColumnList);
        return keywordInfoDtoTableView;
    }


    public static List<TableColumn<SearchInfoDto, ?>> getSearchInfoColumnList() {

        final TableColumn<SearchInfoDto, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        final TableColumn<SearchInfoDto, String> dateColumn = new TableColumn<>("Search Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("searchDate"));

        final TableColumn<SearchInfoDto, String> stateColumn = new TableColumn<>("State");
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        final TableColumn<SearchInfoDto, Long> linkCountColumn = new TableColumn<>("Total Link");
        linkCountColumn.setCellValueFactory(new PropertyValueFactory<>("linkCount"));

        final TableColumn<SearchInfoDto, Long> totalTimeColumn = new TableColumn<>("Total Time");
        totalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("totalTime"));

        final List<TableColumn<SearchInfoDto, ?>> keywordInfoDtoObservableList = new ArrayList<>();
        keywordInfoDtoObservableList.add(idColumn);
        keywordInfoDtoObservableList.add(dateColumn);
        keywordInfoDtoObservableList.add(stateColumn);
        keywordInfoDtoObservableList.add(linkCountColumn);
        keywordInfoDtoObservableList.add(totalTimeColumn);
        return keywordInfoDtoObservableList;
    }

    public static TableView<KeywordCountDto> getKeywordCountTable() {
        final TableView<KeywordCountDto> keywordCountDtoTableView = new TableView<>();

        final TableColumn<KeywordCountDto, Long> keywordColumn = new TableColumn<>("Keyword");
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("keyword"));

        final TableColumn<KeywordCountDto, String> countColumn = new TableColumn<>("Count");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        final List<TableColumn<KeywordCountDto, ?>> keywordInfoDtoObservableList = new ArrayList<>();
        keywordInfoDtoObservableList.add(keywordColumn);
        keywordInfoDtoObservableList.add(countColumn);

        keywordCountDtoTableView.getColumns().addAll(keywordInfoDtoObservableList);
        return keywordCountDtoTableView;
    }


    public static TableView<SiteInfoDto> getSiteInfoTable() {

        final TableView<SiteInfoDto> siteInfoDtoTableView = new TableView<>();
        siteInfoDtoTableView.setMaxWidth(260);

        final TableColumn<SiteInfoDto, String> keywordColumn = new TableColumn<>("Site Name");
        final TableColumn<SiteInfoDto, Boolean> selectionColumn = new TableColumn<>();

        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("siteName"));
        keywordColumn.setMinWidth(200);

        final List<TableColumn<SiteInfoDto, ?>> tableColumnList = new ArrayList<>();
        tableColumnList.add(selectionColumn);
        tableColumnList.add(keywordColumn);

        siteInfoDtoTableView.getColumns().addAll(tableColumnList);
        return siteInfoDtoTableView;
    }
}