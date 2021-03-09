package model.dao;

import model.entities.Department;

import java.util.List;

public interface DepartmentDAO {

    void insert(Department department);
    void update(Department department);
    void deleteById(Integer id);
    Department findById(Integer id);  // Encontro o departamento por sua id
    List<Department> findAll();       // Encontro todos os departamentos e jogo numa lista de departamentos
}
