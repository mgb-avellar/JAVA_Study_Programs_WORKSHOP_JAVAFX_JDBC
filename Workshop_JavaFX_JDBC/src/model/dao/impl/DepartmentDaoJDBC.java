package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDAO;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDAO {

    private Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {

        // Preparando o statement para a inserção de um novo departamento.
        // Departamento só tem nome e id

        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                    "INSERT INTO department " +
                            "(Name) " +
                            "VALUES " +
                            "(?) ",
                    Statement.RETURN_GENERATED_KEYS
            );

            // Obtendo o nome:

            st.setString(1, department.getName());

            int rowsAffected = st.executeUpdate(); // # de linhas atualizadas

            if ( rowsAffected > 0) { // ou seja, se inseriu algo

                ResultSet rs = st.getGeneratedKeys();

                if (rs.next()) { // Se existe a linha

                    int id = rs.getInt(1); // pego o id gerado
                    department.setId(id);  // atribuo o id gerado ao novo departamento
                }
                else {

                    throw new DbException("Unexpected error! No rows affected!");
                    // ou seja, nenhum departamento novo foi criado
                }
            }


        }
        catch (SQLException e) {

            throw new DbException(e.getMessage());
        }
        finally {

            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department department) {

        PreparedStatement st = null;

        try {

            st = connection.prepareStatement(
                    "UPDATE department " +
                            "SET Name = ? " +
                            "WHERE Id = ?"
            );

            st.setString(1, department.getName());
            st.setInt(2, department.getId());

            st.executeUpdate();

        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement st = null;

        try {

            st = connection.prepareStatement(
                    "DELETE FROM department WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();

        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = connection.prepareStatement(
                    "SELECT * FROM department " +
                            "WHERE Id = ? "
            );

            st.setInt(1, id); // Faço a seleção pelo id ao fornecê-lo.
            rs = st.executeQuery(); // Executo a seleção e jogo o resultado acima obtido
            //   no meu conjunto resultado

            if (rs.next()) { // Se encontrei algo, então

                // ... crio uma variável tipo Department para mostrar como resultado
                Department department = new Department();

                department.setName(rs.getString("Name")); // Pego o nome encontrado
                department.setId(rs.getInt("Id"));  // Pego o id encontrado

                return department; // Devolvo o resultado da busca como uma variável Department
            }

            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    @Override
    public List<Department> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = connection.prepareStatement(
                    "SELECT * FROM department ORDER BY Name "
            );

            // Executando a busca:

            rs = st.executeQuery();

            // Como estou procurando por todos os departamentos, os resultados formam uma lista de resultados
            //   Então, inicio uma lista para armazenar meus achados

            List<Department> departmentList = new ArrayList<>();

            // Como é uma lista, preciso percorrer toda a lista com uma estrutura repetitiva como while
            //  para colocar cada departamento encontrado na lista criada

            while ( rs.next()) {

                // Conforme vou percorrendo o conjunto resultado rs com os resultados,
                //  vou instanciando os departamentos e depois jogando-os na lista
                Department department = new Department();

                department.setName(rs.getString("Name"));
                department.setId(rs.getInt("Id"));

                departmentList.add(department);

            }

            // Retorno a lista com os departamentos encontrados
            return departmentList;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }
}
