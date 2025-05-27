package com.ideadistribuidora.visus.views.precios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;

import com.ideadistribuidora.visus.data.Articulos;
import com.ideadistribuidora.visus.data.Lineas;
import com.ideadistribuidora.visus.data.Proveedores;
import com.ideadistribuidora.visus.data.Rubros;
import com.ideadistribuidora.visus.services.ArticulosService;
import com.ideadistribuidora.visus.views.utils.ComponentUtils;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Precios")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 20)
@Route(value = "21/:preciosID?/:action?(edit)")
public class PreciosView extends Div {
    private CollaborationBinder<Articulos> binder;
    private final ArticulosService articulosService;
    private Dialog dialog;
    private RadioButtonGroup<String> opcionPrecios;
    private String selectedOption = "";
    private ComboBox<Lineas> lineas;
    private ComboBox<Rubros> rubros;
    private ComboBox<Proveedores> proveedor;
    private BigDecimalField actualField;
    private BigDecimalField nuevoField;
    private BigDecimalField incrementoField;
    private Button aplicarButton;
    CollaborationAvatarGroup avatarGroup;

    public PreciosView(ArticulosService articulosService) {
        this.articulosService = articulosService;
        addClassNames("precios-view");

        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");
        binder = new CollaborationBinder<>(Articulos.class, userInfo);


        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "visible");

        opcionPrecios = new RadioButtonGroup<>();
        opcionPrecios.setWidth(20, Unit.PERCENTAGE);
        opcionPrecios.setItems("Por Rubro y Línea", "Por Proveedores", "Por Margen de Ganancia");
        opcionPrecios.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setWidth(80, Unit.PERCENTAGE);

        // First item
        HorizontalLayout firstItemLayout = new HorizontalLayout();
        firstItemLayout.setWidth(100, Unit.PERCENTAGE);
        firstItemLayout.getStyle().set("border-bottom", "1px solid black");
        firstItemLayout.setEnabled(false);
        this.lineas = new ComboBox<>("Lineas");
        lineas.setWidth(40, Unit.PERCENTAGE);
        lineas.setPlaceholder("Seleccione Linea");
        lineas.setItems(articulosService.getAllLineas());
        lineas.setItemLabelGenerator(Lineas::getDescripcion);

        rubros = new ComboBox<>("Rubros");
        rubros.setWidth("40%");
        rubros.setPlaceholder("Seleccione Rubros");
        rubros.setItems(articulosService.getAllRubros());
        rubros.setItemLabelGenerator(Rubros::getDescripcion);
        rubros.addValueChangeListener(e -> {
            Rubros selectedRubros = e.getValue();
            if (selectedRubros != null) {
                lineas.setItems(articulosService.findLineasByRubros(selectedRubros));
            } else {
                lineas.clear();
                lineas.setItems();
            }
        });

        firstItemLayout.add(rubros, lineas);
        verticalLayout.add(firstItemLayout);

        // Second item
        HorizontalLayout secondItemLayout = new HorizontalLayout();
        secondItemLayout.setWidth(100, Unit.PERCENTAGE);
        secondItemLayout.setEnabled(false);
        secondItemLayout.getStyle().set("border-bottom", "1px solid black");
        proveedor = new ComboBox<>("Proveedores");
        proveedor.setPlaceholder("Seleccione Provedor");
        proveedor.setItems(articulosService.getAllProveedores());
        proveedor.setItemLabelGenerator(Proveedores::getNombreReal);
        proveedor.getStyle().setWidth("100%");
        secondItemLayout.add(proveedor);
        verticalLayout.add(secondItemLayout);

        // Third item
        HorizontalLayout thirdItemLayout = new HorizontalLayout();
        thirdItemLayout.setWidth(100, Unit.PERCENTAGE);
        thirdItemLayout.setEnabled(false);
        thirdItemLayout.getStyle().set("border-bottom", "1px solid black");
        actualField = new BigDecimalField("Actual");
        ComponentUtils.setDecimalsOFields(actualField, 2);
        nuevoField = new BigDecimalField("Nuevo");
        ComponentUtils.setDecimalsOFields(nuevoField, 2);
        thirdItemLayout.add(actualField, nuevoField);
        verticalLayout.add(thirdItemLayout);

        // Fourth item
        HorizontalLayout fourthItemLayout = new HorizontalLayout();
        fourthItemLayout.getStyle().set("border-bottom", "1px solid black");
        fourthItemLayout.setWidth(100, Unit.PERCENTAGE);
        fourthItemLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        incrementoField = new BigDecimalField("Incremento(%)");
        ComponentUtils.setDecimalsOFields(incrementoField, 2);
        fourthItemLayout.add(incrementoField);
        verticalLayout.add(fourthItemLayout);

        // Fifth item
        HorizontalLayout fifthItemLayout = new HorizontalLayout();
        fifthItemLayout.setWidth(100, Unit.PERCENTAGE);
        fifthItemLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        Button cancelarButton = new Button("Cancelar");
        cancelarButton.addClickListener(e -> dialog.close());
        aplicarButton = new Button("Aplicar", e -> {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Cambio de Precios");
            confirmDialog.setText("¿Está Ud. seguro de actualizar los precios?");
            confirmDialog.setConfirmText("OK");
            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("Cancelar");
            confirmDialog.addCancelListener(event -> {
                confirmDialog.close();
            });
            confirmDialog.addConfirmListener(event -> {
                try {
                    List<Articulos> articulos = new ArrayList<>();
                    if ("Por Rubro y Línea".equals(this.selectedOption)) {
                        if (rubros.getValue() != null && lineas.getValue() != null) {
                            articulos = articulosService.findArticulosByIdLinea(lineas.getValue());
                            if (articulos.isEmpty()) {
                                Notification n = Notification.show(
                                        "No se encontraron Articulos para el Rubro y Linea seleccionada",
                                        3000, Position.MIDDLE);
                                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                            }
                        } else {
                            Notification n = Notification.show(
                                    "Debe seleccionar Rubro y Linea",
                                    3000, Position.MIDDLE);
                            n.addThemeVariants(NotificationVariant.LUMO_ERROR);

                        }

                    } else if ("Por Proveedores".equals(this.selectedOption)) {
                        articulos = articulosService.findArticulosByProveedor(proveedor.getValue());
                        if (articulos.isEmpty()) {
                            Notification n = Notification.show(
                                    "No se encontraron Articulos para el Proveedor seleccionado",
                                    3000, Position.MIDDLE);
                            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    } else if ("Por Margen de Ganancia".equals(this.selectedOption)) {
                        articulos = articulosService.findArticulosByMargen_utilidad(actualField.getValue());
                        if (articulos.isEmpty()) {
                            Notification n = Notification.show(
                                    "No se encontraron Articulos para el Margen de Ganancia solicitado",
                                    3000, Position.MIDDLE);
                            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }

                        for (Articulos articulo : articulos) {
                            articulo.setMargen_utilidad(nuevoField.getValue());
                        }
                    }
                    if (!"Por Margen de Ganancia".equals(this.selectedOption)) {
                        for (Articulos articulo : articulos) {
                            BigDecimal valorIncr = incrementoField.getValue().divide(BigDecimal.valueOf(100));
                            BigDecimal nuevoPrecio = articulo.getPrecio_costo()
                                    .add(articulo.getPrecio_costo().multiply(valorIncr));
                            articulo.setPrecio_costo(nuevoPrecio);
                        }
                    }


                    aplicarCambios(articulos);
                    confirmDialog.close();
                } catch (ObjectOptimisticLockingFailureException exception) {
                    Notification n = Notification.show(
                            "Error al Actualizar los datos. Alguien mas está actualizando los datos.");
                    n.setPosition(Position.MIDDLE);
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
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
            confirmDialog.open();

        });
        aplicarButton.setEnabled(false);
        fifthItemLayout.add(cancelarButton, aplicarButton);
        verticalLayout.add(fifthItemLayout);
        HorizontalLayout princHorizontalLayout = new HorizontalLayout();
        princHorizontalLayout.add(opcionPrecios, verticalLayout);
        

        // Add logic to enable/disable components based on selected radio button
        opcionPrecios.addValueChangeListener(event -> {
            this.selectedOption = event.getValue();
            firstItemLayout.setEnabled("Por Rubro y Línea".equals(selectedOption));
            secondItemLayout.setEnabled("Por Proveedores".equals(selectedOption));
            thirdItemLayout.setEnabled("Por Margen de Ganancia".equals(selectedOption));
            incrementoField.setEnabled(!"Por Margen de Ganancia".equals(selectedOption));
        });

        dialog = new Dialog();
        dialog.open();
        dialog.add(princHorizontalLayout);
        add(avatarGroup);
        add(dialog);

        // Configure CollaborationBinder
        configureBinder();
        
       
    }

    private void configureBinder() {
        binder.setSerializer(Rubros.class,
                rubros -> String.valueOf(rubros.getIdRubro()),
                id -> articulosService.findByIdRubros(Integer.parseInt(id)));

        binder.forField(rubros)
                .bind("idLinea.rubros"); // Usa el nombre de la propiedad anidada.

        binder.setSerializer(Lineas.class,
                lineas -> String.valueOf(lineas.getIdLineas()),
                idLineas -> articulosService
                        .findByIdLinea(Integer.parseInt(idLineas)));
        binder.bind(lineas, "idLinea");

        binder.setSerializer(Proveedores.class,
                proveedores -> String.valueOf(proveedores.getIdProveedor()),
                idProveedor -> articulosService
                        .findByIdProveedores(Integer.parseInt(idProveedor)));
        binder.bind(proveedor, "idProveedor");// Usa el nombre de la propiedad directamente.

        binder.forField(actualField)
                .bind("margen_utilidad"); // Usa el nombre de la propiedad directamente.

        binder.forField(nuevoField)
                .bind("nuevoMargenUtilidad"); // Asegúrate de que esta propiedad exista en la clase `Articulos`.

        binder.forField(incrementoField)
                .bind("incremento"); // Asegúrate de que esta propiedad exista en la clase `Articulos`.

        binder.addStatusChangeListener(event -> aplicarButton.setEnabled(binder.isValid()));
    }

    private void aplicarCambios(List<Articulos> articulos) throws ValidationException {
        for (Articulos articulo : articulos) {
            // Inicializa el objeto `articulos` si es null
            if (articulo == null) {
                articulo = new Articulos();
            }
                // Asigna los valores de los campos del formulario al objeto `articulos`

                rubros.setValue(articulo.getIdLinea().getRubros());
                lineas.setValue(articulo.getIdLinea());
                proveedor.setValue(articulo.getIdProveedor());
                actualField.setValue(articulo.getMargen_utilidad());
                nuevoField.setValue(articulo.getNuevoMargenUtilidad());
                incrementoField.setValue(articulo.getIncremento());

                // Escribe los datos del formulario en el objeto `articulos`
                articulo.setFecha_actPrecios(LocalDate.now());

            binder.writeBean(articulo);

        }
        articulosService.updateList(articulos); // Guarda los cambios en la base de datos
        Notification.show("Precios actualizados correctamente", 3000, Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    }

}