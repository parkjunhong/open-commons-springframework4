/*
 * Copyright 2019 Park Jun-Hong_(fafanmama_at_naver_com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * COPYLEFT by 'Open Commons' & Park Jun-Hong (parkjunhong77@gmail.com) All Rights Reserved.
 *
 * This file is generated under this project, "open-commons-springframework4".
 *
 * Date  : 2019. 3. 28. 오후 3:42:37
 *
 * Author: Park_Jun_Hong_(fafanmama_at_naver_com)
 * 
 */

package open.commons.springframework4.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.ConnectionProxy;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import open.commons.Result;
import open.commons.database.ConnectionCallbackBroker;
import open.commons.database.ConnectionCallbackBroker2;
import open.commons.database.DefaultConCallbackBroker2;
import open.commons.database.IConnectionCallbackSetter;
import open.commons.function.SQLBiFunction;
import open.commons.function.SQLConsumer;
import open.commons.function.SQLFunction;
import open.commons.utils.AssertUtils;
import open.commons.utils.SQLUtils;

/**
 * DAO 공통 기능 제공 클래스.
 * 
 * @since 2019. 3. 28.
 * @version 0.1.0
 * @author Park_Jun_Hong_(fafanmama_at_naver_com)
 */
public abstract class AbstractGenericDao implements IGenericDao {

    protected Logger logger = LogManager.getLogger(getClass());

    protected DataSource dataSource;
    protected ReloadableResourceBundleMessageSource querySource;
    protected JdbcTemplate jdbcTemplate;

    private final ConcurrentSkipListMap<String, SQLBiFunction<ResultSet, Integer, ?>> CREATORS = new ConcurrentSkipListMap<>();

    /**
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @since 2019. 3. 28.
     * @version 0.1.0
     */
    public AbstractGenericDao() {
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        AssertUtils.assertNull("DataSource MUST NOT BE null", this.dataSource);
        AssertUtils.assertNull("QuerySource Source MUST NOT BE null", this.querySource);
    }

    private <T> List<T> createObject(ResultSet rs, Class<T> entity, String... columns) throws SQLException {
        SQLBiFunction<ResultSet, Integer, T> creator = findCreator(entity, columns);

        List<T> l = new ArrayList<>();
        int i = 1;
        while (rs.next()) {
            l.add(creator.apply(rs, i++));
        }
        return l;
    }

    /**
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
    }

    /**
     * 
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param act
     * @return
     *
     * @since 2019. 3. 28.
     * @version _._._
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    private <T> T execute(SQLFunction<Connection, T> act) {

        Connection con = DataSourceUtils.getConnection(getDataSource());
        Connection conToWork = null;

        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        NativeJdbcExtractor nativeJdbcExtractor = jdbcTemplate.getNativeJdbcExtractor();

        DefaultConnectionCallback action = null;

        try {
            con.setAutoCommit(false);

            if (nativeJdbcExtractor != null) {
                conToWork = nativeJdbcExtractor.getNativeConnection(con);
            } else {
                conToWork = (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class<?>[] { ConnectionProxy.class },
                        new CloseSuppressingInvocationHandler(con, jdbcTemplate));
            }

            conToWork.setAutoCommit(false);
            T r = act.apply(conToWork);

            return r;

        } catch (SQLException e) {
            logger.warn("Fail to execute query.", e);

            try {
                con.rollback();
            } catch (SQLException ignored) {
            }

            DataAccessException dae = jdbcTemplate.getExceptionTranslator().translate("ConnectionCallback", (action != null ? action.getBroker().getQuery() : null), e);
            throw new IllegalArgumentException(dae.getMessage(), dae);
        } finally {
            try {
                if (con != null) {
                    con.commit();
                }
            } catch (SQLException ignored) {
            }

            DataSourceUtils.releaseConnection(con, dataSource);

            con = null;
            conToWork = null;
        }
    }

    /**
     * 
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param broker
     * @param creator
     * @return
     *
     * @since 2019. 3. 28.
     * @version _._._
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public final <T> Result<List<T>> executeQuery(ConnectionCallbackBroker broker, Class<T> entity, String... columns) {

        Result<List<T>> result = new Result<>();

        try {
            List<T> list = execute(con -> {
                PreparedStatement pstmt = con.prepareStatement(broker.getQuery());

                IConnectionCallbackSetter setter = broker.getSetter();
                if (setter != null) {
                    setter.set(pstmt);
                }

                ResultSet rs = pstmt.executeQuery();

                return createObject(rs, entity, columns);
            });

            result.andTrue().setData(list);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param broker
     * @param entity
     * @param columns
     * @return
     * @throws SQLException
     *
     * @since 2019. 3. 28.
     * @version _._._
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public final <S, T> Result<List<T>> executeQuery(ConnectionCallbackBroker2<S> broker, Class<T> entity, String... columns) {

        Result<List<T>> result = new Result<>();

        try {
            List<T> list = execute(con -> {
                PreparedStatement pstmt = con.prepareStatement(broker.getQuery());
                broker.set(pstmt);

                ResultSet rs = pstmt.executeQuery();

                return createObject(rs, entity, columns);
            });

            result.andTrue().setData(list);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 단일/다중 (Insert/Update/Delete) 쿼리 요청을 처리한다.<br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param brokers
     *            쿼리 실행 객체
     * @return
     *
     * @since 2019. 3. 28.
     * @version 0.1.0
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public Result<Integer> executeUpdate(ConnectionCallbackBroker... brokers) {
        return executeUpdate(Arrays.asList(brokers));
    }

    /**
     * 단일/다중 (Insert/Update/Delete) 쿼리 요청을 처리한다.<br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param brokers
     * @return
     *
     * @since 2019. 3. 28.
     * @version _._._
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    @SuppressWarnings("unchecked")
    public <T> Result<Integer> executeUpdate(ConnectionCallbackBroker2<T>... brokers) {

        Result<Integer> result = new Result<>();

        try {
            Integer updated = execute(con -> {
                DefaultConnectionCallback2<T> action = null;
                int inserted = 0;
                for (ConnectionCallbackBroker2<T> broker : brokers) {
                    action = new DefaultConnectionCallback2<T>(broker);
                    inserted += action.doInConnection(con);
                }

                return inserted;
            });

            result.andTrue().setData(updated);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 다중 (Insert/Update/Delete) 쿼리 요청을 처리한다.<br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param brokers
     *            쿼리 실행 객체
     * @return
     *
     * @since 2019. 3. 28.
     * @version 0.1.0
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public Result<Integer> executeUpdate(List<ConnectionCallbackBroker> brokers) {

        Result<Integer> result = new Result<>();

        try {
            Integer updated = execute(con -> {
                DefaultConnectionCallback action = null;
                int inserted = 0;
                for (ConnectionCallbackBroker broker : brokers) {
                    action = new DefaultConnectionCallback(broker);
                    inserted += action.doInConnection(con);
                }
                return inserted;
            });

            result.andTrue().setData(updated);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 단일 쿼리 요청을 수행한다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param queryy
     * @param setter
     * @return
     *
     * @since 2019. 3. 28.
     * @version 0.1.0
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public Result<Integer> executeUpdate(String query, IConnectionCallbackSetter setter) {
        return executeUpdate(new ConnectionCallbackBroker(query, setter));
    }

    @SuppressWarnings("unchecked")
    private <T> SQLBiFunction<ResultSet, Integer, T> findCreator(Class<T> entity, String... columns) {
        SQLBiFunction<ResultSet, Integer, T> creator = (SQLBiFunction<ResultSet, Integer, T>) CREATORS.get(entity.getName());

        if (creator == null) {
            creator = (rs, rowNum) -> {
                return SQLUtils.newInstance(entity, rs, columns);
            };
            CREATORS.put(entity.getName(), creator);
        }

        return creator;
    }

    /**
     * {@link DataSource}를 제공한다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @return
     *
     * @since 2019. 3. 28.
     * @version 0.1.0
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * @see open.commons.springframework4.dao.IGenericDao#getJdbcTemplate()
     */
    @Override
    public JdbcTemplate getJdbcTemplate() {
        if (this.jdbcTemplate == null) {
            this.jdbcTemplate = new JdbcTemplate(this.dataSource);
        }

        return this.jdbcTemplate;
    }

    /**
     * 
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param query
     * @param entity
     * @param columns
     * @return
     *
     * @since 2019. 3. 28.
     * @version _._._
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public <T> Result<List<T>> getList(String query, Class<T> entity, String... columns) {
        return executeQuery(new DefaultConCallbackBroker2(query, null), entity, columns);
    }

    /**
     * 
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param query
     * @param setter
     * @param entity
     * @param columns
     * @return
     *
     * @since 2019. 3. 28.
     * @version _._._
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public <T> Result<List<T>> getList(String query, SQLConsumer<PreparedStatement> setter, Class<T> entity, String... columns) {
        return executeQuery(new DefaultConCallbackBroker2(query, setter), entity, columns);
    }

    /**
     * @see open.commons.springframework4.dao.IGenericDao#getQuery(java.lang.String)
     */
    @Override
    public String getQuery(String name) {
        return this.querySource.getMessage(name, null, null);
    }

    /**
     * @see open.commons.springframework4.dao.IGenericDao#getQuery(java.lang.String, java.lang.Object[],
     *      java.util.Locale)
     */
    @Override
    public String getQuery(String name, Object[] args, Locale locale) {
        return this.querySource.getMessage(name, args, locale);
    }

    /**
     * @see open.commons.springframework4.dao.IGenericDao#getQuery(java.lang.String, java.lang.Object[],
     *      java.lang.String, java.util.Locale)
     */
    @Override
    public String getQuery(String name, Object[] args, String defaultMessage, Locale locale) {
        return this.querySource.getMessage(name, args, defaultMessage, locale);
    }

    /**
     * @see open.commons.springframework4.dao.IGenericDao#getQuerySourece()
     */
    @Override
    public ReloadableResourceBundleMessageSource getQuerySourece() {
        return this.querySource;
    }

    /**
     * {@link DataSource} 객체를 설정한다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param dataSource
     *
     * @since 2019. 3. 28.
     * @version 0.1.0
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public abstract void setDataSource(DataSource dataSource);

    /**
     * 쿼리 정보 객체를 설정한다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @param querySource
     *
     * @since 2019. 3. 28.
     * @version 0.1.0
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     */
    public abstract void setQuerySource(ReloadableResourceBundleMessageSource querySource);

    /**
     * Invocation handler that suppresses close calls on JDBC Connections. Also prepares returned Statement
     * (Prepared/CallbackStatement) objects.
     * 
     * @see java.sql.Connection#close()
     */
    private class CloseSuppressingInvocationHandler implements InvocationHandler {

        private final Connection target;

        private JdbcTemplate jdbcTemplate;

        public CloseSuppressingInvocationHandler(Connection target, JdbcTemplate jdbcTemplate) {
            this.target = target;
            this.jdbcTemplate = jdbcTemplate;
        }

        private void applyStatementSettings(JdbcTemplate jdbcTemplate, Statement stmt) throws SQLException {
            int fetchSize = jdbcTemplate.getFetchSize();
            if (fetchSize > 0) {
                stmt.setFetchSize(fetchSize);
            }
            int maxRows = jdbcTemplate.getMaxRows();
            if (maxRows > 0) {
                stmt.setMaxRows(maxRows);
            }
            DataSourceUtils.applyTimeout(stmt, getDataSource(), jdbcTemplate.getQueryTimeout());
        }

        @SuppressWarnings("rawtypes")
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Invocation on ConnectionProxy interface coming in...

            if (method.getName().equals("equals")) {
                // Only consider equal when proxies are identical.
                return (proxy == args[0]);
            } else if (method.getName().equals("hashCode")) {
                // Use hashCode of PersistenceManager proxy.
                return System.identityHashCode(proxy);
            } else if (method.getName().equals("unwrap")) {
                if (((Class) args[0]).isInstance(proxy)) {
                    return proxy;
                }
            } else if (method.getName().equals("isWrapperFor")) {
                if (((Class) args[0]).isInstance(proxy)) {
                    return true;
                }
            } else if (method.getName().equals("close")) {
                // Handle close method: suppress, not valid.
                return null;
            } else if (method.getName().equals("isClosed")) {
                return false;
            } else if (method.getName().equals("getTargetConnection")) {
                // Handle getTargetConnection method: return underlying Connection.
                return this.target;
            }

            // Invoke method on target Connection.
            try {
                Object retVal = method.invoke(this.target, args);

                // If return value is a JDBC Statement, apply statement settings
                // (fetch size, max rows, transaction timeout).
                if (retVal instanceof Statement) {
                    applyStatementSettings(jdbcTemplate, ((Statement) retVal));
                }

                return retVal;
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

}
