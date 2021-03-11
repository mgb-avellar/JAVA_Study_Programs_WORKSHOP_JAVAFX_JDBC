package model.services;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Seller;

import java.util.List;

public class SellerService {

    /*
    Criar um seller é diferente de criar um department; enquanto um department é uma entidade
    única e independente, cada seller é associado a um department, de modo que quando criarmos
    um seller, será preciso escolher um department para associá-lo, via, por exemplo, uma ComboBox.
     */

    /*
    Vou criar uma dependência dessa classe no SellerListController, para mostrar
    a lista de departamentos na tabela do seller view.
     */

    private SellerDAO sellerDAO = DaoFactory.createSellerDAO();

    // Método de procura que retorna uma lista de departamentos encontrados após a busca

    public List<Seller> findAll() {

        // Neste momento do desenvolvimento, buscaremos no banco de dados criado


        return sellerDAO.findAll();

    }

    public void saveOrUpdate(Seller seller) {

        if (seller.getId() == null) {

            sellerDAO.insert(seller);
        }
        else {

            sellerDAO.update(seller);
        }

        /*
          Método que testa se queremos salvar um departamento novo ou
          atualizar um departamento existente.
         */
    }

    /*
    Vamos criar um método para remover um departamento.
     */

    public void removeSeller(Seller obj) {

        sellerDAO.deleteById(obj.getId());
    }

}
