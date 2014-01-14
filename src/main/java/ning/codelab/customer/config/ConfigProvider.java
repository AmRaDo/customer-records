package ning.codelab.customer.config;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.skife.config.ConfigurationObjectFactory;
import com.google.inject.Provider;

public class ConfigProvider implements Provider<DBConfig> {

    private static Logger log = LoggerFactory.getLogger(ConfigProvider.class);
    
	@Override
	public DBConfig get() {
	    
		Properties props = new Properties();
		try {
		FileReader reader = new FileReader("Database.properties");
		props.load(reader);
		}
		catch(IOException io) {
		}
		
		log.info("------- URL--------- : " + System.getProperty("connection.url"));
		log.info("------- USER ------- : " + System.getProperty("connection.user"));
		log.info("------- PASS ------- : " + System.getProperty("connection.pass"));
		ConfigurationObjectFactory c = new ConfigurationObjectFactory(props);
		return c.build(DBConfig.class);
	}

}
