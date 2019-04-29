﻿/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bsu.fpmi.educational_practice2017;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JLabel;
/**
 *
 * @author Kate
 */
public class Message extends Panel{
    protected String label;
    protected String button;
    protected char in;
    
    protected JLabel jL;
    protected Button Result;

    public Message(String label)
    { 
    	this(label, "Жми!");
    }
    
    public Message(String label, String button) 
    {
	// Create the components for this panel
	setLayout(new BorderLayout(15, 15));
        Panel fields=new Panel();
	jL=new JLabel();
        fields.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 15));
        add(fields,BorderLayout.NORTH);
	fields.add(jL);
        Result = new Button();
        Result.setSize(50, 20);
	add(Result, BorderLayout.SOUTH);
	
	Result.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    fireEvent(new AnswerEvent(Message.this,
					      AnswerEvent.MESS));
		}
	    });
        setLabel(label);
	setButton(button);
    }
    
    public String getLabel() { return label; }
    public String getButton() { return button; }
    
    public void setLabel(String label) {
	this.label = label;
	jL.setText(label);
	validate();
    }


    public void setButton(String button) {
	this.button = button;
	Result.setLabel(button);
	Result.setVisible((button != null) && (button.length() > 0));
	validate();
    }

    protected Vector<AnswerListener> listeners = new Vector<AnswerListener>();
    
    public void addAnswerListener(AnswerListener l) {
	listeners.addElement(l);
    }
    
    public void removeAnswerListener(AnswerListener l) {
	listeners.removeElement(l);
    }
    
    public void fireEvent(AnswerEvent e) {Vector list = (Vector) listeners.clone();
	for(int i = 0; i < list.size(); i++) {
	    AnswerListener listener = (AnswerListener)list.elementAt(i);
	    switch(e.getID()) {
	    case AnswerEvent.MESS: listener.Result(e); break;
	    }
	}
    }
}
    
