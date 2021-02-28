package dataPrep;

import java.io.FileNotFoundException;
import java.lang.Exception;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Components.res;

public class SQLink {
    private static Connection conn = null;

    public static void Init() throws Exception {
        if(conn != null) {
            System.err.println("Connection initialized");
            return;
        }
        System.out.print("Establish access to database...");
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:data/info.db3");
            conn.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println("A Sql Exception was thrown");
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            throw new FileNotFoundException();
        }
        System.out.print("Success \n");
        System.out.print("Searching Data...");

        /* Create tables if database is empty */
        Statement stm = null;
        try {
            stm = conn.createStatement();
            try {
                stm.execute("SELECT * FROM History");
            } catch (Exception e) {
                stm.execute("CREATE TABLE History (" + 
                    "ID INTEGER PRIMARY KEY NOT NULL," +
                    "Item_Name VARCHAR(128) NOT NULL," +
                    "Star INT NOT NULL," + 
                    "Pull_Time VARCHAR(255) NOT NULL," +
                    "Is_Up INT);");
            } finally {
                stm.close();
                conn.commit();
            }
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            throw new SQLException();
        }
        System.out.print("Success \n");
    }

    public static void Close() {
        try {
            conn.close();
        } catch (Exception e) {
            System.err.println("Cannot close before open");
            return;
        } finally {
            conn = null;
        }
        return;
    }

    static boolean Date_Check(String date) {
        if(date.length() == 10) {
            for(int i=0; i<10; i+=1) {
                if(i==4 || i==7) {
                    if(date.charAt(i) != '-') return false;
                } else if(date.charAt(i) > '9' || date.charAt(i) < '0') return false;
            }
            return true;
        } else if(date.length() == 19) {
            for(int i=0; i<19; i+=1) {
                if(i==4 || i==7) {
                    if(date.charAt(i) != '-') return false;
                } else if(i==10) {
                    if(date.charAt(i) != ' ') return false;
                } else if(i==13 || i==16) {
                    if(date.charAt(i) != ':') return false;
                } else if(date.charAt(i) > '9' || date.charAt(i) < '0') return false;
            }
            return true;
        }
        return false;
    }

    public static void Wirte_History(String Item_Name, int star, String Time_Token, int Is_Up) throws Exception {
        if(!Date_Check(Time_Token)) {
            throw new Exception("Invalid Date Time");
        }
        PreparedStatement stm = null;
        try {
            String sql = "INSERT INTO History Values(NULL,?,?,?,?)";
			stm = conn.prepareStatement(sql);
            stm.setString(1, Item_Name);
            stm.setInt(2, star);
            stm.setString(3, Time_Token);
            stm.setInt(4, Is_Up);
            stm.execute();
        } catch (Exception e) {
            conn.rollback();
            throw new SQLException();
        } finally {
            conn.commit();
            if(stm != null) {
                stm.close();
            }
        }
    }
    
    public static List<res> Get_History(int total, String last_date) throws Exception {
        if(!Date_Check(last_date)) {
            throw new Exception("Invalid Date Time");
        }
        ResultSet res_set = null;
        PreparedStatement stm = null;
        List<res> res_list = new ArrayList<res>();
        try {
            String sql = "SELECT * FROM History WHERE Pull_Time > ? ORDER BY ID DESC LIMIT ?";
            stm = conn.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stm.setString(1, last_date);
            stm.setInt(2, total);
            res_set = stm.executeQuery();
            while(res_set.next()){
                res_list.add(new res(res_set.getInt("ID"),
                res_set.getInt("Star"),
                res_set.getString("Item_Name"),
                res_set.getInt("Is_Up") > 0,
                res_set.getString("Pull_Time")));
            }
        } catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new SQLException("A sql exception was thrown");
        } finally {
            if(stm != null){
                stm.close();
            }
        } 
        return res_list;
    }

    public static int Total() throws Exception {
        Statement stm = null;
        int count_total = 0;
        try {
            stm = conn.createStatement();
            ResultSet res = stm.executeQuery(("SELECT COUNT(*) total FROM HISTORY"));
            count_total = res.getInt("total");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new SQLException("An sql exception has bean thrown");
        } finally {
            if(stm != null) {
                stm.close();
            }
        }
        return count_total;
    }

    public static int Count(int star) throws Exception {
        int res = 0;
        PreparedStatement stm = null;
        try {
            String sql = "SELECT COUNT(*) Star_Count FROM History WHERE Star=?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, star);
            ResultSet result = stm.executeQuery();
            res = result.getInt("Star_Count");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new SQLException("An sql exception has bean thrown");
        } finally {
            if(stm != null) {
                stm.close();
            }
        }
        return res;
    }

    public static int Count(int star, boolean up) throws Exception {
        int res = 0;
        PreparedStatement stm = null;
        try {
            String sql = "SELECT COUNT(*) Star_Count FROM History WHERE Star=? AND Is_Up=?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, star);
            stm.setInt(2, up ? 1 : 0);
            ResultSet result = stm.executeQuery();
            res = result.getInt("Star_Count");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new SQLException("An sql exception has bean thrown");
        } finally {
            if(stm != null) {
                stm.close();
            }
        }
        return res;
    }

    public static int Count(String Name) throws Exception {
        int res = 0;
        PreparedStatement stm = null;
        try {
            String sql = "SELECT COUNT(*) Star_Count FROM History WHERE Item_Name=?;";
            stm = conn.prepareStatement(sql);
            stm.setString(1, Name);
            ResultSet result = stm.executeQuery();
            res = result.getInt("Star_Count");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new SQLException("An sql exception has bean thrown");
        } finally {
            if(stm != null) {
                stm.close();
            }
        }
        return res;
    }

    public static void Delete(String from_date, String to_date) throws Exception {
        PreparedStatement stm = null;
        if(!Date_Check(from_date) || !Date_Check(to_date)) {
            throw new Exception("Invalid Date Time");
        }
        try {
            String sql = "DELETE FROM History WHERE Pull_Time >= ? AND Pull_Time <= ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, from_date);
            stm.setString(2, to_date);
            stm.execute();
        } catch (Exception e) {
            conn.rollback();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new SQLException("An sql exception has bean thrown");
        } finally {
            conn.commit();
            if(stm != null) {
                stm.close();
            }
        }
    }

}
