package com.ideadistribuidora.visus.views.dialogs;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Comisiones;
import com.ideadistribuidora.visus.data.enums.TipoComisionEnum;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

@Route("save-edit-dialogo-comisiones")
public class DialogSaveEditComisiones extends Dialog {

    private Button saveComisionesButton;
    private Button cancelComisionesButton;
    private Comisiones comisiones;

    public DialogSaveEditComisiones(String header,
            Comisiones comisionesSelected, ConfirmationHandler<Comisiones> onConfirm) {
        // dialogDomicilios = new Dialog();

        setHeaderTitle(header);

        saveComisionesButton = new Button("Guardar");
        saveComisionesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveComisionesButton.setEnabled(false);
        cancelComisionesButton = new Button("Cancelar");
        getFooter().add(cancelComisionesButton);
        getFooter().add(saveComisionesButton);

        VerticalLayout dialogLayout = createDialogLayout(comisionesSelected, onConfirm);
        add(dialogLayout);

        // add(dialogDomicilios);

        // Center the button within the example
        // getStyle().set("position", "fixed").set("top", "0").set("right", "0")
        // .set("bottom", "0").set("left", "0").set("display", "flex")
        // .set("align-items", "center").set("justify-content", "center");
        // dialogDomicilios.open();
    }

    private VerticalLayout createDialogLayout(Comisiones comisionesSelected,
            ConfirmationHandler<Comisiones> onConfirm) {
        Binder<Comisiones> binder = new Binder<>(Comisiones.class);
        ComboBox<TipoComisionEnum> tipoComisionesEnum = new ComboBox<>("Tipo de Comisión");
        tipoComisionesEnum.setPlaceholder("Seleccione Tipo de Comisión");
        tipoComisionesEnum.setItems(TipoComisionEnum.values());
        tipoComisionesEnum.setItemLabelGenerator(TipoComisionEnum::getDisplayTipoComision);
        DateTimePicker fechaModificacion = new DateTimePicker("Fecha de Modificación");
        fechaModificacion.setDatePickerI18n(ComponentUtils.getI18n());
        fechaModificacion.setLocale(Locale.US);
        DateTimePicker vigenciaDesde = new DateTimePicker("Vigencia Desde");
        vigenciaDesde.setDatePickerI18n(ComponentUtils.getI18n());
        vigenciaDesde.setLocale(Locale.US);
        DateTimePicker vigenciaHasta = new DateTimePicker("Vigencia Hasta");
        vigenciaHasta.setDatePickerI18n(ComponentUtils.getI18n());
        vigenciaHasta.setLocale(Locale.US);
        BigDecimalField porcentajeSobreImporte = new BigDecimalField("Porcentual Sobre Importe");
        porcentajeSobreImporte.setValue(BigDecimal.ZERO);
        // porcentajeSobreImporte.setRequired(true);
        // porcentajeSobreImporte.setRequiredIndicatorVisible(true);
        // porcentajeSobreImporte.setErrorMessage("Porcentual sobre Importe es Requerido");

        BigDecimalField porcentajeImporteFijo = new BigDecimalField("Porcentual Sobre Importe Fijo");
        BigDecimalField porcentajeSobreMargen = new BigDecimalField("Porcentual Sobre Margen");
        binder.forField(tipoComisionesEnum).asRequired("Tipo de Comisión es Requerido")
                .bind("tipoComision");
        binder.forField(fechaModificacion).asRequired("Fecha de Modificación es Requerido")
                .bind("fechaModificacion");
        binder.forField(vigenciaDesde).asRequired("Vigencia Desde es Requerido")
                .bind("vigenciaDesde");
        binder.forField(vigenciaHasta).bind("vigenciaHasta");
        binder.forField(porcentajeSobreImporte).asRequired("Porcentual sobre Importe es Requerido")
                .bind("porcentajeSobreImporte");
        binder.forField(porcentajeImporteFijo).bind("porcentajeImporteFijo");
        binder.forField(porcentajeSobreMargen).bind("porcentajeSobreMargen");


        binder.addStatusChangeListener(
                event -> saveComisionesButton.setEnabled(binder.isValid()));
        binder.bindInstanceFields(this);
        if (comisionesSelected != null)
            binder.readBean(comisionesSelected);
        saveComisionesButton.addClickListener(e -> {
            try {
                if (comisiones == null) {
                    comisiones = new Comisiones();
                }
                binder.writeBean(comisiones);
                onConfirm.handle(this.comisiones);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al Actualizar los datos. Alguien mas está actualizando los datos.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show(
                        "Error al Guardar/Modificar la Comision. Revise Nuevamente que todos los datos sean Válidos");
            } catch (Exception excep) {
                Notification n = Notification.show("Error al Guardar la Comisión: " + excep.getMessage() + " "
                        + excep.getCause());
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        cancelComisionesButton.addClickListener(e -> {
            close();
        });
        VerticalLayout dialogLayout = new VerticalLayout(tipoComisionesEnum, fechaModificacion, vigenciaDesde, vigenciaHasta, porcentajeSobreImporte, porcentajeImporteFijo, porcentajeSobreMargen);

        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

}
