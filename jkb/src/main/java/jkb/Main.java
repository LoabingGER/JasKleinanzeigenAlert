package jkb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
   static ArrayList<String> links = new ArrayList();
   static String ChatID = null;

   public static void main(String[] args) {
      System.out.println("Starting...");
      System.out.println("::::::'##::::'###:::::'######::'########:'########:::::'###::::'##:::'##:'########:::'#######::'########:\r\n:::::: ##:::'## ##:::'##... ##: ##.....:: ##.... ##:::'## ##:::. ##:'##:: ##.... ##:'##.... ##:... ##..::\r\n:::::: ##::'##:. ##:: ##:::..:: ##::::::: ##:::: ##::'##:. ##:::. ####::: ##:::: ##: ##:::: ##:::: ##::::\r\n:::::: ##:'##:::. ##:. ######:: ######::: ########::'##:::. ##:::. ##:::: ########:: ##:::: ##:::: ##::::\r\n'##::: ##: #########::..... ##: ##...:::: ##.... ##: #########:::: ##:::: ##.... ##: ##:::: ##:::: ##::::\r\n ##::: ##: ##.... ##:'##::: ##: ##::::::: ##:::: ##: ##.... ##:::: ##:::: ##:::: ##: ##:::: ##:::: ##::::\r\n. ######:: ##:::: ##:. ######:: ########: ########:: ##:::: ##:::: ##:::: ########::. #######::::: ##::::\r\n:......:::..:::::..:::......:::........::........:::..:::::..:::::..:::::........::::.......::::::..:::::");
      System.out.println("Reading Data...");
      refreshData();
      System.out.println("Checking for Internet...");
      isNetAvailable();
      System.out.println("Searching for UserID...");
      File f = new File("data.db");
      if (f.exists() && !f.isDirectory()) {
         try {
            BufferedReader reader = new BufferedReader(new FileReader("data.db"));
            String line = reader.readLine();
            ChatID = line;
            reader.close();
         } catch (Exception var5) {
         }

         System.out.println("UserID Found!");
      } else {
         System.out.println("No UserID found");
      }

      try {
         TelegramBotsLongPollingApplication bot = new TelegramBotsLongPollingApplication();
         bot.registerBot(TelegramConsumer_Bot.getBotToken(), new TelegramConsumer_Bot());
      } catch (TelegramApiException var4) {
         var4.printStackTrace();
      }

      TimedSearch.startThread();
   }

   public static boolean isNetAvailable() {
      try {
         URL url = new URL("http://www.google.com");
         URLConnection conn = url.openConnection();
         conn.connect();
         conn.getInputStream().close();
         return true;
      } catch (MalformedURLException var2) {
         throw new RuntimeException(var2);
      } catch (IOException var3) {
         return false;
      }
   }

   public static void DB(String link, String title, String price, String desc, String Ort) {
      try {
         File file = new File("data.db");
         Scanner scanner = new Scanner(file);
         boolean found = false;

         while(scanner.hasNextLine()) {
            String lineFromFile = scanner.nextLine();
            if (lineFromFile.contains(title)) {
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
               TimeUnit.MILLISECONDS.sleep(200L);
               br.write("\n" + title);
               br.close();
               fw.close();
            } catch (Exception var11) {
               var11.printStackTrace();
            }
         }
      } catch (FileNotFoundException var12) {
         var12.printStackTrace();
      }

   }

   public static void addLink(String link) {
      File file = new File("links.dat");

      try {
         FileWriter fr = new FileWriter(file, true);
         BufferedWriter br = new BufferedWriter(fr);
         br.write(link + "\n");
         br.close();
         fr.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      refreshData();
   }

   public static void refreshData() {
      links.removeAll(links);

      try {
         BufferedReader reader = new BufferedReader(new FileReader("links.dat"));

         for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            links.add(line);
         }

         reader.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static void Search(String URL) {
      ChromeOptions options = new ChromeOptions();
      options.addArguments(new String[]{"--headless"});
      options.addArguments(new String[]{"--no-sandbox"});
      options.addArguments(new String[]{"--disable-dev-shm-usage"});
      options.addArguments(new String[]{"--disable-gpu"});
      WebDriver driver = new ChromeDriver(options);
      driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5L));
      driver.get(URL);
      driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5L));
      int z = 0;

      while(true) {
         List<WebElement> articles = driver.findElements(By.className("aditem"));
         z = articles.size();
         Iterator var6 = articles.iterator();

         while(var6.hasNext()) {
            WebElement a = (WebElement)var6.next();
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
         if (z <= 24) {
            return;
         }

         z = 0;
         String next = driver.findElement(By.className("pagination-next")).getAttribute("href");
         driver.get(next);
         driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5L));
      }
   }
}
