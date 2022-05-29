package ink.eck.news.dao;

import ink.eck.news.dbutils.BlogDB;
import ink.eck.news.dbutils.DBUtils;
import ink.eck.news.entity.BlogBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlogDaolmpl
  implements BlogDao
{
  public List<BlogBean> getstudy()
  {
    Connection conn = BlogDB.getConnection();
    PreparedStatement ps = null;
    List<BlogBean> studylist=new ArrayList<BlogBean>();
    String sql = "select * from list where slug='study'";
    try {
      ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String ctime = rs.getString("ctime");
        String title = rs.getString("title");
        String description = rs.getString("description");
        String source = rs.getString("source");
        String picUrl = rs.getString("picUrl");
        String url = rs.getString("url");
        studylist.add(new BlogBean(id, ctime, title, description, source, picUrl, url));
      }
      rs.close();
      ps.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return studylist;
  }

  public List<BlogBean> getcu()
  {
    Connection conn = BlogDB.getConnection();
    PreparedStatement ps = null;
    List<BlogBean> studylist=new ArrayList<BlogBean>();
    String sql = "select * from list where slug='cu'";
    try {
      ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String ctime = rs.getString("ctime");
        String title = rs.getString("title");
        String description = rs.getString("description");
        String source = rs.getString("source");
        String picUrl = rs.getString("picUrl");
        String url = rs.getString("url");
        studylist.add(new BlogBean(id, ctime, title, description, source, picUrl, url));
      }
      rs.close();
      ps.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return studylist;
  }

  public List<BlogBean> getweb()
  {
    Connection conn = BlogDB.getConnection();
    PreparedStatement ps = null;
    List<BlogBean> studylist=new ArrayList<BlogBean>();
    String sql = "select * from list where slug='web'";
    try {
      ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String ctime = rs.getString("ctime");
        String title = rs.getString("title");
        String description = rs.getString("description");
        String source = rs.getString("source");
        String picUrl = rs.getString("picUrl");
        String url = rs.getString("url");
        studylist.add(new BlogBean(id, ctime, title, description, source, picUrl, url));
      }
      rs.close();
      ps.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return studylist;
  }

  public List<BlogBean> getandroid()
  {
    Connection conn = BlogDB.getConnection();
    PreparedStatement ps = null;
    List<BlogBean> studylist=new ArrayList<BlogBean>();
    String sql = "select * from list where slug='android'";
    try {
      ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String ctime = rs.getString("ctime");
        String title = rs.getString("title");
        String description = rs.getString("description");
        String source = rs.getString("source");
        String picUrl = rs.getString("picUrl");
        String url = rs.getString("url");
        studylist.add(new BlogBean(id, ctime, title, description, source, picUrl, url));
      }
      rs.close();
      ps.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return studylist;
  }

public List<BlogBean> getlist(BlogBean blog) {
	// TODO Auto-generated method stub
	 Connection conn = BlogDB.getConnection();
	    PreparedStatement ps = null;
	    List<BlogBean> studylist=new ArrayList<BlogBean>();
	    String sql = "select * from list where title LIKE '%?%'";
	    try {
	      ps = conn.prepareStatement(sql);
	      ps.setString(1, blog.getTitle());
	      ResultSet rs = ps.executeQuery();
	      while (rs.next()) {
	        int id = rs.getInt("id");
	        String ctime = rs.getString("ctime");
	        String title = rs.getString("title");
	        String description = rs.getString("description");
	        String source = rs.getString("source");
	        String picUrl = rs.getString("picUrl");
	        String url = rs.getString("url");
	        studylist.add(new BlogBean(id, ctime, title, description, source, picUrl, url));
	      }
	      rs.close();
	      ps.close();
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	    return studylist;
}

public List<BlogBean> getlist() {
	// TODO Auto-generated method stub
	return null;
}

public int update(BlogBean b) {
	// TODO Auto-generated method stub
	int flag=0;
	Connection conn=DBUtils.getConnection();
	PreparedStatement ps=null;
	String sql="update users set nickname=?,password=? where username=?";
	try {
		ps=conn.prepareStatement(sql);
		ps.setInt(1, b.getId());
		ps.setString(2, b.getSource());
		ps.setString(3, b.getTitle());
		ps.setInt(4, b.getId());

		flag=ps.executeUpdate();
		ps.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	DBUtils.closeConnection(conn);
	return flag;
}
}