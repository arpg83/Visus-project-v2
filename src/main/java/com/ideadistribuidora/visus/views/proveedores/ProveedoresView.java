package com.ideadistribuidora.visus.views.proveedores;

import com.ideadistribuidora.visus.data.Proveedores;
import com.ideadistribuidora.visus.services.ProveedoresService;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Proveedores")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 1)
@Route(value = "2/:proveedoresID?/:action?(edit)")
public class ProveedoresView extends Div implements BeforeEnterObserver {

    private final String PROVEEDORES_ID = "proveedoresID";
    private final String PROVEEDORES_EDIT_ROUTE_TEMPLATE = "2/%s/edit";

    private final Grid<Proveedores> grid = new Grid<>(Proveedores.class, false);

    CollaborationAvatarGroup avatarGroup;

    private TextField idDocumento;
    private TextField numero;
    private TextField nombreFantasia;
    private TextField nombreReal;
    private TextField idDomicilio;
    private TextField telefono1;
    private TextField telefono2;
    private TextField telefono3;
    private TextField telefonoFax;
    private TextField idLocalidad;
    private TextField idBanco;
    private TextField situacionFiscal;
    private TextField email;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final CollaborationBinder<Proveedores> binder;

    private Proveedores proveedores;

    private final ProveedoresService proveedoresService;

    public ProveedoresView(ProveedoresService proveedoresService) {
        this.proveedoresService = proveedoresService;
        addClassNames("proveedores-view");

        // UserInfo is used by Collaboration Engine and is used to share details
        // of users to each other to able collaboration. Replace this with
        // information about the actual user that is logged, providing a user
        // identifier, and the user's real name. You can also provide the users
        // avatar by passing an url to the image as a third parameter, or by
        // configuring an `ImageProvider` to `avatarGroup`.
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "hidden");

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("idDocumento").setAutoWidth(true);
        grid.addColumn("numero").setAutoWidth(true);
        grid.addColumn("nombreFantasia").setAutoWidth(true);
        grid.addColumn("nombreReal").setAutoWidth(true);
        grid.addColumn("idDomicilio").setAutoWidth(true);
        grid.addColumn("telefono1").setAutoWidth(true);
        grid.addColumn("telefono2").setAutoWidth(true);
        grid.addColumn("telefono3").setAutoWidth(true);
        grid.addColumn("telefonoFax").setAutoWidth(true);
        grid.addColumn("idLocalidad").setAutoWidth(true);
        grid.addColumn("idBanco").setAutoWidth(true);
        grid.addColumn("situacionFiscal").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.setItems(query -> proveedoresService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROVEEDORES_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProveedoresView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(Proveedores.class, userInfo);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.proveedores == null) {
                    this.proveedores = new Proveedores();
                }
                binder.writeBean(this.proveedores);
                proveedoresService.update(this.proveedores);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ProveedoresView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> proveedoresId = event.getRouteParameters().get(PROVEEDORES_ID).map(Long::parseLong);
        if (proveedoresId.isPresent()) {
            Optional<Proveedores> proveedoresFromBackend = proveedoresService.get(proveedoresId.get());
            if (proveedoresFromBackend.isPresent()) {
                populateForm(proveedoresFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested proveedores was not found, ID = %d", proveedoresId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProveedoresView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        idDocumento = new TextField("Id Documento");
        numero = new TextField("Numero");
        nombreFantasia = new TextField("Nombre Fantasia");
        nombreReal = new TextField("Nombre Real");
        idDomicilio = new TextField("Id Domicilio");
        telefono1 = new TextField("Telefono1");
        telefono2 = new TextField("Telefono2");
        telefono3 = new TextField("Telefono3");
        telefonoFax = new TextField("Telefono Fax");
        idLocalidad = new TextField("Id Localidad");
        idBanco = new TextField("Id Banco");
        situacionFiscal = new TextField("Situacion Fiscal");
        email = new TextField("Email");
        formLayout.add(idDocumento, numero, nombreFantasia, nombreReal, idDomicilio, telefono1, telefono2, telefono3,
                telefonoFax, idLocalidad, idBanco, situacionFiscal, email);

        editorDiv.add(avatarGroup, formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Proveedores value) {
        this.proveedores = value;
        String topic = null;
        if (this.proveedores != null && this.proveedores.getId() != null) {
            topic = "proveedores/" + this.proveedores.getId();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.proveedores);
        avatarGroup.setTopic(topic);

    }
}
