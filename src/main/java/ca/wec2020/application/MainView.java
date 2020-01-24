package ca.wec2020.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ca.wec2020.application.backend.controllers.TimeController;
import ca.wec2020.application.views.account.AllAccountsView;
import ca.wec2020.application.views.welcome.WelcomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import ca.wec2020.application.views.dashboard.DashboardView;
import ca.wec2020.application.views.masterdetail.MasterDetailView;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@PWA(name = "WEC 2020", shortName = "WEC 2020")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends AppLayout {

    private final Tabs menu;

    public MainView() {
        menu = createMenuTabs();
        addToNavbar(menu, dateControl());
    }

    private static HorizontalLayout dateControl() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label time = new Label(TimeController.getInstance().getDate().toString());
        horizontalLayout.add(time);
        horizontalLayout.add(new Button("+", buttonClickEvent -> {
            TimeController.getInstance().incrementDay();
            time.setText(TimeController.getInstance().getDate().toString());
        }));
        return horizontalLayout;
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.add(getAvailableTabs());
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
        final List<Tab> tabs = new ArrayList<>();
//        TODO: TABS HERE
        tabs.add(createTab("Welcome", WelcomeView.class));
        tabs.add(createTab("Accounts", AllAccountsView.class));
        tabs.add(createTab("Dashboard", DashboardView.class));
        tabs.add(createTab("MasterDetail", MasterDetailView.class));
        return tabs.toArray(new Tab[tabs.size()]);
    }

    private static Tab createTab(String title,
            Class<? extends Component> viewClass) {
        return createTab(populateLink(new RouterLink(null, viewClass), title));
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(content);
        return tab;
    }

    private static <T extends HasComponents> T populateLink(T a, String title) {
        a.add(title);
        return a;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        selectTab();
    }

    private void selectTab() {
        String target = RouteConfiguration.forSessionScope()
                .getUrl(getContent().getClass());
        Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
            Component child = tab.getChildren().findFirst().get();
            return child instanceof RouterLink
                    && ((RouterLink) child).getHref().equals(target);
        }).findFirst();
        tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
    }
}
