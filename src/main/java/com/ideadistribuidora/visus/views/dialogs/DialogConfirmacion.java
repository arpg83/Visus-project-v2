package com.ideadistribuidora.visus.views.dialogs;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route("confirmacion-dialogo-domicilio")
public class DialogConfirmacion extends Div {
    private Span status;

    public DialogConfirmacion(String msg, Runnable onConfirm) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        status = new Span();
        status.setVisible(false);

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirmar");
        dialog.setText(msg);

        dialog.setCancelable(true);
        dialog.setCancelText("Cancelar");
        dialog.addCancelListener(event -> {
            dialog.close();
        });

        dialog.setConfirmText("Guardar");
        dialog.addConfirmListener(event -> {
            dialog.close();
            onConfirm.run();

        });

        dialog.open();

        // Center the button within the example
        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
    }

}
