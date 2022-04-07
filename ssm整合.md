# 第一章 搭建整合环境

- 整合说明：SSM整合可以使用多种方式，选择**XML + 注解**的方式
- 整合思路
  1. 搭建整合的环境
  2. 将Spring的配置搭建完成
  3. 使用Spring整合SpringMVC框架
  4. 使用Spring整合MyBatis框架

## 准备工作

1. 创建数据库和表结构

   ```sql
   create database ssm;
   use ssm;
   create table account(
   id int primary key auto_increment,
   name varchar(20),
   money double
   );
   ```

2. 导入相关依赖

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
     <modelVersion>4.0.0</modelVersion>
   
     <groupId>com.zhouqiao</groupId>
     <artifactId>ssm</artifactId>
     <version>1.0-SNAPSHOT</version>
     <packaging>war</packaging>
   
     <name>ssm Maven Webapp</name>
     <!-- FIXME change it to the project's website -->
     <url>http://www.example.com</url>
   
     <properties>
       <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
       <maven.compiler.source>1.7</maven.compiler.source>
       <maven.compiler.target>1.7</maven.compiler.target>
   
       <spring.version>5.0.2.RELEASE</spring.version>
       <slf4j.version>1.6.6</slf4j.version>
       <log4j.version>1.2.12</log4j.version>
       <mysql.version>8.0.28</mysql.version>
       <mybatis.version>3.4.5</mybatis.version>
     </properties>
   
     <dependencies>
       <!--AOP相关-->
       <dependency>
         <groupId>org.aspectj</groupId>
         <artifactId>aspectjweaver</artifactId>
         <version>1.6.8</version>
       </dependency>
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-aop</artifactId>
         <version>${spring.version}</version>
       </dependency>
       <!--容器-->
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-context</artifactId>
         <version>${spring.version}</version>
       </dependency>
       <!--web-->
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-web</artifactId>
         <version>${spring.version}</version>
       </dependency>
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-webmvc</artifactId>
         <version>${spring.version}</version>
       </dependency>
       <!--测试-->
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-test</artifactId>
         <version>${spring.version}</version>
       </dependency>
       <!--事务-->
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-tx</artifactId>
         <version>${spring.version}</version>
       </dependency>
       <!--jdbc-->
       <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-jdbc</artifactId>
         <version>${spring.version}</version>
       </dependency>
       <!--junit-->
       <dependency>
         <groupId>junit</groupId>
         <artifactId>junit</artifactId>
         <version>4.11</version>
         <scope>test</scope>
       </dependency>
     <!--数据库-->
       <dependency>
         <groupId>mysql</groupId>
         <artifactId>mysql-connector-java</artifactId>
         <version>${mysql.version}</version>
       </dependency>
       <!--servlet-->
       <dependency>
         <groupId>javax.servlet</groupId>
         <artifactId>servlet-api</artifactId>
         <version>2.5</version>
         <scope>provided</scope>
       </dependency>
       <!--jsp-->
       <dependency>
         <groupId>javax.servlet.jsp</groupId>
         <artifactId>jsp-api</artifactId>
         <version>2.0</version>
         <scope>provided</scope>
       </dependency>
       <!--el表达式-->
       <dependency>
         <groupId>jstl</groupId>
         <artifactId>jstl</artifactId>
         <version>1.2</version>
       </dependency>
       <!-- log start -->
       <dependency>
         <groupId>log4j</groupId>
         <artifactId>log4j</artifactId>
         <version>${log4j.version}</version>
       </dependency>
       <dependency>
         <groupId>org.slf4j</groupId>
         <artifactId>slf4j-api</artifactId>
         <version>${slf4j.version}</version>
       </dependency>
       <dependency>
         <groupId>org.slf4j</groupId>
         <artifactId>slf4j-log4j12</artifactId>
         <version>${slf4j.version}</version>
       </dependency>
       <!-- log end -->
       <dependency>
         <groupId>org.mybatis</groupId>
         <artifactId>mybatis</artifactId>
         <version>${mybatis.version}</version>
       </dependency>
       <dependency>
         <groupId>org.mybatis</groupId>
         <artifactId>mybatis-spring</artifactId>
         <version>1.3.0</version>
       </dependency>
       <dependency>
         <groupId>c3p0</groupId>
         <artifactId>c3p0</artifactId>
         <version>0.9.1.2</version>
         <type>jar</type>
         <scope>compile</scope>
       </dependency>
     </dependencies>
   
     <build>
       <finalName>ssm</finalName>
       <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
         <plugins>
           <plugin>
             <artifactId>maven-clean-plugin</artifactId>
             <version>3.1.0</version>
           </plugin>
           <!-- see http://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_war_packaging -->
           <plugin>
             <artifactId>maven-resources-plugin</artifactId>
             <version>3.0.2</version>
           </plugin>
           <plugin>
             <artifactId>maven-compiler-plugin</artifactId>
             <version>3.8.0</version>
           </plugin>
           <plugin>
             <artifactId>maven-surefire-plugin</artifactId>
             <version>2.22.1</version>
           </plugin>
           <plugin>
             <artifactId>maven-war-plugin</artifactId>
             <version>3.2.2</version>
           </plugin>
           <plugin>
             <artifactId>maven-install-plugin</artifactId>
             <version>2.5.2</version>
           </plugin>
           <plugin>
             <artifactId>maven-deploy-plugin</artifactId>
             <version>2.8.2</version>
           </plugin>
         </plugins>
       </pluginManagement>
     </build>
   </project>
   ```

3. 编写`JavaBean`

   ```java
   package com.zhouqiao.domain;
   
   import java.io.Serializable;
   
   public class Account implements Serializable {
       private static final long serialVersionUID = 7355810572012650248L;
       private Integer id;
       private String name;
       private Double money;
   
       public Integer getId() {
           return id;
       }
   
       public void setId(Integer id) {
           this.id = id;
       }
   
       public String getName() {
           return name;
       }
   
       public void setName(String name) {
           this.name = name;
       }
   
       public Double getMoney() {
           return money;
       }
   
       public void setMoney(Double money) {
           this.money = money;
       }
   }
   ```

4. 编写`dao`接口：MyBatis会为接口生成代理对象，不需要写实现类

   ```java
   package com.zhouqiao.dao;
   
   import com.zhouqiao.domain.Account;
   
   import java.util.List;
   
   public interface AccountDao {
       public void saveAccount(Account account);
       public List<Account> findAll();
   }
   ```

5. 编写`service`接口和实现类

   ```java
   package com.zhouqiao.service;
   
   import com.zhouqiao.domain.Account;
   
   import java.util.List;
   
   public interface AccountService {
       public void saveAccount(Account account);
       public List<Account> findAll();
   }
   
   
   
   
   package com.zhouqiao.service.impl;
   
   import com.zhouqiao.dao.AccountDao;
   import com.zhouqiao.domain.Account;
   import com.zhouqiao.service.AccountService;
   
   import java.util.List;
   
   public class AccountServiceImpl implements AccountService {
   
       private AccountDao accountDao;
   
       @Override
       public void saveAccount(Account account) {
           System.out.println("业务层：保存账户...");
       }
   
       @Override
       public List<Account> findAll() {
           System.out.println("业务层：查询所有账户...");
           return null;
       }
   }
   ```

## 搭建与测试Spring开发环境

1. 在`resources`下创建`applicationContext.xml`配置文件，编写具体的配置信息

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:aop="http://www.springframework.org/schema/aop"
          xmlns:tx="http://www.springframework.org/schema/tx"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/aop
           http://www.springframework.org/schema/aop/spring-aop.xsd
           http://www.springframework.org/schema/tx
           http://www.springframework.org/schema/tx/spring-tx.xsd">
   
       <!--开启注解扫描，处理service和dao，但是不让Spring处理controller-->
       <context:component-scan base-package="com.zhouqiao">
           <!--配置不扫描的注解-->
           <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
       </context:component-scan>
   
   
   </beans>
   ```

2. 在`src`下创建`test.java.test`，将java设置为测试包，编写测试类进行测试

   ```java
   package test;
   
   import com.zhouqiao.service.AccountService;
   import org.junit.Test;
   import org.springframework.context.ApplicationContext;
   import org.springframework.context.support.ClassPathXmlApplicationContext;
   
   public class testSpring {
       @Test
       public void test1(){
           // 加载配置文件
           ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
           // 获取对象
           AccountService as = (AccountService) ac.getBean("accountService");
           // 调用方法
           as.findAll();
       }
   }
   ```

## 搭建与测试SpringMVC开发

1. 在`web.xml`中配置`DispatcherServlet`前端控制器以及过滤器解决中文乱码，过滤器需要在控制器之前配置

   ```xml
   <!DOCTYPE web-app PUBLIC
    "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
    "http://java.sun.com/dtd/web-app_2_3.dtd" >
   
   <web-app>
     <display-name>Archetype Created Web Application</display-name>
   
     <!--解决中文乱码过滤器-->
     <filter>
       <filter-name>characterEncodingFilter</filter-name>
       <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
       <init-param>
         <param-name>encoding</param-name>
         <param-value>UTF-8</param-value>
       </init-param>
     </filter>
   
     <filter-mapping>
       <filter-name>characterEncodingFilter</filter-name>
       <url-pattern>/*</url-pattern>
     </filter-mapping>
   
     <!--配置前端控制器-->
     <servlet>
       <servlet-name>dispatcherServlet</servlet-name>
       <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
       <!--加载springmvc.xml-->
       <init-param>
         <param-name>contextConfigLocation</param-name>
         <param-value>classpath:springmvc.xml</param-value>
       </init-param>
       <!--启动服务器就创建该servlet-->
       <load-on-startup>1</load-on-startup>
     </servlet>
   
     <servlet-mapping>
       <servlet-name>dispatcherServlet</servlet-name>
       <url-pattern>/</url-pattern>
     </servlet-mapping>
     
   </web-app>
   ```

2. 创建`springmvc.xml`的配置文件，编写配置文件，扫描包时要设置默认过滤器为false

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="
   http://www.springframework.org/schema/beans
   http://www.springframework.org/schema/beans/spring-beans.xsd
   http://www.springframework.org/schema/mvc
   http://www.springframework.org/schema/mvc/spring-mvc.xsd
   http://www.springframework.org/schema/context
   http://www.springframework.org/schema/context/spring-context.xsd">
   
       <!-- 扫描controller的注解，别的不扫描 -->
       <context:component-scan base-package="com.zhouqiao" use-default-filters="false">
           <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
       </context:component-scan>
   
       <!--配置视图解析器-->
       <bean id="internalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
           <!-- JSP文件所在的目录 -->
           <property name="prefix" value="/WEB-INF/pages/" />
           <!-- 文件的后缀名 -->
           <property name="suffix" value=".jsp" />
       </bean>
   
       <!-- 设置静态资源不过滤 -->
       <mvc:resources location="/css/" mapping="/css/**" />
       <mvc:resources location="/images/" mapping="/images/**" />
       <mvc:resources location="/js/" mapping="/js/**" />
   
       <!-- 开启对SpringMVC注解的支持 -->
       <mvc:annotation-driven />
       
   </beans>
   ```

3. 测试SpringMVC框架是否搭建成功

   1. 编写`index.jsp`和`list.jsp`

      ```jsp
      <a href="account/findAll">测试表现层功能</a>
      ```

   2. 修改`controller`类

      ```java
      @Controller
      @RequestMapping("/account")
      public class AccountController {
      
          @RequestMapping("/findAll")
          public String findAll(){
              System.out.println("表现层：查询所有账户...");
              return "list";
          }
      }
      ```

   3. 配置服务器，选择`war exploded`，按文件夹结构部署，常用于热部署

## Spring整合SpringMVC

- 目的：在`controller`中能成功调用`service`对象中的方法

- 思路：启动Tomcat服务器时，需要加载Spring的配置文件-->`ServletContext`域对象与服务器生命周期相同-->有一类监听器负责监听`ServletContext`的创建与销毁-->让该监听器监听Spring配置文件

  1. 在`web.xml`中配置`ContextLoaderListener`监听器（该监听器默认只能加载`WEB-INF`目录下的`applicationContext.xml`的配置文
     件）

     ```xml
       <!-- 配置Spring的监听器 -->
       <listener>
         <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
       </listener>
       <!-- 配置加载类路径的配置文件 -->
       <context-param>
         <param-name>contextConfigLocation</param-name>
         <param-value>classpath:ApplicationContext.xml</param-value>
       </context-param>
     ```

  2. 在`controller`中注入`service`，调用`service`的方法，调用成功则说明整合成功

     ```java
     @Controller
     @RequestMapping("/account")
     public class AccountController {
     
         @Autowired
         private AccountService accountService;
     
         @RequestMapping("/findAll")
         public String findAll(){
             System.out.println("表现层：查询所有账户...");
             // 调用service的方法
             accountService.findAll();
             return "list";
         }
     }
     ```

## 搭建与测试MyBatis开发环境

1. `resources`中编写`SqlMapConfig.xml`

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">
   
   <configuration>
       <environments default="mysql">
           <environment id="mysql">
               <transactionManager type="JDBC"/>
               <dataSource type="POOLED">
                   <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                   <property name="url" value="jdbc:mysql:///ssm?serverTimezone=UTC&amp;useSSL=false"/>
                   <property name="username" value="root"/>
                   <property name="password" value="root"/>
               </dataSource>
           </environment>
       </environments>
       <!-- 使用的是注解 -->
       <mappers>
           <!-- <mapper class="cn.itcast.dao.AccountDao"/> -->
           <!-- 该包下所有的dao接口都可以使用 -->
           <package name="com.zhouqiao.dao"/>
       </mappers>
   </configuration>
   ```

2. 在`dao`接口上使用注解添加sql语句

   ```java
   package com.zhouqiao.dao;
   
   import com.zhouqiao.domain.Account;
   import org.apache.ibatis.annotations.Insert;
   import org.apache.ibatis.annotations.Select;
   
   import java.util.List;
   
   public interface AccountDao {
   
       @Insert("insert into account (name, money) values (#{name}, #{money} )")
       public void saveAccount(Account account);
       
       @Select("select * from account ")
       public List<Account> findAll();
   }
   ```

3. 编写测试方法

   ```java
       @Test
       public void test2() throws Exception{
           // 加载配置文件
           InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
           // 创建工厂
           SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
           // 创建sqlSession对象
           SqlSession session = factory.openSession();
           // 获取代理对象
           AccountDao dao = session.getMapper(AccountDao.class);
           // 调用查询的方法
           List<Account> list = dao.findAll();
           for (Account account : list) {
               System.out.println(account);
           }
           // 释放资源
           session.close();
           inputStream.close();
       }
   
   
   
   
       @Test
       public void test3() throws Exception{
           Account account = new Account();
           account.setName("熊大");
           account.setMoney(400d);
   
           // 加载配置文件
           InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
           // 创建工厂
           SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
           // 创建sqlSession对象
           SqlSession session = factory.openSession();
           // 获取代理对象
           AccountDao dao = session.getMapper(AccountDao.class);
           // 调用查询的方法
           dao.saveAccount(account);
           List<Account> list = dao.findAll();
           for (Account a : list) {
               System.out.println(a);
           }
           // 释放资源
           session.close();
           inputStream.close();
       }
   ```

## Spring整合MyBatis

1. 将`SqlMapConfig.xml`内容转移到`ApplicationContext.xml`中，然后可以将其删除

   ```xml
       <!-- 配置C3P0的连接池对象 -->
       <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
           <property name="driverClassName" value="com.mysql.cj.jdbc.Driver" />
           <property name="url" value="jdbc:mysql:///ssm?serverTimezone=UTC&amp;useSSL=false" />
           <property name="username" value="root" />
           <property name="password" value="root" />
       </bean>
   
       <!-- 配置SqlSession的工厂 -->
       <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
           <property name="dataSource" ref="dataSource" />
       </bean>
   
       <!-- 配置扫描dao的包 -->
       <bean id="mapperScanner" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
           <property name="basePackage" value="com.zhouqiao.dao"/>
       </bean>
   ```

2. 在`AccountDao`接口中添加`@Repository`注解

3. 在`service`中注入`dao`对象，进行测试，运行逻辑

   1. 点击超链接会调用`controller`，`controller`调用`service`的方法并跳转页面，并将结果封装到`model`中
   2. `service`的方法中调用`dao`的方法
   3. `list.jsp`从`model`中取出数据

4. 代码

   ```java
   @Controller
   @RequestMapping("/account")
   public class AccountController {
   
       @Autowired
       private AccountService accountService;
   
       @RequestMapping("/findAll")
       public String findAll(Model model){
           System.out.println("表现层：查询所有账户...");
           // 调用service的方法
           List<Account> list = accountService.findAll();
           model.addAttribute("list", list);
           return "list";
       }
   }
   
   
   
   @Service("accountService")
   public class AccountServiceImpl implements AccountService {
   
       @Autowired
       private AccountDao accountDao;
   
       @Override
       public void saveAccount(Account account) {
           System.out.println("业务层：保存账户...");
           accountDao.saveAccount(account);
       }
   
       @Override
       public List<Account> findAll() {
           System.out.println("业务层：查询所有账户...");
           return accountDao.findAll();
       }
   }
   
   
   @Repository
   public interface AccountDao {
   
       @Insert("insert into account (name, money) values (#{name}, #{money} )")
       public void saveAccount(Account account);
   
       @Select("select * from account ")
       public List<Account> findAll();
   }
   ```

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
   <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
   <html>
   <head>
       <title>Title</title>
   </head>
   <body>
       <h3>查询了所有账户信息</h3>
       ${list}
   
       <c:forEach items="${list}" var="account">
           ${account.name}
           ${account.money}
       </c:forEach>
   </body>
   </html>
   ```

5. 配置Spring声明式事务管理

   ```xml
       <!--配置Spring声明式事务管理-->
       <!--配置事务管理器-->
       <bean id="dataSourceTransactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
           <property name="dataSource" ref="dataSource"></property>
       </bean>
       <!--配置事务通知-->
       <tx:advice id="txAdvice" transaction-manager="dataSourceTransactionManager">
           <tx:attributes>
               <tx:method name="find*" read-only="true"/>
               <tx:method name="*"></tx:method>
           </tx:attributes>
       </tx:advice>
   
       <!--配置AOP增强-->
       <aop:config>
           <aop:advisor advice-ref="txAdvice" pointcut="(* com.zhouqiao.service.impl.*ServiceImpl.*(..))"></aop:advisor>
       </aop:config>
   ```

6. 测试保存方法：`controller`中编写`save`方法，`index.jsp`中表单提交执行该方法

   ```
       @RequestMapping("/save")
       public void save(Account account, HttpServletRequest request, HttpServletResponse response) throws IOException {
           System.out.println("表现层：保存账户...");
           // 调用service的方法
           accountService.saveAccount(account);
           response.sendRedirect(request.getContextPath()+"account/findAll");
   //        return "forward:findAll";
           return;
       }
   ```

   