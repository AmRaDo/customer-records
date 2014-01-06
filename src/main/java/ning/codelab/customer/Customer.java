package ning.codelab.customer;

import org.codehaus.jackson.annotate.JsonProperty;

public class Customer {

	private int id;
	private String name;
	private String address;

	@JsonProperty
	public int getId() {
		return id;
	}

	@JsonProperty
	public void setId(int id) {
		this.id = id;
	}

	@JsonProperty
	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	public String getAddress() {
		return address;
	}

	@JsonProperty
	public void setAddress(String address) {
		this.address = address;
	}
}
