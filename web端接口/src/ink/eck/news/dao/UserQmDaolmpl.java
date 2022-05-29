package ink.eck.news.dao;

import ink.eck.news.dbutils.DBUtils;
import ink.eck.news.dbutils.UserQm;
import ink.eck.news.entity.UserQmBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserQmDaolmpl implements UserQmDao{
  public int updataQm(UserQmBean u){
    Connection conn = UserQm.getConnection();
    PreparedStatement ps = null;
    int flag = 0;
    String sql = "update user_qm set qm=? where username=?";
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, u.getQm());
      ps.setString(2, u.getUsername());
      flag = ps.executeUpdate();
      ps.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    DBUtils.closeConnection(conn);
    return flag;
  }
  public UserQmBean getUserQm(UserQmBean u) {
    Connection conn = UserQm.getConnection();
    PreparedStatement ps = null;
    UserQmBean u1 = null;
    String sql = "select qm from user_qm where username=?";
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, u.getUsername());
      ResultSet rs = ps.executeQuery();
      u1 = new UserQmBean();
      if (rs.next()) {
        u1.setQm(rs.getString("qm"));
      }
      ps.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    DBUtils.closeConnection(conn);
    return u1;
  }
}