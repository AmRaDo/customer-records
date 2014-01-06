package ning.codelab.customer.servlet;

import java.util.HashMap;
import java.util.Map;

import ning.codelab.customer.CustomerResource;
import ning.codelab.customer.db.CustomerPersistanceDbImpl;
import ning.codelab.customer.persist.CustomerPersistance;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class CustomerServletModule extends JerseyServletModule {

	private static final String JERSEY_API_JSON_POJO_MAPPING_FEATURE = "com.sun.jersey.api.json.POJOMappingFeature";
	private static final String JERSEY_CONFIG_PROPERTY_PACKAGES = "com.sun.jersey.config.property.packages";
	private static final String CUSTOMER_RESOURCES_PACKAGE = "ning.codelab.customer";

	@Override
	protected void configureServlets() {
		bind(CustomerResource.class);
		bind(CustomerPersistance.class).to(CustomerPersistanceDbImpl.class).asEagerSingleton();
		final Map<String, String> params = new HashMap<String, String>();

        params.put(JERSEY_CONFIG_PROPERTY_PACKAGES, CUSTOMER_RESOURCES_PACKAGE);
        params.put(JERSEY_API_JSON_POJO_MAPPING_FEATURE, "true");

		serve("/*").with(GuiceContainer.class);
	}
}