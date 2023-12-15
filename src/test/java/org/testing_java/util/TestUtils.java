package org.testing_java.util;

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testing_java.ConnectionPool;
import org.testing_java.User;

import java.sql.*;

public class TestUtils {
    private final ConnectionPool connectionPool;
    public TestUtils(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public void setUpData() throws SQLException {
        String createUserSql = "create table if not exists user (id int auto_increment primary key, name varchar(20) null);";
        String insertUserSql = "insert into user(name) values('Fred'), ('Lenka'), ('Scott')";
        try{
            Connection connection = this.connectionPool.getConnection();
            Statement createStatement = connection.createStatement();
            Statement insertStatement = connection.createStatement();
            createStatement.execute(createUserSql);
            insertStatement.execute(insertUserSql);
            connection.close();
        }
        catch (SQLException e){
            throw new SQLException(e);
        }
    }

    public User getRandomUser() throws SQLException {
        String query = "select id, name from user order by rand() limit 1";
        try{
            Connection connection = this.connectionPool.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            User user = new User(resultSet.getInt("id"), resultSet.getString("name"));
            connection.close();
            return user;
        }
        catch(SQLException e){
            throw new SQLException(e);
        }
    }

    public User getUserById( int id) throws SQLException {
        try{
            Connection connection = this.connectionPool.getConnection();
            String query = "SELECT id, name FROM user WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            User user = new User(rs.getInt("id"), rs.getString("name"));
            connection.close();
            return user;
        }
        catch(SQLException e){
            throw new SQLException(e);
        }
    }

    public String getRandomString(int length){
        return RandomStringUtils.randomAlphabetic(length);
    }
}
