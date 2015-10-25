package com.bilalalp.patentsearcher.util;

import javafx.scene.control.Alert;

public final class GuiAlertUtil {

    private GuiAlertUtil() {
        //Util Class
    }

    public static Alert searchInfoMustBeSelected() {
        return getErrorAlert("SearchInfo must be selected.!");
    }

    public static Alert siteMustBeSelected() {
        return getErrorAlert("Site must be selected.!");
    }

    public static Alert keywordsMustBeSelected(){
        return getErrorAlert("Keywords must be selected.!");
    }

    private static Alert getErrorAlert(final String message) {

        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        return alert;
    }
}
