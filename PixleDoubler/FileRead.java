import java.io.*;
import java.util.*;
import java.util.List;

public class FileRead {
    private String CurrentLine;
    private List<String> rowList;
    private List<String> fullList = new ArrayList<String>();

    public void reader(String FILENAME) throws IOException {
        File read = new File(FILENAME);
        int repetitions = 6;
        /*if (!file.exists()) {
            file.createNewFile();
        } */ // if file already exists will do nothing
        File write = new File(FILENAME.substring(0, FILENAME.length() - 3) + " doubled.txt");
        write.createNewFile();
        BufferedReader buffer = new BufferedReader(new FileReader(read));
        BufferedWriter bufferW = new BufferedWriter(new FileWriter(write));
        while ((CurrentLine = buffer.readLine())!=null) {
            rowList = new ArrayList<String>(Arrays.asList(CurrentLine.split("\\t")));
            String newRowString = rowList.get(0);
            for (int i = 1; i < repetitions; i++) {
                newRowString += "\t";
                newRowString = newRowString + rowList.get(0);
            }
            for(int i = 1;rowList.size() > i;i++){
                for (int j = 0; j < repetitions; j++) {
                    newRowString += "\t";
                    newRowString = newRowString + rowList.get(i);
                }
            }
            for (int i = 0; i < repetitions; i++) {
                bufferW.write(newRowString);
                bufferW.newLine();
            }
        }
        buffer.close();
        bufferW.close();
    }
}
