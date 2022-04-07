package jdbcTemplate;

import domain.Account;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcTemplateDemo3 {
    public static void main(String[] args) {
        // 获取容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        // 获取对象
        JdbcTemplate jt = ac.getBean("jdbcTemplate", JdbcTemplate.class);
        // 执行单表操作
        // 保存
        jt.update("insert into account(name, money) values (?, ?) ", "fff", 1000);
        // 更新
        jt.update("update account set name = ?, money = ? where id = ? ", "test", 2000, 5);
        // 删除
        jt.update("delete from account where id = ? ", 7);
        // 查询所有
        List<Account> accounts = jt.query("select * from account where money > ? ", new BeanPropertyRowMapper<Account>(Account.class), 1000f);
        for (Account account : accounts) {
            System.out.println(account);
        }
        // 查询一个
        List<Account> query = jt.query("select * from account where id = ? ", new BeanPropertyRowMapper<Account>(Account.class), 1);
        query.get(0);
        // 查询返回一行一列（使用聚合函数，但不加group by子句）
        Integer i = jt.queryForObject("select count(*) from account where money > ? ", Integer.class, 1000f);
        System.out.println(i);
    }
}
