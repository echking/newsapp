package ink.eck.news.controller;

import ink.eck.news.dao.BlogDao;
import ink.eck.news.dao.BlogDaolmpl;
import ink.eck.news.entity.BlogBean;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BlogWebList extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType("text/html;charset=utf-8");
    PrintWriter out = response.getWriter();
    BlogDao dao = new BlogDaolmpl();
    List<BlogBean> bloglist= dao.getweb();
    Map<String,Object> map=new HashMap<String,Object>();
    map.put("code", Integer.valueOf(200));
    map.put("msg", "success");
    map.put("newslist", bloglist);
    String str = JSONObject.toJSONString(map);
    out.print(str);
    out.flush();
    out.close();
  }
}