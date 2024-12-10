package com.ideadistribuidora.visus.views.medidas;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Medidas;
import com.ideadistribuidora.visus.services.MedidasService;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Medidas")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 12)
@Route(value = "13/:medidasID?/:action?(edit)")
public class MedidasView extends Div implements BeforeEnterObserver {

    private final String MEDIDAS_ID = "medidasID";
    private final String MEDIDAS_EDIT_ROUTE_TEMPLATE = "13/%s/edit";

    private final Grid<Medidas> grid = new Grid<>(Medidas.class, false);

    CollaborationAvatarGroup avatarGroup;

    private TextField descripcion;
    private TextField abreviatura;
    private TextField searchDescripcion;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Grabar");
    private final Button delete = new Button("ELiminar");

    private CollaborationBinder<Medidas> binder;

    private Medidas medidas;

    private final MedidasService medidasService;
    GridListDataView<Medidas> dataView;

    public MedidasView(MedidasService medidasService) {
        this.medidasService = medidasService;
        addClassNames("medidas-view");

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
        grid.addColumn(createMedidasRenderer()).setHeader("Descripción").setAutoWidth(true);
        grid.addColumn(Medidas::getAbreviatura).setHeader("Abreviatura").setAutoWidth(true);
        dataView = grid.setItems(medidasService.medidasList());
        searchFilter(dataView);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                delete.setEnabled(true);
                UI.getCurrent().navigate(
                        String.format(MEDIDAS_EDIT_ROUTE_TEMPLATE,
                                event.getValue().getIdMedida()));
            } else {
                delete.setEnabled(false);
                clearForm();
                UI.getCurrent().navigate(MedidasView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(Medidas.class, userInfo);
        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(descripcion).asRequired("Descripción es Requerido")
                .bind("descripcion");
        binder.forField(abreviatura).asRequired("Abreviatura es Requerido")
                .bind("abreviatura");

        binder.addStatusChangeListener(
                event -> save.setEnabled(binder.isValid()));

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
            searchDescripcion.clear();
            delete.setEnabled(false);
            // save.setEnabled(false);
        });

        save.addClickListener(e -> {

            try {
                if (this.medidas == null) {
                    this.medidas = new Medidas();
                }
                binder.writeBean(this.medidas);
                medidasService.update(this.medidas);

                clearForm();
                refreshGrid();
                Notification.show("Datos Guardados");
                UI.getCurrent().navigate(MedidasView.class);
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
                                "La Medida " + this.medidas.getDescripcion()
                                        + " ya existe");
                        n.setPosition(Position.MIDDLE);
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }

            }
        });

        delete.addClickListener(e -> {
            try {
                if (this.medidas == null) {
                    this.medidas = new Medidas();
                }
                binder.writeBean(this.medidas);
                medidasService.delete(this.medidas.getIdMedida());
                clearForm();
                refreshGrid();
                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                UI.getCurrent().navigate(MedidasView.class);
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

    private void searchFilter(GridListDataView<Medidas> dataView2) {
        dataView.addFilter(med -> {
            String searchTerm = searchDescripcion.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesFullName = matchesTerm(med.getDescripcion(),
                    searchTerm);

            return matchesFullName;
        });
    }

    private LitRenderer<Medidas> createMedidasRenderer() {
        return LitRenderer.<Medidas>of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                        + "  <span> ${item.fullName} </span>"
                        + "</vaadin-horizontal-layout>")

                .withProperty("fullName", Medidas::getDescripcion);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Short> medidasId = event.getRouteParameters().get(MEDIDAS_ID)
                .map(Short::parseShort);
        if (medidasId.isPresent()) {
            Optional<Medidas> medidasFromBackend = medidasService.get(medidasId.get());
            if (medidasFromBackend.isPresent()) {
                populateForm(medidasFromBackend.get());
            } else {
                Notification.show(
                        String.format("La Medida solicitada no fué encontrada, ID = %d",
                                medidasId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MedidasView.class);
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
        descripcion.setMaxLength(60);
        abreviatura = new TextField("Abreviatura");
        abreviatura.setMaxLength(2);

        formLayout.add(descripcion, abreviatura);

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
        dataView = grid.setItems(medidasService.medidasList());
        searchFilter(dataView);

    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Medidas value) {
        this.medidas = value;
        String topic = null;
        if (this.medidas != null) {
            topic = "medidas/" + this.medidas.getIdMedida();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.medidas);
        avatarGroup.setTopic(topic);

    }
}
