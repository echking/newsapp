package ink.eck.news.controller;

import ink.eck.news.dao.UserQmDao;
import ink.eck.news.dao.UserQmDaolmpl;
import ink.eck.news.entity.UserQmBean;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserGetQm extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType("text/html;character=utf-8");
    PrintWriter out = response.getWriter();
    UserQmDao dao = new UserQmDaolmpl();
    String username = request.getParameter("username");
    UserQmBean u = new UserQmBean();
    u.setUsername(username);
    UserQmBean u1 = dao.getUserQm(u);
    Map<String,Object> map=new HashMap<String,Object>();
    if (!u1.equals(""))
    {
      map.put("qmm", u1);
    } else {
      map.put("result_code", Integer.valueOf(404));
      map.put("msg", "FAILED");
      map.put("qmm", "这个人很懒还没有什么描述~");
    }
    String str = JSONObject.toJSONString(map);
    out.print(str);
    out.flush();
    out.close();
  }
}