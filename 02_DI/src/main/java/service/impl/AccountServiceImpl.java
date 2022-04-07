package service.impl;

import service.AccountService;

import java.util.Date;

public class AccountServiceImpl implements AccountService {

    private String name;
    private Integer age;
    private Date birthday;

    public AccountServiceImpl(String name, Integer age, Date birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    @Override
    public void saveAccount() {
//        accountDao.saveAccount();
        System.out.println("AccountService的saveAccount方法被调用："+name+","+age+","+birthday);
    }

}
