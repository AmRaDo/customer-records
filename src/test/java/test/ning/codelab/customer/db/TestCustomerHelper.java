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
package test.ning.codelab.customer.db;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ning.codelab.customer.Customer;
import ning.codelab.customer.config.DBConfig;
import ning.codelab.customer.persist.db.CustomerPersistanceDbImpl;
import ning.codelab.customer.servlet.CustomerServletModule;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestCustomerHelper
{
    private CustomerMysqlHelper helper;

    private CustomerPersistanceDbImpl db;

    @BeforeClass(groups = { "slow", "database" })
    public void startDB() throws Exception
    {
        helper = new CustomerMysqlHelper();
        helper.startMysql();
        helper.initDb();

        System.setProperty("connection.url", helper.getJdbcUrl());
        System.setProperty("connection.user", CustomerMysqlHelper.USERNAME);
        System.setProperty("connection.pass", CustomerMysqlHelper.PASSWORD);

        /*
         * After Creation of specific temporary mysql, extract the necessary parameter or infrastructure resources & inject it
         * appropriately
         */

        Injector inj = Guice.createInjector(new CustomerServletModule());
        inj.injectMembers(this);
        DBConfig db_conf = inj.getInstance(DBConfig.class);
        this.db = new CustomerPersistanceDbImpl(db_conf);
    }

    @Test
    public void testInsertDB()
    {
        Customer c = new Customer();
        c.setId(1);
        c.setName("testName");
        c.setAddress("testAddress");
        Assert.assertEquals(1, db.addCustomer(c));
    }

    @Test(dependsOnMethods = { "testInsertDB" })
    public void testUpdateDB()
    {
        Customer c = new Customer();
        c.setId(1);
        c.setName("updateName");
        c.setAddress("updateAddress");
        Assert.assertEquals(1, db.updateCustomer(1, c));
    }

    @Test(dependsOnMethods = { "testUpdateDB" })
    public void testFindDB()
    {
        Customer c = db.getCustomerWithId(1);
        Assert.assertNotNull(c);
        Assert.assertEquals(1, c.getId());
        Assert.assertEquals("updateName", c.getName());
        Assert.assertEquals("updateAddress", c.getAddress());
    }

    @Test(dependsOnMethods = { "testFindDB" })
    public void testDeleteDB()
    {
        Assert.assertEquals(0, db.deleteCustomer(1));
    }

    @Test(dependsOnMethods = { "testFindDB" })
    public void testDeleteAllDB()
    {
        Assert.assertEquals(0, db.clear());
    }

    @AfterClass(alwaysRun = true, groups = { "slow", "database" })
    public void stopDB() throws Exception
    {
        helper.stopMysql();
    }

}
