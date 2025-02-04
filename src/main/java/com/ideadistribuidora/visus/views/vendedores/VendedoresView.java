package com.ideadistribuidora.visus.views.vendedores;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;

import com.ideadistribuidora.visus.data.Comisiones;
import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.data.Vendedores;
import com.ideadistribuidora.visus.data.Zonas;
import com.ideadistribuidora.visus.data.enums.SituacionFiscalEnum;
import com.ideadistribuidora.visus.data.enums.TipoComisionEnum;
import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;
import com.ideadistribuidora.visus.services.VendedoresService;
import com.ideadistribuidora.visus.views.clientes.ClientesView;
import com.ideadistribuidora.visus.views.dialogs.DialogComisionesTramos;
import com.ideadistribuidora.visus.views.dialogs.DialogConfirmacion;
import com.ideadistribuidora.visus.views.dialogs.DialogSaveEditComisiones;
import com.ideadistribuidora.visus.views.dialogs.DialogSaveEditZonas;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
import com.ideadistribuidora.visus.views.utils.ShortField;
import com.ideadistribuidora.visus.views.utils.StringToLongConverter;
import com.ideadistribuidora.visus.views.utils.StringToShortConverter;
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
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Minus.Horizontal;

@PageTitle("Vendedores")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 2)
@Route(value = "3/:vendedoresID?/:action?(edit)")
public class VendedoresView extends Div implements BeforeEnterObserver {

        private final String VENDEDORES_ID = "vendedoresID";
        private final String VENDEDORES_EDIT_ROUTE_TEMPLATE = "3/%s/edit";

        private final Grid<Vendedores> grid = new Grid<>(Vendedores.class, false);
        private final Grid<Zonas> gridZonas = new Grid<>(Zonas.class, false);
        private final Grid<Comisiones> gridComisiones = new Grid<>(Comisiones.class, false);

        CollaborationAvatarGroup avatarGroup;

        private TextField searchNombre = new TextField();
        private TextField searchSitFiscal = new TextField();
        private TextField nombre;
        private ComboBox<Integer> idDocumento;
        private TextField numeroDeDocumento;
        private ComboBox<SituacionFiscalEnum> situacionFiscal;
        private TextField telefonoMovil;
        private TextField telefonoFijo;
        private EmailField email;
        ComboBox<TipoDomicilioEnum> tipoDomicilio;
        private ComboBox<Localidades> localidades;
        private TextField calle;
        private TextField numero;
        private TextField depto;
        private TextField manzana;
        private TextField casa;
        private TextField sector;
        private TextField oficina;
        private TextField lote;
        private TextField barrio;
        private GridListDataView<Vendedores> vendedoresDataView;
        private GridListDataView<Vendedores> zonasDataView;
        private GridListDataView<Vendedores> comisionesDataView;
        private Button refreshZonasButton; 
        private Button addZonasButton; 
        private Button editZonasButton;
        private Button deleteZonasButton;
        private Button addComisionesButton;
        private Button deleteComisionesButton;
        private Button editComisionesButton;
        private Button refreshComisionesButton;
        private boolean vendSelected = false;

        private final Button cancel = new Button("Cancelar");
        private final Button save = new Button("Grabar");
        private final Button delete = new Button("Eliminar");

        private final CollaborationBinder<Vendedores> binder;

        private Vendedores vendedores;
        private Zonas zonas;
        private Comisiones comisiones;

        private Set<Zonas> zonaList = new HashSet<>();
        private Set<Comisiones> comisionesList = new HashSet<>();
        private Zonas zonaSelected;
        private Zonas zonaMod;
        private Comisiones comisionesSelected;
        private Comisiones comisionesMod;
        private DialogSaveEditZonas dialogSaveEditZonas;
        private DialogSaveEditComisiones dialogSaveEditComisiones;
        private DialogComisionesTramos dialogComisionesTramos;
        private boolean zonaChanges = false;
        private boolean comisionesChanges = false;
        private final String EFFECT_CHANGES = "Para que los cambios surtan efecto debe hacer click en el boton Grabar";
        H4 h4ZonaWarning = new H4(EFFECT_CHANGES);
        H4 h4ComiWarning = new H4(EFFECT_CHANGES);

        private final VendedoresService vendedoresService;

        public VendedoresView(VendedoresService vendedoresService) {
                this.vendedoresService = vendedoresService;
                addClassNames("vendedores-view");

                // UserInfo is used by Collaboration Engine and is used to share details
                // of users to each other to able collaboration. Replace this with
                // information about the actual user that is logged, providing a user
                // identifier, and the user's real name. You can also provide the users
                // avatar by passing an url to the image as a third parameter, or by
                // configuring an `ImageProvider` to `avatarGroup`.
                UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

                // Create UI
                VerticalLayout principalLayout = new VerticalLayout();
                SplitLayout splitLayout = new SplitLayout();
                splitLayout.setSplitterPosition(60);

                avatarGroup = new CollaborationAvatarGroup(userInfo, null);
                avatarGroup.getStyle().set("visibility", "hidden");

                createGridLayout(splitLayout);
                createEditorLayout(splitLayout);
                principalLayout.add(splitLayout);
                createGridComisionesLayout(principalLayout);

                add(principalLayout);

                // Configure Grid

                grid.addColumn(createVendedoresRenderer())
                                .setHeader("Nombre")
                                .setAutoWidth(true);
                grid.addColumn(vendedores -> vendedores.getSituacionFiscal().getDisplaySituacionFiscal())
                                .setHeader("Situación Fiscal").setAutoWidth(true);
                vendedoresDataView = grid.setItems(vendedoresService.vendedoresList());
                searchFilter(vendedoresDataView);


               
                grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

                // when a row is selected or deselected, populate form
                grid.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                UI.getCurrent().navigate(String.format(VENDEDORES_EDIT_ROUTE_TEMPLATE,
                                                                event.getValue().getIdVendedor()));
                                addZonasButton.setEnabled(true);
                                zonaList = vendedoresService.getZonasByIdVendedores(event.getValue().getIdVendedor());
                                gridZonas.setItems(zonaList);
                                addComisionesButton.setEnabled(true);
                                comisionesList = vendedoresService.getComisionesByIdVendedores(event.getValue().getIdVendedor());
                                gridComisiones.setItems(comisionesList);
                                                                
                                delete.setEnabled(true);
                        } else {
                                gridComisiones.setItems();
                                gridZonas.setItems();
                                clearForm();
                                addZonasButton.setEnabled(false);
                                addComisionesButton.setEnabled(false);
                                delete.setEnabled(true);
                                UI.getCurrent().navigate(VendedoresView.class);
                        }
                });

                gridZonas.addColumn(zonas -> zonas.getDescripcion())
                                .setHeader("Descripción").setAutoWidth(true);
                gridZonas.addColumn(zonas -> zonas.getAreaGeografica())
                                .setHeader("Área Geográfica").setAutoWidth(true);
                gridZonas.addColumn(zonas -> zonas.getFrecuenciaVisita()).setHeader("Frecuencia de Visita")
                                .setAutoWidth(true);
                gridZonas.setItems(zonaList);
                gridZonas.addThemeVariants(GridVariant.LUMO_COMPACT);
                // when a row is selected or deselected, populate form
                gridZonas.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                editZonasButton.setEnabled(true);
                                deleteZonasButton.setEnabled(true);
                                zonaSelected = event.getValue();
                                addZonasButton.setEnabled(false);

                        } else {
                                editZonasButton.setEnabled(false);
                                deleteZonasButton.setEnabled(false);
                                addZonasButton.setEnabled(true);
                        }
                });
                gridComisiones.addColumn(comisiones-> comisiones.getTipoComision())
                                .setHeader("Tipo Comisión").setAutoWidth(true);
                gridComisiones.addColumn(comisiones -> comisiones.getVigenciaDesde()).setHeader("Vigencia Desde")
                                .setAutoWidth(true);
                gridComisiones.addColumn(comisiones -> comisiones.getVigenciaHasta()).setHeader("Vigencia Hasta")
                                .setAutoWidth(true);
                gridComisiones.addColumn(comisiones -> comisiones.getPorcentajeSobreImporte()).setHeader("Sobre Importe")
                                .setAutoWidth(true);
                gridComisiones.addColumn(comisiones -> comisiones.getPorcentajeImporteFijo()).setHeader("Importe Fijo")
                                .setAutoWidth(true);
                gridComisiones.addColumn(comisiones -> comisiones.getPorcentajeSobreMargen()).setHeader("Sobre Margen")
                                .setAutoWidth(true);
                gridComisiones.addComponentColumn(comisiones -> {
                        Button tramosButton;
                        if("Escalonada".equals(comisiones.getTipoComision().getDisplayTipoComision())){
                                tramosButton = new Button("Ver Tramos");
                                tramosButton.addClickListener(e -> {
                                        dialogComisionesTramos = new DialogComisionesTramos(vendedores.getIdVendedor(), vendedoresService);
                                        dialogComisionesTramos.open();
                                       
                                });
                        }else{
                                tramosButton = new Button("No Posee");
                                tramosButton.setEnabled(false);
                        }
                        return tramosButton;
                }).setHeader("Bonificado")
                                .setAutoWidth(true);
                gridComisiones.setItems(comisionesList);
                gridComisiones.addThemeVariants(GridVariant.LUMO_COMPACT);
                // when a row is selected or deselected, populate form
                gridComisiones.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                editComisionesButton.setEnabled(true);
                                deleteComisionesButton.setEnabled(true);
                                comisionesSelected = event.getValue();
                                addComisionesButton.setEnabled(false);

                        } else {
                                editComisionesButton.setEnabled(false);
                                deleteComisionesButton.setEnabled(false);
                                addComisionesButton.setEnabled(true);
                        }
                });

                // Configure Form
                binder = new CollaborationBinder<>(Vendedores.class, userInfo);

                // Bind fields. This is where you'd define e.g. validation rules
                binder.forField(idDocumento).asRequired("Tipo de Documento es Requerido")
                                .bind("idDocumento");

                binder.forField(numeroDeDocumento, String.class).asRequired("Número de Documento Requerido")
                                .withConverter(new StringToLongConverter())
                                .bind("numeroDeDocumento");

                binder.forField(nombre).asRequired("Nombre es Requerido")
                                .bind("nombre");
                binder.forField(situacionFiscal).asRequired("Situación Fiscal Requerido")
                                .bind("situacionFiscal");
                binder.forField(tipoDomicilio).asRequired("Tipo de Domicilio es Requerido")
                .bind("tipoDomicilio");
                binder.forField(sector).bind("sector");
                binder.forField(oficina).bind("oficina");
                binder.forField(numero,String.class)
                        .asRequired("Número es Requerido")
                        .withValidator(new RegexpValidator("El valor máximo permitido es 32767", "-?\\d{1,5}"))
                        .withConverter(new StringToShortConverter())
                        .bind("numero");
                binder.forField(manzana).bind("manzana");
                binder.forField(lote).bind("lote");
                binder.setSerializer(Localidades.class,
                        localidades -> String.valueOf(localidades.getIdLocalidad()),
                        idLocalidad -> vendedoresService
                        .findLocalidadesById(Integer.parseInt(idLocalidad)));
                binder.forField(depto).bind("depto");
                binder.forField(casa).bind("casa");
                binder.forField(calle).asRequired("Calle es Requerido").bind("calle");
                binder.forField(barrio).bind("barrio");
                binder.forField(telefonoMovil).bind("telefonoMovil");
                binder.forField(telefonoFijo).bind("telefonoFijo");
                binder.forField(email).bind("email");
                binder.addStatusChangeListener(
                                event -> save.setEnabled(binder.isValid()));

                binder.bindInstanceFields(this);

                cancel.addClickListener(e -> {
                        clearForm();
                        refreshGrid();
                });

                save.addClickListener(e -> {
                        try {
                                if (this.vendedores == null) {
                                        this.vendedores = new Vendedores();
                                }
                                Optional<Vendedores> vendedoresExist = vendedoresService
                                                .getVendedoresByIdDocumentoAndNumero(this.vendedores.getIdDocumento(),
                                                                this.vendedores.getNumeroDeDocumento());
                                if (vendedoresExist.isPresent() && vendedoresExist.get().getIdVendedor() != this.vendedores.getIdVendedor()) {
                                        Notification notification = Notification.show("Ya existe un Vendedor con el mismo Tipo de Documento y Número");
                                        notification.setPosition(Position.MIDDLE);
                                        notification.getElement().getStyle().set("color", "red");
                                }else{
                                        Domicilios domicilios = new Domicilios();
                                        domicilios.setTipoDomicilio(tipoDomicilio.getValue());
                                        domicilios.setCalle(calle.getValue());
                                        domicilios.setNumero(Short.parseShort(numero.getValue()));
                                        domicilios.setBarrio(barrio.getValue());
                                        domicilios.setManzana(manzana.getValue());
                                        domicilios.setCasa(casa.getValue());
                                        domicilios.setSector(sector.getValue());
                                        domicilios.setDepto(depto.getValue());
                                        domicilios.setOficina(oficina.getValue());
                                        domicilios.setLote(lote.getValue());
                                        domicilios.setLocalidad(localidades.getValue());
                                        domicilios.setDireccion("");
                                        domicilios = vendedoresService.saveDomicilios(domicilios);
                                        this.vendedores.setIdDomicilio(domicilios);

                                        if (this.zonaList.size() > 0 && zonaChanges) {
                                                if (this.vendedores.getZonas() != null) {
                                                        this.vendedores.getZonas().clear();
                                                }
                                                vendedoresService.saveZonaList(this.zonaList);
                                                this.vendedores.setZonas(this.zonaList);
                                        }

                                        if (this.comisionesList.size() > 0 && comisionesChanges) {
                                                if (this.vendedores.getComisiones() != null) {
                                                        this.vendedores.getComisiones().clear();
                                                }
                                                vendedoresService.saveComisionesList(this.comisionesList);
                                                this.vendedores.setComisiones(this.comisionesList);
                                        }
                                
                                        binder.writeBean(this.vendedores);
                                        vendedoresService.update(this.vendedores);
                                        h4ComiWarning.setVisible(false);
                                        h4ZonaWarning.setVisible(false);
                                        clearForm();
                                        refreshGrid();
                                        Notification.show("Datos Guardados");
                                        UI.getCurrent().navigate(VendedoresView.class);
                                }
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
                                if (this.vendedores == null) {
                                        this.vendedores = new Vendedores();
                                }
                                binder.writeBean(this.vendedores);
                                vendedoresService.delete(this.vendedores.getIdVendedor());
                                clearForm();
                                refreshGrid();
                                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                                UI.getCurrent().navigate(VendedoresView.class);
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
        private void searchFilter(GridListDataView<Vendedores> dataView2) {
                vendedoresDataView.addFilter(local -> {
                    String searchTerm = searchNombre.getValue().trim();
                    String searchTerm2 = searchSitFiscal.getValue().trim();
        
                    if (searchTerm.isEmpty() && searchTerm2.isEmpty())
                        return true;
        
                    boolean matchesFullName = matchesTerm(local.getNombre(),
                            searchTerm);
                    boolean matchesDepartamentos = matchesTerm(local.getSituacionFiscal().getDisplaySituacionFiscal(), searchTerm2);
        
                    return matchesFullName && matchesDepartamentos;
                });
            }


        private boolean matchesTerm(String value, String searchTerm) {
                return searchTerm == null || searchTerm.isEmpty()
                        || value.toLowerCase().contains(searchTerm.toLowerCase());
            }

        private LitRenderer<Vendedores> createVendedoresRenderer() {
                return LitRenderer.<Vendedores>of(
                                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                                + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                                                + "  <span> ${item.fullName} </span>"
                                                + "</vaadin-horizontal-layout>")

                                .withProperty("fullName", Vendedores::getNombre);
        }

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
                Optional<Integer> vendedoresId = event.getRouteParameters().get(VENDEDORES_ID).map(Integer::parseInt);
                if (vendedoresId.isPresent()) {
                        Optional<Vendedores> vendedoresFromBackend = vendedoresService.get(vendedoresId.get());
                        if (vendedoresFromBackend.isPresent()) {
                                populateForm(vendedoresFromBackend.get());
                        } else {
                                Notification.show(
                                                String.format("The requested vendedores was not found, ID = %d",
                                                                vendedoresId.get()),
                                                3000,
                                                Notification.Position.BOTTOM_START);
                                // when a row is selected but the data is no longer available,
                                // refresh grid
                                refreshGrid();
                                event.forwardTo(VendedoresView.class);
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
                idDocumento = new ComboBox<>("Tipo de Documento");
                idDocumento.setPlaceholder("Seleccione el Tipo de documento");
                Map<Integer, String> docsMap = vendedoresService.getDocumentList();
                idDocumento.setItems(docsMap.keySet());
                idDocumento.setItemLabelGenerator(itemId -> docsMap.get(itemId));
                idDocumento.addBlurListener(event -> {
                        if (idDocumento.isEmpty()) {
                                idDocumento.setErrorMessage("Tipo de Documento es Requerido");
                                idDocumento.setInvalid(true);
                        }
                });
                numeroDeDocumento = new TextField("Numero de Documento");
                numeroDeDocumento.addBlurListener(event -> {
                        if (numeroDeDocumento.isEmpty()) {
                                numeroDeDocumento.setErrorMessage("Número de Documento es Requerido");
                                numeroDeDocumento.setInvalid(true);
                        }
                });
                nombre = new TextField("Nombre");
                nombre.setMaxLength(60);
                nombre.getStyle().set("width", "calc(1ch * 60)");
                nombre.addBlurListener(event -> {
                        if (nombre.isEmpty()) {
                                nombre.setErrorMessage("Nombre es Requerido");
                                nombre.setInvalid(true);
                        }
                });
                telefonoMovil = ComponentUtils.validatePhone(telefonoMovil, "Teléfono Móvil");
                telefonoFijo = ComponentUtils.validatePhone(telefonoFijo, "Teléfono Secundario");
                situacionFiscal = new ComboBox<SituacionFiscalEnum>("Situación Fiscal");
                situacionFiscal.setPlaceholder("Seleccione Situación Fiscal");
                situacionFiscal.setItems(SituacionFiscalEnum.values());
                situacionFiscal.setItemLabelGenerator(SituacionFiscalEnum::getDisplaySituacionFiscal);
                situacionFiscal.addBlurListener(event -> {
                        if (situacionFiscal.isEmpty()) {
                                situacionFiscal.setErrorMessage("Situación Fiscal es Requerida");
                                situacionFiscal.setInvalid(true);
                        }
                });
                email = ComponentUtils.validateEmail(email);
                tipoDomicilio = new ComboBox<>("Tipo de Domicilio");
                tipoDomicilio.setPlaceholder("Seleccione Tipo de Domicilio");
                tipoDomicilio.setItems(TipoDomicilioEnum.values());
                tipoDomicilio.setItemLabelGenerator(TipoDomicilioEnum::getDisplayTipoDomicilio);
                tipoDomicilio.setRequiredIndicatorVisible(true);
                tipoDomicilio.addBlurListener(event -> {
                        if (tipoDomicilio.isEmpty()) {
                                tipoDomicilio.setErrorMessage("Tipo de Domicilio es Requerido");
                                tipoDomicilio.setInvalid(true);
                        }
                });
                localidades = new ComboBox<>("Localidad");
                localidades.setPlaceholder("Seleccione Localidad");
                localidades.setItems(vendedoresService.getAllLocalidades());
                localidades.setItemLabelGenerator(Localidades::getNombre);
                localidades.setRequiredIndicatorVisible(true);
                localidades.addBlurListener(event -> {
                        if (localidades.isEmpty()) {
                                localidades.setErrorMessage("Localidad es Requerido");
                                localidades.setInvalid(true);
                        }
                });
                calle = new TextField("Calle");
                calle.setMaxLength(60);
                calle.setRequiredIndicatorVisible(true);
                calle.addBlurListener(event -> {
                        if (calle.isEmpty()) {
                                calle.setErrorMessage("Calle es Requerida");
                                calle.setInvalid(true);
                        }
                });
                numero = new TextField("Número/Altura");
                numero.setMaxLength(5);
                numero.addBlurListener(event -> {
                        if (numero.isEmpty()) {
                                numero.setErrorMessage("Número/Altura es Requerido");
                                numero.setInvalid(true);
                        }
                });
                barrio = new TextField("Barrio");
                barrio.setMaxLength(60);
                manzana = new TextField("Manzana");
                manzana.setMaxLength(15);
                casa = new TextField("Casa");
                casa.setMaxLength(10);
                sector = new TextField("Sector");
                sector.setMaxLength(10);
                depto = new TextField("Departamento");
                depto.setMaxLength(10);
                oficina = new TextField("Oficina");
                oficina.setMaxLength(10);
                lote = new TextField("Lote");
                lote.setMaxLength(10);
                formLayout.add(nombre, idDocumento, numeroDeDocumento, situacionFiscal, tipoDomicilio, localidades,
                                calle, numero, barrio, manzana, casa, sector,depto,oficina,lote,telefonoMovil,telefonoFijo, email);

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
                HorizontalLayout searchHorizontalLayout = new HorizontalLayout();
                createHorizontalSearchLayout(searchHorizontalLayout);
                Div wrapper = new Div();
                wrapper.setClassName("grid-wrapper");
                wrapper.add(searchHorizontalLayout,grid);
                H4 h4GridZona = new H4("Zonas");
                h4GridZona.getStyle().set("margin-left", "20px");
                h4ZonaWarning.getStyle().setColor("red");
                h4ZonaWarning.setVisible(zonaChanges);
                HorizontalLayout headZonasLayot = new HorizontalLayout();
                headZonasLayot.add(h4GridZona, h4ZonaWarning);
                HorizontalLayout buttonZonasLayot = new HorizontalLayout();
                createHorizontalZonasButtonDomLayout(buttonZonasLayot);
                Div wrapper2 = new Div();
                wrapper2.setClassName("grid-wrapper2");
                wrapper2.add(headZonasLayot, gridZonas, buttonZonasLayot);
                flexLayout.add(wrapper, wrapper2);
                splitLayout.addToPrimary(flexLayout);
        }

        private void createGridComisionesLayout(VerticalLayout principalLayout){
                Div wrapper = new Div();
                wrapper.setClassName("grid-wrapper");
                H4 h4GridComisiones = new H4("Comisiones");
                h4GridComisiones.getStyle().set("margin-left", "20px");
                h4ComiWarning.getStyle().setColor("red");
                h4ComiWarning.setVisible(comisionesChanges);
                HorizontalLayout headComisionesLayot = new HorizontalLayout();
                headComisionesLayot.add(h4GridComisiones, h4ComiWarning);
                HorizontalLayout buttonComisionesLayot = new HorizontalLayout();
                createHorizontalComisionesButtonDomLayout(buttonComisionesLayot);
                wrapper.add(headComisionesLayot, gridComisiones, buttonComisionesLayot);
                principalLayout.add(wrapper);
        }

        private void createHorizontalSearchLayout(HorizontalLayout searchHorizontalLayout) {
                searchNombre.setPlaceholder("Buscar por Nombre");
                searchNombre.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                searchNombre.setValueChangeMode(ValueChangeMode.EAGER);
                searchNombre.addValueChangeListener(e -> vendedoresDataView.refreshAll());
                searchNombre.setWidth("500px");

                searchSitFiscal.setPlaceholder("Buscar por Situación Fiscal");
                searchSitFiscal.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                searchSitFiscal.setValueChangeMode(ValueChangeMode.EAGER);
                searchSitFiscal.addValueChangeListener(e -> vendedoresDataView.refreshAll());
                searchSitFiscal.setWidth("500px");

                searchHorizontalLayout.add(searchNombre, searchSitFiscal);
        }

        private void createHorizontalComisionesButtonDomLayout(HorizontalLayout buttonComisionesLayout) {
                buttonComisionesLayout.setClassName("button-layout");
                refreshComisionesButton = new Button(new Icon(VaadinIcon.REFRESH));
                refreshComisionesButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                refreshComisionesButton.setAriaLabel("Refrescar");
                refreshComisionesButton.addClickListener(e -> {
                        gridComisiones.getDataProvider().refreshAll();
                });
                addComisionesButton = new Button(new Icon(VaadinIcon.PLUS));
                addComisionesButton.addClickListener(e -> {
                        comisionesSelected = null;
                        dialogSaveEditComisiones = new DialogSaveEditComisiones("Nuevo Comisión", comisionesSelected,
                                        this::confirmSaveEditComisionesFunction);

                        dialogSaveEditComisiones.open();
                });
                addComisionesButton.setEnabled(false);
                addComisionesButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                addComisionesButton.setAriaLabel("Agregar Banco");
                editComisionesButton = new Button(new Icon(VaadinIcon.EDIT), e -> {
                        dialogSaveEditComisiones = new DialogSaveEditComisiones("Editar Comisión", comisionesSelected,
                                        this::confirmSaveEditComisionesFunction);
                        dialogSaveEditComisiones.open();
                });
                editComisionesButton.setEnabled(false);
                editComisionesButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                editComisionesButton.setAriaLabel("Editar Banco");
                deleteComisionesButton = new Button(new Icon(VaadinIcon.TRASH), e -> {
                        new DialogConfirmacion("¿Está seguro que desea eliminar esta Comisión?",
                                        this::deleteComisionesFunction);

                });
                deleteComisionesButton.setEnabled(false);
                deleteComisionesButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                deleteComisionesButton.setAriaLabel("Eliminar Banco");
                buttonComisionesLayout.add(refreshComisionesButton, addComisionesButton, editComisionesButton,
                                deleteComisionesButton);
        }

        private void createHorizontalZonasButtonDomLayout(HorizontalLayout buttonZonasLayout) {
                buttonZonasLayout.setClassName("button-layout");
               
                refreshZonasButton = new Button(new Icon(VaadinIcon.REFRESH));
                refreshZonasButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                refreshZonasButton.setAriaLabel("Refrescar");
                refreshZonasButton.addClickListener(e -> {
                        gridZonas.getDataProvider().refreshAll();
                });
                addZonasButton = new Button(new Icon(VaadinIcon.PLUS));
                addZonasButton.addClickListener(e -> {
                        zonaSelected = null;
                        dialogSaveEditZonas = new DialogSaveEditZonas(
                                        "Nueva Zona",
                                        zonaSelected, this::confirmSaveEditZonaFunction);
                        dialogSaveEditZonas.open();
                });
                addZonasButton.setEnabled(false);
                addZonasButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                addZonasButton.setAriaLabel("Nueva Zona");
                editZonasButton = new Button(new Icon(VaadinIcon.EDIT), e -> {
                        dialogSaveEditZonas = new DialogSaveEditZonas("Editar Zona", zonaSelected,
                                        this::confirmSaveEditZonaFunction);
                        dialogSaveEditZonas.open();
                });
                editZonasButton.setEnabled(false);
                editZonasButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                editZonasButton.setAriaLabel("Editar Zona");
                deleteZonasButton = new Button(new Icon(VaadinIcon.TRASH), e -> {
                        new DialogConfirmacion("¿Está seguro que desea eliminar esta Zona?",
                                        this::deleteZonaFunction);

                });
                deleteZonasButton.setEnabled(false);
                deleteZonasButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                deleteZonasButton.setAriaLabel("Eliminar Zona");
                buttonZonasLayout.add(refreshZonasButton, addZonasButton, editZonasButton,
                deleteZonasButton);
        }

        private void deleteZonaFunction() {
                zonaList.remove(zonaSelected);
                this.vendedores.setZonas(zonaList);
                vendedoresService.update(this.vendedores);
                vendedoresService.deleteZonaById(zonaSelected.getIdZona());
                gridZonas.getDataProvider().refreshAll();
                addZonasButton.setEnabled(true);
                editZonasButton.setEnabled(false);
                deleteZonasButton.setEnabled(false);
                UI.getCurrent().navigate(VendedoresView.class);
        }

        private void deleteComisionesFunction() {
                comisionesList.remove(comisionesSelected);
                this.vendedores.setComisiones(comisionesList);
                vendedoresService.update(this.vendedores);
                vendedoresService.deleteComisionesById(comisionesSelected.getIdComision());
                if("Escalonada".equals(comisionesSelected.getTipoComision().getDisplayTipoComision()))
                        vendedoresService.deleteComisionesTramosByIdVendedor(vendedores.getIdVendedor());
                gridComisiones.getDataProvider().refreshAll();
                addComisionesButton.setEnabled(true);
                editComisionesButton.setEnabled(false);
                deleteComisionesButton.setEnabled(false);
                UI.getCurrent().navigate(VendedoresView.class);
        }

        private void confirmSaveEditZonaFunction(Zonas zona) {
                if (zonaSelected != null) {
                        zonaMod = zona;
                        new DialogConfirmacion("¿Está seguro que desea Modificar esta Zona?",
                                        this::saveEditZonaFunction);
                } else {
                        zonaList.add(zona);
                        gridZonas.setItems(zonaList);
                        zonaChanges = true;
                        h4ZonaWarning.setVisible(zonaChanges);
                        dialogSaveEditZonas.close();
                }

        }

        private void confirmSaveEditComisionesFunction(Comisiones comisiones) {
                if (comisionesSelected != null) {
                        if("Escalonada".equals(comisionesSelected.getTipoComision().getDisplayTipoComision()) && !"Escalonada".equals(comisiones.getTipoComision().getDisplayTipoComision())){
                                vendedoresService.deleteComisionesTramosByIdVendedor(vendedores.getIdVendedor());
                        }
                        comisionesMod = comisiones;
                        new DialogConfirmacion("¿Está seguro que desea Modificar esta Comisión?",
                                        this::saveEditComisionesFunction);
                } else {
                        comisionesList.add(comisiones);
                        gridComisiones.setItems(comisionesList);
                        comisionesChanges = true;
                        h4ComiWarning.setVisible(comisionesChanges);
                        dialogSaveEditComisiones.close();
                }

        }

        private void saveEditZonaFunction() {
                zonaMod.setIdZona(this.zonaSelected.getIdZona());
                zonaList.remove(zonaSelected);
                zonaList.add(zonaMod);
                gridZonas.getDataProvider().refreshAll();
                zonaChanges = true;
                h4ZonaWarning.setVisible(zonaChanges);
                dialogSaveEditZonas.close();
        }

        private void saveEditComisionesFunction() {
                comisionesMod.setIdComision(this.comisionesSelected.getIdComision());
                comisionesList.remove(comisionesSelected);
                comisionesList.add(comisionesMod);
                gridComisiones.getDataProvider().refreshAll();
                comisionesChanges = true;
                h4ComiWarning.setVisible(comisionesChanges);
                dialogSaveEditComisiones.close();
        }

        private void refreshGrid() {
                grid.select(null);
                grid.setItems(vendedoresService.vendedoresList());
                gridZonas.getDataProvider().refreshAll();
                gridComisiones.getDataProvider().refreshAll();
        }

        private void clearForm() {
                populateForm(null);
        }

        private void populateForm(Vendedores value) {
                this.vendedores = value;
                String topic = null;
                if (this.vendedores != null) {
                        Domicilios domicilios = this.vendedores.getIdDomicilio();
                        if(domicilios != null){
                          this.vendedores.setTipoDomicilio(domicilios.getTipoDomicilio());
                          this.vendedores.setCalle(domicilios.getCalle());
                          this.vendedores.setNumero(domicilios.getNumero());
                          this.vendedores.setBarrio(domicilios.getBarrio());
                          this.vendedores.setManzana(domicilios.getManzana());
                          this.vendedores.setCasa(domicilios.getCasa());
                          this.vendedores.setSector(domicilios.getSector());
                          this.vendedores.setDepto(domicilios.getDepto());
                          this.vendedores.setOficina(domicilios.getOficina());
                          this.vendedores.setLote(domicilios.getLote());
                          this.vendedores.setLocalidades(domicilios.getLocalidad());
                        }
                        topic = "vendedores/" + this.vendedores.getIdVendedor();
                        avatarGroup.getStyle().set("visibility", "visible");
                } else {
                        avatarGroup.getStyle().set("visibility", "hidden");
                }
                binder.setTopic(topic, () -> this.vendedores);
                avatarGroup.setTopic(topic);

        }
}
