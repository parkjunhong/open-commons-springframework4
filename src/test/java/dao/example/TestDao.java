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
 *
 * This file is generated under this project, "open-commons-springframework4".
 *
 * Date  : 2019. 3. 29. 오후 4:46:54
 *
 * Author: Park_Jun_Hong_(fafanmama_at_naver_com)
 * 
 */

package dao.example;

import javax.sql.DataSource;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import open.commons.Result;
import open.commons.springframework4.dao.AbstractGenericDao;

/**
 * 
 * @since 2019. 3. 29.
 * @author Park_Jun_Hong_(fafanmama_at_naver_com)
 * @version _._._
 */
public class TestDao extends AbstractGenericDao {

    /**
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2019. 3. 29.		박준홍			최초 작성
     * </pre>
     *
     * @since 2019. 3. 29.
     * @version _._._
     */
    public TestDao() {
    }

    public Result<Object> getPlant(String plantID) {

        String query = getQuery("statisticsDao.select.static.info");

        if (logger.isDebugEnabled()) {
            logger.debug("Query: " + query + ", " + String.format("plantID: %s", plantID));
        }

        return getObject(query, pstmt -> pstmt.setString(1, plantID), Object.class);
    }

    public Result<Object> getPlants() {

        String query = getQuery("statisticsDao.select.static.info.all");

        if (logger.isDebugEnabled()) {
            logger.debug("Query: " + query);
        }

        return getObject(query, Object.class);
    }

    /**
     * @see open.commons.springframework4.dao.AbstractGenericDao#setDataSource(javax.sql.DataSource)
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @see open.commons.springframework4.dao.AbstractGenericDao#setQuerySource(org.springframework.context.support.ReloadableResourceBundleMessageSource)
     */
    @Override
    public void setQuerySource(ReloadableResourceBundleMessageSource querySource) {
        this.querySource = querySource;
    }
}
