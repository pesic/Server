
/*
 * -Directory where files are stored should be named in class constructor
 * -'end' word which ends communication client-server
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;


class SendingWorker implements Runnable{
	
	Socket client;
	//PrintWriter out;
	
	OutputStream out;
	PrintStream outString;
	
	BufferedReader in;
	File directory;
	File file;
	String[] file_list;
	File[] listOfFiles;
	
	/*Constructor*/
	SendingWorker(Socket s){
		
		System.out.println("Worker constructor");
		
		/*open directory to reach files for transfer */
		try{
			
			directory = new File("/home/USER/FOLDERLOCAL");
			System.out.println("File assigned");
			
			
			if(!directory.exists()){
				if(directory.mkdir()){
					System.out.println("Directory succesfuly created");
				}else{
					System.out.println("Failed to create directory!");
					System.exit(1);
				}
				
			}//if
			
			/*makes list of folder content*/
			listOfFiles = directory.listFiles();
			
			
		}catch(Exception e){
			System.out.println("Cannot create folder\n"+e.toString());
			System.exit(1);
		}
		
		
		/*part of constructor which initialize client socket*/
		try{
			client=s;
			
			in=new BufferedReader(new InputStreamReader(client.getInputStream()));
			out=client.getOutputStream();
			outString=new PrintStream(out);
			
		}catch(Exception e){
			System.out.println("Cannot initialize client socket! \n"+e.toString());
			System.exit(1);
		}

	
	}//constructor

	
	private void send_content(){
		
		for (int i = 0; i < listOfFiles.length; i++) {
			try{
		      if (listOfFiles[i].isFile()) {
		        
		        	System.out.println("File " + listOfFiles[i].getName());
			        outString.println(listOfFiles[i].getName());
			        
		       
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		        outString.println("Directory " + listOfFiles[i].getName());
		      }
			 }catch(Exception e){
		        	System.out.println("CANNOT WRITE CORRECTLY "+e.toString());
		        	System.exit(1);
		        }
			
		    }
		
		outString.close();
	}
	
	private void send_data(int i){
		try{
			/*0 is used for content sending*/
			
			i=i-1;
			
			InputStream data = new FileInputStream(listOfFiles[i].getPath());
			
			System.out.println("Data sending is "+listOfFiles[i]);
			byte[] buf = new byte[8192];
            int len = 0;
        
            while ((len = data.read(buf)) != -1) {
            	
            	System.out.println("while loop server "+len);
            	out.write(buf, 0, len);
            	out.flush();
            }
            out.close();
            data.close();
		}catch(Exception e){
			System.err.println("ERROR PH 2"+e.toString());
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		
		
		String line=null;
		
		try{
		
			line=in.readLine();
			
				if(line!=null){
					int i=Integer.parseInt(line);
					if(i==0){
						send_content();
						System.out.println("Content sent!");
					}
					else if(i!=0){
						send_data(i);
						System.out.println("Integer readed"+i);
						
					}
					
				}//if not NULL
			
				
			
			
		}catch(Exception e){
				System.out.println("RUN ERROR"+e.toString());
				
				System.exit(1);
			}
				
		System.out.println("Client ends communication");
		
		
	}//run
	
	
	
}//SendingWorker
