package com.ideadistribuidora.visus.views.listasPorcentajes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Listas;
import com.ideadistribuidora.visus.data.ListasPorcentuales;
import com.ideadistribuidora.visus.data.Porcentuales;
import com.ideadistribuidora.visus.services.ListasPorcentualesService;
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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Listas-Porcentuales")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 21)
@Route(value = "22/:listasPorcentualesID?/:action?(edit)")
public class ListasPorcentualesView extends Div implements BeforeEnterObserver {

    private final String LISTAS_ID = "listasPorcentualesID";
   // private final String LISTAS_EDIT_ROUTE_TEMPLATE = "21/%s/edit";

    private final Grid<ListasPorcentuales> grid = new Grid<>(ListasPorcentuales.class, false);

    CollaborationAvatarGroup avatarGroup;

    private ComboBox<Listas> listas;
    private ComboBox<Porcentuales> porcentuales;

    private final Button addList = new Button("Agregar a la Lista");

    private final Button imprimir = new Button("Imprimir");
    private final Button delete = new Button("Borrar");

    private CollaborationBinder<ListasPorcentuales> binder;

    private ListasPorcentuales listasPorcentuales;
    private List<ListasPorcentuales> listasPorcentualesSelected = new ArrayList<>();

    private ListasPorcentualesService listasPorcentualesService;

    public ListasPorcentualesView(ListasPorcentualesService listasPorcentualesService) {
        this.listasPorcentualesService = listasPorcentualesService;
        addClassNames("listasPorcentuales-view");

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
        splitLayout.setSplitterPosition(50);

        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "hidden");
        createEditorLayout(splitLayout);
        createGridLayout(splitLayout);
        
        add(splitLayout);

        // Configure Grid
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(listasPorcentuales ->listasPorcentuales.getLista().getDescripcion()).setHeader("Lista").setSortable(true).setAutoWidth(true);
        grid.addColumn(listasPorcentuales ->listasPorcentuales.getPorcentual().getDescripcion()).setHeader("Porcentual").setSortable(true).setAutoWidth(true);
        grid.addColumn(listasPorcentuales ->listasPorcentuales.getPorcentual().getPorcentual()).setHeader("%").setSortable(true).setAutoWidth(true);
        grid.addColumn(listasPorcentuales ->listasPorcentuales.getPorcentual().getInicioVigencia()).setHeader("Inicio Vigencia").setSortable(true).setAutoWidth(true);
        grid.addColumn(listasPorcentuales ->listasPorcentuales.getPorcentual().getFinVigencia()).setHeader("Fin Vigencia").setSortable(true).setAutoWidth(true);
        grid.addColumn(listasPorcentuales ->listasPorcentuales.getPorcentual().getClasificacion()).setHeader("Clasificación").setSortable(true).setAutoWidth(true);
        grid.setItems(listasPorcentualesService.listasPorcentualesList());
       
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        // when a row is selected or deselected, populate form
        grid.addSelectionListener(event -> {
            listasPorcentualesSelected.clear();
            listasPorcentualesSelected.addAll(event.getAllSelectedItems());
            delete.setEnabled(!listasPorcentualesSelected.isEmpty());
            imprimir.setEnabled(!listasPorcentualesSelected.isEmpty());
        });

        // Configure Form
        binder = new CollaborationBinder<>(ListasPorcentuales.class, userInfo);
        // Bind fields. This is where you'd define e.g. validation rules
        binder.setSerializer(Listas.class,
                        listas -> String.valueOf(listas.getIdLista()),
                        idListas -> listasPorcentualesService
                                .findListasById(Integer.parseInt(idListas)));
                        
        binder.forField(listas).asRequired("Listas es Requerida")
                .bind("lista");
        
        binder.setSerializer(Porcentuales.class,
                        porcentuales -> String.valueOf(porcentuales.getIdPorcentual()),
                        idPorcentuales -> listasPorcentualesService
                                .findPorcentualById(Integer.parseInt(idPorcentuales)));
        binder.forField(porcentuales).asRequired("Porcentuales es Requerida")
                .bind("porcentual");

        binder.addStatusChangeListener(
                event -> addList.setEnabled(binder.isValid()));

        binder.bindInstanceFields(this);

        // Configure buttons

        addList.addClickListener(e -> {

            try {
                if (this.listasPorcentuales == null) {
                    this.listasPorcentuales = new ListasPorcentuales();
                }
                binder.writeBean(this.listasPorcentuales);
                listasPorcentualesService.save(this.listasPorcentuales);

                clearForm();
                refreshGrid();
                Notification.show("Datos Guardados");
                UI.getCurrent().navigate(ListasPorcentualesView.class);
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
                                "La Presentacion " + this.listasPorcentuales.getLista()+ " - "+this.listasPorcentuales.getPorcentual()
                                        + " ya existe");
                        n.setPosition(Position.MIDDLE);
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }

            }
        });

        delete.addClickListener(e -> {
            try {
                listasPorcentualesService.delete(this.listasPorcentualesSelected);
                refreshGrid();
                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                UI.getCurrent().navigate(ListasPorcentualesView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al Eliminar los datos. Alguien mas está actualizando los datos.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Short> listasId = event.getRouteParameters().get(LISTAS_ID)
                .map(Short::parseShort);
        if (listasId.isPresent()) {
            Optional<ListasPorcentuales> listasFromBackend = listasPorcentualesService.get(listasId.get());
            if (listasFromBackend.isPresent()) {
                populateForm(listasFromBackend.get());
            } else {
                Notification.show(
                        String.format("La Presentación solicitada no fué encontrada, ID = %d",
                                listasId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ListasPorcentualesView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");
        addList.setEnabled(false);
        delete.setEnabled(false);
        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);
        FormLayout formLayout = new FormLayout();
        listas = new ComboBox<>("Listas");
        listas.setPlaceholder("Seleccione Listas");
        listas.setItems(listasPorcentualesService.findAllListas());
        listas.setItemLabelGenerator(Listas::getDescripcion);
        
        porcentuales = new ComboBox<>("Porcentuales");
        porcentuales.setPlaceholder("Seleccione Porcentuales");
        porcentuales.setItems(listasPorcentualesService.findAllPorcentuales());
        porcentuales.setItemLabelGenerator(Porcentuales::getDescripcion);

        formLayout.add(listas, porcentuales);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToPrimary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        addList.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(addList);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        HorizontalLayout buttonGridHorizontalLayout = new HorizontalLayout();
        createButtonGridHorizontalLayout(buttonGridHorizontalLayout);
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToSecondary(wrapper);
        wrapper.add(grid, buttonGridHorizontalLayout);
    }

    private void createButtonGridHorizontalLayout(HorizontalLayout buttonGridHorizontalLayout) {
        buttonGridHorizontalLayout.setClassName("button-grid-layout");
        imprimir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonGridHorizontalLayout.add(imprimir, delete);
        
    }

    private void refreshGrid() {
        grid.setItems(listasPorcentualesService.listasPorcentualesList());
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(ListasPorcentuales value) {
        this.listasPorcentuales = value;
        String topic = null;
        if (this.listasPorcentuales != null) {
            topic = "listasPorcentuales/" + this.listasPorcentuales.getIdLisPorc();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.listasPorcentuales);
        avatarGroup.setTopic(topic);

    }
}
