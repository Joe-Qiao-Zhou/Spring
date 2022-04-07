package jdbcTemplate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JdbcTemplateDemo1 {
    public static void main(String[] args) {
        // 准备数据源：spring内置数据源
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //这里不需要使用&amp;来代替&，因为是字符串而不是java的配置文件
        ds.setUrl("jdbc:mysql://localhost:3306/spring?serverTimezone=UTC&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("root");
        // 创建对象
        JdbcTemplate jt = new JdbcTemplate();
//        JdbcTemplate jt = new JdbcTemplate(ds);
        // 给jt设置数据源
        jt.setDataSource(ds);
        // 执行操作
        jt.execute("insert into account(name,money) values ('ddd', 1000) ");
    }
}
