package com.ideadistribuidora.visus.views.formasDePago;

import com.ideadistribuidora.visus.data.Coeficientes;
import com.ideadistribuidora.visus.services.FormasDePagoService;
import com.vaadin.collaborationengine.CollaborationEngine;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FormasDePagoViewTest {

    private FormasDePagoView formasDePagoView;
    private FormasDePagoService formasDePagoService;

    @BeforeEach
    public void setup() {
        // Mock VaadinService and VaadinSession
        VaadinService vaadinService = Mockito.mock(VaadinService.class);
        VaadinSession vaadinSession = Mockito.mock(VaadinSession.class);
        UI ui = Mockito.mock(UI.class);

        Mockito.when(vaadinService.getCurrentRequest()).thenReturn(null);
        Mockito.when(vaadinSession.getService()).thenReturn(vaadinService);
        VaadinSession.setCurrent(vaadinSession);
        UI.setCurrent(ui);

        // Mock CollaborationEngine
        CollaborationEngine collaborationEngine = Mockito.mock(CollaborationEngine.class);
        Mockito.when(collaborationEngine.getInstance()).thenReturn(collaborationEngine);

        formasDePagoService = Mockito.mock(FormasDePagoService.class);
        formasDePagoView = new FormasDePagoView(formasDePagoService);
    }

    @Test
    public void testCoeficienteDescComboBox() {
        ComboBox<Coeficientes> coeficienteDesc = formasDePagoView.getCoeficienteDesc();
        BigDecimalField coeficiente = formasDePagoView.getCoeficiente();
        TextField cuotas = formasDePagoView.getCuotas();

        Coeficientes coef1 = new Coeficientes();
        coef1.setIdCoeficiente(1);
        coef1.setDescripcion("Coeficiente 1");
        coef1.setCoeficiente(new BigDecimal("1.5"));
        coef1.setCuotas((short) 12);

        Coeficientes coef2 = new Coeficientes();
        coef2.setIdCoeficiente(2);
        coef2.setDescripcion("Coeficiente 2");
        coef2.setCoeficiente(new BigDecimal("2.0"));
        coef2.setCuotas((short) 24);

        List<Coeficientes> coeficientesList = Arrays.asList(coef1, coef2);
        Mockito.when(formasDePagoService.getAllCoefiencientes()).thenReturn(coeficientesList);

        coeficienteDesc.setItems(coeficientesList);

        // Simulate selecting an item from the ComboBox
        coeficienteDesc.setValue(coef1);

        // Verify that the corresponding fields are updated
        assertEquals(coef1.getCoeficiente(), coeficiente.getValue());
        assertEquals(String.valueOf(coef1.getCuotas()), cuotas.getValue());
    }
}
