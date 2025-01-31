import { createMenuItems, useViewConfig } from '@vaadin/hilla-file-router/runtime.js';
import { effect, signal } from '@vaadin/hilla-react-signals';
import { AppLayout, DrawerToggle, Icon, SideNav, SideNavItem } from '@vaadin/react-components';
import { useMemo, Suspense } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';

const defaultTitle = document.title;
const documentTitleSignal = signal('');

effect(() => {
  document.title = documentTitleSignal.value;
});

// Publish for Vaadin to use
(window as any).Vaadin.documentTitleSignal = documentTitleSignal;

// Función para agrupar ítems del menú bajo un "displayTitle"
const groupMenuItems = (menuItems: readonly Readonly<{ to: string; icon?: string; title?: string; }>[] | { to: any; title: any; icon: any; }[]) => {
  const groupedItems: { displayTitle?: string; to?: any; title?: any; icon?: any; }[] = [];
  const displayedTitles = new Set();

  menuItems.forEach(({ to, title, icon }) => {
    console.log ("Processing item: ", {to, title, icon});
    let displayTitle = null;

    if (typeof title === 'string') {
      if (['Clientes'].includes(title)) {
        displayTitle = 'Clientes';
      } else if (['Proveedores'].includes(title)) {
        displayTitle = 'Proveedores';
      } else if (['Comisiones','Zonas', 'Vendedores'].includes(title)) {
        displayTitle = 'Ventas';
      } else if (['Articulos', 'Medidas', 'Presentaciones', 'Depositos', 'Rubros', 'Ubicaciones', 'Lineas'].includes(title)) {
        displayTitle = 'Artículos';
      } else if (['Alicuotas', 'Bancos'].includes(title)) {
        displayTitle = 'Finanzas';
      } else if (['Departamentos', 'Localidades'].includes(title)) {
        displayTitle = 'Recursos';
      }
    }

    // Si tenemos un displayTitle y no ha sido mostrado antes, lo agregamos
    if (displayTitle && !displayedTitles.has(displayTitle)) {
      groupedItems.push({ displayTitle });  // Añadimos el título al menú agrupado
      displayedTitles.add(displayTitle);    // Marcamos como mostrado
    }

    // Agregamos el ítem del menú (ya sea agrupado o no)
    groupedItems.push({ to, title, icon });
  });

  return groupedItems;
};

export default function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();

  // Memoizamos los ítems del menú agrupados
  const groupedMenuItems = useMemo(() => groupMenuItems(createMenuItems()), [location]);

  return (
    <AppLayout primarySection="drawer">
      <div slot="drawer" className="flex flex-col justify-between h-full p-m">
        <header className="flex flex-col gap-m">
          <span className="font-semibold text-l">VISUS</span>

          <SideNav onNavigate={({ path }) => navigate(path!)} location={location}>
            {groupedMenuItems.map((item, index) => {
              if (item.displayTitle) {
                // Renderizamos el título agrupado
                return (
                  <SideNavItem key={index}>
                    <span className="font-semibold text-l">{item.displayTitle}</span>
                  </SideNavItem>
                );
              } else {
                // Renderizamos el ítem del menú normal
                return (
                  <SideNavItem path={item.to} key={index}>
                    {item.icon ? <Icon src={item.icon} slot="prefix" /> : null}
                    {item.title === "Clientes" || item.title === "Proveedores" || item.title === "Articulos" ? "Actualización" : item.title}
                  </SideNavItem>
                );
              }
            })}
          </SideNav>
        </header>
      </div>

      <DrawerToggle slot="navbar" aria-label="Menu toggle"></DrawerToggle>
      <h1 slot="navbar" className="text-l m-0">
        {documentTitleSignal}
      </h1>

      <Suspense>
        <Outlet />
      </Suspense>
    </AppLayout>
  );
}
