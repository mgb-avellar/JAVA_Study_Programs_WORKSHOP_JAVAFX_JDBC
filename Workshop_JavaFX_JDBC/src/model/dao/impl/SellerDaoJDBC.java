package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDAO {

    /*
    Esta é uma implementação JDBC do meu SellerDAO
     */

    private Connection connection;  // Esta conexão agora está disponível para uso em todos os métodos
    //   dentro desta classe.

    public SellerDaoJDBC(Connection connection) {

        this.connection = connection;
    }


    @Override
    public void insert(Seller seller) {

        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                    "INSERT INTO seller " +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "VALUES " +
                            "(?, ?, ?, ?, ?) ",
                    Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1 , seller.getSellerName());
            st.setString(2, seller.getSellerEmail());
            st.setDate(3, new Date(seller.getSellerBirthDate().getTime())); // Date é o do SQL
            st.setDouble(4, seller.getSellerBaseSalary());
            st.setInt(5, seller.getSellerDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {

                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {

                    int id = rs.getInt(1);
                    seller.setSellerID(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Unexpected error! No rows affected.");
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
    public void update(Seller seller) {

        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                    "UPDATE seller " +
                            "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                            "WHERE id = ? "
            );

            st.setString(1 , seller.getSellerName());
            st.setString(2, seller.getSellerEmail());
            st.setDate(3, new Date(seller.getSellerBirthDate().getTime())); // Date é o do SQL
            st.setDouble(4, seller.getSellerBaseSalary());
            st.setInt(5, seller.getSellerDepartment().getId());
            st.setInt(6, seller.getSellerID());

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
                    "DELETE FROM seller WHERE Id = ? "
            );

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
    public Seller findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = connection.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = ?"
            );

            st.setInt(1, id);
            rs = st.executeQuery();

            /*
            Caso nosso result set (rs) não encontre nada em nossa consulta, precisamos retornar nulo.
             */

            if (rs.next()) {

                /*
                O ResultSet retorna uma tabela de dados com os resultados de nossa consulta.
                É importante ter em mente que na memória precisaremos "desmembrar" essa tabela
                instanciando dois objetos, um Seller e suas informações e um Department associado
                com suas informações próprias.
                 */

                /*
                Primeiro, instanciaremos um departamento e setamos as informações dele
                 */

                Department dep = instantiateDepartment(rs);

                /*
                Agora precisamos instanciar um vendedor e suas informações, incluindo a associação
                com o departamento que instanciamos acima.
                 */

                Seller seller = instantiateSeller(rs, dep);

                return seller;
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {

            throw new DbException(e.getMessage());
        }
        finally {

            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {

        Seller seller = new Seller();

        seller.setSellerID(rs.getInt("Id"));
        seller.setSellerName(rs.getString("Name"));
        seller.setSellerEmail(rs.getString("Email"));
        seller.setSellerBaseSalary(rs.getDouble("BaseSalary"));
        seller.setSellerBirthDate(rs.getDate("BirthDate"));

        seller.setSellerDepartment(dep);  // Aqui está a associação com o departamento!!

        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {

        Department dep = new Department();

        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = connection.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name "
            );

            rs = st.executeQuery();

            List<Seller> sellerList = new ArrayList<>();

            /*
            É importante ter a noção de que sem tomar muito cuidado com o código,
            posso instanciar várias vezes o departamento, como discutido na aula.
            Para evitar isso, criaremos um mapa, que usaremos dentro do bloco de
            repetição abaixo.
             */

            Map<Integer, Department> map = new HashMap<>();

            while ( rs.next() ) {     // while em vez de 'if' porque posso ter mais de um vendedor no departamento

                /*
                A linha de comando abaixo busca no mapa criado se já há algum departamento
                com o id requerido. Se não houver, o map.get(...) retorna 'null' e então
                podemos instanciar o departamento; se houver, não faremos nova instância.
                 */
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {

                    dep = instantiateDepartment(rs); // Instancio department
                    map.put(rs.getInt("DepartmentId"), dep); // jogo a instância no mapa
                }

                Seller seller = instantiateSeller(rs, dep);

                sellerList.add(seller);
            }
            return sellerList;

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
    public List<Seller> findByDepartment(Department department) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = connection.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ? " +
                            "ORDER BY Name "
            );

            st.setInt(1, department.getId());
            rs = st.executeQuery();

            List<Seller> sellerList = new ArrayList<>();

            /*
            É importante ter a noção de que sem tomar muito cuidado com o código,
            posso instanciar várias vezes o departamento, como discutido na aula.
            Para evitar isso, criaremos um mapa, que usaremos dentro do bloco de
            repetição abaixo.
             */

            Map<Integer, Department> map = new HashMap<>();

            while ( rs.next() ) {     // while em vez de 'if' porque posso ter mais de um vendedor no departamento

                /*
                A linha de comando abaixo busca no mapa criado se já há algum departamento
                com o id requerido. Se não houver, o map.get(...) retorna 'null' e então
                podemos instanciar o departamento; se houver, não faremos nova instância.
                 */
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {

                    dep = instantiateDepartment(rs); // Instancio department
                    map.put(rs.getInt("DepartmentId"), dep); // jogo a instância no mapa
                }

                Seller seller = instantiateSeller(rs, dep);

                sellerList.add(seller);
            }
            return sellerList;

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
