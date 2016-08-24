package pl.containerbuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.IndexedContainer;

public class CtBuilder<DTO> implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_SCHEMA="";
	private final Column[] column;
	private final Class<?> clazz;
	private final String schema;
	private Map<Object, DTO> bindItem;
	private ColumnBinder<DTO> bb;
	private Container container;
	
	public CtBuilder(Class<?> clazz){
		this.column=ColumnReader.readColumnsAsArray(clazz);
		this.clazz=clazz;
		this.schema="";
		
	}
	public CtBuilder(Class<?> clazz,Column[] additionalColumn){
		column=joinCols(ColumnReader.readColumnsAsArray(clazz,""),additionalColumn);
		this.clazz=clazz;
		this.schema=DEFAULT_SCHEMA;
	}
	public CtBuilder(Class<?> clazz,String schema){
		column=ColumnReader.readColumnsAsArray(clazz,schema);
		this.clazz=clazz;
		this.schema=schema;
	}
	public CtBuilder(Class<?> clazz,Column[] additionalColumn,String schema){
		column=joinCols(ColumnReader.readColumnsAsArray(clazz,schema),additionalColumn);
		this.clazz=clazz;
		this.schema=schema;
	}
	public CtBuilder(List<Column> cols){
		this.column=cols.toArray(new Column[cols.size()]);
		this.clazz=null;
		this.schema=DEFAULT_SCHEMA;
	}
	public CtBuilder(Column[] cols){
		this.column=cols;
		this.clazz=null;
		this.schema=DEFAULT_SCHEMA;
	}
	public IndexedContainer buildIContainer(){
		IndexedContainer ct = new IndexedContainer();
		for(Column c : column){
			ct.addContainerProperty(c.getName(), c.getType(),c.getDefaultValue());
		}
		container=ct;
		return ct;
	}	
	public HierarchicalContainer buildHContainer(){
		HierarchicalContainer ct= new HierarchicalContainer();
		for(Column c : column){
			ct.addContainerProperty(c.getName(), c.getType(),c.getDefaultValue());
		}
		container=ct;
		return ct;
	}
	
	private static Column[] joinCols(Column[] readColumnsAsArray, Column[] additionalCols) {
		if(additionalCols!=null){
			List<Column> cc=new ArrayList<Column>();
			cc.addAll(Arrays.asList(readColumnsAsArray));
			cc.addAll(Arrays.asList(additionalCols));
			cc.sort(new Comparator<Column>() {
				public int compare(Column o1, Column o2) {
					return o1.getPosition()-o2.getPosition();
				}
			});
			return cc.toArray(new Column[cc.size()]);
		}
		return readColumnsAsArray;
	}
	public Column[] getColumn() {
		return column;
	}
	
	
	public boolean bind(List<DTO> list) {
		return bind(list,true);
	}
	public boolean bind(List<DTO> list,boolean resetContainer) {
		return bind(list,null,resetContainer);
	}
	public boolean bind(List<DTO> list, ItemBindListener<DTO> binder) {
		return bind(list,binder,true);
	}
	public boolean bind(List<DTO> list, ItemBindListener<DTO> binder,boolean resetContainer) {
		return bind(list,binder,resetContainer,null);
	}
	public boolean bind(List<DTO> list,ItemBindListener<DTO> binder,BinderIdListener<DTO> binderIdListener) {
		return bind(list,binder,true,binderIdListener);
	}
	/**
	 * Uzywane do ustawienia dodatkowych kolumn nie uwzglednionych adnotacjami @Cols
	 * @param list 
	 * @param binder
	 * @return
	 */
	public boolean bind(List<DTO> list, ItemBindListener<DTO> binder,boolean resetContainer,BinderIdListener<DTO> binderIdListener) {
		if(list==null || container==null){
			return false;
		}
		if(list.isEmpty()){
			container.removeAllItems();
		}
		if(bb==null){
			if(clazz==null){
				return false;
			}
			bb = new ColumnBinder<DTO>(clazz, container,binderIdListener,schema);
			if(binder!=null){
				bb.setBinder(binder);
			}
		}
		bindItem = bb.bind(list,resetContainer);
		return true;
	}
	public void bind(DTO e, ItemBindListener<DTO> binder) {
		if(bb==null){
			bb = new ColumnBinder<DTO>(clazz,container,schema);
		}
		if(binder!=null){
			bb.setBinder(binder);
		}
		bindItem = bb.bind(e);
	}
	public void bind(DTO e) {
		this.bind(e, null);
	}
	public DTO getItemDTO(Object id){
		if(bindItem!=null){
			return bindItem.get(id);
		}
		return null;
	}
	public Container getContainer() {
		if(container==null){
			container=buildIContainer();
		}
		return container;
	}
	public void setContainer(Container container) {
		this.container = container;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public String getSchema() {
		return schema;
	}
	public void setProperty(Object itemId, Column col, Object value) {
		setProperty(itemId, col.getName(),value);
	}
	@SuppressWarnings("unchecked")
	public void setProperty(Object itemId, String propertyId, Object value) {
		container.getContainerProperty(itemId, propertyId).setValue(value);
	}
	public Object[] getVisibleColumn() {
		List<String> visibleCol = new ArrayList<>();
		for (Column c : column) {
			if(c.isVisible()){
				visibleCol.add(c.getName());
			}
		}
		return visibleCol.toArray();
	}
}
