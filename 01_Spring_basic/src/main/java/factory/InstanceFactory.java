package factory;


import service.AccountService;
import service.impl.AccountServiceImpl;

/**
 * 模拟一个存在于jar包种的工厂类，无法通过修改源码的方式提供默认函数
 */
public class InstanceFactory {

    public AccountService getAccountService(){
        return new AccountServiceImpl();
    }
}
