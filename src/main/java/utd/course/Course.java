package utd.course;
import rate.my.professor.*;

public class Course {
	private String status;
	private String location;
	private String number;
	private String name;
	private String day;
	private String time;
	private Professor instructor;
	private String term;
	
	public Course(String s, String l, String n, String n2, String d, String t, Professor i, String te)
	{
		status = s;
		location = l;
		number = n;
		name = n2;
		day = d;
		time = t;
		instructor = i;
		term = te;
	}
	
	public String toString()
	{
		return term  + "\n" + status + "\n" + number  + "\n" + name  + "\n" + instructor  + "\n" + day  + "\n" + time  + "\n" + location;
	}
}
