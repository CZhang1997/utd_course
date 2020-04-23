package rate.my.professor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Professor_Rating {
	private HttpClient client;
	private HttpEntity entity;
	public Professor_Rating()
	{
		client = HttpClientBuilder.create().build();
	}
	public Professor_Rating(HttpClient c)
	{
		client = c;
	}
	private BufferedReader request(String url)
	{
		HttpGet request = new HttpGet(url);
		if(entity != null)
		{
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			HttpResponse response = client.execute(request);
			entity = response.getEntity();
			BufferedReader re = new BufferedReader(new InputStreamReader(entity.getContent()));
			return re;
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Professor get(String name) throws IOException
	{
		name = name.trim();
		String temp = search(name);
		if (temp.length() == 0)
		{
			return null;
		}
		String url = "https://www.ratemyprofessors.com" + temp;
		BufferedReader re = request(url);
		String grade = "0";
		String hard = "0";
		if(re != null)
		{
			String line = "";
			while (line != null)
			{
				//System.out.println(line);
				if(line.indexOf("Overall Quality") != -1)
				{
					//line = re.readLine();
					//System.out.println(line);
					int inde = line.indexOf("</div><div class=\"RatingValue__Denominator-qw8sqy-4 kLCgxI\">");
					grade = line.substring(inde - 3, inde);
					grade = getDigit(grade);
					//System.out.println(grade);
				}
				if(line.indexOf("Level of Difficulty")!= -1)
				{
					int inde = line.indexOf("<div class=\"FeedbackItem__FeedbackDescription-uof32n-2 hddnCs\">Level of Difficulty");
					hard = line.substring(inde - 9, inde - 6);
					hard = getDigit(hard);
					break;
				}
				line = re.readLine();
			}
		}
		return new Professor(name, Double.parseDouble(grade),Double.parseDouble(hard));
	}
	public String search(String name)
	{
		String url = "https://www.ratemyprofessors.com/search.jsp?query=" + name.replace(" ", "+");
		String school = "The University of Texas at Dallas";
		String[] names = name.split(" ");
		BufferedReader re = request(url);
		if(re != null)
		{
			String url2 = "";
			String line = "";
			int count = 0;
			boolean list = false;
			while(line != null)
			{
				if(list)
				{
					count ++;
					if(count == 1)
					{
						url2 = line.substring(line.indexOf('"')+1, line.indexOf("\">"));
					}	
//					System.out.println(count + ":" + line);
					if(count == 7)
					{
						
						if(line.indexOf(names[0])!=-1 && line.indexOf(names[1])!= -1)
						{
							String nextLine = "";
							try {
								nextLine = re.readLine();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(nextLine.indexOf(school) != -1)
							{
								return url2;
							}
						}
						else
						{
							System.out.println(line);
						}
						list = false;
					}

				}
				else
				{
					list = line.indexOf("listing PROFESSOR") != -1;
				}
				try {
					line = re.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					line = null;
					System.out.println("forever here");
				}
			}
		}
		else
		{
			System.out.println("request error");
		}
		return "";
	}
	private String getDigit(String s)
	{
		String ret = "";
		for(int i = 0; i < s.length(); i++)
		{
			if((s.charAt(i) <= '9' && s.charAt(i) >= '0') || s.charAt(i) == '.')
				ret += s.charAt(i);
		}
		
		return ret;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Professor_Rating rate = new Professor_Rating();
		
		System.out.println(rate.get("Anjum Chida"));
	}

}
