package utd.course;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ClientProtocolException, IOException
    {
        System.out.println( "Hello World!" );
        CourseManager instance = CourseManager.getInstance();
		ArrayList<Course> list = instance.searchCourses("cs4337", "19f");
		for(Course c: list)
		{
			System.out.println(c);
		}
    }
}
