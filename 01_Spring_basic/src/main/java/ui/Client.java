package ui;

import dao.AccountDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import service.AccountService;

public class Client {

    /**
     * 获取spring的ioc核心容器，并根据id获取对象
     * @param args
     */
    public static void main(String[] args) {
        // 1.获取核心容器对象
//        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext("D:\\javacode\\spring\\src\\main\\resources\\bean.xml");
        // 2.根据id获取Bean对象
        AccountService as1 = (AccountService)ac.getBean("accountService");
//        AccountService as2 = (AccountService)ac.getBean("accountService");
//        System.out.println(as1 == as2);
//        AccountDao adao = ac.getBean("accountDao", AccountDao.class);
        as1.saveAccount();
        ac.close();
//        System.out.println(as);
//        System.out.println(adao);
    }
}
