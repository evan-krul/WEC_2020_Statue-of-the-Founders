package ca.wec2020.application.views.helpers;

import ca.wec2020.application.backend.models.Account;
import ca.wec2020.application.backend.models.Transaction;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.text.NumberFormat;

public class GridHelpers {
    public static ComponentRenderer<Label, Account> getTotalAccountCost() {
        return new ComponentRenderer<>(account -> {
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
            double total = account.getTransactions().stream().mapToDouble(Transaction::getAmount).sum();
            return new Label(defaultFormat.format(total));
        });
    }


    public static ComponentRenderer<Span, Account> getAccountLock() {
        return new ComponentRenderer<>(account -> {
            Span titleSpan = new Span();
            if(account.isIs_locked()) {
                titleSpan.getElement().setAttribute("theme", "badge error");
                titleSpan.setText("Locked until: " + account.getLock_release());
            } else {
                titleSpan.getElement().setAttribute("theme", "badge success");
                titleSpan.setText("Unlocked");
            }
            return titleSpan;
        });
    }

    public static ComponentRenderer<Icon, Account> getInvestment() {
        return new ComponentRenderer<>(account -> {
            Icon icon;
            if(account.isIs_investment()) {
                icon = new Icon(VaadinIcon.CHECK_CIRCLE_O);
            } else {
                icon = new Icon(VaadinIcon.MINUS_CIRCLE_O);
            }
            return icon;
        });
    }

    public static ComponentRenderer<Label, Transaction> getTransactionRow() {
        return new ComponentRenderer<>(transaction -> {
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
            double total = transaction.getAmount();
            boolean isNeg = false;
            if(transaction.getType().equals("Withdrawl")) {
                isNeg = true;
            }
            Label label = new Label();
            label.setText((isNeg?"-":"  ") + defaultFormat.format(total));
            return label;
        });
    }

    public static ComponentRenderer<Span, Transaction> getTransactionBadge() {
        return new ComponentRenderer<>(transaction -> {
            Span titleSpan = new Span();
            switch (transaction.getType()) {
                case "Withdrawl":
                    titleSpan.getElement().setAttribute("theme", "badge error");
                    break;
                case "Deposit":
                    titleSpan.getElement().setAttribute("theme", "badge success");
                    break;
                default:
                    titleSpan.getElement().setAttribute("theme", "badge");
            }
            titleSpan.setText(transaction.getType());


            return titleSpan;
        });
    }
}
