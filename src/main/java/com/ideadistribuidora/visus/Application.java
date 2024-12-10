package com.ideadistribuidora.visus;

import com.ideadistribuidora.visus.data.repositories.AlicuotasRepository;
import com.ideadistribuidora.visus.data.repositories.ArticulosRepository;
import com.ideadistribuidora.visus.data.repositories.BancosRepository;
import com.ideadistribuidora.visus.data.repositories.ClientesBancosRepository;
import com.ideadistribuidora.visus.data.repositories.ClientesRepository;
import com.ideadistribuidora.visus.data.repositories.DepartamentosRepository;
import com.ideadistribuidora.visus.data.repositories.DepositosRepository;
import com.ideadistribuidora.visus.data.repositories.DocumentosRepository;
import com.ideadistribuidora.visus.data.repositories.DomiciliosRepository;
import com.ideadistribuidora.visus.data.repositories.LineasRepository;
import com.ideadistribuidora.visus.data.repositories.LocalidadesRepository;
import com.ideadistribuidora.visus.data.repositories.MedidasRepository;
import com.ideadistribuidora.visus.data.repositories.PresentacionesRepository;
import com.ideadistribuidora.visus.data.repositories.ProveedoresBancosRepository;
import com.ideadistribuidora.visus.data.repositories.ProveedoresRepository;
import com.ideadistribuidora.visus.data.repositories.ProvinciasRepository;
import com.ideadistribuidora.visus.data.repositories.RubrosRepository;
import com.ideadistribuidora.visus.data.repositories.UbicacionesRepository;
import com.vaadin.collaborationengine.CollaborationEngineConfiguration;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;

import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.context.annotation.Bean;

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
public class Application implements AppShellConfigurator {

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
            UbicacionesRepository ubicacionesRepository) {
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
                        || ubicacionesRepository.count() == 0L) {
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
        // String path = "/opt/licenses";
        configuration.setDataDir(path);
        return configuration;
    }
}
