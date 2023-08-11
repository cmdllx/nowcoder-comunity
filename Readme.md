# Readme

Tis:
import 库的时候注意很多重名的库，看清楚namespace

## 基础功能
### 开发社区首页
![img.png](/pics/img.png)
操作数据表为user和discusspost，两者通过userid连接

创建entity，mapper（dao），service来实现对数据库的操作

最后使用分页功能，实现上下页和按指定页数跳转的功能（浏览器中的显示功能需要到index.html中进行一定的修改）

### git功能同步
一开始只使用中央服务器统一保管，容易丢失
![git.png](/pics/git.png)
git采用分布式版本控制，本地仓库和远程仓库

IDEA使用Git实现同步

### 开发登录功能
#### 发送邮件
![email.png](/pics/email.png)
注意，使用真实邮箱的时候需要注意授权问题，在设置里面打开，而且有可能需要将配置文件中的password设置为邮箱的验证码

使用工具类MailClient

#### 注册功能
![register.png](/pics/register.png)

CommunityUtil实现加密功能
注册功能在LoginController中实现

#### 会话管理
![session.png](/pics/session.png)
Http是无状态的，两次请求之间没有关系，使用cookies创建有状态的会话

Cookie由服务器创建发送到浏览器并且保存在浏览器本地，浏览器发送请求时携带发送到服务器上。Cookie只能存字符串类的相应数据，但是session啥都能存
![](/pics/cookie_illustration.png)
Cookie一方面不安全，另一方面会占用资源。对应的解决方法就是Session（比如存密码），但是占用服务器资源
![](/pics/session_illustration.png)

总结来说，一般用cookie，分布式部署session用的比较少

分布式中，无论是黏性session，还是同步session还是共享session,甚至Redis数据库存储session都存在问题，与分布式解决性能瓶颈的初衷有些违背
![](/pics/session分布式.png)

#### 生成验证码
![](/pics/验证码.png)
验证码有现成的Kaptcha工具，wikis里面有how to use
因为是github的依赖，springboot没有做相应的配置，需要自己配置KaptchaConfig（实例化Producer接口）。然后在LoginController中添加验证码功能，在login.html中添加对应位置
这边需要在login.html页面修改验证码相关的信息（验证码路径已经刷新验证码）

#### 登录退出功能
![](/pics/登录退出功能.png)

CREATE TABLE `login_ticket` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`user_id` INT(11) NOT NULL,
`ticket` VARCHAR(45) NOT NULL,
`status` INT(11) DEFAULT '0' COMMENT '0-有效; 1-无效;',
`expired` TIMESTAMP NOT NULL,
PRIMARY KEY (`id`),
KEY `index_ticket` (ticket(20))
) ENGINE=INNODB DEFAULT CHARSET=utf8;


login_ticket这张表来存储用户的登录凭证

LoginTicketMapper中用注解的方式操作数据库,eg:    

@Insert({
"insert into login_ticket(user_id,ticket,status,expired) ",
"values(#{userId}, #{ticket}, #{statis}, #{expired})"
})//用逗号分开自动拼接，注意留出一个空格
@Options(useGeneratedKeys = true, keyProperty = "id")
public int methid(){}

这边mapper的注释实现也支持script(注意双引号需要转义)

在html文件中要显示默认值（例如账号密码）可以用request自带的功能，比如：
th:value="${param.username}"，这句话value的意思相当于request.getParameter(username)

网页里面动态判断：

<input type="text" th:class="|form-control ${usernameMsg!=null?'is-invalid':''}|"
th:value="${param.username}"
id="username" name="username" placeholder="请输入您的账号!" required>
<div class="invalid-feedback" th:text="${usernameMsg}">
该账号不存在!
</div>

userservice和logincontroller里面实现login和logout

#### 显示登录信息
![img.png](pics/显示登录信息.png)
拦截器也算表现层controller，在controller下面新建package：interceptor

拦截器除了Component注解，还要implements HandlerInterceptor，里面一共就三个拦截方法,同时配置类WebMvcConfig

![img.png](pics/拦截器处理流程.png)
配置 LoginTicketiterceptor

因为服务器对浏览器是一对多，每次创建一个新的线程来解决请求（多线程环境考虑隔离，用TreadLocal工具（以线程为key存取值））,针对这个问题编写工具类HostHolder

注意在WebMvcConfig添加拦截器的时候选择拦截的路径



