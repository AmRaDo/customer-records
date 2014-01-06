package ning.codelab.customer.servlet;

import ning.codelab.customer.CustomerResource;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class CustomerServletModule extends JerseyServletModule {

	@Override
	protected void configureServlets() {
		bind(CustomerResource.class);
		serve("/*").with(GuiceContainer.class);
	}
}