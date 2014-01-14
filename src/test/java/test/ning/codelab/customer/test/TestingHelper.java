/*

 * Copyright 2010-2013 Ning, Inc.

 *

 * Ning licenses this file to you under the Apache License, version 2.0

 * (the "License"); you may not use this file except in compliance with the

 * License.  You may obtain a copy of the License at:

 *

 *    http://www.apache.org/licenses/LICENSE-2.0

 *

 * Unless required by applicable law or agreed to in writing, software

 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT

 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the

 * License for the specific language governing permissions and limitations

 * under the License.

 */
package test.ning.codelab.customer.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestingHelper
{

    private CollectorMysqlTestingHelper helper;

    @BeforeClass(groups = { "slow", "database" })
    @Test
    public void startDB() throws Exception
    {
        helper = new CollectorMysqlTestingHelper();
        helper.startMysql();
        helper.initDb();

        System.setProperty("collector.spoolWriter.jdbc.url", helper.getJdbcUrl());
        System.setProperty("collector.spoolWriter.jdbc.user", CollectorMysqlTestingHelper.USERNAME);
        System.setProperty("collector.spoolWriter.jdbc.password", CollectorMysqlTestingHelper.PASSWORD);
        //Guice.createInjector(new DBConfigModule()).injectMembers(this);

    }

    @AfterClass(alwaysRun = true, groups = { "slow", "database" })
    public void stopDB() throws Exception
    {
        helper.stopMysql();
    }
}
