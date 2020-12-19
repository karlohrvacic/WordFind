package main.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../resources/languageToggle.fxml"));
        mainStage.setTitle("English");
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        Main.mainStage = mainStage;
    }

    public static void setNewScene(Scene scene) {
        mainStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}