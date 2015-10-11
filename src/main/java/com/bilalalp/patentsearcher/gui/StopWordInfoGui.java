package com.bilalalp.patentsearcher.gui;

import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.dto.StopWordInfoDto;
import com.bilalalp.patentsearcher.entity.StopWordInfo;
import com.bilalalp.patentsearcher.service.stopwordinfo.StopWordInfoService;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class StopWordInfoGui extends Application {


    final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);
    final StopWordInfoService stopWordInfoService = annotationConfigApplicationContext.getBean(StopWordInfoService.class);

    Label stopWordLabel = new Label("StopWord : ");
    TextField stopWordTextArea = new TextField();

    Button stopWordAddButton = new Button("Add");
    Button clearTextAreaButton = new Button("Clear");
    Button refreshButton = new Button("Refresh");


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("StopWord Operations");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TableView<StopWordInfoDto> table = new TableView<>();

        clearTextAreaButton.setOnAction(event -> clear());
        stopWordAddButton.setOnAction(event -> {
            saveStopWord();
            refreshData(table);
        });

        TableColumn<StopWordInfoDto, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<StopWordInfoDto, String> stopWordColumn = new TableColumn<>("StopWord");
        stopWordColumn.setMinWidth(250);
        stopWordColumn.setEditable(true);
        stopWordColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        Callback<TableColumn<StopWordInfoDto, String>, TableCell<StopWordInfoDto, String>> cellFactory = p -> new EditingCell();

        stopWordColumn.setCellFactory(cellFactory);
        stopWordColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setText(t.getNewValue()));

        TableColumn operationColumn = new TableColumn("Operations");
        operationColumn.setSortable(false);
        table.getColumns().addAll(idColumn, stopWordColumn, operationColumn);

        operationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StopWordInfo, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<StopWordInfo, Boolean> features) {
                return new SimpleBooleanProperty(features.getValue() != null);
            }
        });

        operationColumn.setCellFactory(personBooleanTableColumn -> new StopWordOperationCell(table));

        table.setEditable(true);

        refreshData(table);

        refreshButton.setOnAction(event -> refreshData(table));

        grid.add(stopWordLabel, 0, 1);
        grid.add(stopWordTextArea, 1, 1);
        grid.add(stopWordAddButton, 2, 1);
        grid.add(clearTextAreaButton, 3, 1);
        grid.add(refreshButton, 4, 1);

        VBox root = new VBox();
        root.getChildren().addAll(grid, table);

        Scene scene = new Scene(root, 440, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshData(TableView<StopWordInfoDto> table) {
        final List<StopWordInfo> stopWordInfoList = stopWordInfoService.findAll();
        table.setItems(getStopWordInfoObservableList(stopWordInfoList));
    }

    private ObservableList<StopWordInfoDto> getStopWordInfoObservableList(List<StopWordInfo> stopWordInfoList) {

        final ObservableList<StopWordInfoDto> stopWordInfoDtoObservableList = FXCollections.observableArrayList();

        for (final StopWordInfo stopWordInfo : stopWordInfoList) {
            stopWordInfoDtoObservableList.add(new StopWordInfoDto(stopWordInfo.getId(), stopWordInfo.getWord()));
        }

        return stopWordInfoDtoObservableList;
    }

    private void saveStopWord() {
        final String wordText = stopWordTextArea.getText();

        if (wordText != null && !wordText.isEmpty()) {
            stopWordInfoService.persist(wordText);
            clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("StopWord can not be empty!");
            alert.show();
        }
    }

    private void clear() {
        stopWordTextArea.setText("");
    }

    private class StopWordOperationCell extends TableCell<StopWordInfoDto, Boolean> {
        final Button deleteButton = new Button("Delete");
        final Button updateButton = new Button("Update");

        final StackPane paddedButton = new StackPane();

        StopWordOperationCell(TableView<StopWordInfoDto> table) {
            paddedButton.setPadding(new Insets(3));
            HBox vBox = new HBox();
            vBox.setSpacing(10);
            vBox.getChildren().addAll(deleteButton, updateButton);
            paddedButton.getChildren().add(vBox);

            deleteButton.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof StopWordInfoDto) {
                    final StopWordInfoDto stopWordInfoDto = (StopWordInfoDto) item;
                    stopWordInfoService.remove(stopWordInfoDto.getId());
                    refreshData(table);
                }
            });
            updateButton.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof StopWordInfoDto) {
                    final StopWordInfoDto stopWordInfoDto = (StopWordInfoDto) item;
                    stopWordInfoService.update(stopWordInfoDto);
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

    private class EditingCell extends TableCell<StopWordInfoDto, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener((arg0, arg1, arg2) -> {
                if (!arg2) {
                    commitEdit(textField.getText());
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
}
