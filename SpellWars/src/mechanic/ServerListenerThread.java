package mechanic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ServerListenerThread extends Thread {
	ServerSocket serverSocket;
	ArrayList<Integer> keyInputs;
	private BufferedReader in;
    private PrintWriter out;
    public boolean clientConnected;
    private GameMap map;
	public ServerListenerThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.keyInputs = new ArrayList<Integer>();
		this.clientConnected = false;
	}
	public void setMap(GameMap map) {
		this.map = map;
	}
	@Override
	public void run() {
		Socket s;
		try {
			s = serverSocket.accept();
			this.clientConnected = true;
			System.out.println("CLIENT CONNECTED");
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			String input = null;
			try {
				 input = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(input != null) {
				StringTokenizer st = new StringTokenizer(input);
				while(st.hasMoreElements()) {
					String token = st.nextToken();
					if(token.equals(Game.DRAW_INFO_REQUEST_STRING)) {
						out.println(this.map.getDrawInfo());
					} else {
						try {
							this.keyInputs.add(Integer.parseInt(token));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	public int[] getKeyInputsFromClient() {
		int[] array = new int[this.keyInputs.size()];
		for(int i = 0; i < this.keyInputs.size(); i++) {
			array[i] = this.keyInputs.get(i);
		}
		this.keyInputs.clear(); //clears the buffer
		return array;
	}
}
