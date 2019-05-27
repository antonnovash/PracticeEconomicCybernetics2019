package bsu.fpmi.educational_practice2018;
import java.beans.*;
import java.awt.*;

/**
 * This BeanInfo class provides additional information about the YesNoPanel
 * bean in addition to what can be obtained through  introspection alone.
 **/
public class FormBeanInfo extends SimpleBeanInfo {
    /**
     * Return an icon for the bean.  We should really check the kind argument
     * to see what size icon the beanbox wants, but since we only have one
     * icon to offer, we just return it and let the beanbox deal with it
     **/
    
    /** This is a convenience method for creating PropertyDescriptor objects */
    static PropertyDescriptor prop(String name, String description) {
	try {
	    PropertyDescriptor p = new PropertyDescriptor(name, FormBeanInfo.class);
	    p.setShortDescription(description);
	    return p;
	}
	catch(IntrospectionException e) { return null; } 
    }

    // Initialize a static array of PropertyDescriptor objects that provide
    // additional information about the properties supported by the bean.
    // By explicitly specifying property descriptors, we are able to provide
    // simple help strings for each property; these would not be available to
    // the beanbox through simple introspection.  We are also able to register
    // a special property editors for the messageText property
    static PropertyDescriptor[] props = {
		prop("messageText", "The message text that appears in the bean body"),
		prop("alignment", "The alignment of the message text"),
		prop("buttonLabel", "The label for the button"),
		prop("font", "The font for the message and buttons, and textField"),
    };
    
    /** Return the property descriptors for this bean */
    public PropertyDescriptor[] getPropertyDescriptors() { return props; }
    
    /** The message property is most often customized; make it the default */
    public int getDefaultPropertyIndex() { return 0; }
}
