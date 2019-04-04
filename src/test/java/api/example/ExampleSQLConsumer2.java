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
 * Date  : 2019. 4. 4. 오후 12:33:49
 *
 * Author: Park_Jun_Hong_(fafanmama_at_naver_com)
 * 
 */

package api.example;

import java.sql.PreparedStatement;
import java.util.function.Function;

import org.junit.Test;

import open.commons.function.SQLConsumer;
import open.commons.springframework4.dao.AbstractGenericDao;

import dao.example.TestDao;

/**
 * 
 * @since 2019. 4. 4.
 * @author Park_Jun_Hong_(fafanmama_at_naver_com)
 * @version _._._
 */
public class ExampleSQLConsumer2 {

    static final Function<QueryParamObj, SQLConsumer<PreparedStatement>> PROVIDER = param -> pstmt -> {
        pstmt.setString(1, param.getName());
        pstmt.setString(1, param.getCost());
        pstmt.setString(1, param.getDate());
    };

    @Test
    public void ok() {
        System.out.println("OK");
    }

    @Test
    public void testRun() {
        // 쿼리 파라미터.
        QueryParamObj param = new QueryObj();

        // #1. java.util.function.Function 을 이용하는 법
        SQLConsumer<PreparedStatement> setter = PROVIDER.apply(param);
        // #2. 메소드를 이용하는 법
        setter = create(param);

        AbstractGenericDao dao = new TestDao(); // ...

        // insert/update/delete
        String query = "INSERT ...";
        dao.executeUpdate(query, setter);

        // select
        query = "SELECT ...";
        dao.getList(query, setter, QueryObj.class);

        // select
        query = "SELECT ...";
        dao.getObject(query, setter, QueryObj.class);

    }

    static SQLConsumer<PreparedStatement> create(QueryParamObj param) {
        return pstmt -> {
            pstmt.setString(1, param.getName());
            pstmt.setString(1, param.getCost());
            pstmt.setString(1, param.getDate());
        };
    }

    public static void main(String[] args) {
        // 쿼리 파라미터.
        QueryParamObj param = new QueryObj();

        // #1. java.util.function.Function 을 이용하는 법
        SQLConsumer<PreparedStatement> setter = PROVIDER.apply(param);
        // #2. 메소드를 이용하는 법
        setter = create(param);

        AbstractGenericDao dao = new TestDao(); // ...

        // insert/update/delete
        String query = "INSERT ...";
        dao.executeUpdate(query, setter);

        // select
        query = "SELECT ...";
        dao.getList(query, setter, QueryObj.class);

        // select
        query = "SELECT ...";
        dao.getObject(query, setter, QueryObj.class);
    }

}
