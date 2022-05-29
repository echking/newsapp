package ink.eck.news.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserQm
{
  private static String URL = "jdbc:mysql://101.35.108.244:3306/lefuns_com?useUnicode=true&characterEncoding=utf-8&useSSL=false";
  private static String USERNAME = "";
  private static String PASSWORD = "";
  private static String DRIVER = "com.mysql.jdbc.Driver";

  static {
    try { Class.forName(DRIVER);
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public static Connection getConnection() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }
  public static void closeConnection(Connection conn) {
    if (conn != null)
      try {
        conn.close();
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
  }
}