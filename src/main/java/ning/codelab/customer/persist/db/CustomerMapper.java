package ning.codelab.customer.persist.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import ning.codelab.customer.Customer;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class CustomerMapper implements ResultSetMapper<Customer>{

	@Override
	public Customer map(int index, ResultSet rs, StatementContext ctxt)	throws SQLException {
		Customer customer = new Customer();
		customer.setName(rs.getString("c_name"));
		customer.setId(rs.getInt("c_id"));
		customer.setAddress(rs.getString("c_address"));
		return customer;
	}
}
