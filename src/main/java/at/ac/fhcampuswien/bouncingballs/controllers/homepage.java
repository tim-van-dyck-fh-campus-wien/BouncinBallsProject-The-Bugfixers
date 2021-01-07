package at.ac.fhcampuswien.bouncingballs.controllers;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
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

    @FXML
    private TextField ptext;

    @FXML
    private TextField itext;

    private ResourceBundle resources;

    @FXML protected void manageButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));

            SimulationValues.setBallCount(Integer.parseInt(ptext.getText()));
            SimulationValues.setInitalInfections(Integer.parseInt(itext.getText()));
            MainController controller = new MainController();
            loader.setController(controller);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Bouncing-Balls");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
            stage.setOnHidden(e-> controller.shutdown());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}

