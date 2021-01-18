package at.ac.fhcampuswien.bouncingballs.controllers;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Homepage {

    @FXML private Text actionTarget;

    @FXML
    private TextField pText;

    @FXML
    private TextField iText;

    private ResourceBundle resources;

    @FXML protected void manageButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));

            SimulationValues.setBallCount(Integer.parseInt(pText.getText()));
            SimulationValues.setInitalInfections(Integer.parseInt(iText.getText()));
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

