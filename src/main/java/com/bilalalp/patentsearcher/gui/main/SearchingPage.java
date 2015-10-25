package com.bilalalp.patentsearcher.gui.main;

import com.bilalalp.patentsearcher.business.searcher.SearcherService;
import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.dto.SiteInfoDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.service.keywordinfo.KeywordInfoService;
import com.bilalalp.patentsearcher.service.siteinfo.SiteInfoService;
import com.bilalalp.patentsearcher.util.GuiAlertUtil;
import com.bilalalp.patentsearcher.util.GuiListConverterUtil;
import com.bilalalp.patentsearcher.util.GuiUtil;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchingPage {

    private final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);
    private final SiteInfoService siteInfoService = annotationConfigApplicationContext.getBean(SiteInfoService.class);
    private final KeywordInfoService keywordInfoService = annotationConfigApplicationContext.getBean(KeywordInfoService.class);
    private final SearcherService patentScopePatentSearcherService = annotationConfigApplicationContext.getBean(SearcherService.class);

    private UIInfoDto uiInfoDto = new UIInfoDto();

    private Label totalRecordCountLabel = new Label();
    private Label currentRecordCountLabel = new Label();
    private Label errorRecordCountLabel = new Label();
    private Label totalWaitTime = new Label();
    private final Label stateLabel = new Label("State : ");

    private Label state = new Label(PatentSearcherConstant.NOT_YET);
    private final TextArea textArea = new TextArea();

    private TableView<KeywordInfoDto> table = new TableView<>();
    private TableView<SiteInfoDto> siteInfoDtoTableView = GuiUtil.getSiteInfoTable();

    private final Button startButton = new Button("Start");
    private final Button stopButton = new Button("Stop");
    private final Button clearButton = new Button("Clear");

    private Thread searchThread = new Thread();

    public Tab getTab() {
        final Tab tab = new Tab();
        tab.setOnSelectionChanged(event -> {
            final Tab tab1 = (Tab) event.getSource();

            if (tab1.isSelected()) {
                loadSiteInfos();
                table.setItems(GuiListConverterUtil.getKeywordInfoObservableList(keywordInfoService.findAll()));
            }
        });

        tab.setClosable(false);
        tab.setText("Link Search");

        final BorderPane tab1BorderPane = new BorderPane();
        table = GuiUtil.getKeywordTable();
        table.setItems(GuiListConverterUtil.getKeywordInfoObservableList(keywordInfoService.findAll()));
        loadSiteInfos();

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

    private void loadSiteInfos() {
        final List<SiteInfo> siteInfoList = siteInfoService.findAll();
        siteInfoDtoTableView.setItems(GuiListConverterUtil.getSiteInfoObservableList(siteInfoList));
    }

    public GridPane getCenterOfFirstTab() {

        final GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        final Label totalRecordLabel = new Label("Total Record Count :");
        final Label currentRecordLabel = new Label("Current Record Count : ");
        final Label errorRecordLabel = new Label("Error Record Count : ");
        final Label totalWaitTimeLabel = new Label("Total Wait Time : ");

        stopButton.setDisable(true);

        stopButton.setOnAction(event -> {
            searchThread.stop();
            state.setText("STOPPED!");
            stopAction();
        });

        startButton.setOnAction(event -> {

            final ObservableList<KeywordInfoDto> keywordInfoDtoObservableList = table.getItems();
            final List<KeywordInfoDto> selectedKeywordInfoDtoList = keywordInfoDtoObservableList.stream().filter(keywordInfoDto -> Boolean.TRUE.equals(keywordInfoDto.getSelected())).collect(Collectors.toList());

            final SiteInfoDto selectedItem = siteInfoDtoTableView.getSelectionModel().getSelectedItem();

            if (selectedItem == null) {
                GuiAlertUtil.siteMustBeSelected().show();
                return;
            }

            if (selectedKeywordInfoDtoList == null || selectedKeywordInfoDtoList.isEmpty()) {
                GuiAlertUtil.keywordsMustBeSelected().show();
                return;
            }

            state.setText(PatentSearcherConstant.NOT_YET);
            stopButton.setDisable(false);
            startButton.setDisable(true);
            clearButton.setDisable(true);
            textArea.setText("");

            final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {
                totalRecordCountLabel.setText(uiInfoDto.getTotalRecordCount().toString());
                currentRecordCountLabel.setText(uiInfoDto.getCurrentRecordCount().toString());
                errorRecordCountLabel.setText(uiInfoDto.getErrorCount().toString());
                totalWaitTime.setText(String.valueOf(uiInfoDto.getTotalWaitTime() / 1000));
                if (textArea.getText() == null || textArea.getText().isEmpty()) {
                    textArea.setText(uiInfoDto.getGeneratedLink());
                }
            }), 1, 1, TimeUnit.SECONDS);

            final SearchingDto searchingDto = new SearchingDto();
            searchingDto.setKeywordInfoList(selectedKeywordInfoDtoList);
            searchingDto.setSiteInfoDto(selectedItem);

            searchThread = new Thread(startSearchAsANewThread(searchingDto));
            searchThread.start();
        });

        clearButton.setOnAction(event -> clearButtonAction());

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

    private Runnable startSearchAsANewThread(final SearchingDto searchingDto) {
        return () -> {

            try {
                clearButtonAction();
                patentScopePatentSearcherService.search(searchingDto, uiInfoDto);
                stopAction();
                Platform.runLater(() -> state.setText(PatentSearcherConstant.FINISHED));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void stopAction() {
        startButton.setDisable(false);
        clearButton.setDisable(false);
        stopButton.setDisable(true);
    }

    private void clearButtonAction() {
        uiInfoDto.setCurrentRecordCount(0);
        uiInfoDto.setErrorCount(0);
        uiInfoDto.setGeneratedLink("");
        uiInfoDto.setTotalRecordCount(0);
        uiInfoDto.setTotalWaitTime(0);
        textArea.setText("");
        Platform.runLater(() -> state.setText(""));
    }
}
