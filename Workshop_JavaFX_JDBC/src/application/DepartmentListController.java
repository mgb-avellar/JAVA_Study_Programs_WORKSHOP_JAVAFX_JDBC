package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

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

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // As duas linhas abaixo são um macete para que as dimensões da tabela acompanhem a da cena
        //   principal.
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }
}
