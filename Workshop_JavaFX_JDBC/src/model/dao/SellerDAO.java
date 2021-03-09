package model.dao;

import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public interface SellerDAO {

    void insert(Seller seller);
    void update(Seller seller);
    void deleteById(Integer id);
    Seller findById(Integer id);  // Encontro o vendedor por sua id
    List<Seller> findAll();       // Encontro todos os vendedores e jogo numa lista de vendedores

    // Vamos implementar um novo método, o findByDepartment()
    // Um departamento pode ter mais de um vendedor, de modo que armazenaremos numa lista

    List<Seller> findByDepartment(Department department);


}
