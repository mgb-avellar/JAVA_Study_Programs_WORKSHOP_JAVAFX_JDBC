package application;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

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
    public void onBtNewAction() {

        System.out.println("onBtNewAction");
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


}

