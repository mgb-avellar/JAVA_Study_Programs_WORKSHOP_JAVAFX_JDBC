package application;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;

    // Acima, as correspondências com os botões do Menu de nossa tela principal.
    // Abaixo, declaramos os métodos para tratar cada uma das ações dos itens do menu
    //   (implementação temporária no momento)

    @FXML
    public void onMenuItemSellerAction() {
        System.out.println("onMenuItemSellerAction");
    }
    @FXML
    public void onMenuItemDepartmentAction() {
        //System.out.println("onMenuItemDepartmentAction");
        loadView("/gui/DepartmentList.fxml");
    }
    @FXML
    public void onMenuItemAboutAction() {
        //System.out.println("onMenuItemAboutAction");
        loadView("/gui/About.fxml");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    // Vamos criar um método que carrega uma nova tela

    private synchronized void loadView(String absoluteName) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(absoluteName));

        try {
            VBox newVBox = fxmlLoader.load();

            // Para carregar essa view dentro da tela principal, preciso modificar a classe Main
            //  com um 'private static Scene mainScene' (vide classe Main)

            Scene mainScene = Main.getMainScene();

            // Agora, preciso criar uma referência para o VBox que está dentro da tela
            //  principal que, por sua vez, é um ScrollPane.

            VBox mainVBox = (VBox) ( (ScrollPane) mainScene.getRoot() ).getContent();

            // Para mostrar a tela do About, tenho que excluir todo o content da tela principal
            //   e colocar no lugar o que quero mostrar

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();

            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            /*
            Com isso, consegui manipular a cena principal incluindo nela, além do main menu,
            os filhos da janela que eu estiver abrindo.
             */

        }
        catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
