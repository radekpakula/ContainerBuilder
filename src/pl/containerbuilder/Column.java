package pl.containerbuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Specyfikacja kolumny dla tabel
 * 
 * @author radek
 *
 */
public class Column implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Class<?> type;
	private List<Field> route;
	private Object defaultValue;
	private int position;
	private String fieldName;
	private boolean method;
	private boolean visible;

	public Column() {
	}

	public Column(String name, Class<?> type, int position) {
		this(name, type, null);
		setPosition(position);
	}

	public Column(String name, Class<?> type) {
		this(name, type, null);
	}

	public Column(String name, Class<?> type, boolean visible) {
		this(name, type, null, visible);
	}

	public Column(String name, Class<?> type, Object defaultValue) {
		this(name, type, defaultValue, true);
	}

	public Column(String name, Class<?> type, Object defaultValue, boolean visible) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.visible = visible;
		this.method = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<String> type) {
		this.type = type;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isMethod() {
		return method;
	}

	public void setMethod(boolean method) {
		this.method = method;
	}

	public List<Field> getRoute() {
		return route;
	}

	public void setRoute(List<Field> route) {
		this.route = route;
	}
}
