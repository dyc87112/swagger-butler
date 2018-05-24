# Swagger Butler

Swagger Butler是一个基于Swagger与Zuul构建的API文档汇集工具。通过构建一个简单的Spring Boot应用，增加一些配置就能将现有整合了Swagger的Web应用的API文档都汇总到一起，方便查看与测试。

**项目地址**

- Github：https://github.com/dyc87112/swagger-butler
- Gitee：https://gitee.com/didispace/swagger-butler

**推荐内容**

- 我的博客：http://blog.didispace.com
- 我们社区：http://www.spring4all.com
- 知识星球（深度交流与问答）：https://t.xiaomiquan.com/zfEiY3v
- Spring Boot基础教程：http://blog.didispace.com/Spring-Boot%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/
- Spring Cloud基础教程：http://blog.didispace.com/Spring-Cloud%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/
- 公益调试Eureka：http://eureka.didispace.com

# 使用手册

## 快速入门 

该工具的时候非常简单，先通过下面几步简单入门：

**第一步**：构建一个基础的Spring Boot应用

如您还不知道如何创建Spring Boot应用，可以先阅读[本篇入门文章](http://blog.didispace.com/spring-boot-learning-1/)

**第二步**：在pom.xml中引入依赖

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.10.RELEASE</version>
</parent>

<dependencies>
    <dependency>
        <groupId>com.didispace</groupId>
        <artifactId>swagger-butler-core</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

**第三步**：创建应用主类，增加`@EnableSwaggerButler`注解开启Swagger Butler功能

```java
@EnableSwaggerButler
@SpringBootApplication
public class StaticApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaticApplication.class);
    }

}
```

**第四步**：配置文件中增加Swagger文档的地址配置

```properties
spring.application.name=swagger-butler-example-static
server.port=11000

zuul.routes.service-a.path=/service-a/**
zuul.routes.service-a.url=http://localhost:10010/
swagger.butler.resources[0].name=service-a
swagger.butler.resources[0].url=/service-a/v2/api-docs
swagger.butler.resources[0].swagger-version=2.0

zuul.routes.service-b.path=/service-b/**
zuul.routes.service-b.url=http://localhost:10020/
swagger.butler.resources[1].name=service-b
swagger.butler.resources[1].url=/service-b/v2/api-docs
swagger.butler.resources[1].swagger-version=2.0
```

上面配置了两个文档位置，由于这里还没有引入服务发现机制，所以需要先用zuul来配置访问本应用请求被转发到具体服务的路由规则。然后在配置resource信息指向具体的获取swagger的json配置文档的接口链接。

**第五步**：访问`http://localhost:11000/swagger-ui.html`

![Example](https://github.com/dyc87112/swagger-butler/blob/master/static/example.png)

> 代码示例具体可见`swagger-butler-example-static`目录

**原理可见：[Spring Cloud Zuul中使用Swagger汇总API接口文档](http://blog.didispace.com/Spring-Cloud-Zuul-use-Swagger-API-doc/)**

## 与eureka整合

在整合eureka获取所有该注册中心下的API文档时，只需要在上面工程的基础上增加下面的配置：

**第一步**：`pom.xml`中增加`eureka`依赖，比如：

```xml
<dependencies>
    <dependency>
        <groupId>com.didispace</groupId>
        <artifactId>swagger-butler-core</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
        <version>1.3.2.RELEASE</version>
    </dependency>
</dependencies>
```

**第二步**：应用主类增加`@EnableDiscoveryClient`，比如：

```java
@EnableDiscoveryClient
@EnableSwaggerButler
@SpringBootApplication
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class);
    }

}
```

**第三步**：配置文件中增加eureka的配置，比如：

```properties
spring.application.name=swagger-butler-example-eureka
server.port=11001


eureka.client.service-url.defaultZone=http://eureka.didispace.com/eureka/
```

> 代码示例具体可见`swagger-butler-example-eureka`目录

## 与consul整合 

在整合eureka获取所有该注册中心下的API文档时，只需要在上面工程的基础上增加下面的配置：

**第一步**：`pom.xml`中增加`consul`依赖，比如：

```xml
<dependencies>
    <dependency>
        <groupId>com.didispace</groupId>
        <artifactId>swagger-butler-core</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        <version>1.3.2.RELEASE</version>
    </dependency>
</dependencies>
```

**第二步**：应用主类增加`@EnableDiscoveryClient`，比如：

```java
@EnableDiscoveryClient
@EnableSwaggerButler
@SpringBootApplication
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class);
    }

}
```

**第三步**：配置文件中增加eureka的配置，比如：

```properties
spring.application.name=swagger-butler-example-consul
server.port=11002

spring.cloud.consul.host=localhost
spring.cloud.consul.port=8500
```

> 代码示例具体可见`swagger-butler-example-consul`目录

# 贡献者

- [程序猿DD](https://github.com/dyc87112)

# 公众号

![干货分享](http://git.oschina.net/uploads/images/2017/0105/082137_85109d07_437188.jpeg)