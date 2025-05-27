package com.ideadistribuidora.visus.views.Pagos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ideadistribuidora.visus.data.Pagos;
import com.ideadistribuidora.visus.data.enums.EstadoPagoEnum;
import com.ideadistribuidora.visus.services.PagosService;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Pagos")
@Menu(icon = "line-awesome/svg/columns-solid.svg", order = 16)
@Route(value = "17/:pagosID?/:action?(edit)")
public class PagosView extends Div {
    //private CollaborationBinder<Pagos> binder;
    private CollaborationAvatarGroup avatarGroup;
    private UserInfo userInfo;
    private TextField searchCliente;
    private final Grid<Pagos> gridPagos = new Grid<>(Pagos.class, false);
    private Button aplicar;
    private Button cancelar;
    private final PagosService pagosService;
    private GridListDataView<Pagos> dataView;
    //private Pagos pagos;
    private List<Pagos> listEstPagos = new ArrayList<>();

    public PagosView(PagosService pagosService) {
        this.pagosService = pagosService;
        addClassNames("pagos-view");
        userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        //this.binder = new CollaborationBinder<>(Pagos.class, userInfo);

        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "visible");

        Div search = new Div();
        searchCliente = new TextField();
        searchCliente.setPlaceholder("Buscar Cliente");
        searchCliente.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchCliente.setValueChangeMode(ValueChangeMode.EAGER);
        searchCliente.addValueChangeListener(e -> dataView.refreshAll());
        search.add(searchCliente);
        gridPagos.addColumn(createPagosRenderer())
                .setHeader("Pedido").setAutoWidth(true);
        gridPagos.addColumn(pagos -> pagos.getIdPedido().getIdCliente().getNombreCliente())
                .setHeader("Cliente").setAutoWidth(true);
        gridPagos.addColumn(pagos -> pagos.getIdPedido().getEstadoPedido().getEstadoPedido())
                .setHeader("Status").setAutoWidth(true);
        gridPagos.addColumn(pagos -> {
            ComboBox<EstadoPagoEnum> estadoPagoCombo = new ComboBox<>();
            estadoPagoCombo.setItems(EstadoPagoEnum.values());
            estadoPagoCombo.setItemLabelGenerator(EstadoPagoEnum::getEstadoPago);
            estadoPagoCombo.setValue(pagos.getEstado());
            estadoPagoCombo.addValueChangeListener(event -> {
                pagos.setEstado(estadoPagoCombo.getValue());
                listEstPagos.add(pagos);
                aplicar.setEnabled(true);
            });
            return estadoPagoCombo;
        })
                .setHeader("Pago").setAutoWidth(true);

        dataView = gridPagos.setItems(this.pagosService.pagoList());

        searchFilter(dataView);
        aplicar = new Button("Aplicar", event -> {
            for (Pagos pagos : listEstPagos) {
                pagosService.save(pagos);
            }
            listEstPagos.clear();
        });
        aplicar.setEnabled(false);
        cancelar = new Button("Cancelar", event -> {
            listEstPagos.clear();
            dataView.refreshAll();
            aplicar.setEnabled(false);
        });
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.add(cancelar, aplicar);

        VerticalLayout principalLayout = new VerticalLayout();
        principalLayout.add(search, gridPagos, buttonLayout);
        principalLayout.setSizeFull();
        add(principalLayout);
    }

    private void searchFilter(GridListDataView<Pagos> dataView2) {
        dataView2.addFilter(pagoSearh -> {
            String searchTerm = searchCliente.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesFullName = matchesTerm(pagoSearh.getIdPedido().getIdCliente().getNombreCliente(),
                    searchTerm);

            return matchesFullName;
        });
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private LitRenderer<Pagos> createPagosRenderer() {
        return LitRenderer.<Pagos>of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "  <vaadin-avatar name=\"${item.fullName}\"></vaadin-avatar>"
                        + "  <span> ${item.fullName} </span>"
                        + "</vaadin-horizontal-layout>")

                .withProperty("fullName", Pagos::getIdPago);
    }



}
