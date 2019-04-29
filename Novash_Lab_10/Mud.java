import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public class Mud {

    public interface RemoteMudServer extends Remote {
        String getMudName() throws RemoteException;
        RemoteMudRoom getEntrance() throws RemoteException;
        RemoteMudRoom getNamedPlace(String name) throws RemoteException, NoSuchRoom;
    }

    public interface RemoteMudPerson extends Remote {
        static final int MOVE = 1;
        static final int WAIT = 2;
        char getIcon() throws RemoteException;
        void setIcon(char icon) throws RemoteException;
        int getStatus() throws RemoteException;
        void setStatus(int status) throws RemoteException;
        void tell(String message) throws RemoteException;
    }

    public interface RemoteMudRoom extends Remote {
        String getRoomName() throws RemoteException;
        char[][] getBoard() throws RemoteException;
        int getBoardSize() throws RemoteException;
        Vector getNames() throws RemoteException;
        RemoteMudPerson getPerson(String name) throws RemoteException, NoSuchPerson;
        void speak(RemoteMudPerson speaker, String message) throws RemoteException, NotThere;
        void act(RemoteMudPerson actor, String message) throws RemoteException, NotThere;
        void move(RemoteMudPerson person, int x, int y) throws RemoteException, NotThere, WrongMove, WrongStatus;
        RemoteMudServer getServer() throws RemoteException;
        void exit(RemoteMudPerson who, String message) throws RemoteException, NotThere;
        void enter(RemoteMudPerson who, String name, String message) throws RemoteException, AlreadyThere, FullRoom;
    }

    public static class MudException extends Exception {
        protected String msg = "";
        @Override
        public String toString() { return msg; }
    }

    public static class NotThere extends MudException {
        public NotThere () { super.msg = "You can't do that when you're not there."; }
    }
    public static class AlreadyThere extends MudException {
        public AlreadyThere () { super.msg = "You can't go there; you're already there."; }
    }
    public static class NoSuchPerson extends MudException {
        public NoSuchPerson () { super.msg = "There isn't anyone by that name here."; }
    }
    public static class NoSuchRoom extends MudException {
        public NoSuchRoom () { super.msg = "There isn't any such room."; }
    }
    public static class FullRoom extends MudException {
        public FullRoom() { super.msg = "Room is full."; }
    }
    public static class WrongMove extends MudException {
        public WrongMove() { super.msg = "Wrong move."; }
    }
    public static class WrongStatus extends MudException {
        public WrongStatus() { super.msg = "Wrong status."; }
    }
    public static class RoomAlreadyExists extends MudException {
        public RoomAlreadyExists() { super.msg = "There is already a room with that name."; }
    }

    public static final String mudPrefix = "kozel.katsiaryna.tictactoe.rmi.";

}
.