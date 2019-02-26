# 日报生成器

## 版本号
3.0

## 更新说明
增加jira任务读取能力

## 功能
嗨!我的同事小伙伴  
如果你发现了这个项目  
那么恭喜你捡到宝了  
你可以使用该项目jar包  
配合操作系统定时任务  
自动生成日报和清理存放了太久的日报文件  
每周二还会在日报后面附加code review模板哦~  
欢迎在项目下评论来反馈问题或提出你的优化建议

## 用法
### 使用jdk1.8和maven编译源代码
下载本项目和依赖项目的源代码
```
git@github.com:qiaotaizi/dailyreport.git
git@github.com:qiaotaizi/jaiz-util.git
```
分别编译安装至本地maven仓库
```
mvn clean install -Dmaven.test.skip=true
```
### 必要的配置
某些特定的参数我无法帮你推断  
需要你主动地填写在一个文件中  
首先创建一个dailyReport.properties文件  
建议使用位置%user.home%\Documents\dailyReport\dailyReport.properties  
当然你也可以在其他几个特定地位置创建配置文件  
我会依次查找以下目录来发现配置文件:  
%user.home%  
%user.home%\Documents  
%user.home%\Documents\dailyReport  
本jar包所在目录  
如果在上述多个目录下发现了配置文件  
而且文件中包含相同地配置项时  
后发现地配置项会覆盖先发现地配置项  
你必须主动地配置以下几个项目  
ps:配置项中地非英文字母要使用unicode字符来替代  
否则可能会乱码
```
#你的姓名
reporter.name=

#你的部门名称
department.name=

收件人邮箱地址,多个值时用英文逗号分割
email.receivers=

抄送人邮箱地址,多个值时用英文逗号分割
email.cc.recievers=

codeReview对象邮箱地址
code.review.reciever=

#如果你打算从jira中读取日报内容,你还需要填写下面的配置项
#开启jira读取
read.data.from.jira=true
#你的jira用户名
jira.login.username=

#你的jira密码
jira.login.password=
```
如果你忘记填写了必要地配置项  
我会在命令行的输出中提示你

### 可选的配置
这段代码的启动配置还有很多  
当然大多数不需要你主动配置  
只要接受默认的约定就好  
如果你希望更全面地掌控程序的运行结果  
请移步[Config.java](https://github.com/qiaotaizi/dailyreport/blob/master/src/main/java/com/jaiz/dailyreport/config/Config.java)
查看所有配置  
并在你的dailyReport.properties文件中加入相应的项目

### 生成日报
#### 使用预处理文档(默认方式)
本工具推荐使用jira读取的方式生成日报  
但也可以使用预处理文档,预先填写日报内容  
并根据填写的内容生成日报  
首先创建一个PreDeal.pre文件  
默认位置%user.home%\Documents\dailyReport\PreDeal.pre  
你可以使用pre.deal.file.position配置项来指定预处理文档的位置  
填写文档内容:
```
a(当天工作成果)
1.{任务内容} {jira链接} {任务进度}

b(碰到的问题和解决方案)
1.无

c(明天工作计划)
1.{任务内容} {jira链接} {任务进度}
```
格式就是这个样子  
你可以根据当天的实际工作情况填写各个项目
#### 读取jira(推荐方式)
在你的dailyReport.properties文件中正确地做好jira配置  
其他什么也不用做  
就是这么简单

#### 运行jar包
接下来使用java命令运行jar包
在git目录\target目录下打开命令行
运行如下代码
```
java -jar dailyreport-3.0.jar
```
当然你也可以用其他你喜欢的方式调用程序

### 控制模板
不出意外的话日报已经生成了
第一次运行之后  
你会发现在report.template.position和code.review.template.position指定的位置下分别生成了日报模板和code review模板
如果你觉得生成的日报文件不太符合你的要求
你可以手动修改模板的格式,并重新运行jar包  
现在,我已经学会生成你想要的日报啦!!
