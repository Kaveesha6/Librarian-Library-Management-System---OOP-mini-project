/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import librarian.Database;

/**
 *
 * @author TiVA
 */
public class UserController {

    public int login(String username, String password) throws Exception {
        int result = 0;
        String query = "SELECT * FROM user WHERE username=? AND password=?";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            result = 1;
        }
        return result;
    }
}
