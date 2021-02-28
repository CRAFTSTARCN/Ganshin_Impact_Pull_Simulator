package Components;

import java.lang.Exception;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class fileReader {
    private InputStreamReader reader = null;
    private FileInputStream is = null;

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

    public fileReader(String path) throws Exception {
        File f = new File(path);
        if(!f.exists()) {
            throw new FileNotFoundException("Missing file: " + path);
        }

        is = new FileInputStream(f);
        reader = new InputStreamReader(is,"UTF-8");
    }

    public void Close() {
        if(reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String Read_Line() throws Exception {
        StringBuffer buf = new StringBuffer();
        while(reader.ready()) {
            char c = (char)reader.read();
            if(c == '\n') {
                break;
            }
            buf.append(c);
        }

        return buf.toString();
    }
}
