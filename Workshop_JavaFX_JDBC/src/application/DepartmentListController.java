package application;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class DepartmentListController implements Initializable {

    // Criando a dependência para os department services

    private DepartmentService departmentService; // Não instanciaremos aqui para não criar dependência forte.

    // Para enviar a instanciação para outro lugar, criaremos um set, como o que se segue:
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // Para nos auxiliar a mostrar a lista na tableViewDepartment, crio uma observable list
    //   (criarei um método auxiliar ao final para nos ajudar nisso)

    private ObservableList<Department> departmentObservableList;

    // Criando referências para quatro entitades da tela de DepartmentList

    @FXML
    private TableView<Department> tableViewDepartment;
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnName;
    @FXML
    private Button btNew;

    // Métodos

    @FXML
    public void onBtNewAction(ActionEvent actionEvent) {

        //System.out.println("onBtNewAction");

        Stage parentStage = Utils.currentStage(actionEvent);
        // Para colocar um novo departamento, devo instanciá-lo e modificar o 'createDialogForm()'
        Department obj = new Department();
        createDialogForm(obj,"/gui/DepartmentForm.fxml", parentStage); // Entra 'obj'

        /*
        ps. note que agora o método recebe um 'ActionEvent actionEvent'.
         */

    }

    // Para a tabela funcionar, não basta declarar as variáveis acima.
    //  Precisamos de um método auxiliar chamado 'initializeNodes' para iniciar
    //  algum componente da minha tela

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeNodes();

    }

    private void initializeNodes() {

        // Preciso de alguns comando para iniciar corretamente o comportamento das colunas

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));

        // As duas linhas abaixo são um macete para que as dimensões da tabela acompanhem a da cena
        //   principal.
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableViewDepartment() {

        if (departmentService == null) {
            throw new IllegalStateException("Service was null");
        }

        List<Department> auxList = departmentService.findAll();
        departmentObservableList = FXCollections.observableArrayList(auxList);

        tableViewDepartment.setItems(departmentObservableList);
    }

    private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {

        /*
           Esse método será chamado pelo botão 'New' da janela de departamento,
           o que implica em chamá-lo no método 'onBtNewAction()' acima.
         */

        // Vamos instanciar a janela de diálogo

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = fxmlLoader.load();

            /*
               Quando preciso carregar uma janela modal na frente de uma janela existente,
               preciso instanciar um novo Stage: um palco na frente do outro.
               No entanto, o novo palco precisa ser ccnfigurado, o que fazemos nas linhas
               abaixo.
               ps. janela modal significa que enquanto você não a fechar, você não acessa
                   a janela anterior.
             */

            /*
               Esse método agora precisa injetar o Department obj no controlador da nossa tela de formulário
            */

            DepartmentFormController departmentFormController = fxmlLoader.getController();
            departmentFormController.setDepartment(obj);
            departmentFormController.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter department data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        }
        catch (IOException e) {

            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}

