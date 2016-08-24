package pl.containerbuilder.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;

import pl.containerbuilder.Column;
import pl.containerbuilder.CtBuilder;
import pl.containerbuilder.ItemBindListener;
import pl.containerbuilder.example.ExampleUser;
import pl.containerbuilder.example.ObjectGenerator;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("containerbuilder")
public class ContainerbuilderUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ContainerbuilderUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		Column BUTTON = new Column("My extra button",Button.class);
		Column EXTRANAME = new Column("My extra name",String.class);
		CtBuilder<ExampleUser> ctBuilder = new CtBuilder<ExampleUser>(ExampleUser.class,new Column[]{BUTTON,EXTRANAME});
		Table t = new Table("Fresh table",ctBuilder.getContainer());
		t.setVisibleColumns(ctBuilder.getVisibleColumn());
		ctBuilder.bind(ObjectGenerator.generateUsers(100),new ItemBindListener<ExampleUser>() {
			@Override
			public void bindItem(Item item, ExampleUser t, Object itemId) {
				ctBuilder.setProperty(itemId, BUTTON, new Button("Click me"));
				ctBuilder.setProperty(itemId, EXTRANAME, "Extra name");
			}
		});
		layout.addComponent(t);
	}

}