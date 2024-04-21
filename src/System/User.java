package System;

import java.io.Serializable;

public class User implements Serializable
{
    public String name;
    public String password;

    public User(String name,String password)
    {
        this.name=name;
        //ovo popraviti
        this.password=password;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

}
