## 项目介绍

engine是航空监视项目的后台，处理其他组件的数据、服务请求。

## 运行条件

- JDK >= 1.8 (推荐1.8版本)
- Mysql >= 5.5.0 (推荐5.7版本)
- Maven >= 3.0
- Redis

## 运行系统

1、使用`git clone`命令将本项目克隆到本地

2、以`maven`项目的形式导入到ide中，使用maven下载相应的Maven依赖

3、创建数据库`hkjs`并导入数据脚本`ry_20190215.sql，quartz.sql`，可以使用数据库连接工具导入也可以使用`mysql source`命令

4、运行`mvn clean spring-boot:run`

## 必要配置

1. 修改数据库连接

	`编辑resources目录下的application-druid.yml`

	`url: 服务器地址`

	`username: 账号`

	`password: 密码`

2. 修改redis环境配置

	`编辑resources目录下的application.yml`

	`redis:`

	​	`host: 服务器地址`

	​	`port: 端口`

	​	`password: redis连接密码`

3. 开发环境配置

	`编辑resources目录下的application.yml`

	`port: 端口`

	`context-path: 部署路径`

## 构建+部署

当新版本发布时需要向 [engine](http://172.16.6.201/hkjs/engine)和[web-ui](http://172.16.6.201/hkjs/web-ui)的master分支同时

推送相同的最新tag(形如 vX.Y.Z)，必须保证相同，不同则构建失败

在 jenkins上的 [hkjs-build](http://172.16.6.213:8080/jenkins/job/hkjs-build/)手动执行立即构建

构建成功后 去 [pub](http://172.16.6.189/pub/pkg_output/other/hkjs/)上找的最新构建的jar包

下载后根据环境手动修改application.yml上的配置项运行服务， 如下例所示

    java -Dserver.port=80  -Dspring.redis.host=172.16.2.122 -jar hkjs-v0.1.0-20200513091703-master.jar