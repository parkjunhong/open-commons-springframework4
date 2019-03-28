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
