package ca.wec2020.application.views.account;

import ca.wec2020.application.MainView;
import ca.wec2020.application.backend.controllers.AccountController;
import ca.wec2020.application.backend.models.Account;
import ca.wec2020.application.views.WrapperCard;
import ca.wec2020.application.views.helpers.GridHelpers;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.model.TextAlign;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

@Route(value = "accounts", layout = MainView.class)
@PageTitle("Welcome")
@CssImport(value = "styles/views/style.css", include = "lumo-badge")
@Secured("ROLE_User") //
public class AllAccountsView extends Div implements AfterNavigationObserver {
    private Grid<Account> accountGrid;
    private Dialog accountDialog;

    public AllAccountsView() {
        setId("all-accounts-view");
        Board board = new Board();

//        Account grid
        setAccountGrid();

        board.addRow(
                WrapperCard.createBadgeWithComponent( new H2("Your accounts"), "primary-text", accountGrid, "badge")
        );

        add(board);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }

    private void setAccountGrid() {
        accountGrid = new Grid<>(Account.class);
        accountGrid.setItems(AccountController.getAccountsForUser());
        accountGrid.removeAllColumns();
        accountGrid.addColumn(Account::getAccount_name).setHeader("Account Name");
        accountGrid.addColumn(GridHelpers.getInvestment()).setHeader("Investment").setTextAlign(ColumnTextAlign.CENTER);
        accountGrid.addColumn(GridHelpers.getAccountLock()).setHeader("Account Locked").setTextAlign(ColumnTextAlign.CENTER);;
        accountGrid.addColumn(GridHelpers.getTotalAccountCost()).setHeader("Total").setTextAlign(ColumnTextAlign.END);;

        accountGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        accountGrid.addItemClickListener(event -> openAccountDialog(event.getItem()));
    }

    private void openAccountDialog(Account account) {
        accountDialog = new SingleAccountDialog(account);
        accountDialog.open();
    }
}
