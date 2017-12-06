package indi.ycl.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import com.zzf.DBUtils.DBUtils;

public class DbUtil {

    private static String driverName = "com.mysql.jdbc.Driver";

    private static String dbURL = "jdbc:mysql://localhost:3306/bom_analysis?useUnicode=true&characterEncoding=UTF-8";
    private static String userName = "root";
    private static String userPass = "root";
    
    public static ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();

    public DbUtil() {

        try {
            Class.forName(driverName).newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Connection getSqlConnection() {
         try {
            Class.forName(driverName).newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL, userName, userPass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     *
     * @return
     */
    public static Connection getCurrentConnection() {
        Connection con = (Connection) threadConnection.get();
        if (con == null) {
            con = getSqlConnection();
            threadConnection.set(con);
        }
        return con;
    }

    public static void closeCurrentConnection() {
        Connection con = (Connection) threadConnection.get();
        threadConnection.remove();
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closePreStatement(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void beginTransaction() {
        Connection con = (Connection) threadConnection.get();
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commitTransaction() {
        Connection con = (Connection) threadConnection.get();
        try {
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        Connection con = (Connection) threadConnection.get();
        try {
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

