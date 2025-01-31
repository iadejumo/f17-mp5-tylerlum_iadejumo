package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.JsonObject;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class YelpDBServer {

	// Abstraction Function: yelpDB + serverSocket + port number
	// Rep invariant: serverSocket != null

	// Thread safety argument:
	// YELPDB_PORT is static final and primitive type, so its reference and value
	// are fully immutable
	// TODO serverSocket
	// socket objects
	// readers and writers in handle()
	// data in handle
	// filenames are private and final and String, so its reference and object
	// values are fully immutable
	// commands is only ever read from NEVER WRITTEN TO after constructor and is
	// immutable
	private final YelpDB yelpDB;

	private ServerSocket serverSocket;
	public static final int YELPDB_PORT = 4949;

	private final String userFile = "data/users.json";
	private final String restaurantFile = "data/restaurants.json";
	private final String reviewFile = "data/reviews.json";
	
	public static final String GETRESTAURANT = "GETRESTAURANT";
	public static final String ADDUSER = "ADDUSER";
	public static final String ADDRESTAURANT = "ADDRESTAURANT";
	public static final String ADDREVIEW = "ADDREVIEW";
	public static final String QUERY = "QUERY";

	/**
	 * Make a YelpDBServer that listens for connections on port.
	 * 
	 * @param port
	 *            port number, requires 0 <= port <= 65535
	 * @throws IOException
	 *             if YelpDB data files are not in the proper location
	 * @throws ParseException
	 *             if YelpDB data files are not valid
	 */
	public YelpDBServer(int port) throws ParseException, IOException {
		yelpDB = new YelpDB(restaurantFile, reviewFile, userFile);
		serverSocket = new ServerSocket(port);
	}

	/**
	 * Run the server, listening for connections and handling them.
	 * 
	 * @throws IOException
	 *             if the main server socket is broken
	 */
	public void serve() throws IOException {
		while (true) {
			// block until a client conects
			final Socket socket = serverSocket.accept();
			// create a new thread to handle that client
			Thread handler = new Thread(new Runnable() {
				public void run() {
					try {
						try {
							handle(socket);
						} finally {
							socket.close();
						}
					} catch (IOException ioe) {
						// this exception wouldn't terminate serve()
						// since we're now on a different thread but
						// we still need to handle it
						ioe.printStackTrace();
					} catch (ParseException pe) {
						pe.printStackTrace();
					}
				}
			});
			// start the thread
			handler.start();
		}
	}

	/**
	 * Handle one client connection. Returns when client disconnects.
	 * 
	 * @param socket
	 *            socket where client is connected
	 * @throws IOException
	 *             if connection encounters an error
	 * @throws ParseException 
	 */
	private void handle(Socket socket) throws IOException, ParseException {
		System.err.println("New client connected!");

		// get the socket's input stream, and wrap converters around it
		// that convert it from a byte stream to a character stream,
		// and then buffer it so that we can read it line by line
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// similarly, wrap character=>bytestream converter around the socket output
		// stream
		// and wrap a PrintWriter around that so
		// that we have more convenient ways to write Java primitive
		// types to it
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

		try {
			// each request is a single line containing a command keyword and its parameters
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				if (line.equals("")) 
					System.err.println("Blank input");
				else {
					System.err.println("request: " + line);
					try {
						// get answer and send back to client
						String reply = prepareReply(line.trim());
						System.err.println("reply: " + reply);
						out.println(reply);
					} catch (InvalidUserStringException iuse) {
						// complain about error in format of request
						System.err.println("reply: ERR: INVALID_USER_STRING");
						out.println("ERR: INVALID_USER_STRING\n");
					} catch (InvalidRestaurantStringException irese) {
						// complain about error in format of request
						System.err.println("reply: ERR: INVALID_RESTAURANT_STRING");
						out.println("ERR: INVALID_RESTAURANT_STRING\n");
					} catch (InvalidReviewStringException irvwuse) {
						// complain about error in format of request
						System.err.println("reply: ERR: INVALID_REVIEW_STRING");
						out.println("ERR: INVALID_REVIEW_STRING\n");
					} catch (NoSuchUserException nsue) {
						// complain about error in format of request
						System.err.println("reply: ERR: NO_SUCH_USER");
						out.println("ERR: NO_SUCH_USER\n");
					} catch (NoSuchRestaurantException nsre) {
						// complain about error in format of request
						System.err.println("reply: ERR: NO_SUCH_RESTAURANT");
						out.println("ERR: NO_SUCH_RESTAURANT\n");
					} catch (InvalidCommandException ice) {
						// complain about error in format of request
						System.err.println("reply: ERR: INVALID_COMMAND");
						out.println("ERR: INVALID_COMMAND\n");
					} catch (NoMatchesException nme) {
						System.err.println("reply: ERR: NO_MATCH");
						out.println("ERR: NO_MATCH\n");
					} catch (ParseCancellationException pce) { //Changed to the antlr default exception
						System.err.println("reply: ERR: INVALID_QUERY");
						out.println("ERR: INVALID_QUERY\n");
					}
					// important! our PrintWriter is auto-flushing, but if not
					// out.flush();
				}
			}
		} finally {
			out.close();
			in.close();
		}
	}

	private String prepareReply(String inputLine) throws ParseException, NoSuchRestaurantException, InvalidUserStringException, InvalidReviewStringException, NoSuchUserException, InvalidRestaurantStringException, InvalidCommandException {
		if (inputLine.toUpperCase().startsWith(GETRESTAURANT)) {
			String data = inputLine.substring(GETRESTAURANT.length()).trim();
			System.err.println("Data: " + data);
			Restaurant r = yelpDB.getRestaurants().get(data);
			if (r == null)
				throw new NoSuchRestaurantException();
			return r.toString();
		}
		
		else if (inputLine.toUpperCase().startsWith(ADDUSER)) {
			String data = inputLine.substring(ADDUSER.length()).trim();
			System.err.println("Data: " + data);
			return yelpDB.addUser(data);
		}
			
		else if (inputLine.toUpperCase().startsWith(ADDRESTAURANT)) {
			String data = inputLine.substring(ADDRESTAURANT.length()).trim();
			System.err.println("Data: " + data);
			return yelpDB.addRestaurant(data);
		}
		
		else if (inputLine.toUpperCase().startsWith(ADDREVIEW)) {
			String data = inputLine.substring(ADDREVIEW.length()).trim();
			System.err.println("Data: " + data);
			return yelpDB.addReview(data);
		}
		
		else if (inputLine.toUpperCase().startsWith(QUERY)) {
			String data = inputLine.substring(ADDREVIEW.length()).trim();
			System.err.println("Data: " + data);
			return yelpDB.getMatches(data).toString();
		}
		
		else if (inputLine.equals(""))
			return "BLANK INPUT, SO NO OUTPUT";
		else
			throw new InvalidCommandException();
	}
	

	/**
	 * Start a YelpDBServer running on the default port.
	 */
	public static void main(String[] args) {
		try {
			YelpDBServer server = new YelpDBServer(YELPDB_PORT);
			server.serve();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	

}
