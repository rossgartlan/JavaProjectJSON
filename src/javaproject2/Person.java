/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaproject2;

import java.util.Objects;

/**
 *
 * @author Rossco
 */
public class Person
{
    private double score;
    private String queryName;
    private String name;
    private long Id; 
    private String imageURL;
    private String personLink;
    private  String myComments;
    private int myRating;

    public Person (double score, String queryName, String name, long Id, String imageURL, String personLink, String myComments, int myRating)
    {
        this.score=score;
        this.queryName=queryName;
        this.name=name;
        this.Id=Id;
        this.imageURL=imageURL;
        this.personLink=personLink;
        this.myComments=myComments;
        this.myRating=myRating;
    }

    public double getScore ()
    {
        return score;
    }

    public void setScore (double score)
    {
        this.score=score;
    }

    public String getQueryName ()
    {
        return queryName;
    }

    public void setQueryName (String queryName)
    {
        this.queryName=queryName;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name=name;
    }

    public long getId ()
    {
        return Id;
    }

    public void setId (long Id)
    {
        this.Id=Id;
    }

    public String getImageURL ()
    {
        return imageURL;
    }

    public void setImageURL (String imageURL)
    {
        this.imageURL=imageURL;
    }

    public String getPersonLink ()
    {
        return personLink;
    }

    public void setPersonLink (String personLink)
    {
        this.personLink=personLink;
    }

    public String getMyComments ()
    {
        return myComments;
    }

    public void setMyComments (String myComments)
    {
        this.myComments=myComments;
    }

    public int getMyRating ()
    {
        return myRating;
    }

    public void setMyRating (int myRating)
    {
        this.myRating=myRating;
    }

    @Override
    public int hashCode ()
    {
        int hash=3;
        hash=79 * hash + (int) (Double.doubleToLongBits (this.score) ^ (Double.doubleToLongBits (this.score) >>> 32));
        hash=79 * hash + Objects.hashCode (this.queryName);
        hash=79 * hash + Objects.hashCode (this.name);
        hash=79 * hash + (int) (this.Id ^ (this.Id >>> 32));
        hash=79 * hash + Objects.hashCode (this.imageURL);
        hash=79 * hash + Objects.hashCode (this.personLink);
        hash=79 * hash + Objects.hashCode (this.myComments);
        hash=79 * hash + this.myRating;
        return hash;
    }

    @Override
    public boolean equals (Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass () != obj.getClass ())
        {
            return false;
        }
        final Person other=(Person) obj;
        if (Double.doubleToLongBits (this.score) != Double.doubleToLongBits (other.score))
        {
            return false;
        }
        if (this.Id != other.Id)
        {
            return false;
        }
        if (this.myRating != other.myRating)
        {
            return false;
        }
        if (!Objects.equals (this.queryName, other.queryName))
        {
            return false;
        }
        if (!Objects.equals (this.name, other.name))
        {
            return false;
        }
        if (!Objects.equals (this.imageURL, other.imageURL))
        {
            return false;
        }
        if (!Objects.equals (this.personLink, other.personLink))
        {
            return false;
        }
        if (!Objects.equals (this.myComments, other.myComments))
        {
            return false;
        }
        return true;
    }

    

    

    @Override
    public String toString ()
    {
        return "Person{" + "score=" + score + ", queryName=" + queryName + ", name=" + name + ", Id=" + Id + ", imageURL=" + imageURL + ", personLink=" + personLink + ", myComments=" + myComments + ", myRating=" + myRating + '}';
    }

    
    
    
    
    
    
    
    
}