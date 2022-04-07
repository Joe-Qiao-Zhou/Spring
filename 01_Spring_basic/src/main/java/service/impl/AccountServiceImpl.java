package service.impl;

import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import service.AccountService;

public class AccountServiceImpl implements AccountService {
//    private AccountDao accountDao = new AccountDaoImpl();


    public AccountServiceImpl() {
        System.out.println("对象已创建");
    }

    @Override
    public void saveAccount() {
//        accountDao.saveAccount();
        System.out.println("AccountService的saveAccount方法被调用");
    }

    public void init(){
        System.out.println("对象已初始化");
    }

    public void destroy(){
        System.out.println("对象已销毁");
    }

}
