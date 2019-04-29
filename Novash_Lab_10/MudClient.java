import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Vector;

public class MudClient {

    /**
     * The main program.  It expects two or three arguments:
     *   0) the name of the host on which the mud server is running (for example "Vadimka-PC")
     *   1) the name of the MUD on that host (for example "myMud")
     *   2) the name of a room within that MUD to start at (optional, for example "Room1").
     **/
    public static void main(String[] args) {
        try {
            String hostname = args[0];
            String mudName = args[1];
            String roomName = null;
            if ( args.length > 2 )
                roomName = args[2];

            Mud.RemoteMudServer server = (Mud.RemoteMudServer) Naming.lookup("rmi://" + hostname + "/" + Mud.mudPrefix + mudName);
            Mud.RemoteMudRoom location;
            if ( roomName == null )
                location = server.getEntrance();
            else
                location = server.getNamedPlace(roomName);

            System.out.println("Welcome to " + mudName);
            String name = getLine("Enter your name: ");
            PrintWriter personOut = new PrintWriter(System.out);
            MudPerson me = new MudPerson(name, personOut);

            int pri = Thread.currentThread().getPriority();
            Thread.currentThread().setPriority(pri - 1);

            start(location, me);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Usage: java MudClient <host> <mud> [<room>]");
            System.exit(1);
        }
    }

    public static void start (Mud.RemoteMudRoom entrance, MudPerson me) throws RemoteException {
        Mud.RemoteMudRoom location = entrance;
        String myName = me.getName();
        String roomName = null;
        String mudName = null;

        try {
            roomName = location.getRoomName();
            mudName = location.getServer().getMudName();
            location.enter(me, myName, myName + " has entered the MUD.\n");
            look(location);
        } catch (Mud.FullRoom fr) {
            System.out.println(mudName + "." + roomName + " is full.\nTry to connect later.");
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        while (true) {
            try {
                try { Thread.sleep(200); } catch (InterruptedException ie) {}

                me.changeCmdStatus();
                String line = getLine(mudName + "." + roomName + "> ");
                me.changeCmdStatus();
                String cmd, arg;
                int i = line.indexOf(' ');
                if ( i == -1 ) {
                    cmd = line.toLowerCase();
                    arg = null;
                } else {
                    cmd = line.substring(0, i).toLowerCase();
                    arg = line.substring(i + 1);
                }
                if ( arg == null ) arg = "";

                if ( cmd.equals("look") ) look(location);
                else if ( cmd.equals("say") ) location.speak(me, arg + "\n");
                else if ( cmd.equals("do") ) location.act(me, arg + "\n");
                else if ( cmd.equals("move") ) {
                    int idx = arg.indexOf(' ');
                    int x = Integer.parseInt(arg.substring(0, idx));
                    int y = Integer.parseInt(arg.substring(idx + 1));
                    location.move(me, x, y);
                } else if ( cmd.equals("quit") ) {
                    try { location.exit(me, myName + " has quit."); } catch (Exception e) {}
                    System.out.println("Good bye.");
                    System.out.flush();
                    System.exit(0);
                } else if ( cmd.equals("help") ) System.out.println(help);
                else System.out.println("Unknown command. Try 'help'.");

            } catch (Mud.MudException e) {
                System.out.println(e);
            } catch (RemoteException e) {
                System.out.println("The MUD is having technical difficulties.\nPerhaps the server has crashed:\n" + e);
            } catch (Exception e) {
                System.out.println("Syntax or other error:\n" + e + "\nTry using the 'help' command.");
            }
        }
    }

    /**
     * This convenience method is used in several places in the start() method above.
     * It displays the name and board of the current game room (including the name
     * of the mud the room is in), and also displays the list of people in the current place.
     * @param r game room, which will be described.
     * @throws RemoteException
     */
    public static void look (Mud.RemoteMudRoom r) throws RemoteException {
        String mudName = r.getServer().getMudName();
        String roomName = r.getRoomName();
        char[][] board = r.getBoard();
        int boardSize = r.getBoardSize();
        Vector names = r.getNames();

        System.out.println("You're in " + roomName + " of the Mud:" + mudName);
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j)
                System.out.print(board[i][j]);
            System.out.println();
        }
        System.out.println("People here:");
        for (int i = 0; i < names.size(); ++i) {
            if ( i > 0 )
                System.out.print(", ");
            System.out.print(names.elementAt(i));
        }
        System.out.println();
        System.out.flush();
    }
    /**
     * A convenience method for prompting the user and getting a line of input.
     * It guarantees that the line is not empty and strips off whitespace at
     * the beginning and end of the line.
     * @param prompt prompt message.
     * @return line of input.
     */
    private static String getLine (String prompt) {
        String line = null;
        do {
            try {
                System.out.print(prompt);
                System.out.flush();
                line = in.readLine();
                if ( line != null )
                    line = line.trim();
            } catch (Exception e) {}
        } while ( (line == null) || (line.length() == 0) );
        return line;
    }

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static final String help =
            "Commands are:\n" +
            "\tlook: Look around\n" +
            "\tsay <message>: say something to everyone\n" +
            "\tdo <message>: tell everyone that you are doing something\n" +
            "\tmove <x> <y>: put a label on (x,y)-coordinates. x,y=1,2,3\n" +
            "\tquit: leave the Mud\n" +
            "\thelp: display this message";

}
