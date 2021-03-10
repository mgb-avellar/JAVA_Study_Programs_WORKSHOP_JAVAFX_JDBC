package gui.listeners;

public interface DataChangeListener {

    /*
    Esta interface é criada para ajudar na questão da atualização automática da tela de
    departamentos após a inserção de um novo departamento.
     */

    void onDataChanged();

    // A operação acima dispara um evento quando os dados mudarem.
}
