package sandalia.apps;

import java.io.IOException;

import org.testng.annotations.Test;

import sandalia.apps.config.Init;
import sandalia.apps.pages.AdminPage;


public class AppTest extends Init{
	@Test(priority = 1)
	public void listActiveIntegrations() {
		AdminPage.listActiveIntegrations();
	}
	@Test(priority = 2)
	public void listAllIntegrations() {
		AdminPage.listAllIntegrations();
	}
	
	@Test(priority = 3)
	public void attemptToListIntegrationsWhenEnableIntegrationsIsFalse() throws InterruptedException {
		AdminPage.integrationsWhenEnableIntegrationsIsFalse();
	}
	@Test(priority = 4)
	public void loadingSpinnerConfirmationMsg() throws IOException {
		AdminPage.loadingSpinnerConfirmationMsg();
	}

}
