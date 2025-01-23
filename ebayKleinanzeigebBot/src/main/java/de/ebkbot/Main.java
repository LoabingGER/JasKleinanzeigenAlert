package de.ebkbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Sleeper;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

// "." für klasse
// "#" für ID
// ">" für untergeordnete

public class Main {
	static ArrayList<String> links = new ArrayList<String>();
	static String ChatID = null;

	public static void main(String[] args) {

		System.out.println("Starting...");

		System.out.println(
				"::::::'##::::'###:::::'######::'########:'########:::::'###::::'##:::'##:'########:::'#######::'########:\r\n"
						+ ":::::: ##:::'## ##:::'##... ##: ##.....:: ##.... ##:::'## ##:::. ##:'##:: ##.... ##:'##.... ##:... ##..::\r\n"
						+ ":::::: ##::'##:. ##:: ##:::..:: ##::::::: ##:::: ##::'##:. ##:::. ####::: ##:::: ##: ##:::: ##:::: ##::::\r\n"
						+ ":::::: ##:'##:::. ##:. ######:: ######::: ########::'##:::. ##:::. ##:::: ########:: ##:::: ##:::: ##::::\r\n"
						+ "'##::: ##: #########::..... ##: ##...:::: ##.... ##: #########:::: ##:::: ##.... ##: ##:::: ##:::: ##::::\r\n"
						+ " ##::: ##: ##.... ##:'##::: ##: ##::::::: ##:::: ##: ##.... ##:::: ##:::: ##:::: ##: ##:::: ##:::: ##::::\r\n"
						+ ". ######:: ##:::: ##:. ######:: ########: ########:: ##:::: ##:::: ##:::: ########::. #######::::: ##::::\r\n"
						+ ":......:::..:::::..:::......:::........::........:::..:::::..:::::..:::::........::::.......::::::..:::::");

		System.out.println("Reading Data...");

		refreshData();

		System.out.println("Checking for Internet...");

		isNetAvailable();

		System.out.println("Searching for UserID...");
		File f = new File("data.db");
		if (f.exists() && !f.isDirectory()) {
			try {
				BufferedReader reader;
				reader = new BufferedReader(new FileReader("data.db"));
				String line = reader.readLine();

				ChatID = line;

				reader.close();

			} catch (Exception e) {
				// TODO: handle exception
			}

			System.out.println("UserID Found!");
		} else {
			System.out.println("No UserID found");
		}

		// MySQL.connect();
		// MySQL.update("INSERT INTO `KundenInformationen`(`Vorname`, `Nachname`,
		// `Kaufdatum`, `Ablaufdatum`, `ChatID`, `links`) VALUES
		// ('[1]','[2]','2022-02-01','2022-02-02','[5]', `6`)");
		// ArrayList<ArrayList<String>> s = MySQL.getDatabase("SELECT `ID`, `Vorname`,
		// `Nachname`, `Kaufdatum`, `Ablaufdatum`, `ChatID`, `links` WHERE
		// `KundenInformationen`");
		// System.out.println(MySQL.getDatabaseByChatID(5));

		// MySQL.disconnect();

		try {
			TelegramBotsLongPollingApplication bot = new TelegramBotsLongPollingApplication();
			bot.registerBot(TelegramConsumer_Bot.getBotToken(), new TelegramConsumer_Bot());
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TimedSearch.startThread();

	}

	public static boolean isNetAvailable() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			conn.getInputStream().close();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			return false;
		}
	}

	public static void DB(String link, String title, String price, String desc, String Ort) {

		try {
			File file = new File("data.db");
			Scanner scanner;
			scanner = new Scanner(file);
			boolean found = false;

			while (scanner.hasNextLine()) {
				final String lineFromFile = scanner.nextLine();
				if (lineFromFile.contains(title)) {
					// a match!
					found = true;
					break;

				}
			}

			if (!found) {

				try {
					File filee = new File("data.db");
					FileWriter fw = new FileWriter(filee, true);
					BufferedWriter br = new BufferedWriter(fw);

					TelegramConsumer_Bot.sendMessageToClient("kleinanzeigen.de" + link);
					TelegramConsumer_Bot.sendMessageToClient("Price: " + price);
					TimeUnit.MILLISECONDS.sleep(200);
					
					br.write("\n" + title);
					br.close();
					fw.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		try {
//			FileReader fileReader = new FileReader("data.db");
//			BufferedReader reader = new BufferedReader(fileReader);
//
//			String line = "";
//
//			while (reader.ready()) {
//				line = reader.readLine();
//				System.out.println(line);
//				System.err.println(line);
//				System.err.println(article);
//				if (line == article) {
//					System.err.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//
//				}else {
//					if (line != null || line != "0") {
//						File file = new File("data.db");
//						FileWriter fw = new FileWriter(file, true);
//						BufferedWriter br = new BufferedWriter(fw);
//
//						br.write("\n" + line);
//						br.close();
//						fw.close();
//					}
//					
//				}
//				Thread.sleep(2000);
//			}
//
//			// TODO Auto-generated catch block
//
//			reader.close();
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public static void addLink(String link) {

		File file = new File("links.dat");

		try {

			FileWriter fr = new FileWriter(file, true);
			BufferedWriter br = new BufferedWriter(fr);

			br.write(link + "\n");
			br.close();
			fr.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refreshData();
	}

	public static void refreshData() {
		links.removeAll(links);

		try {
			BufferedReader reader;
			reader = new BufferedReader(new FileReader("links.dat"));
			String line = reader.readLine();

			while (line != null) {

				links.add(line);
				// read next line
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static void Search(String URL) {
		// Die URL zum Scrapen
		// String URL = "https://www.kleinanzeigen.de/s-preis:199:250/gtx1080/k0";

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless"); // Aktiviert den Headless-Modus
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-gpu");

		// Creating webdriver instance
		WebDriver driver = new ChromeDriver(options);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.get(URL);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		// System.out.println(driver.getPageSource());
		

		int z = 0;
		
		while (true) {
			
			List<WebElement> articles = driver.findElements(By.className("aditem"));
			z = articles.size();
			
			for (WebElement a : articles) {

				String title = a.findElement(By.className("text-module-begin")).getText();
				String desc = a.findElement(By.className("aditem-main--middle--description")).getText();
				String price = a.findElement(By.className("aditem-main--middle--price-shipping--price")).getText();
				String link = a.getAttribute("data-href");
				String Ort = a.findElement(By.className("aditem-main--top--left")).getText();

				System.out.println("==============================================");
				System.out.println("https://kleinanzeigen.de" + link);
				System.out.println("----------------------------------------------");
				System.out.println(title);
				System.out.println("----------------------------------------------");
				System.out.println(Ort);
				System.out.println("----------------------------------------------");
				System.out.println(desc);
				System.out.println("----------------------------------------------");
				System.out.println(price);
				System.out.println("==============================================");
				
				DB(link, title, price, desc, Ort);
				
			}
			System.out.println(z);
			if(z <= 24) {
				break;
			}
			z = 0;
			String next = driver.findElement(By.className("pagination-next")).getAttribute("href");
			driver.get(next);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			
			
			
			
		}

		// List<WebElement> l = articles.findElements(By.tagName("a"));

		// System.out.println(Arrays.toString(l.toArray()).toString());

		// driver.quit();

	}

}
