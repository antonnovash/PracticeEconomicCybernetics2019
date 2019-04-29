import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MudPerson extends UnicastRemoteObject implements Mud.RemoteMudPerson {

    private static final long serialVersionUID = 1L;
    private String name;
    private char icon;
    private PrintWriter tellStream;
    private int status = 0;
    private boolean cmdStatus = false;

    public MudPerson (String name, PrintWriter out) throws RemoteException {
        this.name = name;
        this.icon = '?';
        tellStream = out;
    }

    public String getName() { return name; }
    public void changeCmdStatus() { cmdStatus = !cmdStatus; }
    @Override
    public char getIcon() throws RemoteException { return icon; }
    @Override
    public void setIcon (char icon) throws RemoteException { this.icon = icon; }
    @Override
    public int getStatus() throws RemoteException { return status; }
    @Override
    public void setStatus(int status) throws RemoteException { this.status = status; }
    @Override
    public void tell(String message) throws RemoteException {
        if ( cmdStatus ) tellStream.println();
        tellStream.print(message);
        tellStream.flush();
    }
}
