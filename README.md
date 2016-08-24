# ContainerBuilder
Tools addon for Vaadin

Addon use annotation to build container for another component such a table,grid,filtertable.
Can build couple column schema basic on single object or related objects.

Auto bind object or list of object to container based on annotation
Value returned by method 'getId()' from primary object will be mark as id of created container item.
# Simple usage
	//Create ctBuilder object 
	CtBuilder<ExampleUser> ctBuilder = new CtBuilder<ExampleUser>(ExampleUser.class);
	
	//init container builder
	IndexedContainer ct = ctBuilder.buildIContainer();
	//or hierarchical
	HierarchicalContainer hc = ctBuilder.buildHContainer();
	
	//or simple getContainer. Default is indexedContainer
	new Table("extra container",ctBuilder.getContainer());
	
	//bind list of generic object to created container
	List<ExampleUser> listOfUsers=getUserList();
	ctBuilder.bind(listOfUsers);
	
	//get visible col as array
	table.setVisibleColumns(ctBuilder.getVisibleColumn());
	
	//create container witch extra column
	Column BUTTON = new Column("My extra button",Button.class);
	Column EXTRANAME = new Column("My extra name",String.class);
	CtBuilder<ExampleUser> ctBuilder = new CtBuilder<ExampleUser>(ExampleUser.class,new Column[]{BUTTON,EXTRANAME});

	//bind value for default and extra column
	ctBuilder.bind(listOfUsers,new ItemBindListener<ExampleUser>() {
			@Override
			public void bindItem(Item item, ExampleUser t, Object itemId) {
				ctBuilder.setProperty(itemId,BUTTON,new Button("My button"));
				ctBuilder.setProperty(itemId,EXTRANAME,"My extra name");
			}
	});
	
As default created item has id from generic object. So if object has method called 'getId()' value returned by this method will by id of created container item.
If you have this method but you dont want to use this for identified items, you must provide own BinderIdListener.
 For example:	
	
	ctBuilder.bind(listOfUsers,null,new BinderIdListener<ExampleUser>() {
			@Override
			public Object getId(ExampleUser item) {
				return generateItemIdForThisObject(item);
			}
		});	

#Col example usage
	//default usage. The name of column will be name of property
	@Col
	private Long id;
	
	//set own name for column by define property 'value()'
	@Col("User name")
	private String name;
	
	//set position order (default is 0)
	@Col(value="User type",position=1)	
	private UserTypeEnum userType;
	
	//set column visibility. This can by use for store property in container but not show in table
	@Col(value="Format time",visible=false)
	private Date date;
	
	//set schema name for other container.This property will be read when you create CtBuilder with this schema name
	@Col(value="Other schema",schema="another")
	private BigDecimal price;
	
	//read col from realated object
	@InnerCol
	private ExampleGroup exampleGroup;
	
	//read col from realated object whith specific schema name
	@InnerCol(schema="another")
	private ExampleGroup exampleGroup;
	
	//create column from getter
	@Col(value="Additional name")
	private String getAnotherName(){
		return getName()+" additional";
	}	
	
	--- Getters and setters -----
#Col description
	String value() default "";  //name of column
	int position() default 0;  //order column in container
	boolean visible() default true; //property visible. Use for get list of visible column
	String schema() default "";  //schema name. Default is empty.
#InnerCol description
	String schema() default "";  //schema name for col in related object
# Relase note
	1.0.0
		- Beta