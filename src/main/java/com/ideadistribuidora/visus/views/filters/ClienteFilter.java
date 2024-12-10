package com.ideadistribuidora.visus.views.filters;

import com.ideadistribuidora.visus.data.Clientes;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

public class ClienteFilter {
    private final GridListDataView<Clientes> dataView;

    private String nombreCliente;
    private String nombreFantasia;
    private String numeroDeDocumento;

    public ClienteFilter(GridListDataView<Clientes> dataView) {
        this.dataView = dataView;
        this.dataView.addFilter(this::test);
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
        this.dataView.refreshAll();
    }

    public void setNombreFantasia(String nombreFantasia) {
        this.nombreFantasia = nombreFantasia;
        this.dataView.refreshAll();
    }

    public void setNumeroDeDocumento(String numeroDeDocumento) {
        this.numeroDeDocumento = numeroDeDocumento;
        this.dataView.refreshAll();
    }

    public boolean test(Clientes clientes) {
        boolean matchesNombreClientes = matches(clientes.getNombreCliente(), nombreCliente);
        boolean matchesNombreFantasia = matches(clientes.getNombreFantasia(), nombreFantasia);
        boolean matchesNumDoc = matches(String.valueOf(clientes.getNumeroDeDocumento()), numeroDeDocumento);

        return matchesNombreClientes && matchesNombreFantasia && matchesNumDoc;
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
