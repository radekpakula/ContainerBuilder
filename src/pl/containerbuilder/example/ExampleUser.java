package pl.containerbuilder.example;

import java.util.Date;

import pl.containerbuilder.Col;
import pl.containerbuilder.InnerCol;

public class ExampleUser {

	@Col
	private Long id;
	@Col("User name")
	private String name;
	@Col
	private int age;
	@Col
	private boolean gender;
	@Col(value = "User type",visible=false)
	private UserTypeEnum userType;
	@Col(value="Format time")
	private Date date;
	
	@InnerCol
	private ExampleGroup exampleGroup;
	
	@Col(value="Additional name")
	private String getAnotherName(){
		return getName()+" additional";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public UserTypeEnum getUserType() {
		return userType;
	}
	public void setUserType(UserTypeEnum userType) {
		this.userType = userType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ExampleGroup getExampleGroup() {
		return exampleGroup;
	}

	public void setExampleGroup(ExampleGroup exampleGroup) {
		this.exampleGroup = exampleGroup;
	}
}
