package utd.course;
import rate.my.professor.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

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
		rate = new Professor_Rating(client);
		pros = new HashMap<>();
		pros.put("-Staff-", new Professor("-Staff-",0,0));
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
			//System.out.println(na);
			try {
				instr = pros.get(na);
				if(instr == null)
				{
					//instr = new Professor(na);
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
		if(line.indexOf("location") != -1)
			location = getBet(line, "ion\">", "</div>");
		else
			location = "Online";
//		System.out.println(line);
//		System.out.println("location is " + location);
		day = getBet(line, "day\">", "</span>");
		String temp = line.substring(line.indexOf("</span>") + 5);
		time = getBet(temp,"ime\">", "</span>");
		return new Course(status, location, number, name, day, time, instr, t);
		
	}
	public TreeSet<Course> searchCourses(String course, String term) throws IOException
	{
		String url = "https://coursebook.utdallas.edu/" + course + "/term_" + term +"?";
		LinkedList<String> s = request(url, term);
		System.out.println("search found " + s.size() + " course, checking rate my professor");
		
		TreeSet<Course> c = new TreeSet<>();
		for(String i: s)
		{
			//System.out.println();
			c.add(parse(i));
		}
		
		return c;
	}
	public String getAnnouncement() throws ClientProtocolException, IOException
	{
		String url = "https://superrainz.github.io/";
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		entity = response.getEntity();
		BufferedReader re = new BufferedReader(new InputStreamReader(entity.getContent()));
		if(re != null)
		{
			String line = "";
			while(line != null)
			{
				int index = line.indexOf("Announcement:");
				if(index != -1)
				{
					index += "Announcement:".length();
					return line.substring(index, line.indexOf("end"));
				}
				line = re.readLine();
				
			}
		}
		return "";
	}
	public LinkedList<String> request(String url, String term) throws IOException
	{
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
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
		System.out.println(re);
		while(line != null)
		{
			if(line.indexOf("</tbody>") != -1)
				break;
			if(tbody)
			{
				if(line.indexOf("<td>"+term.toUpperCase()+ "<br>") != -1)
				{
					line = line.substring(line.indexOf("<td>"+term.toUpperCase()+ "<br>") + 4);
					if(cur.length() == 0)
					{
						cur += line;
					}
					else
					{
						data.add(cur);
						cur = line;
					}
				}
				else {
					cur += line;
				}
			}
			line = re.readLine();
			if(!tbody && line.indexOf("<tbody>")!= -1)
			{
				tbody = true;
				line = re.readLine();
			}
		}
		int i = cur.indexOf("<td>"+term.toUpperCase()+ "<br>");
		if(i != -1)
		{
			data.add(cur.substring(0, i));
			data.add(cur.substring(i+4));
		}
		else
		{
			data.add(cur);
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
		CourseManager m = new CourseManager();
		TreeSet<Course> courses = m.searchCourses("cs6375", "20f");
			String text = "Results:\n";
			text+="///////////////////////////////////////////////////////////////////////////////////////////////////////////////\n";
			for(Course c: courses)
			{
				text+= c;
				text+= "\n";
			}
			System.out.println(text);
	}
}
