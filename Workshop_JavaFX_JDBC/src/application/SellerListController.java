package application;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class SellerListController implements Initializable, DataChangeListener {

    // Criando a dependência para os seller services

    private SellerService sellerService; // Não instanciaremos aqui para não criar dependência forte.

    // Para enviar a instanciação para outro lugar, criaremos um set, como o que se segue:
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    // Para nos auxiliar a mostrar a lista na tableViewSeller, crio uma observable list
    //   (criarei um método auxiliar ao final para nos ajudar nisso)

    private ObservableList<Seller> sellerObservableList;

    // Criando referências para quatro entitades da tela de SellerList

    @FXML
    private TableView<Seller> tableViewSeller;
    @FXML
    private TableColumn<Seller, Integer> tableColumnId;
    @FXML
    private TableColumn<Seller, String> tableColumnName;
    @FXML
    private TableColumn<Seller, String> tableColumnEmail;
    @FXML
    private TableColumn<Seller, Date> tableColumnBirthDate;
    @FXML
    private TableColumn<Seller, Double> tableColumnBaseSalary;
    @FXML
    private Button btNew;

    /*
    A declaração de atributo a seguir nos ajudará a atualizar um vendedor, criando vários
    botões para atualização, um para cada linha da minha tabela.
    (Pegaremos um método pronto para isso chamado initEditButtons, que deverá ser invocado em
    updateTableViewSeller(). )
     */

    @FXML
    private TableColumn<Seller, Seller> tableColumnEDIT;

    /*
    Faremos uma codificação para remover um vendedor do banco de dados, de modo bastante similar
    ao que fizemos para a atualização dos vendedores.
     */

    @FXML
    private TableColumn<Seller, Seller> tableColumnREMOVE;

    // Métodos

    @FXML
    public void onBtNewAction(ActionEvent actionEvent) {

        //System.out.println("onBtNewAction");

        Stage parentStage = Utils.currentStage(actionEvent);
        // Para colocar um novo vendedor, devo instanciá-lo e modificar o 'createDialogForm()'
        Seller obj = new Seller();
        createDialogForm(obj,"/gui/SellerForm.fxml", parentStage); // Entra 'obj'

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
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("BirthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("BaseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);

        // As duas linhas abaixo são um macete para que as dimensões da tabela acompanhem a da cena
        //   principal.
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableViewSeller() {

        if (sellerService == null) {
            throw new IllegalStateException("Service was null");
        }

        List<Seller> auxList = sellerService.findAll();
        sellerObservableList = FXCollections.observableArrayList(auxList);

        tableViewSeller.setItems(sellerObservableList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {

        /*
           Esse método será chamado pelo botão 'New' da janela de vendedor,
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
               Esse método agora precisa injetar o Seller obj no controlador da nossa tela de
               formulário.
            */

            SellerFormController sellerFormController = fxmlLoader.getController();
            sellerFormController.setSeller(obj);
            sellerFormController.setSellerService(new SellerService());
            /*
            Para a linha acima, ver comentário em 'onBtSaveAction' de 'SellerFormController'
             */

            sellerFormController.subscribeDataChangeListenerList(this); // Inscrição na lista

            sellerFormController.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter seller data");
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

    /*
    Em se falando da atualização automática da tela de vendedor quando inserimos um novo vendedor,
    essa classe é o que chamamos de 'observer' ou 'listener', ou seja, aquela que receber (ou ouve) o evento.
    Para isso, essa classe deve implementar também a interface 'DataChangeListener' e implementar seu método.
    Além disso, em 'createDialogForm' devo inscrever esta classe (objeto) na lista de ouvintes do evento emitido.
    */

    @Override
    public void onDataChanged() {

        updateTableViewSeller();
        /*
        Ao atualizar a tela, o novo vendedor inserido aparecerá automaticamente na tela.
         */
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("Edit");
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/SellerForm.fxml",Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
                    private final Button button = new Button("Remove");
                    @Override
                    protected void updateItem(Seller obj, boolean empty) {
                        super.updateItem(obj, empty);
                        if (obj == null) {
                            setGraphic(null);
                            return;
                        }

                        setGraphic(button);
                        button.setOnAction(event -> removeEntity(obj));
                    }
        });

    }

    private void removeEntity(Seller obj) {

        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

        if (result.get() == ButtonType.OK) {

            if (sellerService == null) {

                throw new IllegalStateException("Service was null");
            }
            try {

                sellerService.removeSeller(obj);
                updateTableViewSeller();
            }
            catch (DbException e) {

                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }

        }

    }
}

