import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class MudRoom extends UnicastRemoteObject implements Mud.RemoteMudRoom, Serializable {
    private static final int MAX_COUNT_OF_PERSONS = 2;
    private MudServer server;
    private String roomName;
    private char[][] board = new char[][]{ {'.', '.', '.'}, {'.', '.', '.'}, {'.', '.', '.'} };
    private int boardSize = 3;
    private transient Vector<String> names = new Vector<>();
    private transient Vector<Mud.RemoteMudPerson> people = new Vector<>();

    protected MudRoom() throws RemoteException { super(); }

    /**
     * This constructor creates a room, and calls a server method to register the object
     * so that it will be accessible by name.
     * @param server room will be created on this server.
     * @param roomName name for this room.
     * @throws RemoteException
     * @throws Mud.RoomAlreadyExists
     */
    public MudRoom (MudServer server, String roomName) throws RemoteException, Mud.RoomAlreadyExists {
        this.server = server;
        this.roomName = roomName;
        server.addRoom(this, roomName);
    }

    @Override
    public String getRoomName() throws RemoteException { return roomName; }
    @Override
    public char[][] getBoard() throws RemoteException { return board; }
    @Override
    public int getBoardSize() throws RemoteException { return boardSize; }
    @Override
    public Vector getNames() throws RemoteException { return names; }
    @Override
    public Mud.RemoteMudPerson getPerson(String name) throws RemoteException, Mud.NoSuchPerson {
        synchronized (names) {
            int i = names.indexOf(name);
            if ( i == -1 )
                throw new Mud.NoSuchPerson();
            return people.get(i);
        }
    }
    @Override
    public void speak(Mud.RemoteMudPerson speaker, String message) throws RemoteException, Mud.NotThere {
        String name = verifyPresence(speaker);
        tellEveryone(name + ":" + message);
    }
    @Override
    public void act(Mud.RemoteMudPerson actor, String message) throws RemoteException, Mud.NotThere {
        String name = verifyPresence(actor);
        tellEveryone(name + " " + message);
    }
    @Override
    public void move(Mud.RemoteMudPerson person, int x, int y) throws RemoteException, Mud.NotThere, Mud.WrongMove, Mud.WrongStatus {
        String name = verifyPresence(person);
        if ( person.getStatus() == Mud.RemoteMudPerson.WAIT ) throw new Mud.WrongStatus();

        boolean continueGame = true;
        int anotherIndex = -1;
        String anotherName = "";
        synchronized (board) {
            try {
                if ( board[x-1][y-1] == '.' )
                    board[x-1][y-1] = person.getIcon();
                else
                    throw new Mud.WrongMove();
            } catch (ArrayIndexOutOfBoundsException e) { throw new Mud.WrongMove(); }
            person.setStatus(Mud.RemoteMudPerson.WAIT);
            synchronized (names) {
                anotherIndex = names.indexOf(name);
                if ( anotherIndex == 0 ) anotherIndex++;
                else anotherIndex--;
                anotherName = names.get(anotherIndex);
            }
            String message = null;
            if ( isEnd() )
                message = name + " winner.\n" + anotherName + " loser.\n";
            else if ( isFull() )
                message = "DRAW!!!\n";
            if ( message != null ) {
                tellEveryone(message);
                continueGame = false;
            }
        }
        tellEveryone("Result:\n" + getBoardAsString());
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        if ( continueGame )
            synchronized (names) {
                Mud.RemoteMudPerson anotherPerson = people.get(anotherIndex);
                anotherPerson.tell("You can move.\n");
                anotherPerson.setStatus(Mud.RemoteMudPerson.MOVE);
            }
        else tellEveryone("Game is over. You can exit room, if you want to play again.\n");
    }
    @Override
    public Mud.RemoteMudServer getServer() throws RemoteException { return server; }
    @Override
    public void exit(Mud.RemoteMudPerson who, String message) throws RemoteException, Mud.NotThere {
        synchronized (names) {
            int i = people.indexOf(who);
            if ( i == -1 ) throw new Mud.NotThere();
            names.removeElementAt(i);
            people.removeElementAt(i);
            board = new char[][]{ {'.', '.', '.'}, {'.', '.', '.'}, {'.', '.', '.'} };
            if ( names.size() != 0 )
                people.get(0).setStatus(Mud.RemoteMudPerson.WAIT);
        }
        if ( message != null ) tellEveryone(message + "\n");
    }
    @Override
    public void enter(Mud.RemoteMudPerson who, String name, String message) throws RemoteException, Mud.AlreadyThere, Mud.FullRoom {
        int currSize = 0;
        synchronized (names) {
            if ( (currSize = names.size()) == MAX_COUNT_OF_PERSONS )
                throw new Mud.FullRoom();
        }
        if ( message != null )
            tellEveryone(message);
        synchronized (names) {
            if ( people.indexOf(who) != -1 ) throw new Mud.AlreadyThere();
            Mud.RemoteMudPerson pers = null;
            if ( currSize == 1 ) pers = people.get(0);
            char icon = (currSize==0) ? 'X' : (pers.getIcon()=='X') ? 'O' : 'X';
            who.setIcon(icon);
            who.setStatus(Mud.RemoteMudPerson.WAIT);
            names.addElement(name);
            people.add(who);
            if ( currSize == 1 ) {
                pers.setStatus(Mud.RemoteMudPerson.MOVE);
                pers.tell("You can move.\n");
            }
        }
    }

    private boolean isEnd() {
        for (int i = 0; i < boardSize; ++i) {
            if ( board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][2] != '.' )
                return true;
            if ( board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[2][i] != '.' )
                return true;
        }
        if ( board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] != '.' )
            return true;
        if ( board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] != '.' )
            return true;
        return false;
    }
    private boolean isFull() {
        int a = 0;
        for (int i = 0; i < boardSize; ++i)
            for (int j = 0; j < boardSize; ++j)
                if ( board[i][j] == '.' )
                    a++;
        return a == 0;
    }
    private String getBoardAsString() {
        synchronized (board) {
            String res = "";
            for (int i = 0; i < boardSize; ++i) {
                for (int j = 0; j < boardSize; ++j)
                    res += board[i][j];
                res += "\n";
            }
            return res;
        }
    }
    /**
     * Create and start a thread that sends out a message everyone in this place. If it gets
     * a RemoteException talking to a person, it silently removes that person from this place.
     * This is not a remote method, but is used internally by a number of remote methods.
     * @param message text of the message.
     */
    protected void tellEveryone (final String message) {
        if ( people.size() == 0 ) return;
        final Vector recipients = (Vector) people.clone();
        new Thread() {
            @Override
            public void run() {
                for (Object person: recipients) {
                    try {
                        ((Mud.RemoteMudPerson)person).tell(message);
                    } catch (RemoteException e) {
                        try {
                            MudRoom.this.exit((Mud.RemoteMudPerson)person, null);
                        } catch (Exception e1) {}
                    }
                }
            }
        }.start();
    }
    /**
     * This convenience method checks whether the specified person is here.
     * If so, it returns their name.  If not it throws a NotThere exception.
     * @param who person, who will be checked.
     * @return name of the person, if he's here.
     * @throws Mud.NotThere if checked person isn't here.
     */
    protected String verifyPresence (Mud.RemoteMudPerson who) throws Mud.NotThere {
        synchronized (names) {
            int i = people.indexOf(who);
            if ( i == -1 )
                throw new Mud.NotThere();
            return names.get(i);
        }
    }
    private void readObject (ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        names = new Vector<>();
        people = new Vector<>();
    }
}
