package ca.wec2020.application.views.account;

import ca.wec2020.application.backend.controllers.AccountController;
import ca.wec2020.application.backend.models.Account;
import ca.wec2020.application.backend.models.Transaction;
import ca.wec2020.application.views.helpers.GridHelpers;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleAccountDialog extends Dialog {
    private Account account;
    private Grid<Transaction> transactionGrid;
    private DatePicker endDatePicker;
    private DatePicker startDatePicker;
    private Label message;
    private VerticalLayout datePicker;
    private List<Transaction> transactionList;

    private Div transactionHistoryPage;
    private Div transferPage;
    private Div withdrawPage;
    private Div accountRequirementsPage;
    private Map<Tab, Component> tabsToPages;
    private Tabs tabs;


    //https://vaadin.com/components/vaadin-tabs/java-examples
    public SingleAccountDialog(Account account) {
        this.account = account;
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("1024px");
        Board board = new Board();


        tabsToPages = new HashMap<>();

        Tab transactionHistoryTab = new Tab("Transaction History");
        transactionHistoryPage = new Div();
        tabsToPages.put(transactionHistoryTab, transactionHistoryPage);
        buildTransactionHistory();

        Tab transferTab = new Tab("Make a Transfer");
        transferPage = new Div();
        transferPage.setVisible(false);
        tabsToPages.put(transferTab, transferPage);
        buildTransfer();

        Tab withdrawTab = new Tab("Withdraw from Account");
        withdrawPage = new Div();
        withdrawPage.setVisible(false);
        tabsToPages.put(withdrawTab, withdrawPage);
        buildWithdrawPage();

        Tab accountRequirementsTab = new Tab("Modify account Settings");
        accountRequirementsPage = new Div();
        accountRequirementsPage.setVisible(false);
        tabsToPages.put(accountRequirementsTab, accountRequirementsPage);
        buildSettings();

        tabs = new Tabs(transactionHistoryTab, transferTab, withdrawTab, accountRequirementsTab);
        tabs.setFlexGrowForEnclosedTabs(1);
        Div pages = new Div(transactionHistoryPage, transferPage, withdrawPage, accountRequirementsPage);
        Set<Component> pagesShown = Stream.of(transactionHistoryPage)
                .collect(Collectors.toSet());
        board.addRow(tabs);
        board.addRow(pages);

        tabs.addSelectedChangeListener(event -> {
            pagesShown.forEach(page -> page.setVisible(false));
            pagesShown.clear();
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            pagesShown.add(selectedPage);
        });

        account.getOwnsPermissions();

        Button cancelButton = new Button("Close", event -> {
            close();
        });


        board.addRow(cancelButton);
        add(board);
    }

    private void buildSettings() {
        Board board = new Board();
        H3 title = new H3("Change secondary account settings for " + account.getAccount_name());
        title.addClassName("primary-text");
        board.addRow(title);

        FormLayout transferForm = new FormLayout();

        NumberField amount = new NumberField();
        amount.setPlaceholder("100");
        amount.setLabel("Max Withdraw Amount");
        double total = account.getTransactions().stream().mapToDouble(Transaction::getAmount).sum();
        amount.setMax(total);
        transferForm.add(amount);

        Button submit = new Button("Update");
        submit.setWidthFull();
        submit.addClickListener(e -> {
            if(amount.isEmpty()) {
                Notification notification = new Notification("Please fill out all fields correctly.");
                notification.open();
            } else {
                AccountController.updateLimit(account, amount.getValue());
                Notification notification = new Notification("Withdraw successful!");
                notification.open();
                amount.setValue(0.0);
                transactionList = account.getTransactions();

                transactionGrid.setItems(transactionList);
                tabs.setSelectedIndex(0);
            }
        });
        transferForm.add(submit);
        board.addRow(transferForm);
        accountRequirementsPage.add(board);
    }

    private void buildWithdrawPage() {
        Board board = new Board();
        H3 title = new H3("Record a withdraw From " + account.getAccount_name());
        title.addClassName("primary-text");
        board.addRow(title);

        FormLayout transferForm = new FormLayout();

        NumberField amount = new NumberField();
        amount.setPlaceholder("123.12");
        amount.setLabel("Amount");
        double total = account.getTransactions().stream().mapToDouble(Transaction::getAmount).sum();
        amount.setMax(total);
        transferForm.add(amount);

        TextField title_w = new TextField();
        title_w.setPlaceholder("Candies");
        title_w.setLabel("Title");
        transferForm.add(title_w);

        Button submit = new Button("Withdraw");
        submit.setWidthFull();
        submit.addClickListener(e -> {
            if(title_w.isEmpty() || amount.isEmpty() || amount.getValue() > total) {
                Notification notification = new Notification("Please fill out all fields correctly.");
                notification.open();
            } else {
                AccountController.withdraw(account, title_w.getValue(), amount.getValue());
                Notification notification = new Notification("Withdraw successful!");
                notification.open();
                title_w.setValue("");
                amount.setValue(0.0);
                transactionList = account.getTransactions();

                transactionGrid.setItems(transactionList);
                tabs.setSelectedIndex(0);
            }
        });
        transferForm.add(submit);
        board.addRow(transferForm);
        withdrawPage.add(board);
    }

    private void buildTransactionHistory() {
        Board board = new Board();
        H3 title = new H3(account.getAccount_name() + " Transaction History");
        title.addClassName("primary-text");
        addTransactionGrid();
        addDatePicker();
        board.addRow(title);
        board.addRow(datePicker);
        board.addRow(transactionGrid);
        transactionHistoryPage.add(board);
    }

    private void buildTransfer() {
        Board board = new Board();
        H3 title = new H3("Make a Transfer From " + account.getAccount_name());
        title.addClassName("primary-text");
        board.addRow(title);

        FormLayout transferForm = new FormLayout();

        NumberField amount = new NumberField();
        amount.setPlaceholder("123.12");
        amount.setLabel("Amount");
        double total = account.getTransactions().stream().mapToDouble(Transaction::getAmount).sum();
        amount.setMax(total);
        transferForm.add(amount);

        Select<Account> accountSelect = new Select<>();
        List<Account> accounts = AccountController.getAccountsForUser();
        accountSelect.setItems(accounts.stream().filter(account1 -> account1 !=  account));
        accountSelect.setLabel("Account");
        transferForm.add(accountSelect);
        Button submit = new Button("Transfer");
        submit.setWidthFull();
        submit.addClickListener(e -> {
            if(accountSelect.isEmpty() || amount.isEmpty() || amount.getValue() > total) {
                Notification notification = new Notification("Please fill out all fields correctly.");
                notification.open();
            } else {
                System.out.println(accountSelect.getValue().getTransactions().get(0));
                AccountController.makeTransfer(account, accountSelect.getValue(), amount.getValue());
                Notification notification = new Notification("Transfer successful!");
                notification.open();
                accountSelect.setValue(null);
                amount.setValue(null);
                transactionList = account.getTransactions();

                transactionGrid.setItems(transactionList);
                tabs.setSelectedIndex(0);
            }
        });
        transferForm.add(submit);
        board.addRow(transferForm);
        transferPage.add(board);
    }

    private void addTransactionGrid() {
        transactionGrid = new Grid<>(Transaction.class);

        transactionList = account.getTransactions();

        transactionGrid.setItems(transactionList);


        transactionGrid.removeAllColumns();
        transactionGrid.addColumn(Transaction::getTitle).setHeader("Title");
        transactionGrid.addColumn(GridHelpers.getTransactionBadge()).setHeader("Type");
        transactionGrid.addColumn(Transaction::getDate).setHeader("Date");
        Column<Transaction> amountCol = transactionGrid.addColumn(GridHelpers.getTransactionRow()).setHeader("Amount");
        amountCol.setFooter(getTotal());
        transactionGrid.appendFooterRow();


    }

    private void addDatePicker() {
//        https://vaadin.com/components/vaadin-date-picker/java-examples
        startDatePicker = new DatePicker();
        startDatePicker.setLabel("Start");
        endDatePicker = new DatePicker();
        endDatePicker.setLabel("End");
        message = new Label();
        message.setClassName("error-text");

        startDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (selectedDate != null) {
                endDatePicker.setMin(selectedDate.plusDays(1));
                if (endDate == null) {
                    endDatePicker.setOpened(true);
//                    message.setText("Select the ending date");
                } else {
                    updateDateFilter(selectedDate, endDate);

                }
            } else {
                endDatePicker.setMin(null);
//                message.setText("Select the starting date");
            }
        });

        endDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            LocalDate startDate = startDatePicker.getValue();
            if (selectedDate != null) {
                startDatePicker.setMax(selectedDate.minusDays(1));
                if (startDate != null) {
                    updateDateFilter(startDate, selectedDate);
                } else {
//                    message.setText("Select the starting date");
                }
            } else {
                startDatePicker.setMax(null);
                if (startDate != null) {
//                    message.setText("Select the ending date");
                } else {
//                    message.setText("No date is selected");
                }
            }
        });

        datePicker = new VerticalLayout(new HorizontalLayout(startDatePicker, endDatePicker), message);
    }

    private Label getTotal() {
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        double total = account.getTransactions().stream().mapToDouble(Transaction::getAmount).sum();
        return new Label(defaultFormat.format(total));
    }

    private void updateDateFilter(LocalDate start, LocalDate end) {
        transactionGrid.setItems(transactionList.stream().filter(t -> t.getDate().toLocalDate().compareTo(end) < 0).filter(t -> t.getDate().toLocalDate().compareTo(start) > 0));
    }
}
