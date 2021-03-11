SpringCloud Alibaba Nacos 注册中心和配置中心


导入这个模块需要添加  bootstrap.yml(系统级) 配置文件。该文件优先加载与 application.yml(用户级) 文件

使用 配置中心，修改配置文件内容，达不到修改之后的效果 在 使用 配置文件内容的类上添加
        
    @RefreshScope   //SpringCloud原生注解 支持Nacos的动态刷新功能