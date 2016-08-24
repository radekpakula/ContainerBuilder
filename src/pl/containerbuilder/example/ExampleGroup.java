package pl.containerbuilder.example;

import pl.containerbuilder.Col;

public class ExampleGroup {

	@Col("Group name")
	private String name;
	@Col("Group active")
	private boolean active;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
