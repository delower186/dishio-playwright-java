package sandalia.apps.pages;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;

import sandalia.apps.config.Init;
import sandalia.apps.helpers.Helper;

public class AdminPage extends Init{
	
	// List all integrations for a valid brand (with/without filters) when enableIntegrations is true.
	public static String brand = "(//div[contains(@class,'flex flex-row items-center gap-4')])[1]";
	public static String name = "";
	public static String integrationsBTN = "//button[@id='underline-button-integrations']";
	public static String enabledIntegrations = "//div[contains(@data-sentry-component,'IntegrationCardWrapper')]";
	
	public static String enableIntegrationRadioBTN = "//label[contains(@for,'enableIntegrations')]";
	public static String saveBTN = "//button[@type='submit']";
	
	// List integrations for a brand with no integrations.
	public static String brandWithNoIntegration = "(//div[contains(@class,'flex flex-row items-center gap-4')])[3]";
	public static String allIntegrations = "(//div[contains(@class,'flex flex-col border border-gray-200 rounded-2xl overflow-hidden transition-all duration-200 hover:border-gray-300 h-auto w-full max-w-full sm:w-[320px] sm:h-[320px]')])";
	public static String brandManager = "(//img[@class='my-3 cursor-pointer'])[1]";
	
	// UI: Loading spinner, error message, empty state, and correct gating by enableIntegrations
	public static String confirmationMsg = "//div[contains(text(),'Restaurant profile updated successfully')]";
	public static String preloader = "//link[@rel='preload']";

	public static void listActiveIntegrations() {
		String brandName = page.locator(brand).textContent();
		page.locator(brand).click();
		
		Locator radioButton = page.locator(enableIntegrationRadioBTN);

		if (radioButton.isChecked()) {
		  System.out.println("Radio button is already selected. Skipping.....");
		} else {
		  System.out.println("Radio button is not selected. Let's select it...");
		  radioButton.click();
		  page.locator(saveBTN).click();
		  System.out.println("Radio button is selected & saved!");
		}
		
		page.locator(integrationsBTN).click();
		page.waitForSelector(enabledIntegrations);
		Locator items = page.locator(enabledIntegrations);
		
		List<String> titles = new ArrayList<String>();
		
	      int count = items.count();
	      for (int i = 0; i < count; i++) {
	        String text = items.nth(i).locator("h4").textContent();
	        titles.add(text);
	        System.out.println("Integration Title " + (i + 1) + ": " + text);
	      }
	      
	      Assert.assertEquals(count, 4);
	      
		Helper.progressInfos.add("Brand: '"+ brandName+"', Total active integrations: " + count +" and They are '" + String.join("', '", titles) + "'");
	}
	
	public static void listAllIntegrations() {
		
		page.locator(brandManager).click();
		
		String brandName = page.locator(brandWithNoIntegration).textContent();
		page.locator(brandWithNoIntegration).click();
		
		Locator radioButton = page.locator(enableIntegrationRadioBTN);

		if (radioButton.isChecked()) {
		  System.out.println("Radio button is already selected. Skipping.....");
		} else {
		  System.out.println("Radio button is not selected. Let's select it...");
		  radioButton.click();
		  page.locator(saveBTN).click();
		  System.out.println("Radio button is selected & saved!");
		}
		
		page.locator(integrationsBTN).click();
		page.waitForSelector(allIntegrations);
		Locator items = page.locator(allIntegrations);
		
		List<String> titles = new ArrayList<String>();
		
	      int count = items.count();
	      for (int i = 0; i < count; i++) {
	        String text = items.nth(i).locator("h4").textContent();
	        titles.add(text);
	        System.out.println("Integration Title " + (i + 1) + ": " + text);
	      }
	      
	      Assert.assertEquals(count, 23);
	      
		Helper.progressInfos.add("Brand: '"+ brandName+"', Total integrations: " + count +" and They are '" + String.join("', '", titles) + "'");
	}
	
	public static void integrationsWhenEnableIntegrationsIsFalse() throws InterruptedException {
		
		page.locator(brandManager).click();
		
		String brandName = page.locator(brand).textContent();
		page.locator(brand).click();
		
		Locator radioButton = page.locator(enableIntegrationRadioBTN);

		if (radioButton.isChecked()) {
		  System.out.println("Radio button is checked. Let's uncheck it...");
		  radioButton.click();
		  page.locator(saveBTN).click();
		  System.out.println("Radio button is unchecked & saved!");
		} else {
		  System.out.println("Radio button is already unchecked. Skipping...");
		}
		
		Helper.wait(3);
		Locator integrationMenuButton = page.locator(integrationsBTN);
		
		if(!integrationMenuButton.isVisible()) {
			System.out.println("button invisiable");
			Helper.scrollToTop(page);
		}
		
		try {
			
			page.locator(integrationsBTN).click();
			page.waitForSelector(allIntegrations);
			Locator items = page.locator(allIntegrations);
			
			List<String> titles = new ArrayList<String>();
			
		      int count = items.count();
		      
		      if(count > 0) {
			      for (int i = 0; i < count; i++) {
				        String text = items.nth(i).locator("h4").textContent();
				        titles.add(text);
				        System.out.println("Integration Title " + (i + 1) + ": " + text);
				  }
		      }
		      
		      Assert.assertEquals(count, 23);
		      
			Helper.progressInfos.add("Brand: '"+ brandName+"', Total integrations: " + count +" and They are '" + String.join("', '", titles) + "'");
		}catch(Exception e) {
			Helper.progressInfos.add("integration menu button 'integrationsBTN' is not found");
		}
		
	}
	
	public static void loadingSpinnerConfirmationMsg() throws IOException {
		// browser video context
		BrowserContext browserContextVideo = browser.newContext(
        	    new NewContextOptions()
        	        .setRecordVideoDir(Paths.get("reports/"))
        	        .setRecordVideoSize(1280, 720)
        	);
        
		Page recordPage = browserContextVideo.newPage();
		recordPage.navigate(baseURL);
		AccessControl.login(recordPage);
		
		recordPage.locator(brandManager).click();
		recordPage.locator(brand).click();
		
		recordPage.waitForSelector(enableIntegrationRadioBTN);
		Locator radioButton = recordPage.locator(enableIntegrationRadioBTN);

		if (radioButton.isChecked()) {
		  System.out.println("Radio button is already selected. Skipping.....");
		} else {
		  System.out.println("Radio button is not selected. Let's select it...");
		  radioButton.click();
		  recordPage.locator(saveBTN).click();
		  System.out.println("Radio button is selected & saved!");
		  recordPage.waitForSelector(confirmationMsg);
		}
		
		recordPage.waitForSelector(integrationsBTN);
		recordPage.locator(integrationsBTN).click();
		recordPage.waitForSelector(enabledIntegrations);
		Locator items = recordPage.locator(enabledIntegrations);
		
		List<String> titles = new ArrayList<String>();
		
	      int count = items.count();
	      for (int i = 0; i < count; i++) {
	        String text = items.nth(i).locator("h4").textContent();
	        titles.add(text);
	        System.out.println("Integration Title " + (i + 1) + ": " + text);
	      }
	      
	      Assert.assertEquals(count, 4);
	      

		browserContextVideo.close();
		System.out.println(recordPage.video().path());
		
		Helper.progressInfos.add("UI: Mapping UI, error display, disables action on loading etc: " + "<video width='600' controls autoplay><source src='"+ recordPage.video().path() +"' type='video/webm'>Your browser does not support HTML video.</video>");
		
		
	}
	
}
