package com.ideadistribuidora.visus.views.dialogs;

import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;
import com.ideadistribuidora.visus.views.utils.StringToShortConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

@Route("save-edit-dialogo-domicilio")
public class DialogSaveEditDomicilios extends Dialog {

    private Button saveDomButton;
    private Button cancelDomButton;
    private Domicilios domicilios;

    public DialogSaveEditDomicilios(TipoDomicilioEnum[] allTipoDomicilio, List<Localidades> allLocalidades,
            String header,
            Domicilios domSelected, ConfirmationHandler<Domicilios> onConfirm) {
        // dialogDomicilios = new Dialog();

        setHeaderTitle(header);

        saveDomButton = new Button("Guardar");
        saveDomButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveDomButton.setEnabled(false);
        cancelDomButton = new Button("Cancelar");
        getFooter().add(cancelDomButton);
        getFooter().add(saveDomButton);

        VerticalLayout dialogLayout = createDialogLayout(allTipoDomicilio,
                allLocalidades, domSelected, onConfirm);
        add(dialogLayout);

        // add(dialogDomicilios);

        // Center the button within the example
        // getStyle().set("position", "fixed").set("top", "0").set("right", "0")
        // .set("bottom", "0").set("left", "0").set("display", "flex")
        // .set("align-items", "center").set("justify-content", "center");
        // dialogDomicilios.open();
    }

    private VerticalLayout createDialogLayout(TipoDomicilioEnum[] allTipoDomicilio,
            List<Localidades> allLocalidades, Domicilios domSelected,
            ConfirmationHandler<Domicilios> onConfirm) {
        Binder<Domicilios> binderDomicilios = new Binder<>(Domicilios.class);
        ComboBox<TipoDomicilioEnum> tipoDomicilio = new ComboBox<TipoDomicilioEnum>("Tipo de Domicilio");
        tipoDomicilio.setPlaceholder("Seleccione Tipo de Domicilio");
        tipoDomicilio.setItems(allTipoDomicilio);
        tipoDomicilio.setItemLabelGenerator(TipoDomicilioEnum::getDisplayTipoDomicilio);
        TextField sector = new TextField("Sector");
        sector.setMaxLength(10);
        TextField oficina = new TextField("Oficina");
        oficina.setMaxLength(10);
        TextField numero = new TextField("Número/Altura");
        numero.setMaxLength(4);
        TextField manzana = new TextField("Manzana");
        manzana.setMaxLength(10);
        TextField lote = new TextField("Lote");
        lote.setMaxLength(10);
        ComboBox<Localidades> localidad = new ComboBox<>("Localidad");
        localidad.setPlaceholder("Seleccione Localidad");
        localidad.setItems(allLocalidades);
        localidad.setItemLabelGenerator(Localidades::getNombre);
        localidad.setRequiredIndicatorVisible(true);
        TextField depto = new TextField("Departamento");
        depto.setMaxLength(10);
        TextField casa = new TextField("Casa");
        casa.setMaxLength(10);
        TextField calle = new TextField("Calle");
        calle.setMaxLength(60);
        TextField barrio = new TextField("Barrio");
        barrio.setMaxLength(60);
        binderDomicilios.forField(tipoDomicilio).asRequired("Tipo de Domicilio es Requerido")
                .bind("tipoDomicilio");
        binderDomicilios.forField(sector).bind("sector");
        binderDomicilios.forField(oficina).bind("oficina");
        binderDomicilios.forField(numero)
                .asRequired("Número es Requerido")
                .withConverter(new StringToShortConverter())
                .bind(Domicilios::getNumero, Domicilios::setNumero);
        binderDomicilios.forField(manzana).bind("manzana");
        binderDomicilios.forField(lote).bind("lote");
        binderDomicilios.forField(localidad).asRequired("Localidad es Requerido").bind("localidad");
        binderDomicilios.forField(depto).bind("depto");
        binderDomicilios.forField(casa).bind("casa");
        binderDomicilios.forField(calle).asRequired("Calle es Requerido").bind("calle");
        binderDomicilios.forField(barrio).bind("barrio");

        binderDomicilios.addStatusChangeListener(
                event -> saveDomButton.setEnabled(binderDomicilios.isValid()));
        binderDomicilios.bindInstanceFields(this);
        if (domSelected != null)
            binderDomicilios.readBean(domSelected);
        saveDomButton.addClickListener(e -> {
            try {
                if (domicilios == null) {
                    domicilios = new Domicilios();
                }
                binderDomicilios.writeBean(domicilios);
                onConfirm.handle(this.domicilios);
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
        cancelDomButton.addClickListener(e -> {
            close();
        });
        VerticalLayout dialogLayout = new VerticalLayout(tipoDomicilio, numero,
                calle, depto, casa,
                oficina, manzana, lote,
                sector, barrio, localidad);

        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

}
