package com.bilalalp.patentsearcher.gui;

import com.bilalalp.patentsearcher.config.PatentSearcherConfiguration;
import com.bilalalp.patentsearcher.dto.KeywordDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.service.keyword.KeywordInfoService;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class KeywordInfoUI extends Application {

    final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(PatentSearcherConfiguration.class);
    final KeywordInfoService keywordInfoService = annotationConfigApplicationContext.getBean(KeywordInfoService.class);

    Label keywordLabel = new Label("Keyword : ");
    TextField keywordTextArea = new TextField();

    Button keywordAddButton = new Button("Add");
    Button clearTextArea = new Button("Clear");
    private TableView<KeywordDto> table = new TableView<>();

    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("Keyword Operations");
        primaryStage.setResizable(false);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        clearTextArea.setOnAction(event -> clear());
        keywordAddButton.setOnAction(event -> {
            saveKeyword();
        });

        TableColumn<KeywordDto, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<KeywordDto, String> keywordColumn = new TableColumn<>("Keyword");
        keywordColumn.setMinWidth(200);
        keywordColumn.setEditable(true);
        keywordColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        Callback<TableColumn<KeywordDto, String>, TableCell<KeywordDto, String>> cellFactory = p -> new EditingCell();

        keywordColumn.setCellFactory(cellFactory);

        keywordColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setText(t.getNewValue()));

        TableColumn operationColumn = new TableColumn("Operations");
        operationColumn.setSortable(false);
        table.getColumns().addAll(idColumn, keywordColumn, operationColumn);

        operationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KeywordDto, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<KeywordDto, Boolean> features) {
                return new SimpleBooleanProperty(features.getValue() != null);
            }
        });

        operationColumn.setCellFactory(personBooleanTableColumn -> new KeywordOperationCell());

        table.setEditable(true);

        refreshData();

        grid.add(keywordLabel, 0, 1);
        grid.add(keywordTextArea, 1, 1);
        grid.add(keywordAddButton, 2, 1);
        grid.add(clearTextArea, 3, 1);

        VBox root = new VBox();
        root.getChildren().addAll(grid, table);

        Scene scene = new Scene(root, 400, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshData() {
        final List<KeywordInfo> keywordInfoList = keywordInfoService.findAll();
        table.setItems(getKeywordInfoObservableList(keywordInfoList));
    }

    private ObservableList<KeywordDto> getKeywordInfoObservableList(List<KeywordInfo> keywordInfoList) {

        final ObservableList<KeywordDto> keywordDtoObservableList = FXCollections.observableArrayList();

        for (final KeywordInfo keywordInfo : keywordInfoList) {
            keywordDtoObservableList.add(new KeywordDto(keywordInfo.getId(), keywordInfo.getKeyword()));
        }

        return keywordDtoObservableList;
    }

    private void saveKeyword() {
        final String keywordText = keywordTextArea.getText();

        if (keywordText != null && !keywordText.isEmpty()) {
            keywordInfoService.persist(keywordText);
            clear();
            refreshData();
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

    private class KeywordOperationCell extends TableCell<KeywordDto, Boolean> {
        final Button deleteButton = new Button("Delete");
        final Button updateButton = new Button("Update");
        final StackPane paddedButton = new StackPane();

        KeywordOperationCell() {
            paddedButton.setPadding(new Insets(3));
            HBox vBox = new HBox();
            vBox.setSpacing(10);
            vBox.getChildren().addAll(deleteButton, updateButton);
            paddedButton.getChildren().add(vBox);

            deleteButton.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof KeywordDto) {
                    final KeywordDto keywordDto = (KeywordDto) item;
                    keywordInfoService.remove(keywordDto.getId());
                    refreshData();
                }
            });
            updateButton.setOnAction(actionEvent -> {
                final Object item = getTableRow().getItem();

                if (item != null && item instanceof KeywordDto) {
                    final KeywordDto keywordDto = (KeywordDto) item;
                    keywordInfoService.update(keywordDto);
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

    private class EditingCell extends TableCell<KeywordDto, String> {

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