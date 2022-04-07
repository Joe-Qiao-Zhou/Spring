package ui;

import dao.AccountDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.AccountService;

public class Client {

    /**
     * 获取spring的ioc核心容器，并根据id获取对象
     * @param args
     */
    public static void main(String[] args) {
        // 1.获取核心容器对象
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 2.根据id获取Bean对象
        AccountService as = (AccountService)ac.getBean("accountServiceImpl");
        System.out.println(as);
//        AccountDao ad = ac.getBean("accountDaoImpl", AccountDao.class);
//        System.out.println(ad);
        as.saveAccount();
        AccountService as2 = (AccountService)ac.getBean("accountServiceImpl");
        System.out.println(as == as2);

    }
}
