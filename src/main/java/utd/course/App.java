 package utd.course;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.http.client.ClientProtocolException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ClientProtocolException, IOException
    {
    	String text = "enter 1 - 200 for the size of the window";
		JLabel l = new JLabel(text);
		l.setFont(new Font("Arial", Font.BOLD, 25));
		String ret = JOptionPane.showInputDialog(l);
		try { 
			int si = Integer.parseInt(ret);
	        UTD_Course_Book book = new UTD_Course_Book(si);
		}
		catch (Exception e23)
		{
			UTD_Course_Book book = new UTD_Course_Book(100);
		}
    }
}
