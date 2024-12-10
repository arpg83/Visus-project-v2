package com.ideadistribuidora.visus.views.departamentos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Departamentos;
import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.services.DepartamentosService;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Departamentos")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 22)
@Route(value = "23/:departamentosID?/:action?(edit)")
public class DepartamentosView extends Div implements BeforeEnterObserver {

        private final String DEPARTAMENTOS_ID = "departamentosID";
        private final String DEPARTAMENTOS_EDIT_ROUTE_TEMPLATE = "23/%s/edit";

        private final Grid<Departamentos> grid = new Grid<>(Departamentos.class, false);

        CollaborationAvatarGroup avatarGroup;

        private TextField nombre;
        private ComboBox<Provincias> provincia;
        private TextField searchDepart;
        private TextField searchProvincia;

        private final Button cancel = new Button("Cancelar");
        private final Button save = new Button("Grabar");
        private final Button delete = new Button("ELiminar");

        private CollaborationBinder<Departamentos> binder;

        private Departamentos departamentos;

        private final DepartamentosService departamentosService;
        private List<Provincias> provinciaList = new ArrayList<>();
        GridListDataView<Departamentos> dataView;

        public DepartamentosView(DepartamentosService departamentosService) {
                this.departamentosService = departamentosService;
                addClassNames("departamentos-view");

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
                grid.addColumn(createDepartamentoRenderer()).setHeader("Departamento").setAutoWidth(true);
                grid.addColumn(Departamentos::getNombreProvincia).setHeader("Nombre Provincia").setAutoWidth(true);
                dataView = grid.setItems(departamentosService.departList());
                searchFilter(dataView);
                grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
                // when a row is selected or deselected, populate form
                grid.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                delete.setEnabled(true);
                                UI.getCurrent().navigate(
                                                String.format(DEPARTAMENTOS_EDIT_ROUTE_TEMPLATE,
                                                                event.getValue().getIdDepartamento()));
                        } else {
                                delete.setEnabled(false);
                                clearForm();
                                UI.getCurrent().navigate(DepartamentosView.class);
                        }
                });

                // Configure Form
                binder = new CollaborationBinder<>(Departamentos.class, userInfo);
                // Bind fields. This is where you'd define e.g. validation rules
                binder.forField(nombre).asRequired("Departamento es Requerido")
                                .bind("nombre");

                binder.setSerializer(Provincias.class,
                                provincias -> String.valueOf(provincias.getIdProvincia()),
                                idProvincia -> departamentosService
                                                .findById(Integer.parseInt(idProvincia)));

                binder.bind(provincia, "provincias");

                binder.addStatusChangeListener(
                                event -> save.setEnabled(binder.isValid()));

                binder.bindInstanceFields(this);

                cancel.addClickListener(e -> {
                        clearForm();
                        refreshGrid();
                        searchDepart.clear();
                        searchProvincia.clear();
                        delete.setEnabled(false);
                        // save.setEnabled(false);
                });

                save.addClickListener(e -> {

                        try {
                                if (this.departamentos == null) {
                                        this.departamentos = new Departamentos();
                                }
                                // if (this.departamentos.getProvincias() == null) {
                                // throw new Exception("Debe seleccionar una Provincia");
                                // }
                                binder.writeBean(this.departamentos);
                                departamentosService.update(this.departamentos);

                                clearForm();
                                refreshGrid();
                                Notification.show("Datos Guardados");
                                UI.getCurrent().navigate(DepartamentosView.class);
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
                                                                "El Departamento " + this.departamentos.getNombre()
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
                                if (this.departamentos == null) {
                                        this.departamentos = new Departamentos();
                                }
                                binder.writeBean(this.departamentos);
                                departamentosService.deleteLocalidadesByDepartamentos(this.departamentos);
                                departamentosService.delete(this.departamentos.getIdDepartamento());
                                clearForm();
                                refreshGrid();
                                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                                UI.getCurrent().navigate(DepartamentosView.class);
                        } catch (ObjectOptimisticLockingFailureException exception) {
                                Notification n = Notification.show(
                                                "Error al Eliminar los datos. Alguien mas está actualizando los datos.");
                                n.setPosition(Position.MIDDLE);
                                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        } catch (ValidationException validationException) {
                                Notification.show(
                                                "Error al Eliminar los datos. Revise Nuevamente que todos los datos sean Válidos");
                        } catch (Exception except) {
                                Notification.show(
                                                "Error al intentar Eliminar los datos: " + except.getMessage());
                        }
                });
        }

        private void searchFilter(GridListDataView<Departamentos> dataView2) {
                dataView.addFilter(depart -> {
                        String searchTerm = searchDepart.getValue().trim();
                        String searchTerm2 = searchProvincia.getValue().trim();

                        if (searchTerm.isEmpty() && searchTerm2.isEmpty())
                                return true;

                        boolean matchesFullName = matchesTerm(depart.getNombre(),
                                        searchTerm);
                        boolean matchesProvincias = matchesTerm(depart.getNombreProvincia(), searchTerm2);

                        return matchesFullName && matchesProvincias;
                });
        }

        private LitRenderer<Departamentos> createDepartamentoRenderer() {
                return LitRenderer.<Departamentos>of(
                                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                                + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                                                + "  <span> ${item.fullName} </span>"
                                                + "</vaadin-horizontal-layout>")

                                .withProperty("fullName", Departamentos::getNombre);
        }

        private boolean matchesTerm(String value, String searchTerm) {
                return searchTerm == null || searchTerm.isEmpty()
                                || value.toLowerCase().contains(searchTerm.toLowerCase());
        }

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
                Optional<Integer> departamentosId = event.getRouteParameters().get(DEPARTAMENTOS_ID)
                                .map(Integer::parseInt);
                if (departamentosId.isPresent()) {
                        Optional<Departamentos> departamentosFromBackend = departamentosService
                                        .get(departamentosId.get());
                        if (departamentosFromBackend.isPresent()) {
                                populateForm(departamentosFromBackend.get());
                        } else {
                                Notification.show(
                                                String.format("El Departamento solicitado no fué encontrado, ID = %d",
                                                                departamentosId.get()),
                                                3000, Notification.Position.BOTTOM_START);
                                // when a row is selected but the data is no longer available,
                                // refresh grid
                                refreshGrid();
                                event.forwardTo(DepartamentosView.class);
                        }
                }
        }

        private void createHorizontalSearchLayout(HorizontalLayout searchHorizontalLayout) {
                searchDepart = new TextField();
                searchDepart.setPlaceholder("Buscar por Departamento");
                searchDepart.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                searchDepart.setValueChangeMode(ValueChangeMode.EAGER);
                searchDepart.addValueChangeListener(e -> dataView.refreshAll());
                searchDepart.setWidth("500px");
                searchProvincia = new TextField();
                searchProvincia.setPlaceholder("Buscar por Provincia");
                searchProvincia.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                searchProvincia.setValueChangeMode(ValueChangeMode.EAGER);
                searchProvincia.addValueChangeListener(e -> dataView.refreshAll());
                searchProvincia.setWidth("500px");
                searchHorizontalLayout.add(searchDepart, searchProvincia);
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
                nombre = new TextField("Departamento");
                nombre.setMaxLength(60);
                provincia = new ComboBox<Provincias>("Provincia");
                provincia.setPlaceholder("Seleccione Provincia");
                provinciaList = departamentosService.getAllProvincias();
                provincia.setItems(provinciaList);
                provincia.setItemLabelGenerator(Provincias::getProvincia);
                provincia.setRequired(true);
                provincia.setRequiredIndicatorVisible(true);

                formLayout.add(nombre, provincia);

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
                dataView = grid.setItems(departamentosService.departList());
                searchFilter(dataView);

        }

        private void clearForm() {
                populateForm(null);
        }

        private void populateForm(Departamentos value) {
                this.departamentos = value;
                String topic = null;
                if (this.departamentos != null) {
                        topic = "departamentos/" + this.departamentos.getIdDepartamento();
                        avatarGroup.getStyle().set("visibility", "visible");
                } else {
                        avatarGroup.getStyle().set("visibility", "hidden");
                }
                binder.setTopic(topic, () -> this.departamentos);
                avatarGroup.setTopic(topic);

        }
}
