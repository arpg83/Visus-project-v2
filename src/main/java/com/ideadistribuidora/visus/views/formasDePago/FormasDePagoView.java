package com.ideadistribuidora.visus.views.formasDePago;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Coeficientes;
import com.ideadistribuidora.visus.data.FormasDePago;
import com.ideadistribuidora.visus.data.enums.ModalidadDePagoEnum;
import com.ideadistribuidora.visus.services.FormasDePagoService;
import com.ideadistribuidora.visus.views.utils.StringToShortConverter;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Formas De Pago")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 18)
@Route(value = "19/:formasDePagoID?/:action?(edit)")
public class FormasDePagoView extends Div implements BeforeEnterObserver {

    private final String FORMASDEPAGO_ID = "formasDePagoID";
    private final String FORMASDEPAGO_EDIT_ROUTE_TEMPLATE = "19/%s/edit";

    private final Grid<FormasDePago> grid = new Grid<>(FormasDePago.class, false);
    private final Grid<Coeficientes> gridSimulador = new Grid<>(Coeficientes.class, false);

    CollaborationAvatarGroup avatarGroup;

    private ComboBox<Coeficientes> coeficienteSim;
    private BigDecimalField valorSim;
    private TextField cuotasSim;
    private BigDecimalField montoSim;
    private ComboBox<ModalidadDePagoEnum> modalidad;
    private ComboBox<Coeficientes> coeficienteDesc;
    private BigDecimalField coeficiente;
    private TextField cuotas;
    private Checkbox esDtoProntoPago;
    private BigDecimalField dtoProntoPago;
    private Checkbox mesesCompletos;
    private TextField searchModalidad;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Grabar");
    private final Button delete = new Button("ELiminar");
    private Button simularButton;
    private Button resetButton;

    

    public ComboBox<Coeficientes> getCoeficienteDesc() {
        return coeficienteDesc;
    }

    public void setCoeficienteDesc(ComboBox<Coeficientes> coeficienteDesc) {
        this.coeficienteDesc = coeficienteDesc;
    }

    public BigDecimalField getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(BigDecimalField coeficiente) {
        this.coeficiente = coeficiente;
    }

    public TextField getCuotas() {
        return cuotas;
    }

    public void setCuotas(TextField cuotas) {
        this.cuotas = cuotas;
    }

    private CollaborationBinder<FormasDePago> binder;

    private FormasDePago formasDePago;

    private final FormasDePagoService formasDePagoService;
    GridListDataView<FormasDePago> dataView;

    public FormasDePagoView(FormasDePagoService formasDePagoService) {
        this.formasDePagoService = formasDePagoService;
        addClassNames("formasDePago-view");

        // UserInfo is used by Collaboration Engine and is used to share details
        // of users to each other to able collaboration. Replace this with
        // information about the actual user that is logged, providing a user
        // identifier, and the user's real name. You can also provide the users
        // avatar by passing an url to the image as a third parameter, or by
        // configuring an `ImageProvider` to `avatarGroup`.
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        splitLayout.setSplitterPosition(36);

        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "hidden");

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        // Configure Grid
        grid.addColumn(createFormasDePagoRenderer()).setHeader("Modalidad").setAutoWidth(true);
        grid.addColumn(formasDePago -> formasDePago.getIdCoeficiente().getDescripcion()).setHeader("Descripción").setAutoWidth(true);
        grid.addColumn(formasDePago -> formasDePago.getIdCoeficiente().getCoeficiente()).setHeader("Coeficiente").setAutoWidth(true);
        grid.addColumn(formasDePago -> formasDePago.getIdCoeficiente().getCuotas()).setHeader("Cuotas").setAutoWidth(true);
        grid.addComponentColumn(articulos -> {
                    Checkbox checkbox = new Checkbox();
                    checkbox.setValue(formasDePago.isEsDtoProntoPago());
                    checkbox.setEnabled(false);
                    return checkbox;
            }).setHeader("Pronto Pago").setAutoWidth(true);
        grid.addColumn(FormasDePago::getDtoProntoPago).setHeader("Dto. PP(%)").setAutoWidth(true);
        grid.addComponentColumn(articulos -> {
                    Checkbox checkbox = new Checkbox();
                    checkbox.setValue(formasDePago.isMesesCompletos());
                    checkbox.setEnabled(false);
                    return checkbox;
            }).setHeader("Meses Completos").setAutoWidth(true);

        dataView = grid.setItems(formasDePagoService.formasDePagoList());
        searchFilter(dataView);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                delete.setEnabled(true);
                UI.getCurrent().navigate(
                        String.format(FORMASDEPAGO_EDIT_ROUTE_TEMPLATE,
                                event.getValue().getIdFormasPago()));
            } else {
                delete.setEnabled(false);
                clearForm();
                UI.getCurrent().navigate(FormasDePagoView.class);
            }
        });

        gridSimulador.addColumn(Coeficientes::getCuotas).setHeader("Cuotas").setAutoWidth(true);
        gridSimulador.addColumn(coef -> new java.sql.Date(coef.getFecha().getTime()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                     .setHeader("Fecha").setAutoWidth(true);
        gridSimulador.addColumn(Coeficientes::getMonto).setHeader("Monto").setAutoWidth(true);

        //dataView = gridSimulador.setItems(formasDePagoService.formasDePagoList());
        searchFilter(dataView);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                delete.setEnabled(true);
                UI.getCurrent().navigate(
                        String.format(FORMASDEPAGO_EDIT_ROUTE_TEMPLATE,
                                event.getValue().getIdFormasPago()));
            } else {
                delete.setEnabled(false);
                clearForm();
                UI.getCurrent().navigate(FormasDePagoView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(FormasDePago.class, userInfo);
        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(modalidad).asRequired("Modalidad es Requerido")
                .bind("modalidad");
        binder.setSerializer(Coeficientes.class,
                coeficientes -> String.valueOf(coeficientes.getIdCoeficiente()),
                id -> formasDePagoService.findCoeficientesById(Integer.parseInt(id)));
        binder.bind(coeficienteDesc, "coeficienteDesc");
        binder.forField(coeficiente).asRequired("Coeficiente es Requerido")
                .bind("coeficiente"); 
        binder.forField(cuotas, String.class)
                                .asRequired("Cuotas es Requerido")
                                .withConverter(new StringToShortConverter())
                                .bind("cuotas");  

        binder.addStatusChangeListener(
                event -> save.setEnabled(binder.isValid()));

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
            searchModalidad.clear();
            delete.setEnabled(false);
            // save.setEnabled(false);
        });

        save.addClickListener(e -> {

            try {
                if (this.formasDePago == null) {
                    this.formasDePago = new FormasDePago();
                }
                binder.writeBean(this.formasDePago);
                formasDePagoService.update(this.formasDePago);

                clearForm();
                refreshGrid();
                Notification.show("Datos Guardados");
                UI.getCurrent().navigate(FormasDePagoView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al Actualizar los datos. Alguien mas está actualizando los datos.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show(
                        "Error al Guardar/Modificar los datos. Revise Nuevamente que todos los datos sean Válidos");
            } catch (Exception except) {
                if (except.getCause().getCause() instanceof SQLException) {
                    SQLException e1 = (SQLException) except.getCause().getCause();
                    if (e1.getMessage().contains("Ya existe la llave")) {
                        Notification n = Notification.show(
                                "La Forma de Pago " + this.formasDePago.getModalidad().getModalidadDePago()
                                        + " ya existe");
                        n.setPosition(Position.MIDDLE);
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }

            }
        });

        delete.addClickListener(e -> {
            try {
                if (this.formasDePago == null) {
                    this.formasDePago = new FormasDePago();
                }
                binder.writeBean(this.formasDePago);
                formasDePagoService.delete(this.formasDePago.getIdFormasPago());
                clearForm();
                refreshGrid();
                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                UI.getCurrent().navigate(FormasDePagoView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al Eliminar los datos. Alguien mas está actualizando los datos.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show(
                        "Error al Eliminar los datos. Revise Nuevamente que todos los datos sean Válidos");
            }
        });

        simularButton.addClickListener(e -> simular());

        // Añadir validadores y listeners para habilitar/deshabilitar el botón simularButton
        coeficienteSim.addValueChangeListener(e -> toggleSimularButton());
        valorSim.addValueChangeListener(e -> toggleSimularButton());
        cuotasSim.addValueChangeListener(e -> toggleSimularButton());
        montoSim.addValueChangeListener(e -> toggleSimularButton());
    }

    private void searchFilter(GridListDataView<FormasDePago> dataView2) {
        dataView.addFilter(fp -> {
            String searchTerm = searchModalidad.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesFullName = matchesTerm(fp.getModalidad().getModalidadDePago(),
                    searchTerm);

            return matchesFullName;
        });
    }

    private LitRenderer<FormasDePago> createFormasDePagoRenderer() {
        return LitRenderer.<FormasDePago>of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                        + "  <span> ${item.fullName} </span>"
                        + "</vaadin-horizontal-layout>")

                .withProperty("fullName", FormasDePago::getModalidad);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Short> formasDePagoId = event.getRouteParameters().get(FORMASDEPAGO_ID)
                .map(Short::parseShort);
        if (formasDePagoId.isPresent()) {
            Optional<FormasDePago> formasDePagoFromBackend = formasDePagoService.get(formasDePagoId.get());
            if (formasDePagoFromBackend.isPresent()) {
                populateForm(formasDePagoFromBackend.get());
            } else {
                Notification.show(
                        String.format("La Medida solicitada no fué encontrada, ID = %d",
                                formasDePagoId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(FormasDePagoView.class);
            }
        }
    }

    private void createHorizontalSearchLayout(HorizontalLayout searchHorizontalLayout) {
        searchModalidad = new TextField();
        searchModalidad.setPlaceholder("Buscar por Modalidad");
        searchModalidad.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchModalidad.setValueChangeMode(ValueChangeMode.EAGER);
        searchModalidad.addValueChangeListener(e -> dataView.refreshAll());
        searchModalidad.setWidth("500px");
        searchHorizontalLayout.add(searchModalidad);
    }

    private void createEditorLayout(SplitLayout splitLayout) {
       
        save.setEnabled(false);
        delete.setEnabled(false);

        coeficienteSim = new ComboBox<>("Coeficiente");
        coeficienteSim.setPlaceholder("Seleccione un Coeficiente");
        coeficienteSim.setItems(formasDePagoService.getAllCoefiencientes());
        coeficienteSim.setItemLabelGenerator(Coeficientes::getDescripcion);
        coeficienteSim.setWidth("25%");
        valorSim = new BigDecimalField("Coeficiente");
        valorSim.setWidth("25%");
        cuotasSim = new TextField("Cuotas");
        cuotasSim.setWidth("25%");
        montoSim = new BigDecimalField("Monto");
        montoSim.setWidth("25%");
        
        modalidad = new ComboBox<>("Modalidad");
        modalidad.setPlaceholder("Seleccione una Modalidad");
        modalidad.setItems(ModalidadDePagoEnum.values());
        modalidad.setItemLabelGenerator(ModalidadDePagoEnum::getModalidadDePago);
        modalidad.setWidth("25%");
        coeficienteDesc = new ComboBox<>("Coeficiente");
        coeficienteDesc.setPlaceholder("Seleccione un Coeficiente");
        coeficienteDesc.setItems(formasDePagoService.getAllCoefiencientes());
        coeficienteDesc.setItemLabelGenerator(Coeficientes::getDescripcion);
        coeficienteDesc.addValueChangeListener(e ->{
            coeficiente.setValue(e.getValue().getCoeficiente());
            cuotas.setValue((e.getValue().getCuotas());
        });
        coeficienteDesc.setWidth("25%");
        coeficiente = new BigDecimalField("Valor del Coeficiente");
        coeficiente.setEnabled(false);
        coeficiente.setWidth("25%");
        cuotas = new TextField("Cuotas");
        cuotas.setEnabled(false);
        cuotas.setWidth("25%");   
        esDtoProntoPago = new Checkbox("Pronto Pago");
        esDtoProntoPago.addValueChangeListener(e -> {
            if (e.getValue()) {
                    dtoProntoPago.setEnabled(true);
            } else {
                    dtoProntoPago.setEnabled(false);
            }
        });
        mesesCompletos = new Checkbox("Meses Completos");
        mesesCompletos.setWidth("25%");
        dtoProntoPago = new BigDecimalField("Dto. Pronto Pago");
         VerticalLayout prontPag = new VerticalLayout();
         prontPag.add(esDtoProntoPago, dtoProntoPago);
         prontPag.setWidth("25%");
        HorizontalLayout formLayout = new HorizontalLayout();
        VerticalLayout areaSim = new VerticalLayout();
        H4 simulacionTitle = new H4("Área de Simulación");
        simulacionTitle.getStyle().setColor("blue");
        areaSim.add(simulacionTitle);
        HorizontalLayout fieldsSim = new HorizontalLayout();
        fieldsSim.add(coeficienteSim, valorSim, cuotasSim, montoSim);
        fieldsSim.getStyle().setWidth("100%");
        areaSim.add(fieldsSim);
        createButtonSimLayout(areaSim);
        VerticalLayout fieldsButton = new VerticalLayout();
        HorizontalLayout fieldsOne = new HorizontalLayout();
        fieldsOne.setWidth("100%");
        HorizontalLayout fieldsTwo = new HorizontalLayout();
        fieldsTwo.setWidth("100%");
        formLayout.add(areaSim);
        fieldsButton.add(avatarGroup);
        fieldsOne.add(modalidad, coeficienteDesc,coeficiente,cuotas);
        fieldsButton.add(fieldsOne); 
        fieldsTwo.add(prontPag, mesesCompletos);
        fieldsButton.add(fieldsTwo);  
                
        createButtonLayout(fieldsButton);
        formLayout.add(fieldsButton);

        splitLayout.addToSecondary(formLayout);
    }

    private void createButtonSimLayout(VerticalLayout areaSim) {
        HorizontalLayout buttonSimLayout = new HorizontalLayout();
        buttonSimLayout.setWidth("100%");
        buttonSimLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Alinea los componentes a la derecha
        resetButton = new Button("Reiniciar", new Icon(VaadinIcon.REFRESH));
        resetButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        resetButton.addClickListener(event->{
            coeficienteSim.clear();
            valorSim.clear();
            cuotasSim.clear();
            montoSim.clear();
            gridSimulador.setItems(new ArrayList<>());
        });
        simularButton = new Button("Simular", new Icon(VaadinIcon.BUILDING));
        simularButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        buttonSimLayout.add(resetButton, simularButton); 
        areaSim.add(buttonSimLayout, gridSimulador);
    }

    private void createButtonLayout(VerticalLayout formLayout) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(save, cancel, delete);
        formLayout.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        HorizontalLayout searchHorizontalLayout = new HorizontalLayout();
        createHorizontalSearchLayout(searchHorizontalLayout);
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(searchHorizontalLayout, grid);
    }

    private void refreshGrid() {
        grid.select(null);
        dataView = grid.setItems(formasDePagoService.formasDePagoList());
        searchFilter(dataView);

    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(FormasDePago value) {
        this.formasDePago = value;
        String topic = null;
        if (this.formasDePago != null) {
            topic = "formasDePago/" + this.formasDePago.getIdFormasPago();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.formasDePago);
        avatarGroup.setTopic(topic);

    }

    private void simular() {
        List<Coeficientes> coeficientesList = new ArrayList<>();
        int cuotas = Integer.parseInt(cuotasSim.getValue());
        BigDecimal monto = montoSim.getValue();
        BigDecimal coeficiente = valorSim.getValue();
        BigDecimal cuota = monto.divide(new BigDecimal(cuotas), 2, RoundingMode.HALF_UP); // 2 decimales
        BigDecimal cuotCoef = cuota.multiply(coeficiente).divide(new BigDecimal(100));
        BigDecimal cuotaTotal = cuotCoef.add(cuota).setScale(2, RoundingMode.HALF_UP); // 2 decimales
        LocalDate fecha = LocalDate.now();

        for (int i = 1; i <= cuotas; i++) {
            Coeficientes coef = new Coeficientes();
            coef.setCuotas((short) i);
            coef.setFecha(java.sql.Date.valueOf(fecha.plusMonths(i - 1)));
            coef.setMonto(cuotaTotal);
            coeficientesList.add(coef);
        }

        gridSimulador.setItems(coeficientesList);
    }

    private void toggleSimularButton() {
        boolean isValid = coeficienteSim.getValue() != null && valorSim.getValue() != null
                && !cuotasSim.isEmpty() && montoSim.getValue() != null;
        simularButton.setEnabled(isValid);
    }
}
