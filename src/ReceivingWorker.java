
/*
 * Class receives request to store files, it receives filename
 * for that filename it creates file in directory, and receives its content from same socket
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


class ReceivingWorker implements Runnable{
	
	
	public final int DATA_PORT=1032;
	public final String END_TRANSMISSION="END";
	public final String SUCCESS="OK";
	
	Socket client;
	//PrintWriter out;
	
	InputStream istream;
	BufferedReader in;
	PrintWriter pwriter;
	
	File directory;
	File file;
	String[] file_list;
	File[] listOfFiles;
	
	/*Constructor*/
	ReceivingWorker(Socket s){
		
		System.out.println("Worker constructor");
		
		/*open directory to store received files */
		try{
			
			directory = new File("/home/USER/LOCALFOLDER");
			System.out.println("File assigned");
			
			if(!directory.exists()){
				if(directory.mkdir()){
					System.out.println("Directory succesfuly created");
				}else{
					System.out.println("Failed to create directory!");
					System.exit(1);
				}
				
			}//if
			
		}catch(Exception e){
			System.out.println("Cannot create folder\n"+e.toString());
			System.exit(1);
		}
		
		/*part of constructor which initialize client socket*/
		try{
			client=s;
			
			pwriter=new PrintWriter(client.getOutputStream());
			istream=client.getInputStream();//used for binary data receiving
			in=new BufferedReader(new InputStreamReader(istream));//for filename receiving
			
		}catch(Exception e){
			System.out.println("Cannot initialize client socket! \n"+e.toString());
			System.exit(1);
		}

	
	}//constructor
	

	@Override
public void run() {
		
		System.out.println("RW, run started");
		String filename=null;
		String line=null;
		
		try{
			
			System.out.println("RW, entered intoTRY ");
				while(!(line=in.readLine()).equals(END_TRANSMISSION)){
					filename=line;
					System.out.println("File that should be received: "+line);
				}
				System.out.println("file received");
				pwriter.println(SUCCESS);
				pwriter.flush();
				
				File newFile=new File(directory,filename);
				newFile.createNewFile();
				FileOutputStream fos=new FileOutputStream(newFile);
				
				byte[] buf = new byte[8192];
		        int len = 0;
		            
				while ((len = istream.read(buf)) != -1) {
		          
		            	fos.write(buf, 0, len);
		            	fos.flush();
		            }
					fos.close();

		}catch(Exception e){
				System.out.println("RUN ERROR"+e.toString());
				
				System.exit(1);
			}
				
		System.out.println("Client ends communication");

	}//run
	
}//ReceivingWorker

