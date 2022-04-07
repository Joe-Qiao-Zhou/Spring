package factory;

import domain.Account;
import service.AccountService;
import utils.TransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 用于创建service代理对象的工厂
 */
public class BeanFactory {
    private AccountService accountService;
    private TransactionManager transactionManager;

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public final void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 获取service的代理对象
     * @return
     */
    public AccountService getAccountService(){
        return (AccountService)Proxy.newProxyInstance(accountService.getClass().getClassLoader(),
                accountService.getClass().getInterfaces(), new InvocationHandler() {
                    /**
                     * 添加事务的支持
                     * @param proxy
                     * @param method
                     * @param args
                     * @return
                     * @throws Throwable
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object rtValue = null;
                        try {
                            transactionManager.beginTransaction();
                            rtValue = method.invoke(accountService, args);
                            transactionManager.commit();
                            return rtValue;
                        } catch (Exception e) {
                            e.printStackTrace();
                            transactionManager.rollback();
                        } finally {
                            transactionManager.release();
                        }
                        return rtValue;
                    }
                });
    }
}
