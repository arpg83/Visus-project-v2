package com.ideadistribuidora.visus.views.proveedores;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;

import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Proveedores;
import com.ideadistribuidora.visus.data.ProveedoresBancos;
import com.ideadistribuidora.visus.data.ProveedoresBancosId;
import com.ideadistribuidora.visus.data.enums.SituacionFiscalEnum;
import com.ideadistribuidora.visus.services.ProveedoresService;
import com.ideadistribuidora.visus.views.clientes.ClientesView;
import com.ideadistribuidora.visus.views.dialogs.DialogConfirmacion;
import com.ideadistribuidora.visus.views.dialogs.DialogSaveEditBancos;
import com.ideadistribuidora.visus.views.dialogs.DialogSaveEditDomicilios;
import com.ideadistribuidora.visus.views.filters.ProveedorFilter;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
import com.ideadistribuidora.visus.views.utils.StringToLongConverter;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Proveedores")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 1)
@Route(value = "2/:proveedoresID?/:action?(edit)")
public class ProveedoresView extends Div implements BeforeEnterObserver {

        private final String PROVEEDORES_ID = "proveedoresID";
        private final String PROVEEDORES_EDIT_ROUTE_TEMPLATE = "2/%s/edit";

        private final Grid<Proveedores> grid = new Grid<>(Proveedores.class, false);
        private final Grid<Domicilios> gridDomicilios = new Grid<>(Domicilios.class, false);
        private final Grid<ProveedoresBancos> gridBancos = new Grid<>(ProveedoresBancos.class, false);

        CollaborationAvatarGroup avatarGroup;

        private ComboBox<Integer> idDocumento;
        private TextField numero;
        private TextField nombreFantasia;
        private TextField nombreReal;
        private TextField telefono1;
        private TextField telefono2;
        private TextField telefono3;
        private ComboBox<SituacionFiscalEnum> situacionFiscal;
        private EmailField email;
        private GridListDataView<Proveedores> dataView;
        private Button addDomicilioButton;
        private Button deleteDomicilioButton;
        private Button editDomicilioButton;
        private Button refreshDomicilioButton;
        private Button addBancoButton;
        private Button deleteBancoButton;
        private Button editBancoButton;
        private Button refreshBancoButton;

        private final Button cancel = new Button("Cancelar");
        private final Button save = new Button("Grabar");
        private final Button delete = new Button("Eliminar");

        private final CollaborationBinder<Proveedores> binder;

        private Proveedores proveedores;

        private Set<Domicilios> domList = new HashSet<>();
        private Set<ProveedoresBancos> bancosList = new HashSet<>();
        private Domicilios domSelected;
        private Domicilios domiciliosMod;
        private ProveedoresBancos proveedoresBancosSelected;
        private ProveedoresBancos proveedoresBancosMod;
        private DialogSaveEditDomicilios dialogSaveEditDomicilios;
        private DialogSaveEditBancos<ProveedoresBancos> dialogSaveEditBancos;
        private boolean domChanges = false;
        private boolean bancoChanges = false;

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
                splitLayout.setSplitterPosition(60);

                avatarGroup = new CollaborationAvatarGroup(userInfo, null);
                avatarGroup.getStyle().set("visibility", "hidden");

                createGridLayout(splitLayout);
                createEditorLayout(splitLayout);

                add(splitLayout);

                // Configure Grid

                Grid.Column<Proveedores> nomfantCol = grid.addColumn(createProveedoresRenderer())
                                .setHeader("Nombre Fantasía")
                                .setAutoWidth(true);
                Grid.Column<Proveedores> nomRealCol = grid.addColumn("nombreReal").setAutoWidth(true);
                dataView = grid.setItems(proveedoresService.proveedList());
                ProveedorFilter proveedorFilter = new ProveedorFilter(dataView);
                grid.getHeaderRows().clear();
                HeaderRow headerRow = grid.appendHeaderRow();

                headerRow.getCell(nomfantCol).setComponent(
                                createFilterHeader(proveedorFilter::setNombreFantasia,
                                                "Buscar por Nombre de Fantasía"));
                headerRow.getCell(nomRealCol).setComponent(
                                createFilterHeader(proveedorFilter::setNombreReal, "Buscar por Nombre Real"));
                grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

                // when a row is selected or deselected, populate form
                grid.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                UI.getCurrent()
                                                .navigate(String.format(PROVEEDORES_EDIT_ROUTE_TEMPLATE,
                                                                event.getValue().getIdProveedor()));
                                domList = proveedoresService.getDomByIDProveedor(event.getValue().getIdProveedor());
                                gridDomicilios.setItems(domList);
                                addDomicilioButton.setEnabled(true);
                                bancosList = proveedoresService
                                                .getBancosByIdProveedor(event.getValue().getIdProveedor());
                                gridBancos.setItems(bancosList);
                                addBancoButton.setEnabled(true);
                                delete.setEnabled(true);
                        } else {
                                gridDomicilios.setItems();
                                gridBancos.setItems();
                                clearForm();
                                addDomicilioButton.setEnabled(false);
                                addBancoButton.setEnabled(false);
                                delete.setEnabled(true);
                                UI.getCurrent().navigate(ProveedoresView.class);
                        }
                });

                gridDomicilios.addColumn(domicilios -> domicilios.getTipoDomicilio().getDisplayTipoDomicilio())
                                .setHeader("Tipo de Domicilio").setAutoWidth(true);
                gridDomicilios.addColumn(domicilios -> domicilios.getCalle() + " " + domicilios.getNumero())
                                .setHeader("Direccion").setAutoWidth(true);
                gridDomicilios.addColumn(domicilios -> domicilios.getLocalidad().getNombre()).setHeader("Localidad")
                                .setAutoWidth(true);
                gridDomicilios.addColumn(domicilios -> domicilios.getLocalidad().getDepartamentos().getProvincias()
                                .getProvincia())
                                .setHeader("Provincia").setAutoWidth(true);
                gridDomicilios.setItems(domList);
                gridDomicilios.addThemeVariants(GridVariant.LUMO_COMPACT);
                // when a row is selected or deselected, populate form
                gridDomicilios.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                editDomicilioButton.setEnabled(true);
                                deleteDomicilioButton.setEnabled(true);
                                domSelected = event.getValue();
                                addDomicilioButton.setEnabled(false);

                        } else {
                                editDomicilioButton.setEnabled(false);
                                deleteDomicilioButton.setEnabled(false);
                                addDomicilioButton.setEnabled(true);
                        }
                });
                gridBancos.addColumn(clientesBancos -> clientesBancos.getBancos().getNombre())
                                .setHeader("Banco").setAutoWidth(true);
                gridBancos.addColumn(clientesBancos -> clientesBancos.getTipoCuenta()).setHeader("Tipo de Cuenta")
                                .setAutoWidth(true);
                gridBancos.addColumn(clientesBancos -> clientesBancos.getCbu()).setHeader("CBU")
                                .setAutoWidth(true);
                gridBancos.addColumn(clientesBancos -> clientesBancos.getAlias()).setHeader("Alias").setAutoWidth(true);
                gridBancos.setItems(bancosList);
                gridBancos.addThemeVariants(GridVariant.LUMO_COMPACT);
                // when a row is selected or deselected, populate form
                gridBancos.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                editBancoButton.setEnabled(true);
                                deleteBancoButton.setEnabled(true);
                                proveedoresBancosSelected = event.getValue();
                                addBancoButton.setEnabled(false);

                        } else {
                                editBancoButton.setEnabled(false);
                                deleteBancoButton.setEnabled(false);
                                addBancoButton.setEnabled(true);
                        }
                });

                // Configure Form
                binder = new CollaborationBinder<>(Proveedores.class, userInfo);

                // Bind fields. This is where you'd define e.g. validation rules
                binder.forField(idDocumento).asRequired("Tipo de Documento es Requerido")
                                .bind("idDocumento");

                binder.forField(numero, String.class).asRequired("Número de Documento Requerido")
                                .withConverter(new StringToLongConverter())
                                .bind("numero");

                binder.forField(nombreFantasia).asRequired("Nombre Fantasía es Requerido")
                                .bind("nombreFantasia");
                binder.forField(nombreReal).asRequired("Nombre Cliente es Requerido")
                                .bind("nombreReal");
                binder.forField(situacionFiscal).asRequired("Situación Fiscal Requerido")
                                .bind("situacionFiscal");
                binder.addStatusChangeListener(
                                event -> save.setEnabled(binder.isValid()));

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
                                if (this.domList.size() > 0 && domChanges) {
                                        if (this.proveedores.getDomicilios() != null) {
                                                this.proveedores.getDomicilios().clear();
                                        }
                                        proveedoresService.saveDomList(this.domList);
                                        this.proveedores.setDomicilios(this.domList);
                                }

                                if (this.bancosList.size() > 0 && bancoChanges) {
                                        this.proveedores.setProveedoresBancos(bancosList);
                                }
                                binder.writeBean(this.proveedores);
                                proveedoresService.update(this.proveedores);
                                clearForm();
                                refreshGrid();
                                Notification.show("Datos Guardados");
                                UI.getCurrent().navigate(ProveedoresView.class);
                        } catch (ObjectOptimisticLockingFailureException exception) {
                                Notification n = Notification.show(
                                                "Error updating the data. Somebody else has updated the record while you were making changes.");
                                n.setPosition(Position.MIDDLE);
                                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        } catch (ValidationException validationException) {
                                Notification.show("Failed to update the data. Check again that all values are valid");
                        } catch (JpaSystemException exep) {
                                Notification n = Notification.show(
                                                "Error al Actualizar los datos: " + exep.getMessage());
                                n.setPosition(Position.MIDDLE);
                                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        } catch (Exception exep) {
                                Notification n = Notification.show(
                                                "Error al Actualizar los datos: " + exep.getMessage());
                                n.setPosition(Position.MIDDLE);
                                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                });
                delete.addClickListener(e -> {
                        try {
                                if (this.proveedores == null) {
                                        this.proveedores = new Proveedores();
                                }
                                binder.writeBean(this.proveedores);
                                proveedoresService.delete(this.proveedores.getIdProveedor());
                                proveedoresService.deleteAllDom(this.proveedores.getDomicilios());
                                clearForm();
                                refreshGrid();
                                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                                UI.getCurrent().navigate(ClientesView.class);
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

        private Component createFilterHeader(Consumer<String> filterChangeConsumer, String columnaBuscar) {
                TextField search = new TextField();
                search.setPlaceholder(columnaBuscar);
                search.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                search.setValueChangeMode(ValueChangeMode.EAGER);
                search.setClearButtonVisible(true);
                search.addThemeVariants(TextFieldVariant.LUMO_SMALL);
                search.setWidthFull();
                search.getStyle().set("max-width", "100%");
                search.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue()));
                VerticalLayout layout = new VerticalLayout(search);
                layout.getThemeList().clear();
                layout.getThemeList().add("spacing-xs");
                return layout;
        }

        private LitRenderer<Proveedores> createProveedoresRenderer() {
                return LitRenderer.<Proveedores>of(
                                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                                + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                                                + "  <span> ${item.fullName} </span>"
                                                + "</vaadin-horizontal-layout>")

                                .withProperty("fullName", Proveedores::getNombreFantasia);
        }

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
                Optional<Integer> proveedoresId = event.getRouteParameters().get(PROVEEDORES_ID).map(Integer::parseInt);
                if (proveedoresId.isPresent()) {
                        Optional<Proveedores> proveedoresFromBackend = proveedoresService.get(proveedoresId.get());
                        if (proveedoresFromBackend.isPresent()) {
                                populateForm(proveedoresFromBackend.get());
                        } else {
                                Notification.show(
                                                String.format("The requested proveedores was not found, ID = %d",
                                                                proveedoresId.get()),
                                                3000,
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
                save.setEnabled(false);
                delete.setEnabled(false);
                Div editorDiv = new Div();
                editorDiv.setClassName("editor");
                editorLayoutDiv.add(editorDiv);

                FormLayout formLayout = new FormLayout();
                idDocumento = new ComboBox<>("Id Documento");
                idDocumento.setPlaceholder("Seleccione el Tipo de documento");
                Map<Integer, String> docsMap = proveedoresService.getDocumentList();
                idDocumento.setItems(docsMap.keySet());
                idDocumento.setItemLabelGenerator(itemId -> docsMap.get(itemId));
                numero = new TextField("Numero de Documento");
                nombreFantasia = new TextField("Nombre de Fantasia");
                nombreFantasia.setMaxLength(50);
                nombreReal = new TextField("Nombre Real");
                nombreReal.setMaxLength(50);
                telefono1 = ComponentUtils.validatePhone(telefono1, "Teléfono Móvil");
                telefono2 = ComponentUtils.validatePhone(telefono2, "Teléfono Secundario");
                telefono3 = ComponentUtils.validatePhone(telefono3, "Otro Teléfono");
                situacionFiscal = new ComboBox<SituacionFiscalEnum>("Situación Fiscal");
                situacionFiscal.setPlaceholder("Seleccione Situación Fiscal");
                situacionFiscal.setItems(SituacionFiscalEnum.values());
                situacionFiscal.setItemLabelGenerator(SituacionFiscalEnum::getDisplaySituacionFiscal);
                email = ComponentUtils.validateEmail(email);
                formLayout.add(nombreFantasia, nombreReal, idDocumento, numero, situacionFiscal, telefono1, telefono2,
                                telefono3, email);

                editorDiv.add(avatarGroup, formLayout);
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
                FlexLayout flexLayout = new FlexLayout();
                flexLayout.setFlexDirection(FlexLayout.FlexDirection.COLUMN); // Coloca los elementos en columna

                // Añadimos padding y margen al FlexLayout
                flexLayout.getStyle().set("padding", "20px"); // Padding de 20px dentro del FlexLayout
                // flexLayout.getStyle().set("margin", "10px"); // Margen de 10px alrededor del
                // FlexLayout
                flexLayout.getStyle().set("gap", "100px"); // Espaciado de 15px entre los componentes internos

                Div wrapper = new Div();
                wrapper.setClassName("grid-wrapper");
                wrapper.add(grid);
                H4 h4GridDom = new H4("Domicilios");
                h4GridDom.getStyle().set("margin-left", "20px");
                HorizontalLayout buttonDomLayot = new HorizontalLayout();
                createHorizontalDomicilioButtonDomLayout(buttonDomLayot);
                Div wrapper2 = new Div();
                wrapper2.setClassName("grid-wrapper2");
                wrapper2.add(h4GridDom, gridDomicilios, buttonDomLayot);
                H4 h4GridBancos = new H4("Bancos");
                h4GridBancos.setWidth("100%");
                h4GridBancos.getStyle().set("margin-left", "20px");
                HorizontalLayout buttonBancosLayot = new HorizontalLayout();
                createHorizontalBancosButtonDomLayout(buttonBancosLayot);
                Div wrapper3 = new Div();
                wrapper3.setClassName("grid-wrapper3");
                wrapper3.add(h4GridBancos, gridBancos, buttonBancosLayot);
                flexLayout.add(wrapper, wrapper2, wrapper3);
                splitLayout.addToPrimary(flexLayout);
        }

        private void createHorizontalBancosButtonDomLayout(HorizontalLayout buttonBancosLayout) {
                buttonBancosLayout.setClassName("button-layout");
                refreshBancoButton = new Button(new Icon(VaadinIcon.REFRESH));
                refreshBancoButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                refreshBancoButton.setAriaLabel("Refrescar");
                refreshBancoButton.addClickListener(e -> {
                        gridBancos.getDataProvider().refreshAll();
                });
                addBancoButton = new Button(new Icon(VaadinIcon.PLUS));
                addBancoButton.addClickListener(e -> {
                        proveedoresBancosSelected = null;
                        dialogSaveEditBancos = new DialogSaveEditBancos<>(proveedoresService.getAllBancos(),
                                        proveedoresService.getAllTipoCuentaEnums(),
                                        "Nuevo Banco", proveedoresBancosSelected, ProveedoresBancos.class,
                                        this::confirmSaveEditBancoFunction);

                        dialogSaveEditBancos.open();
                });
                addBancoButton.setEnabled(false);
                addBancoButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                addBancoButton.setAriaLabel("Agregar Banco");
                editBancoButton = new Button(new Icon(VaadinIcon.EDIT), e -> {
                        dialogSaveEditBancos = new DialogSaveEditBancos<>(
                                        proveedoresService.getAllBancos(), proveedoresService.getAllTipoCuentaEnums(),
                                        "Editar Banco", proveedoresBancosSelected, ProveedoresBancos.class,
                                        this::confirmSaveEditBancoFunction);
                        dialogSaveEditBancos.open();
                });
                editBancoButton.setEnabled(false);
                editBancoButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                editBancoButton.setAriaLabel("Editar Banco");
                deleteBancoButton = new Button(new Icon(VaadinIcon.TRASH), e -> {
                        new DialogConfirmacion("¿Está seguro que desea eliminar estos datos Bancarios?",
                                        this::deleteBancoFunction);

                });
                deleteBancoButton.setEnabled(false);
                deleteBancoButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                deleteBancoButton.setAriaLabel("Eliminar Banco");
                buttonBancosLayout.add(refreshBancoButton, addBancoButton, editBancoButton,
                                deleteBancoButton);
        }

        private void createHorizontalDomicilioButtonDomLayout(HorizontalLayout buttonDomLayout) {
                buttonDomLayout.setClassName("button-layout");
                refreshDomicilioButton = new Button(new Icon(VaadinIcon.REFRESH));
                refreshDomicilioButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                refreshDomicilioButton.setAriaLabel("Refrescar");
                refreshDomicilioButton.addClickListener(e -> {
                        gridDomicilios.getDataProvider().refreshAll();
                });
                addDomicilioButton = new Button(new Icon(VaadinIcon.PLUS));
                addDomicilioButton.addClickListener(e -> {
                        domSelected = null;
                        dialogSaveEditDomicilios = new DialogSaveEditDomicilios(
                                        proveedoresService.getAllTipoDomicilio(),
                                        proveedoresService.getAllLocalidades(),
                                        "Nuevo Domicilio",
                                        domSelected, this::confirmSaveEditDomFunction);
                        dialogSaveEditDomicilios.open();
                });
                addDomicilioButton.setEnabled(false);
                addDomicilioButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                addDomicilioButton.setAriaLabel("Agregar Domicilio");
                editDomicilioButton = new Button(new Icon(VaadinIcon.EDIT), e -> {
                        dialogSaveEditDomicilios = new DialogSaveEditDomicilios(
                                        proveedoresService.getAllTipoDomicilio(),
                                        proveedoresService.getAllLocalidades(), "Editar Domicilio", domSelected,
                                        this::confirmSaveEditDomFunction);
                        dialogSaveEditDomicilios.open();
                });
                editDomicilioButton.setEnabled(false);
                editDomicilioButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                editDomicilioButton.setAriaLabel("Editar Domicilio");
                deleteDomicilioButton = new Button(new Icon(VaadinIcon.TRASH), e -> {
                        new DialogConfirmacion("¿Está seguro que desea eliminar este domicilio?",
                                        this::deleteDomFunction);

                });
                deleteDomicilioButton.setEnabled(false);
                deleteDomicilioButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                deleteDomicilioButton.setAriaLabel("Eliminar Domicilio");
                buttonDomLayout.add(refreshDomicilioButton, addDomicilioButton, editDomicilioButton,
                                deleteDomicilioButton);
        }

        private void deleteDomFunction() {
                domList.remove(domSelected);
                this.proveedores.setDomicilios(domList);
                proveedoresService.update(this.proveedores);
                proveedoresService.deleteDomById(domSelected.getIdDomicilio());
                gridDomicilios.getDataProvider().refreshAll();
                addDomicilioButton.setEnabled(true);
                editDomicilioButton.setEnabled(false);
                deleteDomicilioButton.setEnabled(false);
                UI.getCurrent().navigate(ProveedoresView.class);
        }

        private void deleteBancoFunction() {
                bancosList.remove(proveedoresBancosSelected);
                this.proveedores.setProveedoresBancos(bancosList);
                // clientesService.update(this.clientes);
                proveedoresService.deleteProveedoresBancosById(proveedoresBancosSelected.getId());
                gridBancos.getDataProvider().refreshAll();
                addBancoButton.setEnabled(true);
                editBancoButton.setEnabled(false);
                deleteBancoButton.setEnabled(false);
                UI.getCurrent().navigate(ProveedoresView.class);
        }

        private void confirmSaveEditDomFunction(Domicilios dom) {
                if (domSelected != null) {
                        domiciliosMod = dom;
                        new DialogConfirmacion("¿Está seguro que desea Modificar este domicilio?",
                                        this::saveEditDomFunction);
                } else {
                        domList.add(dom);
                        gridDomicilios.getDataProvider().refreshAll();
                        domChanges = true;
                        dialogSaveEditDomicilios.close();
                }

        }

        private void confirmSaveEditBancoFunction(ProveedoresBancos proveedoresBancos) {
                if (proveedoresBancosSelected != null) {
                        proveedoresBancosMod = proveedoresBancos;
                        new DialogConfirmacion("¿Está seguro que desea Modificar estos datos Bancarios?",
                                        this::saveEditBancoFunction);
                } else {
                        ProveedoresBancosId bancosId = new ProveedoresBancosId(0,
                                        proveedoresBancos.getBancos().getIdBanco());
                        proveedoresBancos.setId(bancosId);
                        proveedoresBancos.setProveedores(proveedores);
                        bancosList.add(proveedoresBancos);
                        gridBancos.getDataProvider().refreshAll();
                        bancoChanges = true;
                        dialogSaveEditBancos.close();

                }

        }

        private void saveEditDomFunction() {
                domiciliosMod.setIdDomicilio(this.domSelected.getIdDomicilio());
                domList.remove(domSelected);
                domList.add(domiciliosMod);
                gridDomicilios.getDataProvider().refreshAll();
                domChanges = true;
                dialogSaveEditDomicilios.close();
        }

        private void saveEditBancoFunction() {
                ProveedoresBancosId bancosId = new ProveedoresBancosId(this.proveedores.getIdProveedor(),
                                proveedoresBancosMod.getBancos().getIdBanco());
                proveedoresBancosMod.setId(bancosId);
                proveedoresBancosMod.setProveedores(this.proveedores);
                bancosList.remove(proveedoresBancosSelected);
                bancosList.add(proveedoresBancosMod);
                gridBancos.getDataProvider().refreshAll();
                bancoChanges = true;
                dialogSaveEditBancos.close();
        }

        private void refreshGrid() {
                grid.select(null);
                grid.setItems(proveedoresService.proveedList());
                gridDomicilios.getDataProvider().refreshAll();
                gridBancos.getDataProvider().refreshAll();
        }

        private void clearForm() {
                populateForm(null);
        }

        private void populateForm(Proveedores value) {
                this.proveedores = value;
                String topic = null;
                if (this.proveedores != null) {
                        topic = "proveedores/" + this.proveedores.getIdProveedor();
                        avatarGroup.getStyle().set("visibility", "visible");
                } else {
                        avatarGroup.getStyle().set("visibility", "hidden");
                }
                binder.setTopic(topic, () -> this.proveedores);
                avatarGroup.setTopic(topic);

        }
}
