package service.impl;

import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import service.AccountService;

import javax.annotation.Resource;
import java.util.Date;


/**
 *     <bean id="" class="" scope="" init-method="" destroy-method="">
 *         <property name="" value="" ref=""></property>
 *     </bean>
 *
 * 用于创建对象的：同<bean></bean>
 *  @Componment：用于把当前类对象存入容器中
 *  属性：
 *      value：指定bean的id。当不写时，默认值是当前类名且首字母小写
 * @Controller @Service @Repository
 * 与Component效果完全相同，只是为了三层架构而设计
 * 用于注入数据的：同<property></property>
 *  @Autowired:自动按照类型注入，可以出现在变量或者方法上;
 *      在使用注解注入时，set方法不是必须的：@Autowired标记的类型会去找@Component标记的类型，只要匹配就注入成功
 *      如果一个匹配没有则注入失败
 *      如果多个匹配，先看类型再看变量名，如果变量名不一样就报错
 * @Qualifier：在类名基础上按照名称注入，再给类成员注入时不能单独使用，必须和@Autowired配合；但在给方法参数注入时可以
 *      属性：value：用于指定注入bean的id
 * @Resource：直接按照bean的id注入，不依赖于@Autowired
 *      属性：name：用于指定注入bean的id
 * 以上三个注入都只能注入Bean类型数据，基本类型和String无法使用
 * 集合类型的注入只能通过XML实现
 * @Value：用于注入基本类型和String
 *      属性：value：用于指定数据的值
 * 用于改变作用范围的：同<scope></scope>
 * @Scope:用于指定bean的作用范围
 *  属性：value：指定范围取值，常用singleton prototype
 * 和生命周期有关的：同<init_method></init_method>和<destroy_method></destroy_method>
 * @PreDestroy @PostConstruct
 */
@Service
@Scope(value = "prototype")
public class AccountServiceImpl implements AccountService {

//    @Autowired
//    @Qualifier("accountDaoImpl")
    @Resource(name="accountDaoImpl")
    private AccountDao accountDao;

    @Override
    public void saveAccount() {
        accountDao.saveAccount();
    }

}
