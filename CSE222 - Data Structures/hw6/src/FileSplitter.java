import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * FileSplitter class to make splitting easier 
 */
public class FileSplitter {
    private String splitStr;
    private BufferedReader file;

    /**
     * Filesplitter splits the file and returns the list of strings
     * @param filename filename that will be splitted
     * @param splitStr character sequence for split
     * @throws FileNotFoundException if there is a file error
     */
    public FileSplitter(String filename, String splitStr) throws FileNotFoundException{
        if(filename == null || splitStr == null)
            throw new NullPointerException();

        this.splitStr = splitStr;
        file = new BufferedReader(new FileReader(filename));
    }

    /**
     * Splits the line and returns the strings as list
     * @return returns the string parts as list
     * @throws IOException if there is a file error
     */
    public List<String> nextLine() throws IOException{
        String line = file.readLine();
        if(line == null){
            file.close();
            return new ArrayList<>();
            
        }
        return Arrays.asList(line.split(splitStr));
    }
}
