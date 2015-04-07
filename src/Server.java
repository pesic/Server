

/*
 * This class starts two server threads,
 * one listen for receiving file request and starts related worker thread
 * second listen for sending file request and start related worker.
 * Those workers work on different ports
 * */


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
	
	public static final int RECEIVING_PORT=1031;
	public static final int SENDING_PORT=1030;
	public static boolean END_FLAG=false;
	
	public static ExecutorService exec;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
			/*Next 4 lines are not related to project, code is just for practice*/
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); //you get the IP as a String
			System.out.println("Server IP is: "+ip+"\nEnter it in your XFR application");
			
			
			exec= Executors.newCachedThreadPool();
			
			exec.execute(new Listener(RECEIVING_PORT));
			exec.execute(new Listener(SENDING_PORT));

		}catch(Exception e){
			System.out.println("Cannot crate server socket "+e.toString());
			System.exit(1);}
		
		
	}//main
}//Server


	class Listener implements Runnable{
		private int PORT_NUMBER;
		ExecutorService exec;
		Listener(int port){
			PORT_NUMBER=port;
			exec=Executors.newCachedThreadPool();
		}
		@Override
		public void run() {
			switch(PORT_NUMBER){
			case Server.SENDING_PORT:{
				/*Servers Thread for receiving files*/
				try{
					ServerSocket server=new ServerSocket(Server.SENDING_PORT);
					while(!Server.END_FLAG){
						System.out.println("Entered into listenning loop");
						Socket socket=server.accept();
						exec.execute(new SendingWorker(socket));
				}
					server.close();
				}catch(Exception e){
					System.out.println("ServerListener,Cannot start SENDING server"+e.toString());
					System.exit(1);
				}
			}
			case Server.RECEIVING_PORT:{
				/*Servers Thread for sending files*/
				try{
					
					ServerSocket server2=new ServerSocket(Server.RECEIVING_PORT);
					
					while(!Server.END_FLAG){
						System.out.println("Entered into listenning loop");
						Socket socket2=server2.accept();
						exec.execute(new ReceivingWorker(socket2));
					}
					server2.close();
				}catch(Exception e){
					System.out.println("ServerListener,Cannot start RECEIVING server"+e.toString());
					System.exit(1);
				}
			}
			}//switch
			
			}//runLISTENER
				
	}//LISTENER
		
		
		

