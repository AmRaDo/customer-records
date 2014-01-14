package test.ning.codelab.customer.db;

import java.io.File;
import java.util.List;
import junit.framework.Assert;
import ning.codelab.customer.Customer;
import ning.codelab.customer.persist.db.CustomerDAO;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;
import org.unitils.dbunit.annotation.DataSet;

public class CustomerDaoTest extends UnitilsTestNG
{
    private CustomerDAO dao;
    private static final String H2_URL = "jdbc:h2:test1";
    private static final String USER = "";
    private static final String PASSWORD = "";

    @BeforeClass
    public void testInit() throws Exception
    {
        DBI dbi = new DBI(H2_URL, USER, PASSWORD);
        Handle h = dbi.open();
        this.dao = h.attach(CustomerDAO.class);
        dao.create();
    }

    @Test
    public void testInsert() throws Exception
    {
        int rowAffected = dao.insert(6, "123", "456");
        Assert.assertEquals(1, rowAffected);
    }

    @Test
    @DataSet("/CustomerDaoTest.xml")
    public void testCount() throws Exception
    {
        int count = dao.checkCount();
        Assert.assertEquals(2, count);
    }

    @Test
    @DataSet("/CustomerDaoTest.xml")
    public void testUpdate() throws Exception
    {
        int rowAffected = dao.update(2, "nameUpdated2", "addressUpdated2");
        Assert.assertEquals(1, rowAffected);
    }

    @Test
    @DataSet("/CustomerDaoTest.xml")
    public void testFindByID() throws Exception
    {
        Customer c = dao.findById(2);
        Assert.assertEquals(2, c.getId());
        Assert.assertEquals("user2", c.getName());
        Assert.assertEquals("address2", c.getAddress());
    }

    @Test
    @DataSet("/CustomerDaoTest.xml")
    public void testShowAll() throws Exception
    {
        List<Customer> lst = dao.showAll();
        Assert.assertNotNull(lst);
        Customer c1 = lst.get(0);
        Assert.assertEquals(1, c1.getId());
        Assert.assertEquals("user1", c1.getName());
        Assert.assertEquals("address1", c1.getAddress());

        Customer c2 = lst.get(1);
        Assert.assertEquals(2, c2.getId());
        Assert.assertEquals("user2", c2.getName());
        Assert.assertEquals("address2", c2.getAddress());

    }

    @AfterTest
    public void cleanUp()
    {
        File f = new File("test1.h2.db");
        if (f.exists())
            f.deleteOnExit();
    }
}
