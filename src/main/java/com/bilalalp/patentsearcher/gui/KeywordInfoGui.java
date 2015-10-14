package com.bilalalp.patentsearcher.gui;

import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.service.keywordinfo.KeywordInfoService;
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
import java.util.stream.Collectors;

public class KeywordInfoGui extends Application {

    final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);
    final KeywordInfoService keywordInfoService = annotationConfigApplicationContext.getBean(KeywordInfoService.class);

    Label keywordLabel = new Label("Keyword : ");
    TextField keywordTextArea = new TextField();

    Button keywordAddButton = new Button("Add");
    Button clearTextAreaButton = new Button("Clear");
    Button refreshButton = new Button("Refresh");


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Keyword Operations");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TableView<KeywordInfoDto> table = new TableView<>();

        clearTextAreaButton.setOnAction(event -> clear());
        keywordAddButton.setOnAction(event -> {
            saveKeyword();
            refreshData(table);
        });

        TableColumn<KeywordInfoDto, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<KeywordInfoDto, String> keywordColumn = new TableColumn<>("Keyword");
        keywordColumn.setMinWidth(250);
        keywordColumn.setEditable(true);
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        Callback<TableColumn<KeywordInfoDto, String>, TableCell<KeywordInfoDto, String>> cellFactory = p -> new EditingCell();

        keywordColumn.setCellFactory(cellFactory);
        keywordColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setText(t.getNewValue()));

        TableColumn operationColumn = new TableColumn("Operations");
        operationColumn.setSortable(false);
        table.getColumns().addAll(idColumn, keywordColumn, operationColumn);

        operationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KeywordInfoDto, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<KeywordInfoDto, Boolean> features) {
                return new SimpleBooleanProperty(features.getValue() != null);
            }
        });

        operationColumn.setCellFactory(personBooleanTableColumn -> new KeywordOperationCell(table));

        table.setEditable(true);

        refreshData(table);

        refreshButton.setOnAction(event -> refreshData(table));

        grid.add(keywordLabel, 0, 1);
        grid.add(keywordTextArea, 1, 1);
        grid.add(keywordAddButton, 2, 1);
        grid.add(clearTextAreaButton, 3, 1);
        grid.add(refreshButton, 4, 1);

        VBox root = new VBox();
        root.getChildren().addAll(grid, table);

        Scene scene = new Scene(root, 430, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshData(TableView<KeywordInfoDto> table) {
        final List<KeywordInfo> keywordInfoList = keywordInfoService.findAll();
        table.setItems(getKeywordInfoObservableList(keywordInfoList));
    }

    private ObservableList<KeywordInfoDto> getKeywordInfoObservableList(List<KeywordInfo> keywordInfoList) {

        final ObservableList<KeywordInfoDto> keywordInfoDtoObservableList = FXCollections.observableArrayList();
        keywordInfoDtoObservableList.addAll(keywordInfoList.stream().map(keywordInfo -> new KeywordInfoDto(keywordInfo.getId(), keywordInfo.getKeyword())).collect(Collectors.toList()));
        return keywordInfoDtoObservableList;
    }

    private void saveKeyword() {
        final String keywordText = keywordTextArea.getText();

        if (keywordText != null && !keywordText.isEmpty()) {
            keywordInfoService.persist(keywordText);
            clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Keyword can not be empty!");
            alert.show();
        }
    }

    private void clear() {
        keywordTextArea.setText("");
    }

    private class KeywordOperationCell extends TableCell<KeywordInfoDto, Boolean> {
        final Button deleteButton = new Button("Delete");
        final Button updateButton = new Button("Update");

        final StackPane paddedButton = new StackPane();

        KeywordOperationCell(TableView<KeywordInfoDto> table) {
            paddedButton.setPadding(new Insets(3));
            HBox vBox = new HBox();
            vBox.setSpacing(10);
            vBox.getChildren().addAll(deleteButton, updateButton);
            paddedButton.getChildren().add(vBox);

            deleteButton.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof KeywordInfoDto) {
                    final KeywordInfoDto keywordInfoDto = (KeywordInfoDto) item;
                    keywordInfoService.remove(keywordInfoDto.getId());
                    refreshData(table);
                }
            });
            updateButton.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof KeywordInfoDto) {
                    final KeywordInfoDto keywordInfoDto = (KeywordInfoDto) item;
                    keywordInfoService.update(keywordInfoDto);
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

    private class EditingCell extends TableCell<KeywordInfoDto, String> {

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