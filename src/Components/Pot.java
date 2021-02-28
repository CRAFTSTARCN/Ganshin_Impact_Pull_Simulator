package Components;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi.Color;

import com.google.gson.*;
import dataPrep.*;


public class Pot {
    /* inner private class */
    private class Pot_Info {
        private boolean Up;
        private int Total;
        private int Rate_Five;
        private int Rate_Four;
        private int Rate_Three;
        private int Rate_Up_Total;
        private int Rate_Up;
        private int Guarantee_Five;
        private int Guarantee_Four;
        private String[] Five_Star_Up;
        private String[] Five_Star;
        private String[] Four_Star_Up;
        private String[] Four_Star;
        private String[] Three_Star;

        private void showInfo() {
            System.out.println("UP: " + Up);
            System.out.println("Rate of five star: " + Rate_Five + "/" + Total);
            System.out.println("Rate of four star: " + Rate_Four + "/" + Total);
            System.out.println("Rate of three star: " + Rate_Three + "/" + Total);
            System.out.println("Rate of up: " + Rate_Up + "/" + Rate_Up_Total);
            System.out.println("UP five: " + Arrays.toString(Five_Star_Up));
            System.out.println("Other five star: " + Arrays.toString(Five_Star));
            System.out.println("Up four: " + Arrays.toString(Four_Star_Up));
            System.out.println("Other four star: " + Arrays.toString(Four_Star));
            System.out.println("Three star: " + Arrays.toString(Three_Star));
        } 
    }

    /* members */
    private static Random Random_Gener = null;
    private static Pot_Info info = null;

    /* methords */

    /**
     * Initialize function
     * @param Item_Path Pot item path
     */
    public static void Init(String Item_Path) throws Exception {
        /* init static variables */
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        AnsiConsole.systemInstall();
        try {
            info = gson.fromJson(fileReader.Read_Text_File(Item_Path), Pot_Info.class);
        } catch(FileNotFoundException e) {
            System.err.print(e.getClass().getName() + ": " + e.getMessage());
            throw new FileNotFoundException("Fail to load pot infomation");
        }
        
        //info.showInfo();
        Random_Gener = new Random();

        /* init Configure */
        Configure.Init();
    }

    /* single pull */
    private static res Single_Pull() {
        int code = Random_Gener.nextInt(info.Total);
        /* core random system */
        Configure.total += 1;
        if(code < info.Rate_Five || Configure.Since_Last_Five == info.Guarantee_Five - 1) {
            Configure.Since_Last_Five = 0;
            Configure.Since_Last_Four = 0;
            int next_code = Random_Gener.nextInt(info.Rate_Up_Total);
            if(info.Up && (Configure.Since_Last_Up_Five > 0 || next_code < info.Rate_Up)) {
                Configure.Since_Last_Up_Five = 0;
                String res_name = info.Five_Star_Up[Random_Gener.nextInt(info.Five_Star_Up.length)];
                return new res(5, res_name,true);
            } else {
                Configure.Since_Last_Up_Five += 1;
                String res_name = info.Five_Star[Random_Gener.nextInt(info.Five_Star.length)];
                return new res(5, res_name,false);
            }
        } else if(code < info.Rate_Five + info.Rate_Four || Configure.Since_Last_Four == info.Guarantee_Four -1) {
            Configure.Since_Last_Four = 0;
            Configure.Since_Last_Five += 1;
            int next_code = Random_Gener.nextInt(info.Rate_Up_Total);
            if(info.Up && (Configure.Since_Last_Up_Four > 0 || next_code < info.Rate_Up)) {
                Configure.Since_Last_Up_Four = 0;
                String res_name = info.Four_Star_Up[Random_Gener.nextInt(info.Four_Star_Up.length)];
                return new res(4,res_name,true);
            } else {
                Configure.Since_Last_Up_Four += 1;
                String res_name = info.Four_Star[Random_Gener.nextInt(info.Four_Star.length)];
                return new res(4,res_name,false);
            }
        } else if(code < info.Rate_Five + info.Rate_Four + info.Rate_Three) {
            Configure.Since_Last_Five += 1;
            Configure.Since_Last_Four += 1;
            String res_name = info.Three_Star[Random_Gener.nextInt(info.Three_Star.length)];
            return new res(3,res_name,false);
        }
        return new res(-1,"null",false);
    }

    private static void Write_File(String File_Name, boolean DESC, List<res> Res_List) throws Exception {
        fileWriter fw = new fileWriter(File_Name);
        if(DESC) {
            for(int i=Res_List.size(); i>0; i-=1) {
                fw.Write_Line(Res_List.get(i-1).Full_Info());
            }
        } else {
            for(int i=0; i<Res_List.size(); i+=1) {
                fw.Write_Line(Res_List.get(i).Full_Info());
            }
        }
        fw.Close();
    }

    /* public interface, pulling args : time */
    public static void pull(Integer time) throws Exception { 
        for(int i=0; i<time; i = i+1) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            java.util.Date dnow = new java.util.Date();
            String now = fmt.format(dnow);
            res resault =  Single_Pull();
            if(resault.Get_Star() == 4) {
                System.out.println(now + " 获得：" + Ansi.ansi().fg(Color.GREEN).a(resault.toString()).reset());
            } else if(resault.Get_Star() == 5) {
                System.out.println(now + " 获得：" + Ansi.ansi().fg(Color.RED).a(resault.toString()).reset());
            } else {
                System.out.println(now + " 获得：" + resault.toString());
            }
            SQLink.Wirte_History(resault.Get_Name(), resault.Get_Star(), now,resault.Up() ? 1 : 0);
        }
    }

    public static void history(Integer max_line, String latest_date, String output_to, Boolean DESC) throws Exception {
        latest_date = latest_date.replace('_', ' ');
        List<res> res_list = SQLink.Get_History(max_line, latest_date);
        boolean to_sysout = output_to.equals("sysout");
        if(!to_sysout) {
            Write_File(output_to, DESC, res_list);
            return;
        }
        if(DESC) {
            for(int i=res_list.size(); i>0; i-=1) {
                System.out.println(res_list.get(i-1).Colored_Full_Info());
            }
        } else {
            for(int i=0; i<res_list.size(); i+=1) {
                System.out.println(res_list.get(i).Colored_Full_Info());
            }
        }
    }

    public static void summary() throws Exception {
        System.out.println("Total pull: " + Configure.total);
        System.out.println(String.format("You have get %d five star", SQLink.Count(5)));
        System.out.println(String.format("Among witch, %d of them is up item of that period", SQLink.Count(5,true)));
        System.out.println(String.format("You have get %d Four star", SQLink.Count(4)));
        System.out.println(String.format("Among witch, %d of them is up item of that period", SQLink.Count(4,true)));
    }

    public static void potinfo() {
        info.showInfo();
        System.out.println("Total pull: " + Configure.total);
        System.out.println("Since Last Five: " + Configure.Since_Last_Five);
        System.out.println("Since Last Four: " + Configure.Since_Last_Four);
        System.out.println("Since Last Up Five: " + Configure.Since_Last_Up_Five);
        System.out.println("Since Last Up Four: " + Configure.Since_Last_Up_Four);
    }

    public static void clean_up(String from_date, String to_date) throws Exception {
        from_date = from_date.replace('_', ' ');
        to_date = to_date.replace('_', ' ');
        SQLink.Delete(from_date, to_date);
        Configure.Reload();
    }

    public static void load_history(String filename) throws Exception {
        fileReader reader = new fileReader(filename);
        String line = null;
        while(true) {
            line =  reader.Read_Line();
            if(line == "") break;
            String[] cols = line.split(",");
            if(cols.length < 4) {
                throw new Exception("invalid data format" + line);
            }
            SQLink.Wirte_History(cols[0], Integer.valueOf(cols[1]).intValue(), cols[2], Integer.valueOf(cols[3]).intValue());
        }
        Configure.Reload();
        reader.Close();
    }
}
