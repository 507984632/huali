#rabbitmq ```镜像集群 配置策略```
>在参考这个文件之前，您应先了解 rabbitmq 中的主从集群，然后在观看本文

## 主从集群弊端
    现有三个节点 A B C， A 为主节点，BC 为从节点，主从集群 会将所有的交换机，消息队列(不包含消息队列中的消息)都有一份，
    当 A 节点 宕机时，从BC 中拿取A节点中的消息时，会有异常出现，因为 主从架构不会将消息一同保存，
    所以才有了 镜像集群架构
## 镜像集群
    镜像集群是在 主从集群的架构上添加了一些 策略，而不是通过配置一些其他的东西
    
### 镜像策略说明
    `rabbitmqctl set_policy [-p <vhost>] [--priority <priority>] [--apply-to <apply-to>] <name> <pattern> <definition>`
    
    -p vhost：可选参数，针对不同的虚拟主机中的 queue 进设置
    name：policy(策略)名称
    pattern：queue 的匹配模式(正则表达式)
    Definition：镜像定义，包含三个部分 ha-mode,ha-params, ha-sync-mode
           ha-mode：指明镜像队列的模式，有效值为 all/exacctly/nodes
                all：表示在集群中所有的节点上进行镜像
                exactly：表示在指定个数的节点上进行镜像，节点的个数由ha-params指定
                nodes：表示在指定的节点上进行镜像，节点名称通过 ha-params指定
           ha-params：ha-mode 模式需要用到的参数
           ha-sync-mode：进行队列中消息的同步方式，有效值为 automatic(自动同步) 和 manual(手动同步)
           priority：可选参数，policy (策略)的优先级

###rabbitmq策略常用命令
    1. 查看当前策略 
        rabbitmqctl list_policies
    2. 添加策略
        rabbit的工具 添加策略  策略名  正则       参数
        rabbitmqctl set_policy ha-all '^hello' '{"ha-mode":"all","ha-sync-mode":"automatic"}'
        说明：策略正则表达式 
                '^'：表示匹配所有队列名称 
                '^hello'：表示匹配 hello 开头的队列
    3. 删除策略
        rabbitmqctl clear_policy 策略名
