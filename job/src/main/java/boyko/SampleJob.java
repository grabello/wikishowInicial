package boyko;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SampleJob {
	
	public void sampleJobMethod() {
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		
		System.out.println("Novo Invoked on " + dateFormat.format(System.currentTimeMillis()));
	}
}