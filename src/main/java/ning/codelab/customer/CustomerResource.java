package ning.codelab.customer;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ning.codelab.customer.persist.CustomerPersistance;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Path("cust")
@Singleton
public class CustomerResource {
	
	private final CustomerPersistance manager;
	
	@Inject
	public CustomerResource(CustomerPersistance manager){
		this.manager = manager;
	}

	private void checkIfCustomerExists(int id) {
		Customer customerWithId = manager.getCustomerWithId(id);
		if(customerWithId == null){
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Customer with id " + id + " not found").build());
		}
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer retrieveCustomer(final @PathParam("id") int id){
		 checkIfCustomerExists(id);
		 return manager.getCustomerWithId(id);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Customer> retrieveCustomer(){
		 return manager.getAllCustomers();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCustomer(Customer customer){
		Customer customerWithId = manager.getCustomerWithId(customer.getId());
		if(customerWithId != null){
			throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Customer with id " + customer.getId() +" already exists.").build());
		}
		
		manager.addCustomer(customer);
		return Response.status(Status.CREATED).entity("Customer with Id " + customer.getId() + " created successfully").build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomer(final @PathParam("id") int id, Customer cust){
		checkIfCustomerExists(id);
		manager.updateCustomer(id, cust);
		return Response.ok("Customer with Id " + id + " udpated successfully").build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteCustomer(final @PathParam("id") int id){
		checkIfCustomerExists(id);
		manager.deleteCustomer(id);
		return Response.ok("Customer with Id " + id + " deleted successfully").build();
	}
	
}
