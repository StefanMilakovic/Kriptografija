import System.*;

public class Main
{
    public static void main(String[] args)
    {
        CommandExecutor.executeCommand("openssl version");
        CryptoSystem sys = new CryptoSystem();

        sys.systemMenu();
    }
}
