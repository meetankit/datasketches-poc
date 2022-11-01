package audience.merchandising.utils;

import java.io.*;
import java.util.Random;

public class FileGenerator {

    public static void generateProfileFile(String fileName, boolean random) throws IOException {
        File file=new File("MOCK_DATA.csv");    //creates a new file instance
        FileReader fr=new FileReader(file);   //reads the file
        BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
        String line;
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName, false));

        while((line=br.readLine())!=null) {
            for(int i = 0 ; i<10000;i++) {
                out.write(line+String.valueOf(random?(new Random()):"") + "\n") ;
            }
        }
        out.close();
        fr.close();
    }

    public static void generateIntegerFile(Integer min, Integer max, String fileName, boolean random) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName, false));
        for(int key = min; key < max; key++){
            out.write(String.valueOf(random?(new Random()).nextInt():key) + "\n");
        }
        out.close();
    }
}

