/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaproject2;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Rossco
 */
public class PersonStore
{

    Scanner kb=new Scanner (System.in);

    private final Map<String, Person> hashMap;

    public PersonStore ()
    {
        hashMap=new HashMap<> ();
    }

    public void addPeople (Map<String, Person> hashMap) throws FileNotFoundException, IOException
    {
        File f=new File ("persons.txt");
        FileInputStream fis=new FileInputStream (f);
        DataInputStream dis=new DataInputStream (fis);

        while (dis.available () > 0)
        {
            double score=dis.readDouble ();
            String QueryName=readString (dis, 32);
            String name=readString (dis, 32);
            long id=dis.readLong ();
            String imageURL=readString (dis, 144);
            String personLink=readString (dis, 48);
            String comments=readString (dis, 16);
            int rating=dis.readInt ();
            Person p=new Person (score, QueryName, name, id, imageURL, personLink, comments, rating);

            hashMap.put (name, p);
            System.out.println (p);
        }

    }

    //works fine
    public LinkedList<Person> searchLocal (Map<String, Person> hashMap, String name) throws FileNotFoundException, IOException
    {
        LinkedList<Person> results=new LinkedList<> ();

        hashMap.entrySet ().stream ().forEach ((Map.Entry<String, Person> e)
                ->
                {
                    if (e.getKey ().contains (name))
                    {
                        results.add (e.getValue ());
                        System.out.println (e.getValue ().getName ());
                    }
        });
        if (results.isEmpty ())
        {
            System.out.println ("No Results in Local store , now searching site");
            LinkedList<Person> resultsSite=new LinkedList<> ();
            LinkedList<Person> resultsSiteSorted=new LinkedList<> ();
            System.out.println (name);
            resultsSite=searchSite (hashMap, name);
            if (resultsSite.isEmpty ())
            {
                System.out.println ("The site search returned no results for your search");
            }
            else
            {
                resultsSiteSorted=sortSiteByScore (resultsSite);

                for (int i=0; i < resultsSiteSorted.size (); i++)
                {
                    hashMap.put (resultsSiteSorted.get (i).getName (), resultsSiteSorted.get (i));
                }
                //writeToFile (resultsSiteSorted,"persons.txt");
                //System.out.println (resultsSiteSorted);
                //LinkedList<Person> people=new LinkedList<Person> (hashMap.values ());
                //people.addAll (resultsSiteSorted);
                //sortSiteByScore (people);
                System.out.println ("Do you wish to export your serach results to an html file Y/N");
                String ans=kb.next ();

                if ("y".equals (ans) || ans == "Y" || ans == "yes")
                {
                    System.out.println ("Please enter the name of the file you want to store your searh results to");
                    String nameHTML=kb.next ();
                    exportResultsHtml (resultsSiteSorted, nameHTML);
                }

            }
        }
        return results;
    }

    //  working fine
    public void listAll (Map<String, Person> hashMap)
    {

        hashMap.entrySet ().stream ().forEach ((e)
                ->
                {
                    System.out.println (e.getKey () + " = " + e.getValue ());
        });

    }

    // works fine
    public LinkedList<Person> searchSite (Map<String, Person> hashMap, String name)

    {
        LinkedList<Person> results=new LinkedList<> ();
        String url1="http://api.tvmaze.com/search/people?q=" + name;
        try
        {

            URL url=new URL (url1);  // an API returning JSON

            InputStream in=url.openStream ();

            JsonReader reader=Json.createReader (in);

            JsonArray array=reader.readArray ();  // top level object - first "{"
            //JsonArray = jsonArray.length();
            // having consumed the first "{", 
            // we can now access values by their key e.g. "time" etc

            int myRating=0;
            String myComments="";
            //String imageURL="";

            for (int i=0; i < array.size (); i++)
            {
                JsonObject object=array.getJsonObject (i);
                int score=object.getJsonNumber ("score").intValue ();
                //System.out.println (score);

                // query name
                JsonObject personObject=object.getJsonObject ("person");
                long id=personObject.getInt ("id");
                //System.out.println (id);

                String urlLink=(String) personObject.getJsonString ("url").getString ();
                //System.out.println (urlLink);

                String nameFromSite=(String) personObject.getJsonString ("name").getString ();
                //System.out.println (nameFromSite);

                String str=null;
                String img1="";

                if (personObject.isNull ("image"))
                {
                    // System.out.println ("inside null");
                    //personObject.put ("image", "medium");
                }
                else
                {
                    JsonObject imageObject=personObject.getJsonObject ("image");
                    img1=imageObject.getJsonString ("medium").getString ();
                    //  System.out.println (img1);
                    String img2=imageObject.getJsonString ("original").getString ();
                    // System.out.println (img2);
                    new StringBuilder ().append (img1).append (img2).toString ();

                }

                JsonObject linkObject=personObject.getJsonObject ("_links");
                JsonObject hrefObject=linkObject.getJsonObject ("self");

                String link2=hrefObject.getJsonString ("href").getString ();
                System.out.println (link2);
                //int score, String queryName, String name, long Id, String imageURL, String personLink, String myComments, int myRating
                Person p1=new Person (score, name, nameFromSite, id, img1, link2, myComments, myRating);

                hashMap.put (nameFromSite, p1);

                results.add (p1);

            }

        }

        catch (Exception ex)
        {
            System.out.println (ex);
        }
        return results;
    }

    // working fine
    public void sortLocalByID (Map<String, Person> hashMap)
    {
        List<Person> people=new ArrayList<Person> (hashMap.values ());
        Comparator<Person> byID=(e1, e2)->Long.compare (e1.getId (), e2.getId ());
        people.stream ();
        people.sort (byID);
        people.forEach ((Person)->System.out.println (Person));

    }
    // working fine

    public void sortLocalByRating (Map<String, Person> hashMap)
    {
        List<Person> people=new ArrayList<Person> (hashMap.values ());
        Comparator<Person> byRating=(e1, e2)->Long.compare (e1.getMyRating (), e2.getMyRating ());
        people.stream ();
        people.sort (byRating);
        people.forEach ((Person)->System.out.println (Person));

    }

    // working fine
    public LinkedList<Person> sortSiteByScore (LinkedList<Person> resultsSite)
    {
        Comparator<Person> byScore=(e1, e2)->Double.compare (e1.getScore (), e2.getScore ());
        resultsSite.stream ();
        resultsSite.sort (byScore);
        LinkedList<Person> results=new LinkedList<> ();
        results.addAll (0, resultsSite);
        results.forEach ((Person)->System.out.println (Person));

        return results;
    }

    public void sortByName (Map<String, Person> hashMap)
    {
        List<Person> people=new ArrayList<Person> (hashMap.values ());
        people.sort ((e1, e2)->e1.getName ().compareTo (e2.getName ()));
        people.stream ();
        people.forEach ((Person)->System.out.println (Person));

    }

    // working fine
    public void setRatingAndComments (Map<String, Person> hashMap, String name)
    {
        hashMap.entrySet ().stream ().forEach ((Map.Entry<String, Person> e)
                ->
                {
                    if (e.getKey ().equalsIgnoreCase (name))
                    {
                        System.out.println ("Please enter your rating for " + e.getKey ());
                        int rating=Integer.parseInt (kb.nextLine ());
                        e.getValue ().setMyRating (rating);

                        System.out.println ("Please enter the comments " + e.getKey ());
                        String comments=kb.nextLine ();
                        e.getValue ().setMyComments (comments);

                    }

        });

    }

    public void writeToFile (LinkedList<Person> people, String file) throws FileNotFoundException, IOException
    {
        File file2=new File (file);
        FileOutputStream fos=new FileOutputStream (file2);
        DataOutputStream dos=new DataOutputStream (fos);

        for (Person c : people)
        {
            dos.writeDouble (c.getScore ());
            dos.writeChars (pad (c.getQueryName (), 32));
            dos.writeChars (pad (c.getName (), 32));
            dos.writeLong (c.getId ());
            dos.writeChars (pad (c.getImageURL (), 144));
            dos.writeChars (pad (c.getPersonLink (), 48));
            dos.writeChars (pad (c.getMyComments (), 16));
            dos.writeInt (c.getMyRating ());

        }
        dos.flush ();
        dos.close ();
    }

    public void exportResultsHtml (List<Person> searchRes, String nameHTML) throws FileNotFoundException, IOException
    {
        String finalhtml=nameHTML + ".html";
        System.out.println ("finalhtml = " + finalhtml);
        File file2=new File (finalhtml);

        FileWriter fstream=new FileWriter (finalhtml);
        BufferedWriter out=new BufferedWriter (fstream);

        StringBuilder sb=new StringBuilder ();

        out.write ("<html><br>");
        out.write ("<head><br>");
        out.write ("<style type=\"text/css\">\n"
                + ".tg  {border-collapse:collapse;border-spacing:0;border-color:#aabcfe;}\n"
                + ".tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#aabcfe;color:#669;background-color:#e8edff;}\n"
                + ".tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#aabcfe;color:#039;background-color:#b9c9fe;}\n"
                + ".tg .tg-9hbo{font-weight:bold;vertical-align:top}\n"
                + ".tg .tg-6k2t{background-color:#D2E4FC;vertical-align:top}\n"
                + "</style>");
        out.write ("<title>finalhtml");
        out.write ("</title>");
        out.write ("</head>");
        out.write ("<body>");
        out.write ("<table class=\"tg\"><tr><th class=\"tg-9hbo\">Image</th><th class=\"tg-9hbo\">Name</th><th class=\"tg-9hbo\">Score</th><th class=\"tg-9hbo\">Query</th><th class=\"tg-9hbo\">ID</th><th class=\"tg-9hbo\">Person Link</th><th class=\"tg-9hbo\">Comments</th><th class=\"tg-9hbo\">Rating</th></tr>");

        for (int i=0; i < searchRes.size (); i++)
        {
            if (searchRes.get (i).getImageURL ().isEmpty ())
            {
                out.write ("<tr><td class=\"tg-6k2t\"><img src='http://www.z90.com/wp-content/uploads/2015/11/Question_mark_black_on_white-237x300.png'></td>");
            }
            else
            {
                out.write ("<tr><td class=\"tg-6k2t\"><img src='" + searchRes.get (i).getImageURL () + "'></td>");
            }
            out.write ("<td class=\"tg-6k2t\">" + searchRes.get (i).getName () + "</td>");
            out.write ("<td class=\"tg-6k2t\">" + searchRes.get (i).getScore () + "</td>");
            out.write ("<td class=\"tg-6k2t\">" + searchRes.get (i).getQueryName () + "</td>");
            out.write ("<td class=\"tg-6k2t\">" + searchRes.get (i).getId () + "</td>");
            out.write ("<td class=\"tg-6k2t\">" + searchRes.get (i).getPersonLink () + "</td>");
            out.write ("<td class=\"tg-6k2t\">" + searchRes.get (i).getMyComments () + "</td>");
            out.write ("<td class=\"tg-6k2t\">" + searchRes.get (i).getMyRating () + "</td></tr>");
           
        }

        out.write ("</tr></table></div>");
        out.write ("</body>");
        out.write ("</html>");

        out.write (sb.toString ());
        out.close ();

        
    }

    private static String pad (String s, int i)
    {
        while (s.length () < i)
        {
            s=s + "*";
        }
        System.out.println ("String length" + s.length ());
        return s;
    }

    public static String readString (DataInputStream dis, int size) throws IOException
    {

        byte[] b=new byte[size * 2];
        dis.read (b);
        return depad (b);
    }

    public static String depad (byte[] b)
    {
        String word=new String (b);
        String word2="";
        for (int i=1; i < b.length; i+=2)
        {
            if (word.charAt (i) != '*')
            {
                word2=word2 + word.charAt (i);
            }

        }
        return word2;
    }
}
