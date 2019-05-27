package bsu.fpmi.educational_practice2018;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;

public class FormIconEditor implements PropertyEditor {
	  protected String value;  // The value we will be editing.
	    
	   public void setValue(Object o) {  value = (String) o; }
	   public Object getValue() { return value; }
	   public void setAsText(String s) { value = s; }
	   public String getAsText() { return value; }
	   public String[] getTags() { return null; }  // not enumerated; no tags
	    
	    // Say that we allow custom editing.
	    public boolean supportsCustomEditor() { return true; }
	    
	    // Return the custom editor.  This just creates and returns a TextArea
	    // to edit the multi-line text.  But it also registers a listener on the
	    // text area to update the value as the user types and to fire the
	    // property change events that property editors are required to fire.
	    public Component getCustomEditor() {
	    	final TextArea t = new TextArea(value);
	    	t.setSize(300, 150); // TextArea has no preferred size, so set one
	    	t.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent e) {
			    value = t.getText();
			    listeners.firePropertyChange(null, null, null);
				}
		    });
	    	return t;
	    }
	    
	    // Visual display of the value, for use with the custom editor.
	    // Just print some instructions and hope they fit in the in the box.
	    // This could be more sophisticated.
	    public boolean isPaintable() { return false; }
	    public void paintValue(Graphics g, Rectangle r) { }
	    
	    // Important method for code generators.  Note that it really ought to
	    // escape any quotes or backslashes in value before returning the string.
	    public String getJavaInitializationString() { return "\"" + value + "\""; }
	    
	    // This code uses the PropertyChangeSupport class to maintain a list of
	    // listeners interested in the edits we make to the value.
	    protected PropertyChangeSupport listeners =new PropertyChangeSupport(this);
	    public void addPropertyChangeListener(PropertyChangeListener l) {
	    	listeners.addPropertyChangeListener(l);
	    }
	    public void removePropertyChangeListener(PropertyChangeListener l) {
	    	listeners.removePropertyChangeListener(l);
	   }
}

