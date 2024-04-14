package System;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class User implements Serializable
{
    public String name;
    public String password;

    /*
    String countryName="";
    String stateOrProvinceName="";
    String organizationName="";
    String organizationalUnitName="";
    String commonName="";
    String emailAddress="";
     */
    public User(String name,String password)
    {
        this.name=name;
        this.password=password;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

}
