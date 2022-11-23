import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	private Consumer<BaccaratInfo> callback;
	String ipAddress;
	int port;
	
	Client(Consumer<BaccaratInfo> call, String address, int portNum) {
		this.callback = call;
		this.ipAddress = address;
		this.port = portNum;
	}
	
	public void run() {
		
		try {
			socketClient = new Socket(ipAddress, port);
		    out = new ObjectOutputStream(socketClient.getOutputStream());
		    in = new ObjectInputStream(socketClient.getInputStream());
		    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		while(true) {
			 
			try {
				BaccaratInfo message = (BaccaratInfo) in.readObject();
				callback.accept(message);
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send(BaccaratInfo data) {
		
		try {
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
