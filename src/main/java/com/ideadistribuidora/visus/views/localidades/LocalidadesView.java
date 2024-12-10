package com.ideadistribuidora.visus.views.localidades;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Departamentos;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.services.LocalidadesService;
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

@PageTitle("Localidades")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 23)
@Route(value = "24/:localidadesID?/:action?(edit)")
public class LocalidadesView extends Div implements BeforeEnterObserver {

    private final String LOCALIDADES_ID = "localidadesID";
    private final String LOCALIDADES_EDIT_ROUTE_TEMPLATE = "24/%s/edit";

    private final Grid<Localidades> grid = new Grid<>(Localidades.class, false);

    CollaborationAvatarGroup avatarGroup;

    private TextField nombre;
    private ComboBox<Departamentos> departamentos;
    private IntegerField codigoPostal;
    private TextField searchLocalidad;
    private TextField searchDepartamento;
    private ComboBox<Provincias> provincias;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Grabar");
    private final Button delete = new Button("ELiminar");

    private CollaborationBinder<Localidades> binder;

    private Localidades localidades;

    private final LocalidadesService localidadesService;
    private List<Departamentos> departamentosList = new ArrayList<>();
    GridListDataView<Localidades> dataView;
    private List<Provincias> provinciaList = new ArrayList<>();
    private Provincias provinciaValue = null;

    public LocalidadesView(LocalidadesService localidadesService) {
        this.localidadesService = localidadesService;
        addClassNames("localidades-view");

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
        grid.addColumn(createLocalidadesRenderer()).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Localidades::getCodigoPostal).setHeader("Código Postal").setAutoWidth(true);
        grid.addColumn(localidades -> localidades.getDepartamentos().getNombre()).setHeader("Departamento")
                .setAutoWidth(true);
        grid.addColumn(localidades -> localidades.getProvincias().getProvincia())
                .setHeader("Provincia").setAutoWidth(true);
        dataView = grid.setItems(localidadesService.localList());
        searchFilter(dataView);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                delete.setEnabled(true);
                provincias.setValue(event.getValue().getProvincias());
                UI.getCurrent().navigate(
                        String.format(LOCALIDADES_EDIT_ROUTE_TEMPLATE,
                                event.getValue().getIdLocalidad()));
            } else {
                delete.setEnabled(false);
                clearForm();
                UI.getCurrent().navigate(LocalidadesView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(Localidades.class, userInfo);
        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(nombre).asRequired("Nombre es Requerido")
                .bind("nombre");
        binder.forField(codigoPostal).asRequired("Código Postal de Requerido")
                .bind("codigoPostal");
        binder.setSerializer(Departamentos.class,
                departamentos -> String.valueOf(departamentos.getIdDepartamento()),
                id -> localidadesService
                        .findById(Integer.parseInt(id)));
        // binder.bind(departamentos, "departamentos");

        binder.setSerializer(Provincias.class,
                provincias -> String.valueOf(provincias.getIdProvincia()),
                idProvincia -> localidadesService
                        .findProvinciaById(Integer.parseInt(idProvincia)));
        // binder.bind(provincias, "provincias");

        binder.addStatusChangeListener(
                event -> save.setEnabled(binder.isValid()));

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
            delete.setEnabled(false);
            searchLocalidad.clear();
            searchDepartamento.clear();
            // save.setEnabled(false);
        });

        provincias.addValueChangeListener(event -> {
            Provincias selectedProvicias = event.getValue();
            if (selectedProvicias != null) {
                departamentos.setItems(localidadesService.findDptoByProvincias(selectedProvicias));
            } else {
                departamentos.clear();
                departamentos.setItems();
            }
        });

        save.addClickListener(e -> {

            try {
                if (this.localidades == null) {
                    this.localidades = new Localidades();
                }
                // if (this.departamentos.getProvincias() == null) {
                // throw new Exception("Debe seleccionar una Provincia");
                // }
                binder.writeBean(this.localidades);
                localidadesService.update(this.localidades);

                clearForm();
                refreshGrid();
                Notification.show("Datos Guardados");
                UI.getCurrent().navigate(LocalidadesView.class);
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
                                "El Departamento " + this.localidades.getNombre()
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
                if (this.localidades == null) {
                    this.localidades = new Localidades();
                }
                binder.writeBean(this.localidades);
                localidadesService.delete(this.localidades.getIdLocalidad());
                clearForm();
                refreshGrid();
                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                UI.getCurrent().navigate(LocalidadesView.class);
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

    private void searchFilter(GridListDataView<Localidades> dataView2) {
        dataView.addFilter(local -> {
            String searchTerm = searchLocalidad.getValue().trim();
            String searchTerm2 = searchDepartamento.getValue().trim();

            if (searchTerm.isEmpty() && searchTerm2.isEmpty())
                return true;

            boolean matchesFullName = matchesTerm(local.getNombre(),
                    searchTerm);
            boolean matchesDepartamentos = matchesTerm(local.getDepartamentos().getNombre(), searchTerm2);

            return matchesFullName && matchesDepartamentos;
        });
    }

    private LitRenderer<Localidades> createLocalidadesRenderer() {
        return LitRenderer.<Localidades>of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                        + "  <span> ${item.fullName} </span>"
                        + "</vaadin-horizontal-layout>")

                .withProperty("fullName", Localidades::getNombre);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> localidadesId = event.getRouteParameters().get(LOCALIDADES_ID)
                .map(Integer::parseInt);
        if (localidadesId.isPresent()) {
            Optional<Localidades> localidadesFromBackend = localidadesService
                    .get(localidadesId.get());
            if (localidadesFromBackend.isPresent()) {
                populateForm(localidadesFromBackend.get());
            } else {
                Notification.show(
                        String.format("La Localidad solicitada no fué encontrado, ID = %d",
                                localidadesId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(LocalidadesView.class);
            }
        }
    }

    private void createHorizontalSearchLayout(HorizontalLayout searchHorizontalLayout) {
        searchLocalidad = new TextField();
        searchLocalidad.setPlaceholder("Buscar por Departamento");
        searchLocalidad.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchLocalidad.setValueChangeMode(ValueChangeMode.EAGER);
        searchLocalidad.addValueChangeListener(e -> dataView.refreshAll());
        searchLocalidad.setWidth("500px");
        searchDepartamento = new TextField();
        searchDepartamento.setPlaceholder("Buscar por Provincia");
        searchDepartamento.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchDepartamento.setValueChangeMode(ValueChangeMode.EAGER);
        searchDepartamento.addValueChangeListener(e -> dataView.refreshAll());
        searchDepartamento.setWidth("500px");
        searchHorizontalLayout.add(searchLocalidad, searchDepartamento);
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
        nombre = new TextField("Nombre");
        nombre.setMaxLength(50);
        codigoPostal = new IntegerField("Código Postal");
        departamentos = new ComboBox<Departamentos>("Departamento");
        departamentos.setPlaceholder("Seleccione Departamento");
        departamentosList = localidadesService.getAllDepartamentos();
        departamentos.setItems(departamentosList);
        departamentos.setItemLabelGenerator(Departamentos::getNombre);
        departamentos.setRequired(true);
        departamentos.setRequiredIndicatorVisible(true);
        provincias = new ComboBox<Provincias>("Provincia");
        provincias.setPlaceholder("Seleccione Provincia");
        provinciaList = localidadesService.getAllProvincias();
        provincias.setItems(provinciaList);
        provincias.setItemLabelGenerator(Provincias::getProvincia);
        provincias.setRequired(true);
        provincias.setRequiredIndicatorVisible(true);

        formLayout.add(nombre, codigoPostal, provincias, departamentos);

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
        dataView = grid.setItems(localidadesService.localList());
        searchFilter(dataView);

    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Localidades value) {
        this.localidades = value;
        String topic = null;
        if (this.localidades != null) {
            topic = "localidades/" + this.localidades.getIdLocalidad();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.localidades);
        avatarGroup.setTopic(topic);

    }
}
