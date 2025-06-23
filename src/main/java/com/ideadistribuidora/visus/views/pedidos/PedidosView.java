package com.ideadistribuidora.visus.views.pedidos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Articulos;
import com.ideadistribuidora.visus.data.Clientes;
import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.ListasPorcentuales;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.Pedidos;
import com.ideadistribuidora.visus.data.PedidosItems;
import com.ideadistribuidora.visus.data.PedidosListas;
import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.data.Vendedores;
import com.ideadistribuidora.visus.data.enums.EstadoPagoEnum;
import com.ideadistribuidora.visus.data.enums.EstadoPedidoEnum;
import com.ideadistribuidora.visus.data.enums.PlataformaEnum;
import com.ideadistribuidora.visus.services.PedidosService;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@PageTitle("Pedidos")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 1)
@Route(value = "2/:pedidosID?/:action?(edit)")
public class PedidosView extends Div {
    private static final String BONIFICAR_LABEL = "Bonificar(%)";
    private static final String BONIFICACION_LABEL = "Bonificación(%)";
    private static final String RECARGAR_LABEL = "Recargo(%)";
    private static final String NINGUNO_LABEL = "Ninguno";
    private static final String APLICAR_LISTAS_LABEL = "Aplicar Listas";
    private CollaborationBinder<Pedidos> binder;
    private CollaborationBinder<PedidosItems> binderPedidosItems;
    private Notification n;
    private final PedidosService pedidosService;
    private Dialog dialog;
    private DatePicker fechaPedido;
    private TextField plataforma;
    private IntegerField idPedidoField;
    private Span estadoPedidoSpan;
    private TextField estadoPedido;
    private TextField estadoPago;
    private Span estadoPagoSpan;

    private ComboBox<Clientes> idCliente;
    private ComboBox<Vendedores> vendedores;

    private ComboBox<String> domicilios;
    //private TextField domicilioClienteString;

    private RadioButtonGroup<String> bonificacionRecargoListas;
    private BigDecimalField bonificacionRecListField;
    private ComboBox<ListasPorcentuales> listas;
    private Checkbox esBonificacionPedido;
    private Checkbox esRecargoPedido;

    private ComboBox<Articulos> idArticulo;
    private TextField medidas;
    private NumberField cantidad;

    private RadioButtonGroup<String> bonificacionRecargoArticulo;
    private BigDecimalField bonificacionRecArtField;
    private BigDecimalField bonificacion;
    private BigDecimalField recargo;
    private Checkbox persistBonArt;
    private Checkbox esBonificacion;
    private Checkbox esRecargo;
    private Button agregarPedido;

    private final Grid<Pedidos> gridPedidos = new Grid<>(Pedidos.class, false);

    private Grid<PedidosItems> gridPedidosItems;

    private TextArea notaAlPie;
    private BigDecimalField bonificacionField;// solo lectura
    private BigDecimalField recargoField;// solo lectura
    private BigDecimalField subTotalSinImpuestos;// solo lectura
    private BigDecimalField subTotalConIMpuestos;// solo lectura
    private BigDecimalField totalPedido;// solo lectura

    private Button nuevoPedido;
    private Button cancelar;
    private Button picking;
    private Button pagarPedido;
    private Button imprimir;
    private Button modificarPedido;

    private Button cancelarAcciones;
    private Button rolbackItem;
    private Button cancelarPedido;
    private Button agregarPedidoBandeja;

    private CollaborationAvatarGroup avatarGroup;
    private UserInfo userInfo;

    private Pedidos pedidos = new Pedidos();
    private PedidosItems pedidosItems;
    private PedidosListas pedidosListas;
    private List<PedidosItems> pedidosItemsList = new ArrayList<>();
    private List<PedidosItems> pedidosItemsSelected = new ArrayList<>();
    private List<Pedidos> pedidosSelected = new ArrayList<>();
    private boolean isAplicarListas;
    private TextField searchPedido;
    private GridListDataView<Pedidos> dataView;

    public PedidosView(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
        addClassNames("pedidos-view");

        userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        this.binder = new CollaborationBinder<>(Pedidos.class, userInfo);
        serialize();

        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "visible");

        Div search = new Div();
        searchPedido = new TextField();
        searchPedido.setPlaceholder("Buscar Cliente");
        searchPedido.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchPedido.setValueChangeMode(ValueChangeMode.EAGER);
        searchPedido.addValueChangeListener(e -> dataView.refreshAll());
        search.add(searchPedido);

        HorizontalLayout horizontalButLayout = new HorizontalLayout();
        nuevoPedido = new Button("Nuevo Pedido", event -> {
            dialog = new Dialog();
            VerticalLayout dialogLayout = fillDialog(pedidos);
            dialog.add(dialogLayout);
            initBinderPedidos();
            initBinderPedidosItems();
            dialog.open();
        });
        nuevoPedido.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nuevoPedido.getStyle().set("background-color", "var(--lumo-primary-color)");
        nuevoPedido.getStyle().set("color", "white");
        modificarPedido = new Button("Modificar Pedido", e -> {
            dialog = new Dialog();
            VerticalLayout dialogLayout = fillDialog(pedidos);
            dialog.add(dialogLayout);
            initBinderPedidos();
            initBinderPedidosItems();
            dialog.open();
        });
        modificarPedido.setEnabled(false);
        modificarPedido.addThemeVariants(ButtonVariant.LUMO_WARNING);
        cancelar = new Button("Cancelar", e -> {
            for (Pedidos ped : pedidosSelected) {
                try {
                    ped.setEstadoPedido(EstadoPedidoEnum.Cancelado);
                    ped.setDomicilios(new Domicilios());
                    pedidosService.update(ped);
                } catch (ObjectOptimisticLockingFailureException ex) {
                    Notification.show("El pedido ya ha sido modificado por otro usuario: " + ex.getMessage(), 3000,
                            Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
            gridPedidos.setItems(pedidosService.pedidosList());

        });
        cancelar.setEnabled(false);
        cancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        cancelar.getStyle().set("background-color", "var(--lumo-error-color)");
        picking = new Button("Picking", e -> {
            // agregar acciones
        });
        picking.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        picking.getStyle().set("background-color", "var(--lumo-success-color)");
        picking.getStyle().set("color", "white");
        pagarPedido = new Button("Pagar Pedido", e -> {
            for (Pedidos ped : pedidosSelected) {
                ped.setEstadoPago(EstadoPagoEnum.Pago_Aprobado);
                pedidosService.update(ped);
            }
            gridPedidos.setItems(pedidosService.pedidosList());
        });
        pagarPedido.setEnabled(false);
        pagarPedido.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        pagarPedido.getStyle().set("background-color", "purple");
        pagarPedido.getStyle().set("color", "white");
        imprimir = new Button("Imprimir", e -> {

            try {
                // Obtener los objetos HttpServletRequest y HttpServletResponse
                Anchor downloadLink = new Anchor(buildPdfResource(), "");
                downloadLink.setTarget("_blank");
                downloadLink.getElement().callJsFunction("click");
                add(downloadLink);

            } catch (Exception ex) {
                Notification.show("Error al exportar el reporte: " + ex.getMessage(), 3000, Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        imprimir.setEnabled(false);
        imprimir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        imprimir.getStyle().set("background-color", "var(--lumo-contrast-30pct)");
        imprimir.getStyle().set("color", "white");
        horizontalButLayout.add(avatarGroup, nuevoPedido, modificarPedido, cancelar, picking, pagarPedido, imprimir);
        gridPedidos.setSelectionMode(Grid.SelectionMode.MULTI);
        gridPedidos.addColumn(createToggleDetailsRenderer(gridPedidos))
                .setWidth("80px").setFlexGrow(0).setFrozen(true);
        gridPedidos.addColumn(createPedidosRenderer())
                .setHeader("Pedido").setAutoWidth(true);
        gridPedidos.addColumn(p -> p.getIdCliente().getNombreCliente())
                .setHeader("Cliente").setAutoWidth(true);
        gridPedidos.addColumn(p -> p.getEstadoPedido().getEstadoPedido())
                .setHeader("Status").setAutoWidth(true);
        gridPedidos
                .addComponentColumn(p -> {
                    return setSpan(p.getEstadoPago());
                })
                .setHeader("Pago").setAutoWidth(true);

        gridPedidos.setDetailsVisibleOnClick(false);
        gridPedidos.setItemDetailsRenderer(createPedidosDetailsRenderer());
        gridPedidos.addSelectionListener(event -> {
            pedidosSelected.clear();
            pedidosSelected.addAll(event.getAllSelectedItems());
            if (event.getAllSelectedItems().size() == 1) {
                pedidos = event.getFirstSelectedItem().orElse(null);
                modificarPedido.setEnabled(true);
            } else {
                pedidos = null;
                modificarPedido.setEnabled(false);
            }
            cancelar.setEnabled(!pedidosSelected.isEmpty());
            pagarPedido.setEnabled(!pedidosSelected.isEmpty());
            imprimir.setEnabled(!pedidosSelected.isEmpty());

        });

        gridPedidos.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        dataView = gridPedidos.setItems(pedidosService.pedidosList());

        searchFilter(dataView);
        VerticalLayout principalLayOut = new VerticalLayout();
        HorizontalLayout avatarSearchLayout = new HorizontalLayout();
        avatarSearchLayout.add(avatarGroup, search);
        principalLayOut.add(avatarSearchLayout, horizontalButLayout, gridPedidos);
        principalLayOut.setHeightFull();
        add(principalLayOut);
        setHeightFull();

    }

    private StreamResource buildPdfResource() throws Exception {
        return new StreamResource("Pedidos.pdf", () -> {
            ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
            try {
                new ReportePedidos().exportarReporte(pedidosSelected, pedidosService, pdfReportStream);
                return new ByteArrayInputStream(pdfReportStream.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                return new ByteArrayInputStream(new byte[0]);
            }
        });

    }

    private ComponentRenderer<PedidosDetailsFormLayout, Pedidos> createPedidosDetailsRenderer() {
        return new ComponentRenderer<>(() -> new PedidosDetailsFormLayout(pedidosService),
                PedidosDetailsFormLayout::setPedidos);
    }

    private void searchFilter(GridListDataView<Pedidos> dataView2) {
        dataView2.addFilter(pedidoSearh -> {
            String searchTerm = searchPedido.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            return matchesTerm(pedidoSearh.getIdCliente().getNombreCliente(),
                    searchTerm);
        });
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private LitRenderer<Pedidos> createToggleDetailsRenderer(Grid<Pedidos> grid) {
        return LitRenderer
                .<Pedidos>of("""
                            <vaadin-button
                                theme="tertiary icon"
                                aria-label="Toggle details"
                                aria-expanded="${model.detailsOpened ? 'true' : 'false'}"
                                @click="${handleClick}"
                            >
                                <vaadin-icon
                                .icon="${model.detailsOpened ? 'lumo:angle-down' : 'lumo:angle-right'}"
                                ></vaadin-icon>
                            </vaadin-button>
                        """)
                .withFunction("handleClick",
                        p -> grid.setDetailsVisible(p,
                                !grid.isDetailsVisible(p)));
    }

    private LitRenderer<Pedidos> createPedidosRenderer() {
        return LitRenderer.<Pedidos>of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        // + " <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                        + "  <span> ${item.fullName} </span>"
                        + "</vaadin-horizontal-layout>")

                .withProperty("fullName", Pedidos::getIdPedido);
    }

    private Span setSpan(EstadoPagoEnum estadoPago2) {
        Span estPagoSpan = new Span();
        estPagoSpan.setText(estadoPago2.getEstadoPago());
        if (estadoPago2.equals(EstadoPagoEnum.Pago_Pendiente)) {
            estPagoSpan.getElement().getStyle().set("background-color",
                    "var(--lumo-warning-color)");
            estPagoSpan.getElement().getStyle().set("padding", "5px");
            estPagoSpan.getElement().getStyle().setColor("white");
            estPagoSpan.getElement().getStyle().setFontWeight("bold");
            estPagoSpan.getElement().getStyle().setPaddingLeft("10px");
            estPagoSpan.getElement().getStyle().setPaddingRight("10px");
        } else if (estadoPago2.equals(EstadoPagoEnum.Pago_Aprobado)) {
            estPagoSpan.getElement().getStyle().set("background-color",
                    "var(--lumo-success-color)");
            estPagoSpan.getElement().getStyle().set("padding", "5px");
            estPagoSpan.getElement().getStyle().setColor("white");
            estPagoSpan.getElement().getStyle().setFontWeight("bold");
            estPagoSpan.getElement().getStyle().setPaddingLeft("10px");
            estPagoSpan.getElement().getStyle().setPaddingRight("10px");
        } else if (estadoPago2.equals(EstadoPagoEnum.Pago_Rechazado)) {
            estPagoSpan.getElement().getStyle()
                    .setBackgroundColor("var(--lumo-error-color)");
            estPagoSpan.getElement().getStyle()
                    .setColor("white").setFontWeight("bold")
                    .setPaddingLeft("10px").setPaddingRight("10px")
                    .setPaddingTop("5px").setPaddingBottom("5px");
        } else if (estadoPago2.equals(EstadoPagoEnum.Sin_Estado)) {
            estPagoSpan.getElement().getStyle()
                    .setBackgroundColor("black");
            estPagoSpan.getElement().getStyle()
                    .setColor("white").setFontWeight("bold")
                    .setPaddingLeft("10px").setPaddingRight("10px")
                    .setPaddingTop("5px").setPaddingBottom("5px");
        }
        return estPagoSpan;
    }

    private void serialize() {
        this.binder.setSerializer(Clientes.class,
                clientes -> String.valueOf(clientes.getIdCliente()),
                idClientes -> (Clientes) pedidosService
                        .findByIdClientes(Integer.parseInt(idClientes)));
        this.binder.setSerializer(Vendedores.class,
                vend -> String.valueOf(vend.getIdVendedor()),
                idVendedores -> pedidosService
                        .findByIdVendedores(Integer.parseInt(idVendedores)));
        this.binder.setSerializer(Domicilios.class,
                dom -> String.valueOf(dom.getIdDomicilio()),
                idDomicilios -> pedidosService
                        .findByIdDomicilios(Integer.parseInt(idDomicilios)));

    }

    private VerticalLayout fillDialog(Pedidos pedidos) {

        this.isAplicarListas = false;
        fechaPedido = new DatePicker("Fecha");
        fechaPedido.setValue(pedidos != null ? pedidos.getFechaPedido() : LocalDate.now());
        fechaPedido.setPlaceholder("dd/mm/aaaa");
        fechaPedido.setI18n(ComponentUtils.getI18n());
        fechaPedido.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                fechaPedido.setValue(LocalDate.now());
            }
        });

        plataforma = new TextField("Plataforma");
        plataforma.setValue(
                pedidos!= null && pedidos.getPlataforma() !=null ? pedidos.getPlataforma().getPlataforma() : PlataformaEnum.WEB.getPlataforma());
        plataforma.setReadOnly(true);
        idPedidoField = new IntegerField("Pedido");
        idPedidoField.setValue(pedidos != null ? pedidos.getIdPedido() : pedidosService.getNextIdPedido());
        idPedidoField.setReadOnly(true);
        estadoPedidoSpan = new Span();
        estadoPedidoSpan.setText(pedidos != null && pedidos.getEstadoPedido() != null ? pedidos.getEstadoPedido().getEstadoPedido()
                : EstadoPedidoEnum.En_Picking.getEstadoPedido());
        estadoPedidoSpan.getElement().getStyle().setBackgroundColor("var(--lumo-primary-text-color)");
        estadoPedidoSpan.getElement().getStyle().setColor("white");
        estadoPedidoSpan.getElement().getStyle().setFontWeight("bold");
        estadoPedidoSpan.getElement().getStyle().setPaddingLeft("10px");
        estadoPedidoSpan.getElement().getStyle().setPaddingRight("10px");
        estadoPedido = new TextField();
        // Populate items first
        estadoPedido.setVisible(false);
        estadoPedido.setValue(pedidos != null && pedidos.getEstadoPedido() != null ? pedidos.getEstadoPedido().getEstadoPedido()
                : EstadoPedidoEnum.En_Picking.getEstadoPedido());

        estadoPagoSpan = new Span();
        estadoPagoSpan.setText(pedidos != null && pedidos.getEstadoPago() != null ? pedidos.getEstadoPago().getEstadoPago()
                : EstadoPagoEnum.Pago_Pendiente.getEstadoPago());
        estadoPagoSpan.getElement().getStyle().set("background-color",
                "var(--lumo-warning-color)");
        estadoPagoSpan.getElement().getStyle().set("padding", "5px");
        estadoPagoSpan.getElement().getStyle().setColor("white");
        estadoPagoSpan.getElement().getStyle().setFontWeight("bold");
        estadoPagoSpan.getElement().getStyle().setPaddingLeft("10px");
        estadoPagoSpan.getElement().getStyle().setPaddingRight("10px");
        estadoPago = new TextField();
        estadoPago.setValue(pedidos != null && pedidos.getEstadoPago() != null ? pedidos.getEstadoPago().getEstadoPago()
                : EstadoPagoEnum.Pago_Pendiente.getEstadoPago());
        estadoPago.setVisible(false);
        idCliente = new ComboBox<>("Cliente");
        idCliente.setWidth(25, Unit.PERCENTAGE);
        idCliente.setItems(pedidosService.getAllclientes());
        idCliente.setItemLabelGenerator(Clientes::getNombreCliente);
        if (pedidos != null && pedidos.getIdCliente() != null) {
            idCliente.setValue(pedidos.getIdCliente());
            idCliente.setPlaceholder(pedidos.getIdCliente().getNombreCliente());
        } else {
            idCliente.setPlaceholder("Buscar Cliente");
            idCliente.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        }
        domicilios = new ComboBox<>("Domicilio de Envío");
        
        List<Domicilios> domi = pedidosService.getDomicilioByIdCliente(idCliente.getValue().getIdCliente());
         List <String> domiString = new ArrayList<>();
        idCliente.addValueChangeListener(event -> {
            if (idCliente.getValue() != null) {
                if (domi.isEmpty()) {
                    domicilios.clear();
                    domicilios.setItems();
                    n = Notification.show(
                            "No se encontraron Domicilios para el Cliente seleccionado",
                            3000, Position.MIDDLE);
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } else {
                domicilios.clear();
                domicilios.setItems();
            }
        });

        vendedores = new ComboBox<>("Vendedor");
        vendedores.setWidth(25, Unit.PERCENTAGE);
        vendedores.setPlaceholder("Buscar Vendedor");
        vendedores.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        vendedores.setItems(pedidosService.getAllVendedores());
        vendedores.setItemLabelGenerator(Vendedores::getNombre);
        if (pedidos != null && pedidos.getIdVendedor() != null) {
            vendedores.setValue(pedidos.getIdVendedor());
        }
         domi.stream().forEach(d -> {
                       domiString.add(construirDireccion(d));
                    });

        if (pedidos != null && pedidos.getDomicilioClienteString() != null){
            domicilios.setItems(domiString);
            domicilios.setItemLabelGenerator(dom -> dom);
            domicilios.setValue(pedidos.getDomicilioClienteString());

            domicilios.setPlaceholder(pedidos.getDomicilioClienteString());
        }else{
            domicilios.setItems(domiString);
            domicilios.setItemLabelGenerator(dom -> dom);
            domicilios.setPlaceholder("Seleccione Domicilio");
        } 
        // domicilios.addValueChangeListener(event -> {
        //     String domicilio = domicilios.getValue() == null ? "" : domicilios.getValue();
        //     if(pedidos != null){
        //         domicilioClienteString.setValue(
        //             pedidos.getDomicilioClienteString() != null ? pedidos.getDomicilioClienteString() : "");
        //     }else{
        //         domicilioClienteString.setValue(construirDireccion(domicilio));
        //     }
        // });
        bonificacionRecargoListas = new RadioButtonGroup<>();
        bonificacionRecargoListas.setItems(NINGUNO_LABEL, BONIFICACION_LABEL, RECARGAR_LABEL, APLICAR_LISTAS_LABEL);
        List<PedidosListas> pedList = new ArrayList<>();
        subTotalConIMpuestos = new BigDecimalField();
        bonificacionRecListField = new BigDecimalField();
        bonificacionRecListField.setEnabled(false);
        bonificacionField = new BigDecimalField();
        recargoField = new BigDecimalField();
        esBonificacionPedido = new Checkbox();
        esRecargoPedido = new Checkbox();
        listas = new ComboBox<>();
        listas.setEnabled(false);
        subTotalSinImpuestos = new BigDecimalField();
        totalPedido = new BigDecimalField();
        
        // Primero agrega el listener
        bonificacionRecargoListas.addValueChangeListener(event -> {
            String selectedOption = event.getValue();
            if (BONIFICACION_LABEL.equals(selectedOption)) {
                esBonificacionPedido.setValue(true);
                esRecargoPedido.setValue(false);
                this.pedidos.setEsBonificacion(true);
                this.pedidos.setEsRecargo(false);
                this.isAplicarListas = false;
                bonificacionRecListField.setEnabled(true);
                bonificacionRecListField.setValue(this.pedidos.getBonificacion().signum() > 0 ? this.pedidos.getBonificacion() : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                bonificacionRecListField.focus();
                listas.setEnabled(false);
            } else if (RECARGAR_LABEL.equals(selectedOption)) {
                esRecargoPedido.setValue(true);
                esBonificacionPedido.setValue(false);
                this.pedidos.setEsBonificacion(false);
                this.pedidos.setEsRecargo(true);
                this.isAplicarListas = false;
                bonificacionRecListField.setEnabled(true);
                bonificacionRecListField.focus();
            } else if (APLICAR_LISTAS_LABEL.equals(selectedOption)) {
                esBonificacionPedido.setValue(false);
                esRecargoPedido.setValue(false);
                this.pedidos.setEsBonificacion(false);
                this.pedidos.setEsRecargo(false);

                this.isAplicarListas = true;
                calculateAndFillTotalPedido(pedidosItemsList);
                bonificacionRecListField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                bonificacionRecListField.setEnabled(false);
                listas.setEnabled(true);
                listas.setEnabled(true);
            } else if (NINGUNO_LABEL.equals(selectedOption)) {
                esBonificacionPedido.setValue(false);
                esRecargoPedido.setValue(false);
                if(this.pedidos == null) this.pedidos = new Pedidos();
                this.pedidos.setEsBonificacion(false);
                this.pedidos.setEsRecargo(false);
                this.isAplicarListas = false;
                bonificacionRecListField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                bonificacionRecListField.setEnabled(false);
                calculateAndFillTotalPedido(pedidosItemsList);
                listas.setEnabled(false);
            }
            
        });

        if(pedidos !=null && pedidos.getIdPedido() != 0) {
            if (pedidos.isEsBonificacion()) {
                bonificacionRecargoListas.setValue(BONIFICACION_LABEL);
                pedidos.setBonificacion(bonificacionRecListField.getValue());
                bonificacionField.setValue(bonificacionRecListField.getValue());
                recargoField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                pedidos.setRecargo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                pedidos.setEsBonificacion(true);
                pedidos.setEsRecargo(false);
                calculateAndFillTotalPedido(pedidosItemsList);
            } else if (pedidos.isEsRecargo()) {
                bonificacionRecargoListas.setValue(RECARGAR_LABEL);
                BigDecimal recargoValue = this.pedidos.getRecargo().signum() > 0 ? this.pedidos.getRecargo() : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                bonificacionRecListField.setValue(recargoValue);
                pedidos.setRecargo(bonificacionRecListField.getValue());
                recargoField.setValue(bonificacionRecListField.getValue());
                pedidos.setBonificacion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                bonificacionField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                pedidos.setEsRecargo(true);
                pedidos.setEsBonificacion(false);
                calculateAndFillTotalPedido(pedidosItemsList);
            } else if (pedList != null && !pedList.isEmpty()) {
                bonificacionRecargoListas.setValue(APLICAR_LISTAS_LABEL);
                pedidos.setBonificacion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                pedidos.setRecargo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                bonificacionField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                recargoField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            } else {
                bonificacionRecargoListas.setValue(NINGUNO_LABEL);
                pedidos.setBonificacion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                pedidos.setRecargo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                bonificacionField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                recargoField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            }
            
        } else {
            bonificacionRecargoListas.setValue(NINGUNO_LABEL);
        }
        
       
        ComponentUtils.setDecimalsOFields(bonificacionRecListField, 2);
        bonificacionRecListField.addValueChangeListener(event -> {
            if (bonificacionRecListField.getValue() != null && pedidos != null) {
                if (Boolean.TRUE.equals(esBonificacionPedido.getValue())) {
                    pedidos.setBonificacion(bonificacionRecListField.getValue());
                    bonificacionField.setValue(bonificacionRecListField.getValue());
                    recargoField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    pedidos.setRecargo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    pedidos.setEsBonificacion(true);
                    pedidos.setEsRecargo(false);
                    calculateAndFillTotalPedido(pedidosItemsList);
                } else if (esRecargoPedido.getValue().booleanValue()) {
                    pedidos.setRecargo(bonificacionRecListField.getValue());
                    recargoField.setValue(bonificacionRecListField.getValue());
                    pedidos.setBonificacion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    bonificacionField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    pedidos.setEsRecargo(true);
                    pedidos.setEsBonificacion(false);
                    calculateAndFillTotalPedido(pedidosItemsList);
                } else {
                    pedidos.setBonificacion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    pedidos.setRecargo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    bonificacionField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    recargoField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                }
            } else {
                bonificacionRecListField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            }
        });

        esBonificacionPedido.setValue(false);
        esBonificacionPedido.setVisible(false);

        esRecargoPedido.setVisible(false);
        esRecargoPedido.setValue(false);
        listas.setAllowCustomValue(true);
        listas.setItems(pedidosService.getAllListasPorc());
        listas.setItemLabelGenerator(listasPorcentuales -> listasPorcentuales.getLista().getDescripcion() + " "
                + listasPorcentuales.getPorcentual().getDescripcion());
        listas.getStyle().set("--vaadin-combo-box-overlay-width", "350px");
        if(pedidos != null && pedidos.getIdPedido() != 0 && pedList != null && !pedList.isEmpty()
            && pedList.get(0).getIdListas() != null && pedList.get(0).getIdListas().getLista() != null){
            listas.setValue(pedList.get(0).getIdListas());
            listas.setPlaceholder(pedList.get(0).getIdListas().getLista().getDescripcion() + " "
                + pedList.get(0).getIdListas().getPorcentual().getDescripcion());
        }else{
            listas.setPlaceholder("Seleccione Lista");
            listas.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        }
        
        
        listas.addValueChangeListener(event -> {
            ListasPorcentuales listaSelected = event.getValue();
            if (listaSelected.getPorcentual().getFinVigencia() != null) {
                if (listaSelected.getPorcentual().getFinVigencia().isBefore(LocalDate.now())) {
                    n = Notification.show("La lista seleccionada no está vigente", 3000, Position.MIDDLE);
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    listas.clear();
                } else {
                    this.pedidosListas.setIdPedido(this.pedidos);
                    this.pedidosListas.setIdListas(listaSelected);
                }
            }
            calculateAndFillTotalPedido(pedidosItemsList);
        });

        idArticulo = new ComboBox<>("Articulos");
        idArticulo.setPlaceholder("Buscar Articulo");
        idArticulo.setItems(pedidosService.getAllArticulos());
        idArticulo.setItemLabelGenerator(Articulos::getDescripcion);
        idArticulo.addValueChangeListener(event -> {
            Articulos articulo = event.getValue();
            if (articulo != null && articulo.getIdMedida() != null) {
                medidas.setValue(articulo.getIdMedida().getDescripcion());
                if (articulo != null) {
                    BigDecimal precioCosto = articulo.getPrecio_costo();
                    BigDecimal margenUtilidad = articulo.getMargen_utilidad();
                    if (precioCosto != null && margenUtilidad != null) {
                        try {
                            BigDecimal calculatedPrice = precioCosto.multiply(margenUtilidad)
                                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                    .add(precioCosto);
                            this.pedidosItems.setPrecioArticulo(calculatedPrice.setScale(2, RoundingMode.HALF_UP));
                        } catch (ArithmeticException | IllegalStateException e) {
                            this.pedidosItems.setPrecioArticulo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)); // Default
                                                                                                                    // value
                                                                                                                    // in
                                                                                                                    // case
                                                                                                                    // of
                                                                                                                    // error
                            Notification.show("Error al calcular el precio del artículo: " + e.getMessage(),
                                    3000, Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    } else {
                        this.pedidosItems.setPrecioArticulo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)); // Default
                                                                                                                // value
                                                                                                                // if
                                                                                                                // null
                    }
                }
            } else {
                medidas.setValue("");
                this.pedidosItems.setPrecioArticulo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            }
        });
        cantidad = new NumberField("Cantidad");
        cantidad.setValue(1.0);
        cantidad.setStep(1.0);
        cantidad.setStepButtonsVisible(true);
        cantidad.setMin(1.0);
        cantidad.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                cantidad.setValue(1.0); // Restablece el valor predeterminado
            }
        });

        medidas = new TextField("Unidades de Medida");
        medidas.setReadOnly(true);

        esBonificacion = new Checkbox();
        esBonificacion.setValue(false);
        esBonificacion.setVisible(false);

        esRecargo = new Checkbox();
        esRecargo.setVisible(false);
        bonificacionRecargoArticulo = new RadioButtonGroup<>();
        bonificacionRecargoArticulo.setValue(NINGUNO_LABEL); // Allow no selection
        bonificacionRecargoArticulo.setItems(NINGUNO_LABEL, BONIFICAR_LABEL, RECARGAR_LABEL);
        // bonificacion = new BigDecimalField();
        // bonificacion.setVisible(false);
        // ComponentUtils.setDecimalsOFields(bonificacion, 2);
        // recargo = new BigDecimalField();
        // recargo.setVisible(false);
        // ComponentUtils.setDecimalsOFields(recargo, 2);
        bonificacionRecArtField = new BigDecimalField();
        bonificacionRecArtField.setEnabled(false);
        if(bonificacionRecArtField.getValue() != null) {
            ComponentUtils.setDecimalsOFields(bonificacionRecArtField, 2);
        }else {
            bonificacionRecArtField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }
        persistBonArt = new Checkbox("Persistir Bonificación en Articulo");
        persistBonArt.setEnabled(false);
        persistBonArt.setValue(false);
        bonificacionRecargoArticulo.addValueChangeListener(event -> {
            String selectedOption = event.getValue();
            if (BONIFICAR_LABEL.equals(selectedOption)) {
                esBonificacion.setValue(true);
                esRecargo.setValue(false);
                esRecargo.setValue(false);
                bonificacionRecArtField.setEnabled(true);
                bonificacionRecArtField.focus();
                persistBonArt.setValue(false);
            } else if (RECARGAR_LABEL.equals(selectedOption)) {
                esRecargo.setValue(true);
                esBonificacion.setValue(false);
                bonificacionRecArtField.setEnabled(true);
                persistBonArt.setValue(false);
                bonificacionRecArtField.focus();
            } else if (NINGUNO_LABEL.equals(selectedOption)) {
                esBonificacion.setValue(false);
                esRecargo.setValue(false);
                bonificacionRecArtField.setEnabled(false);
                persistBonArt.setValue(false);
                persistBonArt.setEnabled(false);
                recargo.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                bonificacion.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            }
            
        });

        agregarPedido = new Button("Agregar al Pedido", e -> {
            if (this.pedidosItems == null) {
                this.pedidosItems = new PedidosItems();
            }

            try {
                binderPedidosItems.writeBean(this.pedidosItems);
            } catch (ValidationException ex) {
                Notification.show("Error al validar los datos: " + ex.getMessage(), 3000, Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (esBonificacion.getValue().booleanValue()) {
                if (bonificacionRecArtField.getValue() != null) {
                    this.pedidosItems.setBonificacion(bonificacionRecArtField.getValue());
                    this.pedidosItems.setRecargo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                } else {
                    this.pedidosItems.setBonificacion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    this.pedidosItems.setRecargo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                }
            } else if (esRecargo.getValue().booleanValue()) {
                if (bonificacionRecArtField.getValue() != null) {
                    this.pedidosItems.setRecargo(bonificacionRecArtField.getValue());
                    this.pedidosItems.setBonificacion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                } else {
                    this.pedidosItems.setRecargo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                    this.pedidosItems.setBonificacion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                }
            }
            if (pedidosItems.isEsBonificacion()) {
                boolean isError = validarBonificacionRecargoArticulo(pedidosItems.getBonificacion(),
                            BONIFICAR_LABEL);
                if (isError) {
                    return;
                }
            } else if (pedidosItems.isEsRecargo()) {
                boolean isError = validarBonificacionRecargoArticulo(pedidosItems.getRecargo(), RECARGAR_LABEL);
                if (isError) {
                    return;
                }
            }
            
            PedidosItems pedItem = new PedidosItems();
            pedItem.setIdArticulo(pedidosItems.getIdArticulo());
            pedItem.setIdPedido(pedidosItems.getIdPedido());
            pedItem.setCantidad(pedidosItems.getCantidad());
            pedItem.setEsBonificacion(pedidosItems.isEsBonificacion());
            pedItem.setEsRecargo(pedidosItems.isEsRecargo());
            pedItem.setBonificacion(pedidosItems.getBonificacion());
            pedItem.setRecargo(pedidosItems.getRecargo());
            pedItem.setPersistBonArt(pedidosItems.isPersistBonArt());
            BigDecimal preciCosto = pedidosItems.getIdArticulo().getPrecio_costo();
            BigDecimal margenUtilidad = pedidosItems.getIdArticulo().getMargen_utilidad();
            pedItem.setPrecioArticulo(
                    preciCosto.multiply(margenUtilidad).divide(BigDecimal.valueOf(100)).add(preciCosto).setScale(2,
                            RoundingMode.HALF_UP));

            // Asegurarse de que no se sobrescriban los elementos existentes
            pedidosItemsList.add(pedItem);

            // Actualizar el grid con una nueva lista para evitar referencias duplicadas
            gridPedidosItems.setItems(new ArrayList<>(pedidosItemsList));
            calculateAndFillTotalPedido(pedidosItemsList);

            gridPedidosItems.recalculateColumnWidths();

            bonificacionRecargoArticulo.setValue(NINGUNO_LABEL);
            bonificacionRecArtField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            initBinderPedidosItems();
        });
        agregarPedido.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        agregarPedido.getStyle().set("background-color", "var(--lumo-primary-color)");
        agregarPedido.getStyle().set("color", "white");
        agregarPedido.setEnabled(false);
        agregarPedido.setHeight(100, Unit.PERCENTAGE);
        gridPedidosItems = new Grid<>(PedidosItems.class, false);
        gridPedidosItems.setSelectionMode(Grid.SelectionMode.MULTI);
        gridPedidosItems.addColumn(pedItems -> pedItems.getIdArticulo().getDescripcion())
                .setHeader("Articulo").setAutoWidth(true);
        gridPedidosItems.addColumn(PedidosItems::getCantidad)
                .setHeader("Cantidad").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(pedItems -> pedItems.getIdArticulo().getIdAlicuota().getDescripcion())
                .setHeader("Alicuota").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(PedidosItems::getPrecioArticulo)
                .setHeader("Precio Unitario").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems
                .addColumn(pedItems -> pedItems.getBonificacion() != null
                        ? pedItems.getBonificacion().setScale(2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                .setHeader("Bon.(%)").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems
                .addColumn(
                        pedItems -> pedItems.getRecargo() != null
                                ? pedItems.getRecargo().setScale(2, RoundingMode.HALF_UP)
                                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                .setHeader("Rec.(%)").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(pedItems -> ComponentUtils.calcSubTotalSinImp(pedItems))
                .setHeader("Sub total-Imp").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(pedItems -> ComponentUtils.calcSubTotalConImp(pedItems))
                .setHeader("Sub total+Imp").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        
        if(this.pedidos != null && this.pedidos.getIdPedido() != 0) {
            pedidosItemsList = pedidosService.findPedidosItemsByIdPedidos(this.pedidos);
            calculateAndFillTotalPedido(pedidosItemsList);
        } 
        
        gridPedidosItems.setItems(pedidosItemsList);

        gridPedidosItems.addSelectionListener(event -> {
            pedidosItemsSelected.clear();
            pedidosItemsSelected.addAll(event.getAllSelectedItems());
            rolbackItem.setEnabled(!pedidosItemsSelected.isEmpty());

        });

        notaAlPie = new TextArea("Nota al Pie");
        notaAlPie.setWidthFull();
        notaAlPie.setHeight(90, Unit.PIXELS);
        bonificacionField.setWidthFull();
        bonificacionField.setReadOnly(true);
        ComponentUtils.setDecimalsOFields(bonificacionField, 2);
        recargoField.setVisible(false);
        ComponentUtils.setDecimalsOFields(recargoField, 2);
        subTotalSinImpuestos.setWidthFull();
        subTotalSinImpuestos.setReadOnly(true);
        ComponentUtils.setDecimalsOFields(subTotalSinImpuestos, 2);
        subTotalConIMpuestos.setReadOnly(true);
        ComponentUtils.setDecimalsOFields(subTotalConIMpuestos, 2);
        subTotalConIMpuestos.setWidthFull();
        
        ComponentUtils.setDecimalsOFields(totalPedido, 2);
        totalPedido.setWidthFull();
        cancelarAcciones = new Button("Cancelar Acciones", e -> {
            bonificacionRecargoListas.setValue(NINGUNO_LABEL);

        });
        cancelarAcciones.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        cancelarAcciones.getStyle().set("background-color", "var(--lumo-warning-color)");
        cancelarAcciones.getStyle().set("color", "black");
        rolbackItem = new Button("Rollback Item", e -> {
            if (!pedidosItemsSelected.isEmpty()) {
                pedidosItemsList.removeAll(pedidosItemsSelected);
                gridPedidosItems.setItems(new ArrayList<>(pedidosItemsList));
                gridPedidosItems.recalculateColumnWidths();
                calculateAndFillTotalPedido(pedidosItemsList);
                bonificacionField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                pedidosItemsSelected.clear();
            }
        });
        rolbackItem.setEnabled(false);
        rolbackItem.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        rolbackItem.getStyle().set("background-color", "var(--lumo-warning-color)");
        rolbackItem.getStyle().set("color", "black");
        cancelarPedido = new Button("Cancelar Pedido", e -> {
            refreshGrid();
            this.binder = new CollaborationBinder<>(Pedidos.class, userInfo);
            serialize();
            dialog.close();

        });
        cancelarPedido.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        cancelarPedido.getStyle().set("background-color", "var(--lumo-error-color)");
        agregarPedidoBandeja = new Button("Agregar Pedido a Bandeja", e -> {
            // Acción del botón
            try {
                if (this.pedidos == null) {
                    this.pedidos = new Pedidos();
                }

                binder.writeBean(this.pedidos);
                if (pedidosItemsList.isEmpty()) {
                    Notification.show("Debe agregar al menos un Articulo al pedido", 3000, Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                pedidosItemsList.stream().filter(pi -> pi.isPersistBonArt())
                        .forEach(pi -> {
                            pi.getIdArticulo().setEs_bonificado(true);
                            pi.getIdArticulo().setBonificacion(pi.getBonificacion());
                            pedidosService.updateArticulos(pi.getIdArticulo());
                        });

                pedidosService.update(this.pedidos);
                pedidosService.saveAllPedItemsList(pedidosItemsList);
                if (this.pedidosListas != null && this.pedidosListas.getIdListas() != null
                        && this.pedidosListas.getIdPedido() != null) {
                    pedidosService.savePedidosListas(this.pedidosListas);
                }

                refreshGrid();
                dialog.close();
                Notification.show("Se ha Generado el Pedido " + this.pedidos.getIdPedido()
                        + " con éxito", 3000, Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al Actualizar los datos. Alguien mas está actualizando los datos.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show(
                        "Error al Guardar/Modificar los datos. Revise Nuevamente que todos los datos sean Válidos");
            } catch (Exception except) {
                if (except.getCause() != null && except.getCause().getCause() instanceof SQLException e1) {
                    if (e1.getMessage().contains("Ya existe la llave")) {
                        Notification n = Notification.show(
                                "El Pedido " + this.pedidos.getIdPedido()
                                        + " ya existe");
                        n.setPosition(Position.MIDDLE);
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                    if (e1.getMessage().contains("el valor nulo")) {
                        Notification n = Notification.show("Debe seleccionar una Provincia");
                        n.setPosition(Position.MIDDLE);
                        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                } else {
                    Notification n = Notification.show("Error al Guardar los datos: " + except.getMessage());
                    n.setPosition(Position.MIDDLE);
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }

            }
        });
        agregarPedidoBandeja.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        agregarPedidoBandeja.getStyle().set("background-color", "var(--lumo-primary-color)");
        agregarPedidoBandeja.getStyle().set("color", "black");
        Div pedidoForm = new Div();
        pedidoForm.setWidthFull();
        pedidoForm.getStyle().setBorder("2px solid green");
        VerticalLayout pedidoFormLayout = new VerticalLayout();
        pedidoFormLayout.setSpacing(false);
        HorizontalLayout pedFormHorizontalLayout1 = new HorizontalLayout();
        pedFormHorizontalLayout1.setPadding(false);
        pedFormHorizontalLayout1.setFlexGrow(1, fechaPedido, plataforma, idPedidoField,
                estadoPedidoSpan, estadoPagoSpan);
        pedFormHorizontalLayout1.add(fechaPedido, plataforma, idPedidoField, estadoPedidoSpan, estadoPagoSpan);
        pedFormHorizontalLayout1.setAlignSelf(FlexComponent.Alignment.CENTER,
                fechaPedido, plataforma, idPedidoField, estadoPedidoSpan, estadoPagoSpan);
        HorizontalLayout pedFormHorizontalLayout2 = new HorizontalLayout();
        pedFormHorizontalLayout2.setWidthFull();
        pedFormHorizontalLayout2.setPadding(false);
        pedFormHorizontalLayout2.setFlexGrow(1, idCliente, domicilios);
        pedFormHorizontalLayout2.add(idCliente, domicilios);
        HorizontalLayout pedFormHorizontalLayout3 = new HorizontalLayout();
        pedFormHorizontalLayout3.setWidthFull();
        pedFormHorizontalLayout3.setPadding(false);
        pedFormHorizontalLayout3.setFlexGrow(1, vendedores);
        pedFormHorizontalLayout3.add(vendedores);
        HorizontalLayout pedFormHorizontalLayout4 = new HorizontalLayout();
        pedFormHorizontalLayout4.setWidthFull();
        pedFormHorizontalLayout4.setPadding(false);
        pedFormHorizontalLayout4.setFlexGrow(1, bonificacionRecargoListas, bonificacionRecListField, listas,
                listas);
        pedFormHorizontalLayout4.add(bonificacionRecargoListas, bonificacionRecListField, listas,
                listas);
        pedidoFormLayout.add(pedFormHorizontalLayout1, pedFormHorizontalLayout2,
                pedFormHorizontalLayout3, pedFormHorizontalLayout4);
        pedidoForm.add(pedidoFormLayout);

        Div pedidoItemsForm = new Div();
        pedidoItemsForm.setWidthFull();
        HorizontalLayout pedidoItemsFormHorizontalLayoutButton = new HorizontalLayout();
        VerticalLayout pedidoItemsFormLayout = new VerticalLayout();
        pedidoItemsFormLayout.setSpacing(false);
        HorizontalLayout pedidoItemsFormHorizontalLayout1 = new HorizontalLayout();
        pedidoItemsFormHorizontalLayout1.setWidthFull();
        pedidoItemsFormHorizontalLayout1.setFlexGrow(1, idArticulo);
        pedidoItemsFormHorizontalLayout1.add(idArticulo, medidas, cantidad);
        pedidoItemsFormHorizontalLayout1.setSpacing(true);
        HorizontalLayout pedidoItemsFormHorizontalLayout2 = new HorizontalLayout();
        pedidoItemsFormHorizontalLayout2.setSpacing(true);
        pedidoItemsFormHorizontalLayout2.add(bonificacionRecargoArticulo, bonificacionRecArtField, persistBonArt);
        pedidoItemsFormLayout.add(pedidoItemsFormHorizontalLayout1,
                pedidoItemsFormHorizontalLayout2);
        pedidoItemsFormHorizontalLayoutButton.setAlignSelf(FlexComponent.Alignment.CENTER,
                pedidoItemsFormLayout, agregarPedido);
        pedidoItemsFormHorizontalLayoutButton.setWidthFull();
        pedidoItemsFormHorizontalLayoutButton.setPadding(true);
        pedidoItemsFormHorizontalLayoutButton.setFlexGrow(1, agregarPedido);
        pedidoItemsFormHorizontalLayoutButton.add(pedidoItemsFormLayout, agregarPedido);
        pedidoItemsForm.getStyle().setBackgroundColor("var(--lumo-contrast-10pct)");
        pedidoItemsForm.getStyle().setBorder("2px solid green");

        pedidoItemsForm.add(pedidoItemsFormHorizontalLayoutButton);

        Div pedidoItemsGrid = new Div();
        pedidoItemsGrid.setWidthFull();
        pedidoItemsGrid.setHeight("200px"); // Set height to show approximately 5 items
        gridPedidosItems.setHeightFull();
        pedidoItemsGrid.add(gridPedidosItems);

        Div pedidoForm2 = new Div();
        pedidoForm2.setWidthFull();
        Div textAreaDiv = new Div();
        textAreaDiv.setWidthFull();
        textAreaDiv.add(notaAlPie);
        HorizontalLayout pedidoForm2HorizontalLayout = new HorizontalLayout();
        pedidoForm2HorizontalLayout.setWidthFull();
        pedidoForm2HorizontalLayout.setFlexGrow(1, textAreaDiv);
        VerticalLayout labeLayout = new VerticalLayout();
        labeLayout.setWidth(30, Unit.PERCENTAGE);
        labeLayout.setAlignItems(FlexComponent.Alignment.END);
        labeLayout.add(new Span("Bonificación"), new Span("Subtotal - Impuestos"),
                new Span("Subtotal + Impuestos"), new Span("TOTALPEDIDO"));
        VerticalLayout pedidoForm2Layout = new VerticalLayout();
        pedidoForm2Layout.setAlignItems(FlexComponent.Alignment.START);
        pedidoForm2Layout.setSpacing(false);
        pedidoForm2Layout.setPadding(false);
        pedidoForm2Layout.setWidth(30, Unit.PERCENTAGE);
        pedidoForm2Layout.add(bonificacionField, subTotalSinImpuestos,
                subTotalConIMpuestos, totalPedido);
        pedidoForm2HorizontalLayout.add(textAreaDiv, labeLayout, pedidoForm2Layout);
        pedidoForm2.add(pedidoForm2HorizontalLayout);

        HorizontalLayout buttoHorizontalLayout = new HorizontalLayout();
        buttoHorizontalLayout.add(cancelarAcciones, rolbackItem, cancelarPedido,
                agregarPedidoBandeja);

        VerticalLayout principVerticalLayout = new VerticalLayout();
        principVerticalLayout.setPadding(false);
        principVerticalLayout.add(pedidoForm, pedidoItemsForm, pedidoItemsGrid,
                pedidoForm2, buttoHorizontalLayout);

        dialog = new Dialog();
        dialog.setHeaderTitle("Nuevo Pedido");
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> {
                    refreshGrid();
                    this.binder = new CollaborationBinder<>(Pedidos.class, userInfo);
                    serialize();
                    dialog.close();
                });
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getHeader().add(closeButton);
        return principVerticalLayout;

    }

    private void calculateAndFillTotalPedido(List<PedidosItems> pedidosItemsList2) {
        BigDecimal subTotalSinImp = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal subTotalConImp = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        for (PedidosItems item : pedidosItemsList) {
            subTotalConImp = subTotalConImp.add(ComponentUtils.calcSubTotalConImp(item));
            subTotalSinImp = subTotalSinImp.add(ComponentUtils.calcSubTotalSinImp(item));
        }
        if (this.pedidos.isEsBonificacion()) {
            subTotalConImp = subTotalConImp
                    .subtract(subTotalConImp.multiply(this.pedidos.getBonificacion()).divide(BigDecimal.valueOf(100)));
            subTotalSinImp = subTotalSinImp
                    .subtract(subTotalSinImp.multiply(this.pedidos.getBonificacion()).divide(BigDecimal.valueOf(100)));
        } else if (this.pedidos.isEsRecargo()) {
            subTotalConImp = subTotalConImp
                    .add(subTotalConImp.multiply(this.pedidos.getRecargo()).divide(BigDecimal.valueOf(100)));
            subTotalSinImp = subTotalSinImp
                    .add(subTotalSinImp.multiply(this.pedidos.getRecargo()).divide(BigDecimal.valueOf(100)));
        }
        if (this.isAplicarListas) {
            if (listas.getValue() != null) {
                ListasPorcentuales listPor = listas.getValue();
                String clasificacion = listPor.getPorcentual().getClasificacion().getDisplayName();
                BigDecimal porcentual = listPor.getPorcentual().getPorcentual();
                if ("Bonificación".equals(clasificacion)) {
                    subTotalConImp = subTotalConImp
                            .subtract(subTotalConImp.multiply(porcentual).divide(BigDecimal.valueOf(100)));
                    subTotalSinImp = subTotalSinImp
                            .subtract(subTotalSinImp.multiply(porcentual).divide(BigDecimal.valueOf(100)));
                } else if ("Recargo".equals(clasificacion)) {
                    subTotalConImp = subTotalConImp
                            .add(subTotalConImp.multiply(porcentual).divide(BigDecimal.valueOf(100)));
                    subTotalSinImp = subTotalSinImp
                            .add(subTotalSinImp.multiply(porcentual).divide(BigDecimal.valueOf(100)));
                }
            }
            subTotalConIMpuestos.setValue(subTotalConImp.setScale(2, RoundingMode.HALF_UP));
            subTotalSinImpuestos.setValue(subTotalSinImp.setScale(2, RoundingMode.HALF_UP));
        } else {
            subTotalConIMpuestos.setValue(subTotalConImp.setScale(2, RoundingMode.HALF_UP));
            subTotalSinImpuestos.setValue(subTotalSinImp.setScale(2, RoundingMode.HALF_UP));
        }
        totalPedido.setValue(subTotalConImp.setScale(2, RoundingMode.HALF_UP));

    }

    private void refreshGrid() {
        pedidosItemsList.clear();
        gridPedidosItems.setItems(pedidosItemsList);
        gridPedidos.setItems(pedidosService.pedidosList());
    }

    private void initBinderPedidos() {
        if (pedidos == null) {
            this.pedidos = new Pedidos();
            this.pedidosListas = new PedidosListas();
            this.pedidos.setFechaPedido(LocalDate.now());
        }

        configurePedidosBinder();

        this.binder.setTopic("pedidos/" + UUID.randomUUID(), () -> this.pedidos);
    }

    private void initBinderPedidosItems() {
        this.pedidosItems = new PedidosItems();
        this.pedidosItems.setCantidad(BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP));
        this.pedidosItems.setPrecioArticulo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        this.pedidosItems.setIdPedido(this.pedidos);
        this.pedidosItems.setPersistBonArt(false);
        this.binderPedidosItems = new CollaborationBinder<>(PedidosItems.class, userInfo);

        this.binderPedidosItems.setSerializer(Articulos.class,
                idart -> String.valueOf(idart.getIdArticulo()),
                idArt -> pedidosService
                        .findArticulosById(Integer.parseInt(idArt)));

        configurePedidosItemsBinder();

        this.binderPedidosItems.setTopic("pedidosItems/" + UUID.randomUUID(), () -> this.pedidosItems);

    }

    private void configurePedidosBinder() {

        this.binder.forField(fechaPedido).asRequired("Fecha es requerido")
                .bind("fechaPedido");
        this.pedidos.setPlataforma(PlataformaEnum.WEB);
        this.binder.forField(plataforma, String.class)
                .withConverter(
                        value -> {
                            if (value == null || value.trim().isEmpty()) {
                                return null; // Maneja valores nulos o vacíos
                            }
                            try {
                                return PlataformaEnum.WEB;
                            } catch (IllegalArgumentException e) {
                                throw new RuntimeException("Valor inválido para PlataformaEnum: " + value, e);
                            }
                        },
                        enumValue -> enumValue != null ? enumValue.getPlataforma() : "" // Maneja valores nulos al
                                                                                        // convertir de Enum a String
                )
                .bind("plataforma");
        this.pedidos.setEstadoPedido(EstadoPedidoEnum.En_Picking);
        this.binder.forField(estadoPedido, String.class)
                .withConverter(
                        value -> {
                            if (value == null || value.trim().isEmpty()) {
                                return null; // Maneja valores nulos o vacíos
                            }
                            try {
                                return EstadoPedidoEnum.En_Picking;
                            } catch (IllegalArgumentException e) {
                                throw new RuntimeException("Valor inválido para EstadoPedidoEnum: " + value, e);
                            }
                        },
                        enumValue -> enumValue != null ? enumValue.getEstadoPedido() : "" // Maneja valores nulos al
                                                                                          // convertir de Enum a String
                )
                .bind("estadoPedido");
        this.pedidos.setEstadoPago(EstadoPagoEnum.Pago_Pendiente);
        this.binder.forField(estadoPago, String.class)
                .withConverter(
                        value -> {
                            if (value == null || value.trim().isEmpty()) {
                                return null; // Maneja valores nulos o vacíos
                            }
                            try {
                                return EstadoPagoEnum.Pago_Pendiente;
                            } catch (IllegalArgumentException e) {
                                throw new RuntimeException("Valor inválido para EstadoPagoEnum: " + value, e);
                            }
                        },
                        enumValue -> enumValue != null ? enumValue.getEstadoPago() : "" // Maneja valores nulos al
                                                                                        // convertir de Enum a String
                )
                .bind("estadoPago");
        this.binder.forField(idCliente).asRequired("Cliente es requerido")
                .bind("idCliente");
        this.binder.forField(vendedores).asRequired("Vendedor es requerido")
                .bind("idVendedor");
        this.binder.forField(domicilios).asRequired("Domicilio es requerido")
                .bind("domicilioClienteString");
        // this.pedidos.setDomicilioClienteString(domicilioClienteString.getValue());
        // this.binder.forField(domicilioClienteString).bind("domicilioClienteString");
        this.binder.forField(esBonificacionPedido).bind("esBonificacion");
        this.binder.forField(esRecargoPedido).bind("esRecargo");
        this.binder.forField(bonificacionField).bind("bonificacion");
        this.binder.forField(recargoField).bind("recargo");
        this.binder.forField(notaAlPie).bind("notaAlPie");

        this.binder.addStatusChangeListener(
                event -> agregarPedidoBandeja.setEnabled(binder.isValid()));

        this.binder.bindInstanceFields(this);
    }

    private boolean validarBonificacionRecargoArticulo(BigDecimal value, String string) {
        boolean isError = false;
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            isError = true;
            bonificacionRecArtField.setInvalid(true);
            Notification n = Notification.show(
                    "Has Seleccionado la opción de " + string + " pero no se ha ingresado el porcentaje",
                    3000, Position.MIDDLE);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        return isError;
    }

    private void configurePedidosItemsBinder() {
        this.binderPedidosItems.forField(idArticulo).asRequired("IdArticulo es requerido")
                .bind("idArticulo");

        this.binderPedidosItems.forField(cantidad, Double.class)
                .asRequired("La cantidad es Requerida")
                .withConverter(
                        value -> value != null ? BigDecimal.valueOf(value) : null,
                        value -> value != null ? value.doubleValue() : null,
                        "Debe ser un número válido")
                .withValidator(pedidoItem -> {
                    // si querés asegurarte del pedido
                    return this.pedidos != null;
                }, "Error al asignar el pedido")
                .bind("cantidad");
        this.binderPedidosItems.forField(esBonificacion).bind("esBonificacion");
        this.binderPedidosItems.forField(esRecargo).bind("esRecargo");
        this.pedidosItems.setPersistBonArt(persistBonArt.getValue());
        this.binderPedidosItems.forField(persistBonArt).bind("persistBonArt");

        this.binderPedidosItems.addStatusChangeListener(
                event -> agregarPedido.setEnabled(binderPedidosItems.isValid()));

        try {
            binderPedidosItems.bindInstanceFields(this.pedidosItems);
        } catch (IllegalStateException e) {
            Notification.show("Error al enlazar campos: " + e.getMessage(), 3000, Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    public String construirDireccion(Domicilios dom) {
        String calle = dom.getCalle();
        String numero = String.valueOf(dom.getNumero());
        StringBuilder direccionBuilder = new StringBuilder();
        direccionBuilder.append(dom.getIdDomicilio()).append(" - ");
        direccionBuilder.append(calle).append(" ").append(numero);

        appendIfNotEmpty(direccionBuilder, dom.getBarrio(), ", Barrio ");
        appendIfNotEmpty(direccionBuilder, dom.getManzana(), ", Manzana ");
        appendIfNotEmpty(direccionBuilder, dom.getCasa(), ", Casa ");
        appendIfNotEmpty(direccionBuilder, dom.getSector(), ", Sector ");
        appendIfNotEmpty(direccionBuilder, dom.getDepto(), ", Depto ");
        appendIfNotEmpty(direccionBuilder, dom.getOficina(), ", Oficina ");
        appendIfNotEmpty(direccionBuilder, dom.getLote(), ", Lote ");

        Localidades localidad = dom.getLocalidad();
        if (localidad != null) {
            direccionBuilder.append(", ").append(localidad.getNombre());
            Provincias provincia = localidad.getDepartamentos() != null ? localidad.getDepartamentos().getProvincias() : null;
            if (provincia != null) {
                direccionBuilder.append(", ").append(provincia.getProvincia());
            }
        }

        return direccionBuilder.toString();
    }

    private void appendIfNotEmpty(StringBuilder builder, String value, String label) {
        if (value != null && !value.isEmpty()) {
            builder.append(label).append(value);
        }
    }

}