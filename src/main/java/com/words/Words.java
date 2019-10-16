package breakIterator;

import java.text.BreakIterator;
import java.util.Locale;

public class Words {
   public static void main(String[] args) {
	   Locale locale = Locale.UK;
	   BreakIterator breakIterator =
	           BreakIterator.getWordInstance(locale);

	   breakIterator.setText("Mary had a little Android device.");

	   int firstIndex = breakIterator.first();
	   while(firstIndex != BreakIterator.DONE) {
		   int lastIndex = breakIterator.next();
		   if(lastIndex != BreakIterator.DONE)
	       System.out.println("Mary had a little Android device.".substring(firstIndex, lastIndex)) ;
	       firstIndex = lastIndex;
	   }
   }
}
