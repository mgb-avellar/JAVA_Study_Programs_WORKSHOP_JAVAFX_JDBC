package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

    /*
    Com essa classe, abriremos uma janela a partir do Stage atual, por exemplo, ao clicar
    num botão.
     */

    public static Stage currentStage(ActionEvent actionEvent) {

        return (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        // Note os dois castings aqui
    }

    public static Integer tryParseToInt (String str) {

        try {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            return null;
        }

        // esse método vai nos ajudar a criar novos departamentos ou atualizar os dados de um
    }
}
