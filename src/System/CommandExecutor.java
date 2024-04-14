package System;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandExecutor
{
    public static String executeCommand(String command)
    {
        StringBuilder returnValue= new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);

            InputStream inputStream = process.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = read.readLine()) != null) {
                returnValue.append(line);
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return returnValue.toString();
    }
}
