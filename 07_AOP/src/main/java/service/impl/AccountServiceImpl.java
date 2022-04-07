package service.impl;

import dao.AccountDao;
import domain.Account;
import service.AccountService;
import utils.TransactionManager;

import java.util.List;


public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;
//    private TransactionManager transactionManager;
//
//    public void setTransactionManager(TransactionManager transactionManager) {
//        this.transactionManager = transactionManager;
//    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public List<Account> findAllAccount() {
        return accountDao.findAllAccount();
    }

    @Override
    public Account findAccountById(Integer accountId) {
        return accountDao.findAccountById(accountId);
    }

    @Override
    public void saveAccount(Account acc) {
        accountDao.saveAccount(acc);
    }

    @Override
    public void updateAccount(Account acc) {
        accountDao.updateAccount(acc);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        accountDao.deleteAccount(accountId);
    }

    @Override
    public void transfer(String sourceName, String targetName, float money) {
//        try {
//            transactionManager.beginTransaction();

            Account source = accountDao.findAccountByName(sourceName);
            Account target = accountDao.findAccountByName(targetName);

            source.setMoney(source.getMoney() - money);
            target.setMoney((target.getMoney() + money));

            accountDao.updateAccount(source);
//            int i = 1 / 0;
            accountDao.updateAccount(target);

//            transactionManager.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            transactionManager.rollback();
//        } finally {
//            transactionManager.release();
//        }
    }

}
