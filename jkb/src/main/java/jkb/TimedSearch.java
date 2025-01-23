package jkb;

import java.io.File;
import java.util.Iterator;

public class TimedSearch extends Thread {
   public static void startThread() {
      new Thread();

      while(true) {
         while(true) {
            try {
               System.out.println("STARTED!");
               File f = new File("data.db");
               if (f.exists() && !f.isDirectory()) {
                  Iterator var3 = Main.links.iterator();

                  while(var3.hasNext()) {
                     String str = (String)var3.next();
                     Main.Search(str);
                     Thread.sleep(300L);
                  }
               }

               Thread.sleep(300000L);
            } catch (InterruptedException var4) {
               var4.printStackTrace();
            }
         }
      }
   }
}