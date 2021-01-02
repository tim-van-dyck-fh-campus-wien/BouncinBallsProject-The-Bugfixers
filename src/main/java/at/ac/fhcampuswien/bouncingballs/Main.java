package at.ac.fhcampuswien.bouncingballs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/homepage.fxml"));

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add("/style.css");
        //primaryStage.setFullScreen(true);
        // We can add EventHandler also to the scene if appropriate: clicking, exiting...

        // Animation: either Timeline with Keyframe or
        // AnimationTimer http://zetcode.com/gui/javafx/animation/

        primaryStage.setTitle("Bouncing-Balls");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
