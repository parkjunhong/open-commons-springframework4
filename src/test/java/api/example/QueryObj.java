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
 * Date  : 2019. 3. 29. 오후 3:15:54
 *
 * Author: Park_Jun_Hong_(fafanmama_at_naver_com)
 * 
 */

package api.example;

public class QueryObj implements QueryParamObj, QueryResultObj {
    private String name;
    private String cost;
    private String date;

    public QueryObj() {
    }

    public String getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

}
