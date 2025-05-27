package com.ideadistribuidora.visus.views.pedidos;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ideadistribuidora.visus.data.Articulos;
import com.ideadistribuidora.visus.data.Pedidos;
import com.ideadistribuidora.visus.data.PedidosItems;
import com.ideadistribuidora.visus.data.PedidosListas;
import com.ideadistribuidora.visus.data.dto.PedidosItemsDTO;
import com.ideadistribuidora.visus.services.PedidosService;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;

public class ReportePedidos {
    

    public byte[] exportarReporte(List<Pedidos> pedidos, PedidosService pedidosService) throws Exception {
        // 1. Cargar y compilar el .jrxml
        URL reportUrl = getClass().getClassLoader().getResource("Blank_A4.jrxml");
        if (reportUrl == null) {
            throw new IllegalArgumentException("No se encontró el recurso reporte_pedido.jrxml en el classpath.");
        }
        String reportPath = reportUrl.getPath();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
        List<JasperPrint> prints = new ArrayList<>();
        for (Pedidos pedido : pedidos) {
            // 2. Preparar parámetros
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("numeroPedido", pedido.getIdPedido());
            parametros.put("fechaPedido",
                    Date.from(pedido.getFechaPedido().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
            parametros.put("plataforma", pedido.getPlataforma().getPlataforma());
            parametros.put("estadoPedido", pedido.getEstadoPedido().getEstadoPedido());
            parametros.put("pago", pedido.getEstadoPago().getEstadoPago());
            parametros.put("cliente", pedido.getIdCliente().getNombreCliente());
            parametros.put("vendedor", pedido.getIdVendedor().getNombre());
            parametros.put("domicilio", pedido.getDomicilioClienteString());
            List<PedidosListas> pedidosListas = pedidosService.findPedidosListasByIdPedido(pedido);
            List<PedidosItems> pedidosItems = pedidosService.findPedidosItemsByIdPedidos(pedido);
            BigDecimal subTotalConImp = BigDecimal.ZERO;
            BigDecimal subTotalSinImp = BigDecimal.ZERO;
            List<PedidosItemsDTO> pedItDto = new ArrayList<>();
            for (PedidosItems pi : pedidosItems) {
                subTotalConImp = subTotalConImp.add(ComponentUtils.calcSubTotalConImp(pi));
                subTotalSinImp = subTotalSinImp.add(ComponentUtils.calcSubTotalSinImp(pi));
                Articulos art = pi.getIdArticulo();
                BigDecimal precioUnitario = art.getPrecio_costo().multiply(art.getMargen_utilidad())
                        .divide(BigDecimal.valueOf(100)).add(art.getPrecio_costo()).setScale(2,
                                RoundingMode.HALF_UP);
                PedidosItemsDTO pedItemsDto = new PedidosItemsDTO(
                        art.getDescripcion(), pi.getCantidad(), pi.getIdArticulo().getIdAlicuota().getDescripcion(),
                        precioUnitario,
                        pi.isEsBonificacion() ? pi.getBonificacion().setScale(2, RoundingMode.HALF_UP)
                                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                        pi.isEsRecargo() ? pi.getRecargo().setScale(2, RoundingMode.HALF_UP)
                                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                        ComponentUtils.calcSubTotalSinImp(pi), ComponentUtils.calcSubTotalConImp(pi));
                pedItDto.add(pedItemsDto);

            }
            if (pedido.isEsBonificacion()) {
                parametros.put("bonificacion", "SI");
                parametros.put("recargo", "NO");
                parametros.put("listas", "NO");
                parametros.put("valor",
                        String.valueOf(pedido.getBonificacion().setScale(2, RoundingMode.HALF_UP)) + " %");
                subTotalConImp = subTotalConImp
                        .subtract(subTotalConImp.multiply(pedido.getBonificacion()).divide(BigDecimal.valueOf(100)));
                subTotalSinImp = subTotalSinImp
                        .subtract(subTotalSinImp.multiply(pedido.getBonificacion()).divide(BigDecimal.valueOf(100)));
            } else if (pedido.isEsRecargo()) {
                parametros.put("bonificacion", "NO");
                parametros.put("recargo", "SI");
                parametros.put("listas", "NO");
                parametros.put("valor", String.valueOf(pedido.getRecargo().setScale(2, RoundingMode.HALF_UP)) + " %");
                subTotalConImp = subTotalConImp
                        .add(subTotalConImp.multiply(pedido.getRecargo()).divide(BigDecimal.valueOf(100)));
                subTotalSinImp = subTotalSinImp
                        .add(subTotalSinImp.multiply(pedido.getRecargo()).divide(BigDecimal.valueOf(100)));
            } else if (pedidosListas != null && !pedidosListas.isEmpty()) {
                parametros.put("bonificacion", "NO");
                parametros.put("recargo", "NO");
                parametros.put("listas", "SI");
                parametros.put("valor", pedidosListas.get(0).getIdListas().getLista().getDescripcion() + " "
                        + pedidosListas.get(0).getIdListas().getPorcentual().getDescripcion());
            } else {
                parametros.put("bonificacion", "NO");
                parametros.put("recargo", "NO");
                parametros.put("listas", "NO");
                parametros.put("valor", "0,00");
            }



            // 3. Usar los ítems como data source
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pedItDto);
            parametros.put("ItemsDataSource", dataSource);
            parametros.put("totalBonificacion", ComponentUtils.getRoundedValueBigdec(pedido.getBonificacion()));
            parametros.put("totalSinImpuestos", ComponentUtils.getRoundedValueBigdec(subTotalSinImp));
            parametros.put("totalConImpuestos", ComponentUtils.getRoundedValueBigdec(subTotalConImp));
            parametros.put("totalPedido", ComponentUtils.getRoundedValueBigdec(subTotalConImp));
            // 4. Llenar el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            prints.add(jasperPrint);
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 5. Exportar a PDF
            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setExporterInput(SimpleExporterInput.getInstance(prints));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

            exporter.exportReport();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new JRException("Error al generar el PDF en memoria", e);
        }
    }

}
