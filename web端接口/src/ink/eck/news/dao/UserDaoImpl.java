package ink.eck.news.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ink.eck.news.dbutils.DBUtils;
import ink.eck.news.entity.User;

public class UserDaoImpl implements UserDao {

	public int addUser(User u) {
		int flag=0;
		// TODO Auto-generated method stub
		//要将User写入到数据库中，必须要连接
		Connection conn=DBUtils.getConnection();
		String sql="insert into users(username,password,nickname) values(?,?,?)";
		//将用户名添加到签名表中
		String sql2="insert into user_qm(username) values(?)";
		//要执行sql语句必须有statment对象
		PreparedStatement ps=null;
		PreparedStatement ps2=null;
		try {
			ps=conn.prepareStatement(sql);
			//对参数赋值
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getNickname());

			ps2=conn.prepareStatement(sql2);
			ps2.setString(1, u.getUsername());
			//执行sql语句
			flag=ps.executeUpdate();
			flag=ps2.executeUpdate();
			ps.close();
			ps2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		DBUtils.closeConnection(conn);
		return flag;
	}

	public int updateUser(User u) {
		// TODO Auto-generated method stub
		int flag=0;
		Connection conn=DBUtils.getConnection();
		PreparedStatement ps=null;
		String sql="update users set nickname=?,password=? where username=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1, u.getNickname());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getUsername());
			flag=ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DBUtils.closeConnection(conn);
		return flag;
	}

	public User checkUser(User u) {
		// TODO Auto-generated method stub
		Connection conn=DBUtils.getConnection();
		PreparedStatement ps=null;
		User u1=null;
		String sql="select id,username,password,nickname from users where username=? and password=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				u1=new User();
				u1.setId(rs.getInt("id"));
				u1.setUsername(rs.getString("username"));
				u1.setPassword(rs.getString("password"));
				u1.setNickname(rs.getString("nickname"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBUtils.closeConnection(conn);
		return u1;
	}

	public int deleteUserById(int id) {
		// TODO Auto-generated method stub
		int flag=0;
		Connection conn=DBUtils.getConnection();
		PreparedStatement ps=null;
		String sql="delete from users where id=?";
		try {
			ps=conn.prepareStatement(sql);
			ps.setInt(1, id);
			flag=ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DBUtils.closeConnection(conn);
		return flag;
	}

	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		Connection conn=DBUtils.getConnection();
		PreparedStatement ps=null;
		List<User> datas=new ArrayList<User>();
		String sql="select id,username,password,nickname from users";
		try {
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			//当数据库连接关闭后，保存在ResultSet结果集中的数据将会丢失
			while(rs.next()){
				int id=rs.getInt("id");
				String username=rs.getString("username");
				String password=rs.getString("password");
				String nickname=rs.getString("nickname");
				datas.add(new User(id,username,password,nickname));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		DBUtils.closeConnection(conn);
		return datas;
	}
	public static void main(String[] args){
		UserDao dao=new UserDaoImpl();
		//int flag=dao.deleteUserById(2);
		List<User> datas=dao.getAllUser();
		for(User u:datas){
			System.out.print(u.getUsername());
		}
	}
}
