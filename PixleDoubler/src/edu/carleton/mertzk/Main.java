package edu.carleton.mertzk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void fileRead() throws IOException{
        File file = new File("score.txt");
        if(!file.exists()){file.createNewFile();} // if file already exists will do nothing
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String CurrentLine;
        List<Integer> scores = new ArrayList<Integer>();
        int line = 0;
        while ((CurrentLine = buffer.readLine()) != null && line < 4 ){
            scores.add(Integer.parseInt(CurrentLine));
            line++;
        }
        while (scores.size()<6) {
            scores.add(0);
        }
        buffer.close();

        BufferedWriter bufferW = new BufferedWriter(new FileWriter(file));
        bufferW.write(Integer.toString(scores.get(0)));
        bufferW.newLine();
        bufferW.write(Integer.toString(scores.get(1)));
        bufferW.newLine();
        bufferW.write(Integer.toString(scores.get(2)));
        bufferW.close();
    }
    public static void main(String[] args) {
        try {
            fileRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
