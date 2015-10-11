package com.bilalalp.patentsearcher.gui;

import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.dto.SiteInfoDto;
import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.service.siteinfo.SiteInfoService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class SiteInfoGui extends Application {

    final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);
    final SiteInfoService siteInfoService = annotationConfigApplicationContext.getBean(SiteInfoService.class);

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Site Operations");


        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        final TableView<SiteInfoDto> table = new TableView<>();

        final TableColumn<SiteInfoDto, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        final TableColumn<SiteInfoDto, String> siteNameColumn = new TableColumn<>("Site Name");
        siteNameColumn.setMinWidth(150);
        siteNameColumn.setCellValueFactory(new PropertyValueFactory<>("siteName"));

        final TableColumn<SiteInfoDto, String> siteAddressColumn = new TableColumn<>("Site Address");
        siteAddressColumn.setMinWidth(350);
        siteAddressColumn.setCellValueFactory(new PropertyValueFactory<>("siteAddress"));

        table.getColumns().addAll(idColumn, siteNameColumn, siteAddressColumn);

        final List<SiteInfo> siteInfoList = siteInfoService.findAll();
        table.setItems(getSiteInfoDtoObservableList(siteInfoList));

        final Label label = new Label("Site List");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font("Arial", 20));

        final VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(label, table);

        final Scene scene = new Scene(root, 550, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ObservableList<SiteInfoDto> getSiteInfoDtoObservableList(final List<SiteInfo> siteInfoList) {

        final ObservableList<SiteInfoDto> siteInfoDtoObservableList = FXCollections.observableArrayList();

        for (final SiteInfo siteInfo : siteInfoList) {
            siteInfoDtoObservableList.add(new SiteInfoDto(siteInfo.getId(), siteInfo.getSiteName(), siteInfo.getSiteAddres()));
        }

        return siteInfoDtoObservableList;
    }
}