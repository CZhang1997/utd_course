package utd.course;
import rate.my.professor.*;

public class Course implements Comparable{
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
	public Professor getProf()
	{
		return instructor;
	}
	public String toString()
	{
		return number + "\t" + status + "\t" + term + "\n" + name  + "\n" + instructor  + "\n" + day  + "\t" + time  + "\t" + location;
	}

	@Override
	public int compareTo(Object o) {
		Course a = (Course)o;
		int dif = (int)(instructor.getScore() * 10 - a.getProf().getScore() * 10);
		if(dif == 0)
			return -1;
		return dif;
	}
	
}
