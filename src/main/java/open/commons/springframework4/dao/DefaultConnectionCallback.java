/* 
 * MIT License
 * 
 * Copyright (c) 2019 Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * This file is generated under this project, "open-commons-springframework4".
 *
 * Date  : 2019. 3. 28. 오후 4:25:43
 *
 * Author: Park_Jun_Hong_(fafanmama_at_naver_com)
 * 
 */

package open.commons.springframework4.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

import open.commons.database.ConnectionCallbackBroker;
import open.commons.database.IConnectionCallbackSetter;

/**
 * DBMS 연결 후 쿼리를 수행하는 객체.
 * 
 * @since 2019. 3. 28.
 * @author Park_Jun_Hong_(fafanmama_at_naver_com)
 * @version 0.1.0
 */
public class DefaultConnectionCallback implements ConnectionCallback<Integer> {

    protected Logger logger = LogManager.getLogger(getClass());

    private final ConnectionCallbackBroker broker;

    /**
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     * 
     * @param broker
     *            콜백처리 객체.
     *
     * @since 2019. 3. 28.
     * @version 0.1.0
     */
    public DefaultConnectionCallback(ConnectionCallbackBroker broker) {
        this.broker = broker;
    }

    /**
     * @see org.springframework.jdbc.core.ConnectionCallback#doInConnection(java.sql.Connection)
     */
    @Override
    public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
        int count = 0;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(broker.getQuery());

            IConnectionCallbackSetter setter = broker.getSetter();

            if (setter != null) {
                setter.set(stmt);
            }

            count = stmt.executeUpdate();

        } catch (Exception e) {
            logger.warn(e.getLocalizedMessage(), e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return count;
    }

    /**
     * 콜백처리 객체를 제공한다. <br>
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
     * @author Park_Jun_Hong_(fafanmama_at_naver_com)
     * @version 0.1.0
     */
    public ConnectionCallbackBroker getBroker() {
        return this.broker;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DefaultConnectionCallback [broker=");
        buffer.append(broker);
        buffer.append("]");
        return buffer.toString();
    }
}
