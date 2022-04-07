package com.itheima.dao;

import com.itheima.domain.Account;

import java.util.List;

public interface AccountDao {

    List<Account> findAllAccount();

    Account findAccountById(Integer accountId);

    void saveAccount(Account acc);

    void updateAccount(Account acc);

    void deleteAccount(Integer accountId);

}
