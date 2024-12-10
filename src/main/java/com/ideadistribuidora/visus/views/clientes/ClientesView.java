package com.ideadistribuidora.visus.views.clientes;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;

import com.ideadistribuidora.visus.data.Clientes;
import com.ideadistribuidora.visus.data.ClientesBancos;
import com.ideadistribuidora.visus.data.ClientesBancosId;
import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.enums.EstadoClienteEnum;
import com.ideadistribuidora.visus.data.enums.NivelFidelizacionEnum;
import com.ideadistribuidora.visus.data.enums.SituacionFiscalEnum;
import com.ideadistribuidora.visus.services.ClientesService;
import com.ideadistribuidora.visus.views.dialogs.DialogConfirmacion;
import com.ideadistribuidora.visus.views.dialogs.DialogSaveEditBancos;
import com.ideadistribuidora.visus.views.dialogs.DialogSaveEditDomicilios;
import com.ideadistribuidora.visus.views.filters.ClienteFilter;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
import com.ideadistribuidora.visus.views.utils.StringToLongConverter;
import com.ideadistribuidora.visus.views.utils.StringToShortConverter;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
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

@PageTitle("Clientes")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 0)
@Route(value = "1/:clientesID?/:action?(edit)")
public class ClientesView extends Div implements BeforeEnterObserver {

        private final String CLIENTES_ID = "clientesID";
        private final String CLIENTES_EDIT_ROUTE_TEMPLATE = "1/%s/edit";

        private final Grid<Clientes> grid = new Grid<>(Clientes.class, false);
        private final Grid<Domicilios> gridDomicilios = new Grid<>(Domicilios.class, false);
        private final Grid<ClientesBancos> gridBancos = new Grid<>(ClientesBancos.class, false);

        CollaborationAvatarGroup avatarGroup;

        private TextField nombreFantasia;
        private TextField nombreCliente;
        private Button addDomicilioButton;
        private Button deleteDomicilioButton;
        private Button editDomicilioButton;
        private Button refreshDomicilioButton;
        private Button addBancoButton;
        private Button deleteBancoButton;
        private Button editBancoButton;
        private Button refreshBancoButton;
        private TextField telefonoSecundario;
        private TextField telefonoMovil;
        private TextField otroTelefono;
        private ComboBox<Integer> tipoDeDocumento;
        private TextField numeroDeDocumento;
        private EmailField email;
        private DatePicker fechaBaja;
        private DatePicker fechaIngreso;
        private DatePicker fechaUltimaCompra;
        private TextField limiteFacturasVencidas;
        private BigDecimalField limiteCredito;
        private BigDecimalField pagoMinimo;
        private IntegerField periodoCarencia;
        private ComboBox<NivelFidelizacionEnum> nivelFidelizacion;
        private ComboBox<SituacionFiscalEnum> situacionFiscal;
        private BigDecimalField saldoCuentaCorriente;
        private ComboBox<EstadoClienteEnum> estadoCliente;
        private TextArea observaciones;
        private RadioButtonGroup<Boolean> regente;
        private TextField matricula;

        private final Button cancel = new Button("Cancelar");
        private final Button save = new Button("Grabar");
        private final Button delete = new Button("Eliminar");

        private CollaborationBinder<Clientes> binder;

        private Clientes clientes;

        private Set<Domicilios> domList = new HashSet<>();
        private Set<ClientesBancos> bancosList = new HashSet<>();
        private GridListDataView<Clientes> dataView;
        private Domicilios domSelected;
        private Domicilios domiciliosMod;
        private ClientesBancos clientesBancosSelected;
        private ClientesBancos clientesBancosMod;
        private DialogSaveEditDomicilios dialogSaveEditDomicilios;
        private DialogSaveEditBancos<ClientesBancos> dialogSaveEditBancos;
        private boolean domChanges = false;
        private boolean bancoChanges = false;
        private final String EFFECT_CHANGES = "Para que los cambios surtan efecto debe hacer click en el boton Grabar";
        H4 h4DomWarning = new H4(EFFECT_CHANGES);
        H4 h4BankWarning = new H4(EFFECT_CHANGES);

        private final ClientesService clientesService;

        public ClientesView(ClientesService clientesService) {
                this.clientesService = clientesService;
                addClassNames("clientes-view");

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
                Grid.Column<Clientes> nomCliCol = grid.addColumn(createClientesRenderer())
                                .setHeader("Nombre del Cliente").setAutoWidth(true);
                Grid.Column<Clientes> nomFantCol = grid.addColumn("nombreFantasia").setAutoWidth(true);
                Grid.Column<Clientes> numDocCol = grid.addColumn("numeroDeDocumento").setAutoWidth(true);
                grid.addColumn("email").setAutoWidth(true);
                grid.addColumn("nivelFidelizacion").setAutoWidth(true);
                dataView = grid.setItems(clientesService.clienteList());
                ClienteFilter clienteFilter = new ClienteFilter(dataView);
                // searchFilter(dataView);
                grid.getHeaderRows().clear();
                HeaderRow headerRow = grid.appendHeaderRow();

                headerRow.getCell(nomCliCol).setComponent(
                                createFilterHeader(clienteFilter::setNombreCliente, "Buscar por Nombre del Cliente"));
                headerRow.getCell(nomFantCol).setComponent(
                                createFilterHeader(clienteFilter::setNombreFantasia, "Buscar por Nombre de Fantasía"));
                headerRow.getCell(numDocCol).setComponent(
                                createFilterHeader(clienteFilter::setNumeroDeDocumento,
                                                "Buscar por Número de Documento"));

                grid.addThemeVariants(GridVariant.LUMO_COMPACT);
                // when a row is selected or deselected, populate form
                grid.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                UI.getCurrent()
                                                .navigate(String.format(CLIENTES_EDIT_ROUTE_TEMPLATE,
                                                                event.getValue().getIdCliente()));

                                domList = clientesService.getDomByIDCliente(event.getValue().getIdCliente());
                                gridDomicilios.setItems(domList);
                                addDomicilioButton.setEnabled(true);
                                bancosList = clientesService.getBancosByIdcliente(event.getValue().getIdCliente());
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
                                limiteCredito.setValue(BigDecimal.ZERO);
                                pagoMinimo.setValue(BigDecimal.ZERO);
                                periodoCarencia.setValue(0);
                                saldoCuentaCorriente.setValue(BigDecimal.ZERO);
                                UI.getCurrent().navigate(ClientesView.class);
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
                                clientesBancosSelected = event.getValue();
                                addBancoButton.setEnabled(false);

                        } else {
                                editBancoButton.setEnabled(false);
                                deleteBancoButton.setEnabled(false);
                                addBancoButton.setEnabled(true);
                        }
                });

                // Configure Form
                binder = new CollaborationBinder<>(Clientes.class, userInfo);

                // Bind fields. This is where you'd define e.g. validation rules
                binder.forField(tipoDeDocumento).asRequired("Tipo de Documento es Requerido")
                                .bind("tipoDeDocumento");

                binder.forField(numeroDeDocumento, String.class).asRequired("Número de Documento Requerido")
                                .withConverter(new StringToLongConverter())
                                .bind("numeroDeDocumento");

                binder.forField(nombreFantasia).asRequired("Nombre Fantasía es Requerido")
                                .bind("nombreFantasia");
                binder.forField(nombreCliente).asRequired("Nombre Cliente es Requerido")
                                .bind("nombreCliente");
                binder.forField(fechaIngreso).asRequired("Fecha de Ingreso es Requerida")
                                .bind("fechaIngreso");
                binder.forField(fechaUltimaCompra).asRequired("Fecha de Ultima Compra es Requerida")
                                .bind("fechaUltimaCompra");

                binder.forField(limiteFacturasVencidas, String.class)
                                .asRequired("Límite de facturas vencidas es Requerido")
                                .withConverter(new StringToShortConverter())
                                .bind("limiteFacturasVencidas");
                binder.forField(nivelFidelizacion).asRequired("Nivel de Fidelización Requerido")
                                .bind("nivelFidelizacion");
                binder.forField(situacionFiscal).asRequired("Situación Fiscal Requerido")
                                .bind("situacionFiscal");
                binder.forField(regente).bind("regente");
                binder.forField(matricula).bind("matricula");
                binder.forField(limiteCredito)
                                .withValidator(value -> value == null || value.scale() <= 2,
                                                "Debe tener máximo 2 decimales")
                                .withValidator(value -> value == null || value.precision() <= 18,
                                                "Debe tener máximo 18 dígitos")
                                .bind("limiteCredito");
                binder.forField(pagoMinimo)
                                .withValidator(value -> value == null || value.scale() <= 2,
                                                "Debe tener máximo 2 decimales")
                                .withValidator(value -> value == null || value.precision() <= 18,
                                                "Debe tener máximo 18 dígitos")
                                .bind("pagoMinimo");
                binder.forField(periodoCarencia)
                                .withValidator(value -> value == null || (value >= 0 && value <= 9999),
                                                "El número debe tener hasta 4 dígitos")
                                .bind("periodoCarencia");
                binder.forField(saldoCuentaCorriente).asRequired("Saldo Cuenta Corriente es Requerido")
                                .withValidator(value -> value == null || value.scale() <= 2,
                                                "Debe tener máximo 2 decimales")
                                .withValidator(value -> value == null || value.precision() <= 18,
                                                "Debe tener máximo 18 dígitos")
                                .bind("saldoCuentaCorriente");
                binder.forField(estadoCliente).asRequired("Estado del Cliente Requerido")
                                .bind("estadoCliente");

                binder.addStatusChangeListener(
                                event -> save.setEnabled(binder.isValid()));

                binder.bindInstanceFields(this);

                cancel.addClickListener(e -> {
                        clearForm();
                        refreshGrid();
                });

                save.addClickListener(e -> {
                        try {
                                if (this.clientes == null) {
                                        this.clientes = new Clientes();
                                }

                                if (this.domList.size() > 0 && domChanges) {
                                        if (this.clientes.getDomicilios() != null) {
                                                this.clientes.getDomicilios().clear();
                                        }
                                        clientesService.saveDomList(this.domList);
                                        this.clientes.setDomicilios(this.domList);
                                }

                                if (this.bancosList.size() > 0 && bancoChanges) {
                                        this.clientes.setClientesBancos(bancosList);
                                }
                                binder.writeBean(this.clientes);
                                clientesService.update(this.clientes);
                                h4DomWarning.setVisible(false);
                                h4BankWarning.setVisible(false);
                                clearForm();
                                refreshGrid();
                                Notification.show("Datos Guardados");
                                UI.getCurrent().navigate(ClientesView.class);
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
                                if (this.clientes == null) {
                                        this.clientes = new Clientes();
                                }
                                binder.writeBean(this.clientes);
                                clientesService.delete(this.clientes.getIdCliente());
                                clientesService.deleteAllDom(this.clientes.getDomicilios());
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

        private static Component createFilterHeader(Consumer<String> filterChangeConsumer, String columnaBuscar) {
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

        private LitRenderer<Clientes> createClientesRenderer() {
                return LitRenderer.<Clientes>of(
                                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                                + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                                                + "  <span> ${item.fullName} </span>"
                                                + "</vaadin-horizontal-layout>")

                                .withProperty("fullName", Clientes::getNombreCliente);
        }

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
                Optional<Integer> clientesId = event.getRouteParameters().get(CLIENTES_ID).map(Integer::parseInt);
                if (clientesId.isPresent()) {
                        Optional<Clientes> clientesFromBackend = clientesService.get(clientesId.get());
                        if (clientesFromBackend.isPresent()) {
                                populateForm(clientesFromBackend.get());
                        } else {
                                Notification.show(
                                                String.format("The requested clientes was not found, ID = %d",
                                                                clientesId.get()),
                                                3000, Notification.Position.BOTTOM_START);
                                // when a row is selected but the data is no longer available,
                                // refresh grid
                                refreshGrid();
                                event.forwardTo(ClientesView.class);
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
                Map<Integer, String> docsMap = clientesService.getDocumentList();
                tipoDeDocumento.setItems(docsMap.keySet());
                tipoDeDocumento.setItemLabelGenerator(itemId -> docsMap.get(itemId));
                numeroDeDocumento = new TextField("Número de Documento");
                nombreFantasia = new TextField("Nombre Fantasía");
                nombreFantasia.setMaxLength(50);
                nombreCliente = new TextField("Nombre Cliente");
                nombreCliente.setMaxLength(50);
                telefonoSecundario = ComponentUtils.validatePhone(telefonoSecundario, "Teléfono Secundario");
                telefonoMovil = ComponentUtils.validatePhone(telefonoMovil, "Teléfono Movil");
                otroTelefono = ComponentUtils.validatePhone(telefonoSecundario, "Otro Teléfono");
                email = ComponentUtils.validateEmail(email);
                fechaBaja = new DatePicker("Fecha Baja");
                fechaBaja.setPlaceholder("dd/mm/aaaa");
                fechaBaja.setI18n(ComponentUtils.getI18n());
                fechaIngreso = new DatePicker("Fecha Ingreso");
                fechaIngreso.setPlaceholder("dd/mm/aaaa");
                fechaIngreso.setI18n(ComponentUtils.getI18n());
                fechaUltimaCompra = new DatePicker("Fecha Ultima Compra");
                fechaUltimaCompra.setI18n(ComponentUtils.getI18n());
                fechaUltimaCompra.setPlaceholder("dd/mm/aaaa");
                limiteFacturasVencidas = new TextField("Límite Facturas Vencidas");
                limiteFacturasVencidas.setMaxLength(4);
                limiteCredito = new BigDecimalField("Límite Crédito");
                limiteCredito.setValue(BigDecimal.ZERO);
                limiteCredito.getElement().addEventListener("input", e -> {
                        limitsBigdecimal(limiteCredito);
                }).debounce(300);
                pagoMinimo = new BigDecimalField("Pago Mínimo");
                pagoMinimo.setValue(BigDecimal.ZERO);
                pagoMinimo.getElement().addEventListener("input", e -> {
                        limitsBigdecimal(pagoMinimo);
                }).debounce(300);
                periodoCarencia = new IntegerField("Período Carencia");
                periodoCarencia.setValue(0);
                nivelFidelizacion = new ComboBox<NivelFidelizacionEnum>("Nivel Fidelización");
                nivelFidelizacion.setPlaceholder("Seleccione Nivel Fidelización");
                nivelFidelizacion.setItems(NivelFidelizacionEnum.values());
                nivelFidelizacion.setItemLabelGenerator(NivelFidelizacionEnum::getDisplayNivelFidelizacion);
                situacionFiscal = new ComboBox<SituacionFiscalEnum>("Situación Fiscal");
                situacionFiscal.setPlaceholder("Seleccione Situación Fiscal");
                situacionFiscal.setItems(SituacionFiscalEnum.values());
                situacionFiscal.setItemLabelGenerator(SituacionFiscalEnum::getDisplaySituacionFiscal);
                saldoCuentaCorriente = new BigDecimalField("Saldo Cuenta Corriente");
                saldoCuentaCorriente.setValue(BigDecimal.ZERO);
                saldoCuentaCorriente.getElement().addEventListener("input", e -> {
                        limitsBigdecimal(saldoCuentaCorriente);
                }).debounce(300);
                estadoCliente = new ComboBox<EstadoClienteEnum>("Estado del Cliente");
                estadoCliente.setPlaceholder("Seleccione Estado del Cliente");
                estadoCliente.setItems(EstadoClienteEnum.values());
                estadoCliente.setItemLabelGenerator(EstadoClienteEnum::getDisplayEstadoCliente);
                HorizontalLayout horizontalLayoutRadioButton = new HorizontalLayout();
                regente = new RadioButtonGroup<>();
                regente.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
                regente.setLabel("Regente");
                regente.setItems(true, false);
                regente.setItemLabelGenerator(value -> value ? "Habilitado" : "Inhabilitado");
                matricula = new TextField("Matricula");
                matricula.setMaxLength(30);
                matricula.setEnabled(false);

                regente.addValueChangeListener(event -> {
                        if (event.getValue() != null)
                                matricula.setEnabled(event.getValue());
                });
                horizontalLayoutRadioButton.add(regente, matricula);

                observaciones = ComponentUtils.validateTextArea(observaciones, 100, "Observaciones");

                formLayout.add(nombreFantasia, nombreCliente, tipoDeDocumento, numeroDeDocumento,
                                situacionFiscal, limiteCredito, pagoMinimo, limiteFacturasVencidas,
                                nivelFidelizacion, periodoCarencia, fechaIngreso, fechaUltimaCompra,
                                fechaBaja, telefonoMovil, telefonoSecundario, otroTelefono, email, estadoCliente,
                                horizontalLayoutRadioButton,
                                saldoCuentaCorriente, observaciones);

                editorDiv.add(avatarGroup, formLayout);
                createButtonLayout(editorLayoutDiv);

                splitLayout.addToSecondary(editorLayoutDiv);
        }

        private void limitsBigdecimal(BigDecimalField bigDecimalField) {
                String value = bigDecimalField.getValue() != null ? bigDecimalField.getValue().toPlainString() : "0";
                // Permitir solo números, comas, puntos y signo negativo
                value = value.replaceAll("[^0-9.,-]", ""); // Filtrar los caracteres no permitidos
                bigDecimalField.setValue(new BigDecimal(value.isEmpty() ? "0" : value));
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
                h4DomWarning.getStyle().setColor("red");
                h4DomWarning.setVisible(domChanges);
                HorizontalLayout horizontalH4domLay = new HorizontalLayout();
                horizontalH4domLay.add(h4GridDom, h4DomWarning);
                HorizontalLayout buttonDomLayot = new HorizontalLayout();
                createHorizontalDomicilioButtonDomLayout(buttonDomLayot);
                Div wrapper2 = new Div();
                wrapper2.setClassName("grid-wrapper2");
                wrapper2.add(horizontalH4domLay, gridDomicilios, buttonDomLayot);
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
                        clientesBancosSelected = null;
                        dialogSaveEditBancos = new DialogSaveEditBancos<>(clientesService.getAllBancos(),
                                        clientesService.getAllTipoCuentaEnums(),
                                        "Nuevo Banco", clientesBancosSelected, ClientesBancos.class,
                                        this::confirmSaveEditBancoFunction);
                        dialogSaveEditBancos.open();
                });
                addBancoButton.setEnabled(false);
                addBancoButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                addBancoButton.setAriaLabel("Agregar Banco");
                editBancoButton = new Button(new Icon(VaadinIcon.EDIT), e -> {
                        dialogSaveEditBancos = new DialogSaveEditBancos<>(
                                        clientesService.getAllBancos(), clientesService.getAllTipoCuentaEnums(),
                                        "Editar Banco", clientesBancosSelected, ClientesBancos.class,
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
                                        clientesService.getAllTipoDomicilio(), clientesService.getAllLocalidades(),
                                        "Nuevo Domicilio",
                                        domSelected, this::confirmSaveEditDomFunction);
                        dialogSaveEditDomicilios.open();
                });
                addDomicilioButton.setEnabled(false);
                addDomicilioButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                addDomicilioButton.setAriaLabel("Agregar Domicilio");
                editDomicilioButton = new Button(new Icon(VaadinIcon.EDIT), e -> {
                        dialogSaveEditDomicilios = new DialogSaveEditDomicilios(
                                        clientesService.getAllTipoDomicilio(),
                                        clientesService.getAllLocalidades(), "Editar Domicilio", domSelected,
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
                this.clientes.setDomicilios(domList);
                clientesService.update(this.clientes);
                clientesService.deleteDomById(domSelected.getIdDomicilio());
                gridDomicilios.getDataProvider().refreshAll();
                addDomicilioButton.setEnabled(true);
                editDomicilioButton.setEnabled(false);
                deleteDomicilioButton.setEnabled(false);
                UI.getCurrent().navigate(ClientesView.class);
        }

        private void deleteBancoFunction() {
                bancosList.remove(clientesBancosSelected);
                this.clientes.setClientesBancos(bancosList);
                // clientesService.update(this.clientes);
                clientesService.deleteClientesBancosById(clientesBancosSelected.getId());
                gridBancos.getDataProvider().refreshAll();
                addBancoButton.setEnabled(true);
                editBancoButton.setEnabled(false);
                deleteBancoButton.setEnabled(false);
                UI.getCurrent().navigate(ClientesView.class);
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
                        h4DomWarning.setVisible(domChanges);
                        dialogSaveEditDomicilios.close();
                }

        }

        private void confirmSaveEditBancoFunction(ClientesBancos clientesBancos) {
                if (clientesBancosSelected != null) {
                        clientesBancosMod = clientesBancos;
                        new DialogConfirmacion("¿Está seguro que desea Modificar estos datos Bancarios?",
                                        this::saveEditBancoFunction);
                } else {
                        ClientesBancosId bancosId = new ClientesBancosId(0, clientesBancos.getBancos().getIdBanco());
                        clientesBancos.setId(bancosId);
                        clientesBancos.setClientes(this.clientes);
                        bancosList.add(clientesBancos);
                        gridBancos.getDataProvider().refreshAll();
                        bancoChanges = true;
                        h4BankWarning.setVisible(bancoChanges);
                        dialogSaveEditBancos.close();

                }

        }

        private void saveEditDomFunction() {
                domiciliosMod.setIdDomicilio(this.domSelected.getIdDomicilio());
                domList.remove(domSelected);
                domList.add(domiciliosMod);
                gridDomicilios.getDataProvider().refreshAll();
                domChanges = true;
                h4DomWarning.setVisible(domChanges);
                dialogSaveEditDomicilios.close();
        }

        private void saveEditBancoFunction() {
                ClientesBancosId bancosId = new ClientesBancosId(this.clientes.getIdCliente(),
                                clientesBancosMod.getBancos().getIdBanco());
                clientesBancosMod.setId(bancosId);
                clientesBancosMod.setClientes(this.clientes);
                bancosList.remove(clientesBancosSelected);
                bancosList.add(clientesBancosMod);
                gridBancos.getDataProvider().refreshAll();
                bancoChanges = true;
                h4BankWarning.setVisible(bancoChanges);
                dialogSaveEditBancos.close();
        }

        private void refreshGrid() {
                grid.select(null);
                grid.setItems(clientesService.clienteList());
                gridDomicilios.getDataProvider().refreshAll();
                gridBancos.getDataProvider().refreshAll();
        }

        private void clearForm() {
                populateForm(null);
        }

        private void populateForm(Clientes value) {
                this.clientes = value;
                String topic = null;
                if (this.clientes != null) {
                        topic = "clientes/" + this.clientes.getIdCliente();
                        avatarGroup.getStyle().set("visibility", "visible");
                } else {
                        avatarGroup.getStyle().set("visibility", "hidden");
                }
                binder.setTopic(topic, () -> this.clientes);
                avatarGroup.setTopic(topic);

        }

}
