package com.example.traveler.service;

import com.example.traveler.model.User;
import org.springframework.stereotype.Service;

import com.example.traveler.dao.ConnDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static ConnDao connDao;

    public static void createTableUser() {
        String query = "" +
                "CREATE TABLE IF NOT EXISTS user (" +
                "userid INTEGER, " +
                "email	TEXT, " +
                "password TEXT, " +
                "roleid INTEGER, " +
                "accesstoken TEXT, " +
                "PRIMARY KEY('userid' AUTOINCREMENT) " +
                ");";

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("Table user has been created!");
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

    }

    public static void deleteTableUser() {
        String query = "DROP TABLE IF EXISTS user;";

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("Table user has been deleted!");
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

    }
    
    public static boolean insertUser(String email, String password, int roleId, String accessToken) {
        
        if (email == null || email == "") {
            return false;
        }

        if (password == null || password == "") {
            return false;
        }

        if (roleId != (int)roleId || (int)roleId < 1) {
            return false;
        }
        if (accessToken == null || accessToken == "") {
            return false;
        }
        /* Does NOT work on Java 8, needs Java 11+ maybe:
        String query = "" +
            "INSERT INTO user (userid, email, password, roleid, accesstoken) " +
            "VALUES (:userId, :email, :password, :roleId, :accessToken);";
         */

        /* Does NOT work on Java 8, needs Java 11+ maybe:
        String query = "" +
            "INSERT INTO user (userid, email, password, roleid, accesstoken) " +
            "VALUES (?, ?, ?, ?);";
        */

        /* This works: */
        String query = "" +
                "INSERT INTO user " +
                "(email, password, roleid, accesstoken) " +
                "VALUES " +
                "(" +
                "'" + email + "'" + ", " +
                "'" + password + "'" + ", " +
                "'" + roleId + "'" + ", " +
                "'" + accessToken + "'" +
                ");";

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("Users have been migrated!");
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        return false;

    }

    public static boolean editUser(int userId, String email, String password, int roleId, String accessToken) {
        
        if (userId != (int)userId || (int)userId < 1)  {
            return false;
        }

        if (email == null || email == "") {
            return false;
        }

        if (password == null || password == "") {
            return false;
        }

        if (roleId != (int)roleId || (int)roleId < 1)  {
            return false;
        }

        if (accessToken == null || accessToken == "") {
            return false;
        }

        String query = "UPDATE user SET " +
                "email = '" + email + "', " +
                "password = '" + password + "', " +
                "roleid = '" + roleId + "', " +
                "accesstoken = '" + accessToken + "'" +
                " WHERE userid = " + userId;

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("User has been edited!");
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        return false;

    }

    public static boolean deleteUser(int userId) {

        if (userId != (int)userId || (int)userId < 1)  {
            return false;
        }

        String query = "DELETE FROM user WHERE userid = " + userId;

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("User has been deleted!");
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        return false;

    }

    public static List<User> getUsers() {

        String sql = "SELECT * FROM user;";

        try {

            Connection conn = connDao.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<User> data = new ArrayList<>();

            while(rs.next()) {
                int userId = rs.getInt("userid");
                String email = rs.getString("email");
                String password = rs.getString("password");
                int roleId = rs.getInt("roleid");
                String accessToken = rs.getString("accesstoken");

                User dbRow = new User(userId, email, password, roleId, accessToken);
                data.add(dbRow);
            }

            return data;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public static void migrate() {
        deleteTableUser();
        createTableUser();
        insertUser("admin@yahoo.com", "admin", 1, "5Lsj3klGc7vno7PkX3HeN9s");
        // editUser(1, "admin@yahoo.com", "admin", 1, "5Lsj3klGc7vno7PkX3HeN9s");
        // deleteUser(1); // works
    }

    public static void printAllUsers() {

        List<User> users = getUsers();
        for (User user : users) {
            System.out.println(
                user.getUserId() + " " +
                user.getEmail() + " " +
                user.getPassword() + " " +
                user.getRoleId() + " " +
                user.getAccessToken()
            );
        }
    }

    public static User getUserOnLogin(String inputEmail, String inputPassword) {
        
        // Of course, it should be easier to do this using sql query in java code like this: 
        // .. String query = select * from user where email = :inputEmail and password = :inputPassword ..
        // just like in the editUser method, BUT let me show you another (not better but existed) approach: 

        List<User> users = getUsers();
        User user = new User();
                
        for (int i = 0; i < users.size(); i++) {
            
            String email = users.get(i).getEmail();
            String password = users.get(i).getPassword();
            
            if (email.equals(inputEmail) && password.equals(inputPassword)) {
                
                int userId = users.get(i).getUserId();
                int  roleId = users.get(i).getRoleId();
                String accessToken = users.get(i).getAccessToken();

                user.setUserId(userId);
                user.setEmail(email);
                user.setPassword(password);
                user.setRoleId(roleId);
                user.setAccessToken(accessToken);
                break;
            }
        }
        
        return (user.getUserId() > 0) ? user : null;
    }

    public static boolean hasRole(User user, int roleId) {
        return user.getRoleId() == roleId ? true : false;
    }
    
    
}
