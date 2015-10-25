package com.bilalalp.patentsearcher.gui.table;

import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class KeywordCheckBoxCell extends TableCell<KeywordInfoDto, Boolean> {

    private final CheckBox deleteButton = new CheckBox();
    private final StackPane paddedButton = new StackPane();

    public KeywordCheckBoxCell() {
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