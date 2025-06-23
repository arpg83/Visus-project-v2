package com.ideadistribuidora.visus.views.pedidos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.ideadistribuidora.visus.data.ListasPorcentuales;
import com.ideadistribuidora.visus.data.Pedidos;
import com.ideadistribuidora.visus.data.PedidosItems;
import com.ideadistribuidora.visus.data.PedidosListas;
import com.ideadistribuidora.visus.services.PedidosService;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class PedidosDetailsFormLayout extends FormLayout {
    private DatePicker fechaPedido = new DatePicker("fecha");
    private TextField plataforma = new TextField("Plataforma");
    private IntegerField pedidodField = new IntegerField("Pedido");
    private TextField estadoPedido = new TextField("Estado del Pedido");
    private TextField estadoPago = new TextField("Pago");
    private TextField cliente = new TextField("Cliente");
    private TextField vendedor = new TextField("Vendedor");
    private TextField domicilioClienteString = new TextField("Domicilio del Envío");
    private Grid<PedidosItems> gridPedidosItems = new Grid<>(PedidosItems.class, false);
    private RadioButtonGroup <String> bonificacionRecargoListas = new RadioButtonGroup<>();
    private BigDecimalField  bonificacionRecListField = new BigDecimalField("Bonificación / Recargo");
    private TextField pedidosListasField = new TextField("Listas");
    private TextArea notaAlPie = new TextArea("Nota al Pie");
    private BigDecimalField bonificacionField = new BigDecimalField("Bonificación");
    private BigDecimalField subTotalSinImpuestos = new BigDecimalField("SubTotal-Impuestos");
    private BigDecimalField subTotalConIMpuestos = new BigDecimalField("SubTotal+Impuestos");
    private BigDecimalField totalPedido = new BigDecimalField("TOTAL PEDIDO");
    private PedidosService pedidosService;

    public PedidosDetailsFormLayout(PedidosService pedidosService){
        this.pedidosService = pedidosService;
        fechaPedido.setReadOnly(true);
        plataforma.setReadOnly(true);
        pedidodField.setReadOnly(true);
        estadoPedido.setReadOnly(true);
        estadoPago.setReadOnly(true);
        cliente.setReadOnly(true);
        vendedor.setReadOnly(true);
        domicilioClienteString.setReadOnly(true);
        bonificacionRecargoListas.setReadOnly(true);
        bonificacionRecListField.setReadOnly(true);
        pedidosListasField.setReadOnly(true);
        add(fechaPedido,plataforma,pedidodField,estadoPedido,estadoPago,cliente,vendedor,domicilioClienteString,bonificacionRecargoListas,bonificacionRecListField,pedidosListasField);

        setResponsiveSteps(new ResponsiveStep("0",5));
        setColspan(cliente, 3);
        setColspan(vendedor, 2);
        setColspan(bonificacionRecargoListas, 3);
        setColspan(bonificacionRecListField, 1);
        setColspan(domicilioClienteString, 5);
        setColspan(pedidosListasField, 1);
        setColspan(notaAlPie, 5);
        getElement().getStyle().set("border", "2px solid var(--lumo-success-color)");
        getElement().getStyle().set("padding", "16px");
        getElement().getStyle().set("border-radius", "8px");
    }

    // public PedidosDetailsFormLayout(PedidosService pedidosService){
    //     this.pedidosService = pedidosService;
    // }

    public void setPedidos(Pedidos pedidos){
        fechaPedido.setValue(pedidos.getFechaPedido());
        plataforma.setValue(pedidos.getPlataforma().getPlataforma());
        pedidodField.setValue(pedidos.getIdPedido());
        estadoPedido.setValue(pedidos.getEstadoPedido().getEstadoPedido());
        estadoPago.setValue(pedidos.getEstadoPago().getEstadoPago());
        cliente.setValue(pedidos.getIdCliente().getNombreCliente());
        domicilioClienteString.setValue(pedidos.getDomicilioClienteString());
        vendedor.setValue(pedidos.getIdVendedor().getNombre());
        List<PedidosListas> pedidosListas = pedidosService.findPedidosListasByIdPedido(pedidos);
        bonificacionRecargoListas.setItems("Ninguno", "Bonificación(%)", "Recargo(%)", "Aplicar Listas");
        if(pedidos.isEsBonificacion()){
            bonificacionRecargoListas.setValue("Bonificación(%)");
            bonificacionRecListField.setValue(pedidos.getBonificacion());
            bonificacionField.setValue(pedidos.getBonificacion());
        }else if(pedidos.isEsRecargo()){
            bonificacionRecargoListas.setValue("Recargo(%)");
            bonificacionRecListField.setValue(pedidos.getRecargo());
            bonificacionField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }else if (pedidosListas != null && !pedidosListas.isEmpty()){
            bonificacionRecargoListas.setValue("Aplicar Listas");
            pedidosListasField.setValue(pedidosListas.get(0).getIdListas().getLista().getDescripcion()+" "+pedidosListas.get(0).getIdListas().getPorcentual().getDescripcion());
            bonificacionField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }else{
           bonificacionRecargoListas.setValue("Ninguno");
           bonificacionField.setValue(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }

        List<PedidosItems> pedidosItemsList = pedidosService.findPedidosItemsByIdPedidos(pedidos);

        calculateAndFillTotalPedido(pedidosItemsList,pedidos,pedidosListas);
             ComponentUtils.setDecimalsOFields(bonificacionRecListField,2);
        ComponentUtils.setDecimalsOFields(bonificacionField, 2);
        ComponentUtils.setDecimalsOFields(subTotalSinImpuestos, 2);
        ComponentUtils.setDecimalsOFields(subTotalConIMpuestos,2);
        ComponentUtils.setDecimalsOFields(totalPedido, 2);

        gridPedidosItems.removeAllColumns(); // Clear any existing columns to avoid duplication

        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getIdArticulo().getDescripcion())
            .setHeader("Artículo").setAutoWidth(true);
        gridPedidosItems.addColumn(pedidosItems -> ComponentUtils.getRoundedValueBigdec(pedidosItems.getCantidad()))
            .setHeader("Cantidad").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getIdArticulo().getIdAlicuota().getDescripcion())
            .setHeader("Alicuota").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getPrecioArticulo())
            .setHeader("Precio Unitario").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getBonificacion() != null 
                ? pedidosItems.getBonificacion().setScale(2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
            .setHeader("Bonificación (%)").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(pedidosItems -> pedidosItems.getRecargo() != null 
                ? pedidosItems.getRecargo().setScale(2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
            .setHeader("Recargo (%)").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(pedidosItems ->ComponentUtils.calcSubTotalSinImp(pedidosItems))
            .setHeader("Subtotal - Impuestos").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        gridPedidosItems.addColumn(pedidosItems ->ComponentUtils.calcSubTotalConImp(pedidosItems))
            .setHeader("Subtotal + Impuestos").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);

        gridPedidosItems.setItems(pedidosItemsList);
        gridPedidosItems.setAllRowsVisible(true);
        notaAlPie.setValue(pedidos.getNotaAlPie());

        setColspan(gridPedidosItems, 5);


        add(gridPedidosItems);
        notaAlPie.setReadOnly(true); 
        bonificacionField.setReadOnly(true);
        subTotalSinImpuestos.setReadOnly(true);
        subTotalConIMpuestos.setReadOnly(true);
        totalPedido.setReadOnly(true);

        add(notaAlPie, bonificacionField,subTotalSinImpuestos,subTotalConIMpuestos,totalPedido);

        setColspan(notaAlPie, 5);
        
    }


    private void calculateAndFillTotalPedido(List<PedidosItems> pedidosItemsList, Pedidos pedi,List<PedidosListas> pedidosListas) {
        BigDecimal subTotalSinImp = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal subTotalConImp = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        for (PedidosItems item : pedidosItemsList) {
            subTotalConImp = subTotalConImp.add(ComponentUtils.calcSubTotalConImp(item));
            subTotalSinImp = subTotalSinImp.add(ComponentUtils.calcSubTotalSinImp(item));
        }
        if (pedi.isEsBonificacion()) {
            subTotalConImp = subTotalConImp
                    .subtract(subTotalConImp.multiply(pedi.getBonificacion()).divide(BigDecimal.valueOf(100)));
            subTotalSinImp = subTotalSinImp
                    .subtract(subTotalSinImp.multiply(pedi.getBonificacion()).divide(BigDecimal.valueOf(100)));
        } else if (pedi.isEsRecargo()) {
            subTotalConImp = subTotalConImp
                    .add(subTotalConImp.multiply(pedi.getRecargo()).divide(BigDecimal.valueOf(100)));
            subTotalSinImp = subTotalSinImp
                    .add(subTotalSinImp.multiply(pedi.getRecargo()).divide(BigDecimal.valueOf(100)));
        }
        if (pedidosListas != null && !pedidosListas.isEmpty()) {
            PedidosListas pl= pedidosListas.get(0);
            if (pl != null) {
                ListasPorcentuales listPor = pl.getIdListas();
                String clasificacion = listPor.getPorcentual().getClasificacion().getDisplayName();
                BigDecimal porcentual = listPor.getPorcentual().getPorcentual();
                if (listPor != null && "Bonificación".equals(clasificacion)) {
                    subTotalConImp = subTotalConImp
                            .subtract(subTotalConImp.multiply(porcentual).divide(BigDecimal.valueOf(100)));
                    subTotalSinImp = subTotalSinImp
                            .subtract(subTotalSinImp.multiply(porcentual).divide(BigDecimal.valueOf(100)));
                } else if (listPor != null && "Recargo".equals(clasificacion)) {
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




}
