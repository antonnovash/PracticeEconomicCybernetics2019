package bsu.fpmi.educational_practice2018;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * This JavaBean displays a multi-line message and up to three buttons.  It
 * fires an AnswerEvent when the user clicks on one of the buttons
 **/
public class Form extends Panel {
    // Properties of the bean.
	protected String text, path = "./src/baseline_check_black_18dp.png";
    protected String messageText;  // The message to display
    protected int alignment;
    protected String buttonLabel;     // Text for the yes, no, & cancel buttons
    
    // Internal components of the panel
    protected Label message;
    protected JLabel picLabel;
    protected Button button;
    protected TextField textField;
    
    /** The no-argument bean constructor, with default property values 
     * @throws IOException */
    public Form() throws IOException { this("Your\nMessage\nHere", "Input smth"); }

    public Form(String messageText, String textField) throws IOException { 
    	this(messageText, Label.LEFT, "Button", textField);
    }
    
    /** A constructor for programmers using this class "by hand" 
     * @throws IOException */
    public Form(String messageText, int alignment, String buttonLabel, String field) throws IOException {
	// Create the components for this panel
    	setLayout(new BorderLayout(15, 15));
    	
    	BufferedImage myPicture = ImageIO.read(new File(path));
    	picLabel = new JLabel(new ImageIcon(myPicture));
    	picLabel.setVisible(false);
	
	// Put the message label in the middle of the window. new Label(MultiLineLabel(messageText, 20, 20, alignment);
    	message = new Label(messageText, alignment); //???????????????
    	add(message, BorderLayout.CENTER);
	
	// Create a panel for the Panel buttons and put it at the bottom
	// of the Panel.  Specify a FlowLayout layout manager for it.
    	Panel buttonbox = new Panel();
    	buttonbox.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 15));
    	add(buttonbox, BorderLayout.SOUTH);
	
	// Create each specified button, specifying the action listener
	// and action command for each, and adding them to the buttonbox
    	button = new Button();                   // Create buttons
    	
	// Add the buttons to the button box
    	textField = new TextField(field);
    	
    	System.out.println(textField.getText());
    	
    	buttonbox.add(picLabel);
    	
    	buttonbox.add(textField);
    	
    	buttonbox.add(button);
    	
    	

	// Register listeners for each button

    	button.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
			    fireEvent(new AnswerEvent(Form.this,  Form.this.textField.getText(),
						      AnswerEvent.ACCEPT));
			    setText(Form.this.textField.getText());
			}
		});

    	
    	textField.addKeyListener(new KeyListener() {
    		public void keyPressed(KeyEvent e) {
    			picLabel.setVisible(false);
    			 fireEvent(new AnswerEvent(Form.this, Form.this.textField.getText(), AnswerEvent.WRITE));     
            }

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
    	});
	
		// Now call property setter methods to set the message and button
		// components to contain the right text
		setMessageText(messageText);
		setAlignment(alignment);
		setButtonLabel(buttonLabel);
    }
    
    // Methods to query all of the bean properties.
    public String getMessageText() { return messageText; }
    public int getAlignment() { return alignment; }
    public String getButtonLabel() { return buttonLabel; }

    
    // Methods to set all of the bean properties.
    public void setMessageText(String messageText) {
		this.messageText = messageText;
		message.setText(messageText);
		validate();
    }
    
    private void setText(String text) {
		this.text = text;
		this.picLabel.setVisible(true);
    }
    
    public void setIconPath(String path) {
		this.path = path;
    }
    
    private String getText() {
		return this.text;
    }

    public void setAlignment(int alignment) {
		this.alignment = alignment;
		message.setAlignment(alignment);
    }

    public void setButtonLabel(String l) {
		buttonLabel = l;
		button.setLabel(l);
		button.setVisible((l != null) && (l.length() > 0));
		validate();
    }

    public void setFont(Font f) {
		super.setFont(f);    // Invoke the superclass method
		message.setFont(f);  
		button.setFont(f);
		textField.setFont(f);
		validate();
    }
    
    /** This field holds a list of registered ActionListeners. */
    protected Vector<AnswerListener> listeners = new Vector<AnswerListener>();
    
    /** Register an action listener to be notified when a button is pressed */
    public void addAnswerListener(AnswerListener l) {
    	listeners.addElement(l);
    }
    
    /** Remove an Answer listener from our list of interested listeners */
    public void removeAnswerListener(AnswerListener l) {
    	listeners.removeElement(l);
    }
    
    /** Send an event to all registered listeners */
    public void fireEvent(AnswerEvent e) {
	// Make a copy of the list and fire the events using that copy.
	// This means that listeners can be added or removed from the original
	// list in response to this event.  We ought to be able to just use an
	// enumeration for the vector, but that doesn't actually copy the list.
	Vector list = (Vector) listeners.clone();
	for(int i = 0; i < list.size(); i++) {
	    AnswerListener listener = (AnswerListener)list.elementAt(i);
	    switch(e.getID()) {
		    case AnswerEvent.ACCEPT: listener.accept(e); break;
		    case AnswerEvent.WRITE:  listener.write(e); break;
	    }
	}
    }
    
    /** A main method that demonstrates the class 
     * @throws IOException */
    public static void main(String[] args) throws IOException {
		// Create an instance of InfoPanel, with title and message specified:
		Form p = new Form("Do you really want to quit?", "hhh");
	
		// Register an action listener for the Panel.  This one just prints
		// the results out to the console.
		p.addAnswerListener(new AnswerListener() {
			public void write(AnswerEvent e) { 
				System.out.println("write");
		//		System.out.println(e.getMessage());
			}
			public void accept(AnswerEvent e) {
				System.out.println("accept"); 
				System.exit(0);
			}
		});
		
		Frame f = new Frame();
		f.add(p);
		f.pack();
		f.setVisible(true);
    }
}
