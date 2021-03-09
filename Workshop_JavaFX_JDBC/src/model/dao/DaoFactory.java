package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    /*
    Esta classe será responsável por instanciar meus DAOs
     */

    public static SellerDAO createSellerDAO () {

        return new SellerDaoJDBC(DB.getConnection());  // Depois da implementação de findById em SellerDaoJDBC
        //   precisamos passar o DB.getConnection() aqui

        /*
        O que acontece é que o método retorna um objeto interface SellerDAO,
        mas internamente instancia uma implementação.
        Assim, não precisamos expor a implementação, apenas a interface.
         */
    }

    public static DepartmentDAO createDepartmentDAO () {

        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
