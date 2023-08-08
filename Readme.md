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

#### 注册功能
![register.png](/pics/register.png)

#### 会话管理
![session.png](/pics/session.png)
