package com.ideadistribuidora.visus.views.filters;

import com.ideadistribuidora.visus.data.Proveedores;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

public class ProveedorFilter {
    private final GridListDataView<Proveedores> dataView;

    private String nombreFantasia;
    private String nombreReal;

    public ProveedorFilter(GridListDataView<Proveedores> dataView) {
        this.dataView = dataView;
        this.dataView.addFilter(this::test);
    }

    public void setNombreFantasia(String nombreFantasia) {
        this.nombreFantasia = nombreFantasia;
        this.dataView.refreshAll();
    }

    public void setNombreReal(String nombreReal) {
        this.nombreReal = nombreReal;
        this.dataView.refreshAll();
    }

    public boolean test(Proveedores proveedores) {
        boolean matchesNombreClientes = matches(proveedores.getNombreReal(), nombreReal);
        boolean matchesNombreFantasia = matches(proveedores.getNombreFantasia(), nombreFantasia);

        return matchesNombreClientes && matchesNombreFantasia;
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }

}
