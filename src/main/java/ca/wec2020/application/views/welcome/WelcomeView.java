package ca.wec2020.application.views.welcome;

import ca.wec2020.application.MainView;
import ca.wec2020.application.views.WrapperCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route(value = "welcome", layout = MainView.class)
@PageTitle("Welcome")
@CssImport(value = "styles/views/style.css", include = "lumo-badge")
@Secured("ROLE_User") //
public class WelcomeView extends Div implements AfterNavigationObserver {
    public WelcomeView() {
        setId("welcome-view");

        Board board = new Board();
        board.addRow(
                createBadge("Welcome", new H2("123"), "primary-text", "hello world", "badge")
        );


        // Create a chart of some primary type
        Chart chart = new Chart(ChartType.SCATTER);

// Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Average Temperatures in Turku");
        conf.getLegend().setEnabled(false);

// The primary data series
        ListSeries averages = new ListSeries(
                -6, -6.5, -4, 3, 9, 14, 17, 16, 11, 6, 2, -2.5);

// Error bar data series with low and high values
        DataSeries errors = new DataSeries();
        errors.add(new DataSeriesItem(0,  -9, -3));
        errors.add(new DataSeriesItem(1, -10, -3));
        errors.add(new DataSeriesItem(2,  -8,  1));

// Need to be used for series to be recognized as error bar
        PlotOptionsErrorbar barOptions = new PlotOptionsErrorbar();
        errors.setPlotOptions(barOptions);

// The errors should be drawn lower
        conf.addSeries(errors);
        conf.addSeries(averages);

        WrapperCard welcomeWrapper = new WrapperCard("wrapper",
                new Component[] { chart }, "card");
        board.add(welcomeWrapper);
//        board.add(chart);


        add(board);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }

    private WrapperCard createBadge(String title, H2 h2, String h2ClassName,
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

}
