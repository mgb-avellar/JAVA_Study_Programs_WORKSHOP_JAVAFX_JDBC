package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    // Método auxiliar que conecta com a base de dados
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {

            try {
                Properties properties = loadProperties();
                String url = properties.getProperty("dburl");
                connection = DriverManager.getConnection(url, properties);
            }
            catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return connection;
    }

    // Método auxiliar que fecha a conexão com a base de dados

    public static void closeConnection () {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    // Método auxiliar que carrega as propriedades que estão no darquivo db.properties
    private static Properties loadProperties() {

        try (FileInputStream fs =  new FileInputStream("db.properties")) {

                Properties properties = new Properties();
                properties.load(fs);
                return properties;
        }
        catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    // Métodos auxiliares para fechamento dos recursos abertos no Main

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            }
            catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }


}
