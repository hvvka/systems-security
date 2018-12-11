package com.blogspot.debukkitsblog.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.IllegalBlockingModeException;
import java.util.*;

/**
 * A very simple-to-use Server class for Java network applications<br>
 * originally created on March 9, 2016 in Horstmar, Germany
 *
 * @author Leonard Bienbeck
 * @version 2.4.2
 */
public abstract class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private static final String INTERNAL_LOGIN_ID = "_INTERNAL_LOGIN_";

    private Map<String, Executable> idMethods = new HashMap<>();

    private ServerSocket serverSocket;
    private int port;
    private List<RemoteClient> clients;
    private List<RemoteClient> toBeDeleted;

    private Thread listeningThread;

    private boolean autoRegisterEveryClient;
    private boolean secureMode;

    private boolean stopped;
    private boolean muted;
    private long pingInterval = 30L * 1000; // 30 seconds

    /**
     * Constructs a simple serverSocket listening on the given port. Every client that
     * connects to this serverSocket is registered and can receive broadcast and direct
     * messages, the connection will be kept alive using a ping and ssl will not be
     * used. <b>This constructor is deprecated! It is strongly recommended to
     * substitute it with the constructor that has the option <i>muted</i> as its
     * last parameter.</b>
     *
     * @param port The port to listen on
     */
    public Server(int port) {
        this(port, true, true, false);
    }

    /**
     * Constructs a simple serverSocket listening on the given port. Every client that
     * connects to this serverSocket is registered and can receive broadcast and direct
     * messages, the connection will be kept alive using a ping and ssl will not be
     * used.
     *
     * @param port  The port to listen on
     * @param muted Whether the mute mode should be activated on startup
     */
    public Server(int port, boolean muted) {
        this(port, true, true, false, muted);
    }

    /**
     * Constructs a simple serverSocket with all possible configurations. <b>This
     * constructor is deprecated! It is strongly recommended to substitute it with
     * the constructor that has the option <i>muted</i> as its last parameter.</b>
     *
     * @param port                    The port to listen on
     * @param autoRegisterEveryClient Whether a client that connects should be registered to send it
     *                                broadcast and direct messages later
     * @param keepConnectionAlive     Whether the connection should be kept alive using a ping package.
     *                                The transmission interval can be set using
     *                                <code>setPingInterval(int seconds)</code>.
     * @param useSSL                  Whether SSL should be used to establish a secure connection
     */
    public Server(int port, boolean autoRegisterEveryClient, boolean keepConnectionAlive, boolean useSSL) {
        this.clients = new ArrayList<>();
        this.port = port;
        this.autoRegisterEveryClient = autoRegisterEveryClient;
        this.muted = false;

        this.secureMode = useSSL;
        if (secureMode) {
            System.setProperty("javax.net.ssl.keyStore", "ssc.store");
            System.setProperty("javax.net.ssl.keyStorePassword", "SimpleServerClient");
        }
        if (autoRegisterEveryClient) {
            registerLoginMethod();
        }
        preStart();

        start();

        if (keepConnectionAlive) {
            startPingThread();
        }
    }

    /**
     * Constructs a simple serverSocket with all possible configurations
     *
     * @param port                    The port to listen on
     * @param autoRegisterEveryClient Whether a client that connects should be registered to send it
     *                                broadcast and direct messages later
     * @param keepConnectionAlive     Whether the connection should be kept alive using a ping package.
     *                                The transmission interval can be set using
     *                                <code>setPingInterval(int seconds)</code>.
     * @param useSSL                  Whether SSL should be used to establish a secure connection
     * @param muted                   Whether the mute mode should be activated on startup
     */
    public Server(int port, boolean autoRegisterEveryClient, boolean keepConnectionAlive, boolean useSSL, boolean muted) {
        this.clients = new ArrayList<>();
        this.port = port;
        this.autoRegisterEveryClient = autoRegisterEveryClient;
        this.muted = muted;

        this.secureMode = useSSL;
        if (secureMode) {
            System.setProperty("javax.net.ssl.keyStore", "ssc.store");
            System.setProperty("javax.net.ssl.keyStorePassword", "SimpleServerClient");
        }
        if (autoRegisterEveryClient) {
            registerLoginMethod();
        }
        preStart();

        start();

        if (keepConnectionAlive) {
            startPingThread();
        }
    }

    /**
     * Mutes the console output of this instance, stack traces will still be
     * printed.<br>
     * <b>Be careful:</b> This will not prevent processing of messages passed to the
     * onLog and onLogError methods, if they were overwritten.
     *
     * @param muted true if there should be no console output
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    /**
     * Sets the interval in which ping packages should be sent to keep the
     * connection alive. Default is 30 seconds.
     *
     * @param seconds The interval in which ping packages should be sent
     */
    public void setPingInterval(int seconds) {
        this.pingInterval = seconds * 1000L;
    }

    /**
     * Starts the thread sending a dummy package every <i>pingInterval</i> seconds.
     * Adjust the interval using <code>setPingInterval(int seconds)</code>.
     */
    private void startPingThread() {
        new Thread(() -> {
            while (serverSocket != null) {
                try {
                    Thread.sleep(pingInterval);
                } catch (InterruptedException e) {
                    // This exception does not need to result in any further action or output
                    LOG.error("", e);
                }
                broadcastMessage(new Datapackage("_INTERNAL_PING_", "OK"));
            }

        }).start();
    }

    /**
     * Starts the listening thread waiting for messages from clients
     */
    protected void startListening() {
        if (listeningThread == null && serverSocket != null) {
            listeningThread = new Thread(() -> {
                while (!Thread.interrupted() && !stopped && serverSocket != null) {
                    try {
                        // Wait for client to connect
                        onLog("[Server] Waiting for connection" + (secureMode ? " using SSL..." : "..."));
                        final Socket tempSocket = serverSocket.accept(); // potential resource leak, tempSocket might not be closed!

                        // Read the client's message
                        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(tempSocket.getInputStream()));
                        Object raw = ois.readObject();

                        if (raw instanceof Datapackage) {
                            final Datapackage msg = (Datapackage) raw;
                            onLog("[Server] Message received: " + msg);

                            // inspect all registered methods
                            for (String current : idMethods.keySet()) {
                                // if the current method equals the identifier of the Datapackage...
                                if (msg.id().equalsIgnoreCase(current)) {
                                    onLog("[Server] Executing method for identifier '" + msg.id() + "'");
                                    // execute the Executable on a new thread
                                    new Thread(() -> {
// Run the method registered for the ID of this Datapackage
                                        idMethods.get(current).run(msg, tempSocket);
// and close the temporary socket if it is no longer needed
                                        if (!msg.id().equals(INTERNAL_LOGIN_ID)) {
                                            try {
                                                tempSocket.close();
                                            } catch (IOException e) {
                                                LOG.error("", e);
                                            }
                                        }
                                    }).start();
                                    break;
                                }
                            }
                        }

                    } catch (SocketException e) {
                        onLog("Server stopped.");
                        onServerStopped();
                    } catch (IllegalBlockingModeException | IOException | ClassNotFoundException e) {
                        LOG.error("", e);
                    }

                }
            });

            listeningThread.start();
        }
    }

    /**
     * Sends a reply to client. This method should only be called from within the
     * run-Method of an <code>Executable</code> implementation.
     *
     * @param toSocket           The socket the message should be delivered to
     * @param datapackageContent The content of the message to be delivered. The ID of this
     *                           Datapackage will be "REPLY".
     */
    public synchronized void sendReply(Socket toSocket, Object... datapackageContent) {
        sendMessage(new RemoteClient(null, toSocket), new Datapackage("REPLY", datapackageContent));
    }

    /**
     * Sends a message to a client with specified id
     *
     * @param remoteClientId     The id of the client it registered on login
     * @param datapackageId      The id of message
     * @param datapackageContent The content of the message
     */
    public synchronized void sendMessage(String remoteClientId, String datapackageId, Object... datapackageContent) {
        sendMessage(remoteClientId, new Datapackage(datapackageId, datapackageContent));
    }

    /**
     * Sends a message to a client with specified id
     *
     * @param remoteClientId The id of the client it registered on login
     * @param message        The message
     */
    public synchronized void sendMessage(String remoteClientId, Datapackage message) {
        for (RemoteClient current : clients) {
            if (current.getId().equals(remoteClientId)) {
                sendMessage(current, message);
            }
        }
    }

    /**
     * Sends a message to a client
     *
     * @param remoteClient       The target client
     * @param datapackageId      The id of message
     * @param datapackageContent The content of the message
     */
    public synchronized void sendMessage(RemoteClient remoteClient, String datapackageId, Object... datapackageContent) {
        sendMessage(remoteClient, new Datapackage(datapackageId, datapackageContent));
    }

    /**
     * Sends a message to a client
     *
     * @param remoteClient The target client
     * @param message      The message
     */
    public synchronized void sendMessage(RemoteClient remoteClient, Datapackage message) {
        try {
            // send message
            if (!remoteClient.getSocket().isConnected()) {
                throw new ConnectException("Socket not connected.");
            }
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(remoteClient.getSocket().getOutputStream()));
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            onLogError("[Server] [Send Message] Error: " + e.getMessage());

            // if an error occurred: remove client from list
            if (toBeDeleted != null) {
                toBeDeleted.add(remoteClient);
            } else {
                clients.remove(remoteClient);
                onClientRemoved(remoteClient);
            }
        }
    }

    /**
     * Broadcasts a message to a group of clients
     *
     * @param group   The group name the clients registered on their login
     * @param message The message
     * @return The number of clients reached
     */
    public synchronized int broadcastMessageToGroup(String group, Datapackage message) {
        toBeDeleted = new ArrayList<>();

        // send message to all clients
        int txCounter = 0;
        for (RemoteClient current : clients) {
            if (current.getGroup().equals(group)) {
                sendMessage(current, message);
                txCounter++;
            }
        }

        // remove all clients which produced errors while sending
        txCounter -= toBeDeleted.size();
        for (RemoteClient current : toBeDeleted) {
            clients.remove(current);
            onClientRemoved(current);
        }

        toBeDeleted = null;

        return txCounter;
    }

    /**
     * Broadcasts a message to a group of clients
     *
     * @param message The message
     * @return The number of clients reached
     */
    public synchronized int broadcastMessage(Datapackage message) {
        toBeDeleted = new ArrayList<>();

        // send message to all clients
        int txCounter = 0;
        for (RemoteClient current : clients) {
            sendMessage(current, message);
            txCounter++;
        }

        // remove all clients which produced errors while sending
        txCounter -= toBeDeleted.size();
        for (RemoteClient current : toBeDeleted) {
            clients.remove(current);
            onClientRemoved(current);
        }

        toBeDeleted = null;

        return txCounter;
    }

    /**
     * Registers a method that will be executed if a message containing
     * <i>identifier</i> is received
     *
     * @param identifier The ID of the message to proccess
     * @param executable The method to be called when a message with <i>identifier</i> is
     *                   received
     */
    public void registerMethod(String identifier, Executable executable) {
        if (identifier.equalsIgnoreCase(INTERNAL_LOGIN_ID) && autoRegisterEveryClient) {
            throw new IllegalArgumentException("Identifier may not be '" + INTERNAL_LOGIN_ID + "'. "
                    + "Since v1.0.1 the serverSocket automatically registers new clients. "
                    + "To react on new client registered, use the onClientRegisters() listener by overwriting it.");
        }
        idMethods.put(identifier, executable);
    }

    /**
     * Registers a login handler. This method is called only if the constructor has
     * been applied to register clients.
     */
    private void registerLoginMethod() {
        idMethods.put(INTERNAL_LOGIN_ID, (msg, socket) -> {
            if (msg.size() == 3) {
                registerClient((String) msg.get(1), (String) msg.get(2), socket);
            } else if (msg.size() == 2) {
                registerClient((String) msg.get(1), socket);
            } else {
                registerClient(UUID.randomUUID().toString(), socket);
            }
            onClientRegistered(msg, socket);
            onClientRegistered();
        });
    }

    /**
     * Registers a client to allow sending it direct and broadcast messages later
     *
     * @param id              The client's id
     * @param newClientSocket The client's socket
     */
    private synchronized void registerClient(String id, Socket newClientSocket) {
        clients.add(new RemoteClient(id, newClientSocket));
    }

    /**
     * Registers a client to allow sending it direct and broadcast messages later
     *
     * @param id              The client's id
     * @param group           The client's group name
     * @param newClientSocket The client's socket
     */
    private synchronized void registerClient(String id, String group, Socket newClientSocket) {
        clients.add(new RemoteClient(id, group, newClientSocket));
    }

    /**
     * Starts the serverSocket. This method is automatically called after
     * <code>preStart()</code> and starts the actual and the listening thread.
     */
    private void start() {
        stopped = false;
        serverSocket = null;
        try {
            serverSocket = secureMode
                    ? SSLServerSocketFactory.getDefault().createServerSocket(port)
                    : new ServerSocket(port);
        } catch (IOException e) {
            onLogError("Error opening ServerSocket");
            LOG.error("", e);
        }
        startListening();
    }

    /**
     * Stops the serverSocket
     *
     * @throws IOException If closing the ServerSocket fails
     */
    public void stop() throws IOException {
        stopped = true;

        if (listeningThread.isAlive()) {
            listeningThread.interrupt();
        }

        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    /**
     * Counts the number of clients registered
     *
     * @return The number of clients registered
     */
    public synchronized int getClientCount() {
        return clients != null ? clients.size() : 0;
    }

    /**
     * Checks whether a RemoteClient with the given ID is currently connected to the
     * serverSocket
     *
     * @param clientId The clients ID
     * @return true, if a RemoteClient with ID <i>clientId</i> is connected to the
     * serverSocket
     */
    public boolean isClientIdConnected(String clientId) {
        if (clients != null && !clients.isEmpty()) {
            // Iterate all clients connected
            for (RemoteClient c : clients) {
                // Check client exists and its socket is connected
                if (c.getId().equals(clientId) && c.getSocket() != null && c.getSocket().isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether any client is currently connected to the serverSocket
     *
     * @return true, if at least one client is connected to the serverSocket
     */
    public boolean isAnyClientConnected() {
        return getClientCount() > 0;
    }

    /**
     * Called just before the actual serverSocket starts. Register your handler methods in
     * here using
     * <code>registerMethod(String identifier, Executable executable)</code>!
     */
    public abstract void preStart();

    /**
     * Called on the listener's main thread when a new client registers
     */
    public void onClientRegistered() {
        // Overwrite this method when extending this class
    }

    /**
     * Called on the listener's main thread when a new client registers
     *
     * @param msg    The message the client registered with
     * @param socket The socket the client registered with. Be careful with this! You
     *               should not close this socket, because the serverSocket should have
     *               stored it normally to reach this client later.
     */
    public void onClientRegistered(Datapackage msg, Socket socket) {
        // Overwrite this method when extending this class
    }

    /**
     * Called on the listener's main thread when a client is removed from the list.
     * This normally happens if there was a problem with its connection. You should
     * wait for the client to connect again.
     *
     * @param remoteClient The client that was removed from the list of reachable clients
     */
    public void onClientRemoved(RemoteClient remoteClient) {
        // Overwrite this method when extending this class
    }

    /**
     * Called when the serverSocket finally stops after the stop () method has been
     * called.
     */
    public void onServerStopped() {
        // Overwrite this method when extending this class
    }

    /**
     * By default, this method is called whenever an output is to be made. If this
     * method is not overwritten, the output is passed to the system's default
     * output stream (if output is not muted).<br>
     * Error messages are passed to the <code>onLogError</code> event listener.<br>
     * <b>Override this method to catch and process the message in a custom way.</b>
     *
     * @param message The content of the output to be made
     */
    public void onLog(String message) {
        if (!muted) {
            LOG.info(message);
        }
    }

    /**
     * By default, this method is called whenever an error output is to be made. If
     * this method is not overwritten, the output is passed to the system's default
     * error output stream (if output is not muted).<br>
     * Non-error messages are passed to the <code>onLog</code> event listener.<br>
     * <b>Override this method to catch and process the message in a custom way.</b>
     *
     * @param message The content of the error output to be made
     */
    public void onLogError(String message) {
        if (!muted) {
            LOG.error(message);
        }
    }

    /**
     * A RemoteClient representing a client connected to this serverSocket storing an id
     * for identification and a socket for communication.
     */
    protected class RemoteClient {

        private final String id;

        private final String group;

        private final Socket socket;

        /**
         * Creates a RemoteClient representing a client connected to this serverSocket
         * storing an id for identification and a socket for communication. The client
         * will be member of the default group.
         *
         * @param id     The clients id (to use for identification; choose a custom String)
         * @param socket The socket (to use for communication)
         */
        public RemoteClient(String id, Socket socket) {
            this.id = id;
            this.group = "_DEFAULT_GROUP_";
            this.socket = socket;
        }

        /**
         * Creates a RemoteClient representing a client connected to this serverSocket
         * storing an id for identification and a socket for communication. The client
         * can be set as a member of a group of clients to receive messages broadcasted
         * to a group.
         *
         * @param id     The clients id (to use for identification; choose a custom String)
         * @param group  The group the client is member of
         * @param socket The socket (to use for communication)
         */
        public RemoteClient(String id, String group, Socket socket) {
            this.id = id;
            this.group = group;
            this.socket = socket;
        }

        public String getId() {
            return id;
        }

        public String getGroup() {
            return group;
        }

        public Socket getSocket() {
            return socket;
        }

        /**
         * Returns a String representing the RemoteClient, format is <i>[RemoteClient ID
         * (GROUP) @ SOCKET_REMOTE_ADDRESS]</i>
         */
        @Override
        public String toString() {
            return "[RemoteClient: " + id + " (" + group + ") @ " + socket.getRemoteSocketAddress() + "]";
        }
    }

}