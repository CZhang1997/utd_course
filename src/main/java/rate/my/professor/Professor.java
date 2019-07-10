package rate.my.professor;

public class Professor {
	String name;
	Double overall;
	Double difficulty;
	public Professor(String n, double o, double d)
	{
		name = n;
		overall = o;
		difficulty = d;
	}
	public String getName()
	{
		return name;
	}
	public double getScore()
	{
		return overall;
	}
	public String toString()
	{
		return "Professor Name = "+ name + ", Overall Grade = " + overall + ", Difficulty = " + difficulty;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Professor p = new Professor("jack he", 4.0, 1.2);
		System.out.println(p);
	}
}
