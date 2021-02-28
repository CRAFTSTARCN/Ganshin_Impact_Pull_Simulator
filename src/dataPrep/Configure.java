package dataPrep;

import java.util.List;
import Components.res;

public class Configure {
    public static int total = 0; 
    public static int Since_Last_Five = 0; // 90 for Guarantee
    public static int Since_Last_Four = 0; // 10 for Guarantee
    public static int Since_Last_Up_Five = 0; // Guaranteed if last four star is not uped
    public static int Since_Last_Up_Four = 0; // Guarabteed if last five stat is not uped

    public static void Init() throws Exception {
        System.out.print("Loading configures paraments...");
        Reload();
        System.out.print("Success \n");
    }

    public static void Reload() throws Exception {
        total = 0; 
        Since_Last_Five = 0; 
        Since_Last_Four = 0;
        Since_Last_Up_Five = 0;
        Since_Last_Up_Four = 0;
        
        total = SQLink.Total();
        List<res> res_list = SQLink.Get_History(90, "0000-00-00 00:00:00");
        boolean get_four = false, get_five = false;
        for(int i=0; i<res_list.size(); i+=1) {
            int star = res_list.get(i).Get_Star();
            if(star < 4){
                if(!get_four && !get_five) {
                    Since_Last_Four += 1;
                }
                if(!get_five) {
                    Since_Last_Five += 1;
                }
            }
            if(star == 4) {
                if(!get_four) {
                    if(!res_list.get(i).Up()) {
                        Since_Last_Up_Four = 1;
                    }
                }
                if(!get_five) {
                    Since_Last_Five += 1;
                }
                get_four = true;
            }
            if(star == 5) {
                if(!get_five) {
                    if(!res_list.get(i).Up()) {
                        Since_Last_Up_Five = 1;
                    }
                }
                get_five = true;
            }
            if(get_five && get_four) {
                break;
            }
        }
    }
}
