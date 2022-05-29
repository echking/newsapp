package ink.eck.news.controller;

import ink.eck.news.dao.UserDao;
import ink.eck.news.dao.UserDaoImpl;
import ink.eck.news.entity.User;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserEdit extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType("text/html;character=utf-8");
    PrintWriter out = response.getWriter();
    UserDao dao = new UserDaoImpl();
    String uname = request.getParameter("username");
    String password = request.getParameter("password");
    String nickname = request.getParameter("nickname");
    User u = new User();
    u.setUsername(uname);
    u.setPassword(password);
    u.setNickname(nickname);
    int u1 = dao.updateUser(u);

    Map<String,Object> map=new HashMap<String,Object>();
    if (u1 != 0) {
      map.put("result_code", Integer.valueOf(200));
      map.put("msg", "SUCCEED");
      map.put("user", Integer.valueOf(u1));
    } else {
      map.put("result_code", Integer.valueOf(404));
      map.put("msg", "FAILED");
      map.put("user", null);
    }
    String str = JSONObject.toJSONString(map);
    out.print(str);
    out.flush();
    out.close();
  }
}