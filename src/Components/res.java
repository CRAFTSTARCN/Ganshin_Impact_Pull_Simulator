package Components;

import org.fusesource.jansi.*;
import org.fusesource.jansi.Ansi.Color;

public class res {
    private int Id;
    private int star;
    private String name;
    private boolean is_up;
    private String pull_time;
    
    public res(int ID, int Star, String Name, boolean Is_Up, String Pull_Time) {
        Id = ID;
        star = Star;
        name = Name;
        is_up = Is_Up;
        pull_time = Pull_Time;
    }

    public res(int Star, String Name, boolean Is_Up, String Pull_Time) {
        star = Star;
        name = Name;
        is_up = Is_Up;
        pull_time = Pull_Time;
    }

    public res(int Star, String Name,boolean Is_Up) {
        star = Star;
        name = Name;
        is_up = Is_Up;
    }

    public int Ger_Id() {
        return Id;
    }

    public String toString() {    
        String str = star + "星物品 "+ name;
        return str;
    }

    public int Get_Star() {
        return star;
    }

    public String Get_Name() {
        return name;
    }

    public boolean Up() {
        return is_up;
    }

    public String Get_Time() {
        return pull_time;
    }

    public String Full_Info() {
        StringBuffer buff = new StringBuffer();
        buff.append(Id).append(" ")
        .append(pull_time).append(" ")
        .append(star).append("星物品 ")
        .append(name);
        if(is_up) {
            buff.append(" 当期Up获得");
        }
        return buff.toString();
    }

    public String Colored_Full_Info() {
        if(star == 5) {
            return Ansi.ansi().fg(Color.RED).a(Full_Info()).reset().toString();
        } else if (star == 4) {
            return Ansi.ansi().fg(Color.GREEN).a(Full_Info()).reset().toString();
        }
        return Full_Info();
    }
}
