package com.ideadistribuidora.visus.views.porcentuales;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Departamentos;
import com.ideadistribuidora.visus.data.Porcentuales;
import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.data.enums.ClasificacionEnum;
import com.ideadistribuidora.visus.services.PorcentualesService;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Porcentuales")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 16)
@Route(value = "17/:porcentualesID?/:action?(edit)")
public class PorcentualesView extends Div implements BeforeEnterObserver {

    private final String PORCENTUALES_ID = "porcentualesID";
    private final String PORCENTUALES_EDIT_ROUTE_TEMPLATE = "17/%s/edit";

    private final Grid<Porcentuales> grid = new Grid<>(Porcentuales.class, false);

    CollaborationAvatarGroup avatarGroup;

    private TextField descripcion;
    private BigDecimalField porcentual;
    private DatePicker inicioVigencia;
    private DatePicker finVigencia;
    private ComboBox<ClasificacionEnum> clasificacion;
    private TextField searchDescripcion;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Grabar");
    private final Button delete = new Button("ELiminar");

    private CollaborationBinder<Porcentuales> binder;

    private Porcentuales porcentuales;

    private final PorcentualesService porcentualesService;
  
    private GridListDataView<Porcentuales> dataView;

    public PorcentualesView(PorcentualesService porcentualesService) {
        this.porcentualesService = porcentualesService;
        addClassNames("porcentuales-view");

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
        grid.addColumn(createPorcentualesRenderer()).setHeader("Descripción").setAutoWidth(true);
        grid.addColumn(Porcentuales::getPorcentual).setHeader("Porcentual").setAutoWidth(true);
        grid.addColumn(Porcentuales::getInicioVigencia).setHeader("Inicio Vigencia").setAutoWidth(true);
        grid.addColumn(Porcentuales::getFinVigencia).setHeader("Fin Vigencia").setAutoWidth(true);
        grid.addColumn(Porcentuales::getClasificacion).setHeader("Clasificacion").setAutoWidth(true);
        dataView = grid.setItems(porcentualesService.localList());
        searchFilter(dataView);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                delete.setEnabled(true);
                UI.getCurrent().navigate(
                        String.format(PORCENTUALES_EDIT_ROUTE_TEMPLATE,
                                event.getValue().getIdPorcentual()));
            } else {
                delete.setEnabled(false);
                clearForm();
                UI.getCurrent().navigate(PorcentualesView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(Porcentuales.class, userInfo);
        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(descripcion).asRequired("Descripción es Requerido")
                .bind("descripcion");
        binder.forField(porcentual).asRequired("Porcentual es Requerido")
                .bind("porcentual");
        binder.forField(inicioVigencia).asRequired("Inicio Vigencia es Requerido")
                .bind("inicioVigencia");
        binder.forField(finVigencia).bind("finVigencia");
        binder.forField(clasificacion).asRequired("Clasificación es Requerido")
                .bind("clasificacion");

        binder.addStatusChangeListener(
                event -> save.setEnabled(binder.isValid()));

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
            delete.setEnabled(false);
            searchDescripcion.clear();
        });

        save.addClickListener(e -> {

            try {
                if (this.porcentuales == null) {
                    this.porcentuales = new Porcentuales();
                }
                // if (this.departamentos.getProvincias() == null) {
                // throw new Exception("Debe seleccionar una Provincia");
                // }
                binder.writeBean(this.porcentuales);
                porcentualesService.update(this.porcentuales);

                clearForm();
                refreshGrid();
                Notification.show("Datos Guardados");
                UI.getCurrent().navigate(PorcentualesView.class);
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
                                "El Porcentual " + this.porcentuales.getDescripcion()
                                        + " ya existe");
                        n.setPosition(Position.MIDDLE);
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                    if (e1.getMessage().contains("el valor nulo")) {
                        Notification n = Notification.show("Debe seleccionar una Provincia");
                        n.setPosition(Position.MIDDLE);
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }

            }
        });

        delete.addClickListener(e -> {
            try {
                if (this.porcentuales == null) {
                    this.porcentuales = new Porcentuales();
                }
                binder.writeBean(this.porcentuales);
                porcentualesService.delete(this.porcentuales.getIdPorcentual());
                clearForm();
                refreshGrid();
                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                UI.getCurrent().navigate(PorcentualesView.class);
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
    }

    private void searchFilter(GridListDataView<Porcentuales> dataView2) {
        dataView.addFilter(local -> {
            String searchTerm = searchDescripcion.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesFulldesc = matchesTerm(local.getDescripcion(),
                    searchTerm);

            return matchesFulldesc;
        });
    }

    private LitRenderer<Porcentuales> createPorcentualesRenderer() {
        return LitRenderer.<Porcentuales>of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                        + "  <span> ${item.fullName} </span>"
                        + "</vaadin-horizontal-layout>")

                .withProperty("fullName", Porcentuales::getDescripcion);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> porcentualesId = event.getRouteParameters().get(PORCENTUALES_ID)
                .map(Integer::parseInt);
        if (porcentualesId.isPresent()) {
            Optional<Porcentuales> porcentualesFromBackend = porcentualesService
                    .get(porcentualesId.get());
            if (porcentualesFromBackend.isPresent()) {
                populateForm(porcentualesFromBackend.get());
            } else {
                Notification.show(
                        String.format("La Localidad solicitada no fué encontrado, ID = %d",
                                porcentualesId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PorcentualesView.class);
            }
        }
    }

    private void createHorizontalSearchLayout(HorizontalLayout searchHorizontalLayout) {
        searchDescripcion = new TextField();
        searchDescripcion.setPlaceholder("Buscar por Descripción");
        searchDescripcion.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchDescripcion.setValueChangeMode(ValueChangeMode.EAGER);
        searchDescripcion.addValueChangeListener(e -> dataView.refreshAll());
        searchDescripcion.setWidth("500px");
        searchHorizontalLayout.add(searchDescripcion);
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");
        save.setEnabled(false);
        delete.setEnabled(false);
        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);
        FormLayout formLayout = new FormLayout();
        descripcion = new TextField("Descripción");
        descripcion.setMaxLength(50);
        porcentual = new BigDecimalField("Porcentual");
        clasificacion = new ComboBox<ClasificacionEnum>("Clasificacion");
        clasificacion.setPlaceholder("Seleccione Departamento");
        clasificacion.setItems(ClasificacionEnum.values());
        clasificacion.setItemLabelGenerator(ClasificacionEnum::getDisplayName);
        clasificacion.setRequired(true);
        clasificacion.setRequiredIndicatorVisible(true);
        inicioVigencia = new DatePicker("Inicio Vigencia");
        inicioVigencia.setI18n(ComponentUtils.getI18n());
        finVigencia = new DatePicker("FIn Vigencia");
        finVigencia.setI18n(ComponentUtils.getI18n());

        formLayout.add(descripcion, porcentual, inicioVigencia, finVigencia, clasificacion);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(save, cancel, delete);
        editorLayoutDiv.add(buttonLayout);
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
        dataView = grid.setItems(porcentualesService.localList());
        searchFilter(dataView);

    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Porcentuales value) {
        this.porcentuales = value;
        String topic = null;
        if (this.porcentuales != null) {
            topic = "porcentuales/" + this.porcentuales.getIdPorcentual();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.porcentuales);
        avatarGroup.setTopic(topic);

    }
}
