package ning.codelab.customer.config;

import org.skife.config.Config;
import org.skife.config.Default;

public interface DBConfig {

	@Config("connection.url")
	public abstract String getUrl();

	@Config("connection.dbase")
	public abstract String getDataBase();

	@Config("connection.user")
	@Default("")
	public abstract String getUser();

	@Config("connection.pass")
	@Default("")
	public abstract String getPass();
}
