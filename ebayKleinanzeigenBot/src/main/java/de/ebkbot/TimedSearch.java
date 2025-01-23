package de.ebkbot;

import java.io.File;

public class TimedSearch extends Thread {

	public static void startThread() {
		Thread th = new Thread();
		while (true) {

			try {

				System.out.println("STARTED!");
				File f = new File("data.db");
				if (f.exists() && !f.isDirectory()) {

					for (String str : Main.links) {

						Main.Search(str);
						th.sleep(300);
					}
				}

				th.sleep(1000 * 60 * 5);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
