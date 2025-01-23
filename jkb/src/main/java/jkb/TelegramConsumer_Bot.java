package jkb;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramConsumer_Bot implements LongPollingSingleThreadUpdateConsumer {
   private TelegramClient telegramClient = new OkHttpTelegramClient(getBotToken());

   public static void Run() {
   }

   public static void sendMessageToID(String ChatID, String msg) {
      String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
      urlString = String.format(urlString, getBotToken(), ChatID, msg);

      try {
         URL url = new URL(urlString);
         URLConnection conn = url.openConnection();
         new BufferedInputStream(conn.getInputStream());
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public static void sendMessageToClient(String msg) {
      try {
         String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
         BufferedReader brTest = new BufferedReader(new FileReader("data.db"));
         String ChatID = brTest.readLine();
         urlString = String.format(urlString, getBotToken(), ChatID, msg);
         URL url = new URL(urlString);
         URLConnection conn = url.openConnection();
         new BufferedInputStream(conn.getInputStream());
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public void consume(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         File f = new File("data.db");
         if (!f.exists() && !f.isDirectory()) {
            try {
               FileWriter fr = new FileWriter(f, true);
               BufferedWriter br = new BufferedWriter(fr);
               br.write(update.getMessage().getChatId().toString());
               br.close();
               fr.close();
            } catch (Exception var13) {
            }
         }

         String command = update.getMessage().getText();

         try {
            SendMessage response;
            SendMessage response1;
            String cmd;
            if (command.equals("/add")) {
               response1 = new SendMessage(update.getMessage().getChatId().toString(), getPrefix() + "Bitte gehe auf \"https://kleinanzeigen.de\", suche nach einem begriff, filter diese Suche mit der " + "Filter funktion und schreibe den link in den Chat.\n\n Beispiel: \" /add https://www.kleinanzeigen.de/s-preis:5:10/kochtopf/k0\"");
               this.telegramClient.execute(response1);
            } else if (command.startsWith("/add https://www.kleinanzeigen.")) {
               cmd = command.substring(5);
               if (Main.links.contains(cmd)) {
                  response1 = new SendMessage(update.getMessage().getChatId().toString(), "Error\n\nLink \"" + cmd + "\" ist bereits Hinzugefügt!");
                  this.telegramClient.execute(response1);
               } else {
                  Main.addLink(cmd);
                  response1 = new SendMessage(update.getMessage().getChatId().toString(), getPrefix() + "Link \"" + cmd + "\" Hinzugefügt!");
                  this.telegramClient.execute(response1);
               }
            }

            String line;
            int number;
            if (command.startsWith("/delete")) {
               if (!command.contains(" ")) {
                  response1 = new SendMessage(update.getMessage().getChatId().toString(), getPrefix() + "Gebe /delete \"zahl\" ein die du aus der liste von /list bekommst.");
                  this.telegramClient.execute(response1);
               } else {
                  cmd = command.substring(8);
                  if (cmd.matches("\\d+")) {
                     number = Integer.valueOf(cmd);

                     try {
                        ArrayList<String> arl = new ArrayList();
                        BufferedReader reader = new BufferedReader(new FileReader("links.dat"));

                        for(line = reader.readLine(); line != null; line = reader.readLine()) {
                           arl.add(line);
                        }

                        reader.close();
                        String removed = (String)arl.get(number - 1);
                        arl.remove(number - 1);
                        FileWriter writer = new FileWriter("links.dat");
                        Iterator var12 = arl.iterator();

                        while(var12.hasNext()) {
                           String str = (String)var12.next();
                           writer.write(str + System.lineSeparator());
                        }

                        writer.close();
                        SendMessage response11 = new SendMessage(update.getMessage().getChatId().toString(), getPrefix() + number + "." + removed + " wurde gelöscht.");
                        this.telegramClient.execute(response11);
                     } catch (IOException var14) {
                        var14.printStackTrace();
                     }

                     Main.refreshData();
                  } else {
                     response1 = new SendMessage(update.getMessage().getChatId().toString(), getPrefix() + "Gebe /delete \"zahl\" ein die du aus der liste von /list bekommst.");
                     this.telegramClient.execute(response1);
                  }
               }
            }

            if (command.startsWith("/list")) {
               ArrayList<String> s = new ArrayList();

               for(number = 1; number < Main.links.size() + 1; ++number) {
                  s.add(number + "." + (String)Main.links.get(number - 1) + "\n");
               }

               String a = s.toString();
               String b = a.replace(",", "");
               String c = b.replace("[", "");
               line = c.replace("]", "");
               SendMessage response11 = new SendMessage(update.getMessage().getChatId().toString(), getPrefix() + "Liste deiner links:\n\n" + line);
               this.telegramClient.execute(response11);
            }

            System.out.println(command);
            new SendMessage(update.getMessage().getChatId().toString(), update.getMessage().getText().toString());
         } catch (TelegramApiException var15) {
            var15.printStackTrace();
         }
      }

   }

   public static String getBotName() {
      return "JasEbay_Bot";
   }

   public static String getPrefix() {
      return "JasEbayBot\n\n";
   }

   public static String getBotToken() {
      String token = null;

      try {
         BufferedReader reader = new BufferedReader(new FileReader("BOTTOKEN.txt"));
         token = reader.readLine();
         reader.close();
      } catch (Exception var2) {
      }

      return token;
   }
}