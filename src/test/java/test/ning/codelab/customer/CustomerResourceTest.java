package test.ning.codelab.customer;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

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
		testCustomer.setId(1);
		testCustomer.setName("testName");
		testCustomer.setAddress("testAddress");
		expect(manager.getCustomerWithId(1)).andReturn(testCustomer).anyTimes();
		replay(manager);
		Customer retrieveCustomer = resource.retrieveCustomer(1);

		Assert.assertNotNull(retrieveCustomer);
		Assert.assertEquals(retrieveCustomer.getId(), 1);
		Assert.assertEquals(retrieveCustomer.getName(), "testName");
		Assert.assertEquals(retrieveCustomer.getAddress(), "testAddress");
	}
	
	@Test
	public void testCreateNewCustomer(){
		Customer testCustomer = new Customer();
		testCustomer.setId(1);
		testCustomer.setName("testName");
		testCustomer.setAddress("testAddress");
		expect(manager.getCustomerWithId(1)).andReturn(null).anyTimes();
		replay(manager);
		Response createCustomer = resource.createCustomer(testCustomer);
		Assert.assertNotNull(createCustomer);
		Assert.assertEquals(createCustomer.getStatus(), Status.CREATED.getStatusCode());
	}
	
	@Test(expectedExceptions = WebApplicationException.class)
	public void testCreateExistingCustomer(){
		Customer testCustomer = new Customer();
		testCustomer.setId(1);
		testCustomer.setName("testName");
		testCustomer.setAddress("testAddress");
		expect(manager.getCustomerWithId(1)).andReturn(testCustomer).anyTimes();
		replay(manager);
		resource.createCustomer(testCustomer);
	}
	
	@Test
	public void testUpdatePresentCustomer()
	{
		Customer cust = new Customer();
		cust.setId(1);
		cust.setName("UpdatedName");
		cust.setAddress("updatedAddress");
		expect(manager.getCustomerWithId(1)).andReturn(cust).anyTimes();
		replay(manager);
		Response updateCustomer = resource.updateCustomer(cust.getId(), cust);
		Assert.assertNotNull(updateCustomer);
		Assert.assertEquals(updateCustomer.getStatus(), Status.OK.getStatusCode());
	}
	
	@Test (expectedExceptions = WebApplicationException.class)
	public void testUpdateAbsentCustomer()
	{
		Customer cust = new Customer();
		cust.setId(1);
		cust.setName("UpdatedName");
		cust.setAddress("updatedAddress");
		expect(manager.getCustomerWithId(1)).andReturn(null).anyTimes();
		replay(manager);
		resource.updateCustomer(cust.getId(), cust);
	}
}
