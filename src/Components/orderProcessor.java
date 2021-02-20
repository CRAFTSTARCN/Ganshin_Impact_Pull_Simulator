package Components;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import mException.Exit;
import mException.FatalException;

class arg {
    String Name;
    String Default;
    String Type;
    String Explain;
}

class order {
    String Called_Func;
    String Help;
    arg[] Args;
}

public class orderProcessor {
    public static Map<String,order> methods = null;

    public static void Init() throws Exception {
        System.out.print("Loading configuration interfaces...");
        String list = null;
        try {
            list = fileReader.Read_Text_File("./config/interface_regedit.json");
        } catch(FileNotFoundException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new FileNotFoundException("Fail to load interface list");
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        methods = gson.fromJson(list, new TypeToken<Map<String,order>>(){}.getType());
        System.out.print("Success \n");
    }


    private static void Call(String func_name, Object[] args, Class<?>[] arg_classes) throws FatalException {
        try {
            Method m_func = Class.forName("Components.Pot").getMethod(func_name, arg_classes);
            m_func.invoke(null, args);
        } catch (NoSuchMethodException e) {
            System.err.println("No such method Exception: " + e.getMessage());
            System.err.println("This exception shall not happend in program, place check and update config file!");
            throw new FatalException();
        } catch (ClassNotFoundException e) {
            System.err.println("No such class Exception: " + e.getMessage());
            System.err.println("This exception shall not happend in program, place check and update config file!");
            throw new FatalException();
        } catch (IllegalArgumentException e) {
            System.err.println("Parament Exception: " + e.getMessage());
            System.err.println("This exception shall not happend in program, place check and update config file!");
            throw new FatalException();
        } catch (IllegalAccessException e) {
            System.err.println("Illegal Access Exception: " + e.getMessage());
            System.err.println("This exception shall not happend in program, place check and update config file!");
            throw new FatalException();
        } catch (InvocationTargetException e) {
           Throwable t = e.getTargetException();
           System.err.println(t.getClass().getName() + ": " + t.getMessage() + "\nCaused by: " + e.getCause());
           if(t.getClass() == FatalException.class) {
               throw new FatalException();
           }
        }
    }

    private static void Execute(String[] line_array) throws Exception {
        order call_order = methods.get(line_array[0]);
        if(call_order == null){
            throw new Exception("None such order " + line_array[0]);
        }
        String func_name = call_order.Called_Func;
        Object args[] = new Object[call_order.Args.length];
        Class<?> arg_classes[] = new Class[call_order.Args.length];
        try {
            for(int i=0; i<call_order.Args.length; i+=1) {
                arg_classes[i] =  Class.forName(call_order.Args[i].Type);
                if(arg_classes[i] == String.class){
                    args[i] = call_order.Args[i].Default;
                }else {
                    args[i] = arg_classes[i].getMethod("valueOf", new Class[]{String.class})
                    .invoke(null, call_order.Args[i].Default);
                }
            }
        } catch (Exception e) {
            System.err.println("Internal arg exception: ");
            System.err.println("Valid argument type can only be Integer Boolean String or Double");
            System.err.println("This should not happen in system check and update config file");
            throw new FatalException();
        }
        
        for(int i=1; i<line_array.length; i+=1) {
            String[] arg_array = line_array[i].split("=");
            boolean has_arg = false;
            for(int j=0; j<call_order.Args.length; j+=1) {
                if(arg_array[0].equals(call_order.Args[j].Name)) {
                    has_arg = true;
                    try {
                        String arg_val = arg_array[1];
                        if(arg_classes[j] == String.class){
                            args[j] = arg_val;
                        }else {
                            args[j] = arg_classes[j].getMethod("valueOf", new Class[]{String.class})
                            .invoke(null, arg_val);
                        }
                    } catch (IllegalArgumentException e) {
                        throw new Exception("Illegal parament " + arg_array[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new Exception("No value after parament " + arg_array[0]);
                    }
                }
            }
            if(!has_arg) {
                throw new Exception("No parament called " + arg_array[0]);
            }
        }

        Call(func_name, args, arg_classes);
    }

    public static void help(String Key) throws Exception {
        order helped = methods.get(Key);
        if(helped == null) {
            throw new Exception("None such order " + Key);
        }
        System.out.println(Key + ": " + helped.Help);
        System.out.println("  Args:");
        for (arg argument : helped.Args) {
            System.out.println("    " + argument.Name + ": " + argument.Explain + "; Default: " + argument.Default);
        }
    }

    public static void help() {
        for (String Key : methods.keySet()) {
            System.out.println(Key + ": " + methods.get(Key).Help);
            System.out.println("  Args:");
            for (arg argument : methods.get(Key).Args) {
                System.out.println("    " + argument.Name + ": " + argument.Explain + "; Default: " + argument.Default);
            }
        }
    }

    public static void Process(String line) throws Exit, FatalException {
        String[] line_array = line.split(" ");
        if(line_array[0].equals("exit")) {
            throw new Exit();
        } else if(line_array[0].equals("help")) {
            if(line_array.length >= 2) {
                try {
                    for(int i=1; i<line_array.length; i+=1){
                        help(line_array[i]);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                help();
            }
        } else {
            try{
                Execute(line_array);
            } catch(FatalException e) {
                throw new FatalException();
            } catch(Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    

}
