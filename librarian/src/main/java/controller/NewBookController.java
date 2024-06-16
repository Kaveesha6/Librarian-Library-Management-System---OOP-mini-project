/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import librarian.Database;
import model.BookCopy;
import model.BookMin;
import model.BookSearchResult;
import model.BookTitle;

/**
 *
 * @author Sudeera Dilshan
 */
public class NewBookController {

    public int addBook(BookTitle newBook, File book) throws Exception {

        String query = "INSERT INTO book_title\n"
                + "(`title`,\n"
                + "`author`,\n"
                + "`isbn`,\n"
                + "`edition`,\n"
                + "`published_year`,\n"
                + "`publisher`,\n"
                + "`catelog_number`)\n"
                + "VALUES\n"
                + "(?,?,?,?,?,?,?)";

        PreparedStatement statement = Database.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, newBook.getTitle());
        statement.setString(2, newBook.getAuthor());
        statement.setString(3, newBook.getIsbn());
        statement.setString(4, newBook.getEdition());
        statement.setString(5, newBook.getPubYear());
        statement.setString(6, newBook.getPublisher());
        statement.setString(7, newBook.getCatelogNumber());

        statement.executeUpdate();

        ResultSet generatedKeys = statement.getGeneratedKeys();
        long primaryKey = 0;
        if (generatedKeys.next()) {
            // Retrieve the primary key of the newly inserted row
            primaryKey = generatedKeys.getLong(1);
        }
        statement.close();
        return (int) primaryKey;
    }

    public void updateBook(BookTitle newBook, File book) throws Exception {
        String query = "UPDATE book_title\n"
                + "SET\n"
                + "`title` = ?,\n"
                + "`author` = ?,\n"
                + "`isbn` = ?,\n"
                + "`edition` = ?,\n"
                + "`published_year` = ?,\n"
                + "`publisher` = ?,\n"
                + "`catelog_number` = ?\n"
                + "WHERE `book_title_id` = ?;";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);

        statement.setString(1, newBook.getTitle());
        statement.setString(2, newBook.getAuthor());
        statement.setString(3, newBook.getIsbn());
        statement.setString(4, newBook.getEdition());
        statement.setString(5, newBook.getPubYear());
        statement.setString(6, newBook.getPublisher());
        statement.setString(7, newBook.getCatelogNumber());
        statement.setInt(8, newBook.getId());

        statement.executeUpdate();
        statement.close();
    }

    public int deleteBook(File book, BookTitle deleteBook) throws Exception {
        String query = "DELETE FROM book_title\n"
                + "WHERE book_title_id=?";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);
        statement.setInt(1, deleteBook.getId());

        statement.executeUpdate();
        statement.close();

        return 1;
    }

    public BookTitle searchBook(File book, int id) throws Exception {
        String query = "SELECT * FROM book_title WHERE book_title_id=?";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        BookTitle bookSearchResult = new BookTitle();
        while (rs.next()) {
            bookSearchResult.setId(rs.getInt("book_title_id"));
            bookSearchResult.setTitle(rs.getString("title"));
            bookSearchResult.setAuthor(rs.getString("author"));
            bookSearchResult.setIsbn(rs.getString("isbn"));
            bookSearchResult.setEdition(rs.getString("edition"));
            bookSearchResult.setPubYear(rs.getString("published_year"));
            bookSearchResult.setPublisher(rs.getString("publisher"));
            bookSearchResult.setCatelogNumber(rs.getString("catelog_number"));
        }
        rs.close();
        statement.close();
        return bookSearchResult;
    }

    public List<BookSearchResult> searchBookByTitleAndISBN(File book, String searchText) throws Exception {
        String query = "SELECT bt.*,COUNT(bc.book_title_id) As book_count FROM book_title AS bt "
                + "INNER JOIN book_copy AS bc ON bt.book_title_id = bc.book_title_id\n"
                + "WHERE bt.title LIKE ? OR bt.isbn LIKE ?";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);
        statement.setString(1, "%" + searchText + "%");
        statement.setString(2, "%" + searchText + "%");

        ResultSet rs = statement.executeQuery();
        List<BookSearchResult> bookSearchResultList = new ArrayList<>();

        while (rs.next()) {
            BookSearchResult bookSearchResult = new BookSearchResult();
            bookSearchResult.setId(rs.getInt("bt.book_title_id"));
            bookSearchResult.setTitle(rs.getString("bt.title"));
            bookSearchResult.setCopies(rs.getInt("book_count"));
            bookSearchResultList.add(bookSearchResult);
        }

        return bookSearchResultList;
    }

    public List<BookMin> getAllBookList() throws Exception {
        String query = "SELECT * FROM book_title";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);

        ResultSet rs = statement.executeQuery();
        List<BookMin> bookList = new ArrayList<>();

        while (rs.next()) {
            BookMin bookResult = new BookMin();
            bookResult.setId(rs.getInt("book_title_id"));
            bookResult.setTitle(rs.getString("title"));
            bookList.add(bookResult);
        }

        return bookList;
    }

    public void addCopies(int noOfCopies, int bookTitleId) throws Exception {

        String query = "SELECT COUNT(*) As copies FROM book_copy WHERE book_title_id=?";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);
        statement.setInt(1, bookTitleId);

        ResultSet rs = statement.executeQuery();
        int copies = 0;

        if (rs.next()) {
            copies = rs.getInt("copies");
        }
        rs.close();
        statement.close();

        if (copies != noOfCopies) {
            int copiesToAdd = noOfCopies - copies;
            if (copiesToAdd > 0) {
                for (int i = 0; i < copiesToAdd; i++) {
                    String query2 = "INSERT INTO book_copy\n"
                            + "(`book_title_id`,\n"
                            + "`patron_id`,\n"
                            + "`status`)\n"
                            + "VALUES (?,?,'Available');";

                    PreparedStatement statement2 = Database.getConnection().prepareStatement(query2);

                    statement2.setInt(1, bookTitleId);
                    statement2.setInt(2, -1);

                    statement2.executeUpdate();
                    statement2.close();
                }
            }
        }
    }

    public List<BookCopy> searchBookCopies(int bookTitleId) throws Exception {
        String query = "SELECT * FROM book_copy WHERE book_title_id=?";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);
        statement.setInt(1, bookTitleId);

        ResultSet rs = statement.executeQuery();
        List<BookCopy> bookCopyResultList = new ArrayList<>();

        while (rs.next()) {
            BookCopy bookCopyResult = new BookCopy();
            bookCopyResult.setId(rs.getInt("book_copy_id"));
            bookCopyResult.setBookTitleId(rs.getInt("book_title_id"));
            bookCopyResult.setPatronId(rs.getInt("patron_id"));
            bookCopyResult.setStatus(rs.getString("status"));
            bookCopyResultList.add(bookCopyResult);
        }

        return bookCopyResultList;
    }

    public void updateBookCopy(BookCopy bookCopy) throws Exception {
        String query = "UPDATE book_copy\n"
                + "SET\n"
                + "`book_title_id` = ?,\n"
                + "`patron_id` = ?,\n"
                + "`status` = ?\n"
                + "WHERE `book_copy_id` = ?;";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);

        statement.setInt(1, bookCopy.getBookTitleId());
        statement.setInt(2, bookCopy.getPatronId());
        statement.setString(3, bookCopy.getStatus());
        statement.setInt(4, bookCopy.getId());

        statement.executeUpdate();
        statement.close();
    }

    public List<BookCopy> searchReservedBookCopyByPatronId(int patronId) throws Exception {
        String query = "SELECT * FROM book_copy WHERE patron_id=? AND status='Reserved'";

        PreparedStatement statement = Database.getConnection().prepareStatement(query);
        statement.setInt(1, patronId);

        ResultSet rs = statement.executeQuery();
        List<BookCopy> bookCopyResultList = new ArrayList<>();

        while (rs.next()) {
            BookCopy bookCopyResult = new BookCopy();
            bookCopyResult.setId(rs.getInt("book_copy_id"));
            bookCopyResult.setBookTitleId(rs.getInt("book_title_id"));
            bookCopyResult.setPatronId(rs.getInt("patron_id"));
            bookCopyResult.setStatus(rs.getString("status"));
            bookCopyResultList.add(bookCopyResult);
        }

        return bookCopyResultList;
    }
}
