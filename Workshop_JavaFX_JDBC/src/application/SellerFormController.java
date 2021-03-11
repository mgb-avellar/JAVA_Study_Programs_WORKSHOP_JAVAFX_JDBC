package application;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    /*
    Lembrando que o SellerForm serve para adicionar ou atualizar um vendedor
    no nosso banco de dados, precisamos instanciar um objeto Seller aqui.
    Além disso, será preciso atualizar a classe SellerListController em suas
    ações e métodos
     */

    // Criando a dependência com a classe Seller

    private Seller seller;

    // Criando a dependência com a classe SellerService

    private SellerService sellerService;

    // Criando a dependência com a classe DepartmentService (necessário para
    //    podermos escolher o departamento que atribuiremos ao novo vendedor
    //    adicionado; faremos isso via ComboBox)

    private DepartmentService departmentService;

    /*
    Em se falando da atualização automática da tela de vendedor quando inserimos um novo vendedor,
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
    private TextField txtEmail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtBaseSalary;

    @FXML
    private ComboBox<Department> comboBoxDepartment;  // Para escolhermos o departamento do vendedor adicionado

    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    private ObservableList<Department> departmentObservableList;

    // Método para tratar o novo seller

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    // Método para tratar o novo seller service

    public void setServices(SellerService sellerService, DepartmentService departmentService) {
        this.sellerService = sellerService;
        this.departmentService = departmentService;

        /*
        Esse método também precisa ser atualizado para a injeção da dependência com
        o departamento. Inclusive o nome do método foi mudado (antigo: 'setSellerService')
        Precisaremos também de um método loadAssociatedObjects() (ver ao final desta classe)
        para ser chamado no método createDialogForm(...) da classe SellerListController.
         */
    }

    public void subscribeDataChangeListenerList(DataChangeListener listener) {

        dataChangeListenerList.add(listener);
        /*
         Médodo de inscrição na lista de objetos que receberão o evento disparado;
         após o salvamento do novo vendedor for efetuado com sucesso, um método deve
         notificar os meus listeners. Esse método será chamado dentro de 'onBtSaveAction'.
         */
    }

    public void updateFormData() {

        // Programação defensiva

        if (seller == null) {
            throw new IllegalStateException("Seller was null.");
        }

        txtId.setText(String.valueOf(seller.getId()));
        txtName.setText(seller.getName());
        txtEmail.setText(seller.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
        if (seller.getBirthDate() != null) {
            dpBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }

        if (seller.getDepartment() == null) {

            comboBoxDepartment.getSelectionModel().selectFirst();
        }
        else {
            comboBoxDepartment.setValue(seller.getDepartment());
        }

        /*
        A questão de dpBirthDate é complicada, pois o seller.getBirthDate trabalha com a dada do Java.utils,
        enquanto o dpBirthDate.setValue trabalha com o Local Date.
        O que fizemos acima foi passar do formato Java.utils para o LocalDate incluindo o formato da máquina
        localmente (ZoneId ...).
         */

        // A variável txtXXX trabalha com String, por isso o String.valueOf(...)
    }

    // Métodos para o tratamento dos botões.

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        //System.out.println("onBtSaveAction");

        /*
         Agora vamos ter que atualizar essa ação para salvar o novo vendedor no banco de dados.
         Note que o usuário vai digitar apenas o nome do novo vendedor na tela, pois o campo
         Id estará travado (nosso sistema encontrará automaticamente esse valor). Quando ele clicar
         em Save, o vendedor será salvo no banco.
         */

        // Programação defensiva

        if (seller == null) {

            throw new IllegalStateException("Seller was null");
        }
        if (sellerService == null) {

            throw new IllegalStateException("Seller service was null");
        }

        try {
            // operações com banco de dados sempre podem gerar exceções, por isso o try-catch
            seller = getFormData();
            /*
            Note que após a questão do Validation Exception, o getFormData pode lançar
            exceções também e precisamos tratar disso no catch.
             */
            sellerService.saveOrUpdate(seller);

            notifyDataChangeListeners();  // Método para notificação dos listeners

            Utils.currentStage(event).close();
            // A linha logo acima fecha a janela após salvamento. Note que inserimos um ActionEvent
            //   na chamada do método
        }
        catch (ValidationException e) {

            setErrorMessages(e.getErrors());
        }
        catch (DbException e) {

            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
        // ps. para que isso funcione, preciso injetar uma instância de SellerService
        //     em SellerListController
    }

    private void notifyDataChangeListeners() {

        for (DataChangeListener listener : dataChangeListenerList) {

            listener.onDataChanged();
        }
    }

    private Seller getFormData() {

        /*
        Aqui que precisamos verificar o código e lanças as exceções do ValidationException;
        para efeitos da aula, vamos apenas verificar se txtName é vazio ou não.
         */

        Seller obj = new Seller();

        ValidationException validationException = new ValidationException("Validation error");

        // Seller id
        obj.setId(Utils.tryParseToInt(txtId.getText()));

        // Seller name
        if (txtName.getText() == null || txtName.getText().trim().equals("")) {

            validationException.addError("name", "Field can't be empty.");
        }

        obj.setName(txtName.getText());

        // Seller email
        if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {

            validationException.addError("email", "Field can't be empty.");
        }

        obj.setEmail(txtEmail.getText());

        // Seller birthDate
        if (dpBirthDate.getValue() == null) {
            validationException.addError("birthDate", "Field can't be empty.");
        }
        else {

            Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));

            obj.setBirthDate(Date.from(instant));
        }

        // Seller baseSalary
        if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {

            validationException.addError("baseSalary", "Field can't be empty.");
        }

        obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

        // Seller department
        obj.setDepartment(comboBoxDepartment.getValue());


        if (validationException.getErrors().size() > 0) {

            throw validationException;
        }

        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        //System.out.println("onBtCancelAction");

        Utils.currentStage(event).close();

        // atualização do método para cancelar uma operação de salvamento de novo vendedor
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

        initializeComboBoxDepartment();
    }

    private void setErrorMessages(Map<String, String> errors) {

        /*
        Esse método é responsável por mostrar os erros (VelidationExceptions) na tela.
         */

        Set<String> fields = errors.keySet();

        if(fields.contains("name")) {

            labelErrorName.setText(errors.get("name"));
        }
        else {
            labelErrorName.setText("");
        }

        // Abaixo codificarei a mesma ideia para os outros campo, mas usando
        //   operadores ternários em substituição à estrutura if-else

        labelErrorEmail.setText( (fields.contains("email") ? errors.get("email") : "") );
        labelErrorBaseSalary.setText( (fields.contains("baseSalary") ? errors.get("baseSalary") : "") );
        labelErrorBirthDate.setText( (fields.contains("birthDate") ? errors.get("birthDate") : "") );

    }

    public void loadAssociatedObjects() {

        if (departmentService == null) {

            throw new IllegalStateException("Department service was null");
        }
        List<Department> list = departmentService.findAll();
        departmentObservableList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(departmentObservableList);
    }

    private void initializeComboBoxDepartment() {

        /*
        Obviamente, precisamos inicializar a ComboBox do departamento para ser escolhido.
        Esse método precisa ser chamado dentro do método initializeNodes().
         */

        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };

        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }



}
