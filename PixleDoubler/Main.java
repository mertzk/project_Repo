import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ChooseFile chooseFile = new ChooseFile();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                chooseFile.createAndShowGUI();
            }
        });
    }
}
