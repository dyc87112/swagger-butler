# Swagger Butler

当我们构建分布式系统的时候，虽然我们可以用Swagger来方便为每个服务自动产出API文档页面。但是随着服务数量的增多，内部服务间的依赖关系的复杂度增加，每个服务开发人员要关心和查阅的文档变得越来越多。由于每个服务的文档地址可能都不一样，这使得不得不维护一个文档的索引来方便查阅，并且这个索引还需要不断的去维护更新。

那么有没有什么工具可以帮我们快速的汇集这些服务的Swagger文档呢？只需要记住一个访问地址，就可以访问所有服务的API文档？当服务增加的时候，不需要手工维护就能自动发现新服务的API文档？如果您有上面的这些问题，那么不妨看看这个项目，或许可以解决您的这些烦恼！

Swagger Butler是一个基于Swagger与Zuul构建的API文档汇集工具。通过构建一个简单的Spring Boot应用，增加一些配置就能将现有整合了Swagger的Web应用的API文档都汇总到一起，方便查看与测试。

**项目地址**

- Github：https://github.com/dyc87112/swagger-butler
- Gitee：https://gitee.com/didispace/swagger-butler

# 使用手册

## 版本说明

swagger-butler的使用版本与Spring Boot版本直接相关，对应关系如下；

| Spring Boot版本 | swagger-butler版本 |
|---|---|
|1.x|1.x|
|2.x|2.x|

当前最新版本2.0.0，1.x版本将不再继续更新。

## 快速入门 

该工具的时候非常简单，先通过下面几步简单入门：

**第一步**：构建一个基础的Spring Boot应用

如您还不知道如何创建Spring Boot应用，可以先阅读[本篇入门文章](http://blog.didispace.com/spring-boot-learning-1/)

**第二步**：在pom.xml中引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>com.didispace</groupId>
        <artifactId>swagger-butler-core</artifactId>
        <version>2.0.0</version>
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

# default config
swagger.butler.api-docs-path=/v2/api-docs
swagger.butler.swagger-version=2.0

# swagger resource
zuul.routes.user.path=/service-a/**
zuul.routes.user.url=http://localhost:10010/
swagger.butler.resources.user.name=user-service

# swagger resource
zuul.routes.product.path=/service-b/**
zuul.routes.product.url=http://localhost:10020/
swagger.butler.resources.product.name=product-service
swagger.butler.resources.product.api-docs-path=/xxx/v2/api-docs
swagger.butler.resources.product.swagger-version=2.0
```

上面配置了两个文档位置，由于这里还没有引入服务发现机制，所以Zuul的路由需要我们自己配置。然后在配置resource信息的时候，从1.1.0版本开始做了较大的调整，由于具体的访问路径是可以通过路由信息产生的，所以对于resource的配置信息只关注三个内容：

- `name`：API文档在swagger中展现名称
- `api-docs-path`：要获取的swagger文档的具体路径；如果不配置会使用全局的`swagger.butler.api-docs-path`配置，默认为`/v2/api-docs`。；这里的配置主要用户一些特殊情况，比如服务自身设置了context-path，或者修改了swagger默认的文档路径
- `swagger-version`：swagger版本信息；如果不配置会使用全局的`swagger.butler.swagger-version`配置，默认为`2.0`。

**第五步**：查看聚合文档。

原生文档：访问`http://localhost:11000/swagger-ui.html`

![Example-1](https://github.com/dyc87112/swagger-butler/blob/master/static/example.png?raw=true)

增强文档：访问`http://localhost:11000/doc.html`

![Example-2](https://github.com/dyc87112/swagger-butler/blob/master/static/example-2.png?raw=true)

> 代码示例具体可见`swagger-butler-example-static`目录

## Zuul的路由与SwaggerResources配置之间的关系

如上示例中`<route-name>`展示了Zuul的路由名称与SwaggerResources配置之间的关联关系

```properties
zuul.routes.<route-name>.path=/service-b/**
zuul.routes.<route-name>.url=http://localhost:10020/

swagger.butler.resources.<route-name>.name=product-service
swagger.butler.resources.<route-name>.api-docs-path=/xxx/v2/api-docs
swagger.butler.resources.<route-name>.swagger-version=2.0
```

> 注意：在没有使用自动配置或整合服务治理的时候，要生成Swagger文档的时候，resources信息中的`name`属性是必须配置的，`api-docs-path`和`swagger-version`不配置的时候会使用默认的全局配置

## 全局配置

对于Swagger文档获取的全局配置内容，目前主要包含下面几个参数：

```properties
swagger.butler.api-docs-path=/v2/api-docs
swagger.butler.swagger-version=2.0
```

## 使用Zuul中的路由自动配置

在快速入门示例中我们配置了两个路由信息，同时为这两个路由信息配置了对应的Swagger信息来获取API文档详情，从1.1.0版本开始，增加了几个通过Zuul的路由配置来自动生成文档信息的参数，这样可以减少快速入门示例中那些繁琐的配置。对于快速入门例子，我们可以做如下改造：

```properties
# swagger resource
zuul.routes.user.path=/service-a/**
zuul.routes.user.url=http://localhost:10010/

# swagger resource
zuul.routes.product.path=/service-b/**
zuul.routes.product.url=http://localhost:10020/

# use zuul routes generate swagger resources
swagger.butler.auto-generate-from-zuul-routes=true
```

在设置了`swagger.butler.auto-generate-from-zuul-routes=true`之后会默认的根据zuul中的路由信息来生成SwaggerResource。其中，原来resource中的`name`会使用zuul route的名称（比如：上面的user和product），而`api-docs-path`和`swagger-version`配置会使用默认的全局配置。如果resource中的三个参数有特殊情况要处理，可以采用快速入门中的配置方式来特别指定即可。

### 忽略某些路由生成

```properties
# swagger resource
zuul.routes.user.path=/service-a/**
zuul.routes.user.url=http://localhost:10010/

# swagger resource
zuul.routes.product.path=/service-b/**
zuul.routes.product.url=http://localhost:10020/

# use zuul routes generate swagger resources
swagger.butler.auto-generate-from-zuul-routes=true
swagger.butler.ignore-routes=product
```

如上示例，通过`swagger.butler.ignore-routes`参数可以从当前配置的路由信息中排除某些路由内容不生成文档，配置内容为zuul中的路由名称，配置多个的时候使用`,`分割。

> 注意：`swagger.butler.ignore-routes`和`swagger.butler.generate-routes`不能同时配置。这两个参数都不配置的时候，默认为zuul中的所有路由生成文档。

### 指定某些路由生成

```properties
# swagger resource
zuul.routes.user.path=/service-a/**
zuul.routes.user.url=http://localhost:10010/

# swagger resource
zuul.routes.product.path=/service-b/**
zuul.routes.product.url=http://localhost:10020/

# use zuul routes generate swagger resources
swagger.butler.auto-generate-from-zuul-routes=true
swagger.butler.generate-routes=product
```

如上示例，通过`swagger.butler.generate-routes`参数可以从当前配置的路由信息中指定某些路由内容生成文档，配置内容为zuul中的路由名称，配置多个的时候使用`,`分割。

> 注意：`swagger.butler.ignore-routes`和`swagger.butler.generate-routes`不能同时配置。这两个参数都不配置的时候，默认为zuul中的所有路由生成文档。

## 与服务治理整合

### 与eureka整合

在整合eureka获取所有该注册中心下的API文档时，只需要在上面工程的基础上增加下面的配置：

**第一步**：`pom.xml`中增加`eureka`依赖，比如：

```xml
<dependencies>
    <dependency>
        <groupId>com.didispace</groupId>
        <artifactId>swagger-butler-core</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
        <version>2.0.0.RELEASE</version>
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

**第三步**：修改配置文件，增加eureka的配置，比如：

```properties
spring.application.name=swagger-butler-example-eureka
server.port=11001

eureka.client.service-url.defaultZone=http://eureka.didispace.com/eureka/

swagger.butler.auto-generate-from-zuul-routes=true
swagger.butler.generate-routes=swagger-service-a, swagger-service-b

swagger.butler.resources.swagger-service-b.api-docs-path=/xxx/v2/api-docs
```

由于整合了eureka之后，zuul会默认为所有注册服务创建路由配置（默认的路由名为服务名），所以只需要通过`swagger.butler.auto-generate-from-zuul-routes=true`参数开启根据路由信息生成文档配置的功能，配合`swagger.butler.ignore-routes`和`swagger.butler.generate-routes`参数就可以指定要生成的范围了，如果某些服务需要特殊配置，也可以通过`wagger.butler.resources.*`的配置来覆盖默认设置，比如上面的`swagger.butler.resources.swagger-service-b.api-docs-path=/xxx/v2/api-docs`指定了`swagger-service-b`服务获取swagger文档的请求路径为`/xxx/v2/api-docs`。

> 代码示例具体可见`swagger-butler-example-eureka`目录

### 与consul整合 

在整合eureka获取所有该注册中心下的API文档时，只需要在上面工程的基础上增加下面的配置：

**第一步**：`pom.xml`中增加`consul`依赖，比如：

```xml
<dependencies>
    <dependency>
        <groupId>com.didispace</groupId>
        <artifactId>swagger-butler-core</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        <version>2.0.0.RELEASE</version>
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

swagger.butler.auto-generate-from-zuul-routes=true
swagger.butler.generate-routes=swagger-service-a, swagger-service-b

swagger.butler.resources.swagger-service-b.api-docs-path=/xxx/v2/api-docs
```

这里除了consul自身的配置之外，其他内容与整合eureka时候的是一样的。

> 代码示例具体可见`swagger-butler-example-consul`目录

# 贡献者

- [程序猿DD](https://github.com/dyc87112)

# 推荐内容

- [我的博客](http://blog.didispace.com)
- [知识星球](https://t.xiaomiquan.com/zfEiY3v)
- [Spring Boot基础教程](http://blog.didispace.com/Spring-Boot%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/)
- [Spring Cloud基础教程](http://blog.didispace.com/Spring-Cloud%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/)
- [公益调试Eureka](http://eureka.didispace.com)

# 公众号

![干货分享](http://git.oschina.net/uploads/images/2017/0105/082137_85109d07_437188.jpeg)
