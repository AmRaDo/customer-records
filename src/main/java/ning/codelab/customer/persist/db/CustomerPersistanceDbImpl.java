package ning.codelab.customer.persist.db;

import java.util.List;

import ning.codelab.customer.Customer;
import ning.codelab.customer.config.DBConfig;
import ning.codelab.customer.persist.CustomerPersistance;

import org.skife.jdbi.v2.DBI;

import com.google.inject.Inject;
//TODO: licensing information.
public class CustomerPersistanceDbImpl implements CustomerPersistance {

	private final DBConfig db;
	private CustomerDAO dao;

	
	@Inject
	public CustomerPersistanceDbImpl(DBConfig dbase) {
		this.db=dbase;
		makeConnection();
		createTable();
	}
	
	private void makeConnection() {
		DBI dbAccess = new DBI(db.getUrl()+db.getDataBase(), db.getUser(), db.getPass());
		this.dao = dbAccess.open(CustomerDAO.class);
	}

	private void createTable() {
		try {
			dao.checkCount();
		} catch (Exception e) {
			dao.create();
		}
	}

	@Override
	public Customer getCustomerWithId(int id) {
		return dao.findById(id);
	}

	@Override
	public int addCustomer(Customer customer) {
			dao.insert(customer.getId(), customer.getName(), customer.getAddress());
	 return dao.checkCount();
	}

	@Override
	public int updateCustomer(int id, Customer customer) {
		String name = customer.getName();
		String address = customer.getAddress();
		dao.update(id, name, address);
		return dao.checkCount();
	}

	@Override
	public int deleteCustomer(int id) {
		Customer customerWithId = dao.findById(id);
		if(customerWithId!=null)
			dao.delete(id);
		return dao.checkCount();
	}

	@Override
	public List<Customer> getAllCustomers() {
		return dao.showAll();
	}

	@Override
	public int clear() {
		dao.clearAllRecords();
		return dao.checkCount();
	}
	
	public void close()	{
		dao.close(); //Closing Handlers
	}
}
