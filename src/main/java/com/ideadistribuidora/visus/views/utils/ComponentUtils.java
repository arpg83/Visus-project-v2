package com.ideadistribuidora.visus.views.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.ideadistribuidora.visus.data.Alicuotas;
import com.ideadistribuidora.visus.data.PedidosItems;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class ComponentUtils {

    public static TextField validatePhone(TextField telefono, String label) {
        telefono = new TextField(label);
        telefono.setPlaceholder("Ingrese Número de Teléfono");
        telefono.setRequiredIndicatorVisible(false);
        telefono.setPattern("^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$");
        telefono.setAllowedCharPattern("[0-9()+-]");
        telefono.setMinLength(5);
        telefono.setMaxLength(15);

        return telefono;
    }

    public static EmailField validateEmail(EmailField emailField) {
        emailField = new EmailField();
        emailField.setLabel("Email address");
        emailField.getElement().setAttribute("name", "email");
        emailField.setPlaceholder("julia.scheider@email.com");
        emailField.setErrorMessage("Ingrese un email válido");
        emailField.setClearButtonVisible(true);
        return emailField;

    }

    public static TextArea validateTextArea(TextArea textArea, int charLimit, String label) {
        textArea = new TextArea();
        textArea.setLabel(label);
        textArea.setMaxLength(charLimit);
        textArea.setValueChangeMode(ValueChangeMode.EAGER);
        textArea.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + charLimit);
        });
        return textArea;
    }

    // public static void ordenarCombo(ComboBox<String> comboBox, List<Documento>
    // documentos,
    // ClientesService clientesService) {
    // comboBox.setItems(documentos.stream().map(Documento::getDescripcion).toArray(String[]::new));

    // // Configurar la vinculación manualmente usando el convertidor
    // comboBox.addValueChangeListener(event -> {
    // String value = event.getValue();
    // Documento documento = new DocumentoToStringConverter(clientesService)
    // .convertToModel(value, new ValueContext()).getOrThrow(msg -> new
    // RuntimeException(msg));
    // // Manejar la selección del documento
    // System.out.println("Selected Documento: "
    // + (documento != null ? documento.getDescripcion() + " " +
    // documento.getId_Documento() : "null"));
    // });
    // }

    public static TextField validateOnlyNumbers(TextField textField, String label) {
        textField = new TextField(label);
        textField.setPattern("\\d*");
        textField.setRequiredIndicatorVisible(true);
        textField.setAllowedCharPattern("[0-9]");
        textField.setMaxLength(15);

        return textField;
    }

    public static void errorMessage(TextField textField, String errorMsj, String pattern) {
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addKeyPressListener(event -> {
            String value = textField.getValue();
            if (!value.matches(pattern)) {
                textField.setInvalid(true);
                textField.setErrorMessage(errorMsj);
                Notification.show("Entrada invalida: " + errorMsj, 3000,
                        Notification.Position.TOP_CENTER);
            } else {
                textField.setInvalid(false);
            }
        });
        textField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (!value.matches(pattern)) {
                textField.setInvalid(true);
                textField.setErrorMessage(errorMsj);
                Notification.show("Entrada invalida: " + errorMsj, 3000, Notification.Position.TOP_CENTER);
            } else {
                textField.setInvalid(false);
            }
        });
    }

    public static DatePickerI18n getI18n() {
        DatePickerI18n españolI18n = new DatePickerI18n();
        españolI18n.setMonthNames(List.of("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"));
        españolI18n.setWeekdays(
                List.of("Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"));
        españolI18n.setWeekdaysShort(List.of("Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab"));
        españolI18n.setCancel("Cancelar");
        españolI18n.setToday("Hoy");
        return españolI18n;
    }

    public static void getRoundedValue(BigDecimalField field) {
        BigDecimal valor = field.getValue();

        // Redondear el valor al número de decimales especificado
        if (valor != null) {
            BigDecimal valorRedondeado = valor.setScale(4, RoundingMode.HALF_UP);
            // Establecer el valor redondeado de vuelta en el BigDecimalField
            field.setValue(valorRedondeado);
        }
    }

    public static BigDecimal getRoundedValueBigdec(BigDecimal precioFinalConIva) {

        // Redondear el valor al número de decimales especificado
        if (precioFinalConIva != null) {
            precioFinalConIva = precioFinalConIva.setScale(2, RoundingMode.HALF_UP);
            // Establecer el valor redondeado de vuelta en el BigDecimalField
        }
        return precioFinalConIva;
    }

    public static void setDecimalsOFields(BigDecimalField field, int number) {
        //field.setValue(BigDecimal.ZERO.setScale(number, RoundingMode.HALF_UP));
        field.setValue(field != null ? field.getValue().setScale(number, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(number, RoundingMode.HALF_UP));
        // field.addValueChangeListener(e -> {
        //     if (field.getValue() != null) {
        //         BigDecimal value = field.getValue().setScale(number, RoundingMode.HALF_UP);
        //         field.setValue(value);
        //     }
        // });
    }

    public static BigDecimal calcSubTotalConImp(PedidosItems pedidosItems2) {
        BigDecimal precioUnitario = pedidosItems2.getPrecioArticulo();
        BigDecimal cantidad = pedidosItems2.getCantidad();
        BigDecimal bonificacion = pedidosItems2.getBonificacion() != null ? pedidosItems2.getBonificacion()
                : BigDecimal.ZERO;
        BigDecimal recargo = pedidosItems2.getRecargo() != null ? pedidosItems2.getRecargo() : BigDecimal.ZERO;
        // BigDecimal precioSInImp = calcSubTotalSinImp(pedidosItems2).divide(cantidad);
        BigDecimal alic = BigDecimal.ZERO;
        Alicuotas alicuotas = pedidosItems2.getIdArticulo().getIdAlicuota();
        alic = alicuotas.getDescripcion().contains("%")
                ? BigDecimal.valueOf(Double
                        .parseDouble(alicuotas.getDescripcion().replace("%",
                                "")))
                : BigDecimal.ZERO;
        BigDecimal precBonRec = precioUnitario
                .subtract(precioUnitario.multiply(bonificacion).divide(BigDecimal.valueOf(100)))
                .add(precioUnitario.multiply(recargo).divide(BigDecimal.valueOf(100)));
        BigDecimal subtotalConImpuesto = precBonRec.add(precBonRec.multiply(alic).divide(BigDecimal.valueOf(100)))
                .multiply(cantidad);
        return subtotalConImpuesto.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcSubTotalSinImp(PedidosItems pedItem) {
        BigDecimal precioUnitario = pedItem.getPrecioArticulo();
        BigDecimal cantidad = pedItem.getCantidad();
        BigDecimal bonificacion = pedItem.getBonificacion() != null ? pedItem.getBonificacion() : BigDecimal.ZERO;
        BigDecimal recargo = pedItem.getRecargo() != null ? pedItem.getRecargo() : BigDecimal.ZERO;
        BigDecimal subtotalSinImpuesto = precioUnitario
                .subtract(precioUnitario.multiply(bonificacion).divide(BigDecimal.valueOf(100)))
                .add(precioUnitario.multiply(recargo).divide(BigDecimal.valueOf(100)))
                .multiply(cantidad);
        return subtotalSinImpuesto.setScale(2, RoundingMode.HALF_UP);
    }


}
