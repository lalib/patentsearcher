package com.bilalalp.patentsearcher.gui;

import com.bilalalp.patentsearcher.business.searcher.PatentScopePatentSearcherServiceImpl;
import com.bilalalp.patentsearcher.business.searcher.PatentSearcherService;
import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import com.bilalalp.patentsearcher.util.GuiUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainGui extends Application {

    private UIInfoDto uiInfoDto = new UIInfoDto();
    private Label totalRecordCountLabel = new Label();
    private Label currentRecordCountLabel = new Label();
    private Label errorRecordCountLabel = new Label();
    private Label totalWaitTime = new Label();
    private Label state = new Label("NOT YET");

    final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.setTitle("Patent Searcher");
        primaryStage.setOnCloseRequest(event -> System.exit(1));
        primaryStage.setResizable(false);

        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        root.setCenter(grid);
        root.setTop(GuiUtil.getMenuBar(root.widthProperty()));

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label totalRecordLabel = new Label("Total Record Count :");
        Label currentRecordLabel = new Label("Current Record Count : ");
        Label errorRecordLabel = new Label("Error Record Count : ");
        Label totalWaitTimeLabel = new Label("Total Wait Time : ");
        Label stateLabel = new Label("State : ");

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

        Scene scene = new Scene(root, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.show();

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {
            totalRecordCountLabel.setText(uiInfoDto.getTotalRecordCount().toString());
            currentRecordCountLabel.setText(uiInfoDto.getCurrentRecordCount().toString());
            errorRecordCountLabel.setText(uiInfoDto.getErrorCount().toString());
            totalWaitTime.setText(String.valueOf(uiInfoDto.getTotalWaitTime() / 1000));
        }), 1, 1, TimeUnit.SECONDS);

        new Thread(startSearchAsANewThread()).start();
    }

    private Runnable startSearchAsANewThread() {
        return () -> {

            final long startTime = System.currentTimeMillis();
            final PatentSearcherService patentScopePatentSearcherService = annotationConfigApplicationContext.getBean(PatentScopePatentSearcherServiceImpl.class);
            final List<PatentInfo> patentInfoList;

            KeywordInfoDto keywordInfoDto = new KeywordInfoDto(0L, "nanotechnology");
            KeywordInfoDto keywordInfoDto1 = new KeywordInfoDto(1L, "weblogic");

            try {
                patentInfoList = patentScopePatentSearcherService.getPatentInfoList(Arrays.asList(keywordInfoDto, keywordInfoDto1), uiInfoDto);
                final long endTime = System.currentTimeMillis();
                System.out.println("Total Time : " + (endTime - startTime) / 1000);
                System.out.println(patentInfoList.size());

                Platform.runLater(() -> state.setText("FINISHED!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}