import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

public class MudServer extends UnicastRemoteObject implements Mud.RemoteMudServer, Serializable {
    private MudRoom entrance;
    private String password;
    private String mudName;
    Hashtable <String, Mud.RemoteMudRoom> rooms;

    protected MudServer() throws RemoteException {}
    public MudServer (String mudName, String password, String roomName) throws RemoteException {
        this.mudName = mudName;
        this.password = password;
        rooms = new Hashtable<>();
        try {
            entrance = new MudRoom(this, roomName);
        } catch (Mud.RoomAlreadyExists e) {}
    }

    @Override
    public String getMudName() throws RemoteException { return mudName; }
    @Override
    public Mud.RemoteMudRoom getEntrance() throws RemoteException { return entrance; }
    @Override
    public Mud.RemoteMudRoom getNamedPlace(String name) throws RemoteException, Mud.NoSuchRoom {
        Mud.RemoteMudRoom r = (Mud.RemoteMudRoom) rooms.get(name);
        if ( r == null ) throw new Mud.NoSuchRoom();
        return r;
    }
    public void addRoom (Mud.RemoteMudRoom room, String name) throws Mud.RoomAlreadyExists {
        if ( rooms.containsKey(name) ) throw new Mud.RoomAlreadyExists();
        rooms.put(name, room);
    }

    /**
     * This main() method defines the standalone program that starts up a MUD server.
     * It expects four command-line arguments:
     *   0) the name of the MUD (for example "myMud")
     *   1) the password (for example "1111")
     *   2) the name of the enrance room for the MUD (for example "Room1").
     */
    public static void main(String[] args) {
        try {
            if ( args.length != 3 )
                throw new Exception("Wrong number of arguments.");
            MudServer server;
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            server = new MudServer(args[0], args[1], args[2]);
            Naming.rebind(Mud.mudPrefix + server.getMudName(), server);
            System.out.println("Initialized successfully.");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Usage: java MudServer <mudname> <password> <roomname>");
            System.exit(1);
        }
    }

}
