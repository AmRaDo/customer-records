package ning.codelab.customer.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.skife.config.ConfigurationObjectFactory;

import com.google.inject.Provider;

public class ConfigProvider implements Provider<DBConfig> {

	@Override
	public DBConfig get() {
		Properties props = new Properties();
		try {
		FileReader reader = new FileReader("Database.properties");
		props.load(reader);
		}
		catch(IOException io) {
		}
		ConfigurationObjectFactory c = new ConfigurationObjectFactory(props);
		return c.build(DBConfig.class);
	}

}
