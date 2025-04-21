package com.ideadistribuidora.visus.views.pedidos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ideadistribuidora.visus.data.Articulos;
import com.ideadistribuidora.visus.data.Clientes;
import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Listas;
import com.ideadistribuidora.visus.data.Pedidos;
import com.ideadistribuidora.visus.data.PedidosItems;
import com.ideadistribuidora.visus.data.Vendedores;
import com.ideadistribuidora.visus.data.Zonas;
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
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Pedidos")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 1)
@Route(value = "2/:pedidosID?/:action?(edit)")
public class PedidosView extends Div {
    private CollaborationBinder<Pedidos> binder;
    private CollaborationBinder<PedidosItems> binderPedidosItems;
    private Notification n;
    private final PedidosService pedidosService;
    private Dialog dialog;
    private DatePicker fechaPedido;
    private TextField plataforma;
    private IntegerField pedido;
    private Span estadoPedido;
    private ComboBox<EstadoPedidoEnum> estadoPedidoCombo;
    private ComboBox<PlataformaEnum> plataformaCombo;
    private Span estadoPago;

    private ComboBox<Clientes> clientes;
    private ComboBox<Vendedores> vendedores;
    private ComboBox<Zonas> zonas;

    private ComboBox<Domicilios> domicilios;
    private TextField domicilioField;

    private RadioButtonGroup<String> bonificacionRecargoListas;
    private BigDecimalField bonificacionRecListField;
    private ComboBox<Listas> listas;
    private Checkbox esBonificacionPedido;
    private Checkbox esRecargoPedido;

    private ComboBox<Articulos> articulos;
    private TextField medidas;
    private IntegerField cantidadField;

    private RadioButtonGroup<String> bonificacionRecargoArticulo;
    private BigDecimalField bonificacionRecArtField;
    private BigDecimalField bonificacion;
    private BigDecimalField recargo;
    private Checkbox persistrBonRec;
    private Checkbox esBonificacion;
    private Checkbox esRecargo;
    private Button agregarPedido;

    private final Grid<Pedidos> gridPedidos = new Grid<>(Pedidos.class, false);

    private final Grid<PedidosItems> gridPedidosItems = new Grid<>(PedidosItems.class, false);

    private TextArea notaAlPie;
    private BigDecimalField bonificacionField;// solo lectura
    private BigDecimalField subTotalSinImpuestos;// solo lectura
    private BigDecimalField subTotalConIMpuestos;// solo lectura
    private BigDecimalField totalPedido;// solo lectura

    private Button nuevoPedido;
    private Button cancelar;
    private Button Picking;
    private Button facturar;

    private Button cancelarAcciones;
    private Button rolbackItem;
    private Button cancelarPedido;
    private Button agregarPedidoBandeja;

    CollaborationAvatarGroup avatarGroup;

    private Pedidos pedidos;
    private PedidosItems pedidosItems;
    private List<PedidosItems> pedidosItemsList = new ArrayList<>();
    private List<PedidosItems> pedidosItemsSelected = new ArrayList<>();

    public PedidosView(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
        addClassNames("pedidos-view");

        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");
        binder = new CollaborationBinder<>(Pedidos.class, userInfo);
        binder.setTopic("pedidos/" + UUID.randomUUID(), () -> this.pedidos);
        binderPedidosItems = new CollaborationBinder<>(PedidosItems.class, userInfo);
        binderPedidosItems.setTopic("pedidosItems/" + UUID.randomUUID(), () -> {
            if (this.pedidosItemsList.isEmpty()) {
                PedidosItems pedidosItems = new PedidosItems();
                pedidosItems.setCantidad(BigDecimal.ONE);
                return pedidosItems;
            }
            return this.pedidosItemsList.get(0); // Ejemplo: usa el primer item
        });

        binderPedidosItems.setSerializer(Articulos.class,
                articulos -> String.valueOf(articulos.getIdArticulo()),
                idArticulos -> pedidosService
                        .findArticulosById(Integer.parseInt(idArticulos)));
        binder.setSerializer(Clientes.class,
                clientes -> String.valueOf(clientes.getIdCliente()),
                idClientes -> (Clientes) pedidosService
                        .findByIdClientes(Integer.parseInt(idClientes)));
        binder.setSerializer(Vendedores.class,
                vendedores -> String.valueOf(vendedores.getIdVendedor()),
                idVendedores -> (Vendedores) pedidosService
                        .findByIdVendedores(Integer.parseInt(idVendedores)));
        binder.setSerializer(Domicilios.class,
                domicilios -> String.valueOf(domicilios.getIdDomicilio()),
                idDomicilios -> (Domicilios) pedidosService
                        .findByIdDomicilios(Integer.parseInt(idDomicilios)));

        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "visible");

        HorizontalLayout horizontalButLayout = new HorizontalLayout();
        nuevoPedido = new Button("Nuevo Pedido", event -> {
            fillDialog();
            dialog.open();
        });
        cancelar = new Button("Cancelar", e -> {
            // agregar acciones
        });
        Picking = new Button("Picking", e -> {
            // agregar acciones
        });
        facturar = new Button("Facturar", e -> {
            // agregar acciones
        });
        horizontalButLayout.add(nuevoPedido, cancelar, Picking, facturar);

        gridPedidos.addColumn("idPedido")
                .setHeader("Pedido").setAutoWidth(true);
        gridPedidos.addColumn(pedidos -> pedidos.getIdCliente().getNombreCliente())
                .setHeader("Cliente").setAutoWidth(true);
        gridPedidos.addColumn("estadoPedido")
                .setHeader("Status").setAutoWidth(true);
        gridPedidos
                .addColumn("estadoPago")
                .setHeader("Pago").setAutoWidth(true);
        // gridPedidosItems.addColumn("stock_minimo").setHeader("Stock Min.")
        // .setAutoWidth(true);
        // gridPedidosItems.addColumn("stock_maximo").setHeader("Stock Máx.")
        // .setAutoWidth(true);
        // gridPedidosItems.addColumn(articulos -> articulos.getBonificacion() == null ?
        // 0
        // : articulos.getBonificacion().setScale(4,
        // RoundingMode.HALF_UP)).setHeader("Bonif.(%)")
        // .setAutoWidth(true);
        // gridPedidosItems.addColumn(articulos ->
        // calcularPrecioFinal(articulos).getPrecioFinalConIva().setScale(4,
        // RoundingMode.HALF_UP)).setHeader("Precio Final").setAutoWidth(true);
        // gridPedidosItems.addColumn(articulos ->
        // articulos.getEstado().getDisplayEstadoArticulo()).setHeader("Estado")
        // .setAutoWidth(true);
        gridPedidos.setItems(pedidosService.pedidosList());

        add(avatarGroup,
                horizontalButLayout, gridPedidos);

        // Configure CollaborationBinder
        // configureBinder();

    }

    private void fillDialog() {
        fechaPedido = new DatePicker("Fecha");
        fechaPedido.setValue(LocalDate.now());
        fechaPedido.setPlaceholder("dd/mm/aaaa");
        fechaPedido.setI18n(ComponentUtils.getI18n());
        fechaPedido.addValueChangeListener(event -> {
            if (event.getValue() == null) {
            fechaPedido.setValue(LocalDate.now());
            }
        });

        plataforma = new TextField("Plataforma");
        plataforma.setValue(PlataformaEnum.WEB.name());
        plataforma.setReadOnly(true);
        if (pedidos == null) {
            pedidos = new Pedidos();
        }
        pedido = new IntegerField("Pedido");
        pedido.setValue(this.pedidos.getIdPedido());
        pedido.setReadOnly(true);
        estadoPedido = new Span();
        estadoPedido.setText(EstadoPedidoEnum.EN_PICKING.getEstadoPedido());
        estadoPedido.getElement().getStyle().setBackgroundColor("var(--lumo-primary-text-color)");
        estadoPedido.getElement().getStyle().setColor("white");
        estadoPedido.getElement().getStyle().setFontWeight("bold");
        estadoPedido.getElement().getStyle().setPaddingLeft("10px");
        estadoPedido.getElement().getStyle().setPaddingRight("10px");
        estadoPago = new Span();
        estadoPago.setText(EstadoPagoEnum.valueOf("PAGO_PENDIENTE").getEstadoPago());
        estadoPago.getStyle().setBackgroundColor("orange");
        // estadoPago.getElement().getStyle().set("background-color",
        // "var(--lumo-error-color-50pct)");
        // estadoPedido.getElement().getStyle().set("padding", "5px");
        estadoPago.getElement().getStyle().setColor("white");
        estadoPago.getElement().getStyle().setFontWeight("bold");
        estadoPago.getElement().getStyle().setPaddingLeft("10px");
        estadoPago.getElement().getStyle().setPaddingRight("10px");

        clientes = new ComboBox<>("Cliente");
        clientes.setWidth(25, Unit.PERCENTAGE);
        clientes.setPlaceholder("Buscar Cliente");
        clientes.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        clientes.setItems(pedidosService.getAllclientes());
        clientes.setItemLabelGenerator(Clientes::getNombreCliente);
        clientes.addValueChangeListener(event -> {
            if (clientes.getValue() != null) {
                List<Domicilios> domi = pedidosService.getDomicilioByIdCliente(clientes.getValue().getIdCliente());
                if (domi.isEmpty()) {
                    domicilios.clear();
                    domicilios.setItems();
                    n = Notification.show(
                            "No se encontraron Domicilios para el Cliente seleccionado",
                            3000, Position.MIDDLE);
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    domicilios.setItems(domi);
                    domicilios.setItemLabelGenerator(domicilios -> domicilios.getCalle() + " " + domicilios.getNumero()
                            + " - " + domicilios.getDepto());
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
        vendedores.addValueChangeListener(event -> {
            if (vendedores.getValue() != null) {
                List<Zonas> zona = pedidosService.getZonasByIdVendedor(vendedores.getValue().getIdVendedor());
                if (zona.isEmpty()) {
                    zonas.clear();
                    zonas.setItems();
                    n = Notification.show(
                            "No se encontraron Zonas para el Vendedor seleccionado",
                            3000, Position.MIDDLE);
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    zonas.setItems(zona);
                    zonas.setItemLabelGenerator(zonas -> zonas.getDescripcion());
                }
            } else {
                zonas.clear();
                zonas.setItems();
            }
        });

        zonas = new ComboBox<>("Zona");
        zonas.setWidth(25, Unit.PERCENTAGE);
        zonas.setPlaceholder("Buscar Zona");
        zonas.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        zonas.setItems(pedidosService.getAllZonas());
        zonas.setItemLabelGenerator(Zonas::getDescripcion);

        domicilios = new ComboBox<>("Domicilio de Envío");
        domicilios.setPlaceholder("Selecione Domicilio");

        bonificacionRecargoListas = new RadioButtonGroup<>();
        // opcionPedidos.setWidth(20, Unit.PERCENTAGE);
        bonificacionRecargoListas.setItems("Ninguno", "Bonificación(%)", "Recargo(%)", "Aplicar Listas");

        bonificacionRecListField = new BigDecimalField();
        bonificacionRecListField.setEnabled(false);
        bonificacionRecListField.addBlurListener(event -> {
            if (bonificacionRecListField.getValue() != null) {
                if (esBonificacionPedido.getValue()) {
                    pedidos.setBonificacion(bonificacionRecListField.getValue());
                    pedidos.setRecargo(BigDecimal.ZERO);
                    pedidos.setEsBonificacion(true);
                    pedidos.setEsRecargo(false);
                } else if (esRecargoPedido.getValue()) {
                    pedidos.setRecargo(bonificacionRecListField.getValue());
                    pedidos.setBonificacion(BigDecimal.ZERO);
                    pedidos.setEsRecargo(true);
                    pedidos.setEsBonificacion(false);
                } else {
                    pedidos.setBonificacion(BigDecimal.ZERO);
                    pedidos.setRecargo(BigDecimal.ZERO);
                }
            } else {
                bonificacionRecListField.setValue(BigDecimal.ZERO);
            }
        });

        esBonificacionPedido = new Checkbox();
        esBonificacionPedido.setValue(false);
        esBonificacionPedido.setVisible(false);

        esRecargoPedido = new Checkbox();
        esRecargoPedido.setVisible(false);
        esRecargoPedido.setValue(false);

        bonificacionRecargoListas.addValueChangeListener(event -> {
            String selectedOption = event.getValue();
            if ("Bonificación(%)".equals(selectedOption)) {
                esBonificacionPedido.setValue(true);
                esRecargoPedido.setValue(false);
                bonificacionRecListField.setEnabled(true);
                bonificacionRecListField.focus();
                listas.setEnabled(false);
            } else if ("Recargo(%)".equals(selectedOption)) {
                esRecargoPedido.setValue(true);
                esBonificacionPedido.setValue(false);
                bonificacionRecListField.setEnabled(true);
                bonificacionRecListField.focus();
                listas.setEnabled(false);
            } else if ("Aplicar Listas".equals(selectedOption)) {
                esBonificacionPedido.setValue(false);
                esRecargoPedido.setValue(false);
                bonificacionRecListField.setEnabled(false);
                listas.setEnabled(true);
                listas.focus();
            } else if ("Ninguno".equals(selectedOption)) {
                esBonificacionPedido.setValue(false);
                esRecargoPedido.setValue(false);
                bonificacionRecListField.setEnabled(false);
                listas.setEnabled(false);
                bonificacionRecListField.setValue(BigDecimal.ZERO);
            }
        });

        listas = new ComboBox<>();
        listas.setEnabled(false);
        listas.setPlaceholder("Seleccione Lista");
        listas.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        listas.setItems(pedidosService.getAllListas());
        listas.setItemLabelGenerator(Listas::getDescripcion);

        articulos = new ComboBox<>("Articulos");
        articulos.setPlaceholder("Buscar Articulo");
        articulos.setItems(pedidosService.getAllArticulos());
        articulos.setItemLabelGenerator(Articulos::getDescripcion);
        articulos.addValueChangeListener(event -> {
            if (articulos.getValue() != null && articulos.getValue().getIdMedida() != null) {
                medidas.setValue(articulos.getValue().getIdMedida().getDescripcion());
            } else {
                medidas.setValue("");
            }
        });

        medidas = new TextField("Unidades de Medida");
        medidas.setReadOnly(true);

        cantidadField = new IntegerField("Cantidad");
        cantidadField.setValue(1);
        cantidadField.setStepButtonsVisible(true);
        esBonificacion = new Checkbox();
        esBonificacion.setValue(false);
        esBonificacion.setVisible(false);

        esRecargo = new Checkbox();
        esRecargo.setVisible(false);
        esRecargo.setValue(false);

        bonificacionRecargoArticulo = new RadioButtonGroup<>();
        bonificacionRecargoArticulo.setValue("Ninguno"); // Allow no selection
        // opcionPedidos.setWidth(20, Unit.PERCENTAGE);
        bonificacionRecargoArticulo.setItems("Ninguno", "Bonificar(%)", "Recargar(%)");
        // bonificacionRecargoListas.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        bonificacion = new BigDecimalField();
        bonificacion.setVisible(false);
        bonificacion.setValue(BigDecimal.ZERO);
        recargo = new BigDecimalField();
        recargo.setVisible(false);
        recargo.setValue(BigDecimal.ZERO);
        bonificacionRecArtField = new BigDecimalField();
        bonificacionRecArtField.setEnabled(false);
        bonificacionRecArtField.setValue(BigDecimal.ZERO);
        bonificacionRecArtField.addBlurListener(event -> {
            if (esBonificacion.getValue()) {
                if (bonificacionRecArtField.getValue() != null) {
                    bonificacion.setValue(bonificacionRecArtField.getValue());
                    recargo.setValue(BigDecimal.ZERO);
                } else {
                    bonificacion.setValue(BigDecimal.ZERO);
                    recargo.setValue(BigDecimal.ZERO);
                }
            } else if (esRecargo.getValue()) {
                if (bonificacionRecArtField.getValue() != null) {
                    recargo.setValue(bonificacionRecArtField.getValue());
                    bonificacion.setValue(BigDecimal.ZERO);
                } else {
                    recargo.setValue(BigDecimal.ZERO);
                    bonificacion.setValue(BigDecimal.ZERO);
                }
            }
        });
        bonificacionRecargoArticulo.addValueChangeListener(event -> {
            String selectedOption = event.getValue();

            if ("Bonificar(%)".equals(selectedOption)) {
                esBonificacion.setValue(true);
                esRecargo.setValue(false);
                bonificacionRecArtField.setEnabled(true);
                bonificacionRecArtField.focus();
                persistrBonRec.setEnabled(true);
            } else if ("Recargar(%)".equals(selectedOption)) {
                esRecargo.setValue(true);
                esBonificacion.setValue(false);
                bonificacionRecArtField.setEnabled(true);
                bonificacionRecArtField.focus();
                persistrBonRec.setEnabled(false);
            } else if ("Ninguno".equals(selectedOption)) {
                esBonificacion.setValue(false);
                esRecargo.setValue(false);
                bonificacionRecArtField.setEnabled(false);
                persistrBonRec.setEnabled(false);
                recargo.setValue(BigDecimal.ZERO);
                bonificacion.setValue(BigDecimal.ZERO);
            }
        });

        persistrBonRec = new Checkbox("Persistir Bonificación en Articulo");
        persistrBonRec.addValueChangeListener(event -> {
            // agregar acciones
        });
        persistrBonRec.setEnabled(false);

        agregarPedido = new Button("Agregar al Pedido", e -> {
            if (pedidosItems == null) {
                pedidosItems = new PedidosItems();
            }

            try {
                binderPedidosItems.writeBean(pedidosItems);
            } catch (ValidationException ex) {
                Notification.show("Error al validar los datos: " + ex.getMessage(), 3000, Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (pedidosItems.isEsBonificacion()) {
                boolean isError = validarBonificacionRecargoArticulo(pedidosItems.getBonificacion(),
                        "Bonificar(%)");
                if (isError) {
                    return;
                }
            } else if (pedidosItems.isEsRecargo()) {
                boolean isError = validarBonificacionRecargoArticulo(pedidosItems.getRecargo(), "Recargar(%)");
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
            pedItem.setPrecioArticulo(pedidosItems.getIdArticulo().getPrecio_costo());

            // Asegurarse de que no se sobrescriban los elementos existentes
            pedidosItemsList.add(pedItem);

            // Actualizar el grid con una nueva lista para evitar referencias duplicadas
            gridPedidosItems.setItems(new ArrayList<>(pedidosItemsList));
            gridPedidosItems.recalculateColumnWidths();
            binderPedidosItems.setTopic("pedidosItems/" + UUID.randomUUID(), () -> null);
            // binderPedidosItems.setTopic("pedidosItems/", () -> null);
            bonificacionRecargoArticulo.setValue("Ninguno");
            bonificacionRecArtField.setValue(BigDecimal.ZERO);
            // cleanForm();
            // } else {
            // Notification n = Notification.show(
            // "Debe seleccionar un Articulo y una Cantidad",
            // 3000, Position.MIDDLE);
            // n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            // }
        });
        agregarPedido.setHeight(100, Unit.PERCENTAGE);
        gridPedidosItems.setSelectionMode(Grid.SelectionMode.MULTI);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getIdArticulo().getDescripcion())
                .setHeader("Articulos").setAutoWidth(true);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getCantidad())
                .setHeader("Cantidad").setAutoWidth(true);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getIdArticulo().getIdAlicuota().getDescripcion())
                .setHeader("Alicuota").setAutoWidth(true);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getPrecioArticulo())
                .setHeader("Precio Unitario").setAutoWidth(true);
        gridPedidosItems
                .addColumn(pedidosItems -> pedidosItems.getBonificacion() != null ? pedidosItems.getBonificacion()
                        : BigDecimal.ZERO)
                .setHeader("Bon.(%)").setAutoWidth(true);
        gridPedidosItems
                .addColumn(
                        pedidosItems -> pedidosItems.getRecargo() != null ? pedidosItems.getRecargo() : BigDecimal.ZERO)
                .setHeader("Rec.(%)").setAutoWidth(true);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getIdArticulo().getPrecio_costo()
                .multiply(BigDecimal.valueOf(pedidosItems.getCantidad().intValue())))
                .setHeader("Sub total-Imp").setAutoWidth(true);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getIdArticulo().getPrecio_costo()
                .multiply(BigDecimal.valueOf(pedidosItems.getCantidad().intValue())))
                .setHeader("Sub total+Imp").setAutoWidth(true);
        gridPedidosItems.setItems(pedidosItemsList);

        gridPedidosItems.addSelectionListener(event -> {
            pedidosItemsSelected.clear();
            pedidosItemsSelected.addAll(event.getAllSelectedItems());
            rolbackItem.setEnabled(!pedidosItemsSelected.isEmpty());

        });

        notaAlPie = new TextArea("Nota al Pie");
        notaAlPie.setWidthFull();
        notaAlPie.setHeight(90, Unit.PIXELS);
        bonificacionField = new BigDecimalField();
        bonificacionField.setWidthFull();
        subTotalSinImpuestos = new BigDecimalField();
        subTotalSinImpuestos.setWidthFull();
        subTotalConIMpuestos = new BigDecimalField();
        subTotalConIMpuestos.setWidthFull();
        totalPedido = new BigDecimalField();
        totalPedido.setWidthFull();
        cancelarAcciones = new Button("Cancelar Acciones", e -> {
            // agregar acciones
        });
        rolbackItem = new Button("Rollback Item", e -> {
            if (!pedidosItemsSelected.isEmpty()) {
                pedidosItemsList.removeAll(pedidosItemsSelected);
                gridPedidosItems.setItems(new ArrayList<>(pedidosItemsList));
                gridPedidosItems.recalculateColumnWidths();
                pedidosItemsSelected.clear();
            }
        });
        rolbackItem.setEnabled(false);
        cancelarPedido = new Button("Cancelar Pedido", e -> {
            // agregar acciones
        });
        agregarPedidoBandeja = new Button("Agregar Pedido a Bandeja", e -> {
            // agregar acciones
        });
        Div pedidoForm = new Div();
        pedidoForm.setWidthFull();
        pedidoForm.getStyle().setBorder("2px solid green");
        VerticalLayout pedidoFormLayout = new VerticalLayout();
        pedidoFormLayout.setSpacing(false);
        HorizontalLayout pedFormHorizontalLayout1 = new HorizontalLayout();
        pedFormHorizontalLayout1.setPadding(false);
        pedFormHorizontalLayout1.setFlexGrow(1, fechaPedido, plataforma, pedido,
                estadoPedido, estadoPago);
        pedFormHorizontalLayout1.add(fechaPedido, plataforma, pedido, estadoPedido, estadoPago);
        pedFormHorizontalLayout1.setAlignSelf(FlexComponent.Alignment.CENTER,
                fechaPedido, plataforma, pedido, estadoPedido, estadoPago);
        HorizontalLayout pedFormHorizontalLayout2 = new HorizontalLayout();
        pedFormHorizontalLayout2.setWidthFull();
        pedFormHorizontalLayout2.setPadding(false);
        pedFormHorizontalLayout2.setFlexGrow(1, clientes, domicilios);
        pedFormHorizontalLayout2.add(clientes, domicilios);
        HorizontalLayout pedFormHorizontalLayout3 = new HorizontalLayout();
        pedFormHorizontalLayout3.setWidthFull();
        pedFormHorizontalLayout3.setPadding(false);
        pedFormHorizontalLayout3.setFlexGrow(1, vendedores, zonas);
        pedFormHorizontalLayout3.add(vendedores, zonas);
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
        pedidoItemsFormHorizontalLayout1.add(articulos, medidas, cantidadField);
        pedidoItemsFormHorizontalLayout1.setSpacing(true);
        HorizontalLayout pedidoItemsFormHorizontalLayout2 = new HorizontalLayout();
        pedidoItemsFormHorizontalLayout2.setSpacing(true);
        pedidoItemsFormHorizontalLayout2.add(bonificacionRecargoArticulo, bonificacionRecArtField, persistrBonRec);
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
        dialog.add(principVerticalLayout);
        configurePedidosItemsBinder();
        configurePedidosBinder();
    }

    private void configurePedidosBinder() {
        binder.forField(fechaPedido).asRequired("Fecha es requerido")
                .bind("fechaPedido");
        plataformaCombo = new ComboBox<>();
        plataformaCombo.setItems(PlataformaEnum.values()); // Populate items first
        plataformaCombo.setVisible(false);
        plataformaCombo.setValue(PlataformaEnum.valueOf("WEB"));
        binder.forField(plataformaCombo)
                .bind("plataforma");
        estadoPedidoCombo = new ComboBox<>();
        estadoPedidoCombo.setItems(EstadoPedidoEnum.values()); // Populate items first
        estadoPedidoCombo.setVisible(false);
        estadoPedidoCombo.setValue(EstadoPedidoEnum.valueOf("EN_PICKING"));
        binder.forField(estadoPedidoCombo)
                .bind("estadoPedido");
        binder.forField(clientes).asRequired("Cliente es requerido")
                .bind("idCliente");
        binder.forField(vendedores).asRequired("Vendedor es requerido")
                .bind("idVendedor");
        // binder.setSerializer(Zonas.class,
        // zonas -> String.valueOf(zonas.getIdZona()),
        // idZonas -> (Zonas) pedidosService
        // .findByIdZonas(Integer.parseInt(idZonas)));
        // binder.forField(zonas).asRequired("Zona es requerido")
        // .bind("idZona");

        binder.forField(domicilios).asRequired("Domicilio es requerido")
                .bind("domicilioCliente");
        domicilioField = new TextField();
        domicilioField.setVisible(false);
        Domicilios domicilio = domicilios.getValue() == null ? new Domicilios() : domicilios.getValue();
        domicilioField.setValue(domicilio.getCalle() + " " + domicilio.getNumero()
                + " - " + domicilio.getDepto());
        binder.forField(domicilioField).bind("domicilioClienteString");
        binder.forField(notaAlPie).bind("notaAlPie");

        binder.addStatusChangeListener(
                event -> agregarPedidoBandeja.setEnabled(binder.isValid()));

        binder.bindInstanceFields(this);
    }

    private boolean validarBonificacionRecargoArticulo(BigDecimal value, String string) {
        boolean isError = false;
        if (value == null) {
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

        binderPedidosItems.forField(articulos).asRequired("Articulos es requerido")
                .bind("idArticulo");

        binderPedidosItems.forField(cantidadField, Integer.class)
                .asRequired("La cantidad es obligatoria")
                .withConverter(
                        value -> value != null ? BigDecimal.valueOf(value) : null,
                        value -> value != null ? value.intValue() : null,
                        "Debe ser un número válido")
                .bind("cantidad");
        binderPedidosItems.forField(esBonificacion).bind("esBonificacion");
        binderPedidosItems.forField(esRecargo).bind("esRecargo");
        binderPedidosItems.forField(bonificacion).bind("bonificacion");
        binderPedidosItems.forField(recargo).bind("recargo");

        binderPedidosItems.withValidator(pedidoItem -> {
            pedidoItem.setIdPedido(pedidos == null ? new Pedidos() : pedidos);

            return true;
        }, "Error al asignar el pedido");

        // binderPedidosItems.forField(bonificacionRecargoArticulo)
        // .bind("esBonificacion");
        binderPedidosItems.addStatusChangeListener(
                event -> agregarPedido.setEnabled(binder.isValid()));

        binderPedidosItems.bindInstanceFields(this);
    }

}