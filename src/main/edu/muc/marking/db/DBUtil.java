package edu.muc.marking.db;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-4   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class DBUtil {

    private static Logger logger = LoggerFactory.getLogger(DBUtil.class);
    private static Properties properties = new Properties();
    private static Connection connection = null;

    private static DataSource dataSource;

    static {
        // 读取配置文件
        try {
            properties.load(DBUtil.class.getResourceAsStream("/dbcp.properties"));
        } catch (IOException e) {
            logger.error("读取数据库连接池配置文件失败：/dbcp.properties", e);
        }
        // 创建数据库连接池
        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            logger.error("数据库连接池创建失败", e);
        }
    }

    public static Connection openConnection() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("从数据库连接池中获取连接失败", e);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (null != connection)
                connection.close();
        } catch (SQLException e) {
            logger.error("数据库关闭异常", e);
        } finally {
            connection = null;
            System.gc();
        }
    }

    public static ResultSet query(Connection con, PreparedStatement preStmt, String sql, Object... params) {
        preStmt = null;
        ResultSet rs = null;
        try {
            try {
                preStmt = con.prepareStatement(sql);
                for (int i = 0; i < params.length; i++)
                    preStmt.setObject(i + 1, params[i]);
                rs = preStmt.executeQuery();
            } finally {
               /* if (null != rs)
                    rs.close();
                if (null != preStmt)
                    preStmt.close();*/
            }
        } catch (SQLException e) {
            logger.error("", e);
        }

        return rs;
    }

    public static List<Map<String, Object>> queryMapList(Connection con, String sql) throws SQLException,
            InstantiationException, IllegalAccessException {
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        Statement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = con.createStatement();
            rs = preStmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    map.put(name, value);
                }
                lists.add(map);
            }
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    public static List<Map<String, Object>> queryMapList(Connection con, String sql, Object... params)
            throws SQLException, InstantiationException, IllegalAccessException {
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            rs = preStmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    map.put(name, value);
                }
                lists.add(map);
            }
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    public static <T> List<T> queryBeanList(Connection con, String sql, Class<T> beanClass) {
        List<T> lists = new ArrayList<T>();
        Statement stmt = null;
        ResultSet rs = null;
        Field[] fields = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            fields = beanClass.getDeclaredFields();
            for (Field f : fields)
                f.setAccessible(true);
            while (null != rs && rs.next()) {
                T t = beanClass.newInstance();
                for (Field f : fields) {
                    String name = f.getName();
                    try {
                        Object value = rs.getObject(name);
                        setValue(t, f, value);
                    } catch (Exception e) {
                    }
                }
                lists.add(t);
            }
        } catch (SQLException e) {
            logger.error("", e);
        } catch (InstantiationException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        } finally {
            try {
                if (null != rs)
                    rs.close();
                if (null != stmt)
                    stmt.close();
            } catch (SQLException e) {
                logger.error("", e);
            }

        }
        return lists;
    }

    public static <T> List<T> queryBeanList(Connection con, String sql, Class<T> beanClass, Object... params)
            throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = new ArrayList<T>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Field[] fields = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            rs = preStmt.executeQuery();
            fields = beanClass.getDeclaredFields();
            for (Field f : fields)
                f.setAccessible(true);
            while (null != rs && rs.next()) {
                T t = beanClass.newInstance();
                for (Field f : fields) {
                    String name = f.getName();
                    try {
                        Object value = rs.getObject(name);
                        setValue(t, f, value);
                    } catch (Exception e) {
                    }
                }
                lists.add(t);
            }
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    public static <T> List<T> queryBeanList(Connection con, String sql, IResultSetCall<T> qdi) throws SQLException {
        List<T> lists = new ArrayList<T>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (null != rs && rs.next())
                lists.add(qdi.invoke(rs));
        } finally {
            if (null != rs)
                rs.close();
            if (null != stmt)
                stmt.close();
        }
        return lists;
    }

    public static <T> List<T> queryBeanList(Connection con, String sql, IResultSetCall<T> qdi, Object... params)
            throws SQLException {
        List<T> lists = new ArrayList<T>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);
            rs = preStmt.executeQuery();
            while (null != rs && rs.next())
                lists.add(qdi.invoke(rs));
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    public static <T> T queryBean(Connection con, String sql, Class<T> beanClass) throws SQLException,
            InstantiationException, IllegalAccessException {
        List<T> lists = queryBeanList(con, sql, beanClass);
        if (lists.size() != 1)
            throw new SQLException("SqlError：期待一行返回值，却返回了太多行！");
        return lists.get(0);
    }

    public static <T> T queryBean(Connection con, String sql, Class<T> beanClass, Object... params)
            throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = queryBeanList(con, sql, beanClass, params);
        if (lists.size() != 1)
            throw new SQLException("SqlError：期待一行返回值，却返回了太多行！");
        return lists.get(0);
    }

    public static <T> List<T> queryObjectList(Connection con, String sql, Class<T> objClass) throws SQLException,
            InstantiationException, IllegalAccessException {
        List<T> lists = new ArrayList<T>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            label:
            while (null != rs && rs.next()) {
                Constructor<?>[] constor = objClass.getConstructors();
                for (Constructor<?> c : constor) {
                    Object value = rs.getObject(1);
                    try {
                        lists.add((T) c.newInstance(value));
                        continue label;
                    } catch (Exception e) {
                    }
                }
            }
        } finally {
            if (null != rs)
                rs.close();
            if (null != stmt)
                stmt.close();
        }
        return lists;
    }

    public static <T> List<T> queryObjectList(Connection con, String sql, Class<T> objClass, Object... params)
            throws SQLException, InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<T>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);
            rs = preStmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (rs.next()) {
                //通过反射机制创建一个实例
                T resultObject = objClass.newInstance();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = rs.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    Field field = objClass.getDeclaredField(cols_name);
                    field.setAccessible(true); //打开javabean的访问权限
                    field.set(resultObject, cols_value);
                }
                list.add(resultObject);
            }
        } catch (NoSuchFieldException e) {
            logger.error("NoSuchFieldException", e);
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return list;
    }

    public static <T> T queryObject(Connection con, String sql, Class<T> objClass) {
        try {
            List<T> lists = queryObjectList(con, sql, objClass);
            if (lists.size() != 1)
                throw new SQLException("SqlError：期待一行返回值，却返回了太多行！");
            return lists.get(0);
        } catch (SQLException e) {
            logger.error("", e);
        } catch (InstantiationException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        }
        return null;
    }

    public static <T> T queryObject(Connection con, String sql, Class<T> objClass, Object... params) {
        try {
            List<T> lists = queryObjectList(con, sql, objClass, params);
            if (lists.size() == 0)
                return null;
            else if (lists.size() > 1)
                throw new SQLException("SqlError：期待一行返回值，却返回了太多行！");
            return lists.get(0);
        } catch (SQLException e) {
            logger.error("", e);
        } catch (InstantiationException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        }
        return null;
    }

    public static int execute(Connection con, String sql) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            return stmt.executeUpdate(sql);
        } finally {
            if (null != stmt)
                stmt.close();
        }
    }

    public static int execute(Connection con, String sql, Object... params) throws SQLException {
        PreparedStatement preStmt = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            return preStmt.executeUpdate();
        } finally {
            if (null != preStmt)
                preStmt.close();
        }
    }

    public static int[] executeAsBatch(Connection con, List<String> sqlList) throws SQLException {
        return executeAsBatch(con, sqlList.toArray(new String[]{}));
    }

    public static int[] executeAsBatch(Connection con, String[] sqlArray) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            for (String sql : sqlArray) {
                stmt.addBatch(sql);
            }
            return stmt.executeBatch();
        } finally {
            if (null != stmt) {
                stmt.close();
            }
        }
    }

    public static int[] executeAsBatch(Connection con, String sql, Object[][] params) throws SQLException {
        PreparedStatement preStmt = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                Object[] rowParams = params[i];
                for (int k = 0; k < rowParams.length; k++) {
                    Object obj = rowParams[k];
                    preStmt.setObject(k + 1, obj);
                }
                preStmt.addBatch();
            }
            return preStmt.executeBatch();
        } finally {
            if (null != preStmt) {
                preStmt.close();
            }
        }
    }

    private static <T> void setValue(T t, Field f, Object value) throws IllegalAccessException {
        // TODO 以数据库类型为准绳，还是以java数据类型为准绳？还是混合两种方式？
        if (null == value)
            return;
        String v = value.toString();
        String n = f.getType().getName();
        if ("java.lang.Byte".equals(n) || "byte".equals(n)) {
            f.set(t, Byte.parseByte(v));
        } else if ("java.lang.Short".equals(n) || "short".equals(n)) {
            f.set(t, Short.parseShort(v));
        } else if ("java.lang.Integer".equals(n) || "int".equals(n)) {
            f.set(t, Integer.parseInt(v));
        } else if ("java.lang.Long".equals(n) || "long".equals(n)) {
            f.set(t, Long.parseLong(v));
        } else if ("java.lang.Float".equals(n) || "float".equals(n)) {
            f.set(t, Float.parseFloat(v));
        } else if ("java.lang.Double".equals(n) || "double".equals(n)) {
            f.set(t, Double.parseDouble(v));
        } else if ("java.lang.String".equals(n)) {
            f.set(t, value.toString());
        } else if ("java.lang.Character".equals(n) || "char".equals(n)) {
            f.set(t, (Character) value);
        } else if ("java.lang.Date".equals(n)) {
            f.set(t, new Date(((java.sql.Date) value).getTime()));
        } else if ("java.lang.Timer".equals(n)) {
            f.set(t, new Time(((java.sql.Time) value).getTime()));
        } else if ("java.sql.Timestamp".equals(n)) {
            f.set(t, (java.sql.Timestamp) value);
        } else {
            System.out.println("SqlError：暂时不支持此数据类型，请使用其他类型代替此类型！");
        }
    }

    public static void executeProcedure(Connection con, String procedureName, Object... params) throws SQLException {
        CallableStatement proc = null;
        try {
            proc = con.prepareCall(procedureName);
            for (int i = 0; i < params.length; i++) {
                proc.setObject(i + 1, params[i]);
            }
            proc.execute();
        } finally {
            if (null != proc)
                proc.close();
        }
    }


    public static <T> List<List<T>> listLimit(List<T> lists, int pageSize) {
        List<List<T>> llists = new ArrayList<List<T>>();
        for (int i = 0; i < lists.size(); i = i + pageSize) {
            try {
                List<T> list = lists.subList(i, i + pageSize);
                llists.add(list);
            } catch (IndexOutOfBoundsException e) {
                List<T> list = lists.subList(i, i + (lists.size() % pageSize));
                llists.add(list);
            }
        }
        return llists;
    }


}
