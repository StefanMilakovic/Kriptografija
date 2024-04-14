package Algorithms;

public class Playfair
{
    public static final int DIM=5;
    private static char[][] matrix;
    public static String encrypt(String input,String key)
    {
        String text=input.replace(" ","").toUpperCase();
        if(text.length()%2!=0)
        {
            text+="X";
        }

        StringBuilder output= new StringBuilder();
        setMatrix(key);
        printMatrix();

        int firstCharX;
        int firstCharY;

        int secondCharX;
        int secondCharY;

        for(int i=0;i<text.length();i+=2)
        {
            firstCharY=findY(text.charAt(i));
            firstCharX=findX(text.charAt(i),firstCharY);

            secondCharY=findY(text.charAt(i+1));
            secondCharX=findX(text.charAt(i+1),secondCharY);

            if(firstCharY==secondCharY)
            {
                output.append(Playfair.matrix[firstCharY][(firstCharX + 1)%DIM]);
                output.append(Playfair.matrix[secondCharY][(secondCharX + 1)%DIM]);
                output.append(' ');
            }

            else if(firstCharX==secondCharX)
            {
                output.append(Playfair.matrix[(firstCharY + 1)%DIM][firstCharX]);
                output.append(Playfair.matrix[(secondCharY + 1)%DIM][secondCharX]);
                output.append(' ');
            }

            else if(firstCharY!=secondCharY && firstCharX!=secondCharX)
            {
                output.append(Playfair.matrix[firstCharY][secondCharX]);//dobar
                output.append(Playfair.matrix[secondCharY][firstCharX]);
                output.append(' ');
            }
        }


        System.out.println("\nEnkriptovano: "+output);
        return output.toString();
    }

    private static int findY(char c)
    {
        int y=0;
        boolean found=false;
        for(int i=0;i<DIM;i++)
        {
            for(int j=0;j<DIM;j++)
            {
                if(Playfair.matrix[i][j]==c)
                {
                    y=i;
                    found=true;
                    break;
                }
            }
            if(found)
                break;
        }
        return y;
    }

    private static int findX(char c,int y)
    {
        int x=0;
        for(int i=0;i<DIM;i++)
        {
            if(Playfair.matrix[y][i]==c)
            {
                x=i;
                break;
            }
        }
        return x;
    }



    private static void setMatrix(String key)
    {
        String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        Playfair.matrix=new char[DIM][DIM];
        StringBuilder newKey= new StringBuilder();
        for(int i=0;i<key.length();i++)
        {
            if(!newKey.toString().contains(String.valueOf(Character.toUpperCase(key.charAt(i)))))
            {
                newKey.append(Character.toUpperCase(key.charAt(i)));
            }
        }
        String reducedKey=newKey.toString();
        int reducedKeyIndex =0;
        int alphabetIndex=0;
        for(int i=0;i<DIM;i++)
        {
            for(int j=0;j<DIM;j++)
            {
                if(reducedKeyIndex <reducedKey.length())
                {
                    Playfair.matrix[i][j]=reducedKey.charAt(reducedKeyIndex);
                    reducedKeyIndex++;
                }
                else
                {
                    if(!reducedKey.contains(String.valueOf(alphabet.charAt(alphabetIndex))))
                    {
                        Playfair.matrix[i][j]=alphabet.charAt(alphabetIndex);
                    }
                    else
                    {
                        j--;
                    }
                    alphabetIndex++;
                }
            }
        }
    }

    private static void printMatrix()
    {
        for(int i=0;i<DIM;i++)
        {
            System.out.println();
            for(int j=0;j<DIM;j++)
            {
                System.out.print(Playfair.matrix[i][j]+"  ");
            }
        }
    }

}
