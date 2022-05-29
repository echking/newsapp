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

public class UserQmEdit extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType("text/html;character=utf-8");
    PrintWriter out = response.getWriter();
    UserQmDao dao = new UserQmDaolmpl();
    //通过用户名修改签名
    //签名表中含username和qm两个字段
    String username = request.getParameter("username");
    String qm = request.getParameter("qm");
    UserQmBean u = new UserQmBean();
    //传入修改签名的用户，和修改的签名
    u.setUsername(username);
    u.setQm(qm);
    int u1 = dao.updataQm(u);
    Map<String,Object> map=new HashMap<String,Object>();
    if (u1 != 0) {
      map.put("result_code", Integer.valueOf(200));
      map.put("msg", "SUCCEED");
      map.put("user", qm);
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