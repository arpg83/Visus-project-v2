package com.ideadistribuidora.visus.views.articulos;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;

import com.ideadistribuidora.visus.data.Alicuotas;
import com.ideadistribuidora.visus.data.Articulos;
import com.ideadistribuidora.visus.data.Depositos;
import com.ideadistribuidora.visus.data.Lineas;
import com.ideadistribuidora.visus.data.Medidas;
import com.ideadistribuidora.visus.data.Presentaciones;
import com.ideadistribuidora.visus.data.Proveedores;
import com.ideadistribuidora.visus.data.Rubros;
import com.ideadistribuidora.visus.data.Ubicaciones;
import com.ideadistribuidora.visus.data.enums.EstadoArticuloEnum;
import com.ideadistribuidora.visus.data.enums.TipoArticuloEnum;
import com.ideadistribuidora.visus.services.ArticulosService;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
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

@PageTitle("Articulos")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 6)
@Route(value = "7/:articulosID?/:action?(edit)")
public class ArticulosView extends Div implements BeforeEnterObserver {

        private final String ARTICULOS_ID = "articulosID";
        private final String ARTICULOS_EDIT_ROUTE_TEMPLATE = "7/%s/edit";

        private final Grid<Articulos> grid = new Grid<>(Articulos.class, false);

        CollaborationAvatarGroup avatarGroup;

        private TextField codigoInterno;
        private TextField codigoBarra;
        private TextField descripcion;
        private TextField nroLote;
        private ComboBox<Rubros> rubros;
        private ComboBox<Lineas> linea;
        private ComboBox<Medidas> medida;
        private ComboBox<Presentaciones> presentacion;
        private ComboBox<Depositos> deposito;
        private ComboBox<Ubicaciones> ubicacion;
        private IntegerField fila;
        private IntegerField columna;
        private BigDecimalField precioCosto;
        private BigDecimalField margenUtilidad;
        private Checkbox esBonificado;
        private BigDecimalField bonificacion;
        private DatePicker fechaActPrecios;
        private DatePicker fechaCompra;
        private DatePicker fechaVencimiento;
        private DatePicker fechaBaja;
        private ComboBox<TipoArticuloEnum> tipo;
        private IntegerField stock;
        private IntegerField stockMinimo;
        private IntegerField stockMaximo;
        private ComboBox<EstadoArticuloEnum> estado;
        private ComboBox<Proveedores> proveedor;
        private BigDecimalField ganancia;
        private BigDecimalField precioFinalSinIva;
        private ComboBox<Alicuotas> alicuota;
        private BigDecimalField precioFinalConIva;
        private TextField searchCodigo;
        private TextField searchDescrpcion;
        private TextField searchRubro;
        private TextField searchLinea;
        private TextField searchEstado;

        private final Button cancel = new Button("Cancelar");
        private final Button save = new Button("Grabar");
        private final Button delete = new Button("Eliminar");

        private CollaborationBinder<Articulos> binder;

        private Articulos artic;

        private GridListDataView<Articulos> dataView;

        private final ArticulosService articulosService;

        public ArticulosView(ArticulosService articulosService) {
                this.articulosService = articulosService;
                addClassNames("articulos-view");

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
                splitLayout.setSplitterPosition(36);

                avatarGroup = new CollaborationAvatarGroup(userInfo, null);
                avatarGroup.getStyle().set("visibility", "hidden");

                createGridLayout(splitLayout);
                createEditorLayout(splitLayout);

                add(splitLayout);

                // Configure Grid
                grid.addColumn(createArticulosRenderer())
                                .setHeader("Código Interno").setAutoWidth(true);
                grid.addColumn("descripcion")
                                .setHeader("Descripción").setAutoWidth(true);
                grid.addColumn("tipo")
                                .setHeader("Tipo").setAutoWidth(true);
                grid.addColumn("stock").setAutoWidth(true);
                grid.addColumn("stock_minimo").setHeader("Stock Min.")
                                .setAutoWidth(true);
                grid.addColumn("stock_maximo").setHeader("Stock Máx.")
                                .setAutoWidth(true);
                grid.addComponentColumn(articulos -> {
                        Checkbox checkbox = new Checkbox();
                        checkbox.setValue(articulos.isEs_bonificado());
                        checkbox.setEnabled(false);
                        return checkbox;
                }).setHeader("Bonificado")
                                .setAutoWidth(true);
                grid.addColumn("bonificacion").setHeader("Bonif.(%)").setAutoWidth(true);
                grid.addColumn("precioFinalConIva").setHeader("Precio Final").setAutoWidth(true);
                grid.addColumn(articulos -> articulos.getEstado().getDisplayEstadoArticulo()).setHeader("Estado")
                                .setAutoWidth(true);

                dataView = grid.setItems(articulosService.articulosList());

                searchFilter(dataView);

                grid.addThemeVariants(GridVariant.LUMO_COMPACT);
                // when a row is selected or deselected, populate form
                grid.asSingleSelect().addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                delete.setEnabled(true);
                                UI.getCurrent()
                                                .navigate(String.format(ARTICULOS_EDIT_ROUTE_TEMPLATE,
                                                                event.getValue().getIdArticulo()));

                        } else {

                                clearForm();
                                delete.setEnabled(true);
                                UI.getCurrent().navigate(ArticulosView.class);
                        }
                });

                // Configure Form
                binder = new CollaborationBinder<>(Articulos.class, userInfo);

                // Bind fields. This is where you'd define e.g. validation rules
                binder.forField(codigoInterno).asRequired("Código Interno es Requerido")
                                .bind("codigo_interno");
                binder.forField(codigoBarra).asRequired("Código de Barra es Requerido")
                                .bind("codigo_barra");

                binder.forField(descripcion).asRequired("Descripción es Requerido")
                                .bind("descripcion");
                binder.forField(nroLote).bind("nroLote");
                binder.setSerializer(Rubros.class,
                                rubros -> String.valueOf(rubros.getIdRubro()),
                                idRubros -> articulosService
                                                .findByIdRubros(Integer.parseInt(idRubros)));
                binder.bind(rubros, "rubros");
                binder.setSerializer(Lineas.class,
                                lineas -> String.valueOf(lineas.getIdLineas()),
                                idLineas -> articulosService
                                                .findByIdLinea(Integer.parseInt(idLineas)));
                binder.bind(linea, "idLinea");
                binder.setSerializer(Medidas.class,
                                medidas -> String.valueOf(medidas.getIdMedida()),
                                idMedida -> articulosService
                                                .findByIdMedidas(Integer.parseInt(idMedida)));
                binder.bind(medida, "idMedida");
                binder.setSerializer(Presentaciones.class,
                                presentaciones -> String.valueOf(presentaciones.getIdPresentacion()),
                                idpresentacion -> articulosService
                                                .findByIdPresentaciones(Integer.parseInt(idpresentacion)));
                binder.bind(presentacion, "idPresentacion");
                binder.setSerializer(Depositos.class,
                                depositos -> String.valueOf(depositos.getIdDeposito()),
                                idDeposito -> articulosService
                                                .findByIdDepositos(Integer.parseInt(idDeposito)));
                binder.bind(deposito, "deposito");
                binder.setSerializer(Ubicaciones.class,
                                ubicaciones -> String.valueOf(ubicaciones.getIdubicacion()),
                                idubicacion -> articulosService
                                                .findByIdUbicaciones(Integer.parseInt(idubicacion)));
                binder.bind(ubicacion, "idUbicacion");
                binder.forField(fila).asRequired("Fila es Requerido")
                                .bind("fila");
                binder.forField(columna).asRequired("Columna es Requerido")
                                .bind("columna");
                binder.setSerializer(Proveedores.class,
                                proveedores -> String.valueOf(proveedores.getIdProveedor()),
                                idProveedor -> articulosService
                                                .findByIdProveedores(Integer.parseInt(idProveedor)));
                binder.bind(proveedor, "idProveedor");
                binder.forField(tipo)
                                .asRequired("Tipo es Requerido")
                                .bind("tipo");
                binder.forField(stock).asRequired("Stock es Requerido")
                                .bind("stock");
                binder.forField(stockMinimo).asRequired("Stock Minimo es Requerido")
                                .bind("stock_minimo");
                binder.forField(stockMaximo).asRequired("Stock Maximo es Requerido")
                                .bind("stock_maximo");
                binder.forField(precioCosto).asRequired("Precio Costo es Requerido")
                                .bind("precio_costo");
                binder.forField(margenUtilidad).asRequired("Margen Utilidad es Requerido")
                                .bind("margen_utilidad");
                binder.forField(ganancia)
                                .bind("ganancia");
                binder.forField(precioFinalSinIva).bind("precioFinalSinIva");
                binder.setSerializer(Alicuotas.class,
                                alicuotas -> String.valueOf(alicuotas.getIdAlicuota()),
                                idAlicuota -> articulosService
                                                .findByIdAlicuotas(Integer.parseInt(idAlicuota)));
                binder.bind(alicuota, "idAlicuota");

                binder.forField(bonificacion).bind("bonificacion");
                binder.forField(esBonificado).bind("es_bonificado");
                binder.forField(precioFinalConIva).bind("precioFinalConIva");
                binder.forField(fechaActPrecios).asRequired("Fecha de Última Actualización de Precios es Requerido")
                                .bind("fecha_actPrecios");
                binder.forField(fechaCompra).asRequired("Fecha ültima Compra es Requerido")
                                .bind("fecha_compra");
                binder.forField(fechaVencimiento).bind("fecha_vencimiento");
                binder.forField(fechaBaja).bind("fecha_baja");
                binder.forField(estado).asRequired("Estado del Articulo es Requerido")
                                .bind("estado");

                binder.addStatusChangeListener(
                                event -> save.setEnabled(binder.isValid()));

                binder.bindInstanceFields(this);

                cancel.addClickListener(e -> {
                        clearForm();
                        refreshGrid();
                });

                save.addClickListener(e -> {
                        try {
                                if (this.artic == null) {
                                        this.artic = new Articulos();
                                }

                                binder.writeBean(this.artic);
                                articulosService.update(this.artic);
                                clearForm();
                                refreshGrid();
                                Notification.show("Datos Guardados");
                                UI.getCurrent().navigate(ArticulosView.class);
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
                                if (this.artic == null) {
                                        this.artic = new Articulos();
                                }
                                binder.writeBean(this.artic);
                                articulosService.delete(this.artic.getIdArticulo());
                                clearForm();
                                refreshGrid();
                                Notification.show("Datos Eliminados").setPosition(Position.TOP_CENTER);
                                UI.getCurrent().navigate(ArticulosView.class);
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

        private void searchFilter(GridListDataView<Articulos> dataView2) {
                dataView2.addFilter(articSearh -> {
                        String searchTerm = searchCodigo.getValue().trim();
                        String searchTerm2 = searchDescrpcion.getValue().trim();
                        String searchTerm3 = searchRubro.getValue().trim();
                        String searchTerm4 = searchLinea.getValue().trim();
                        String searchTerm5 = searchEstado.getValue().trim();

                        if (searchTerm.isEmpty() && searchTerm2.isEmpty() && searchTerm3.isEmpty()
                                        && searchTerm4.isEmpty() && searchTerm5.isEmpty())
                                return true;

                        boolean matchesFullName = matchesTerm(articSearh.getCodigo_interno(),
                                        searchTerm);
                        boolean matchesDescripcion = matchesTerm(articSearh.getDescripcion(), searchTerm2);
                        boolean matchesRubros = matchesTerm(articSearh.getIdLinea().getRubros().getDescripcion(),
                                        searchTerm3);
                        boolean matchesLinea = matchesTerm(articSearh.getIdLinea().getDescripcion(), searchTerm4);
                        boolean matchesEstado = matchesTerm(articSearh.getEstado().getDisplayEstadoArticulo(), searchTerm5);

                        return matchesFullName && matchesDescripcion && matchesRubros && matchesLinea && matchesEstado;
                });
        }

        private LitRenderer<Articulos> createArticulosRenderer() {
                return LitRenderer.<Articulos>of(
                                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                                + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                                                + "  <span> ${item.fullName} </span>"
                                                + "</vaadin-horizontal-layout>")

                                .withProperty("fullName", Articulos::getCodigo_interno);
        }

        private boolean matchesTerm(String value, String searchTerm) {
                return searchTerm == null || searchTerm.isEmpty()
                                || value.toLowerCase().contains(searchTerm.toLowerCase());
        }

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
                Optional<Integer> articulosId = event.getRouteParameters().get(ARTICULOS_ID).map(Integer::parseInt);
                if (articulosId.isPresent()) {
                        Optional<Articulos> articulosFromBackend = articulosService.get(articulosId.get());
                        if (articulosFromBackend.isPresent()) {
                                populateForm(articulosFromBackend.get());
                        } else {
                                Notification.show(
                                                String.format("The requested articulos was not found, ID = %d",
                                                                articulosId.get()),
                                                3000, Notification.Position.BOTTOM_START);
                                // when a row is selected but the data is no longer available,
                                // refresh grid
                                refreshGrid();
                                event.forwardTo(ArticulosView.class);
                        }
                } else {
                        clearForm();
                }
        }

        private void createEditorLayout(SplitLayout splitLayout) {
                save.setEnabled(false);
                delete.setEnabled(false);
                // FormLayout formLayout = new FormLayout();
                codigoInterno = new TextField("Código de Interno");
                codigoInterno.setWidth("12.5%");
                codigoInterno.setMaxLength(13);
                codigoBarra = new TextField("Código de Barra");
                codigoBarra.setWidth("12.5%");
                codigoBarra.setMaxLength(13);
                descripcion = new TextField("Descripción");
                descripcion.setWidth("12.5%");
                descripcion.setMaxLength(60);
                nroLote = new TextField("Número de Lote");
                nroLote.setMaxLength(20);
                nroLote.setWidth("12.5%");
                rubros = new ComboBox<>("Rubros");
                rubros.setWidth("12.5%");
                rubros.setPlaceholder("Seleccione Rubros");
                rubros.setItems(articulosService.getAllRubros());
                rubros.setItemLabelGenerator(Rubros::getDescripcion);
                rubros.setRequired(true);
                rubros.setRequiredIndicatorVisible(true);
                rubros.addBlurListener(e -> {
                        Rubros selectedRubros = rubros.getValue();
                        if (selectedRubros != null) {
                                linea.setItems(articulosService.findLineasByRubros(selectedRubros));
                        } else {
                                linea.clear();
                                linea.setItems();
                        }
                });
                linea = new ComboBox<>("Linea");
                linea.setWidth("12.5%");
                linea.setPlaceholder("Seleccione Linea");
                linea.setItems(articulosService.getAllLineas());
                linea.setItemLabelGenerator(Lineas::getDescripcion);
                linea.setRequired(true);
                linea.setRequiredIndicatorVisible(true);
                medida = new ComboBox<>("Medida");
                medida.setWidth("12.5%");
                medida.setPlaceholder("Seleccione Medida");
                medida.setItems(articulosService.getAllMedidas());
                medida.setItemLabelGenerator(Medidas::getDescripcion);
                medida.setRequired(true);
                medida.setRequiredIndicatorVisible(true);
                presentacion = new ComboBox<>("Presentaciones");
                presentacion.setWidth("12.5%");
                presentacion.setPlaceholder("Seleccione Presentaciones");
                presentacion.setItems(articulosService.getAllPresentaciones());
                presentacion.setItemLabelGenerator(Presentaciones::getDescripcion);
                presentacion.setRequired(true);
                presentacion.setRequiredIndicatorVisible(true);
                deposito = new ComboBox<>("Depósito");
                deposito.setPlaceholder("Seleccione Depósito");
                deposito.setItems(articulosService.getAllDepositos());
                deposito.setItemLabelGenerator(Depositos::getDescripcion);
                deposito.setRequired(true);
                deposito.setRequiredIndicatorVisible(true);
                deposito.addBlurListener(event -> {
                        Depositos selectedDepositos = deposito.getValue();
                        if (selectedDepositos != null) {
                                ubicacion.setItems(articulosService.findUbicacionByDeposito(selectedDepositos));
                        } else {
                                ubicacion.clear();
                                ubicacion.setItems();
                        }
                });
                ubicacion = new ComboBox<>("Ubicación");
                ubicacion.setPlaceholder("Seleccione Ubicación");
                ubicacion.setItems(articulosService.getAllUbicacion());
                ubicacion.setItemLabelGenerator(Ubicaciones::getDescripcion);
                ubicacion.setRequired(true);
                ubicacion.setRequiredIndicatorVisible(true);
                fila = new IntegerField("Fila");
                columna = new IntegerField("Columna");
                proveedor = new ComboBox<>("Proveedor");
                proveedor.setPlaceholder("Seleccione Provedor");
                proveedor.setItems(articulosService.getAllProveedores());
                proveedor.setItemLabelGenerator(Proveedores::getNombreReal);
                proveedor.setRequired(true);
                proveedor.setRequiredIndicatorVisible(true);
                proveedor.getStyle().setWidth("100%");
                tipo = new ComboBox<>("Tipo de Articulo");
                tipo.setPlaceholder("Seleccione Tipo de Articulo");
                tipo.setItems(articulosService.getAllTipoArticulo());
                tipo.setItemLabelGenerator(TipoArticuloEnum::getDisplayTipoArticulo);
                stock = new IntegerField("Stock");
                stockMinimo = new IntegerField("Stock Minimo");
                stockMaximo = new IntegerField("Stock Maximo");
                precioCosto = new BigDecimalField("Precio Costo");
                precioCosto.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
                precioCosto.setValue(BigDecimal.ZERO);
                precioCosto.addBlurListener(e -> {
                        ComponentUtils.getRoundedValue(precioCosto);
                        BigDecimal precCost = precioCosto.getValue();
                        BigDecimal mUtilidad = margenUtilidad.getValue();
                        if (precCost != null && mUtilidad != null) {
                                if (precCost.compareTo(BigDecimal.ZERO) > 0
                                                && mUtilidad.compareTo(BigDecimal.ZERO) > 0) {
                                        BigDecimal res = precCost.multiply(mUtilidad).divide(BigDecimal.valueOf(100));
                                        ganancia.setValue(res);
                                        precioFinalSinIva.setValue(precCost.add(res));
                                }
                        }

                });
                margenUtilidad = new BigDecimalField("Margen/Utilidad");
                margenUtilidad.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
                margenUtilidad.setSuffixComponent(new Span("%"));
                margenUtilidad.setValue(BigDecimal.ZERO);
                margenUtilidad.addBlurListener(e -> {
                        ComponentUtils.getRoundedValue(margenUtilidad);
                        BigDecimal precCost = precioCosto.getValue();
                        BigDecimal mUtilidad = margenUtilidad.getValue();
                        if (precCost != null && mUtilidad != null) {
                                if (precCost.compareTo(BigDecimal.ZERO) > 0
                                                && mUtilidad.compareTo(BigDecimal.ZERO) > 0) {
                                        BigDecimal res = precCost.multiply(mUtilidad).divide(BigDecimal.valueOf(100));
                                        ganancia.setValue(res);
                                        precioFinalSinIva.setValue(precCost.add(res));
                                }
                        }

                });
                ganancia = new BigDecimalField("Ganancia");
                ganancia.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
                ganancia.setReadOnly(true);
                // ganancia.setValue(gananciaValue);
                precioFinalSinIva = new BigDecimalField("Precio Final Sin IVA");
                precioFinalSinIva.getStyle().setWidth("33%");
                precioFinalSinIva.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
                precioFinalSinIva.setReadOnly(true);
                ComponentUtils.getRoundedValue(precioFinalSinIva);
                alicuota = new ComboBox<>("Alicuota");
                alicuota.setPlaceholder("Seleccione Alicuota");
                alicuota.setItems(articulosService.getAllAlicuotas());
                alicuota.setItemLabelGenerator(Alicuotas::getDescripcion);
                alicuota.setRequired(true);
                alicuota.setRequiredIndicatorVisible(true);
                alicuota.addBlurListener(e -> {

                        BigDecimal alic = BigDecimal.ZERO;
                        BigDecimal preFinSinIv = precioFinalSinIva.getValue();
                        if (alicuota.getValue() != null && preFinSinIv != null) {
                                Alicuotas alicuotas = alicuota.getValue();
                                alic = alicuotas.getDescripcion().contains("%")
                                                ? BigDecimal.valueOf(Double
                                                                .parseDouble(alicuotas.getDescripcion().replace("%",
                                                                                "")))
                                                : BigDecimal.ZERO;
                                if (alic.compareTo(BigDecimal.ZERO) > 0 && preFinSinIv.compareTo(BigDecimal.ZERO) > 0) {
                                        BigDecimal resConIva = precioFinalSinIva.getValue().multiply(alic)
                                                        .divide(BigDecimal.valueOf(100));
                                        precioFinalConIva.setValue(preFinSinIv.add(resConIva));
                                } else {
                                        precioFinalConIva.setValue(BigDecimal.ZERO);
                                }
                        }
                });

                fechaActPrecios = new DatePicker("Fecha de última Actualización de Precios");
                fechaActPrecios.setPlaceholder("dd/mm/aaaa");
                fechaActPrecios.setI18n(ComponentUtils.getI18n());
                fechaCompra = new DatePicker("Fecha última Compra");
                fechaCompra.setPlaceholder("dd/mm/aaaa");
                fechaCompra.setI18n(ComponentUtils.getI18n());
                fechaVencimiento = new DatePicker("Fecha de Vencimiento");
                fechaVencimiento.setPlaceholder("dd/mm/aaaa");
                fechaVencimiento.setI18n(ComponentUtils.getI18n());
                fechaBaja = new DatePicker("Fecha de Baja");
                fechaBaja.setPlaceholder("dd/mm/aaaa");
                fechaBaja.setI18n(ComponentUtils.getI18n());
                esBonificado = new Checkbox("Bonificado");
                esBonificado.setValue(false);
                esBonificado.addValueChangeListener(e -> {
                        if (e.getValue()) {
                                bonificacion.setEnabled(true);
                        } else {
                                bonificacion.setEnabled(false);
                        }
                });
                bonificacion = new BigDecimalField("Porcentaje Bonificado");
                bonificacion.setSuffixComponent(new Span("%"));
                bonificacion.getStyle().setWidth("100%");
                bonificacion.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
                bonificacion.addBlurListener(e -> {
                        ComponentUtils.getRoundedValue(bonificacion);
                        BigDecimal precFinSinIva = precioFinalSinIva.getValue();
                        if (bonificacion.getValue() != null && bonificacion.getValue().compareTo(BigDecimal.ZERO) > 0 &&
                                        precioFinalSinIva != null) {
                                precioFinalSinIva.setValue(precFinSinIva.subtract(
                                                precFinSinIva.multiply(bonificacion.getValue())
                                                                .divide(BigDecimal.valueOf(100))));
                        }
                });
                precioFinalConIva = new BigDecimalField("Precio Final Con IVA");
                precioFinalConIva.getStyle().setWidth("32%");
                precioFinalConIva.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
                precioFinalConIva.setReadOnly(true);
                precioFinalConIva.setValue(BigDecimal.ZERO);
                precioFinalConIva.addValueChangeListener(e -> {
                        ComponentUtils.getRoundedValue(precioFinalConIva);
                });
                estado = new ComboBox<>("Estado del Articulo");
                estado.setPlaceholder("Seleccione Estado del Articulo");
                estado.setItems(EstadoArticuloEnum.values());
                estado.setItemLabelGenerator(EstadoArticuloEnum::getDisplayEstadoArticulo);
                estado.getStyle().setWidth("50%");
                VerticalLayout datGenArt = new VerticalLayout();
                datGenArt.setId("datGenArt");
                HorizontalLayout dataArtic = new HorizontalLayout();
                dataArtic.setClassName("horizontal-layout");
                dataArtic.add(codigoInterno, codigoBarra, descripcion, nroLote, rubros, linea, medida, presentacion);
                HorizontalLayout dataArtic2 = new HorizontalLayout();
                dataArtic2.setClassName("horizontal-layout");
                dataArtic2.setSpacing(false);
                H4 ubicacionTitle = new H4("En Relación con la Ubicación Física");
                ubicacionTitle.getStyle().setColor("blue");
                H4 preciosTitle = new H4("En Relación con los Precios");
                preciosTitle.getStyle().setColor("blue");
                VerticalLayout ubicPrecios = new VerticalLayout();
                HorizontalLayout ubicHor = new HorizontalLayout();
                ubicHor.add(deposito, ubicacion, fila, columna);
                HorizontalLayout preciosHor1 = new HorizontalLayout();
                preciosHor1.add(precioCosto, margenUtilidad, ganancia, alicuota);
                preciosHor1.setClassName("horizontal-layout");
                preciosHor1.setFlexGrow(1, precioCosto, margenUtilidad, ganancia, alicuota);
                HorizontalLayout preciosHor2 = new HorizontalLayout();
                VerticalLayout bonif = new VerticalLayout();
                bonif.add(esBonificado, bonificacion);
                bonif.setSpacing(false);
                bonif.getStyle().setWidth("34%");
                preciosHor2.add(bonif, precioFinalSinIva, precioFinalConIva);
                preciosHor2.setClassName("horizontal-layout");
                ubicPrecios.add(ubicacionTitle, ubicHor, preciosTitle, preciosHor1, preciosHor2);
                VerticalLayout provFech = new VerticalLayout();
                H4 proveedorTitle = new H4("En Relación con el Proveedor");
                proveedorTitle.getStyle().setColor("blue");
                H4 fechasTitle = new H4("En Relación con las fechas");
                fechasTitle.getStyle().setColor("blue");
                HorizontalLayout fechHor1 = new HorizontalLayout();
                fechHor1.add(fechaActPrecios, fechaCompra);
                HorizontalLayout fechHor2 = new HorizontalLayout();
                fechHor2.add(fechaVencimiento, fechaBaja);
                provFech.add(proveedorTitle, proveedor, fechasTitle, fechHor1, fechHor2);
                VerticalLayout movStock = new VerticalLayout();
                H4 stockTitle = new H4("En Relación con los Movimientos de Stock");
                stockTitle.getStyle().setColor("blue");
                HorizontalLayout movStockHor = new HorizontalLayout();
                movStockHor.add(tipo, stock, stockMinimo, stockMaximo);
                HorizontalLayout movStockHor2 = new HorizontalLayout();
                movStockHor2.setClassName("horizontal-border-layout");
                movStockHor2.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
                movStockHor2.setAlignItems(FlexComponent.Alignment.CENTER);
                movStockHor2.add(estado);
                movStock.add(stockTitle, movStockHor, movStockHor2);
                dataArtic2.add(ubicPrecios, provFech, movStock);
                datGenArt.add(avatarGroup, dataArtic, dataArtic2);
                createButtonLayout(datGenArt);
                // formLayout.add(datGenArt);

                splitLayout.addToSecondary(datGenArt);
        }

        private void limitsBigdecimal(BigDecimalField bigDecimalField) {
                String value = bigDecimalField.getValue() != null ? bigDecimalField.getValue().toPlainString() : "0";
                // Permitir solo números, comas, puntos y signo negativo
                value = value.replaceAll("[^0-9.,-]", ""); // Filtrar los caracteres no permitidos
                bigDecimalField.setValue(new BigDecimal(value.isEmpty() ? "0" : value));
        }

        private void createButtonLayout(VerticalLayout datGenArt) {
                HorizontalLayout buttonLayout = new HorizontalLayout();

                buttonLayout.setClassName("button-layout");
                cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
                buttonLayout.add(save, cancel, delete);
                buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
                datGenArt.add(buttonLayout);
        }

        private void createGridLayout(SplitLayout splitLayout) {
                HorizontalLayout searchHorizontalLayout = new HorizontalLayout();
                createHorizontalSearchLayout(searchHorizontalLayout);
                Div wrapper = new Div();
                wrapper.setClassName("grid-wrapper");
                splitLayout.addToPrimary(wrapper);
                wrapper.add(searchHorizontalLayout, grid);
        }

        private void createHorizontalSearchLayout(HorizontalLayout searchHorizontalLayout) {
                searchCodigo = new TextField();
                searchCodigo.setPlaceholder("Buscar por Código Interno");
                searchCodigo.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                searchCodigo.setValueChangeMode(ValueChangeMode.EAGER);
                searchCodigo.addValueChangeListener(e -> dataView.refreshAll());
                searchCodigo.setWidth("20%");
                searchDescrpcion = new TextField();
                searchDescrpcion.setPlaceholder("Buscar por Descripcion");
                searchDescrpcion.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                searchDescrpcion.setValueChangeMode(ValueChangeMode.EAGER);
                searchDescrpcion.addValueChangeListener(e -> dataView.refreshAll());
                searchDescrpcion.setWidth("20%");
                searchRubro = new TextField();
                searchRubro.setPlaceholder("Buscar por Rubro");
                searchRubro.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                searchRubro.setValueChangeMode(ValueChangeMode.EAGER);
                searchRubro.addValueChangeListener(e -> dataView.refreshAll());
                searchRubro.setWidth("20%");
                searchLinea = new TextField();
                searchLinea.setPlaceholder("Buscar por Linea");
                searchLinea.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                searchLinea.setValueChangeMode(ValueChangeMode.EAGER);
                searchLinea.addValueChangeListener(e -> dataView.refreshAll());
                searchLinea.setWidth("20%");
                searchEstado = new TextField();
                searchEstado.setPlaceholder("Buscar por Estado");
                searchEstado.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
                searchEstado.setValueChangeMode(ValueChangeMode.EAGER);
                searchEstado.addValueChangeListener(e -> dataView.refreshAll());
                searchEstado.setWidth("20%");

                searchHorizontalLayout.add(searchCodigo, searchDescrpcion, searchRubro, searchLinea, searchEstado);
        }

        private void refreshGrid() {
                grid.select(null);
                grid.setItems(articulosService.articulosList());
        }

        private void clearForm() {
                populateForm(null);
        }

        private void populateForm(Articulos value) {
                this.artic = value;
                String topic = null;
                if (this.artic != null) {
                        topic = "articulos/" + this.artic.getIdArticulo();
                        avatarGroup.getStyle().set("visibility", "visible");
                } else {
                        avatarGroup.getStyle().set("visibility", "hidden");
                }
                binder.setTopic(topic, () -> this.artic);
                avatarGroup.setTopic(topic);

        }

}