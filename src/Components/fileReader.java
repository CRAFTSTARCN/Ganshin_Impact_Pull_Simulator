package Components;

import java.lang.Exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class fileReader {
    public static String Read_Text_File (String File_Path) throws Exception {
        File f = new File(File_Path);
        if(!f.exists()) {
            throw new FileNotFoundException("Missing file: " + File_Path);
        }
        FileInputStream ins = new FileInputStream(f);
        InputStreamReader in = new InputStreamReader(ins, "UTF-8");
        
        StringBuffer buffer = new StringBuffer();
        while(in.ready()) {
            buffer.append((char)in.read());
        }
        
        String ans = buffer.toString();
        in.close();
        return ans;
    }
}
