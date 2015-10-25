package com.bilalalp.patentsearcher.util;

import javafx.scene.control.Alert;

public final class GuiAlertUtil {

    private GuiAlertUtil(){
        //Util Class
    }

    public static Alert siteInfoMustBeSelected(){
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("SearchInfo must be selected.!");
        return alert;
    }


}
