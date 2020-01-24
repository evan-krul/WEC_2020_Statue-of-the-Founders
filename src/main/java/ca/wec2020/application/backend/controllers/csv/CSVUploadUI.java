package ca.wec2020.application.backend.controllers.csv;

import ca.wec2020.application.MainView;
import ca.wec2020.application.views.WrapperCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.VerticalAlign;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.security.access.annotation.Secured;

@Route(value = "uploadCSV", layout = MainView.class)
@PageTitle("Welcome")
@CssImport(value = "styles/views/style.css", include = "lumo-badge")
@Secured("ROLE_User")

public class CSVUploadUI extends Div implements AfterNavigationObserver {
    private Upload uploadCSVChequing = new Upload();
    private Upload uploadCSVSavings = new Upload();
    private final MemoryBuffer bufferCSV = new MemoryBuffer();

    /**
     * Creates a new FileUploadUi.
     */
    public CSVUploadUI() {
        H2 chequingTitle = new H2("Insert CSV File: Chequing");
        add(chequingTitle);
        add(uploadCSVChequing);

        H2 savingsTitle = new H2("Insert CSV File: Savings");
        add(savingsTitle);
        add(uploadCSVSavings);


    // File upload manager: Chequing and Saving
        uploadCSVChequing.setReceiver(bufferCSV);
        Notification notificationOne = new Notification(
                "CSV Uploaded", 3000,
                Notification.Position.TOP_START);
        uploadCSVChequing.addSucceededListener(event -> {
            CSVUploadProcessor csvImportProcessor = new CSVUploadProcessor(bufferCSV);
            notificationOne.open();
            csvImportProcessor.insertChequingDB();
        });

        uploadCSVSavings.setReceiver(bufferCSV);
        Notification notificationTwo = new Notification(
                "CSV Uploaded", 3000,
                Notification.Position.TOP_START);
        uploadCSVSavings.addSucceededListener(event -> {
            CSVUploadProcessor csvImportProcessor = new CSVUploadProcessor(bufferCSV);
            notificationTwo.open();
            csvImportProcessor.insertSavingsDB();
        });
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    }

    /**
     * This model binds properties between FileUploadUi and file-upload-ui
     */
//    public interface CSVUploadUIModel extends TemplateModel {
//        // Add setters and getters for template properties here.
//    }
}
