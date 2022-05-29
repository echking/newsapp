package ink.eck.news.dao;
import ink.eck.news.entity.BlogBean;
import ink.eck.news.entity.User;

import java.util.List;

//获取博客列表接口
public abstract interface BlogDao
{
List<BlogBean> getcu();
List<BlogBean> getweb();
List<BlogBean> getandroid();
List<BlogBean> getstudy();
List<BlogBean> getlist();
int update(BlogBean b);
}