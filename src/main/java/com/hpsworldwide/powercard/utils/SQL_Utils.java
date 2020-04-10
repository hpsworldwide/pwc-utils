package com.hpsworldwide.powercard.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;

public class SQL_Utils {

    /**
     * also allows to check Connection is valid<br />
     * note : in Apache Tomcat hot redeploy, will generate errors with embedded Derby database ; please undeploy WAR before deploying new one<br />
     * see ERROR XSDB6: Another instance of Derby may have already booted the database<br />
     * see SEVERE org.apache.catalina.loader.WebappClassLoaderBase.checkThreadLocalMapForLeaks The web application created a ThreadLocal with key of type [java.lang.ThreadLocal] (value [java.lang.ThreadLocal]) and a value of type [org.apache.derby.iapi.services.context.ContextManager] (value [org.apache.derby.iapi.services.context.ContextManager]) but failed to remove it when the web application was stopped. Threads are going to be renewed over time to try and avoid a probable memory leak.
     */
    public static Connection getConnection(String driverName, String connectionTemplate, String dataBaseName) throws ClassNotFoundException, SQLException {
        Class.forName(driverName); // throws an exception if class not found, thus stopping this function
        String url = getConnectionURL(connectionTemplate, dataBaseName);
        return DriverManager.getConnection(url);
    }

    /**
     * from JNDI<br />
     * note : in Apache Tomcat hot WAR redeploy, will generate errors with embedded Derby database ; please undeploy WAR before deploying new one
     * see ERROR XSDB6: Another instance of Derby may have already booted the database<br />
     * see SEVERE org.apache.catalina.loader.WebappClassLoaderBase.checkThreadLocalMapForLeaks The web application created a ThreadLocal with key of type [java.lang.ThreadLocal] (value [java.lang.ThreadLocal]) and a value of type [org.apache.derby.iapi.services.context.ContextManager] (value [org.apache.derby.iapi.services.context.ContextManager]) but failed to remove it when the web application was stopped. Threads are going to be renewed over time to try and avoid a probable memory leak.
     * 
     * @param environmentName e.g. "java:/comp/env"
     * @param dataSourceName e.g. "jdbc/DatabaseDataSource"
     */
    public static DataSource getDataSource(String environmentName, String dataSourceName) throws NamingException, SQLException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup(environmentName);
        return (DataSource) envContext.lookup(dataSourceName);
    }

    public static String getConnectionURL(String connectionTemplate, String dataBaseName) {
        return String.format(connectionTemplate, dataBaseName);
    }

    public static int executeUpdate(Logger logger, Statement statement, String sql) throws SQLException {
        logger.info("executing update for statement :\n" + sql);
        return statement.executeUpdate(sql);
    }

    public static long[] selectCountQuery(Connection connection, String sql) throws SQLException {
        long[] result;
        try (Statement countStatement = connection.createStatement(); ResultSet resultSet = countStatement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new RuntimeException("no row returned in ResultSet");
            }
            int nbColumns = resultSet.getMetaData().getColumnCount();
            result = new long[nbColumns];
            for (int i = 0; i < nbColumns; i++) {
                result[i] = resultSet.getLong(i + 1);
            }
        }
        return result;
    }

    public static void auditConnection(Logger logger, Connection connection) throws SQLException {
        logger.info("autoCommit: " + connection.getAutoCommit());
        logger.info("catalog: " + connection.getCatalog());
        logger.info("schema: " + connection.getSchema());
        logger.info("clientInfo: " + connection.getClientInfo());
        logger.info("holdability: " + connection.getHoldability());
        logger.info("metaData: " + connection.getMetaData());
        auditDataBaseMetaData(logger, connection.getMetaData());
        logger.info("networkTimeout: ");
        try {
            logger.info(Integer.toString(connection.getNetworkTimeout()));
        } catch (SQLFeatureNotSupportedException ex) {
            logger.info(ex.getMessage());
        }
        logger.info("transactionIsolation: " + connection.getTransactionIsolation());
        logger.info("typeMap: " + connection.getTypeMap());
        logger.info("warnings: " + connection.getWarnings().toString());
    }

    public static void auditDataBaseMetaData(Logger logger, DatabaseMetaData databaseMetaData) throws SQLException {
        logger.info("Database Product Name   : " + databaseMetaData.getDatabaseProductName());
        logger.info("Database Product Version: " + databaseMetaData.getDatabaseProductVersion());
        logger.info("Database Driver Name    : " + databaseMetaData.getDriverName());
        logger.info("Database Driver Version : " + databaseMetaData.getDriverVersion());
    }

    public static boolean doesTableExist(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        boolean exists;
        try (ResultSet resultSet = databaseMetaData.getTables(null, null, tableName, null)) {
            exists = resultSet.next();
        }
        return exists;
    }

    public static void close(Logger logger, AutoCloseable... closeable) {
        for (AutoCloseable _closeable : closeable) {
            if (_closeable != null) {
                try {
                    logger.info("closing " + _closeable + " (class=" + _closeable.getClass() + ") ...");
                    _closeable.close();
                    logger.info(_closeable + " closed properly.");
                } catch (Exception ex) {
                    throw new RuntimeException("SQLException while closing " + _closeable, ex);
                }
            }
        }
    }

    /**
     * format : 2013-01-01 00:00:00.
     */
    public static String formatSSQL_Date(Date date) {
        return new Timestamp(date.getTime()).toString();
    }

    public static void closeAllDrivers(Logger logger) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                logger.info(String.format("deregistering jdbc driver: %s ...", driver));
                DriverManager.deregisterDriver(driver);
                logger.info(String.format("jdbc driver %s deregistered.", driver));
            } catch (SQLException e) {
                throw new RuntimeException(String.format("Error deregistering driver %s", driver), e);
            }
        }
    }
}
