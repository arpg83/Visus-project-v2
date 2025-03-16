package com.ideadistribuidora.visus.views.transportistas;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;

import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.Transportistas;
import com.ideadistribuidora.visus.data.TransportistasBancos;
import com.ideadistribuidora.visus.data.TransportistasBancosId;
import com.ideadistribuidora.visus.data.enums.SituacionFiscalEnum;
import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;
import com.ideadistribuidora.visus.data.enums.TipoTransporteEnum;
import com.ideadistribuidora.visus.services.TransportistasService;
import com.ideadistribuidora.visus.views.dialogs.DialogConfirmacion;
import com.ideadistribuidora.visus.views.dialogs.DialogSaveEditBancos;
import com.ideadistribuidora.visus.views.dialogs.DialogSaveEditDomicilios;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
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
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Transportistas")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 5)
@Route(value = "6/:transportistasID?/:action?(edit)")
public class TransportistasView extends Div implements BeforeEnterObserver {

        private final String TRANSPORTISTAS_ID = "transportistasID";
        private final String TRANSPORTISTAS_EDIT_ROUTE_TEMPLATE = "6/%s/edit";

        private final Grid<Transportistas> grid = new Grid<>(Transportistas.class, false);
        private final Grid<TransportistasBancos> gridBancos = new Grid<>(TransportistasBancos.class, false);

        CollaborationAvatarGroup avatarGroup;

        private TextField nombreFantasia;
        private TextField nombreReal;
        private Button addBancoButton;
        private Button deleteBancoButton;
        private Button editBancoButton;
        private Button refreshBancoButton;
        private TextField telefonoUno;
        private TextField telefonoDos;
        private TextField telefonoTres;
        private ComboBox<Integer> tipoDeDocumento;
        private TextField numeroDocumento;
        private EmailField email;
        private ComboBox<SituacionFiscalEnum> situacionFiscal;
        private ComboBox<TipoTransporteEnum> tipoTransporte;
        private TextField searchNombreFantasia;
        private TextField searchNombreReal;
        private ComboBox<TipoDomicilioEnum> tipoDomicilio;
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

        private final Button cancel = new Button("Cancelar");
        private final Button save = new Button("Grabar");
        private final Button delete = new Button("Eliminar");

        private CollaborationBinder<Transportistas> binder;

        private Transportistas transportistas;

        private Set<TransportistasBancos> bancosList = new HashSet<>();
        private GridListDataView<Transportistas> dataView;
        private TransportistasBancos transportistasBancosSelected;
        private TransportistasBancos transportistasBancosMod;
        private DialogSaveEditBancos<TransportistasBancos> dialogSaveEditBancos;
        private boolean bancoChanges = false;
        private final String EFFECT_CHANGES = "Para que los cambios surtan efecto debe hacer click en el boton Grabar";
        H4 h4DomWarning = new H4(EFFECT_CHANGES);
        H4 h4BankWarning = new H4(EFFECT_CHANGES);

        private final TransportistasService transportistasService;

        public TransportistasView(TransportistasService transportistasService) {
                this.transportistasService = transportistasService;
                addClassNames("transportistas-view");

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
                grid.addColumn(createTransportistasRenderer())
                                .setHeader("Nombre de Fantasia").setAutoWidth(true);
                grid.addColumn("nombreReal").setHeader("Nombre Real").setAutoWidth(true);
                grid.addColumn("tipoTransporte").setHeader("Tipo Transporte").setAutoWidth(true);
                dataView = grid.setItems(transportistasService.transportistasList());
                searchFilter(dataView);

                grid.addThemeVariants(GridVariant.LUMO_COMPACT);
                // when a row is selected or deselected, populate form
                grid.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                UI.getCurrent()
                                                .navigate(String.format(TRANSPORTISTAS_EDIT_ROUTE_TEMPLATE,
                                                                event.getValue().getIdTransportista()));

                                bancosList = transportistasService.getBancosByIdcliente(event.getValue().getIdTransportista());
                                gridBancos.setItems(bancosList);
                                addBancoButton.setEnabled(true);
                                delete.setEnabled(true);

                        } else {
                                gridBancos.setItems();
                                clearForm();
                                addBancoButton.setEnabled(false);
                                delete.setEnabled(true);
                                UI.getCurrent().navigate(TransportistasView.class);
                        }
                });

                gridBancos.addColumn(transportistasBancos -> transportistasBancos.getBancos().getNombre())
                                .setHeader("Banco").setAutoWidth(true);
                gridBancos.addColumn(transportistasBancos -> transportistasBancos.getTipoCuenta()).setHeader("Tipo de Cuenta")
                                .setAutoWidth(true);
                gridBancos.addColumn(transportistasBancos -> transportistasBancos.getCbu()).setHeader("CBU")
                                .setAutoWidth(true);
                gridBancos.addColumn(transportistasBancos -> transportistasBancos.getAlias()).setHeader("Alias").setAutoWidth(true);
                gridBancos.setItems(bancosList);
                gridBancos.addThemeVariants(GridVariant.LUMO_COMPACT);
                // when a row is selected or deselected, populate form
                gridBancos.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                editBancoButton.setEnabled(true);
                                deleteBancoButton.setEnabled(true);
                                transportistasBancosSelected = event.getValue();
                                addBancoButton.setEnabled(false);

                        } else {
                                editBancoButton.setEnabled(false);
                                deleteBancoButton.setEnabled(false);
                                addBancoButton.setEnabled(true);
                        }
                });

                // Configure Form
                binder = new CollaborationBinder<>(Transportistas.class, userInfo);

                // Bind fields. This is where you'd define e.g. validation rules
                binder.forField(tipoDeDocumento).asRequired("Tipo de Documento es Requerido")
                                .bind("idDocumento");

                binder.forField(numeroDocumento, String.class).asRequired("Número de Documento Requerido")
                                .withConverter(new StringToLongConverter())
                                .bind("numeroDocumento");

                binder.forField(nombreFantasia).asRequired("Nombre Fantasía es Requerido")
                                .bind("nombreFantasia");
                binder.forField(nombreReal).asRequired("Nombre Real es Requerido")
                                .bind("nombreReal");
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
                        idLocalidad -> transportistasService
                        .findLocalidadesById(Integer.parseInt(idLocalidad)));
                binder.forField(depto).bind("depto");
                binder.forField(casa).bind("casa");
                binder.forField(calle).asRequired("Calle es Requerido").bind("calle");
                binder.forField(barrio).bind("barrio");
                binder.forField(telefonoUno).bind("telefonoUno");
                binder.forField(telefonoDos).bind("telefonoDos");
                binder.forField(telefonoTres).bind("telefonoTres");
                binder.forField(email).bind("email");
                binder.forField(tipoTransporte).asRequired("Tipo de Transporte es Requerido")
                                .bind("tipoTransporte");              

                binder.addStatusChangeListener(
                                event -> save.setEnabled(binder.isValid()));

                binder.bindInstanceFields(this);

                cancel.addClickListener(e -> {
                        clearForm();
                        refreshGrid();
                });

                save.addClickListener(e -> {
                        try {
                                if (this.transportistas == null) {
                                        this.transportistas = new Transportistas();
                                }

                                 Optional<Transportistas> transportistaExist = transportistasService
                                                .getTransportistaByIdDocumentoAndNumero(this.transportistas.getIdDocumento(),
                                                                this.transportistas.getNumeroDocumento());
                                if (transportistaExist.isPresent() && transportistaExist.get().getIdTransportista() != this.transportistas.getIdTransportista()) {
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
                                    domicilios = transportistasService.saveDomicilios(domicilios);
                                    this.transportistas.setIdDomicilio(domicilios);

                                    if (this.bancosList.size() > 0 && bancoChanges) {
                                            this.transportistas.setTransportistasBancos(bancosList);
                                    }
                                    binder.writeBean(this.transportistas);
                                    transportistasService.update(this.transportistas);
                                    h4DomWarning.setVisible(false);
                                    h4BankWarning.setVisible(false);
                                    clearForm();
                                    refreshGrid();
                                    Notification.show("Datos Guardados");
                                    UI.getCurrent().navigate(TransportistasView.class);
                                }
                        } catch (ObjectOptimisticLockingFailureException exception) {
                                Notification n = Notification.show(
                                                "Error al Actualizar los datos. Alguien mas está actualizando los datos.");
                                n.setPosition(Position.MIDDLE);
                                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        } catch (ValidationException validationException) {
                                Notification.show(
                                                "Error al Guardar/Modificar los datos. Revise Nuevamente que todos los datos sean Válidos");
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
                                if (this.transportistas == null) {
                                        this.transportistas = new Transportistas();
                                }
                                binder.writeBean(this.transportistas);
                                transportistasService.delete(this.transportistas.getIdTransportista());
                                transportistasService.deleteDom(this.transportistas.getIdDomicilio());
                                clearForm();
                                refreshGrid();
                                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                                UI.getCurrent().navigate(TransportistasView.class);
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

        private void searchFilter(GridListDataView<Transportistas> dataView2) {
            dataView.addFilter(local -> {
                String searchTerm = searchNombreFantasia.getValue().trim();
                String searchTerm2 = searchNombreReal.getValue().trim();
    
                if (searchTerm.isEmpty() && searchTerm2.isEmpty())
                    return true;
    
                boolean matchesFullNombreFantasia = matchesTerm(local.getNombreFantasia(),
                        searchTerm);
                boolean matchesNombreReal = matchesTerm(local.getNombreReal(), searchTerm2);
    
                return matchesFullNombreFantasia && matchesNombreReal;
            });
        }

        private boolean matchesTerm(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }

        private LitRenderer<Transportistas> createTransportistasRenderer() {
                return LitRenderer.<Transportistas>of(
                                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                                + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                                                + "  <span> ${item.fullName} </span>"
                                                + "</vaadin-horizontal-layout>")

                                .withProperty("fullName", Transportistas::getNombreFantasia);
        }

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
                Optional<Integer> transportistasId = event.getRouteParameters().get(TRANSPORTISTAS_ID).map(Integer::parseInt);
                if (transportistasId.isPresent()) {
                        Optional<Transportistas> transportistasFromBackend = transportistasService.get(transportistasId.get());
                        if (transportistasFromBackend.isPresent()) {
                                populateForm(transportistasFromBackend.get());
                        } else {
                                Notification.show(
                                                String.format("The requested transportistas was not found, ID = %d",
                                                                transportistasId.get()),
                                                3000, Notification.Position.BOTTOM_START);
                                // when a row is selected but the data is no longer available,
                                // refresh grid
                                refreshGrid();
                                event.forwardTo(TransportistasView.class);
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
                tipoDeDocumento = new ComboBox<>("Tipo de Documento");
                tipoDeDocumento.setPlaceholder("Seleccione el Tipo de documento");
                Map<Integer, String> docsMap = transportistasService.getDocumentList();
                tipoDeDocumento.setItems(docsMap.keySet());
                tipoDeDocumento.setItemLabelGenerator(itemId -> docsMap.get(itemId));
                numeroDocumento = new TextField("Número de Documento");
                nombreFantasia = new TextField("Nombre de Fantasía");
                nombreFantasia.setMaxLength(50);
                nombreReal = new TextField("Nombre Real");
                nombreReal.setMaxLength(50);
                telefonoUno = ComponentUtils.validatePhone(telefonoUno, "Teléfono Uno");
                telefonoDos = ComponentUtils.validatePhone(telefonoDos, "Teléfono Dos");
                telefonoTres = ComponentUtils.validatePhone(telefonoTres, "Teléfono Tres");
                email = ComponentUtils.validateEmail(email);
                situacionFiscal = new ComboBox<>("Situación Fiscal");
                situacionFiscal.setPlaceholder("Seleccione Situación Fiscal");
                situacionFiscal.setItems(SituacionFiscalEnum.values());
                situacionFiscal.setItemLabelGenerator(SituacionFiscalEnum::getDisplaySituacionFiscal);
                tipoTransporte = new ComboBox<>("Tipo de Transporte");
                tipoTransporte.setPlaceholder("Seleccione Tipo de Transporte");
                tipoTransporte.setItems(TipoTransporteEnum.values());
                tipoTransporte.setItemLabelGenerator(TipoTransporteEnum::getDisplayName);
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
                localidades.setItems(transportistasService.getAllLocalidades());
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
                formLayout.add(nombreFantasia, nombreReal, tipoDeDocumento, numeroDocumento,situacionFiscal,  tipoDomicilio, localidades,
                                calle, numero, barrio, manzana, casa, sector,depto,oficina,lote, telefonoUno, telefonoDos, telefonoTres, email,
                                tipoTransporte);

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
                H4 h4GridBancos = new H4("Bancos");
                h4GridBancos.getStyle().set("margin-left", "20px");
                h4BankWarning.getStyle().setColor("red");
                h4BankWarning.setVisible(bancoChanges);
                HorizontalLayout horizontalH4BankLay = new HorizontalLayout();
                horizontalH4BankLay.add(h4GridBancos, h4BankWarning);
                HorizontalLayout buttonBancosLayot = new HorizontalLayout();
                createHorizontalBancosButtonDomLayout(buttonBancosLayot);
                Div wrapper3 = new Div();
                wrapper3.setClassName("grid-wrapper3");
                wrapper3.add(horizontalH4BankLay, gridBancos, buttonBancosLayot);
                flexLayout.add(wrapper, wrapper3);
                splitLayout.addToPrimary(flexLayout);
        }

        private void createHorizontalSearchLayout(HorizontalLayout searchHorizontalLayout) {
                searchHorizontalLayout.setClassName("search-layout");
                searchNombreFantasia = new TextField("Buscar por Nombre de Fantasía");
                searchNombreFantasia.setPlaceholder("Buscar por Nombre de Fantasía");
                searchNombreFantasia.setClearButtonVisible(true);
                searchNombreFantasia.addValueChangeListener(e -> dataView.refreshAll());
                searchNombreFantasia.setWidth("500px");
                searchNombreReal = new TextField("Buscar por Nombre Real");
                searchNombreReal.setPlaceholder("Buscar por Nombre Real");
                searchNombreReal.setClearButtonVisible(true);
                searchNombreReal.addValueChangeListener(e -> dataView.refreshAll());
                searchNombreReal.setWidth("500px");
                searchHorizontalLayout.add(searchNombreFantasia, searchNombreReal);
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
                        transportistasBancosSelected = null;
                        dialogSaveEditBancos = new DialogSaveEditBancos<>(transportistasService.getAllBancos(),
                                        transportistasService.getAllTipoCuentaEnums(),
                                        "Nuevo Banco", transportistasBancosSelected, TransportistasBancos.class,
                                        this::confirmSaveEditBancoFunction);
                        dialogSaveEditBancos.open();
                });
                addBancoButton.setEnabled(false);
                addBancoButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                addBancoButton.setAriaLabel("Agregar Banco");
                editBancoButton = new Button(new Icon(VaadinIcon.EDIT), e -> {
                        dialogSaveEditBancos = new DialogSaveEditBancos<>(
                                        transportistasService.getAllBancos(), transportistasService.getAllTipoCuentaEnums(),
                                        "Editar Banco", transportistasBancosSelected, TransportistasBancos.class,
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

        private void deleteBancoFunction() {
                bancosList.remove(transportistasBancosSelected);
                this.transportistas.setTransportistasBancos(bancosList);
                // transportistasService.update(this.transportistas);
                transportistasService.deleteTransportistasBancosById(transportistasBancosSelected.getId());
                gridBancos.getDataProvider().refreshAll();
                addBancoButton.setEnabled(true);
                editBancoButton.setEnabled(false);
                deleteBancoButton.setEnabled(false);
                UI.getCurrent().navigate(TransportistasView.class);
        }

        private void confirmSaveEditBancoFunction(TransportistasBancos transportistasBancos) {
                if (transportistasBancosSelected != null) {
                        transportistasBancosMod = transportistasBancos;
                        new DialogConfirmacion("¿Está seguro que desea Modificar estos datos Bancarios?",
                                        this::saveEditBancoFunction);
                } else {
                        TransportistasBancosId bancosId = new TransportistasBancosId(0, transportistasBancos.getBancos().getIdBanco());
                        transportistasBancos.setId(bancosId);
                        transportistasBancos.setTransportistas(this.transportistas);
                        bancosList.add(transportistasBancos);
                        gridBancos.getDataProvider().refreshAll();
                        bancoChanges = true;
                        h4BankWarning.setVisible(bancoChanges);
                        dialogSaveEditBancos.close();

                }

        }

        private void saveEditBancoFunction() {
                TransportistasBancosId bancosId = new TransportistasBancosId(this.transportistas.getIdTransportista(),
                                transportistasBancosMod.getBancos().getIdBanco());
                transportistasBancosMod.setId(bancosId);
                transportistasBancosMod.setTransportistas(this.transportistas);
                bancosList.remove(transportistasBancosSelected);
                bancosList.add(transportistasBancosMod);
                gridBancos.getDataProvider().refreshAll();
                bancoChanges = true;
                h4BankWarning.setVisible(bancoChanges);
                dialogSaveEditBancos.close();
        }

        private void refreshGrid() {
                grid.select(null);
                grid.setItems(transportistasService.transportistasList());
                gridBancos.getDataProvider().refreshAll();
        }

        private void clearForm() {
                populateForm(null);
        }

        private void populateForm(Transportistas value) {
                this.transportistas = value;
                String topic = null;
                if (this.transportistas != null) {
                        Domicilios domicilios = this.transportistas.getIdDomicilio();
                        if(domicilios != null){
                          this.transportistas.setTipoDomicilio(domicilios.getTipoDomicilio());
                          this.transportistas.setCalle(domicilios.getCalle());
                          this.transportistas.setNumero(domicilios.getNumero());
                          this.transportistas.setBarrio(domicilios.getBarrio());
                          this.transportistas.setManzana(domicilios.getManzana());
                          this.transportistas.setCasa(domicilios.getCasa());
                          this.transportistas.setSector(domicilios.getSector());
                          this.transportistas.setDepto(domicilios.getDepto());
                          this.transportistas.setOficina(domicilios.getOficina());
                          this.transportistas.setLote(domicilios.getLote());
                          this.transportistas.setLocalidades(domicilios.getLocalidad());
                        }
                        topic = "transportistas/" + this.transportistas.getIdTransportista();
                        avatarGroup.getStyle().set("visibility", "visible");
                } else {
                        avatarGroup.getStyle().set("visibility", "hidden");
                }
                binder.setTopic(topic, () -> this.transportistas);
                avatarGroup.setTopic(topic);

        }

}
