package utd.course;
import rate.my.professor.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class CourseManager {
	
	private static CourseManager instance;
	
	private HttpClient client;
	private ArrayList<Course> courses;
	private HashMap<String, Professor> pros;
	private Professor_Rating rate;
	private HttpEntity entity;
	public synchronized static CourseManager getInstance()
	{
		if (instance == null)
			instance = new CourseManager();
		return instance;
	}
	private CourseManager()
	{
		client = HttpClientBuilder.create().build();
		courses = new ArrayList<>();
		rate = new Professor_Rating(client);
		pros = new HashMap<>();
		pros.put("-Staff-", new Professor("-Staff-",0,0));
	}
	
	public void addCourse(Course c)
	{
		courses.add(c);
	}
	
	
	public Course parse(String line)
	{
		String status = "";
		String location;
		String number;
		String name;
		String day;
		String time;
		Professor instr;
		String t = line.substring(0, 3);
		if(line.indexOf("Closed") != -1)
			status = "Closed";
		else
			status = "Open";
		line = line.substring(line.indexOf("</span>"));
		number = getBet(line, "ble\">", "</a>");
		line = line.substring(line.indexOf("</a>")).substring(line.indexOf("</td>"));
		//System.out.println(line);
		name = getBet("<td>" + line, "<td>", "</td>");
		if(line.indexOf("-Staff-") == -1)
		{
			String na = getBet(line, "title=\"", "\">").trim();
			try {
				instr = pros.get(na);
				if(instr == null)
				{
					instr = rate.get(na);
					if(instr == null)
						instr = new Professor(na,0,0);
					pros.put(na, instr);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				instr = pros.get("-Staff-");
				e.printStackTrace();
			}
		}
		else
			instr = pros.get("-Staff-");
		location = getBet(line, "ion\">", "</div>");
		day = getBet(line, "day\">", "</span>");
		String temp = line.substring(line.indexOf("</span>") + 5);
		time = getBet(temp,"ime\">", "</span>");
		return new Course(status, location, number, name, day, time, instr, t);
		
	}
	public ArrayList<Course> searchCourses(String course, String term) throws IOException
	{
		String url = "https://coursebook.utdallas.edu/" + course + "/term_" + term +"?";
		LinkedList<String> s = request(url);
		ArrayList<Course> c = new ArrayList<>();
		for(String i: s)
		{
			c.add(parse(i));
		}
		return c;
	}
	public LinkedList<String> request(String url) throws IOException
	{
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		//int code = response.getStatusLine().getStatusCode();
		if(entity != null)
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		entity = response.getEntity();
		BufferedReader re = new BufferedReader(new InputStreamReader(entity.getContent()));
		String line = "";
		LinkedList<String> data = new LinkedList<>();
		String cur = "";
		boolean tbody = false;
		while(line != null)
		{
			if(tbody)
			{
				if(line.indexOf("location") == -1)
				{
					cur += line;
				}
				else
				{
					data.add(cur.substring(cur.indexOf("19F")));
					cur = "";
				}
				tbody = line.indexOf("tbody") == -1;
			}
			else
				tbody = line.indexOf("tbody") != -1;
			line = re.readLine();
		}
		return data;
	}
	
	public static String getBet(String source, String left, String right)
	{
		int l = source.indexOf(left);
		int r = source.indexOf(right);
		if(l != -1 && r != -1 && l < r)
		{
			return source.substring(l + left.length(), r);
		}
		return "";
	}
	public static void main(String[] args) throws ClientProtocolException, IOException
	{
		
	}
}
