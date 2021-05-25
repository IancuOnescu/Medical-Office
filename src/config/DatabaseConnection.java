package config;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection () {

    }

    public static Connection getInstance() throws SQLException {
        if(connection == null) {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("root");
            dataSource.setPassword("Iancuaremere");
            dataSource.setServerName("localhost");

            connection = dataSource.getConnection();
        }
        return connection;
    }
}
