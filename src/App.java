import dataPrep.SQLink;

import java.util.Scanner;

import mException.*;
import Components.*;

public class App {
    public static void main(String[] args) {
        try {
            SQLink.Init();
            Pot.Init("./data/Pot_Item.json");
            orderProcessor.Init();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        }
        System.out.println("=====================================================");
        System.out.println("Ganshin Impact Pull Simulator 0.0.2 alpha 2021-02-19");
        System.out.println("Powered by Ag2S");
        System.out.println("Enter \"help\" to get a quick start");
        Scanner line_reader = new Scanner(System.in);
        try {
            while(true) {
                System.out.print(">>");
                String line = line_reader.nextLine();
                if(!line.isEmpty()) {
                    orderProcessor.Process(line);
                }
            }
        } catch (Exit exit) {
            line_reader.close();
            SQLink.Close();
            System.exit(0);
        } catch (FatalException f) {
            System.err.println("System quit because of a fatal error");
            line_reader.close();
            SQLink.Close();
            System.exit(-1);
        }
    }
}
