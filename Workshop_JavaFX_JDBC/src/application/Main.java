package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));

        //Parent root = loader.load();

        /* Para ajustar minha cena à janela, faremos modificações aqui, não no Scene Builder
           A primeira coisa a fazer é comentar (deletar, na verdade, mas deixo isso para o
           próximo commit) a linha 'Parent root = loader.load();', fazendo novas, como
           as três próximas abaixo:
         */

        ScrollPane scrollPane = loader.load();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        Scene mainScene = new Scene(scrollPane);
        primaryStage.setTitle("Sample JavaFX application");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
