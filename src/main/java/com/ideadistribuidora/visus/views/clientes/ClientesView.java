package com.ideadistribuidora.visus.views.clientes;

import com.ideadistribuidora.visus.data.Clientes;
import com.ideadistribuidora.visus.services.ClientesService;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Clientes")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 0)
@Route(value = "/:clientesID?/:action?(edit)")
@RouteAlias(value = "")
public class ClientesView extends Div implements BeforeEnterObserver {

    private final String CLIENTES_ID = "clientesID";
    private final String CLIENTES_EDIT_ROUTE_TEMPLATE = "/%s/edit";

    private final Grid<Clientes> grid = new Grid<>(Clientes.class, false);

    CollaborationAvatarGroup avatarGroup;

    private TextField nombreFantasia;
    private TextField nombreCliente;
    private TextField sexo;
    private TextField estadoCivil;
    private TextField idDomicilio;
    private TextField idLocalidad;
    private TextField telefonoFijo;
    private TextField telefonoMovil;
    private TextField telefonoFax;
    private TextField idDocumento;
    private TextField numero;
    private TextField email;
    private DatePicker fechaNacimiento;
    private DatePicker fechaJubilacion;
    private DatePicker fechaBaja;
    private DatePicker fechaActualizacion;
    private DatePicker fechaIngreso;
    private DatePicker fechaUltimaCompra;
    private TextField limiteFacturasVencidas;
    private TextField limiteCredito;
    private TextField pagoMinimo;
    private TextField periodoCarencia;
    private TextField nivelFidelizacion;
    private TextField tipoCliente;
    private TextField idBanco;
    private TextField situacionFiscal;
    private TextField saldoCuentaCorriente;
    private TextField estadoCliente;
    private TextField observaciones;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final CollaborationBinder<Clientes> binder;

    private Clientes clientes;

    private final ClientesService clientesService;

    public ClientesView(ClientesService clientesService) {
        this.clientesService = clientesService;
        addClassNames("clientes-view");

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
        grid.addColumn("nombreFantasia").setAutoWidth(true);
        grid.addColumn("nombreCliente").setAutoWidth(true);
        grid.addColumn("sexo").setAutoWidth(true);
        grid.addColumn("estadoCivil").setAutoWidth(true);
        grid.addColumn("idDomicilio").setAutoWidth(true);
        grid.addColumn("idLocalidad").setAutoWidth(true);
        grid.addColumn("telefonoFijo").setAutoWidth(true);
        grid.addColumn("telefonoMovil").setAutoWidth(true);
        grid.addColumn("telefonoFax").setAutoWidth(true);
        grid.addColumn("idDocumento").setAutoWidth(true);
        grid.addColumn("numero").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("fechaNacimiento").setAutoWidth(true);
        grid.addColumn("fechaJubilacion").setAutoWidth(true);
        grid.addColumn("fechaBaja").setAutoWidth(true);
        grid.addColumn("fechaActualizacion").setAutoWidth(true);
        grid.addColumn("fechaIngreso").setAutoWidth(true);
        grid.addColumn("fechaUltimaCompra").setAutoWidth(true);
        grid.addColumn("limiteFacturasVencidas").setAutoWidth(true);
        grid.addColumn("limiteCredito").setAutoWidth(true);
        grid.addColumn("pagoMinimo").setAutoWidth(true);
        grid.addColumn("periodoCarencia").setAutoWidth(true);
        grid.addColumn("nivelFidelizacion").setAutoWidth(true);
        grid.addColumn("tipoCliente").setAutoWidth(true);
        grid.addColumn("idBanco").setAutoWidth(true);
        grid.addColumn("situacionFiscal").setAutoWidth(true);
        grid.addColumn("saldoCuentaCorriente").setAutoWidth(true);
        grid.addColumn("estadoCliente").setAutoWidth(true);
        grid.addColumn("observaciones").setAutoWidth(true);
        grid.setItems(query -> clientesService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CLIENTES_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ClientesView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(Clientes.class, userInfo);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.clientes == null) {
                    this.clientes = new Clientes();
                }
                binder.writeBean(this.clientes);
                clientesService.update(this.clientes);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ClientesView.class);
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
        Optional<Long> clientesId = event.getRouteParameters().get(CLIENTES_ID).map(Long::parseLong);
        if (clientesId.isPresent()) {
            Optional<Clientes> clientesFromBackend = clientesService.get(clientesId.get());
            if (clientesFromBackend.isPresent()) {
                populateForm(clientesFromBackend.get());
            } else {
                Notification.show(String.format("The requested clientes was not found, ID = %d", clientesId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ClientesView.class);
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
        nombreFantasia = new TextField("Nombre Fantasia");
        nombreCliente = new TextField("Nombre Cliente");
        sexo = new TextField("Sexo");
        estadoCivil = new TextField("Estado Civil");
        idDomicilio = new TextField("Id Domicilio");
        idLocalidad = new TextField("Id Localidad");
        telefonoFijo = new TextField("Telefono Fijo");
        telefonoMovil = new TextField("Telefono Movil");
        telefonoFax = new TextField("Telefono Fax");
        idDocumento = new TextField("Id Documento");
        numero = new TextField("Numero");
        email = new TextField("Email");
        fechaNacimiento = new DatePicker("Fecha Nacimiento");
        fechaJubilacion = new DatePicker("Fecha Jubilacion");
        fechaBaja = new DatePicker("Fecha Baja");
        fechaActualizacion = new DatePicker("Fecha Actualizacion");
        fechaIngreso = new DatePicker("Fecha Ingreso");
        fechaUltimaCompra = new DatePicker("Fecha Ultima Compra");
        limiteFacturasVencidas = new TextField("Limite Facturas Vencidas");
        limiteCredito = new TextField("Limite Credito");
        pagoMinimo = new TextField("Pago Minimo");
        periodoCarencia = new TextField("Periodo Carencia");
        nivelFidelizacion = new TextField("Nivel Fidelizacion");
        tipoCliente = new TextField("Tipo Cliente");
        idBanco = new TextField("Id Banco");
        situacionFiscal = new TextField("Situacion Fiscal");
        saldoCuentaCorriente = new TextField("Saldo Cuenta Corriente");
        estadoCliente = new TextField("Estado Cliente");
        observaciones = new TextField("Observaciones");
        formLayout.add(nombreFantasia, nombreCliente, sexo, estadoCivil, idDomicilio, idLocalidad, telefonoFijo,
                telefonoMovil, telefonoFax, idDocumento, numero, email, fechaNacimiento, fechaJubilacion, fechaBaja,
                fechaActualizacion, fechaIngreso, fechaUltimaCompra, limiteFacturasVencidas, limiteCredito, pagoMinimo,
                periodoCarencia, nivelFidelizacion, tipoCliente, idBanco, situacionFiscal, saldoCuentaCorriente,
                estadoCliente, observaciones);

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

    private void populateForm(Clientes value) {
        this.clientes = value;
        String topic = null;
        if (this.clientes != null && this.clientes.getId() != null) {
            topic = "clientes/" + this.clientes.getId();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.clientes);
        avatarGroup.setTopic(topic);

    }
}
