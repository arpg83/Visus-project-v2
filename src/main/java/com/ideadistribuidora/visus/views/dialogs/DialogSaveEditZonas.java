package com.ideadistribuidora.visus.views.dialogs;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Zonas;
import com.ideadistribuidora.visus.data.enums.VisitaEnum;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

@Route("save-edit-dialogo-zonas")
public class DialogSaveEditZonas extends Dialog {

    private Button saveZonaButton;
    private Button cancelZonaButton;
    private Zonas zonas;
    private VisitaEnum[] allVisita;

    public DialogSaveEditZonas(String header,
            Zonas zonaSelected, ConfirmationHandler<Zonas> onConfirm) {
        // dialogDomicilios = new Dialog();

        setHeaderTitle(header);

        saveZonaButton = new Button("Guardar");
        saveZonaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveZonaButton.setEnabled(false);
        cancelZonaButton = new Button("Cancelar");
        getFooter().add(cancelZonaButton);
        getFooter().add(saveZonaButton);

        VerticalLayout dialogLayout = createDialogLayout(zonaSelected, onConfirm);
        add(dialogLayout);

        // add(dialogDomicilios);

        // Center the button within the example
        // getStyle().set("position", "fixed").set("top", "0").set("right", "0")
        // .set("bottom", "0").set("left", "0").set("display", "flex")
        // .set("align-items", "center").set("justify-content", "center");
        // dialogDomicilios.open();
    }

    private VerticalLayout createDialogLayout(Zonas zonaSelected,
            ConfirmationHandler<Zonas> onConfirm) {
        Binder<Zonas> binderZonas = new Binder<>(Zonas.class);
        TextField desc = new TextField("Descripción");
        desc.setMaxLength(100);
        TextArea areGeografica = new TextArea("Área Geográfica");
        areGeografica.setMaxLength(200);
        ComboBox<VisitaEnum> frecVisitaEnum = new ComboBox<VisitaEnum>("Frecuencia de Visita");
        frecVisitaEnum.setPlaceholder("Seleccione Frecuencia de Visita");
        frecVisitaEnum.setItems(VisitaEnum.values());
        frecVisitaEnum.setItemLabelGenerator(VisitaEnum::getDisplayVisita);
        binderZonas.forField(desc).asRequired("Descripción es Requerido")
                .bind("descripcion");
        binderZonas.forField(areGeografica).asRequired("Área Geográfica es requerida")
                .bind("areaGeografica");
        binderZonas.forField(frecVisitaEnum).asRequired("Frecuencia de Visite es requerido")
                .bind("frecuenciaVisita");

        binderZonas.addStatusChangeListener(
                event -> saveZonaButton.setEnabled(binderZonas.isValid()));
        binderZonas.bindInstanceFields(this);
        if (zonaSelected != null)
            binderZonas.readBean(zonaSelected);
        saveZonaButton.addClickListener(e -> {
            try {
                if (zonas == null) {
                    zonas = new Zonas();
                }
                binderZonas.writeBean(zonas);
                onConfirm.handle(this.zonas);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al Actualizar los datos. Alguien mas está actualizando los datos.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show(
                        "Error al Guardar/Modificar el Domicilio. Revise Nuevamente que todos los datos sean Válidos");
            } catch (Exception excep) {
                Notification n = Notification.show("Error al Guardar Domicilios: " + excep.getMessage() + " "
                        + excep.getCause());
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        cancelZonaButton.addClickListener(e -> {
            close();
        });
        VerticalLayout dialogLayout = new VerticalLayout(desc, areGeografica,
                frecVisitaEnum);

        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

}
