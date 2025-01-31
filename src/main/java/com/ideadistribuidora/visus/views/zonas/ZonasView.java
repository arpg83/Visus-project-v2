package com.ideadistribuidora.visus.views.zonas;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Zonas;
import com.ideadistribuidora.visus.data.enums.VisitaEnum;
import com.ideadistribuidora.visus.services.ZonasService;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Zonas")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 4)
@Route(value = "5/:zonasID?/:action?(edit)")
public class ZonasView extends Div implements BeforeEnterObserver {

    private final String ZONAS_ID = "zonasID";
    private final String ZONAS_EDIT_ROUTE_TEMPLATE = "5/%s/edit";

    private final Grid<Zonas> grid = new Grid<>(Zonas.class, false);

    CollaborationAvatarGroup avatarGroup;
    private TextField descripcion;
    private TextArea areaGeografica;
    private ComboBox<VisitaEnum> frecuenciaVisita;
    private TextField searchDescripcion;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Grabar");
    private final Button delete = new Button("ELiminar");

    private CollaborationBinder<Zonas> binder;

    private Zonas zonas;

    private final ZonasService zonasService;
    GridListDataView<Zonas> dataView;

    public ZonasView(ZonasService zonasService) {
        this.zonasService = zonasService;
        addClassNames("zonas-view");

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
        grid.addColumn(createZonasRenderer()).setHeader("Descripción").setAutoWidth(true);
        grid.addColumn(Zonas::getFrecuenciaVisita).setHeader("Frecuencia Visita").setAutoWidth(true);
        dataView = grid.setItems(zonasService.zonasList());
        searchFilter(dataView);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                delete.setEnabled(true);
                UI.getCurrent().navigate(
                        String.format(ZONAS_EDIT_ROUTE_TEMPLATE,
                                event.getValue().getIdZona()));
            } else {
                delete.setEnabled(false);
                clearForm();
                UI.getCurrent().navigate(ZonasView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(Zonas.class, userInfo);
        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(descripcion).asRequired("Descripción es Requerido")
                .bind("descripcion");
        binder.forField(areaGeografica).asRequired("Area Geográfica es Requerido")
                .bind("areaGeografica");
        binder.forField(frecuenciaVisita).asRequired("Frecuencia de Visita es Requerido")
                .bind("frecuenciaVisita");

        binder.addStatusChangeListener(
                event -> save.setEnabled(binder.isValid()));

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
            delete.setEnabled(false);
        });

        save.addClickListener(e -> {

            try {
                if (this.zonas == null) {
                    this.zonas = new Zonas();
                }
                // if (this.departamentos.getProvincias() == null) {
                // throw new Exception("Debe seleccionar una Provincia");
                // }
                binder.writeBean(this.zonas);
                zonasService.update(this.zonas);

                clearForm();
                refreshGrid();
                Notification.show("Datos Guardados");
                UI.getCurrent().navigate(ZonasView.class);
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
                                "La Zona " + this.zonas.getDescripcion()
                                + " ya existe");
                        n.setPosition(Position.MIDDLE);
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }

            }
        });

        delete.addClickListener(e -> {
            try {
                if (this.zonas == null) {
                    this.zonas = new Zonas();
                }
                binder.writeBean(this.zonas);
                zonasService.delete(this.zonas.getIdZona());
                clearForm();
                refreshGrid();
                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                UI.getCurrent().navigate(ZonasView.class);
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

    private void searchFilter(GridListDataView<Zonas> dataView2) {
        dataView.addFilter(local -> {
            String searchTerm = searchDescripcion.getValue().trim();

            if (searchTerm.isEmpty()) {
                return true;
            }

            boolean matchesFullName = matchesTerm(local.getDescripcion(),
                    searchTerm);

            return matchesFullName;
        });
    }

    private LitRenderer<Zonas> createZonasRenderer() {
        return LitRenderer.<Zonas>of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                + "  <span> ${item.fullName} </span>"
                + "</vaadin-horizontal-layout>")
                .withProperty("fullName", Zonas::getDescripcion);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> zonasId = event.getRouteParameters().get(ZONAS_ID)
                .map(Integer::parseInt);
        if (zonasId.isPresent()) {
            Optional<Zonas> zonasFromBackend = zonasService
                    .get(zonasId.get());
            if (zonasFromBackend.isPresent()) {
                populateForm(zonasFromBackend.get());
            } else {
                Notification.show(
                        String.format("La Comisión solicitada no fué encontrado, ID = %d",
                                zonasId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ZonasView.class);
            }
        }
    }

    private void createHorizontalSearchLayout(HorizontalLayout searchHorizontalLayout) {
        searchDescripcion = new TextField();
        searchDescripcion.setPlaceholder("Buscar por Tipo de Comisión");
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
        descripcion.setMaxLength(100);
        areaGeografica = new TextArea("Area Geográfica");
        frecuenciaVisita = new ComboBox<>("Frecuencia de Visita");
        frecuenciaVisita.setPlaceholder("Seleccione Frecuencia de Visita");
        frecuenciaVisita.setItems(VisitaEnum.values());
        frecuenciaVisita.setItemLabelGenerator(VisitaEnum::getDisplayVisita);
        frecuenciaVisita.setRequired(true);
        frecuenciaVisita.setRequiredIndicatorVisible(true);
        formLayout.add(descripcion, areaGeografica, frecuenciaVisita);

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
        dataView = grid.setItems(zonasService.zonasList());
        searchFilter(dataView);

    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Zonas value) {
        this.zonas = value;
        String topic = null;
        if (this.zonas != null) {
            topic = "zonas/" + this.zonas.getIdZona();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.zonas);
        avatarGroup.setTopic(topic);

    }
}
