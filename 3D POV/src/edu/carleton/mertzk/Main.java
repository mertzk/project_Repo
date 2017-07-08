package edu.carleton.mertzk;
import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartScreen();
            }
        });
    }
}