package com.ideadistribuidora.visus;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.ideadistribuidora.visus.data.repositories.AlicuotasRepository;
import com.ideadistribuidora.visus.data.repositories.ArticulosRepository;
import com.ideadistribuidora.visus.data.repositories.BancosRepository;
import com.ideadistribuidora.visus.data.repositories.ClientesBancosRepository;
import com.ideadistribuidora.visus.data.repositories.ClientesRepository;
import com.ideadistribuidora.visus.data.repositories.CoeficientesRepository;
import com.ideadistribuidora.visus.data.repositories.ComisionesRepository;
import com.ideadistribuidora.visus.data.repositories.ComisionesTramosRepository;
import com.ideadistribuidora.visus.data.repositories.DepartamentosRepository;
import com.ideadistribuidora.visus.data.repositories.DepositosRepository;
import com.ideadistribuidora.visus.data.repositories.DocumentosRepository;
import com.ideadistribuidora.visus.data.repositories.DomiciliosRepository;
import com.ideadistribuidora.visus.data.repositories.LineasRepository;
import com.ideadistribuidora.visus.data.repositories.ListasPorcentualesRepository;
import com.ideadistribuidora.visus.data.repositories.ListasRepository;
import com.ideadistribuidora.visus.data.repositories.LocalidadesRepository;
import com.ideadistribuidora.visus.data.repositories.MedidasRepository;
import com.ideadistribuidora.visus.data.repositories.PorcentualesRepository;
import com.ideadistribuidora.visus.data.repositories.PresentacionesRepository;
import com.ideadistribuidora.visus.data.repositories.ProveedoresBancosRepository;
import com.ideadistribuidora.visus.data.repositories.ProveedoresRepository;
import com.ideadistribuidora.visus.data.repositories.ProvinciasRepository;
import com.ideadistribuidora.visus.data.repositories.RubrosRepository;
import com.ideadistribuidora.visus.data.repositories.TransportistasBancosRepository;
import com.ideadistribuidora.visus.data.repositories.TransportistasRepository;
import com.ideadistribuidora.visus.data.repositories.UbicacionesRepository;
import com.ideadistribuidora.visus.data.repositories.VendedoresRepository;
import com.ideadistribuidora.visus.data.repositories.ZonasRepository;
import com.vaadin.collaborationengine.CollaborationEngineConfiguration;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "visus")
@Push
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
            SqlInitializationProperties properties, ProveedoresRepository proveedoresRepository,
            ClientesRepository clientesRepository, DepartamentosRepository departamentosRepository,
            ProvinciasRepository provinciasRepository, LocalidadesRepository localidadesRepository,
            BancosRepository bancosRepository, DomiciliosRepository domiciliosRepository,
            DocumentosRepository documentosRepository, ClientesBancosRepository clientesBancosRepository,
            AlicuotasRepository alicuotasRepository, ArticulosRepository articulosRepository,
            DepositosRepository depositosRepository, LineasRepository lineasRepository,
            MedidasRepository medidasRepository, PresentacionesRepository presentacionesRepository,
            ProveedoresBancosRepository proveedoresBancosRepository, RubrosRepository rubrosRepository,
            UbicacionesRepository ubicacionesRepository, ComisionesRepository comisionesRepository,
            VendedoresRepository vendedoresRepository, ZonasRepository zonasRepository,
            CoeficientesRepository coeficientesRepository, ComisionesTramosRepository comisionesTramosRepository,
            TransportistasBancosRepository transportistasBancosRepository, TransportistasRepository transportistasRepository,
            ListasPorcentualesRepository listasPorcentualesRepository, ListasRepository listasRepository, PorcentualesRepository porcentualesRepository) { 
        // This bean ensures the database is only initialized when empty
        return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties) {
            @Override
            public boolean initializeDatabase() {
            if (proveedoresRepository.count() == 0L || clientesRepository.count() == 0L
                || departamentosRepository.count() == 0L
                || provinciasRepository.count() == 0L
                || localidadesRepository.count() == 0L
                || bancosRepository.count() == 0L
                || domiciliosRepository.count() == 0L
                || documentosRepository.count() == 0L
                || clientesBancosRepository.count() == 0L
                || depositosRepository.count() == 0L
                || lineasRepository.count() == 0L
                || medidasRepository.count() == 0L
                || presentacionesRepository.count() == 0L
                || proveedoresBancosRepository.count() == 0L
                || rubrosRepository.count() == 0L
                || ubicacionesRepository.count() == 0L
                || comisionesRepository.count() == 0L
                || vendedoresRepository.count() == 0L
                || zonasRepository.count() == 0L
                || coeficientesRepository.count() == 0L
                || comisionesTramosRepository.count() == 0L
                || transportistasBancosRepository.count() == 0L
                || transportistasRepository.count() == 0L
                || listasPorcentualesRepository.count() == 0L 
                || listasRepository.count() == 0L
                || porcentualesRepository.count() == 0L) {
                return super.initializeDatabase();
            }
            return false;
            }
        };
        }

        @Bean
        public CollaborationEngineConfiguration ceConfigBean() {
        CollaborationEngineConfiguration configuration = new CollaborationEngineConfiguration(
                licenseEvent -> {
                    // See <<ce.production.license-events>>
                });
        String path = "C:\\vaadin\\licenses";
        //String path = "/opt/licenses";
        configuration.setDataDir(path);
        return configuration;
    }
}
