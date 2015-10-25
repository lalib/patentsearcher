package com.bilalalp.patentsearcher.gui.table;

import com.bilalalp.patentsearcher.dto.StopWordInfoDto;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class StopWordCheckBoxCell extends TableCell<StopWordInfoDto, Boolean> {

    private final CheckBox checkBox = new CheckBox();
    private final StackPane stackPane = new StackPane();

    public StopWordCheckBoxCell() {
        stackPane.setPadding(new Insets(3));
        HBox vBox = new HBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(checkBox);
        stackPane.getChildren().add(vBox);

        checkBox.setOnAction(actionEvent -> {
            final Object item = getTableRow().getItem();

            if (item != null && item instanceof StopWordInfoDto) {
                final StopWordInfoDto keywordInfoDto = (StopWordInfoDto) item;
                keywordInfoDto.setSelected(checkBox.isSelected());
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
