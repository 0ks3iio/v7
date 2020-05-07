1.程序名称：telephoneService（天波考勤话机服务器）
    telephoneService 远程式话机（一般部署在/opt/telephoneService,监听10100端口）
    
2.功能简介：接收设备(考勤机)发来的考勤记录。

3.目录结构
    telephoneService(程序主目录)
    |__run（启动脚本目录）
    |   |__telephoneService.sh（Linux启动脚本）
    |   
    |
    |__cfg（配置文件目录）
    |   |__serviceConfig.xml 程序主配置文件
    |   |__log4j.properties 日志信息配置
    |
    |__lib（Java类库及代码目录，非开发人员不用关注）


4.部署步骤
    （1）.检查操作系统是否配置JAVA_HOME环境变量,如果没有就加上，注意JDK必须是1.6及以上。
    （2）.程序通常部署在Linux环境下的/opt/telephoneService目录。
    （3）.修改conf.properties的参数配置，conf.properties文件在/opt/telephoneService/cfg目录下。
    （4）.修改telephoneService.sh中的JAVA_HOME参数，根据实际jdk路径来填写，telephoneService.sh文件在/opt/telephoneService/run目录下。
    （5）.修改完成后启动服务./telephoneService.sh start
        PS：以上5步做完之后就完成了telephoneService在Linux环境下的部署。
    
5.启动/关闭方法
  telephoneService.sh脚本有四个参数说明如下：
  start     ：启动程序
  stop      ：关闭程序
  restart   ：重启程序
  status    ：查看程序运行状态

6.代码结构介绍
远距离式话机（天波版）：
        接口文档：docs目录下的通讯协议word文档
        接口介绍：  
                公话认证操作 对应 PhoneAuthenMessage类                funcNo[10]                       
                网络连接状态查询（心跳） 对应ConnectStatusMessage类   funcNo[05]
                获取公话状态操作 对应 AbtStatusMessageResp类          funcNo[82]           
                学生签到记录 对应 SignRecordMessage类                 funcNo[04] 
		学生进出校记录 对应 SignMultiRecordMessage类          funcNo[08]                     
  
