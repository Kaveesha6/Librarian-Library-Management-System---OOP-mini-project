/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import model.BookTitle;

/**
 *
 * @author TiVA
 */
public class CheckOutHandler {

    public void checkOut(String author, String book, File checkout) throws Exception {
        String record = null;

        record = author + "    " + book + "    " + "\n";

        FileWriter fw = new FileWriter(checkout, true);
        BufferedWriter bw = new BufferedWriter(fw);

        fw.write(record);
        bw.newLine();
        bw.close();
        fw.close();
    }
}
