package application;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    // Criando a dependência com a classe DepartmentService

    private DepartmentService departmentService;

    /*
    Em se falando da atualização automática da tela de departamento quando inserimos um novo departamento,
    essa classe é o que chamamos de 'subject', ou seja, aquela que emite o evento. Para isso, cria-se
    uma lista de objetos interessados em receber o evento e um método de subscribers para que objetos
    se inscrevam na lista.
     */

    private List<DataChangeListener> dataChangeListenerList = new ArrayList<>();

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

    // Método para tratar o novo department service

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void subscribeDataChangeListenerList(DataChangeListener listener) {

        dataChangeListenerList.add(listener);
        /*
         Médodo de inscrição na lista de objetos que receberão o evento disparado;
         após o salvamento do novo departamento for efetuado com sucesso, um método deve
         notificar os meus listeners. Esse método será chamado dentro de 'onBtSaveAction'.
         */
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
    public void onBtSaveAction(ActionEvent event) {
        //System.out.println("onBtSaveAction");

        /*
         Agora vamos ter que atualizar essa ação para salvar o novo departamento no banco de dados.
         Note que o usuário vai digitar apenas o nome do novo departamento na tela, pois o campo
         Id estará travado (nosso sistema encontrará automaticamente esse valor). Quando ele clicar
         em Save, o departamento será salvo no banco.
         */

        // Programação defensiva

        if (department == null) {

            throw new IllegalStateException("Department was null");
        }
        if (departmentService == null) {

            throw new IllegalStateException("Department service was null");
        }

        try {
            // operações com banco de dados sempre podem gerar exceções, por isso o try-catch
            department = getFormData();
            departmentService.saveOrUpdate(department);

            notifyDataChangeListeners();  // Método para notificação dos listeners

            Utils.currentStage(event).close();
            // A linha logo acima fecha a janela após salvamento. Note que inserimos um ActionEvent
            //   na chamada do método
        }
        catch (DbException e) {

            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
        // ps. para que isso funcione, preciso injetar uma instância de DepartmentService
        //     em DepartmentListController
    }

    private void notifyDataChangeListeners() {

        for (DataChangeListener listener : dataChangeListenerList) {

            listener.onDataChanged();
        }
    }

    private Department getFormData() {

        Department obj = new Department();
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setName(txtName.getText());

        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        //System.out.println("onBtCancelAction");

        Utils.currentStage(event).close();

        // atualização do método para cancelar uma operação de salvamento de novo departamento
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
