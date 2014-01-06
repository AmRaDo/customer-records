package ning.codelab.customer.persist.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ning.codelab.customer.Customer;
import ning.codelab.customer.persist.CustomerPersistance;

public class CustomerPersistanceDbImpl implements CustomerPersistance{

	private Map<Integer, Customer> db= new HashMap<Integer, Customer>();
	
	@Override
	public Customer getCustomerWithId(int id) {
		return db.get(id);
	}

	@Override
	public int addCustomer(Customer customer) {
		db.put(customer.getId(), customer);
		return 1;
	}

	@Override
	public int updateCustomer(int id, Customer customer) {
		db.put(customer.getId(), customer);
		return 1;
	}

	@Override
	public int deleteCustomer(int id) {
		db.remove(id);
		return 1;
	}

	@Override
	public List<Customer> getAllCustomers() {
		return new ArrayList<Customer>(db.values());
	}

	@Override
	public int clear() {
		db.clear();
		return 1;
	}

}
