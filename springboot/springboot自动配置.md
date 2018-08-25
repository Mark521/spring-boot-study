### 自动配置原理

1. 启动时加载配置类,EnableAutoCOnfiguration

2. EnableAutoCOnfiguration作用

   1. 利用EnableAutoCOnfigurationImportSelector给容器中导入一些组件

   2. selectImport方法

      - 从类路径下的META-INF/spring.factories
      - 封装为properties
      - 将EnableAutoConfigration.class对应的值加入到容器中

   3. 每一个自动配置类进行自动配置

   4. 以**HttpEncodingAutoConfiguration**为例进行

      ```java
      @Configuration
      @EnableConfigurationProperties({HttpEncodingProperties.class})//配置文件对应值绑定
      @ConditionalOnWebApplication//spring底层@Conditional直接，根据不同的条件，如果满足指定的条件，整个配置类的配置就会生效       当前判断应用是否是web，如果市当前配置类生效
      @ConditionalOnClass({CharacterEncodingFilter.class})//判断当前项目是否有这个类
      @ConditionalOnProperty(
          prefix = "spring.http.encoding",
          value = {"enabled"},
          matchIfMissing = true
      )
      ```

      根据当前不同的条件判断，决定这个配置类是否生效

      一但生效这个配置类就会给容器中添加各种组件了这些容器的属性是从对应的properties中获取，这些类的属性值又是与配置文件绑定

   5. 自动配置类大多在一定条件下才能生效 ，查看那些自动配置类生效 启用debug= true

# **三、日志**

## 1、日志框架





## 2、Slf4J(框架)->logback(实现)

slf4j只是一个日志框架，最终实现有不同实现(log4j性能问题，log4j2apche自己写的，logback-log4j那个人改写的，common-log java 自带)

## 3、遗留问题

使用框架使用不同的日志框架：spring(commons-logging)、hibernate(log4j)、    可以使用中间替换层，将实现用slf4j进行实现，最终实现用目标实现

##### 如何将系统中所有日志统一到slf4j

1.  将系统中其他日志框架先派出出去
2.  用中间包替换原有日志框架
3. 我们导入slf4j其他实现

## 4、 springboot日志

1. springboot底层使用sel4j+flogback进行日志记录

2. springboot将其他日志转换为slf4j

3. 中间包替换

4. 如果我们要引入那个框架，则一定要将这个框架的默认日志实现依赖移除掉

   spring用commons-logging

   ```xml
   <dependency>
   			<groupId>org.springframework</groupId>
   			<artifactId>spring-core</artifactId>
   			<exclusions>
   				<exclusion>
   					<groupId>commons-logging</groupId>
   					<artifactId>commons-logging</artifactId>
   				</exclusion>
   			</exclusions>
   		</dependency>
   ```



springboot能自动适配所有的日志，而且将底层使用slf4j+logback的方式纪律日志，引入其他框架的时候，只需要把这个框架依赖的日志框架去除掉

5. 日志级别：trace<debug<info<warn<error；调整日志级别，就只会在这个级别以后的高级级别生效，打印这个级别以后的日志

### 2、指定配置

 默认可以使用logback.xml 可以直接被日志框架识别，但是推荐使用logbak-profile.xml可以被springboot识别可以使用springboot的高级功能

```xm
<springProfile name="dev">根据不同的环境进行日志格式的输出

</springProfile>
```



### 3、切换日志框架

根据slf4j日志是配图，进行相关的切换；排除包和导入包



# 四、web开发

#### 自动配置原理

场景下springboot配置了什么？能不能修改？能修改那些配置？能不能扩展

```java
XXXXXAutoConfiguration：帮我们给容器中自动配置组件
xxxxProperties配置类来封装配置文件的内容，负责和文件中配置变量进行绑定
```

### 2、SpringWeb对静态资源默认映射规则（WebMvcAutoConfiguration.addResourceHandlers）方法

```java
public void addResourceHandlers(ResourceHandlerRegistry registry) {
            if(!this.resourceProperties.isAddMappings()) {
                logger.debug("Default resource handling disabled");
            } else {
                Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
                CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
                if(!registry.hasMappingForPattern("/webjars/**")) {
                    this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"}).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
                }

                String staticPathPattern = this.mvcProperties.getStaticPathPattern();
                if(!registry.hasMappingForPattern(staticPathPattern)) {
                    this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{staticPathPattern}).addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations())).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
                }

            }
        }
```



1.  /webjars/* 去找/META_INF/resource/webjars/*文件夹
2. /**的请求  去下面找(静态资源文件夹)

```json
{"classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"}
```

3. 首页；找静态资源下的index.html
4. 所有的**/favicon.png都在静态资源文件下找

### 4、模板引擎（推荐thymeleaf）

语法简单功能强大；

##### 1、引入pom文件

thymleaf3必须要layout2以上版本，   布局功能支持；

springboot切换版本，直接新增pom.xml的properties属性值

放到templates是由thymeleaf定义 前后缀  /templates    .html

##### 2、thymeleaf的语法

普通变量"${}"获取，国际化值使用"#{}"获取，URL“@{}”获取，模板片段“~{}”获取

##### 3、公共页面抽取

```html
1.抽取公共片段
<div th:fragment="copy">
    fasfdsafdsafdsd
</div>
2.引入公共片段
<div th:insert="~{footer::copy}"></div>
~{templatename::seletor}模板名::选择器
~{templatename::前端名}模板名::片段名

3. th:insert 元素整个插入；th:replace 元素替换为；th:include 元素片段内容插入 
```





### 5、如何修改SpringBoot的默认配置

模式

1. 配置很多组件的时候，优先先看用户是否有，如果没有则使用默认的  
2. XXXConfiguratar 帮助我们进行扩展配置



```java
//使用WebMvcConfigurerAdaquter可以扩展springmvc的功能不能@EnableMVC  
//可以实现多个，因为容器的原则是所有的都生效
@EnableMVC 全面接管MVC配置，springMVC自动配置不生效
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdaquter{
    //复写方法，以实现<mvc>标签实现的功能
    //方法实现 视图映射
    public void addViewControllers(ViewControllerRegistry registry){
        resistry.addViewController('/atguigu').setViewer("success"); 
    }
}
```

@EnableMVC注解自动配置为何失效

webmvcautoconfiguration条件注解中判断WebMvcConfiguratuionSupport组件是否存在，如果存在就不使用自动配置，如果不存在就使用自动配置EnableMVC有WebMvcConfiguratuionSupport组件的实现



### 6、国际化

1. 编写国际化配置文件
2. 使用ResourceBundleMessageSource管理国际化文件
3. 在页面使用fmt:message取出





springboot 编写国际化

1、编写国际化资源

2、MessageSourceAutoCOnfigure spring自动配置好了

​	spring.messages.basename=il8n.login

3、去页面获取国际化值

​	thymeleaf的获取国际化值使用"#{}"获取

> 如果出现乱码需要将编译器的编码设置 file encodeing

##### 需求： 根据点击转换语言

原理：国际化local（区域信息对象）；LocaleResovler(获取区域信息对象)

springboot在配置中自动增加了解析 语言的功能，WebMVCAutoConfiguration的localResolver方法默认根据解析请求的国际化Local

自己编写LocaleResolver

> 模板引擎修改后要实时生效。 spring.them.cache
>
> ###### 按Ctrl+F9重新编译





登陆错误显示 th:if 

拦截器  

==redirect:重定向到地址==

==forward：转发到地址==



Restful请求需要配置

1. springmvc中配置HiddenHttpMethodFilter（中有_method变量）（Spring boot配置好了,WebMVCAutoConfiguration）
2. 页面创建一个post表单
3. 创建一个input项，name="_method"；值就是我们指定的请求方式（/PUT/DELETE/）





### 7、错误处理机制(ErroMVCAutoConfiguration)

1）默认错误处理机制

​	浏览器返回页面，其他客户端返回json数据

ErroMVCAutoConfiguration给容器添加了组件

1. DefaultErrorAttributers;

   帮我们在在页面共享信息

   timestamp,status,error,exception,message,errors(JSR303数据校验)

2. BasicErrorController

   ```java
   处理路径请求server.error.path
   有html方式和json方式的数据
   ```

   

3. ErrpageCustomizer

   ```
   系统出现错误请求error.path:/error
   ```

   

4. DefaultErrorGViewResolver

   去那个页面由他转换，

2）如何定制错误页面，和错误json数据

 1. 通过状态码名称4xx.html 5xx.html文件放置到templates/error下

    有模板引擎，则在模板引擎下找，没有则在静态文件下找但不能渲染页面数据

    都没有则在springboot的空白页面

	2. 异常数据 

    1、@ControllerAdvice class   @ExceptionHandle方法（无自适应效果 ）

    2、出现错误后可以进行转发到return "forword:./error"; 进行自适应效果（注意传入自己的错误状态码request.setAttribute("status_code", 400) ，否则不会进入错误页面，因为会以为请求到了数据。）

    3、将定制数据携带出去（页面上能用的数据，或者json数据都是通过errorAttributes.getErrorAttributes得到，容器中的DefaultErrorAttributes.getErrorAttributes，默认进行数据处理）可以继承DefaultErrorAttributes复写getErrorAttributes方法

### 8、配置嵌入式Servlet容器

##### 1）、定制和修改Servlet容器相关配置

1，修改Server相关的配置ServerProperties[也是EmbeddedServletContainerCustomizer实现类]

2，编写一个EmbeddedServletContainerCustomizer嵌入式servlet容器定制器，来修改容器的配置



##### 2）、注册Servlet filter Listener

需要返回XXXRegisterBean@Bean 模拟配置到web.xml文件的配置 ，

SpringBoot帮我们自动spirngMVC的时候，自动的注册SpringMVC的前端控制器，DispatcherServlet



##### 3）其他Sevlet容器

tomcat(引入web模块默认使用)jetty(长连接) Undertow(不支持JSP)

排除starter-tomcat 引入 start-jetty

##### 4）、嵌入式Servlet容器自动配置原理

==EmbeddedServletContainerAutoConfiguration==

EmbeddedServletContainerFactory(嵌入式Servlet工厂)三种

==EmbeddedServletContainer==）（嵌入式Servlet容器）三种

对嵌入式容器 配置如何生效：

```
ServerProperties,EmbeddedServletContainerCustomizer
```

EmbeddedServletContainerCustomizer帮我们修改了Servlet容器的配置？

BeanPostProcessorRegister 

步骤：

1、根据导入情况，导入响应的容器工厂，

2、容器中某个组件创建对象就会惊动后置处理器

3、获取所有Servlet定制器，执行customize方法



##### 5）Servlet容器启动原理

容器工厂何处执行

嵌入式容器何时创建

获取嵌入式Servlet容器工厂

1、启动Run方法

2、refreshContenxt()刷新IOC容器，并初始化容器创建容器中的每一个bean如果是web

3、刷新IOC容器

4、onRefresh();WebIoc容器重写了这个方法

5、创建嵌入式Servlet容器

6、获取嵌入式Servlet工厂

​	从IOC容器获取Servlet容器

​	后置处理器发现是这个对象，获取所有的定制器来先定制容器的相关配置

7、获取嵌入式容器

8、容器创建并启动





先启动Servlet容器，==然后再将剩下的对象获取出来==

### 9、使用外置Servlet容器

嵌入式容器：jar

​	有点：简单便携

​	缺点：默认不支持JSP，优化定制比较复杂，使用定制器



外置Servlet容器：war包打包

1. war方式创建文件
2. pom文件中tomcat 设置为provided
3. 辨析一个SpringBootServletInitializer的子类，并调用configure方法
4. 启动服务就好



原理：

jar: 执行mainf方法启动IOC容器，创建嵌入式容器

war:启动服务器，服务器启动springboot应用。启动IOC容器

==定义了规则==，

1）、服务启动，会创建当前web应用里面每一个jar里面的WebApplicationInitializer实例

2）、WebApplicationInitializer的实现放在jar包的META-INF/services文件夹下，有一个全名为javax.servlet.ServletContainerInitializer的文件，内容就是ServletContainerInitializer实现类的全名

3）、还可以使用@HandlesTypes，再启动应用的时候加载我们感兴趣的类



流程：

1）、启动tomcat服务器

2)、javax.servlet.ServletContainerInitializer

spring的web模块中有这个文件org.springframework.web.SpringServletContainerInitializer

3）、SpringServletContainerInitializer将 HandlesTypes标注的类传入onStartUp为这些类创建实例

4）、每一个实例都调用自己的Onstartup方法

5）、相当于SpringServletContainerInitializer被创建，并执行方法

6）、SpringServletContainerInitializer实例执行onstartup会 创建容器

7）、会调用configure，而我们重写了这个方法，将springboot 主程序类传入

8)、spring应用创建并启动



==先启动servlet容器，在启动springboot应用==



## 五、Docker

1. 简介
2. 核心概念
   1. docker主机
   2. docker客户端
   3. docker仓库
   4. docker镜像
   5. docker容器 镜像启动后的实例



## 六、springboot链接数据源

org.springframework.boot.autoconfigure.jdbc:

1、参考DataSourceCOnfiguratiion，根据配置创建数据源，默认使用tomcat连接池，也可以使用spring.datasourece.type指定自定义数据源类型

2、springboot默认支持

```java
org.apache.tomcat.jdbc.pool.DataSource、HikariDataSource、BasicDataSource
```

3、自定义数据源

```java
static class Generic {
    Generic() {
    }

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        //使用DataSourceBuilder创建数据源，利用反射创建数据源
        return properties.initializeDataSourceBuilder().build();
    }
}
```

4、DataSourceInitializer：ApplicationListener

​	作用：

​	1、初始化，this.runSchemaScripts();执行见表语句

​	2、运行插入数据的sql语句

默认只需要将文件命名为

```properties
schema-*.sql，data-*.sql 
默认规则
自定义： spring.datasource.schema:
```

5、操作数据库：自动配置JdbcTemplate查询数据



### 2、整合mysbatis

pom 文件 不同， mybatis-spring-boot-starter/官方自动写

MybatisAutoConfigutation配置了所有SqlSession

```java
@Mapper
public interface DeptMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into department(name) values(#{name})")
    public int insert(String name);
}

}
```

自定义mybatis的配置规则，只需要给容器中编写一个ConfigurationCustomize

```java
//开启驼峰命名法
@org.springframework.context.annotation.Configuration
public class MybatisConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return new ConfigurationCustomizer() {
            @Override
            public void customize(Configuration configuration) {
                configuration.setMapUnderscoreToCamelCase(true);
            }
        };
    }
}
```

自动扫描mapper 在主程序类上添加==MapperScan(value = "")==扫描所有mapper接口

```java
@ConfigurationProperties(prefix="spring.datasource.user")
    public DataSource primaryDataSource() {
        System.out.println("-------------------- userDataSource init ---------------------");
        return new DruidDataSource();
    }
```





### 3、整合SpringData

为我们提供了统一的API对数据访问层进行操作。

提供数据访问统一模板类

写@Entity @table @ID @Column

数据访问 继承JpaRespository<User, Integer>;

```yaml
spring:
	jpa:
		hibernate:
			ddl-auto: update
			show-sql: true
	
```



## 七、SpringBoot启动配置原理

ApplicationContextInitializer,SpringApplicationRunListener 需配置在META-INF/spring.factories

ApplicationRunner,CommandLinerRunner添加到容器中即可@Component 





1、创建SpringApplication对象

```java
initialize
private void initialize(Object[] sources) {
        if(sources != null && sources.length > 0) {
            this.sources.addAll(Arrays.asList(sources));
        }
		//判断当前是否为web应用
        this.webEnvironment = this.deduceWebEnvironment();
 		//从类路径下META-INF/spring.factories导入ApplicationContextInitializer的class     
      this.setInitializers(this.getSpringFactoriesInstances(ApplicationContextInitializer.class));
        //从类路径下META-INF/spring.factories导入ApplicationListener的class 
        this.setListeners(this.getSpringFactoriesInstances(ApplicationListener.class));
        //从多个配置类中找到main方法的主配置类
        this.mainApplicationClass = this.deduceMainApplicationClass();
    }
```

2、run方法

```java
 public ConfigurableApplicationContext run(String... args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = null;
        Object analyzers = null;
        this.configureHeadlessProperty();
     //获取SpringApplicationRunListeners从类路径下获取SpringApplicationRunListeners
        SpringApplicationRunListeners listeners = this.getRunListeners(args);
     	//回调所有starting方法
        listeners.starting();

        try {
            //封装命令
            DefaultApplicationArguments ex = new DefaultApplicationArguments(args);
            //准备环境
            ConfigurableEnvironment environment = this.prepareEnvironment(listeners, ex);
            Banner printedBanner = this.printBanner(environment);
            //创建IOC容器；决定是web的IOC还是普通IOC容器
            context = this.createApplicationContext();
            new FailureAnalyzers(context);
            //准备上下文环境
            //applyInitializers  回调ApplicationContextInitializer的initialize方法
            //回调SpringApplicationRunListener的contextPrepared方法
            this.prepareContext(context, environment, listeners, ex, printedBanner);
            //contextLoaded方法
            
            //刷新容器（如果是web应用会创建其纳入是的tomcat容器），初始化容器
            this.refreshContext(context);
            //从容器中获取所有的ApplicationRunner方法和CommandLineRunner进行回调
            this.afterRefresh(context, ex);
            //所有springapplicationlistener 调用finished方法
            listeners.finished(context, (Throwable)null);
            stopWatch.stop();
            if(this.logStartupInfo) {
                (new StartupInfoLogger(this.mainApplicationClass)).logStarted(this.getApplicationLog(), stopWatch);
            }

            return context;
        } catch (Throwable var9) {
            this.handleRunFailure(context, listeners, (FailureAnalyzers)analyzers, var9);
            throw new IllegalStateException(var9);
        }
    }
```

## 八、SpringBoot自定义starters

1、Starters

1. 场景需要的依赖
2. 如何编写自动配置

```java
@Configuration//指定这是一个配置类
@ConditionalOnXXX//指定条成立的情况下自动配置类生效
@AutoConfigureAfter//指定自动配置类的顺序
@Bean//给容器中添加组件
@ConfigurationProperties//结合自动配置类生效
@EnableConfigurationProperties//结合自动配置类生效

自动配置类要能生效
需要将自动配置类卸载META0-INF/spring.factories
```

## 九、Spring Boot缓存整合

#### 1、JSR-107

 	1. CacheingProvider
		2. CacheManager
		3. Cache
		4. Entry
		5. Expiry

#### 2、Spring缓存抽象

#### 3、环境搭建

 	1. 建好数据表
		2. 创建JavaBean封装数据
		3. 整合mybatis操作数据库
      	1. 配置数据源信息
      	2. 使用注解版的mybatis;
           	1. @Scanner指定需要扫描的mapper接口所在的包
     	3. 开启驼峰命名法
         	1. mybatis.configuration.map-underscore-to-camel-case=true

#### 4、体验缓存

1. 开启基于注解的缓存@EnableCache
2. 标注缓存注解即可
3. logging.level.mapper=debug;
4. cacheable 缓存
   1. cachenames 指定缓存组件的名字,可指定多个缓存
   2. key 指定缓存数据使用的key；默认事方法名，可支持编写spel表达式
   3. keygenerator：key生成器，可以使用自己指定的key生成器
   4. cacheManager：缓存管理器cacheRevosolver指定获取解析器
   5. condition：田间缓存
   6. unless：否定缓存
   7. sync：是否使用异步模式

| 名称          | 位置       | 描述                                                         | 示例                   |
| ------------- | ---------- | ------------------------------------------------------------ | ---------------------- |
| methodName    | root对象   | 当前被调用的方法名                                           | `#root.methodname`     |
| method        | root对象   | 当前被调用的方法                                             | `#root.method.name`    |
| target        | root对象   | 当前被调用的目标对象实例                                     | `#root.target`         |
| targetClass   | root对象   | 当前被调用的目标对象的类                                     | `#root.targetClass`    |
| args          | root对象   | 当前被调用的方法的参数列表                                   | `#root.args[0]`        |
| caches        | root对象   | 当前方法调用使用的缓存列表                                   | `#root.caches[0].name` |
| Argument Name | 执行上下文 | 当前被调用的方法的参数，如findArtisan(Artisan artisan),可以通过#artsian.id获得参数 | `#artsian.id`          |
| result        | 执行上下文 | 方法执行后的返回值（仅当方法执行后的判断有效，如 unless cacheEvict的beforeInvocation=false） | `#result`              |

#### 5、工作原理

1. CacheAutoConfiguratio自动配置类
2. CacheConfigurationImportSelector(ImportSelector)导入一些组件
   1. 导入缓存配置类
   2. 哪个配置类默认生效 SimpleCacheConfiguration
3. 给容器中注册了一个缓存管理器CacheManager：ConcorrentMapCacheManager
4. 可以获取和创建一个ConcurrentMapCache类型的缓存组件，他的作用将数据保存到ConcurrentMap中

#### 6、运行流程

1. 方法运行之前，先去查询cache（缓存组件），按照cacheName指定的名字获取

   （Cache Manager先获取响应的缓存），第一次获取缓存如果没有Cache组件会自动创建

2. 去Cache中查找缓存内容，使用一个key， 默认就是方法的参数

   key是按照某种策略生成的，默认是使用keyGenerator生成的，默认使用SimpleKeyGenerator生成key

   ​	生成策略，如果没有参数，key= new Simple()

   ​	如果一个参数  key = 参数值

   ​	多个参数 key = new SimpleKey(params)

3. 没有查询到缓存就调用目标方法

4. 将目标方法结果放到缓存中

#### 7、属性使用

1. keyGenerator 可以自己实现继承keyGenerator 的类，并加入到容器中，Name(""), 将Cachable（keyGeneraotr="name"）即可

2. CacheEvit beforInvocation  是否在方法执行之前清楚缓存，默认在方法之后

3. Caching定义复杂的缓存规则

   1. ```java 
      @Cacheing(
          cacheable = {
              @Cacheable(value="emp")
          },
          put={
              @CachePut(""),
              @CachePut("").
          }
      )
      ```

4. @CacheConfig（value="emp"）抽取缓存的公共配置



#### 8、整合redis

1. 引入redis的starters

2. redis 配置

   1. RedisTemplates k~v 都是对象
   2. StringRedisTemplates(简化操作字符串的)k~v 都是字符串

3. Redis基础

   1. 常见五大数据类型String字符串 list 列表set集合 hash哈希 zset有序集合

4. 测试保存对象

   1. 默认保存对象，使用jdk序列化机制，序列化后的数据保存到redis中

   2. 可以自己定义empRedisTemplate 

      ```java
      @Bean
      public RedisTemplate<Object, Employee> empRedisTemplate(){
          RedisTemplate<Object, Employee> template = new RedisTemplate<Object, Employee>();
          template.setConnectionFactory(redisConnectionFactory);
          Jackson2JsonRedisSerializer<Employee> ser = new Jackson2JsonRedisSerializer<Employee>();
          template.setDefaultSerializer(ser);
          return template;
      }
      ```

      

5、测试缓存

 	1. 引入redis的starter，容器保存的是RedisCacheManager
		2. RedisCacheManager 帮我们创建 RedisCache 来作为缓存组件，RedisCache操作reids
		3. 默认保存数据使用jdk序列化机制
		4. 默认序列化的东西 @Primary将某个缓存管理器作为默认的

## 十、Springboot与消息

1. 消息代理和目的地
2. 消息队列两种形式的目的地
   1. 队列：点对点消息通信，消息一旦被消费就会被删除，只能一个收到
   2. 主题：发布/订阅消息通信，多个都能同事收到
3. JMS：基于JVM消息代理的规范；ActiveMQ实现
4. AMQP：高级消息队列，也是一种消息代理规范，兼容JMS；RabbitMQ实现



Exchange类型，分发消息时根据类型的不同分发策略有区别，目前共四种类型：direct（点对点）、fanout（发送消息速度最快，给所有绑定的队列进行发送，广播模式）、topic（允许进行模糊匹配）、headers



>  docker run -d -p 15672:15672 -p 5672:5672 --name myrabbitmq df80af9ca0c9



#### 1、自动配置类

1. 自动配置链接工厂ConnectionFactory
2. RabbitmqProperties封装配置信息
3. 在容器中加一个RabbitTemplate：给rabbitmq发送和接受消息
4. 在容器中加一个AmqpAdmin：rabbitMq系统管理组件

#### 2、功能测试

1. rabbitTemplate.send
2. rabbitTemplate.convertAndSend(序列化使用MessageConverter)
3. rabbitTemplate.recive
4. rabbitTemplate.receiveAndConvert

#### 3、监听

@EnableRabbit//开启注解版监听模式，在主程序上

@RabbitListener(queues="")在处理方法上

#### 4、AmqpAdmin

创建和和删除Queue,Exchange,Binding

declare都是创建方法



## 十一、SpringBoot检索

##### 1.elasticsearch简介

 分布式搜索服务，提供RestFulAPI，底层shiyonglucence,采用多shard（分片）的方式保证数据安全，提供自动resharding的功能。

##### 2.docker run

docker run -e ES_JAVA_OPTS="-Xms256m -Xms256m" -d -p 9200:9200 -p 9300:9300 --name els els

##### 3.基本使用

https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html

##### 4.ES概念呢

1. 存储整个对象或者文档，利用json作为文档序列化格式
2. 存储数据动作叫做 索引
3. 一个集群可以包含多个索引，每个索引包含多个类型每个类型包含多个文档，每个文档包含多个属性

##### 5.基本操作

​	PUT新增更新数据，get获取数据，head检查文档是否存在，delete删除文档

##### 6.整合elasticsearch

1. 提供了两种技术和ES交互，

   1. jest默认不生效，需导入jest工具包
   2. Springdata ElasticSearch
      1. 配置了一些客户端Client，
      2. ElasticSearchTemplate操作es
      3. 编写一个ElasticSearchRepository接口操作

2. 简单使用JestClient

   1. 新增

      ```java
      new Index.Builder
      ```

   2. 搜索

      ```java
      String json = "";
      Search search = new Search.Builder(json).addIndex(index).addType(type);
      jestClient.excute(search);
      ```

3. SpringDataElasticSearch

   1. ES超时由于版本不合适
   2. 使用ElasticSearchRepository interface通过民命方法，就可直接使用不用实现方法

## 十二、SpringBoot任务

##### 1、使用异步

在方法上注解@Async

@EnableAsync



##### 2、 定时任务

@Scheduled(cron = "" )

,枚举；-区间；/步长；?日/星期冲突匹配；L最后；W工作日；#星期4#2第二个星期四



##### 3、邮件任务

simpleMessage

MimeMessage

## 十三、SpringBoot安全

shiro、springsecurity

认证和授权

1. springsecurity配置类
   1. @EnableWebSecurity extends WebSecurityConfigurerAdapter
2. 请求控制权限 
3. 重写configure(httpSecurity)定制请求的授权规则
4. 开启登陆功能http.formLogin();
5. 定义认证规则configure(AuthentictionManagerBuilder auth)
6. 注销功能http.logOut();访问/logout用户注销；注销成功返回登录页；可定制。logout().logoutsuccessUrl("/")
7. 记住我功能：开启记住我http.remeberme();登陆成功后将Cookie中写入浏览器，以后用浏览器携带cookie信息，直接获取信息。
8. 自定义的数据页面需要自定义界面loginPage 但是需要注意处理请求的url和处理方式，处理参数usernameParameter(),passwordParameter自定义请求后，自定义请求的post去验证用户
   1. 一旦定制loginPage的post处理登陆请求
9. 记住我能也需要指定参数值 remeberMeParameter指定请求

## 十四、springboot分布式

##### 1、Dubbo

1、dubbo(服务)

 	1. 有相关springboot-starters
 	2. zookeeper pom
 	3. dubbo配置扫描包注册中心地址
 	4. @Service dubbo注解

2、dubbo消费者

1. dubbo相关配置 
2. @Refference注入

##### 2、springclound

​	一个分布式的整体解决方案，为开发者提供了在分布式系统（配置管理，服务发现，熔断，路由，微代理，控制总线，一次性token，全局所，leader选举，分布式session，集群状态）中快速构建的工具，

​	五大常用组件 ：

 	1. 服务发现---netflix eureka
 	2. 客户端负载均衡---netflix ribbon
 	3. 断路器---netflix hystrix
 	4. 服务网关--Netflix zuul
 	5. 分布式配置---spring cloud config







## 十五、springboot开发热部署

## 十六、springboot监控管理

ops-actuator- spring-security进行权限控制

managerment-security.enabled=false;//安全不监控

端点访问

endpoints.beans.path=mybean  定制访问路径

management.contex-path=/manage 定制端点访问根路径

management.port =    端口



自定义健康状态指示器

healthIndicator