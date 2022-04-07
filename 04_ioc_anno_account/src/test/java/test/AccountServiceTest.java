package test;

import domain.Account;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.AccountService;

import java.util.List;

public class AccountServiceTest {

    @Test
    public void testFindAll(){
        // 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 获取业务层对象
        AccountService as = ac.getBean("accountService", AccountService.class);
        List<Account> accounts = as.findAllAccount();
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    @Test
    public void testFindOne(){
        // 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 获取业务层对象
        AccountService as = ac.getBean("accountService", AccountService.class);
        Account account = as.findAccountById(1);
        System.out.println(account);
    }

    @Test
    public void testSave(){
        // 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 获取业务层对象
        AccountService as = ac.getBean("accountService", AccountService.class);
        Account account = new Account();
        account.setName("test");
        account.setMoney(1000f);
        as.saveAccount(account);
    }

    @Test
    public void testUpdate(){
        // 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 获取业务层对象
        AccountService as = ac.getBean("accountService", AccountService.class);
        Account account = as.findAccountById(4);
        account.setMoney(2000f);
        as.updateAccount(account);
    }

    @Test
    public void testDelete(){
        // 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 获取业务层对象
        AccountService as = ac.getBean("accountService", AccountService.class);
        as.deleteAccount(4);
    }
}
