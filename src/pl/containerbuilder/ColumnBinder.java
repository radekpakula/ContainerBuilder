package pl.containerbuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.data.Container;
import com.vaadin.data.Item;


public class ColumnBinder<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private Container ct;
	private List<Column> cols;
	private BinderIdListener<T> binderIdListener;
	private ItemBindListener<T> itemBinder;
	private Map<Object,T> objectMap = new HashMap<Object,T>();
	
	public ColumnBinder(Class<?> z, Container ct,String schema) {
		this(z, ct, ColumnReader.readColumns(z,schema),null);
	}
	public ColumnBinder(Class<?> z,Container ct,List<Column> cols){
		this(z, ct, cols, null);
	}
	public ColumnBinder(Class<?> z,Container ct,BinderIdListener<T> binderIdListener,String schema){
		this(z,ct,ColumnReader.readColumns(z,schema),binderIdListener);
	}
	public ColumnBinder(Class<?> z,List<T> object,Container ct,String schema){
		this(z,ct,ColumnReader.readColumns(z,schema));
	}
	public ColumnBinder(Class<?> z,Set<T> object,Container ct,BinderIdListener<T> binderIdListener,String schema) {
		this(z,ct,binderIdListener,schema);
	}
	public ColumnBinder(Class<?> z,Container ct,List<Column> cols,BinderIdListener<T> binderIdListener){
		this.ct=ct;
		this.cols=cols;
		this.binderIdListener=binderIdListener;
	}
	public Map<Object,T> bind(List<T> list,boolean resetContainer) {
		try{
			if(list!=null && list.size()>0){
				if(resetContainer){
					objectMap.clear();
					ct.removeAllItems();
				}
				passIdBinder(list.get(0));
				for (T t : list) {
					Object id = bindSingleObject(t);
					objectMap.put(id, t);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return objectMap;
	}
	public Map<Object,T> bind(T t) {
		try{
			if(t!=null){
				passIdBinder(t);
				Object id =bindSingleObject(t);
				objectMap.put(id, t);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return objectMap;
	}
	private void passIdBinder(T t) {
		if(binderIdListener==null){
			try{
				t.getClass().getDeclaredField("id");
			}catch(java.lang.NoSuchFieldException e){
				if(binderIdListener==null){
					binderIdListener=defaultBinder();
					//field id not found. Set default
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	private Object bindSingleObject(T t) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, SecurityException {
		Object id=null;
		if(binderIdListener!=null){
			id=binderIdListener.getId(t);
		}else{
			id =t.getClass().getMethod("getId").invoke(t);
		}
		if(id!=null){
			Item it = ct.addItem(id);
			if(it==null){
				it = ct.getItem(id);
			}
			if(it!=null){
				for(Column c : cols){
					Object value=null;
					if(c.isMethod()){
						boolean acc= false;
						Method m = t.getClass().getDeclaredMethod(c.getFieldName());
						if(!m.isAccessible()){
							acc=true;
							m.setAccessible(true);
						}
						value = m.invoke(t);
						if(acc){
							m.setAccessible(false);
						}
					}else if(c.getRoute()!=null){
						value=t;
						for(int j=c.getRoute().size()-1; j>= 0; j--){
							  if(value==null){
								  break;
							  }
							  Field f = c.getRoute().get(j);
							  f.setAccessible(true);
							  value = f.get(value);
							  f.setAccessible(false);
						 }
						 if(value!=null){
							 value = getValueFromField(c.getFieldName(),value);
						 }
					}else{
					  value =getValueFromField(c.getFieldName(),t);
					}
					it.getItemProperty(c.getName()).setValue(value);
				}
				if(itemBinder!=null){
					itemBinder.bindItem(it, t,id);
				}
			}
		}
		return id;
	}
	private Object getValueFromField(String fieldName, Object t) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = t.getClass().getDeclaredField(fieldName);
		boolean access= f.isAccessible();
		f.setAccessible(true);
		Object value=f.get(t);
		f.setAccessible(access);
		return value;
	}
	public BinderIdListener<T> defaultBinder(){
		return new BinderIdListener<T>(){
			Long id=0L;
			@Override
			public Object getId(T item) {
				return id+=1;
			}
		};
	}
	public void setBinder(ItemBindListener<T> itemBinder) {
		this.itemBinder=itemBinder;
	}
	public static String toGetterName(String s) {
		return "get"+s.substring(0, 1).toUpperCase() + s.substring(1);
	}
}
