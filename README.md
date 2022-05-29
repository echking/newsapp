# newsapp

## 搭建环境

- Myeclipse 10
- Android Studio Arctic Fox (2020.3.1)
- jdk1.7.0_67

## 预览

**Android端**
![QQ浏览器截图20220108112453.png](https://eck.ink/usr/uploads/2022/01/1654880849.png#vwid=1936&vhei=1066)

**web端**
![QQ浏览器截图20220108112539.png](https://eck.ink/usr/uploads/2022/01/1111839225.png#vwid=1892&vhei=1047)

## 详细设计

**这里我只介绍几个简单的接口，其他接口基本原理相同，学会了这个其他几个都不成问题。**

### 1.登录接口

登录我采用的为`HttpURLConnection`，因为当时只学了这个，所以就用这个写了，注册我采用的是`okHttp`，使用起来比`HttpURLConnection`方便的多，很多方法都集成起来了，不用再自己去敲。

> HttpURLConnection是一种多用途、轻量极的HTTP客户端，使用它来进行HTTP操作可以适用于大多数的应用程序。

* HttpClient是apache的开源框架，封装了访问http的请求头，参数，内容体，响应等等，使用起来比较方便，而HttpURLConnection是java的标准类，什么都没封装，用起来太原始，不方便，比如重访问的自定义，以及一些高级功能等。
* 从稳定性方面来说的话，HttpClient很稳定，功能强，BUG少，容易控制细节，而之前的HttpURLConnection一直存在着版本兼容的问题，不过在后续的版本中已经相继修复掉了。引用自[# [Android探索之HttpURLConnection网络请求](https://www.cnblogs.com/whoislcj/p/5520384.html)](https://www.cnblogs.com/whoislcj/p/5520384.html)

---

#### 1.1 Android端

##### 1.1.1 HttpURLConnection

**从Android 6.0之后，访问网络的代码不能放在主线程中，必须开启一个子线程。**

```java
//代码实例
if (username_edit.length() == 0) {
Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
} else {
//判断密码是否输入，如果未输入，提示“请输入密码”
if (password_edit.length() == 0) {
Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
} else {
new Thread(new Runnable() {
@Override
public void run() {
//启动子线程要做的事情全部放在run方法中
...
}
```

*在代码中我作了多个if判断，这样可以避免输入不合法的数据提交到web端，造成资源的浪费。*

##### 1.1.2 实现自动填写账号密码

在登录前我添加了一个`Shardperfrence`方法和一个`putExtra`方法，然后做了一个if判断以免两个方法发生冲突。
`Shardperfrence`方法：当用户登录时选择了记住密码时，则将用户名和密码存储起来，下一次用户登录就不需要再输入。具体实现在我的另一篇博客里有详细介绍：[『Android Studio』利用SharedPreferences类创建一个登录页面](https://eck.ink/study/207.html)

`putExtra`方法：当新用户注册时，不用从`Shardperfrence`中取出数据，可以直接用不同Activity间参数的传递，直接将注册时填入的用户名和密码传递到登录界面，同样免去了输入的步骤。具体实现可查看：[ 『Android Studio』在Activity中进行数据传递](https://eck.ink/study/133.html)

```
//代码片段
cbPd=(CheckBox)findViewById(R.id.cbPd);
        Intent it=getIntent();
        //从Sharedperfrence中获取username和password
        String name=it.getStringExtra("username_extra");
        String password=it.getStringExtra("password_extra");
        //判断Shardperfrence是否为空，未空则为新用户，从注册页获取用户名和密码
        if(name!=null&&password!=null){
            username_edit.setText(name);
            password_edit.setText(password);
        }else{
            Map<String,String>user=get_userMes(LoginActivity.this);
            final String user_name=user.get("username_cd");
            final String pass_word=user.get("password_cd");
            username_edit.setText(user_name);
            password_edit.setText(pass_word);
        }
```

##### 1.1.3 修改主线程

**前面提到了，从Android 6.0之后，访问网络的代码不能放在主线程中，必须开启一个子线程，所以我们的操作都在子线程中，如果要对主线程进行更改，则可以通过Handler+Message发送消息。**

```
...
@Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.obj.equals(404)) {
                Toast.makeText(getApplicationContext(), "账号或密码错误！", Toast.LENGTH_SHORT).show();
            } else {
                User u = (User) msg.obj;
                //在此处更新主线程中的UI
                Toast.makeText(getApplicationContext(), u.getUsername() + ",登陆成功！", Toast.LENGTH_SHORT).show();
            }
        }
    };
...
//此处省略较多代码，详细代码请在下方查看
//子线程更新主线程中的UI,不能将更新UI的操作放在子线程中
                                            //通过消息，把要更新的数据发送出去，主线程再到消息队列中去取改数据，然后在线程中更新
                                            Message message = new Message();
                                            message.obj = u;
                                            //通过Handler发送消息
                                            myHandler.sendMessage(message);
			...
```

#### 1.2 Web端

##### 1.2.1 结构

![20220420231950.png](https://eck.ink/usr/uploads/2022/04/746197256.png#vwid=301&vhei=809)

从上图可以看出，整个web端分为Controller层，Dao层，Dbutils层，Entity层。

1. Entity层：实体层，存放实体，基本上与数据库中的列名相同。
2. Dao层：DAO层主要是做数据持久层的工作，负责与数据库进行联络的一些任务都封装在此。
3. Dbutils层：用于数据库连接。我这部分其实可以更正为Service层。
4. Controller层：接收客户端的请求，然后调用Bbutils层接口控制业务逻辑，将获取到数据封装成json数据。

##### 1.2.2 登录信息验证

为了让安卓端登录时可以验证登录信息，我们需要完成以下基本流程：

![20220420233817.png](https://eck.ink/usr/uploads/2022/04/191640398.png#vwid=575&vhei=694)

前面已经介绍了如何在客户端中验证用户输入的登录信息，下文将讲解在Web端对登录信息进行验证。

要想在Web端对客户端中输入的登录信息进行验证，首先必须要将输入的登录信息传递到Web端。那么就要用到Web接口，用post方式通过请求体传递参数。
例如我的接口格式为：
https://explame.com/login.do?`username`=`admin`&`password`=`123456`
也就是将用户名 admin 密码123465传递给了Web端。

实例代码：

```
public class UserServlet extends HttpServlet {

public void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
//登录接口
response.setContentType("text/html;character=utf-8");
PrintWriter out = response.getWriter();
UserDao dao=new UserDaoImpl();
//从url中获取用户名和密码
String uname=request.getParameter("username");
String password=request.getParameter("password");
//User 为创建的实体类
User u=new User();
u.setUsername(uname);
u.setPassword(password);
//checkUser(u)方法为检查用户是否存在
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
//将数据封装成json数据返回给客户端，由客户端解析判断结果
String str=JSONObject.toJSONString(map);
out.print(str);
out.flush();
out.close();
}

}
```

**访问示例：**
![20220420235318.png](https://eck.ink/usr/uploads/2022/04/64032867.png#vwid=1069&vhei=107)

传回的json数据为：
`{"result_code":200,"user":{"id":156,"nickname":"admin","password":"1234567","username":"admin"},"msg":"SUCCESS"}`

这就是Web端验证登录信息的过程。

但整个信息验证过程还没有结束，这一条返回的json数据会传回到客户端，在客户端中由编写的程序对json数据进行解析，验证用户是否登录成功。

![20220421001521.png](https://eck.ink/usr/uploads/2022/04/2796749419.png#vwid=1085&vhei=332)

一个简单的新闻浏览app，包含接口，修改自CSDN博主
原文链接：https://blog.csdn.net/qq_34149526/article/details/80992341
增添或修改内容如下：
