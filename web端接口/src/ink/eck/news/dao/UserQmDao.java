package ink.eck.news.dao;

import ink.eck.news.entity.UserQmBean;
//对个人签名的接口
public interface UserQmDao{
  int updataQm(UserQmBean paramUserQmBean);
  UserQmBean getUserQm(UserQmBean paramUserQmBean);
}