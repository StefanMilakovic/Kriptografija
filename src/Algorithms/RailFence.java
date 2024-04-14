package Algorithms;

public class RailFence
{
    private static char[][] matrix;
    private static String text="";
    private static int numOfTracks;
    public static String encrypt(String input, int numOfTracks)
    {
        StringBuilder output= new StringBuilder();
        RailFence.numOfTracks=numOfTracks;
        setMatrix(input,numOfTracks);

        for(int i=0;i<numOfTracks;i++)
        {
            for(int j=0;j<text.length();j++)
            {
                if(RailFence.matrix[i][j]!='.')
                    output.append(RailFence.matrix[i][j]);
            }
        }
        printMatrix();
        System.out.println("\nEnkriptovano: "+output);
        return output.toString();
    }

    private static void setMatrix(String input,int numOfTracks)
    {
        RailFence.text=input.replace(" ","").toUpperCase();
        matrix=new char[numOfTracks][RailFence.text.length()];

        //postaviti
        for(int i=0;i<numOfTracks;i++)
        {
            for(int j=0;j<text.length();j++)
            {
                RailFence.matrix[i][j]='.';
            }
        }

        int i=0;
        while(i<RailFence.text.length())
        {
            for (int j = 0; j < numOfTracks && i<RailFence.text.length(); j++)
            {
                RailFence.matrix[j][i]=RailFence.text.charAt(i);
                //System.out.print(RailFence.text.charAt(i));
                i++;
            }
            for (int k = numOfTracks-2 ; k > 0 && i<RailFence.text.length(); k--)
            {
                RailFence.matrix[k][i]=RailFence.text.charAt(i);
                //System.out.print(RailFence.text.charAt(i));
                i++;
            }
        }
    }

    private static void printMatrix()
    {

        for(int i=0;i<numOfTracks;i++)
        {
            System.out.println();
            for(int j=0;j<text.length();j++)
            {
                System.out.print(RailFence.matrix[i][j]+"  ");
            }
        }
    }
}
