package main;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import runner.SyncPipe;

public class CMD {

	public static void main(String[] args) throws IOException, InterruptedException {
		/*OutputStream outputStream = new FileOutputStream("C:/test/test.io");
		String[] command =
		    {
		        "cmd",
		    };
		    Process p = Runtime.getRuntime().exec(command);
		    new Thread(new SyncPipe(p.getErrorStream(), outputStream)).start();
		    new Thread(new SyncPipe(p.getInputStream(), outputStream)).start();
		    PrintWriter stdin = new PrintWriter(p.getOutputStream());
		    stdin.println("dir c:\\ /A /Q");
		    // write any other commands you want here
		    stdin.close();
		    int returnCode = p.waitFor();
		    System.out.println("Return code = " + returnCode);*/
		
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
