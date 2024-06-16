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
import model.Reservation;
/**
 *
 * @author kavindu
 */
 

public class ReservedBook{

    public String reservebook;

    
     public void addReservationBook( String BookName, String Patronname, File reservebook1) throws Exception {
         Reservation newReservedBook = new Reservation();
         newReservedBook.setBookName(BookName);
         newReservedBook.setPatronName(Patronname);
          String record = null;

           record = newReservedBook.getPatronName()+ "    " + newReservedBook.getBookName()+ "         " + "\n";

        FileWriter fw = new FileWriter(reservebook, true);
        BufferedWriter bw = new BufferedWriter(fw);

        fw.write(record);
        bw.newLine();
        bw.close();
        fw.close();
     }
}

