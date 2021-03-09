package application;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    /*
    Lembrando que o DepartmentForm serve para adicionar ou atualizar um departamento
    no nosso banco de dados, precisamos instanciar um objeto Department aqui.
    Além disso, será preciso atualizar a classe DepartmentListController em suas
    ações e métodos
     */

    // Criando a dependência com a classe Department

    private Department department;

    // Aqui vai a declaração dos componentes da tela

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Label labelErrorName;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    // Método para tratar o novo department

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void updateFormData() {

        // Programação defensiva

        if (department == null) {
            throw new IllegalStateException("Department was null.");
        }

        txtId.setText(String.valueOf(department.getId()));
        txtName.setText(department.getName());

        // A variável txtXXX trabalha com String, por isso o String.valueOf(...)
    }

    // Métodos para o tratamento dos botões.

    @FXML
    public void onBtSaveAction() {
        System.out.println("onBtSaveAction");
    }
    @FXML
    public void onBtCancelAction() {
        System.out.println("onBtCancelAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
}
