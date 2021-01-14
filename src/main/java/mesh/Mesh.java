package mesh;

import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;

import fi.utu.tech.distributed.gorilla.logic.ChatMessage;
import fi.utu.tech.distributed.gorilla.logic.GorillaMultiplayerLogic;
import javafx.application.Platform;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;

public class Mesh extends Thread {
    final boolean verboseMode;
    final int port;
    List<Handler> handlerlist =Collections.synchronizedList(new ArrayList<Handler>());
    Set<Long> viestit = Collections.synchronizedSet(new HashSet<>());
    Random rand = new Random();
    public long meshId = rand.nextLong();
	private GorillaMultiplayerLogic logic;
    // All client names, so we can check for duplicates upon registration.
    //private final Set<String> names = new HashSet<>();

    // The set of all the print writers for all the clients, used for broadcast.
    //private final Set<PrintWriter> writers = new HashSet<>();

    public Mesh(final int port, final boolean verboseMode, GorillaMultiplayerLogic logic)  throws Exception  {
        this.verboseMode = verboseMode;
        this.port=port;
        this.logic=logic;
        
    }

    public void broadcast(MeshPackage mp) throws IOException {
        viestit.add(mp.token);
        synchronized (handlerlist) {
            for (Handler handler : handlerlist) handler.send(mp);
        }
        //if (verboseMode) System.out.println("Broadcast: " + msg);
    }

    public void broadcast(Serializable o) throws IOException {
        broadcast(new MeshPackage(meshId, 0, o));
    }
    /**
     boolean add(final String name, final PrintWriter writer) {
     synchronized (writers) {
     if (name.isBlank() || names.contains(name)) return false;
     names.add(name);
     writers.add(writer);
     }
     return true;
     }

     void remove(final String name, final PrintWriter writer) {
     synchronized (writers) {
     if (name != null) names.remove(name);
     if (writer != null) writers.remove(writer);
     }
     }
     **/


    public void run(){

        System.out.println("Starting the chat server..");
        var pool = Executors.newFixedThreadPool(500);
        try (var listener = new ServerSocket(port)) {
            System.out.println("Listening to port " + port + " at " + listener.getInetAddress());

            while (true) {
                Handler handler = new Handler(listener.accept(), this);
                pool.execute(handler);
                handlerlist.add(handler);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    public void connect(String address, int port) throws UnknownHostException, IOException {
        Socket socket = new Socket(address, port);
        Handler handler = new Handler(socket, this);
        new Thread(handler).start();

        handlerlist.add(handler);

    }


    /**
     * The client handler task.
     */
    private class Handler implements Runnable {
        Mesh mesh;
        private final Socket socket;
        ObjectInputStream oIn;
        ObjectOutputStream oOut;

        /**
         * Constructs a handler thread, squirreling away the socket. All the interesting
         * work is done in the run method. Remember the constructor is called from the
         * server's main method, so this has to be as short as possible.
         * @throws IOException
         */
        public Handler(final Socket socket, Mesh mesh) throws IOException {
            this.socket = socket;
            this.mesh=mesh;
            oOut = new ObjectOutputStream(socket.getOutputStream());
            oIn = new ObjectInputStream(socket.getInputStream());


        }

        /**
         * Services this thread's client by repeatedly requesting a screen name until a
         * unique one has been submitted, then acknowledges the name and registers the
         * output stream for the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
            try {
                while (true) {
                    Object o = oIn.readObject();
                    if(!(o instanceof MeshPackage)) {
                        continue;
                    }
                    final MeshPackage mp = (MeshPackage)o;

                    if(viestit.contains(mp.token)) {
                        continue;
                    }
                    viestit.add(mp.token);
                    
                    Platform.runLater(() -> mesh.logic.processMessages(mp));
                   


                }
            } catch (Exception e) {
                System.out.println(e);
            }


        }

        public void send(MeshPackage mp) throws IOException {
            oOut.writeObject(mp);
            oOut.flush();

        }
    }
}