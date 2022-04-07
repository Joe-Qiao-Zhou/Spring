package config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * 配置类，与bean.xml作用相同
 * @Configuration：指定当前类是一个配置类
 * @ComponentScan：指定spring在创建容器时要扫描的包
 * @Bean：将当前方法返回值作为Bean对象存入Spring容器中；
 * 如果方法有参数，spring会去容器中查找有无可用的Bean对象，同@Autowired
 *      name：指定bean的id，默认是方法名
 */

@Configuration
//@ComponentScans(value={@ComponentScan("com.itheima.dao"), @ComponentScan("com.itheima.service")})
@ComponentScan("com.itheima")
public class SpringConfiguration {

    @Bean(name = "runner")
    public QueryRunner createQueryRunner(DataSource ds){
        return new QueryRunner(ds);
    }

    @Bean(name = "dataSource")
    public DataSource createDataSource(){
        ComboPooledDataSource ds = new ComboPooledDataSource();
        try {
            ds.setDriverClass("com.mysql.cj.jdbc.Driver");
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/spring?serverTimezone=UTC");
            ds.setUser("root");
            ds.setPassword("root");
            return ds;
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return null;
    }
}
