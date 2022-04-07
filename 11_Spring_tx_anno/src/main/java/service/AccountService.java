package service;

import domain.Account;

public interface AccountService {

    Account findAccountById(Integer accountId);

    void transfer(String sourceName, String targetName, Float money);

}
