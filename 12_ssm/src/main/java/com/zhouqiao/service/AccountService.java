package com.zhouqiao.service;

import com.zhouqiao.domain.Account;

import java.util.List;

public interface AccountService {
    public void saveAccount(Account account);
    public List<Account> findAll();
}
