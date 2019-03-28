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
 * Date  : 2019. 3. 28. 오후 5:32:00
 *
 * Author: Park_Jun_Hong_(fafanmama_at_naver_com)
 * 
 */

package open.commons.springframework4.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

/**
 * 
 * @since 2019. 3. 28.
 * @author Park_Jun_Hong_(fafanmama_at_naver_com)
 * @version _._._
 */
public class DefaultPreparedStatementCallback<T> implements PreparedStatementCallback<T> {

    /**
     * <br>
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 28.		박준홍			최초 작성
     * </pre>
     *
     * @since 2019. 3. 28.
     * @version _._._
     */
    public DefaultPreparedStatementCallback() {
    }

    /**
     * @see org.springframework.jdbc.core.PreparedStatementCallback#doInPreparedStatement(java.sql.PreparedStatement)
     */
    @Override
    public T doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
        return null;
    }

}
