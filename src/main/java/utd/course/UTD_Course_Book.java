package utd.course;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.border.Border;

import org.apache.http.client.ClientProtocolException;

public class UTD_Course_Book extends JFrame implements ActionListener, MouseListener{

	// window dimensions
		private static int FRAME_WIDTH       = 1200; // adjust to have a wider/slimmer window
	 	private static int FRAME_HEIGHT      = 700; // adjust to have a taller/shorter window
	 	private static final int FRAME_X_ORIGIN    = 100;
	 	private static final int FRAME_Y_ORIGIN    = 100;
	 	private static int FONT_SIZE    = 25;
	 	private CourseManager manager;
		// declare GUI elements
		private JLabel search;
		private JTextArea display;
		private JMenuBar menuBar;
		private JMenu menu;
		private JMenuItem save, load, quit, help;
		private JScrollPane scroller;
		private String announcement;
		
		String defaultTerm = "20f";
		
		public UTD_Course_Book(int size)
		{
			manager = CourseManager.getInstance();
			// set up the window
			display = new JTextArea();
			reSize(size);
			setTitle("UTD Course Book");
	 		
	 		setResizable(true);
	 		setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
	 	// set up the main display area and make it scrollable
	 		
	 		display.setFont(new Font("Arial",Font.BOLD,FONT_SIZE + 5));
	 		display.setEditable(false);
//	 		display.setBorder(BorderFactory.createLineBorder(Color.blue));
	 		display.setLineWrap(true);
	 		scroller = new JScrollPane(display);
			String ann;
			try {
				ann = manager.getAnnouncement();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ann= "";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ann= "";
			}
			announcement = "          " + ann + "\n";
			if(announcement.indexOf("FALSE") != -1)
			{
				String url = announcement.indexOf("http") == -1 ? "": announcement.substring(announcement.indexOf("http"));
				String text = "Sorry, this app is currenly not available, for more imformation please go to " + url;
				JLabel l = new JLabel(text);
				l.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
				JOptionPane.showMessageDialog(null,l);
				System.exit(0);
			}
			display.setText(announcement);
			// set up the menu bar
			menuBar = new JMenuBar();
			setJMenuBar(menuBar);	
			save = new JMenuItem("Info");
			quit = new JMenuItem("Quit");
			load = new JMenuItem("Resize");
			help = new JMenuItem("Help");	
			save.addActionListener(this);
			help.addActionListener(this);
			load.addActionListener(this);
			quit.addActionListener(this);
			menu = new JMenu("menu");
			menu.setFont(new Font("Arial",Font.BOLD,FONT_SIZE));
			menu.add(save);
			menu.add(load);
			menu.add(help);
			menu.add(quit);
			menuBar.add(menu);
			JLabel temp = new JLabel("       ");
			menuBar.add(temp);
			search = new JLabel("Search");
			search.setFont(new Font("Arial",Font.BOLD,FONT_SIZE));
			search.addMouseListener(this);
			menuBar.add(search);
//			menu.addActionListener(this);
	 		for(int i = 0; i < menu.getItemCount(); i ++)
	 		{
	 			menu.getItem(i).setFont(new Font("Arial",Font.BOLD,FONT_SIZE + 5));
	 		}
	 		
			setLayout(new GridLayout(1,1));
			add(scroller); 
			
			scroller.setBorder(BorderFactory.createEmptyBorder(FONT_SIZE, FONT_SIZE, FONT_SIZE, FONT_SIZE));
//			display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// when the user clicks the X in the top right corner, the program will exit
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true); // make it visible LAST
	 		
		}
		
		public void reSize(int size)
		{
			double d = size /100.0;
			FRAME_WIDTH = (int)(FRAME_WIDTH * d);
			FRAME_HEIGHT = (int)(FRAME_HEIGHT * d);
			FONT_SIZE = (int)(FONT_SIZE * d);
			display.setFont(new Font("Arial",Font.BOLD,FONT_SIZE + 5));
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
		}
		/**
		 * required by the MouseListener interface.  Invoked when the mouse is clicked.
		 * @param e the MouseEvent that triggered the method
		 */
		public void mouseClicked(MouseEvent e)
		{
			// nothing needed, but method required by interface
			System.out.println(e.getSource());
		}
		/**
		 * required by the MouseListener interface.  Invoked when the mouse is released.
		 * @param e the MouseEvent that triggered the method
		 */
		public void mouseReleased(MouseEvent e)
		{
			// nothing needed, but method required by interface
		}
		/**
		 * required by the MouseListener interface.  Invoked when no mouse buttons are clicked and 
		 * the mouse pointer enters a particular gui element.  In this implementation, if the mouse
		 * pointer enters one of the menu labels, the label is set to red.
		 * @param e the MouseEvent that triggered the method
		 */
		public void mouseEntered(MouseEvent e)
		{
			JLabel label = (JLabel) e.getSource();
			label.setForeground(Color.red);
		}
		/**
		 * required by the MouseListener interface.  Invoked when no mouse buttons are clicked and 
		 * the mouse pointer leaves a particular gui element.  In this implementation, if the mouse
		 * pointer leaves one of the menu labels, the label is set to back to black.
		 * @param e the MouseEvent that triggered the method
		 */
		public void mouseExited(MouseEvent e)
		{
			JLabel label = (JLabel) e.getSource();
			label.setForeground(Color.black);
		}
		
		/**
		 * required by the MouseListener interface.  Invoked when the left mouse button is pressed.
		 * In this implementation, if the mouse is pressed on a particular labels, that menu item
		 * is invoked.
		 * @param e the MouseEvent that triggered the method
		 */
		public void mousePressed(MouseEvent e) 
		{
			JLabel label = (JLabel) e.getSource();
			
			if(label.equals(search))
			{
				display.setText(announcement + "loading...");
				JLabel ques = new JLabel("what is the course? ex: cs3345.");
				ques.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
				String course = JOptionPane.showInputDialog(ques);
				ques.setText("in what term? 20f?");// = new JLabel("in what term? 19f?");
				//ques.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
				String term = JOptionPane.showInputDialog(ques);
				course = course.replaceAll(" ", "");
				if(term.length() == 0)
					term = defaultTerm;
				if(course.length() != 0 || term.length() != 0)
				try {
					TreeSet<Course> courses = manager.searchCourses(course, term);
					if(courses == null || courses.size() == 0)
					{
						display.setText(announcement + "N/A");
					}
					else
					{
						String text = announcement + "Results:\n";
						text+="///////////////////////////////////////////////////////////////////////////////////////////////////////////////\n";
						for(Course c: courses)
						{
							text+= c;
							text+="\n///////////////////////////////////////////////////////////////////////////////////////////////////////////////\n\n";
						}
						display.setText(text);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					display.setText(announcement + "N/A");
					e1.printStackTrace();
				}
				catch (java.lang.StringIndexOutOfBoundsException e2)
				{
					display.setText(announcement + "N/A");
				}
				else
				{
					display.setText(announcement + "N/A");
				}
			}
		}
		
		/**
		 * actionPerformed is required by the ActionListener interface.  
		 * It is invoked whenever one of the menu items is selected.
		 * The possible menu items that can be activated are quit, save and load.
		 * @param e the ActionEvent that triggered the method
		 */
		public void actionPerformed(ActionEvent e)
		{
			String menuName = e.getActionCommand();
			if (menuName.equals("Quit"))
				System.exit(0);
			else if (menuName.equals("Info"))
			{
				String text = announcement + " Bugs report to zhang121215028@gmail.com";
				JLabel l = new JLabel(text);
				l.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
				JOptionPane.showMessageDialog(null,l);
			}
			else if(menuName.equals("Help"))
			{	
				String text = "Click Search and the enter course number like cs3340, then enter term 19f = 2019 fall, 19u = 2019 summer, and 19s = 2019 spring";
				JLabel l = new JLabel(text);
				l.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
				JOptionPane.showMessageDialog(null,l);
			}
			else if(menuName.equals("Resize"))
			{	
				String text = "enter 1 - 200 for the size";
				JLabel l = new JLabel(text);
				l.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
				String ret = JOptionPane.showInputDialog(l);
				try {
					int si = Integer.parseInt(ret);
					reSize(si);
				}
				catch (Exception e23)
				{
					System.out.println(e23.fillInStackTrace());
				}
			}
		}
		
		
		
		
}
