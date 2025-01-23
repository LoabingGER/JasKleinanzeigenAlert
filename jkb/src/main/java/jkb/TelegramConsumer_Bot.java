package jkb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

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

		// Add Telegram token (given Token is fake)

		// Add chatId (given chatId is fake)

		urlString = String.format(urlString, getBotToken(), ChatID, msg);

		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			InputStream is = new BufferedInputStream(conn.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendMessageToClient(String msg) {

		try {
			String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

			BufferedReader brTest = new BufferedReader(new FileReader("data.db"));
			String ChatID;
			ChatID = brTest.readLine();
			urlString = String.format(urlString, getBotToken(), ChatID, msg);

			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			InputStream is = new BufferedInputStream(conn.getInputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void consume(Update update) {

		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			File f = new File("data.db");
			if (!f.exists() && !f.isDirectory()) {
				try {
					FileWriter fr = new FileWriter(f, true);
					BufferedWriter br = new BufferedWriter(fr);

					br.write(update.getMessage().getChatId().toString());
					br.close();
					fr.close();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
			String command = update.getMessage().getText();
			try {

				if (command.equals("/add")) {

					SendMessage response = new SendMessage(update.getMessage().getChatId().toString(), getPrefix()
							+ "Bitte gehe auf \"https://kleinanzeigen.de\", suche nach einem begriff, filter diese Suche mit der "
							+ "Filter funktion und schreibe den link in den Chat.\n\n Beispiel: \" /add https://www.kleinanzeigen.de/s-preis:5:10/kochtopf/k0\"");
					telegramClient.execute(response);


				} else if (command.startsWith("/add https://www.kleinanzeigen.")) {
					String s = command;

					s = s.substring(5);

					if (Main.links.contains(s)) {
						SendMessage response = new SendMessage(update.getMessage().getChatId().toString(),
								"Error\n\nLink \"" + s + "\" ist bereits Hinzugefügt!");
						telegramClient.execute(response);

					} else {
						Main.addLink(s);
						SendMessage response = new SendMessage(update.getMessage().getChatId().toString(),
								getPrefix() + "Link \"" + s + "\" Hinzugefügt!");
						telegramClient.execute(response);
					}

				}
				if (command.startsWith("/delete")) {
					if (!command.contains(" ")) {
						SendMessage response = new SendMessage(update.getMessage().getChatId().toString(),
								getPrefix() + "Gebe /delete \"zahl\" ein die du aus der liste von /list bekommst.");
						telegramClient.execute(response);
					} else {
						String cmd = command;

						cmd = cmd.substring(8);

						if (cmd.matches("\\d+")) {
							int number = Integer.valueOf(cmd);

							try {
								ArrayList<String> arl = new ArrayList<String>();
								BufferedReader reader;
								reader = new BufferedReader(new FileReader("links.dat"));
								String line = reader.readLine();

								while (line != null) {

									arl.add(line);
									// read next line
									line = reader.readLine();
								}

								reader.close();

								String removed = arl.get(number - 1);

								arl.remove(number - 1);
								FileWriter writer = new FileWriter("links.dat");
								for (String str : arl) {
									writer.write(str + System.lineSeparator());
								}
								writer.close();
								SendMessage response = new SendMessage(update.getMessage().getChatId().toString(),
										getPrefix() + number + "." + removed + " wurde gelöscht.");
								telegramClient.execute(response);

							} catch (IOException e) {
								e.printStackTrace();
							}
							Main.refreshData();

						} else {
							SendMessage response = new SendMessage(update.getMessage().getChatId().toString(),
									getPrefix() + "Gebe /delete \"zahl\" ein die du aus der liste von /list bekommst.");
							telegramClient.execute(response);
						}

					}

				}
				if (command.startsWith("/list")) {
					ArrayList<String> s = new ArrayList<String>();

					for (int i = 1; i < Main.links.size() + 1; i++) {
						s.add(i + "." + Main.links.get(i - 1) + "\n");

					}

					String a = s.toString();
					String b = a.replace(",", "");
					String c = b.replace("[", "");
					String d = c.replace("]", "");

					SendMessage response = new SendMessage(update.getMessage().getChatId().toString(),
							getPrefix() + "Liste deiner links:\n\n" + d);
					telegramClient.execute(response);
				}

				System.out.println(command);
				// Create your send message object
				SendMessage sendM = new SendMessage(update.getMessage().getChatId().toString(),
						update.getMessage().getText().toString());

				// Execute it
				// telegramClient.execute(sendM);

			} catch (TelegramApiException e) {
				e.printStackTrace();
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
			BufferedReader reader;
			reader = new BufferedReader(new FileReader("BOTTOKEN.txt"));
			token = reader.readLine();

			reader.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return token;
	}

}