package ca.wec2020.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;

public class WrapperCard extends Div {

	public WrapperCard(String className, Component[] components,
                       String... classes) {
		addClassName(className);

		Div card = new Div();
		card.addClassNames(classes);
		card.add(components);

		add(card);
	}

	public static WrapperCard createBadge(String title, H2 h2, String h2ClassName,
									String description, String badgeTheme) {
		Span titleSpan = new Span(title);
		titleSpan.getElement().setAttribute("theme", badgeTheme);

		h2.addClassName(h2ClassName);

		Span descriptionSpan = new Span(description);
		descriptionSpan.addClassName("secondary-text");

		return new WrapperCard("wrapper",
				new Component[] { titleSpan, h2, descriptionSpan }, "card",
				"space-m");
	}

	public static WrapperCard createBadgeWithComponent(H2 h2, String h2ClassName,
									Component component, String badgeTheme) {

		h2.addClassName(h2ClassName);

//		Span descriptionSpan = new Span(description);
//		descriptionSpan.addClassName("secondary-text");

		return new WrapperCard("wrapper",
				new Component[] { h2, component }, "card",
				"space-m");
	}

}
