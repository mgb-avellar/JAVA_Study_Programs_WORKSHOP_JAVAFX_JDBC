package model.services;

import model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    /*
    Vou criar uma dependência dessa classe no DepartmentListController, para mostrar
    a lista de departamentos na tabela do department view.
     */


    // Método de procura que retorna uma lista de departamentos encontrados após a busca

    public List<Department> findAll() {

        // Neste momento do desenvolvimento, não buscaremos em banco de dados


       List<Department> list = new ArrayList<>();
       list.add(new Department(1, "Books"));
       list.add(new Department(2, "Computers"));
       list.add(new Department(3, "Electronics"));
       return list;


    }
}
