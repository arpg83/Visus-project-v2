package com.ideadistribuidora.visus.views.dialogs;

import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.ideadistribuidora.visus.data.Bancos;
import com.ideadistribuidora.visus.data.enums.TipoCuentaEnum;
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
import com.vaadin.flow.data.converter.StringToLongConverter;

public class DialogSaveEditBancos<T> extends Dialog {
    private Button saveDomButton;
    private Button cancelDomButton;
    private T classBancos;
    private Binder<T> binder;

    public DialogSaveEditBancos(List<Bancos> bancosList, TipoCuentaEnum[] tipoCuentaEnums,
            String header, T bancosSelected, Class<T> clazz, ConfirmationHandler<T> onConfirm) {
        // dialogDomicilios = new Dialog();

        setHeaderTitle(header);

        saveDomButton = new Button("Guardar");
        saveDomButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveDomButton.setEnabled(false);
        cancelDomButton = new Button("Cancelar");
        getFooter().add(cancelDomButton);
        getFooter().add(saveDomButton);
        binder = new Binder<>(clazz);
        VerticalLayout dialogLayout = createDialogLayout(bancosList, tipoCuentaEnums, bancosSelected, clazz,
                onConfirm);
        add(dialogLayout);

    }

    private VerticalLayout createDialogLayout(List<Bancos> bancosList,
            TipoCuentaEnum[] tipoCuentaEnums, T bancoSelected, Class<T> clazz,
            ConfirmationHandler<T> onConfirm) {

        ComboBox<Bancos> bancos = new ComboBox<>("Banco");
        bancos.setPlaceholder("Seleccione el Banco");
        bancos.setItems(bancosList);
        bancos.setItemLabelGenerator(Bancos::getNombre);
        ComboBox<TipoCuentaEnum> tipoCuenta = new ComboBox<>("Tipo de Cuenta");
        tipoCuenta.setPlaceholder("Seleccione el Tipo de Cuenta");
        tipoCuenta.setItems(tipoCuentaEnums);
        tipoCuenta.setItemLabelGenerator(TipoCuentaEnum::getDisplayTipoDeCuenta);
        TextField cbu = new TextField("CBU");
        TextField alias = new TextField("Alias");
        alias.setMaxLength(60);
        binder.forField(bancos).asRequired("Banco es Requerido")
                .bind("bancos");
        binder.forField(tipoCuenta).asRequired("Tipo de Cuenta es Requerido")
                .bind("tipoCuenta");
        binder.forField(cbu).asRequired("CBU es Requerido")
                .withConverter(new StringToLongConverter("Sólo puede ingresar números"))
                .bind("cbu");

        binder.forField(alias)
                .bind("alias");

        binder.addStatusChangeListener(
                event -> saveDomButton.setEnabled(binder.isValid()));
        binder.bindInstanceFields(this);
        if (bancoSelected != null)
            binder.readBean(bancoSelected);

        saveDomButton.addClickListener(e -> {
            try {
                if (classBancos == null) {
                    classBancos = clazz.getDeclaredConstructor().newInstance();
                }

                binder.writeBean(this.classBancos);
                onConfirm.handle(this.classBancos);

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
        VerticalLayout dialogLayout = new VerticalLayout(bancos, tipoCuenta, cbu, alias);

        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

}
