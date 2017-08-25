package javaproject2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Map;
import javaproject2.Person;

public class MainApp
{
    // test to see if commit works //  test again check
    public static void main(String[] args)throws IOException
    {
        Map<String, Person> hashMap = new HashMap<>();
        
        MainApp theApp = new MainApp();
        theApp.start(hashMap);
        
        
        
    }

    private void start(Map <String, Person> hashMap) throws IOException
    {
        PersonStore personStore = new PersonStore();
        personStore.addPeople(hashMap);

        Scanner kb = new Scanner(System.in);
        /**
         * Options for menu that is displayed, uses while, switch, do etc
         */
        String displayMenu = ("Enter one of the following commands:"
                + "\n1 - Search for Persons"
                + "\n2 - List All Persons"
                + "\n3 - List Persons Sorted by Person ID"
                + "\n4 - List Persons by Rating"
                + "\n5 - Edit Person"
                + "\n6 - Export Search Results to HTML File"
                + "\n7 - Save to file"
                + "\n8 -  To Exit");
        System.out.println(displayMenu);
        System.out.println("\nYour choice is: ");
        int selection = 0;
        {
            try
            {
                selection = kb.nextInt();
                kb.nextLine();//buffer

            } catch (InputMismatchException e)
            {
                System.out.println("ERROR:Enter a Number");
                 kb.nextLine();
            }

        }
        while (selection != 8) // 8 = exit
        {
            switch (selection)
            {

                case 1:
                {
// Each time a name is entered, the system will look for that name in the local store (HashMap) and, 
//if found, will return a list of matching Persons from the local store.  
//If the name entered is not in the local store, then a corresponding request is sent to the TV Movies API.  
//The API will return a list of Persons (possibly empty).  
//This list will be parsed, sorted by score, displayed, and the retrieved data will also be added to the local store. 
//The local store will persist (i.e. will be written to a binary file “persons.dat”, and reloaded at beginning of each run.)   

                    System.out.println("please enter the person you are looking for ");
                    String name = kb.nextLine();

                    LinkedList<Person> peopleLocalStore = new LinkedList<>();
                    peopleLocalStore = personStore.searchLocal(hashMap, name);
                    System.out.println(peopleLocalStore);

                    break;
                }
                case 2:
                {
                    //personStore.listAll();
                    personStore.sortByName(hashMap);
                    
                    break;
                }
                case 3:
                {
                    personStore.sortLocalByID(hashMap);
                    break;
                }
                case 4:
                {
                    personStore.sortLocalByRating(hashMap);
                    break;
                }
                
                case 5:
                {
                    //  Allows a user to search for a particular Person in the local store, set their rating (1-5), and add comments.
                    System.out.println("Please enter the name of the person you want to set rating to and add comments");
                    String name = kb.nextLine();
                    personStore.setRatingAndComments(hashMap, name);
                    break;
                }
                case 6:
                {
                    //Allows users to export search results to a H5TML file. Each set of results should display the medium image and all other data.
                    break;
                            
                }
                case 7:
                {
                    //  The local store data (HashMap) will be written to a binary file before exiting, and loaded from file when the system starts. (File: “persons.dat”)
                    LinkedList<Person> people = new LinkedList<Person>(hashMap.values());
                    String filename = "persons.txt";
                    personStore.writeToFile(people, filename);

                    break;
                }

                default: // anything inputted not from 1-8 gets error message
                    System.out.println("You have entered an invalid choice, please re-enter your choice: ");
                    break;
            }
            System.out.println("\nOptions 1-8");
            displayMenu = ("Enter one of the following commands:"
                    + "\n1 - Search for Persons"
                    + "\n2 - List All Persons"
                    + "\n3 - List Persons Sorted by Person ID"
                    + "\n4 - List Persons by Rating"
                    + "\n5 - Edit Person"
                    + "\n6 - Export Search Results to HTML File"
                    + "\n7 - Save to file"
                    + "\n8 -  To Exit");
            System.out.println(displayMenu);
            System.out.println("\nYour choice is: ");

            try
            {
                selection = kb.nextInt();
                kb.nextLine();//buffer

            } catch (InputMismatchException e)
            {
                System.out.println("ERROR:Enter a Number");
                 kb.nextLine();
            }

        }
    }
}
