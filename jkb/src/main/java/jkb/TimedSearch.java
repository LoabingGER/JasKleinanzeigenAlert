package jkb;


import java.io.File;

public class TimedSearch extends Thread {

	public static void startThread() {
		Thread th = new Thread();
		try {
			th.sleep(1000* 60 * 1);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
			th.sleep(1000 * 60 *  5);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}

}