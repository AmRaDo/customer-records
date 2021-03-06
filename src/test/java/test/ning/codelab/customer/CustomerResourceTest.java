package test.ning.codelab.customer;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ning.codelab.customer.Customer;
import ning.codelab.customer.CustomerResource;
import ning.codelab.customer.persist.CustomerPersistance;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CustomerResourceTest {
	private CustomerPersistance manager;
	private CustomerResource resource;

	@BeforeClass
	private void setup() {
		manager = createNiceMock(CustomerPersistance.class);
		resource = new CustomerResource(manager);
	}

	@BeforeMethod
	private void resetMockManager() {
		EasyMock.resetToNice(manager);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void testRetrieveUnknownCustomer() {
		expect(manager.getCustomerWithId(1)).andReturn(null);
		replay(manager);
		resource.retrieveCustomer(1);
	}

	@Test
	public void testRetrieveCustomer() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("testName");
		testCustomer.setAddress("testAddress");
		expect(manager.getCustomerWithId(0)).andReturn(testCustomer).anyTimes();
		replay(manager);
		Customer retrieveCustomer = resource.retrieveCustomer(0);

		Assert.assertNotNull(retrieveCustomer);
		Assert.assertEquals(retrieveCustomer.getId(), 0);
		Assert.assertEquals(retrieveCustomer.getName(), "testName");
		Assert.assertEquals(retrieveCustomer.getAddress(), "testAddress");
	}

	@Test
	public void testCreateNewCustomer() {
		Customer testCustomer = new Customer();
		testCustomer.setId(1);
		testCustomer.setName("testName");
		testCustomer.setAddress("testAddress");
		expect(manager.getCustomerWithId(1)).andReturn(null).anyTimes();
		replay(manager);
		Response createCustomer = resource.createCustomer(testCustomer);
		Assert.assertNotNull(createCustomer);
		Assert.assertEquals(createCustomer.getStatus(),
				Status.CREATED.getStatusCode());
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void testCreateExistingCustomer() {
		Customer testCustomer = new Customer();
		testCustomer.setId(1);
		testCustomer.setName("testName");
		testCustomer.setAddress("testAddress");
		expect(manager.getCustomerWithId(1)).andReturn(testCustomer).anyTimes();
		replay(manager);
		resource.createCustomer(testCustomer);
	}

	@Test
	public void testUpdatePresentCustomer() {
		Customer cust = new Customer();
		cust.setId(1);
		cust.setName("UpdatedName");
		cust.setAddress("updatedAddress");
		expect(manager.getCustomerWithId(1)).andReturn(cust).anyTimes();
		replay(manager);
		Response updateCustomer = resource.updateCustomer(cust.getId(), cust);
		Assert.assertNotNull(updateCustomer);
		Assert.assertEquals(updateCustomer.getStatus(),
				Status.OK.getStatusCode());
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void testUpdateAbsentCustomer() {
		expect(manager.getCustomerWithId(1)).andReturn(null).anyTimes();
		replay(manager);
		resource.updateCustomer(1, new Customer());
	}

	@Test
	public void testDeletePresentCustomer() {
		Customer cust = new Customer();
		cust.setId(1);
		cust.setName("Name");
		cust.setAddress("Address");
		expect(manager.getCustomerWithId(1)).andReturn(cust).anyTimes();
		replay(manager);
		Response delCustomer = resource.deleteCustomer(cust.getId());
		Assert.assertNotNull(delCustomer);
		Assert.assertEquals(delCustomer.getStatus(), Status.OK.getStatusCode());
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void testDeleteAbsentCustomer() {
		expect(manager.getCustomerWithId(1)).andReturn(null).anyTimes();
		replay(manager);
		resource.deleteCustomer(1);
	}

	@Test
	public void retrieveAllCustomers() {
		Customer cust1 = new Customer();
		cust1.setId(1);
		cust1.setName("Name1");
		cust1.setAddress("Address1");

		Customer cust2 = new Customer();
		cust2.setId(2);
		cust2.setName("Name2");
		cust2.setAddress("Address2");

		expect(manager.getAllCustomers()).andReturn(Arrays.asList(cust1, cust2)).anyTimes();
		replay(manager);

		List<Customer> l = resource.retrieveCustomer();
		Assert.assertNotNull(l);
		Assert.assertEquals(2, l.size());

		Customer c = l.get(0);
		Assert.assertNotNull(c);
		Assert.assertEquals(1, c.getId());
		Assert.assertEquals("Name1", c.getName());
		Assert.assertEquals("Address1", c.getAddress());
	}

	@Test
	public void retrieveAbsentCustomers() {
		expect(manager.getCustomerWithId(1)).andReturn(null).anyTimes();
		replay(manager);
		List<Customer> l = resource.retrieveCustomer();
		Assert.assertNull(l);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateCreateCustomerInvalidId() {
		Customer testCustomer = new Customer();
		testCustomer.setId(-1);
		testCustomer.setName("Name");
		testCustomer.setAddress("Address");
		resource.createCustomer(testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateCreateCustomerNullName() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName(null);
		testCustomer.setAddress("Address");
		resource.createCustomer(testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateCreateCustomerEmptyName() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("");
		testCustomer.setAddress("Address");
		resource.createCustomer(testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateCreateCustomerBlankName() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("  \t");
		testCustomer.setAddress("Address");
		resource.createCustomer(testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateCreateCustomerNullAddress() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("name");
		testCustomer.setAddress(null);
		resource.createCustomer(testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateCreateCustomerEmptyAddress() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("name");
		testCustomer.setAddress("");
		resource.createCustomer(testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateCreateCustomerBlankAddress() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("name");
		testCustomer.setAddress("  \t");
		resource.createCustomer(testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateUpdateCustomerDifferentId() {
		Customer testCustomer = new Customer();
		testCustomer.setId(3);
		testCustomer.setName("Name");
		testCustomer.setAddress("Address");
		resource.updateCustomer(1, testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateUpdateCustomerInvalidId() {
		Customer testCustomer = new Customer();
		testCustomer.setId(-1);
		testCustomer.setName("Name");
		testCustomer.setAddress("Address");
		resource.updateCustomer(-1, testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateUpdateCustomerNullName() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName(null);
		testCustomer.setAddress("Address");
		resource.updateCustomer(0, testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateUpdateCustomerEmptyName() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("");
		testCustomer.setAddress("Address");
		resource.updateCustomer(0, testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateUpdateCustomerBlankName() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName(" \t");
		testCustomer.setAddress("Address");
		resource.updateCustomer(0, testCustomer);
	}
	
	@Test(expectedExceptions = WebApplicationException.class)
	public void validateUpdateCustomerNullAddress() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("name");
		testCustomer.setAddress(null);
		resource.updateCustomer(0, testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateUpdateCustomerEmptyAddress() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("name");
		testCustomer.setAddress("");
		resource.updateCustomer(0, testCustomer);
	}

	@Test(expectedExceptions = WebApplicationException.class)
	public void validateUpdateCustomerBlankAddress() {
		Customer testCustomer = new Customer();
		testCustomer.setId(0);
		testCustomer.setName("name");
		testCustomer.setAddress(" \t   ");
		resource.updateCustomer(0, testCustomer);
	}
}
