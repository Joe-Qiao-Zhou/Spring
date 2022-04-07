# 1.框架概述

- 概念：分层的JavaSE/EE全栈轻量级开源框架，能整合其他开源框架与类库，是使用最多的JavaEE企业应用开源框架

- 两大核心：反转控制与面向切面编程

- 优势

  - 方便解耦，简化开发：通过IoC容器将对象间依赖关系交给Spring控制，不需要为底层编写代码
  - AOP编程的支持
  - 声明式事务的支持：通过配置方式
  - 方便程序测试
  - 方便继承各种框架
  - 降低JavaEEAPI使用难度

- 体系结构

  ![image-20220319150646915](C:\Users\91494\AppData\Roaming\Typora\typora-user-images\image-20220319150646915.png)

- Core Container核心容器：Spring的IoC部分

# 2.程序耦合与解耦

- 程序间的依赖问题，包括类之间的依赖和方法之间的依赖：业务层的service调用持久层，需要持久层的接口与实现类，如果没有编写则编译无法通过
- 实际开发中应做到编译期不依赖，运行时才依赖
- 解耦思路
  1. 使用反射创建对象，避免使用`new`关键字
     - 正常方式：引入包名-->new一个对象
     - 反射方式：实例化对象-->调用`getClass()`方法-->获得包名，可以在执行期获取任何类的内部信息

  2. 在反射基础上，通过读取配置文件得到要创建的对象全限定类名
- 工厂模式解耦
  - 一个创建Bean对象的工厂
    - Bean在计算机英语中有可重用组件的含义，JavaBean>实体类
    - 符合一定规范编写的Java类就是Java Bean
      1. 所有属性为private
      2. 提供默认构造方法
      3. 提供getter和setter
      4. 实现Serializable接口
  - 工厂用于从容器中获取或创建三层对象
    1. 通过读取配置文件，反射创建三层对象
    3. 使用一个Map将对象先创建再**保存**，等到用的时候不用创建直接拿来使用，采用单例模式，这个Map就是Spring的**容器**


# 3.IoC

- 概念：Inverse of Control，反转控制，将创建对象的权利/对对象的控制权**交给工厂**/框架
- 是一种设计思想，而不是技术
- 包括依赖注入和依赖查找
- 目的：削减程序的耦合
- 以往是直接通过new创建对象，是程序主动去创建依赖对象；而IoC是有专门的一个容器来创建这些对象，即由IoC容器来控制对象的创建
- 谁控制谁：IoC容器控制对象
- 控制什么：控制了外部资源获取(除了对象还有文件等)
- 为何是反转：因为是由容器进行查找和注入依赖对象的工作，而对象只是被动地接收依赖对象
- 反转了什么：依赖对象的获取方式被反转了

# 4.Spring中基于XML的IoC配置

## 4.1 配置过程

1. `pom.xml`中导入坐标，并且设置打包方式为jar

   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-context</artifactId>
           <version>5.0.2.RELEASE</version>
       </dependency>
   </dependencies>
   ```

2. `beans.xml`导入约束

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd">
  </beans>
  ```

3. 将创建对象的工作交给Spring，并存入IoC容器中

   ```xml
   <!-- 唯一标识与全限定类名 -->
   <bean id="accountService" class="service.impl.AccountServiceImpl"></bean>
   <bean id="accountDao" class="dao.impl.AccountDaoImpl"></bean>
   ```

4. 获取IoC容器，并通过容器获取对象

  ```java
  // 1.获取核心容器对象，二选一
  ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
  ApplicationContext ac = new FileSystemXmlApplicationContext("D:\\javacode\\spring\\src\\main\\resources\\bean.xml");
  // 2.根据id获取Bean对象
  AccountService as = (AccountService)ac.getBean("accountService");
  AccountDao adao = ac.getBean("accountDao", AccountDao.class);
  ```

## 4.2 核心容器

- `ApplicationContext`就是**Spring中的IoC容器**，它有三个常用实现类
  1. `ClassPathXmlApplicationContext`：可以加载类路径下的配置文件，实际开发中**较常用**
  2. `FileSystemXmlApplicationContext`：可以加载磁盘任意路径下的配置文件，但必须有访问权限
  3. `AnnotationConfigApplicationContext`：用于读取注解创建容器
- 核心容器的两个接口

  1. `ApplicationContext`：在构建核心容器时，采取**立即加载**策略创建对象，即只要一读取配置文件就立马创建对象，创建**单例**对象，实际开发常用
  1. `BeanFactory`：Spring容器的**顶层接口**；在构建核心容器时，采取**延迟加载**策略创建对象，即什么时候需要使用对象时才创建对象，创建多例对象

## 4.3 Spring对Bean的管理细节

- `<bean>`标签：用于配置Bean对象，默认情况下调用无参构造方法

- 创建Bean对象的三种方式

  1. 使用默认构造方法创建：在Spring配置文件中使用`<bean>`标签，只有id和class属性时，采用默认构造方法创建Bean对象，如果没有默认构造方法则无法创建

     ```xml
     <bean id="accountService" class="service.impl.AccountServiceImpl"></bean>
     ```
  
  2. Spring管理实例工厂：必须先有工厂实例对象，使用其方法创建对象并存入容器
  
     ```xml
     <bean id="instanceFactory" class="factory.InstanceFactory"></bean>
     <bean id="accountService" factory-bean="instanceFactory" factory-method="getAccountService"></bean>
     ```
  
  3. Spring管理静态工厂：使用静态工厂的静态方法创建对象并存入容器
  
  ```xml
  <bean id="accountService" class="factory.StaticFactory" factory-method="getAccountService"></bean>
  ```


- `id`属性：给对象在容器中提供一个唯一标识，用于获取对象 

- `class`属性：指定类的全限定类名，用于反射创建对象，默认情况下调用无参构造函数

- `scope`属性：指定Bean对象的作用范围

  - `singleton`：单例的(默认值)
  - `prototype`：多例的
  - `request`：作用于web应用的请求范围
  - `session`：作用于web应用的会话范围
  - `global-session`：作用于集群环境的会话范围（全局会话范围），当不是集群环境时就等于session，常用于负载均衡

  ```xml
  <bean id="accountService" class="service.impl.AccountServiceImpl" scope="prototype"></bean>
  ```

- 对象的生命周期

  - 单例对象：容器创建时出生，只要容器存在就存在，容器销毁时死亡
  - 多例对象：使用对象时出生，对象只要还在使用就存在，当对象长时间不使用且未被引用时，由Java的垃圾回收机制回收，不归Spring管


# 5.依赖注入

- Dependency Injection, DI，是IoC的**具体实现**，同一概念的不同角度描述，**被注入对象依赖IoC容器配置依赖对象**
- 依赖关系的维护：组件之间的依赖关系由容器在运行期决定，由容器动态地将某个依赖关系注入到组件中
- 谁依赖于谁：应用程序依赖于IoC容器
- 为什么依赖：需要容器提供组件所需要的外部资源
- 谁注入谁：IoC容器注入某个对象
- 注入了什么：所需要的外部资源(对象、资源、常量等)
- 能注入的数据有三类
  - 基本类型与String
  - 其他Bean类型：在配置文件或注解中配置过的Bean
  - 复杂类型/集合类型

## 5.1 注入方式

1. 使用构造函数注入：使用`<bean>`标签内部的`<constructor-arg>`标签

   - 标签中的属性

     - `type`属性：用于指定注入的数据类型，同时也是构造函数中某个/某些参数的类型，并**不能独立注入**，只能给同一种数据类型一起注入
     - `index`属性：用于给指定索引位置的参数赋值，从0开始
     - `name`属性：用于给指定名称的参数赋值
     - `value`属性：用于给基本类型和String传值
     - `ref`属性：传其他Bean类型的引用，必须是核心容器中的Bean对象

   - 特点：必须注入数据才能创建Bean对象，保证了在注入时某些需要的数据不会缺失

   - 弊端：改变了Bean对象的实例化方式，即使某些数据用不到也必须提供

     ```xml
     <!--构造函数中必须传3个成员变量值-->
     <bean id="accountService" class="service.impl.AccountServiceImpl">
         <constructor-arg name="name" value="test"></constructor-arg>
         <constructor-arg name="age" value="18"></constructor-arg>
         <constructor-arg name="birthday" ref="now"></constructor-arg>
     </bean>
     
     <bean id="now" class="java.util.Date"></bean>
     ```

2. 使用set方法注入：使用`<bean>`标签内的`<property>`标签，优劣与构造注入相反，实际开发中**较常用**

   ```xml
   <bean id="accountService2" class="service.impl.AccountServiceImpl2">
       <property name="name" value="TEST"></property>
       <property name="age" value="19"></property>
       <property name="birthday" ref="now"></property>
   </bean>
   ```

3. 复杂类型/集合类型注入

   1. 给List结构集合注入：`<list> <set> <array>`

   2. 给Map结构集合注入：`<map> <props>`

      ```xml
      <bean id="accountService3" class="service.impl.AccountServiceImpl3">
       <!--String[] myStrs-->
      <property name="myStrs">
          <array>
              <value>AAA</value>
              <value>BBB</value>
              <value>CCC</value>
          </array>
      </property>
      
      <!--List<String> myList-->
      <property name="myList">
          <list>
              <value>AAA</value>
              <value>BBB</value>
              <value>CCC</value>
          </list>
      </property>
      
      <!--Set<String> mySet-->
      <property name="mySet">
          <set>
              <value>AAA</value>
              <value>BBB</value>
              <value>CCC</value>
          </set>
      </property>
      
      <!--Map<String, String> myMap-->
      <property name="myMap">
          <map>
              <entry key="testA" value="AAA"></entry>
              <entry key="testB">
                  <value>BBB</value>
              </entry>
          </map>
      </property>
      
      <!--Properties myProps-->
      <property name="myProps">
          <props>
              <prop key="testC">CCC</prop>
              <prop key="testD">DDD</prop>
          </props>
      </property>
      
      </bean>
      ```

# 6.基于注解的IOC

- 导入约束

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context.xsd">
      
          <!--告知spring在创建容器时要扫描的包，但标签不在beans中，而是在context中-->
      <context:component-scan base-package="service"></context:component-scan>
      <context:component-scan base-package="dao"></context:component-scan>
  </beans>
  ```

## 6.1 常用注解

- 用于创建对象，同`<bean>`标签
  - `@Componment`：用于实现类
    - 将当前类对象存入容器中，`value`属性用于指定Bean对象的id，默认是当前类名且首字母小写
    - `@Controller @Service @Repository`：与`@Componment`完全相同，只是为了三层架构而设计
- 用于注入数据，同`<property>`标签
  - `@Autowired`：用于变量或方法
    - set方法**不是必须**的：`@Autowired`标记会去找`@Component`标记，只要匹配就注入成功；如果有多个匹配，则先看类型再看变量名，如果变量名仍不一样就注入失败
  - `@Qualifier`：用于变量或方法，搭配`@Autowired`使用
    - 在类名基础上按照名称注入：当一个接口的多个实现类都在容器中时Spring无法自动注入，必须手动使用该注解指定名称
  - `@Resource`：直接按照Bean对象的id注入
  - 以上三个注入都**只能**注入Bean类型数据，基本类型和String无法使用
  - 集合类型的注入**只能**通过XML实现
  - `@Value`：用于注入基本类型和String
    - `value`属性：用于指定数据的值，可以使用Spring中的SPEL：`${表达式}`
- 用于改变作用范围，同`<scope>`标签
  - `@Scope`：用于实现类，用于指定Bean对象的作用范围
    - `value`属性：指定范围，常用singleton和prototype
- 和生命周期有关，同`<init_method>`和`<destroy_method>`
  - `@PreDestroy` 和`@PostConstruct`

# 7.IOC案例

- 对于自己写的类，使用注解更方便；如果使用别人的jar包，就用xml方法配置

## 7.1 基于XML实现单表CRUD

1. 导入依赖

   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-context</artifactId>
           <version>5.0.2.RELEASE</version>
       </dependency>
   
       <dependency>
           <groupId>commons-dbutils</groupId>
           <artifactId>commons-dbutils</artifactId>
           <version>1.4</version>
       </dependency>
   
       <dependency>
           <groupId>mysql</groupId>
           <artifactId>mysql-connector-java</artifactId>
           <version>8.0.11</version>
       </dependency>
   
       <dependency>
           <groupId>c3p0</groupId>
           <artifactId>c3p0</artifactId>
           <version>0.9.1.2</version>
       </dependency>
   
       <dependency>
           <groupId>junit</groupId>
           <artifactId>junit</artifactId>
           <version>4.10</version>
           <scope>test</scope>
       </dependency>
   
       <dependency>
           <groupId>javax.annotation</groupId>
           <artifactId>javax.annotation-api</artifactId>
           <version>1.3.2</version>
       </dependency>
   </dependencies>
   ```
   
2. 编写domain类、service类和dao类

3. 配置`bean.xml`

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <!--配置Service-->
       <bean id="accountService" class="service.impl.AccountServiceImpl">
           <!--注入Dao-->
           <property name="accountDao" ref="accountDao"></property>
       </bean>
   
       <!--配置Dao对象-->
       <bean id="accountDao" class="dao.impl.AccountDaoImpl">
           <!--注入QueryRunner-->
           <property name="runner" ref="runner"></property>
       </bean>
   
       <!--配置QueryRunner对象-->
       <bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
           <!--注入数据源-->
           <constructor-arg name="ds" ref="dataSource"></constructor-arg>
       </bean>
   
       <!--配置数据源-->
       <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
           <!--连接数据库的必备信息-->
           <property name="driverClass" value="com.mysql.cj.jdbc.Driver"></property>
           <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/spring?serverTimezone=UTC"></property>
           <property name="user" value="root"></property>
           <property name="password" value="root"></property>
       </bean>
   </beans>
   ```

4. 编写测试类

   ```java
   // 获取容器
   ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
   // 获取业务层对象
   AccountService as = ac.getBean("accountService", AccountService.class);
   // 执行方法
   ```

## 7.2 基于注解实现单表CRUD

1. 修改`bean.xml`依赖

2. service类和dao类分别加上`@Service`和`@Repository`标签

3. 类中变量加上`@Autowired`标签，且不需要set方法

4. 告知Spring要扫描的包

   ```xml
   <!--告知spring在创建容器时要扫描的包-->
   <context:component-scan base-package="dao"></context:component-scan>
   <context:component-scan base-package="service"></context:component-scan>
   ```

5. 删除service和dao的配置

## 7.3 两者对比

|                    | 基于XML配置                                | 基于注解配置                                                 |
| ------------------ | ------------------------------------------ | ------------------------------------------------------------ |
| Bean定义           | \<bean id="..." class="..."/>              | @Component("id")/@Controller("id")/Service("id")/@Repository("id") |
| Bean注入           | \<property name="..."/>                    | @Autowired @Qualifier                                        |
| 生命周期、作用范围 | \<init-method> \<destroy-method> scope属性 | @PostConstruct @PreDestroy @Scope                            |
| 适合场景           | 第三方库                                   | 自编写类                                                     |

## 7.4 基于纯注解实现单表CRUD

- 思路：创建一个配置类，使其与`bean.xml`作用相同，解决扫描包和配置数据源的问题

1. `@Configuration`：指定当前类是一个配置类

   - 当配置类作为`AnnotationConfigApplicationContext`创建参数时，该注解可省略

2. `@ComponentScan`：指定Spring在创建容器时要扫描的包

3. `@Bean`：只能写在**方法**上，将当前方法返回值作为Bean对象存入Spring容器中； 如果方法有参数，spring会去容器中查找有无可用的Bean对象，同`@Autowired`；name属性用于指定Bean的id，默认是方法名

4. `@PropertySource("classpath:文件名")`：指定properties文件

5. `@Import`：用于在主配置类中导入其他子配置类，传入配置类的**字节码**

6. `@Value("${jdbc.driver}")`：在成员变量上写，用于从配置文件中获取对应内容

7. 创建容器时传入配置类的字节码

   ```java
   ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
   ```

8. 为了使用多例，需要给QueryRunner加入`@Scope`注解

```java
@Configuration
//@ComponentScans(value={@ComponentScan("com.itheima.dao"), @ComponentScan("com.itheima.service")})
@ComponentScan("com.itheima")
public class SpringConfiguration {
    
    @Bean(name = "runner")
    public QueryRunner createQueryRunner(DataSource ds){
        return new QueryRunner(ds);
    }

    @Bean(name = "dataSource")
    public DataSource createDataSource(){
        ComboPooledDataSource ds = new ComboPooledDataSource();
        try {
            ds.setDriverClass("com.mysql.cj.jdbc.Driver");
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/spring?serverTimezone=UTC");
            ds.setUser("root");
            ds.setPassword("root");
            return ds;
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

## 7.5 Spring和Junit整合

- 不关注创建容器，只关注方法是否正确，即使使用`@Before`注解也无法消除这种依赖

- main方法是程序入口，而Junit集成了一个main方法，判断哪些方法有`@Test`注解并让其执行，其并不关注是否使用框架，也就不会创建核心容器，因此没有核心容器，即使写了@`Autowired`注解也无法注入

- Spring整合Junit的配置

  1. 导入Spring整合Junit的坐标

     ```xml
     <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-test</artifactId>
         <version>5.0.2.RELEASE</version>
     </dependency>
     ```

  2. 使用Spring提供`@RunWith(SpringJUnit4ClassRunner.class)`注解代替测试类的main方法

  3. 告知Spring的运行器，IoC的创建方式和对应位置：`@ContextConfiguration(locations = "classpath:bean.xml"/classes = XXX.class)`
  
  4. 使用`@Autowired`给测试类中的变量注入数据

  ```java
  @RunWith(SpringJUnit4ClassRunner.class) 
  @ContextConfiguration(locations= {"classpath:bean.xml"}) 
  public class AccountServiceTest { 
      @Autowired 
      private IAccountService as ; 
  }
  ```

# 8.Spring中的AOP

- 概念：Aspect Oriented Programming，面向切面编程，通过**预编译**方式和**运行期动态代理**实现程序功能：**抽取重复代码**，在需要执行的时候使用动态代理，不修改源码对已有方法进行增强；类似于抽取出一个公共方法，但是不需要调用

<img src="https://img-blog.csdn.net/20180530172331597?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3E5ODIxNTE3NTY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70" alt="这里写图片描述" style="zoom: 25%;" /><img src="https://img-blog.csdn.net/20180530172427993?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3E5ODIxNTE3NTY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70" alt="这里写图片描述" style="zoom: 25%;" /><img src="https://img-blog.csdn.net/20180530172528617?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3E5ODIxNTE3NTY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70" alt="这里写图片描述" style="zoom:25%;" />

- Spring中通过配置的方式实现AOP
- 一些名词
  - 连接点：业务中所有被拦截的方法
  - 切入点：真正能被增强的那些方法，是连接点的子集
  - 通知/增强：拦截到切入点后所做的事情；整个`invoke()`方法就是环绕通知，`method.invoke()`之前执行的是前置通知，之后的是后置通知，catch代码块中的是异常通知，finally代码块中的是最终通知
  - 织入：加入增强的过程
  - 切面：切入点与通知的结合
  - 能够有选择性地、低耦合地把切面和核心业务功能结合在一起的编程思想，就叫做面向切面编程
- 开发流程
  - 开发阶段：编写核心业务代码-->抽取公共代码制作成通知-->在配置文件中声明切入点和通知间的关系，即**切面**
  - 运行阶段：由Spring监控切入点方法执行，一旦方法被执行就进行代理，动态创建代理对象，在对应位置织入通知对应功能

## 8.1 案例中存在的问题

- 原先案例的问题：写一个转账操作，查询2次更新2次，每次都会获取一个连接，每次成功就会提交事务，而为了保证成功，应该使得这些操作只使用一个connection

- 解决方法：使用`ThreadLocal`对象把Connection与当前线程绑定，使得一个线程只有一个控制事务的对象

  1. 创建获取线程连接工具类

     ```java
     /**
      * 连接的工具类，从数据源中获取一个连接，并与线程绑定
      */
     public class ConnectionUtils {
         private ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
         private DataSource dataSource;
     
         public void setDataSource(DataSource dataSource) {
             this.dataSource = dataSource;
         }
     
         /**
          * 获取当前线程上的连接
          *
          * @return
          */
         public Connection getThreadConnection() {
             try {
                 // 1.先从给ThreadLocal上获取
                 Connection conn = tl.get();
                 // 2.判断当前线程上是否有连接
                 if (conn == null) {
                     // 3.从数据源中获取一个连接，并与线程绑定，并存入ThreadLocal中
                     conn = dataSource.getConnection();
                     tl.set(conn);
                 }
                 return conn;
             } catch (SQLException e) {
                 e.printStackTrace();
             }
             return null;
         }
         
         /**
          * 将连接与线程解绑
          */
         public void removeConnection(){
             tl.remove();
         }
     
     }
     ```

  2. 创建控制事务的类

     ```java
     /**
      * 与事务管理相关的工具类，开启、提交、回滚、释放
      */
     public class TransactionManager {
     
         private ConnectionUtils connectionUtils;
     
         public void setConnectionUtils(ConnectionUtils connectionUtils) {
             this.connectionUtils = connectionUtils;
         }
     
         public void beginTransaction(){
             try {
                 connectionUtils.getThreadConnection().setAutoCommit(false);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     
         public void commit(){
             try {
                 connectionUtils.getThreadConnection().commit();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     
         public void rollback(){
             try {
                 connectionUtils.getThreadConnection().rollback();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     
         public void release(){
             try {
                 connectionUtils.getThreadConnection().close(); // 还回线程池中
                 connectionUtils.removeConnection(); // 解绑线程与连接
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     }
     ```

  3. 删除`bean.xml`中注入数据源的部分

  4. 在dao中添加`ConnectionUtils`，并在执行方法中加入`connectionUtils.getThreadConnection()`以获取连接

  5. 修改配置

     ```xml
     <bean id="connectionUtils" class="utils.ConnectionUtils">
         <!--注入数据源-->
         <property name="dataSource" ref="dataSource"></property>
     </bean>
     
     <!--配置Dao对象-->
     <bean id="accountDao" class="dao.impl.AccountDaoImpl">
         <!--注入QueryRunner-->
         <property name="runner" ref="runner"></property>
         <!--注入connectionUtils-->
         <property name="connectionUtils" ref="connectionUtils"></property>
     </bean>
     
     <bean id="txManager" class="utils.TransactionManager">
         <property name="connectionUtils" ref="connectionUtils"></property>
     </bean>
     
     <!--配置Service-->
     <bean id="accountService" class="service.impl.AccountServiceImpl">
         <!--注入Dao-->
         <property name="accountDao" ref="accountDao"></property>
         <property name="transactionManager" ref="txManager"></property>
     </bean>
     ```

  6. 为了解决代码重复和修改部分太多的问题，使用动态代理

## 8.2 动态代理

- 特点：字节码随用随创建，随用随加载

- 作用：不修改源码的基础上对方法增强

- 分类

  1. 基于接口的动态代理

     - 涉及的类：`Proxy`，由JDK官方提供
     - 创建代理对象的要求：被代理类最少实现一个接口
     - 如何创建代理对象：使用`Proxy`类中的`newProxyInstance()`方法
     - 方法参数
       - `ClassLoader`：用于加载代理对象字节码，使用的是被代理对象类加载器，固定写法：`xxx.getClass().getClassLoader()`
       - `Class[]`：用于使得代理对象与被代理对象具有相同方法，固定写法：`xxx.getClass().getInterfaces()`
       - `InvocationHandler`：用于提供增强的代码，即如何代理，一般都是写该接口实现类，通常情况下是匿名内部类；任何被代理对象的**任何接口方法**都会经过该方法
     - 弊端：动态代理必须有接口，否则无法代理

     ```java
     final Producer producer = new Producer();
     
     Producer proxyProducer = (Producer) Proxy.newProxyInstance(producer.getClass().getClassLoader(),
             producer.getClass().getInterfaces(),
             new InvocationHandler() {
                 @Override
                 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                     Object returnValue = null;
                     // 获取方法执行参数
                     Float money = (Float) args[0];
                     // 判断当前方法
                     if("saleProduct".equals(method.getName())){
                         returnValue = method.invoke(producer, money*0.8f);
                     }
                     return returnValue;
                 }
             });
     proxyProducer.saleProduct(10000f);
     ```

  2. 基于子类的动态代理

     - 涉及的类：`Enhancer`，由第三方库cglib提供
     - 创建代理对象的要求：被代理类不能是最终类，即没有被**final**修饰
     - 如何创建代理对象：使用`Enhancer`类中的`create`方法
     - 方法参数
       - `Class`：指定被代理对象的字节码
       - `callback`：用于增强，一般写的是该接口的子接口实现类，MethodInterceptor

  3. 对案例进行修改

     1. 编写创建service代理对象的工厂

        ```java
        /**
         * 用于创建service代理对象的工厂
         */
        public class BeanFactory {
            private AccountService accountService;
            private TransactionManager transactionManager;
        
            public void setTransactionManager(TransactionManager transactionManager) {
                this.transactionManager = transactionManager;
            }
        
            public final void setAccountService(AccountService accountService) {
                this.accountService = accountService;
            }
        
            /**
             * 获取service的代理对象
             * @return
             */
            public AccountService getAccountService(){
                return (AccountService)Proxy.newProxyInstance(accountService.getClass().getClassLoader(),
                        accountService.getClass().getInterfaces(), new InvocationHandler() {
                            /**
                             * 添加事务的支持
                             * @param proxy
                             * @param method
                             * @param args
                             * @return
                             * @throws Throwable
                             */
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                Object rtValue = null;
                                try {
                                    transactionManager.beginTransaction();
                                    rtValue = method.invoke(accountService, args);
                                    transactionManager.commit();
                                    return rtValue;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    transactionManager.rollback();
                                } finally {
                                    transactionManager.release();
                                }
                                return rtValue;
                            }
                        });
            }
        }
        ```

     2. 修改配置文件

        ```xml
        <!--配置代理的service-->
        <bean id="proxyAccountService" factory-bean="beanFactory" factory-method="getAccountService"></bean>
        
        <!--配置BeanFactory-->
        <bean id="beanFactory" class="factory.BeanFactory">
            <property name="accountService" ref="accountService"></property>
            <property name="transactionManager" ref="txManager"></property>
        </bean>
        
        <!--配置Service-->
        <bean id="accountService" class="service.impl.AccountServiceImpl">
            <!--注入Dao-->
            <property name="accountDao" ref="accountDao"></property>
        <!--        <property name="transactionManager" ref="txManager"></property>-->
        </bean>
        ```

     3. 把service方法中的事务控制全部删掉

     4. 给test中的service添加注解

        ```java
        @Autowired
        @Qualifier("proxyAccountService")
        private AccountService as;
        ```

## 8.3 基于XML的AOP配置

1. 导入坐标

   ```xml
       <dependencies>
           <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-context</artifactId>
               <version>5.0.2.RELEASE</version>
           </dependency>
   
           <dependency>
               <groupId>org.aspectj</groupId>
               <artifactId>aspectjweaver</artifactId>
               <version>1.8.7</version>
           </dependency>
   ```

2. 编写`bean.xml`

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:aop="http://www.springframework.org/schema/aop"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/aop
           http://www.springframework.org/schema/aop/spring-aop.xsd">
   
   
       <!--配置spring的ioc-->
       <bean id="accountService" class="service.impl.AccountServiceImpl"></bean>
   
       <!--spring中基于xml的aop配置
           1.把通知bean交给spring管理
           2.使用aop:config开始aop的配置
           3.使用aop:aspect开始配置切面
           4.在aspect内部使用对应标签配置通知类型
           5指定切入点表达式
       -->
   
       <!--配置logger类-->
       <bean id="logger" class="utils.logger"></bean>
   
       <!--配置aop-->
       <aop:config>
           <!--配置切面-->
           <aop:aspect id="logAdvice" ref="logger">
               <!--配置通知类型且建立通知方法与切入点方法的关联-->
   <!--            <aop:before method="printLog" pointcut="execution(* *..*.*(..))"></aop:before>-->
               <aop:before method="printLog" pointcut="execution(* service.impl.*.*(..))"></aop:before>
           </aop:aspect>
       </aop:config>
   </beans>
   ```

3. 切入点表达式

   写法：`关键字：execution 表达式：访问修饰符 返回值 包名.类名.方法名（参数列表）`

   - 访问修饰符可以省略
   - 返回值可以使用通配符
   - 包名可以使用通配符，有几级写几个*；*..表示当前包和子包
   - 类名和方法名都可以使用*
   - 参数类型可使用\*，但必须有参数；有无参数可使用..
   - 全通配：`* *..*.*(..)`
   - 通用化切入点表达式：`<aop:pointcut>`，如果写在切面内部就只能该切面使用，如果写在切面外部则所有切面使用，但必须写在切面之前
   - 实际开发中通常写法：切到业务层实现类下的所有方法 

4. 通知类型

   ```xml
   <aop:before method="beforePrintLog" pointcut="execution(* service.impl.*.*(..))"></aop:before>
   <aop:after-returning method="afterReturningPrintLog" pointcut="execution(* service.impl.*.*(..))"></aop:after-returning>
   <aop:after-throwing method="afterThrowingPrintLog" pointcut="execution(* service.impl.*.*(..))"></aop:after-throwing>
   <aop:after method="afterPrintLog" pointcut="execution(* service.impl.*.*(..))"></aop:after>
   
   <aop:around method="aroundPrintLog" pointcut-ref="pt1"/>
   ```

​      永远只会有3个执行，后置和异常不可能一起执行

- 环绕通知：当配置后切入点方法不执行而环绕通知方法执行，因为动态代理的环绕通知有明确的切入点方法调用，而配置中则没有；Spring提供了一个接口`ProceedingJoinPoint`，方法`proceed()`相当于明确调用切入点方法；该接口可作为环绕通知的方法参数；实质就是Spring提供的可以通过代码指定通知执行位置的方法；

  ```java
      public Object aroundPrintLog(ProceedingJoinPoint pjp){
          Object rtValue = null;
          try {
              Object[] args = pjp.getArgs();
              System.out.println("前置通知...");
              rtValue = pjp.proceed(args);
              System.out.println("后置通知...");
              return rtValue;
          } catch (Throwable e) {
              e.printStackTrace();
              System.out.println("异常通知...");
          } finally {
              System.out.println("最终通知...");
              return rtValue;
          }
      }
  ```

## 8.4 基于注解的AOP配置

1. 修改`bean.xml`头部
2. 配置创建容器要扫描的包：`<context:componet:scan base-package="">`
3. 给service类加上`@Service("")`注解
4. 给通知类加上`@Component("")`
5. 给通知类加上`@Aspect`表示为切面
6. 定义一个空方法pt1，加上`@Pointcut`标签，里面写execution参数
7. 给每个方法加上通知类型`@Before("pt1()")`
8. `bean.xml`中配置spring开启aop的支持：`<aop:aspectj-autoproxy>`
9. 注意：注解的aop配置有**执行顺序问题**，最终通知会在后置和异常以前执行

## 8.5 基于纯注解的AOP配置

```java
@Configuration 
@ComponentScan(basePackages="com.itheima") 
@EnableAspectJAutoProxy 
public class SpringConfiguration {
}
```

# 9.Spring中的JdbcTemplate

- 最普通的操作

  ```java
  // 准备数据源：spring内置数据源
  DriverManagerDataSource ds = new DriverManagerDataSource();
  ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
  //这里不需要使用&amp;来代替&，因为是字符串而不是java的配置文件
  ds.setUrl("jdbc:mysql://localhost:3306/spring?serverTimezone=UTC&useSSL=false");
  ds.setUsername("root");
  ds.setPassword("root");
  // 创建对象
  JdbcTemplate jt = new JdbcTemplate();
  //        JdbcTemplate jt = new JdbcTemplate(ds);
  // 给jt设置数据源
  jt.setDataSource(ds);
  // 执行操作
  jt.execute("insert into account(name,money) values ('ddd', 1000) ");
  ```

- IoC配置

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd">
  
      <!--配置jdbcTemplate-->
      <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
          <property name="dataSource" ref="dataSource"></property>
      </bean>
  
      <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
          <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"></property>
          <property name="url" value="jdbc:mysql://localhost:3306/spring?serverTimezone=UTC&amp;useSSL=false"></property>
          <property name="username" value="root"></property>
          <property name="password" value="root"></property>
          
          <!--配置文件-->
          <property name="location" value="classpath:jdbc.properties"></property>
          <!--另一种方式-->
          <context:property-placeholder location="classpath:jdbc.properties"/>
      </bean>
  
  </beans>
  ```

  ```java
          // 获取容器
          ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
          // 获取对象
          JdbcTemplate jt = ac.getBean("jdbcTemplate", JdbcTemplate.class);
          // 执行单表操作
          // 保存
          jt.update("insert into account(name, money) values (?, ?) ", "fff", 1000);
          // 更新
          jt.update("update account set name = ?, money = ? where id = ? ", "test", 2000, 5);
          // 删除
          jt.update("delete from account where id = ? ", 7);
          // 查询所有
          List<Account> accounts = jt.query("select * from account where money > ? ", new BeanPropertyRowMapper<Account>(Account.class), 1000f);
          for (Account account : accounts) {
              System.out.println(account);
          }
          // 查询一个
          List<Account> query = jt.query("select * from account where id = ? ", new BeanPropertyRowMapper<Account>(Account.class), 1);
          query.get(0);
          // 查询返回一行一列（使用聚合函数，但不加group by子句）
          Integer i = jt.queryForObject("select count(*) from account where money > ? ", Integer.class, 1000f);
          System.out.println(i);
  ```

- Dao实现
  1. Dao类中定义一个私有的`JdbcTemplate`变量，并设置set方法；`bean.xml`中配置账户持久层，注入jdbcTemplate
  2. 如果存在多个dao，可以把`jdbctemplate`抽取出来单独封装成一个类，设置set和get方法，并使dao继承该类，使用`super.getJdbcTemplate()`-->Spring实现了一个`JdbcDaoSupport`类用于**继承**；但如果是这种方式就**不能用注解**配置

# 10.Spring事务控制

- `PlatformTransactionManager`：事务管理器接口
- `DataSourceTransactionManager`：真正管理事务的对象

## 10.1 基于XML的声明式事务控制

- 事务属性

  - 事务传播属性`propogation`

    - `REQUIRED`：如果有事务在运行，当前方法就在该事务内执行，否则就启动一个新的事务并在自己的事务内执行

      ![这里写图片描述](https://img-blog.csdn.net/20161206094710629)

      这种情况下如果用户余额只够买一本的话，就会导致最终一本书也买不了

    - `REQUIRED_NEW`：当前方法必须启动新事务，并在自己的事务内执行，如果有事务在运行则将它挂起

      ![这里写图片描述](https://img-blog.csdn.net/20161206094937915)

    - SUPPORTS：如果有事务在运行，当前方法就在事务中运行，否则可以不运行在事务中

  - 事务隔离属性`isolation`：为了解决脏读、重复读、幻读等问题

- ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:aop="http://www.springframework.org/schema/aop"
         xmlns:tx="http://www.springframework.org/schema/tx"
         xsi:schemaLocation="
          http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/tx
          http://www.springframework.org/schema/tx/spring-tx.xsd
          http://www.springframework.org/schema/aop
          http://www.springframework.org/schema/aop/spring-aop.xsd">
  
      <!--配置持久层-->
      <bean id="accountDao" class="dao.impl.AccountDaoImpl">
          <property name="dataSource" ref="dataSource"></property>
      </bean>
  
      <!--配置业务层-->
      <bean id="accountService" class="service.impl.AccountServiceImpl">
          <property name="accountDao" ref="accountDao"></property>
      </bean>
  
      <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
          <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"></property>
          <property name="url" value="jdbc:mysql://localhost:3306/spring?serverTimezone=UTC&amp;useSSL=false"></property>
          <property name="username" value="root"></property>
          <property name="password" value="root"></property>
      </bean>
  
      <!--spring中基于xml的声明式事务控制配置步骤
          1.配置事务管理器
          2.配置事务通知：此时需要导入事务约束:tx名称空间和约束以及aop的
          3.配置aop中的切入点表达式
          4.建立事务通知和切入点表达式之间的对应关系
          5.配置事务属性：在tx:advice内部
              isolation属性：指定事务隔离级别，默认是数据库隔离级别，默认DEFAULT
              propagation：指定事务传播行为，默认一定会有事务，默认REQUIRED，增删改选择，查选择SUPPORTS
              read-only：指定事务是否只读，只有查可以设置为true
              rollback-for：指定一个异常，产生时回滚，没有默认值，表示都回滚
              no-rollback-for：指定一个异常，产生时不回滚，没有默认值，表示都回滚
              timeout：指定事务超时时间，默认-1，永不超时，以秒为单位
  
  
      -->
      <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
          <property name="dataSource" ref="dataSource"></property>
      </bean>
  
      <tx:advice id="txAdvice" transaction-manager="transactionManager">
          <tx:attributes>
              <tx:method name="transfer" propagation="REQUIRED" read-only="false"/>
              <tx:method name="find*" propagation="SUPPORTS" read-only="true"/>
          </tx:attributes>
      </tx:advice>
  
      <aop:config>
          <aop:pointcut id="pt1" expression="execution(* service.impl.*.*(..))"/>
          <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"></aop:advisor>
      </aop:config>
  
  </beans>
  ```

## 10.2 基于注解的声明式事务控制

1. 修改`bean.xml`约束

2. service类上加上`@Service("accountService")`，并给accountDao加上`@Autowired`，set方法不再需要

3. dao类上加上`@Repository("accountDao")`，**不能**使用继承，而是`@Autowired private JdbcTemplate jdbcTemplate`，set方法不再需要

4. `bean.xml`配置

   ```xml
   <!--配置创建容器时要扫描的包-->
   <context:component-scan base-package="service"></context:component-scan>
   <context:component-scan base-package="dao"></context:component-scan>
   
   <!--配置jdbctemplate-->
   <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
       <property name="dataSource" ref="dataSource"></property>
   </bean>
   
   <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
       <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"></property>
       <property name="url" value="jdbc:mysql://localhost:3306/spring?serverTimezone=UTC&amp;useSSL=false"></property>
       <property name="username" value="root"></property>
       <property name="password" value="root"></property>
   </bean>
   
   <!--spring中基于xml的声明式事务控制配置步骤
       1.配置事务管理器
       2.开启spring对注解事务的支持：此时需要导入事务约束:tx名称空间和约束以及aop的
       3.在需要事务支持的地方使用@Transactional
   -->
   <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
       <property name="dataSource" ref="dataSource"></property>
   </bean>
   
   <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>
   
   ```

5. 在业务层使用`@Transactional`注解：`@Transactional(readOnly=true,propagation=Propagation.SUPPORTS)`，如果类中有方法需要另外配置则在方法上继续使用`@Transactional`注解

6. 如果不使用xml则在配置类上加上`@EnableTransactionManagement`