package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CMD {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		String proc = "cmd.exe /c C:/test/Doc1Gen64/doc1gen C:/IMPRE_T3/doc1-src/conf/gen/AMDOCS_T3_CICLICA.hip ops=C:/IMPRE_T3/doc1-gen/movil/ML/tmp/20_201801_T20180122003014_20180122011515_config_doc1gen_N.ops";
		
		 ProcessBuilder builder = new ProcessBuilder(proc.split(" "));
		 // ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd \"C:\\Program Files\\Microsoft SQL Server\" && dir");
		        builder.redirectErrorStream(true);
		        Process p = builder.start();
		        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line;
		        while ((line= r.readLine())!=null) {
		            System.out.println(line);
		        }
		        
		        p.destroy();
		        System.out.println("RETURN:"+ p.exitValue()); 
	}

}
