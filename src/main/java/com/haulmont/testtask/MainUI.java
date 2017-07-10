package main.java.com.haulmont.testtask;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import main.java.com.haulmont.testtask.pages.ClientsPage;
import main.java.com.haulmont.testtask.pages.MechanicsPage;
import main.java.com.haulmont.testtask.pages.OrdersPage;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    private Navigator navigator;

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        Panel contentPanel = new Panel();
        contentPanel.setSizeUndefined();

        navigator = new Navigator(this, contentPanel);
        getNavigator().addView(ClientsPage.NAME, ClientsPage.class);
        getNavigator().addView(MechanicsPage.NAME, MechanicsPage.class);
        getNavigator().addView(OrdersPage.NAME, OrdersPage.class);

        MenuBar.Command clients = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getNavigator().navigateTo(ClientsPage.NAME);
            }
        };

        MenuBar.Command mechanics = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getNavigator().navigateTo(MechanicsPage.NAME);
            }
        };

        MenuBar.Command orders = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getNavigator().navigateTo(OrdersPage.NAME);
            }
        };

        MenuBar mainMenu = new MenuBar();
        mainMenu.addItem("Clients", clients);
        mainMenu.addItem("Mechanics", mechanics);
        mainMenu.addItem("Orders", orders);

        layout.addComponent(mainMenu);
        layout.addComponent(contentPanel);
        getNavigator().navigateTo(ClientsPage.NAME);
    }

    public void refreshClients(){
        navigator.navigateTo(ClientsPage.NAME);
    }

    public void refreshMechanics() {
        navigator.navigateTo(MechanicsPage.NAME);
    }

    public void refreshOrders() {
        navigator.navigateTo(OrdersPage.NAME);
    }
}