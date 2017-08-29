package experimental;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder.Header;

public class testReads {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 HashMap<String,ArrayList<Integer> >readResults = new HashMap<String,ArrayList<Integer>>();
	        HashMap<String,String >reads = new HashMap<String,String>();
	    	String path="/Users/huebler/Desktop/reads.txt";
	    	boolean first=true;
			try(BufferedReader br = new BufferedReader(new FileReader(path))) {
				String line;
				ArrayList<String> headers = new ArrayList<String>();
				ArrayList<String> sequences = new ArrayList<String>();
				while ((line = br.readLine())!=null) {
					if(line.startsWith(">")){
						System.out.println(line);
						headers.add(line);
					}else{
						sequences.add(line);
					}
				}
				System.out.println(sequences.size());
				for(int i = 0;i<headers.size();i++)
					reads.put(headers.get(i), sequences.get(i));
				
			}catch(IOException io){
				io.printStackTrace();
			}
			System.out.println("Reads Size "+ reads.size());
			for(String key:reads.keySet())
				System.out.println(key);
	}

}
