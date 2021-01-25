package at.ac.fhcampuswien.bouncingballs.controllers;
import at.ac.fhcampuswien.bouncingballs.balls.InfectableBalls;
import at.ac.fhcampuswien.bouncingballs.params.InfectableBallsParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    @FXML
    private TextField bText;

    private ResourceBundle resources;


    @FXML protected void manageButton(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));    //Start the simulation by pressing the button

            String pTextNoSpace = pText.getText().replaceAll(" +","");
            String iTextNoSpace = iText.getText().replaceAll(" +","");
            String bTextNoSpace =bText.getText().replaceAll(" +","");

            int population;
            int initialInfections;
            int ballRadius;

            try{
                population = Integer.parseInt(pTextNoSpace);
                initialInfections = Integer.parseInt(iTextNoSpace);
                ballRadius = Integer.parseInt(bTextNoSpace);
            }
            catch(NumberFormatException e){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("Please enter numbers!");
                errorAlert.showAndWait();

                return;
            }
            SimulationValues.setBallCount(population);
            SimulationValues.setInitalInfections(initialInfections);
            InfectableBallsParams.setBallradius(ballRadius);


            if (population > 5000) {            //check Population
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The Population size must be between 1 and 5000!");
                errorAlert.showAndWait();
                ;


            }else if(ballRadius <= 0 || population <= 0 || initialInfections <= 0){      //check if positive number
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("Please enter values greater than 0!");
                errorAlert.showAndWait();
            }
        else if(ballRadius > 20 ){              //check radius
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Ballradius must not be greater than 20!");
            errorAlert.showAndWait();
        }
            else if(initialInfections > population ){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("Population has to be greater than Infectiousness!");
                errorAlert.showAndWait();
            } else if(!InfectableBalls.checkIfBallsFitInCanvas(population,initialInfections)){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("The Balls dont fit");
                errorAlert.setContentText("The Balls do not fit on the Canvas! Please reduce the Population-Count or the Ballradius");
                errorAlert.showAndWait();
            }

            else {

                MainController controller = new MainController();               //start the simulation
                loader.setController(controller);
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setTitle("Bouncing-Balls");
                stage.setScene(new Scene(root, 1280, 720));
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                //stage.setOnHidden(e-> controller.shutdown());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}

