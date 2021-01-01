package at.ac.fhcampuswien.bouncingballs.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class homepage {

    @FXML private Text actiontarget;
    private ResourceBundle resources;

    @FXML protected void manageButton(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"), resources);
            Stage stage = new Stage();
            stage.setTitle("Bouncing-Balls");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}

