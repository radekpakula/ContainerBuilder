package pl.containerbuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ColumnReader {
	public static List<Column> readColumns(Class<?> z){
		return readColumns(z, "");
	}
	public static List<Column> readColumns(Class<?> z, String schemaName){
		List<Column> formData = new ArrayList<Column>();
		readFromField(z, formData,schemaName);
		readFromGetter(z, formData,schemaName);
		formData.sort(new Comparator<Column>() {
			public int compare(Column o1, Column o2) {
				return o1.getPosition()-o2.getPosition();
			}
		});
		return formData;
	}
	private static void readFromGetter(Class<?> z, List<Column> formData, String schemaName) {
		for(Method m : z.getDeclaredMethods()){
			m.setAccessible(true);
			Col[] f = m.getAnnotationsByType(Col.class);
			if(f!=null){
				for (Col col : f) {
					if(col.schema().equals(schemaName)){
						Class<?> type = m.getReturnType();
						String name = col.value().isEmpty() ? convertGetterName(m.getName()) : col.value();
						String fieldName=m.getName();
						generateCol(formData, col, type, name, fieldName,true);
					}
				}
			}
		}
	}
	private static void readFromField(Class<?> z, List<Column> formData, String schemaName) {
		for (Field field : z.getDeclaredFields()) {
			InnerCol[] in =field.getAnnotationsByType(InnerCol.class);
			if(in.length>0){
				for(InnerCol inner : in){
					if(inner.schema().equals(schemaName)){
						List<Column> innerCol = readColumns(field.getType(), schemaName);
						for (Column column : innerCol) {
							if(column.getRoute()==null){
								column.setRoute(new ArrayList<Field>());
							}
							column.getRoute().add(field);
						}
						formData.addAll(innerCol);
					}
				}
			}
			Col[] f = field.getAnnotationsByType(Col.class);
			if(f!=null){
				for (Col col : f) {
					if(col.schema().equals(schemaName)){
						Class<?> type = field.getType();
						String name =col.value().isEmpty() ? field.getName() : col.value();
						String fieldName=field.getName();
						generateCol(formData, col, type, name, fieldName,false);
					}
				}
			}
        }
	}
	private static String convertGetterName(String name) {
		return name.replaceFirst("get", "");
	}
	private static void generateCol(List<Column> formData, Col f, Class<?> type, String name, String fieldName, boolean method) {
		if(type.equals(boolean.class)){
			type=Boolean.class;
		}else if(type.equals(long.class)){
			type=Long.class;
		}else if(type.equals(int.class)){
			type=Integer.class;
		}else if(type.equals(double.class)){
			type=Double.class;
		}
		Column c = new Column(name, type);
		c.setVisible(f.visible());
		c.setDefaultValue(null);
		c.setFieldName(fieldName);
		c.setPosition(f.position());
		c.setMethod(method);
		formData.add(c);
	}
	public static Column[] readColumnsAsArray(Class<?> z, String schemaName){
		List<Column> cc =readColumns(z,schemaName);
		return cc.toArray(new Column[cc.size()]);
	}
	public static Column[] readColumnsAsArray(Class<?> z){
		return readColumnsAsArray(z,"");
	}
}
