# 1. 自定义向量化应用的image制作

## 1.1. 链接

[t2v-transformers-models](https://github.com/zjt148680/t2v-transformers-models)

## 1.2. 镜像制作

t2v-transformers-models项目根目录下运行Dockerfile文件最后的build命令即可

注意命令中使用了代理，不需要的话删除相关配置

# 2. 数据库修改

## 2.1 新增表

执行./.config/db/*.sql

共两张表

## 2.2 注意

当前配置的数据库都是default，根据实际修改sql文件

# 3. docker-compose

./.config/docker/docker-compose.yml

## 3.1. 启动

可统一启动，也可以一个个启动

## 3.2. 数据库修改

这部分使用实际运行的数据库即可，不用再配置

## 3.3 统一修改

注意挂载目录，这部分需要全部修改

# 4. 向量库表的创建（只需在weaviate服务启动成功后在bash执行一次）

执行./.config/db/vdb.py

# 4. 本项目修改

## 4.1. 需要覆盖的配置项

下面的项在主模块配置文件中重写即可

```
spring.application.name=speedbotagent
server.port=${SERVER_PORT:17777}

spring.datasource.url=${DATABASE_URL:jdbc:mysql://127.0.0.1:3306/default_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true}
spring.datasource.username=${DATABASE_USERNAME:root}
spring.datasource.password=${DATABASE_PASSWORD:mysql_password}
```

# 5. 当作module引入时的处理

按实际修改

# 6. 前端页面

./config/1.html

供测试用