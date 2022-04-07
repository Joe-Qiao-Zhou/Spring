package service.impl;

import service.AccountService;

public class AccountServiceImpl implements AccountService {
    @Override
    public void saveAccount() {
        System.out.println("执行保存操作");
//        int i = 1 / 0;
    }

    @Override
    public void updateAccount(int i) {
        System.out.println("执行更新操作" + i);
    }

    @Override
    public int deleteAccount() {
        System.out.println("执行删除操作");
        return 0;
    }
}
