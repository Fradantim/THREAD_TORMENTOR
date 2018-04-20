package test;


import java.util.ArrayList;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import model.Work;

public class TestSpring {
	
	
	public static void main(String args[]) {
			String inputLoteContextFile="context15.xml";
			
			ApplicationContext inputLoteContext = new FileSystemXmlApplicationContext(inputLoteContextFile);
			
			//Work work = (Sleeper) inputLoteContext.getBean("Job_1");
			
			//work.execute();
			
			
			Map<String, Work> works = inputLoteContext.getBeansOfType(Work.class);
			
			
			System.out.println("C'est fini "+works.size());
			ArrayList<Work> worksArr = new ArrayList<>();
			for(String key: works.keySet()) {
				worksArr.add(works.get(key));
				for(Work next : works.get(key).getNext()) {
					System.out.println("id "+ works.get(key).getId()+" next: " + next.getId());
				}
			}
			
			
			
			System.out.println(worksArr.get(0).equals(worksArr.get(0)));
			System.out.println(worksArr.get(0).equals(worksArr.get(1)));

			
			Map<String, String> strs = inputLoteContext.getBeansOfType(String.class);
			
			
			System.out.println("C'est fini "+works.size());
			
			System.out.println("Strings");
			for(String key: works.keySet()) {
				
				System.out.println(key+" "+strs.get(key));
				
			}
			
			
			((ConfigurableApplicationContext)inputLoteContext).close();
			
			
			//ServidorE2Vault serverE2 = (ServidorE2Vault) context.getBean("ServidorE2Vault");
	}
}
