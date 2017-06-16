import java.io.*;
import java.util.*;
import java.util.List;

public class FileRead {
    private String CurrentLine;
    private List<String> rowList;
    private List<String> fullList = new ArrayList<String>();

    public void reader(String FILENAME) throws IOException {
        File read = new File(FILENAME);
        /*if (!file.exists()) {
            file.createNewFile();
        } */ // if file already exists will do nothing
        File write = new File(FILENAME.substring(0, FILENAME.length() - 3) + " doubled.txt");
        write.createNewFile();
        BufferedReader buffer = new BufferedReader(new FileReader(read));
        BufferedWriter bufferW = new BufferedWriter(new FileWriter(write));
        while ((CurrentLine = buffer.readLine())!=null) {
            rowList = new ArrayList<String>(Arrays.asList(CurrentLine.split("\\t")));
            System.out.println(rowList);
            String newRowString = rowList.get(0) + "\t" + rowList.get(0);
            for(int i = 1;rowList.size() > i;i++){
                newRowString += "\t";
                newRowString = newRowString + rowList.get(i);
                newRowString += "\t";
                newRowString = newRowString + rowList.get(i);
            }
            bufferW.write(newRowString);
            bufferW.newLine();
            bufferW.write(newRowString);
            bufferW.newLine();
        }
        buffer.close();
        bufferW.close();
    }
}
