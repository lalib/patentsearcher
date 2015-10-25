package com.bilalalp.patentsearcher.gui.main;

import com.bilalalp.patentsearcher.business.analyser.PatentAnalyser;
import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.*;
import com.bilalalp.patentsearcher.entity.ContentType;
import com.bilalalp.patentsearcher.service.parsedkeywordinfo.ParsedKeywordInfoService;
import com.bilalalp.patentsearcher.service.patentinfo.PatentInfoService;
import com.bilalalp.patentsearcher.service.searchinfo.SearchInfoService;
import com.bilalalp.patentsearcher.service.stopwordinfo.StopWordInfoService;
import com.bilalalp.patentsearcher.util.GuiAlertUtil;
import com.bilalalp.patentsearcher.util.GuiListConverterUtil;
import com.bilalalp.patentsearcher.util.GuiUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AnalysingPage {

    private final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);
    private final StopWordInfoService stopWordInfoService = annotationConfigApplicationContext.getBean(StopWordInfoService.class);
    private final SearchInfoService searchInfoService = annotationConfigApplicationContext.getBean(SearchInfoService.class);
    private final PatentInfoService patentInfoService = annotationConfigApplicationContext.getBean(PatentInfoService.class);
    private final ParsedKeywordInfoService parsedKeywordInfoService = annotationConfigApplicationContext.getBean(ParsedKeywordInfoService.class);
    private final PatentAnalyser patentAnalyser = annotationConfigApplicationContext.getBean(PatentAnalyser.class);

    private final TableView<StopWordInfoDto> stopWordInfoDtoTableView = GuiUtil.getStopWordTable();
    private final TableView<SearchInfoDto> analyseInfoTableView = new TableView<>();

    private final Button analyseStartButton = new Button("Start");

    private final Label stateOfAnalysing = new Label(PatentSearcherConstant.NOT_YET);
    private final Label currentContentCount = new Label("0");
    private final Label abstractContentCount = new Label("0");
    private final Label claimContentCount = new Label("0");
    private final Label descriptionContentCount = new Label("0");

    private final AnalyseDto analyseDto = new AnalyseDto();

    private final CheckBox analyseAbstractCheckBox = new CheckBox("Abstract");
    private final CheckBox analyseClaimCheckBox = new CheckBox("Claim");
    private final CheckBox analyseDescriptionCheckBox = new CheckBox("Description");

    public Tab getTab() {

        final Tab thirdTab = new Tab();
        thirdTab.setText("Content Analyse");
        thirdTab.setClosable(false);

        thirdTab.setOnSelectionChanged(event -> {
            final Tab tab1 = (Tab) event.getSource();

            if (tab1.isSelected()) {
                fillAnalyseInfoTable();
                stopWordInfoDtoTableView.setItems(GuiListConverterUtil.getStopWordInfoObservableList(stopWordInfoService.findAll()));
            }
        });

        final TableColumn<SearchInfoDto, Boolean> selectionColumn = new TableColumn<>();
        selectionColumn.setMaxWidth(150);
        selectionColumn.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));
        selectionColumn.setCellFactory(personBooleanTableColumn -> new SearchInfoDtoCheckBoxCell());

        final TableColumn<SearchInfoDto, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        final TableColumn<SearchInfoDto, String> dateColumn = new TableColumn<>("Search Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("searchDate"));

        final TableColumn<SearchInfoDto, String> stateColumn = new TableColumn<>("State");
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        final TableColumn<SearchInfoDto, Long> linkCountColumn = new TableColumn<>("Total Link");
        linkCountColumn.setCellValueFactory(new PropertyValueFactory<>("linkCount"));

        final List<TableColumn<SearchInfoDto, ?>> keywordInfoDtoObservableList = new ArrayList<>();
        keywordInfoDtoObservableList.add(selectionColumn);
        keywordInfoDtoObservableList.add(idColumn);
        keywordInfoDtoObservableList.add(dateColumn);
        keywordInfoDtoObservableList.add(stateColumn);
        keywordInfoDtoObservableList.add(linkCountColumn);

        fillAnalyseInfoTable();

        analyseInfoTableView.getColumns().addAll(keywordInfoDtoObservableList);
        HBox hBox = new HBox();

        VBox vbox = getAnalyseContent();

        hBox.getChildren().addAll(analyseInfoTableView, vbox);

        thirdTab.setContent(hBox);

        return thirdTab;
    }

    public VBox getAnalyseContent() {
        final VBox analyseContent = new VBox();

        final GridPane gridPane = getAnalyseGridPane();

        final HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(25, 25, 25, 25));

        final TableView<KeywordCountDto> keywordCountDtoTableView = GuiUtil.getKeywordCountTable();

        stopWordInfoDtoTableView.setItems(GuiListConverterUtil.getStopWordInfoObservableList(stopWordInfoService.findAll()));

        final VBox vBox = new VBox();
        vBox.setSpacing(5);

        final Button analyseButton = new Button("Analyse");
        analyseButton.setOnAction(event -> {

            final List<Long> selectedSearchInfoIdList = analyseInfoTableView.getItems()
                    .stream()
                    .filter(SearchInfoDto::getSelected)
                    .map(SearchInfoDto::getId)
                    .collect(Collectors.toList());

            if (selectedSearchInfoIdList.isEmpty()) {
                GuiAlertUtil.searchInfoMustBeSelected().show();
                return;
            }

            final ObservableList<StopWordInfoDto> keywordInfoDtoTableViewItems = stopWordInfoDtoTableView.getItems();
            final List<String> selectedKeywordInfoDtoList = keywordInfoDtoTableViewItems.stream().filter(stopWordInfoDto -> Boolean.TRUE.equals(stopWordInfoDto.getSelected())).map(StopWordInfoDto::getText).collect(Collectors.toList());
            final List<ContentType> contentTypes = getSelectedContentTypes();
            final List<KeywordCountDto> keywordCountDtoList = parsedKeywordInfoService.getKeywordCountDto(selectedSearchInfoIdList, selectedKeywordInfoDtoList, contentTypes);
            final ObservableList<KeywordCountDto> observableList = FXCollections.observableList(keywordCountDtoList);
            keywordCountDtoTableView.setItems(observableList);
        });

        final HBox box = new HBox();
        box.getChildren().addAll(analyseButton);
        vBox.getChildren().addAll(box, stopWordInfoDtoTableView);

        hBox.getChildren().addAll(keywordCountDtoTableView, vBox);
        analyseContent.getChildren().addAll(gridPane, hBox);

        return analyseContent;
    }

    private void fillAnalyseInfoTable() {
        final List<SearchInfoDto> searchInfoDtoList = searchInfoService.findAllSearchInfos();
        analyseInfoTableView.setItems(FXCollections.observableList(searchInfoDtoList));
    }

    private GridPane getAnalyseGridPane() {
        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        final Label abstractContentCountLabel = new Label("Abstract : ");
        final Label claimContentCountLabel = new Label("Claim : ");
        final Label descriptionContentCountLabel = new Label("Description : ");
        final Label currentContentCountLabel = new Label("Current : ");
        final Label stateLabel = new Label("State : ");

        analyseStartButton.setOnAction(event -> {
            analyseDto.setCurrentCount(0);

            final ConfigDto configDto = new ConfigDto();
            configDto.setCrawlAbstract(analyseAbstractCheckBox.isSelected());
            configDto.setCrawlClaim(analyseClaimCheckBox.isSelected());
            configDto.setCrawlDescription(analyseDescriptionCheckBox.isSelected());
            analyseDto.setConfigDto(configDto);

            final List<SearchInfoDto> selectedKeywordInfoDtoList = getCheckedSearchInfoDtos();
            final Thread thread = new Thread(startAnalysingAsANewThread(selectedKeywordInfoDtoList, analyseDto));
            thread.run();
        });

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> currentContentCount.setText(analyseDto.getCurrentCount().toString())), 1, 100, TimeUnit.MILLISECONDS);

        gridPane.add(abstractContentCountLabel, 0, 0);
        gridPane.add(claimContentCountLabel, 0, 1);
        gridPane.add(descriptionContentCountLabel, 0, 2);

        gridPane.add(abstractContentCount, 1, 0);
        gridPane.add(claimContentCount, 1, 1);
        gridPane.add(descriptionContentCount, 1, 2);

        gridPane.add(analyseAbstractCheckBox, 3, 0);
        gridPane.add(analyseClaimCheckBox, 3, 1);
        gridPane.add(analyseDescriptionCheckBox, 3, 2);

        gridPane.add(currentContentCountLabel, 4, 0);
        gridPane.add(currentContentCount, 5, 0);

        gridPane.add(stateLabel, 4, 1);
        gridPane.add(stateOfAnalysing, 5, 1);
        gridPane.add(analyseStartButton, 4, 2);
        return gridPane;
    }


    private List<ContentType> getSelectedContentTypes() {

        final List<ContentType> contentTypeList = new ArrayList<>();

        if (analyseAbstractCheckBox.isSelected()) {
            contentTypeList.add(ContentType.ABSTRACT);
        }

        if (analyseClaimCheckBox.isSelected()) {
            contentTypeList.add(ContentType.CLAIM);
        }

        if (analyseDescriptionCheckBox.isSelected()) {
            contentTypeList.add(ContentType.DESCRIPTION);
        }

        return contentTypeList;
    }

    private class SearchInfoDtoCheckBoxCell extends TableCell<SearchInfoDto, Boolean> {
        final CheckBox checkBox = new CheckBox();

        final StackPane stackPane = new StackPane();

        public SearchInfoDtoCheckBoxCell() {
            stackPane.setPadding(new Insets(3));
            HBox vBox = new HBox();
            vBox.setSpacing(10);
            vBox.getChildren().addAll(checkBox);
            stackPane.getChildren().add(vBox);

            checkBox.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof SearchInfoDto) {
                    final SearchInfoDto searchInfoDto = (SearchInfoDto) item;
                    searchInfoDto.setSelected(checkBox.isSelected());
                    updateAnalyseTable();
                }
            });
        }

        @Override
        protected void updateItem(final Boolean item, final boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(stackPane);
            } else {
                setGraphic(null);
            }
        }
    }

    private void updateAnalyseTable() {

        final List<SearchInfoDto> selectedKeywordInfoDtoList = getCheckedSearchInfoDtos();

        final List<Long> searchIdList = selectedKeywordInfoDtoList.stream().map(SearchInfoDto::getId).collect(Collectors.toList());
        final AnalyseDto analyseDto = patentInfoService.getContentCounts(searchIdList);

        abstractContentCount.setText(analyseDto.getAbstractCount().toString());
        claimContentCount.setText(analyseDto.getClaimCount().toString());
        descriptionContentCount.setText(analyseDto.getDescriptionCount().toString());
    }

    private List<SearchInfoDto> getCheckedSearchInfoDtos() {
        final ObservableList<SearchInfoDto> keywordInfoDtoObservableList = analyseInfoTableView.getItems();
        return keywordInfoDtoObservableList.stream().filter(keywordInfoDto -> Boolean.TRUE.equals(keywordInfoDto.getSelected())).collect(Collectors.toList());
    }

    private Runnable startAnalysingAsANewThread(final List<SearchInfoDto> searchInfoDtoList, final AnalyseDto analyseDto) {
        return () -> {
            analyseStartButton.setDisable(true);
            stateOfAnalysing.setText(PatentSearcherConstant.NOT_YET);
            patentAnalyser.analyse(searchInfoDtoList, analyseDto);
            stateOfAnalysing.setText(PatentSearcherConstant.FINISHED);
            analyseStartButton.setDisable(false);
        };
    }
}
