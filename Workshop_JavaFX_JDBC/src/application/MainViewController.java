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
import model.services.DepartmentService;
import model.services.SellerService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
        //System.out.println("onMenuItemSellerAction");

        loadView("/gui/SellerList.fxml", (SellerListController controller) ->
                {
                    controller.setSellerService(new SellerService());
                    controller.updateTableViewSeller();
                }
        );
    }
    @FXML
    public void onMenuItemDepartmentAction() {
        //System.out.println("onMenuItemDepartmentAction");
        //loadView2("/gui/DepartmentList.fxml");

        /*
            Nessa etapa da construção de nossa aplicação, vamos atualizar a rotina loadView()
            passando a inicialização do controle DepartmentListController que fizemos temporariamente
            na rotina loadView2() como parâmetro da função loadView() usando expressões lambda.
            Com isso, poderemos deletar a função loadView2() do nosso código.
            Então, aqui eu retorno à chamada de loadView() já passando uma função como parâmetro.
            Além disso, como o método loadView() agora recebe dois parâmetros, preciso atualizar
            também a ação da tela About, colocando mais um parâmetro em seu loadView(). (Vide a seguir.)
            Por fim, note que precisaremos atualizar o próprio método de loadView() para receber a
            função de inicialização, por meio de um Consumer.
        */

        loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) ->
                {
                    controller.setDepartmentService(new DepartmentService());
                    controller.updateTableViewDepartment();
                }
        );

    }
    @FXML
    public void onMenuItemAboutAction() {
        //System.out.println("onMenuItemAboutAction");
        loadView("/gui/About.fxml", x -> {});
        /*
        Note que como a tela About não tem nada de especial, p. ex., não precisa carregar uma lista,
        o parâmetro de inicialização da expressão lambda é uma função vazia.
         */
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    // Vamos criar um método que carrega uma nova tela

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {

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

            /*
            Depois de carregar a tela com os comandos acima, vamos acrescentar um comando para ativar
            a função passada como Consumer.
             */

            T controller = fxmlLoader.getController();
            initializingAction.accept(controller);

            /*
            O que tem que ficar claro das duas linhas acima é que o initializingAction executará
            o que passamos como função lambda em loadView().
            No caso da atualização da tela Department, executará os seguintes comandos:

                DepartmentListController controller;
                controller.setDepartmentService(new DepartmentService());
                controller.updateTableViewDepartment();

            No caso da tela About, executará:

                {}

             */

        }
        catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
