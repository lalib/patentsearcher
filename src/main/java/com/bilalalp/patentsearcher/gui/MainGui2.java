package com.bilalalp.patentsearcher.gui;

import com.bilalalp.patentsearcher.business.searcher.SearchService;
import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.dto.SiteInfoDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.service.keywordinfo.KeywordInfoService;
import com.bilalalp.patentsearcher.service.siteinfo.SiteInfoService;
import com.bilalalp.patentsearcher.util.GuiUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MainGui2 extends Application {

    final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);

    private UIInfoDto uiInfoDto = new UIInfoDto();
    private Label totalRecordCountLabel = new Label();
    private Label currentRecordCountLabel = new Label();
    private Label errorRecordCountLabel = new Label();
    private Label totalWaitTime = new Label();
    private Label state = new Label("NOT YET");
    private TextArea textArea = new TextArea();

    private TableView<KeywordInfoDto> table = new TableView<>();
    private TableView<SiteInfoDto> siteInfoDtoTableView = new TableView<>();

    private Button startButton = new Button("Start");
    private Button stopButton = new Button("Stop");
    private Button clearButton = new Button("Clear");

    private Thread searchThread = new Thread();

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Patent Searcher");
        primaryStage.setOnCloseRequest(event -> System.exit(1));
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 600, Color.WHITE);

        TabPane tabPane = new TabPane();

        BorderPane borderPane = new BorderPane();

        Tab tab = getFirstTab();

        Tab tab2 = getSecondTab();
        tabPane.getTabs().addAll(tab, tab2);

        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        borderPane.setCenter(tabPane);
        borderPane.setTop(GuiUtil.getMenuBar(borderPane.widthProperty()));

        root.getChildren().add(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab getSecondTab() {
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Content Search");
        return tab2;
    }

    private Tab getFirstTab() {
        final Tab tab = new Tab();
        tab.setClosable(false);
        tab.setText("Link Search");

        final BorderPane tab1BorderPane = new BorderPane();
        table = getKeywordTable();
        siteInfoDtoTableView = getSiteInfoTable();

        final GridPane gridPane = getCenterOfFirstTab();
        final VBox vBox = new VBox();

        Label linkOutOut = new Label("Link Output : ");

        textArea.setEditable(false);
        textArea.setWrapText(true);
        vBox.getChildren().addAll(gridPane, linkOutOut, textArea);

        tab1BorderPane.setLeft(siteInfoDtoTableView);
        tab1BorderPane.setRight(table);
        tab1BorderPane.setCenter(vBox);
        tab.setContent(tab1BorderPane);
        return tab;
    }

    private TableView<KeywordInfoDto> getKeywordTable() {
        TableView<KeywordInfoDto> table = new TableView<>();
        table.setMaxWidth(260);
        TableColumn<KeywordInfoDto, String> keywordColumn = new TableColumn<>("Keyword");
        TableColumn<KeywordInfoDto, Boolean> selectionColumn = new TableColumn<>();
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        keywordColumn.setMinWidth(200);

        final KeywordInfoService keywordInfoService = annotationConfigApplicationContext.getBean(KeywordInfoService.class);
        final List<KeywordInfo> keywordInfoList = keywordInfoService.findAll();
        table.setItems(getKeywordInfoObservableList(keywordInfoList));

        selectionColumn.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));
        selectionColumn.setCellFactory(personBooleanTableColumn -> new KeywordCheckBoxCell(table));

        table.getColumns().addAll(selectionColumn, keywordColumn);
        return table;
    }

    private ObservableList<KeywordInfoDto> getKeywordInfoObservableList(List<KeywordInfo> keywordInfoList) {

        final ObservableList<KeywordInfoDto> keywordInfoDtoObservableList = FXCollections.observableArrayList();
        keywordInfoDtoObservableList.addAll(keywordInfoList.stream().map(keywordInfo -> new KeywordInfoDto(keywordInfo.getId(), keywordInfo.getKeyword())).collect(Collectors.toList()));
        return keywordInfoDtoObservableList;
    }

    private ObservableList<SiteInfoDto> getSiteInfoObservableList(List<SiteInfo> keywordInfoList) {

        final ObservableList<SiteInfoDto> keywordInfoDtoObservableList = FXCollections.observableArrayList();
        keywordInfoDtoObservableList.addAll(keywordInfoList.stream().map(keywordInfo -> new SiteInfoDto(keywordInfo.getId(), keywordInfo.getSiteName(), keywordInfo.getSiteAddres())).collect(Collectors.toList()));
        return keywordInfoDtoObservableList;
    }

    public TableView<SiteInfoDto> getSiteInfoTable() {

        TableView<SiteInfoDto> siteInfoTable = new TableView<>();
        siteInfoTable.setMaxWidth(260);

        TableColumn<SiteInfoDto, String> keywordColumn = new TableColumn<>("Site Name");
        TableColumn<SiteInfoDto, Boolean> selectionColumn = new TableColumn<>();
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("siteName"));
        keywordColumn.setMinWidth(200);

        final SiteInfoService siteInfoService = annotationConfigApplicationContext.getBean(SiteInfoService.class);
        final List<SiteInfo> siteInfoList = siteInfoService.findAll();
        siteInfoTable.setItems(getSiteInfoObservableList(siteInfoList));

        siteInfoTable.getColumns().addAll(selectionColumn, keywordColumn);
        return siteInfoTable;
    }

    public GridPane getCenterOfFirstTab() {

        final GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label totalRecordLabel = new Label("Total Record Count :");
        Label currentRecordLabel = new Label("Current Record Count : ");
        Label errorRecordLabel = new Label("Error Record Count : ");
        Label totalWaitTimeLabel = new Label("Total Wait Time : ");
        Label stateLabel = new Label("State : ");

        stopButton.setOnAction(event -> {
            searchThread.stop();
            state.setText("STOPPED!");
        });

        startButton.setOnAction(event -> {

            final ObservableList<KeywordInfoDto> keywordInfoDtoObservableList = table.getItems();
            final List<KeywordInfoDto> selectedKeywordInfoDtoList = keywordInfoDtoObservableList.stream().filter(keywordInfoDto -> Boolean.TRUE.equals(keywordInfoDto.getSelected())).collect(Collectors.toList());

            final SiteInfoDto selectedItem = siteInfoDtoTableView.getSelectionModel().getSelectedItem();

            if (selectedItem == null) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Site must be selected.!");
                alert.show();
                return;
            }

            if (selectedKeywordInfoDtoList == null || selectedKeywordInfoDtoList.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Keywords must be selected.!");
                alert.show();
                return;
            }

            state.setText("NOT YET");

            final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {
                totalRecordCountLabel.setText(uiInfoDto.getTotalRecordCount().toString());
                currentRecordCountLabel.setText(uiInfoDto.getCurrentRecordCount().toString());
                errorRecordCountLabel.setText(uiInfoDto.getErrorCount().toString());
                totalWaitTime.setText(String.valueOf(uiInfoDto.getTotalWaitTime() / 1000));
                textArea.setText(uiInfoDto.getGeneratedLink());
            }), 1, 1, TimeUnit.SECONDS);

            final SearchingDto searchingDto = new SearchingDto();
            searchingDto.setKeywordInfoList(selectedKeywordInfoDtoList);
            searchingDto.setSiteInfoDto(selectedItem);

            searchThread = new Thread(startSearchAsANewThread(searchingDto));
            searchThread.start();
        });

        grid.add(totalRecordLabel, 0, 1);
        grid.add(totalRecordCountLabel, 1, 1);

        grid.add(currentRecordLabel, 0, 2);
        grid.add(currentRecordCountLabel, 1, 2);

        grid.add(errorRecordLabel, 0, 3);
        grid.add(errorRecordCountLabel, 1, 3);

        grid.add(totalWaitTimeLabel, 0, 4);
        grid.add(totalWaitTime, 1, 4);

        grid.add(stateLabel, 0, 5);
        grid.add(state, 1, 5);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(startButton, stopButton, clearButton);
        grid.add(hBox, 1, 6);

        return grid;
    }

    private Runnable startSearchAsANewThread(SearchingDto searchingDto) {
        return () -> {

            final SearchService patentScopePatentSearcherService = annotationConfigApplicationContext.getBean(SearchService.class);

            try {
                patentScopePatentSearcherService.search(searchingDto, uiInfoDto);
                Platform.runLater(() -> state.setText("FINISHED!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private class KeywordCheckBoxCell extends TableCell<KeywordInfoDto, Boolean> {
        final CheckBox deleteButton = new CheckBox();

        final StackPane paddedButton = new StackPane();

        KeywordCheckBoxCell(TableView<KeywordInfoDto> table) {
            paddedButton.setPadding(new Insets(3));
            HBox vBox = new HBox();
            vBox.setSpacing(10);
            vBox.getChildren().addAll(deleteButton);
            paddedButton.getChildren().add(vBox);

            deleteButton.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof KeywordInfoDto) {
                    final KeywordInfoDto keywordInfoDto = (KeywordInfoDto) item;
                    keywordInfoDto.setSelected(deleteButton.isSelected());
                }
            });
        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
            } else {
                setGraphic(null);
            }
        }
    }
}
