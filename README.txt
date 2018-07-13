日报生成器2.0版本

主要升级内容为:
你可以一边工作,一边将工作内容写入一个指定的文件中
我们先称它为预处理文档
这个文件可以在startWork.bat中使用批处理语句打开
一定不要用window自带的记事本打开,因为它会给这个文件的内容加一个多余的前缀
推荐用notepad++,使用utf-8编码存储(没有BOM!!)
比如说,我的批处理命令为:
@echo off
start E:\ShadowSocksR\ShadowsocksR-dotnet4.0.exe
start chrome https://www.baidu.com http://jira.ttpai.cn/secure/Dashboard.jspa https://exmail.qq.com/cgi-bin/frame_html?sid=yNuZS0i3ODgGPxVO,2&r=0ab1011bf411e74f1b753dbd5fd94243
start E:\Tencent\TIM\Bin\QQScLauncher.exe
start E:\Tencent\WeChat\WeChat.exe
start D:\eMessage\E-Message\E-Message.exe
start D:\eclipse-jee-photon\eclipse\eclipse.exe
start E:\Evernote\Evernote.exe
#加一行
start notepad++ C:\Users\graci\Documents\dailyReport\PreDeal.pre
下班时,日报生成器将会将你填写进去的工作内容自动写入日报模板
你只需要做一些修改即可

预处理文档应该遵循以下格式
a(当天工作成果)
1.工作成果1
2.工作成果2

b(碰到的问题和解决方案)
1.问题1
2.问题2

c(明天工作计划)
1.计划1
2.计划2