package edu.carleton.mertzk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by keaton on 7/3/17.
 */
public class StartScreen implements ActionListener{

    public static int height = 0;
    public static int rows = 0;
    public static int updates = 0;
    private boolean firstTime = true;

    private JFrame frame;
    private JPanel panel;
    private JTextField rowField;
    private JTextField heightField;
    private JTextField updateField;
    private JLabel helpLable;

    public StartScreen(String s){
    }
    public StartScreen(int height, int rows){
        this.height = height;
        this.rows = rows;
        this.updates = dialogWindow();
    }
    public StartScreen(){
        this.frame = new JFrame("3D POV Settings");
        multiDialogWindow();
    }

    private int dialogWindow() {
        String errorMessage = "";
        int number=0;
        do {
            // Show input dialog with current error message, if any
            String stringInput = JOptionPane.showInputDialog(errorMessage + "Enter even number.");
            try {
                number = Integer.parseInt(stringInput);
                if (number>2 && number%2==0) {
                    JOptionPane.showMessageDialog(null, "The number you chose is " + number + ".");
                    errorMessage = ""; // no more error
                }else{
                    errorMessage = "That number is not within the \n" + "allowed range!\n";
                }
            } catch (NumberFormatException e) {
                // The typed text was not an integer
                if(e.getMessage()=="null"){
                    return 0;
                }
                System.out.println(e.getMessage());
                errorMessage = "The text you typed is not a number.\n";
            }
        } while (!errorMessage.isEmpty());
        return number;
    }

    private void multiDialogWindow() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);
        frame.setLocation(200, 200);
        frame.setResizable(false);
        panel = new JPanel();
        GridLayout layout = new GridLayout(3, 3, 5, 2);
        panel.setLayout(layout);
        panel.add(new JLabel("LEDs in a row:"));
        panel.add(new JLabel("# of rows:"));
        panel.add(new JLabel("Arc pixel:"));
        rowField = new JTextField("16");
        heightField = new JTextField("10");
        updateField = new JTextField("120");
        JButton submitButton = new JButton("Submit");
        panel.add(rowField);
        panel.add(heightField);
        panel.add(updateField);
        panel.add(submitButton);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        submitButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(firstTime) {
            helpLable = new JLabel();
            panel.add(helpLable);
            frame.add(panel);
            frame.pack();
            firstTime = false;
        }
        try{
            helpLable.setText("");
            this.rows = Integer.parseInt(rowField.getText());
            this.height = Integer.parseInt(heightField.getText());
            this.updates = Integer.parseInt(updateField.getText());
            if(this.rows < 2){
                helpLable.setText("LEDs in a row must be > 2");
            } else if(this.height<2){
                helpLable.setText("# of rows must be > 2");
            } else if(this.updates%2!=0){
                helpLable.setText("Arc Pixels must be even");
            } else if(this.updates < 2){
                helpLable.setText("Arc Pixels must > 2");
            }else{
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new MainScreen();
                    }
                });
            }
            frame.pack();
        }catch(NumberFormatException error){
            helpLable.setText("All Fields Must Be Integers, Try Again!");
            frame.pack();
        }
    }
}
