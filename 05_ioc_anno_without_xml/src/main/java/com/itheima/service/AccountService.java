package com.itheima.service;

import com.itheima.domain.Account;

import java.util.List;

public interface AccountService {

    List<Account> findAllAccount();

    Account findAccountById(Integer accountId);

    void saveAccount(Account acc);

    void updateAccount(Account acc);

    void deleteAccount(Integer accountId);
}
