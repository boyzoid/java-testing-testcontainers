package org.testing_java;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    private final ConnectionPool connectionPool;

    public UserService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
    public User getUser( int id) throws SQLException {
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

    public User createUser(User user) throws SQLException {
        try{
            Connection connection = this.connectionPool.getConnection();
            String query = "INSERT INTO user (name) VALUES (?)";
            PreparedStatement pstmt = connection.prepareStatement(
                    query,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            pstmt.setString(1, user.getName());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                user.setId(rs.getInt(1));
            }
            else {
                throw new SQLException("Failed to create user");
            }
            connection.close();
        }
        catch (SQLException e){
            throw new SQLException(e);
        }
        return user;
    }
}
