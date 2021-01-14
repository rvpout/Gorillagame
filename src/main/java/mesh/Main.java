package mesh;


import mesh.Mesh;
import mesh.MainApp;

public class Main {
    public static String server = null;
    public static int port = 4002;

    /**
     * Arguments:
     * - [none] = server, use the default port 59059
     * - [none] + CHATPORT env set = server, use port CHATPORT
     * - [port] = server, use the given port
     * - [host] = client, connect to host:59059
     * - [port, host] = client, connect to host:port
     *
     * @param cmdline none / [port] / [host] / [port, host]
     * @throws Exception
     */
    public static void main(final String[] cmdline) throws Exception {
        String[] args = cmdline;

        String env = System.getenv("CHATPORT");

        // if no cmd line arguments are provided, but CHATPORT is defined
        // use the CHATPORT as an argument.
        if (args.length == 0 && env != null) {
            System.out.println("No command line arguments provided, using CHATPORT env variable instead.");
            args = new String[] { env };
        }

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
                if (args.length > 1) {
                    server = args[1];
                }
            } catch (Exception e) {
                server = args[0];
            }
        }

        // if server == null, start a server,
        // otherwise connect to server:port as client
        if (server == null)
            new Mesh(port, args.length > 1);
        else {
            MainApp.launch(MainApp.class, args);
        }
    }

}