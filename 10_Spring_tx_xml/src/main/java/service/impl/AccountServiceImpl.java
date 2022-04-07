package service.impl;

import dao.AccountDao;
import domain.Account;
import service.AccountService;


public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account findAccountById(Integer accountId) {
        return accountDao.findAccountById(accountId);
    }


    @Override
    public void transfer(String sourceName, String targetName, Float money) {

        Account source = accountDao.findAccountByName(sourceName);
        Account target = accountDao.findAccountByName(targetName);

        source.setMoney(source.getMoney() - money);
        target.setMoney((target.getMoney() + money));

        accountDao.updateAccount(source);
            int i = 1 / 0;
        accountDao.updateAccount(target);

    }

}
