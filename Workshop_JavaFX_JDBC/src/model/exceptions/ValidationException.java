package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

    /*
    Essa classe servirá para carregar as mensagens de erro, caso existam, pois
    ela serve para validar o formulário.
     */

    private static final long serialVersionUID = 1L;

    private Map<String, String> errors = new HashMap<>();

    /*
    Note que o Map precisa de dois campos: aqui, o nome do campo de origem do erro, e
    a mensagem de erro propriamente dita. Exemplo:

    campo: 'Name', erro: precisa ser string
    campo: 'Id', erro: precisa ser int

    etc
     */

    public ValidationException(String msg) {

        super(msg);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String errorMessage) {

        errors.put(fieldName, errorMessage);

        // Aqui, adicionamos os erros no Map.
    }

}
