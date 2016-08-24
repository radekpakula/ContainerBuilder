package pl.containerbuilder;

import com.vaadin.data.Item;

public interface ItemBindListener<T> {

	/**
	 * Przekazuje element do zbindowania dodatkowych kolumn (wszystkich odatkowych w jednej metodzie)
	 * @param item
	 * @param t
	 * @param itemId
	 */
	public void bindItem(Item item,T t,Object itemId);
}
