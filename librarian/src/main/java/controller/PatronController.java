/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import librarian.Database;
import model.BookSearchResult;
import model.Patron;
import model.PatronMin;

/**
 *
 * @author Sudeera Dilshan
 */
public class PatronController {

    public void addPatron(Patron patron) throws Exception {
        String query = "INSERT INTO patron\n"
                + "(`name`,\n"
                + "`address`,\n"
                + "`telephone`,\n"
                + "`type`)\n"
                + "VALUES\n"
                + "(?,?,?,?);";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);

        statement.setString(1, patron.getName());
        statement.setString(2, patron.getAddress());
        statement.setString(3, patron.getTelephone());
        statement.setString(4, patron.getType());

        statement.executeUpdate();
        statement.close();
    }

    public void updatePatron(Patron patron) throws Exception {
        String query = "UPDATE `librarian`.`patron`\n"
                + "SET\n"
                + "`name` = ?\n"
                + "`address` = ?,\n"
                + "`telephone` = ?,\n"
                + "`type` = ?\n"
                + "WHERE `patron_id` = ?;";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);

        statement.setString(1, patron.getName());
        statement.setString(2, patron.getAddress());
        statement.setString(3, patron.getTelephone());
        statement.setString(4, patron.getType());
        statement.setInt(5, patron.getId());

        statement.executeUpdate();
        statement.close();
    }

    public List<PatronMin> getAllPatronList() throws Exception {
        String query = "SELECT * FROM patron";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);

        ResultSet rs = statement.executeQuery();
        List<PatronMin> patronList = new ArrayList<>();

        while (rs.next()) {
            PatronMin patronResult = new PatronMin();
            patronResult.setId(rs.getInt("patron_id"));
            patronResult.setName(rs.getString("name"));
            patronList.add(patronResult);
        }

        return patronList;
    }
}
