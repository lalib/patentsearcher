package com.bilalalp.patentsearcher.gui;

import com.bilalalp.patentsearcher.business.analyser.PatentAnalyser;
import com.bilalalp.patentsearcher.business.parser.ParserService;
import com.bilalalp.patentsearcher.business.searcher.SearcherService;
import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.dto.*;
import com.bilalalp.patentsearcher.entity.ContentType;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.entity.StopWordInfo;
import com.bilalalp.patentsearcher.service.keywordinfo.KeywordInfoService;
import com.bilalalp.patentsearcher.service.parsedkeywordinfo.ParsedKeywordInfoService;
import com.bilalalp.patentsearcher.service.patentinfo.PatentInfoService;
import com.bilalalp.patentsearcher.service.searchinfo.SearchInfoService;
import com.bilalalp.patentsearcher.service.siteinfo.SiteInfoService;
import com.bilalalp.patentsearcher.service.stopwordinfo.StopWordInfoService;
import com.bilalalp.patentsearcher.util.GuiUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MainGui extends Application {

    private final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);
    private final PatentInfoService patentInfoService = annotationConfigApplicationContext.getBean(PatentInfoService.class);
    private final ParserService parserService = annotationConfigApplicationContext.getBean(ParserService.class);
    private final PatentAnalyser patentAnalyser = annotationConfigApplicationContext.getBean(PatentAnalyser.class);
    private final ParsedKeywordInfoService parsedKeywordInfoService = annotationConfigApplicationContext.getBean(ParsedKeywordInfoService.class);

    private UIInfoDto uiInfoDto = new UIInfoDto();
    private ContentSearchDto contentSearchDto = new ContentSearchDto();
    private ConfigDto configDto = new ConfigDto();
    private AnalyseDto analyseDto = new AnalyseDto();

    private Label totalRecordCountLabel = new Label();
    private Label currentRecordCountLabel = new Label();
    private Label errorRecordCountLabel = new Label();
    private Label totalWaitTime = new Label();
    private final Label totalLinkCount = new Label(contentSearchDto.getTotalLinkCount().toString());
    private final Label analysiedLinkCount = new Label(contentSearchDto.getAnalysiedLinkCount().toString());
    private final Label notAnalysiedLinkCount = new Label(contentSearchDto.getNotAnalysiedLinkCount().toString());
    private final Label crawledCount = new Label();
    private final Label stateLabel = new Label("State : ");
    private final Label currentContentCount = new Label("0");

    private final Label abstractCountLabel = new Label("Abstract Count : ");
    private final Label abstractCount = new Label(contentSearchDto.getAbstractCount().toString());

    private final Label crawlingTotalWaitTime = new Label();
    private final Label crawlingTotalErrorCount = new Label();
    private final Label siteId = new Label();

    private final Label abstractContentCount = new Label("0");
    private final Label claimContentCount = new Label("0");
    private final Label descriptionContentCount = new Label("0");

    private Label state = new Label("NOT YET");
    private Label stateOfCrawling = new Label("NOT YET");
    private Label stateOfAnalysing = new Label("NOT YET");
    private final TextArea textArea = new TextArea();
    private final TextArea crawledLinkTextArea = new TextArea();

    private TableView<KeywordInfoDto> table = new TableView<>();
    private TableView<SiteInfoDto> siteInfoDtoTableView = new TableView<>();
    private final TableView<SearchInfoDto> searchInfoDtoTableView = new TableView<>();
    private final TableView<SearchInfoDto> analyseInfoTableView = new TableView<>();
    private final TableView<SiteInfoDto> siteInfoTable = new TableView<>();
    private final TableView<StopWordInfoDto> stopWordInfoDtoTableView = getStopWordTable();

    private final Button startButton = new Button("Start");
    private final Button stopButton = new Button("Stop");
    private final Button clearButton = new Button("Clear");
    private final Button crawlStartButton = new Button("Start");
    private final Button crawlStopButton = new Button("Stop");
    private final Button analyseStartButton = new Button("Start");

    final CheckBox abstractCheckBox = new CheckBox("Abstract");
    final CheckBox descriptionCheckBox = new CheckBox("Description");
    final CheckBox claimCheckBox = new CheckBox("Claim");

    final CheckBox analyseAbstractCheckBox = new CheckBox("Abstract");
    final CheckBox analyseClaimCheckBox = new CheckBox("Claim");
    final CheckBox analyseDescriptionCheckBox = new CheckBox("Description");

    private Thread searchThread = new Thread();
    private Thread crawlThread = new Thread();

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
        Tab tab3 = getThirdTab();

        tabPane.getTabs().addAll(tab, tab2, tab3);

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

        tab2.setOnSelectionChanged(event1 -> {

            final Tab tab = (Tab) event1.getSource();
            if (tab.isSelected()) {
                fillSearchInfoTableForCrawlTab();
            }
        });

        final HBox hBox = new HBox();

        searchInfoDtoTableView.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                final Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                } else {
                    row = (TableRow) node.getParent();
                }

                if (row.getItem() instanceof SearchInfoDto) {
                    final SearchInfoDto selectedSearchInfoDto = (SearchInfoDto) row.getItem();
                    final ContentSearchDto contentSearchDto = patentInfoService.getContentSearchDto(selectedSearchInfoDto.getId());

                    this.contentSearchDto.setAnalysiedLinkCount(contentSearchDto.getAnalysiedLinkCount());
                    this.contentSearchDto.setNotAnalysiedLinkCount(contentSearchDto.getNotAnalysiedLinkCount());
                    this.contentSearchDto.setTotalLinkCount(contentSearchDto.getTotalLinkCount());
                    this.contentSearchDto.setAbstractCount(contentSearchDto.getAbstractCount());
                    setActualValues();
                }
            }
        });

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(this::setActualValues), 1, 1, TimeUnit.SECONDS);

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

        searchInfoDtoTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        searchInfoDtoTableView.setMinWidth(600);

        fillSearchInfoTableForCrawlTab();

        searchInfoDtoTableView.getColumns().addAll(keywordInfoDtoObservableList);
        hBox.getChildren().add(searchInfoDtoTableView);

        final VBox vBox = getVBox();

        hBox.getChildren().add(vBox);

        tab2.setContent(hBox);
        return tab2;
    }

    private void fillSearchInfoTableForCrawlTab() {
        final List<SearchInfoDto> searchInfoDtoList = getSearchInfoDtos();
        searchInfoDtoTableView.setItems(FXCollections.observableList(searchInfoDtoList));
    }

    private List<SearchInfoDto> getSearchInfoDtos() {
        final SearchInfoService searchInfoService = annotationConfigApplicationContext.getBean(SearchInfoService.class);
        return searchInfoService.findAllSearchInfos();
    }

    private void setActualValues() {
        this.totalLinkCount.setText(this.contentSearchDto.getTotalLinkCount().toString());
        this.analysiedLinkCount.setText(this.contentSearchDto.getAnalysiedLinkCount().toString());
        this.notAnalysiedLinkCount.setText(this.contentSearchDto.getNotAnalysiedLinkCount().toString());
        this.abstractCount.setText(this.contentSearchDto.getAbstractCount().toString());
    }

    private VBox getVBox() {

        crawledLinkTextArea.setEditable(false);
        crawledLinkTextArea.setWrapText(true);

        final VBox vBox = new VBox();
        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        final Label totalLink = new Label("Total Link Count : ");
        final Label analysiedLink = new Label("Analysied Link Count : ");
        final Label notAnalysiedLink = new Label("Not Analysied Link Count : ");
        final Label crawlingErrorCountLabel = new Label("Error Count : ");
        final Label crawlingTotalWaitingTime = new Label("Error Waiting Time : ");
        final Label siteIdLabel = new Label("Search Id : ");

        final Label crawledCountLabel = new Label("Current Count : ");

        crawlStartButton.setOnAction(event -> {
            stateOfCrawling.setText("NOT YET");
            configDto.setCrawlingDto(new CrawlingDto());
            final SearchInfoDto searchInfoDto = searchInfoDtoTableView.getSelectionModel().getSelectedItem();
            crawlThread = new Thread(startCrawlingAsANewThread(searchInfoDto));
            crawlThread.start();
        });

        crawlStopButton.setDisable(true);
        crawlStopButton.setOnAction(event -> {
            crawlThread.stop();
            crawlStopButton.setDisable(true);
            crawlStartButton.setDisable(false);
        });

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {

            crawledCount.setText(configDto.getCrawlingDto().getCrawlCount().toString());
            crawlingTotalWaitTime.setText(configDto.getCrawlingDto().getTotalWaitTime().toString());
            crawlingTotalErrorCount.setText(configDto.getCrawlingDto().getErrorCount().toString());
            crawledLinkTextArea.setText(configDto.getCrawlingDto().getCurrentLink());

        }), 1, 1, TimeUnit.MILLISECONDS);

        gridPane.add(siteIdLabel, 0, 1);
        gridPane.add(siteId, 1, 1);

        gridPane.add(totalLink, 0, 2);
        gridPane.add(totalLinkCount, 1, 2);
        gridPane.add(abstractCheckBox, 2, 2);
        gridPane.add(analysiedLink, 0, 3);
        gridPane.add(analysiedLinkCount, 1, 3);
        gridPane.add(descriptionCheckBox, 2, 3);
        gridPane.add(notAnalysiedLink, 0, 4);
        gridPane.add(notAnalysiedLinkCount, 1, 4);
        gridPane.add(claimCheckBox, 2, 4);
        gridPane.add(abstractCountLabel, 0, 5);
        gridPane.add(abstractCount, 1, 5);

        final HBox hBox = new HBox();
        hBox.getChildren().addAll(crawlStartButton, crawlStopButton);
        gridPane.add(hBox, 2, 5);

        gridPane.add(crawledCountLabel, 0, 7);
        gridPane.add(crawledCount, 1, 7);
        gridPane.add(crawlingErrorCountLabel, 0, 8);
        gridPane.add(crawlingTotalErrorCount, 1, 8);
        gridPane.add(crawlingTotalWaitingTime, 0, 9);
        gridPane.add(crawlingTotalWaitTime, 1, 9);

        gridPane.add(stateLabel, 0, 10);
        gridPane.add(stateOfCrawling, 1, 10);

        final Label linkOutOut = new Label("Link Output : ");
        vBox.getChildren().addAll(gridPane, linkOutOut, crawledLinkTextArea);
        return vBox;
    }

    private Tab getFirstTab() {
        final Tab tab = new Tab();
        tab.setOnSelectionChanged(event -> {
            final Tab tab1 = (Tab) event.getSource();

            if (tab1.isSelected()) {
                loadSiteInfos();
                table.setItems(getKeywordInfoObservableList(fillKeywordInfoTable()));
            }
        });

        tab.setClosable(false);
        tab.setText("Link Search");

        final BorderPane tab1BorderPane = new BorderPane();
        table = getKeywordTable();
        table.setItems(getKeywordInfoObservableList(fillKeywordInfoTable()));
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

    private TableView<StopWordInfoDto> getStopWordTable(){
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

    private List<KeywordInfo> fillKeywordInfoTable() {
        final KeywordInfoService keywordInfoService = annotationConfigApplicationContext.getBean(KeywordInfoService.class);
        return keywordInfoService.findAll();
    }

    private List<StopWordInfo> getAllStopWords(){
        final StopWordInfoService stopWordInfoService = annotationConfigApplicationContext.getBean(StopWordInfoService.class);
        return stopWordInfoService.findAll();
    }

    private ObservableList<KeywordInfoDto> getKeywordInfoObservableList(final List<KeywordInfo> keywordInfoList) {

        final ObservableList<KeywordInfoDto> keywordInfoDtoObservableList = FXCollections.observableArrayList();
        keywordInfoDtoObservableList.addAll(keywordInfoList.stream().map(keywordInfo -> new KeywordInfoDto(keywordInfo.getId(), keywordInfo.getKeyword())).collect(Collectors.toList()));
        return keywordInfoDtoObservableList;
    }

    private ObservableList<StopWordInfoDto> getStopWordInfoObservableList(final List<StopWordInfo> stopWordInfoList){
        final ObservableList<StopWordInfoDto> keywordInfoDtoObservableList = FXCollections.observableArrayList();
        keywordInfoDtoObservableList.addAll(stopWordInfoList.stream().map(keywordInfo -> new StopWordInfoDto(keywordInfo.getId(), keywordInfo.getWord())).collect(Collectors.toList()));
        return keywordInfoDtoObservableList;
    }

    private ObservableList<SiteInfoDto> getSiteInfoObservableList(final List<SiteInfo> keywordInfoList) {

        final ObservableList<SiteInfoDto> keywordInfoDtoObservableList = FXCollections.observableArrayList();
        keywordInfoDtoObservableList.addAll(keywordInfoList.stream().map(keywordInfo -> new SiteInfoDto(keywordInfo.getId(), keywordInfo.getSiteName(), keywordInfo.getSiteAddres())).collect(Collectors.toList()));
        return keywordInfoDtoObservableList;
    }

    public TableView<SiteInfoDto> getSiteInfoTable() {

        siteInfoTable.setMaxWidth(260);

        TableColumn<SiteInfoDto, String> keywordColumn = new TableColumn<>("Site Name");
        TableColumn<SiteInfoDto, Boolean> selectionColumn = new TableColumn<>();
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("siteName"));
        keywordColumn.setMinWidth(200);

        loadSiteInfos();

        final List<TableColumn<SiteInfoDto, ?>> tableColumnList = new ArrayList<>();
        tableColumnList.add(selectionColumn);
        tableColumnList.add(keywordColumn);

        siteInfoTable.getColumns().addAll(tableColumnList);
        return siteInfoTable;
    }

    private void loadSiteInfos() {
        final SiteInfoService siteInfoService = annotationConfigApplicationContext.getBean(SiteInfoService.class);
        final List<SiteInfo> siteInfoList = siteInfoService.findAll();
        siteInfoTable.setItems(getSiteInfoObservableList(siteInfoList));
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

                final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Site must be selected.!");
                alert.show();
                return;
            }

            if (selectedKeywordInfoDtoList == null || selectedKeywordInfoDtoList.isEmpty()) {
                final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Keywords must be selected.!");
                alert.show();
                return;
            }

            state.setText("NOT YET");
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

    private void stopAction() {
        startButton.setDisable(false);
        clearButton.setDisable(false);
        stopButton.setDisable(true);
    }

    private Runnable startSearchAsANewThread(final SearchingDto searchingDto) {
        return () -> {

            final SearcherService patentScopePatentSearcherService = annotationConfigApplicationContext.getBean(SearcherService.class);

            try {
                clearButtonAction();
                patentScopePatentSearcherService.search(searchingDto, uiInfoDto);
                stopAction();
                Platform.runLater(() -> state.setText("FINISHED!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private Runnable startCrawlingAsANewThread(final SearchInfoDto searchInfoDto) {
        return () -> {
            crawlStopButton.setDisable(false);
            crawlStartButton.setDisable(true);
            Platform.runLater(() -> siteId.setText(String.valueOf(searchInfoDto.getId())));
            configDto.setCrawlAbstract(abstractCheckBox.isSelected());
            configDto.setCrawlClaim(claimCheckBox.isSelected());
            configDto.setCrawlDescription(descriptionCheckBox.isSelected());
            parserService.crawl(searchInfoDto, configDto);
            Platform.runLater(() -> stateOfCrawling.setText("FINISHED"));
            crawlStopButton.setDisable(true);
            crawlStartButton.setDisable(false);
        };
    }

    private Runnable startAnalysingAsANewThread(final List<SearchInfoDto> searchInfoDtoList, final AnalyseDto analyseDto) {
        return () -> {
            analyseStartButton.setDisable(true);
            stateOfAnalysing.setText("NOT YET");

            patentAnalyser.analyse(searchInfoDtoList, analyseDto);

            stateOfAnalysing.setText("FINISHED");
            analyseStartButton.setDisable(false);
        };
    }

    public Tab getThirdTab() {
        final Tab thirdTab = new Tab();
        thirdTab.setText("Content Analyse");
        thirdTab.setClosable(false);

        thirdTab.setOnSelectionChanged(event -> {
            final Tab tab1 = (Tab) event.getSource();

            if (tab1.isSelected()) {
                fillAnalyseInfoTable();
                stopWordInfoDtoTableView.setItems(getStopWordInfoObservableList(getAllStopWords()));
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

    private void fillAnalyseInfoTable() {
        final List<SearchInfoDto> searchInfoDtoList = getSearchInfoDtos();
        analyseInfoTableView.setItems(FXCollections.observableList(searchInfoDtoList));
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

    public VBox getAnalyseContent() {
        final VBox analyseContent = new VBox();

        final GridPane gridPane = getAnalyseGridPane();

        final HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(25, 25, 25, 25));

        final TableView<KeywordCountDto> keywordCountDtoTableView = new TableView<>();

        final TableColumn<KeywordCountDto, Long> keywordColumn = new TableColumn<>("Keyword");
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("keyword"));

        final TableColumn<KeywordCountDto, String> countColumn = new TableColumn<>("Count");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        final List<TableColumn<KeywordCountDto, ?>> keywordInfoDtoObservableList = new ArrayList<>();
        keywordInfoDtoObservableList.add(keywordColumn);
        keywordInfoDtoObservableList.add(countColumn);

        keywordCountDtoTableView.getColumns().addAll(keywordInfoDtoObservableList);

        stopWordInfoDtoTableView.setItems(getStopWordInfoObservableList(getAllStopWords()));

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
                final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("SearchInfo must be selected.!");
                alert.show();
                return;
            }

            final ObservableList<StopWordInfoDto> keywordInfoDtoTableViewItems = stopWordInfoDtoTableView.getItems();

            final List<String> selectedKeywordInfoDtoList = new ArrayList<>();

            for(StopWordInfoDto stopWordInfoDto : keywordInfoDtoTableViewItems){
                if(Boolean.TRUE.equals(stopWordInfoDto.getSelected())){
                    selectedKeywordInfoDtoList.add(stopWordInfoDto.getText());
                }
            }

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

    private class KeywordCheckBoxCell extends TableCell<KeywordInfoDto, Boolean> {
        final CheckBox deleteButton = new CheckBox();

        final StackPane paddedButton = new StackPane();

        KeywordCheckBoxCell() {
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
        protected void updateItem(final Boolean item, final boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
            } else {
                setGraphic(null);
            }
        }
    }

    private class StopWordCheckBoxCell extends TableCell<StopWordInfoDto,Boolean>{
        final CheckBox deleteButton = new CheckBox();

        final StackPane paddedButton = new StackPane();

        StopWordCheckBoxCell() {
            paddedButton.setPadding(new Insets(3));
            HBox vBox = new HBox();
            vBox.setSpacing(10);
            vBox.getChildren().addAll(deleteButton);
            paddedButton.getChildren().add(vBox);

            deleteButton.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof StopWordInfoDto) {
                    final StopWordInfoDto keywordInfoDto = (StopWordInfoDto) item;
                    keywordInfoDto.setSelected(deleteButton.isSelected());
                }
            });
        }

        @Override
        protected void updateItem(final Boolean item, final boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
            } else {
                setGraphic(null);
            }
        }
    }

    private class SearchInfoDtoCheckBoxCell extends TableCell<SearchInfoDto, Boolean> {
        final CheckBox deleteButton = new CheckBox();

        final StackPane paddedButton = new StackPane();

        SearchInfoDtoCheckBoxCell() {
            paddedButton.setPadding(new Insets(3));
            HBox vBox = new HBox();
            vBox.setSpacing(10);
            vBox.getChildren().addAll(deleteButton);
            paddedButton.getChildren().add(vBox);

            deleteButton.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof SearchInfoDto) {
                    final SearchInfoDto searchInfoDto = (SearchInfoDto) item;
                    searchInfoDto.setSelected(deleteButton.isSelected());
                    updateAnalyseTable();
                }
            });
        }

        @Override
        protected void updateItem(final Boolean item, final boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
            } else {
                setGraphic(null);
            }
        }
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