package com.bilalalp.patentsearcher.gui.main;

import com.bilalalp.patentsearcher.business.parser.ParserService;
import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.ConfigDto;
import com.bilalalp.patentsearcher.dto.ContentSearchDto;
import com.bilalalp.patentsearcher.dto.CrawlingDto;
import com.bilalalp.patentsearcher.dto.SearchInfoDto;
import com.bilalalp.patentsearcher.service.patentinfo.PatentInfoService;
import com.bilalalp.patentsearcher.service.searchinfo.SearchInfoService;
import com.bilalalp.patentsearcher.util.GuiUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ParsingPage {

    private final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);
    private final SearchInfoService searchInfoService = annotationConfigApplicationContext.getBean(SearchInfoService.class);
    private final PatentInfoService patentInfoService = annotationConfigApplicationContext.getBean(PatentInfoService.class);
    private final ParserService parserService = annotationConfigApplicationContext.getBean(ParserService.class);

    private final ContentSearchDto contentSearchDto = new ContentSearchDto();
    private ConfigDto configDto = new ConfigDto();

    private final TableView<SearchInfoDto> searchInfoDtoTableView = new TableView<>();

    private final Label totalLinkCount = new Label(contentSearchDto.getTotalLinkCount().toString());
    private final Label analysiedLinkCount = new Label(contentSearchDto.getAnalysiedLinkCount().toString());
    private final Label notAnalysiedLinkCount = new Label(contentSearchDto.getNotAnalysiedLinkCount().toString());
    private final Label crawledCount = new Label();
    private final Label stateLabel = new Label("State : ");
    private final Label abstractCount = new Label(contentSearchDto.getAbstractCount().toString());
    private final Label stateOfCrawling = new Label(PatentSearcherConstant.NOT_YET);
    private final Label crawlingTotalWaitTime = new Label();
    private final Label crawlingTotalErrorCount = new Label();
    private final Label siteId = new Label();
    private final Label abstractCountLabel = new Label("Abstract Count : ");

    private final CheckBox abstractCheckBox = new CheckBox("Abstract");
    private final CheckBox descriptionCheckBox = new CheckBox("Description");
    private final CheckBox claimCheckBox = new CheckBox("Claim");

    private final TextArea crawledLinkTextArea = new TextArea();

    private final Button crawlStartButton = new Button("Start");
    private final Button crawlStopButton = new Button("Stop");


    private Thread crawlThread = new Thread();

    public Tab getTab() {
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

        searchInfoDtoTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        searchInfoDtoTableView.setMinWidth(600);

        fillSearchInfoTableForCrawlTab();

        searchInfoDtoTableView.getColumns().addAll(GuiUtil.getSearchInfoColumnList());
        hBox.getChildren().add(searchInfoDtoTableView);

        final VBox vBox = getVBox();

        hBox.getChildren().add(vBox);

        tab2.setContent(hBox);
        return tab2;
    }

    private void fillSearchInfoTableForCrawlTab() {
        final List<SearchInfoDto> searchInfoDtoList = searchInfoService.findAllSearchInfos();
        searchInfoDtoTableView.setItems(FXCollections.observableList(searchInfoDtoList));
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
            stateOfCrawling.setText(PatentSearcherConstant.NOT_YET);
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

    private Runnable startCrawlingAsANewThread(final SearchInfoDto searchInfoDto) {
        return () -> {
            crawlStopButton.setDisable(false);
            crawlStartButton.setDisable(true);
            Platform.runLater(() -> siteId.setText(String.valueOf(searchInfoDto.getId())));
            configDto.setCrawlAbstract(abstractCheckBox.isSelected());
            configDto.setCrawlClaim(claimCheckBox.isSelected());
            configDto.setCrawlDescription(descriptionCheckBox.isSelected());
            parserService.crawl(searchInfoDto, configDto);
            Platform.runLater(() -> stateOfCrawling.setText(PatentSearcherConstant.FINISHED));
            crawlStopButton.setDisable(true);
            crawlStartButton.setDisable(false);
        };
    }
}