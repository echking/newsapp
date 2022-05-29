package ink.eck.news.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import ink.eck.news.dao.UserDao;
import ink.eck.news.dao.UserDaoImpl;
import ink.eck.news.entity.User;

public class UserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//登录接口
		response.setContentType("text/html;character=utf-8");
		PrintWriter out = response.getWriter();
		UserDao dao=new UserDaoImpl();
		String uname=request.getParameter("username");
		String password=request.getParameter("password");
		User u=new User();
		u.setUsername(uname);
		u.setPassword(password);
		//UserDao.checkUser(uname,password)
		User u1=dao.checkUser(u);
		Map<String,Object> map=new HashMap<String,Object>();
		if(u1!=null){
			map.put("result_code", 200);
			map.put("msg", "SUCCESS");
			map.put("user", u1);
		}else{
			map.put("result_code", 404);
			map.put("msg", "FAIL");
			map.put("user", null);
		}
		String str=JSONObject.toJSONString(map);
		out.print(str);
		out.flush();
		out.close();
	}

}
