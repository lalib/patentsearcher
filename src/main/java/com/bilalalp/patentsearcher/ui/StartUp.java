package com.bilalalp.patentsearcher.ui;

import com.bilalalp.patentsearcher.business.PatentScopePatentSearcherServiceImpl;
import com.bilalalp.patentsearcher.business.PatentSearcherService;
import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartUp extends Application {

    private UIInfoDto uiInfoDto = new UIInfoDto();
    private Label totalRecordCountLabel = new Label();
    private Label currentRecordCountLabel = new Label();
    private Label errorRecordCountLabel = new Label();
    private Label totalWaitTime = new Label();
    private Label state = new Label("NOT YET");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {


        primaryStage.setTitle("Patent Searcher");

        GridPane grid = new GridPane();
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

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();

        final ScheduledExecutorService scheduler
                = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {

            Platform.runLater(() -> {
                totalRecordCountLabel.setText(uiInfoDto.getTotalRecordCount().toString());
                currentRecordCountLabel.setText(uiInfoDto.getCurrentRecordCount().toString());
                errorRecordCountLabel.setText(uiInfoDto.getErrorCount().toString());
                totalWaitTime.setText(String.valueOf(uiInfoDto.getTotalWaitTime() / 1000));
            });
        }, 1, 1, TimeUnit.SECONDS);


        Thread thread = new Thread(() -> {


            final long startTime = System.currentTimeMillis();
            final PatentSearcherService patentScopePatentSearcherService = new PatentScopePatentSearcherServiceImpl();
            final List<PatentInfo> patentInfoList;
            try {
                patentInfoList = patentScopePatentSearcherService.getPatentInfoList(PatentSearcherConstant.PATENT_SCOPE_URL, uiInfoDto);
                final long endTime = System.currentTimeMillis();
                System.out.println("Total Time : " + (endTime - startTime) / 1000);
                System.out.println(patentInfoList.size());

                Platform.runLater(() -> {
                    state.setText("FINISHED!");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();


    }
}
