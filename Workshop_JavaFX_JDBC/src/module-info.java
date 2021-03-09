module Workshop.JavaFX.JDBC {

    requires javafx.controls;
    requires javafx.fxml;
    opens application;
    opens model.entities;  // Essa linha faz a atualização da tabela na interface gráfica ser atualizada
}