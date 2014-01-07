package ning.codelab.customer.persist.db;

import java.util.List;

import ning.codelab.customer.Customer;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface CustomerDAO {

	@SqlUpdate("create table customer (c_id int primary key, c_name varchar(30) , c_address varchar(50))")
	void create();

	@SqlQuery("select count(*) from customer")
	int checkCount();

	@SqlUpdate("insert into customer (c_id, c_name,c_address) values (:c_id, :c_name, :c_address)")
	void insert(@Bind("c_id") int id, @Bind("c_name") String name, @Bind("c_address") String addr);

	@SqlUpdate("update customer set c_name=:c_name , c_address=:c_address where c_id =:c_id")
	void update(@Bind("c_id") int id, @Bind("c_name") String name, @Bind("c_address") String addr);

	@SqlUpdate("delete from customer where c_id=:c_id")
	void delete(@Bind("c_id") int id);

	@Mapper(CustomerMapper.class)
	@SqlQuery("select * from customer where c_id = :c_id")
	Customer findById(@Bind("c_id") int id);

	@Mapper(CustomerMapper.class)
	@SqlQuery("select * from customer")
	List<Customer> showAll();

	@SqlUpdate("delete from customer")
	void clearAllRecords();

	void close();
}
