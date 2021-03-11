package model.services;

import model.entities.Department;
import model.dao.DaoFactory;
import model.dao.DepartmentDAO;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    /*
    Vou criar uma dependência dessa classe no DepartmentListController, para mostrar
    a lista de departamentos na tabela do department view.
     */

    private DepartmentDAO departmentDAO = DaoFactory.createDepartmentDAO();

    // Método de procura que retorna uma lista de departamentos encontrados após a busca

    public List<Department> findAll() {

        // Neste momento do desenvolvimento, buscaremos no banco de dados criado


        return departmentDAO.findAll();

        /*

        // Aqui era quando não buscávamos no banco de dados

       List<Department> list = new ArrayList<>();
       list.add(new Department(1, "Books"));
       list.add(new Department(2, "Computers"));
       list.add(new Department(3, "Electronics"));
       return list;

         */
    }

    public void saveOrUpdate(Department department) {

        if (department.getId() == null) {

            departmentDAO.insert(department);
        }
        else {

            departmentDAO.update(department);
        }

        /*
          Método que testa se queremos salvar um departamento novo ou
          atualizar um departamento existente.
         */
    }

    /*
    Vamos criar um método para remover um departamento.
     */

    public void removeDepartment(Department obj) {

        departmentDAO.deleteById(obj.getId());
    }

}
