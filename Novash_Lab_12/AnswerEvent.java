package bsu.fpmi.educational_practice2018;
/**
 * The YesNoPanel class fires an event of this type when the user clicks one
 * of its buttons.  The id field specifies which button the user pressed.
 **/
public class AnswerEvent extends java.util.EventObject {
    public static final int ACCEPT = 0, WRITE = 1;  // Button constants
    protected int id;
    protected String message;									// Which button was pressed?
    public AnswerEvent(Object source, String message, int id) {
		super(source);
		this.id = id;
		this.message = message;
    }
    public int getID() { return id; }       
    public String getMessage() { return this.message; } 
}
