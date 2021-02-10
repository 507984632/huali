该框架为工具框架，正在逐步完善中

用法：
1. 先将本项目 maven install 到本地仓库中
2. 在自己的启动类上添加   
    2.1 @ComponentScan(basePackages = {"com.huali", "你项目包名前缀"})
    2.2 @MapperScan(basePackages = {"com.huali.**.mapper", "你项目包名前缀.**.mapper"})
    2.3 在配置文件中 添加 mybatis-plus:global-config:db-config:update-strategy: DEFAULT
    2.4 关于所有的配置类 都是以 XXProperties 结尾，可以自行寻找
    
可能有帮助的网址
1. **ngrok** 
    [内网穿透，可以让任何人通过网络访问你的本地路径](http://www.ngrok.cc)    

