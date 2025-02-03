package com.ideadistribuidora.visus.views.dialogs;

import com.ideadistribuidora.visus.data.ComisionesTramos;
import com.ideadistribuidora.visus.services.VendedoresService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

@Route("save-edit-dialogo-comsiones-tramos")
public class DialogComisionesTramos extends Dialog {

    private Grid<ComisionesTramos> grid;
    private BigDecimalField desdeMontoField;
    private BigDecimalField hastaMontoField;
    private BigDecimalField porcentualField;
    private Button cancelButton;
    private Button saveButton;
    private Button deleteButton;
    private Binder<ComisionesTramos> binder;
    private ComisionesTramos comisionesTramos;
    private int idVendedor;
    private VendedoresService vendedoresService;
    private Notification notification;

    public DialogComisionesTramos(int idVendedor, VendedoresService vendedoresService) {
        setHeaderTitle("Comisiones Por Tramos");
        setWidth("800px");
        Button closeButton = new Button(new Icon("lumo", "cross"),
            (e) -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        getHeader().add(closeButton);
        this.idVendedor = idVendedor;
        this.vendedoresService = vendedoresService;
        createSplitLayout();
        configureBinder();
    }

    private void createSplitLayout() {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSplitterPosition(60);

        // Primary section with grid
        grid = new Grid<>(ComisionesTramos.class, false);
        grid.addColumn(ComisionesTramos::getDesdeImporte).setHeader("Desde Monto");
        grid.addColumn(ComisionesTramos::getHastaImporte).setHeader("Hasta Monto");
        grid.addColumn(ComisionesTramos::getPorcentaje).setHeader("Porcentual(%)");
        grid.setItems(vendedoresService.getComisionesTramosByIdVendedor(idVendedor));

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
            comisionesTramos = e.getValue();
            binder.readBean(comisionesTramos);
            saveButton.setEnabled(true);
            deleteButton.setEnabled(true);
            }else{
                clearForm();
                deleteButton.setEnabled(false);
            }
        });

        splitLayout.addToPrimary(grid);

        // Secondary section with BigDecimalFields and buttons
        VerticalLayout secondaryLayout = new VerticalLayout();

        desdeMontoField = new BigDecimalField("Desde Monto");
        hastaMontoField = new BigDecimalField("Hasta Monto");
        porcentualField = new BigDecimalField("Porcentual");

        secondaryLayout.add(desdeMontoField, hastaMontoField, porcentualField);

        // HorizontalLayout with buttons
        HorizontalLayout buttonLayout = new HorizontalLayout();
        cancelButton = new Button("Cancelar");
        saveButton = new Button("Grabar");
        deleteButton = new Button("Eliminar");

        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setEnabled(false);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setEnabled(false);

        buttonLayout.add(cancelButton, saveButton, deleteButton);
        secondaryLayout.add(buttonLayout);

        splitLayout.addToSecondary(secondaryLayout);

        add(splitLayout);
    }

    private void configureBinder() {
        binder = new Binder<>(ComisionesTramos.class);

        binder.forField(desdeMontoField)
              .asRequired("Desde Monto es Requerido")
              .bind(ComisionesTramos::getDesdeImporte, ComisionesTramos::setDesdeImporte);

        binder.forField(hastaMontoField)
              .asRequired("Hasta Monto es Requerido")
              .bind(ComisionesTramos::getHastaImporte, ComisionesTramos::setHastaImporte);

        binder.forField(porcentualField)
              .asRequired("Porcentual es Requerido")
              .bind(ComisionesTramos::getPorcentaje, ComisionesTramos::setPorcentaje);
        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        saveButton.addClickListener(e -> save());
        cancelButton.addClickListener(e -> cancel());
        deleteButton.addClickListener(e -> delete());
    }

    private void save() {
        try {
            if (comisionesTramos == null) {
                comisionesTramos = new ComisionesTramos();
            }

            comisionesTramos.setIdVendedor(idVendedor);
            binder.writeBean(comisionesTramos);
            //Save comisionesTramos to the database
            vendedoresService.saveComisionesTramos(comisionesTramos);
            notification=Notification.show("Datos Guardados");
            notification.setPosition(Position.MIDDLE);
            notification.setDuration(3000);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            grid.setItems(vendedoresService.getComisionesTramosByIdVendedor(idVendedor));
            clearForm();
        } catch (ValidationException e) {
            notification = Notification.show("Ocurrió un error al guardar los datos, verifique que los datos sean correctos", 3000,Notification.Position.MIDDLE);
            notification.setPosition(Position.MIDDLE);
            notification.setDuration(3000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void cancel() {
        clearForm();
        deleteButton.setEnabled(false);
    }

    private void delete() {
        if (comisionesTramos != null) {
            // Delete comisionesTramos from the database
             vendedoresService.deleteComisionesTramos(comisionesTramos);
            notification=Notification.show("Datos Eliminados");
            notification.setPosition(Position.MIDDLE);
            notification.setDuration(3000);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            grid.setItems(vendedoresService.getComisionesTramosByIdVendedor(idVendedor));
            clearForm();
        }
    }

    private void clearForm() {
        binder.readBean(null);
        comisionesTramos = null;
    }
}
