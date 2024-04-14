package Algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Myszkowski
{
    public static String text;
    public static String key;

    public static char[][] matrix;
    public static int rows;
    public static int columns;

    static public void setParameters(String input,String key)
    {
        Myszkowski.text=input.replace(" ","").toUpperCase();
        Myszkowski.key=key;

        Myszkowski.rows=1;
        Myszkowski.columns=key.length();
        while(Myszkowski.rows*Myszkowski.columns<Myszkowski.text.length())
        {
            Myszkowski.rows++;
        }
    }

    static public void setMatrix()
    {
        Myszkowski.matrix =new char[rows][columns];
        int index=0;
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<columns;j++)
            {
                if (index < Myszkowski.text.length())
                {
                    Myszkowski.matrix[i][j] = Myszkowski.text.charAt(index);
                    index++;
                } else
                {
                    Myszkowski.matrix[i][j] = ' ';
                }
            }
        }
    }

    public static String encrypt(String input,String key)
    {
        StringBuilder output= new StringBuilder();
        setParameters(input,key);
        setMatrix();
        printMatrix();

        List<Character> chars=new ArrayList<>();
        for (int i=0;i<key.length();i++)
        {
            if(!chars.contains(key.charAt(i)))
            {
                chars.add(key.charAt(i));
            }
        }
        Collections.sort(chars);

        for(Character c:chars)
        {
            if(!appearsMoreThanOnce(key,c))
            {
                for (int i=0;i<rows;i++)
                {
                    if(matrix[i][key.indexOf(c)]!=' ')
                    {
                        output.append(matrix[i][key.indexOf(c)]);
                    }
                }
            }
            else if (appearsMoreThanOnce(key,c))
            {
                for(int i=0;i<rows;i++)
                {
                    for (int j = 0; j < columns; j++)
                    {
                        if (key.charAt(j) == c && matrix[i][key.indexOf(c)] != ' ')
                        {
                            output.append(matrix[i][j]);
                        }
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Enkriptovano: "+output);
        return output.toString();
    }
    public static boolean appearsMoreThanOnce(String key, char c)
    {
        int count =0;
        for(int i=0;i<key.length();i++)
        {
            if(key.charAt(i)==c)
            {
                count++;
            }
        }
        return count >1;
    }

    public static void printMatrix()
    {
        for (char[] chars : Myszkowski.matrix)
        {
            System.out.println();
            for (int j = 0; j < columns; j++)
            {
                System.out.print(chars[j]+" ");
            }
        }
    }
}
