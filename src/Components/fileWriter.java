package Components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class fileWriter {
    FileOutputStream out;
    OutputStreamWriter writer;

    public fileWriter(String File_Name) throws Exception {
        File f = new File(File_Name);
        if(!f.exists()) {
            f.createNewFile();
        }
        out = new FileOutputStream(f);
        writer = new OutputStreamWriter(out,"UTF-8");
    }

    public void Write_Line(String line) throws Exception {
        writer.write(line + "\n");
    }

    public void Close() throws Exception {
        writer.close();
    }
}
