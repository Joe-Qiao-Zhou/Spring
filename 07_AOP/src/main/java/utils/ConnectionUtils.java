package utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 连接的工具类，从数据源中获取一个连接，并与线程绑定
 */
public class ConnectionUtils {
    private ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取当前线程上的连接
     *
     * @return
     */
    public Connection getThreadConnection() {
        try {
            // 1.先从给ThreadLocal上获取
            Connection conn = tl.get();
            // 2.判断当前线程上是否有连接
            if (conn == null) {
                // 3.从数据源中获取一个连接，并与线程绑定，并存入ThreadLocal中
                conn = dataSource.getConnection();
                tl.set(conn);
            }
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将连接与线程解绑
     */
    public void removeConnection(){
        tl.remove();
    }
}
