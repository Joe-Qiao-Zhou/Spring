package test;

import com.zhouqiao.dao.AccountDao;
import com.zhouqiao.domain.Account;
import com.zhouqiao.service.AccountService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;
import java.util.List;

public class test {
    @Test
    public void test1(){
        // 加载配置文件
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
        // 获取对象
        AccountService as = (AccountService) ac.getBean("accountService");
        // 调用方法
        as.findAll();
    }

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

}
