# Simple-Db-Router
一个简单的Mybatis分库分表starter组件

在项目中引入配置文件如下
```properties
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.url=jdbc:mysql://ip:port/vipuser?useSSL=false

server.port=8080

mybatis.mapper-locations=classpath:mapper/*.xml

db-router.db.datasource.dbCount=2
db-router.db.datasource.tbCount=4
db-router.db.datasource.default=db00
db-router.db.datasource.list=db01,db02
db-router.db.datasource.db00.driver-class-name=com.mysql.jdbc.Driver
db-router.db.datasource.db00.url=jdbc:mysql://ip:port/vipuser?useSSL=false
db-router.db.datasource.db00.username=root
db-router.db.datasource.db00.password=123456

db-router.db.datasource.db01.driver-class-name=com.mysql.jdbc.Driver
db-router.db.datasource.db01.url=jdbc:mysql://ip:port/vipuser?useSSL=false
db-router.db.datasource.db01.username=root
db-router.db.datasource.db01.password=123456

db-router.db.datasource.db02.driver-class-name=com.mysql.jdbc.Driver
db-router.db.datasource.db02.url=jdbc:mysql://ip:port/vipuser?useSSL=false
db-router.db.datasource.db02.username=root
db-router.db.datasource.db02.password=123456
```
