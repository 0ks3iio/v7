关于运营平台程序包的说明
一、运营平台采用Spring Boot框架，使用内嵌Tomcat，将整个程序包打包成可执行的压缩包，通过脚本启动
    1、直接通过startup.sh 启动
    2、目前没有提供shutdown脚本，按照原来Tomcat的形式直接kill掉好了
    3、因为是通过"java -jar xx.jar" 的方式启动 所以 ps -ef|grep tomcat 查询不到信息 可以通过xx的名称查询
二、关于配置的说明
   1、运营平台可以加载包外部的配置文件，加载顺序如下：
    {spring.application.config.location}/application.properties
    -> {operation.directory}/config/application.properties
    -> {operation.directory}/application.properties
    -> 包内配置
    说明：spring.application.config.location 可在startup.sh配置绝对路径
         operation.directory压缩包的路径
         若前面的配置文件存在则不会向下继续查找
   2、配置文件详情和可能需要配置的参数说明
     * Tomcat相关配置
        端口号 -> server.port
        上下文 -> server.servlet.context-path
        开启http压缩 ->  server.compression.enabled
        压缩mime类型(以英文逗号分隔) -> server.compression.mime-types
        Tomcat accept队列数量 ->  server.tomcat.accept-count
        Tomcat 最大连接数 -> server.tomcat.max-connections
        Tomcat 最大线程数 -> server.tomcat.max-threads
        Tomcat 临时目录  —> server.tomcat.basedir
     * Session相关配置
        Session失效时间 -> server.servlet.session.timeout
        Cookie 失效时间 -> server.servlet.session.cookie.max-age
     * 数据库配置
        用户名 -> spring.datasource.username
        密码  ->  spring.datasource.password
        url  ->  spring.datasource.url
        驱动  -> spring.datasource.driver-class-name
     * dubbo
        zookeepr注册中心 -> szxy.dubbo.registry.address
     * 日志(运营平台产生的日志)
        日志存储位置 -> logging.path
        日志名称 -> logging.file
     说明：.内嵌的Tomcat默认使用NIO，如果有需要变更请联系开发
          .其他的配置需要新增或者不清楚的也请联系开发增加说明
          .----沈克
三、关于原Catalina.out的变更说明
   使用WAR部署的时候Tomcat的控制台输出会被重定向到 logs/catalina.out
   这里也和Tomcat类似在startup.sh 中将输出重定向到了 logs/catalina.out 每次更新包都会"覆盖"如需要变更也可以更改