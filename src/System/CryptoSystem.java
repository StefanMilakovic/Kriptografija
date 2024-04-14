package System;

import Algorithms.Myszkowski;
import Algorithms.Playfair;
import Algorithms.RailFence;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CryptoSystem
{
    public static Scanner scan=new Scanner(System.in);
    public Map<String,User> Users=new HashMap<String,User>();
    public CryptoSystem()
    {
        deserializeUsers();
    }
    public void registerUser()
    {
        String tempName="";
        String tempPassword="";
        System.out.println("Registracija novog korisnika: ");
        System.out.print("    Unesite ime: ");
        tempName=scan.nextLine();
        System.out.print("    Unesite lozinku: ");
        tempPassword=scan.nextLine();
        User newUser=new User(tempName,tempPassword);

        generateRSA(newUser);
        issueDigitalCert(newUser);

        Users.put(tempName,newUser);
        serializeUsers();
    }
    public void loginUser()
    {
        String temp="";
        String tempName="";
        String password="";
        System.out.println("Prijavljivanje:");
        System.out.print("    Unesite digitalni sertifikat: ");
        temp=scan.nextLine();

        X509Certificate certificate=findCertificate(temp);

        if(certificate!=null)
        {
            /*
            X500Principal subjectPrincipal = certificate.getSubjectX500Principal();
            String certInfo= subjectPrincipal.getName(X500Principal.RFC2253);
            System.out.println("       Sertifikat: "+certInfo);

             */

            System.out.print("    Unesite korisnicko ime: ");
            tempName=scan.nextLine();
            System.out.print("    Unesite lozinku: ");
            password=scan.nextLine();

            User tempUsr=Users.get(tempName);
            if((tempUsr.password).equals(password))
            {
                if(validateCertificate(tempUsr,certificate))
                {
                    System.out.println("Uspjesna prijava!");
                    loggedInOptions(tempUsr);
                }
            }
            else
            {
                System.out.println("Neispravni pristupni podaci!");
            }
        } else if (certificate==null)
        {
            System.out.println("Sertifikat ne postoji!");
        }
    }
    public void issueDigitalCert(User user)
    {

        //generisanje zahtjeva za sertifikat
        String command="openssl req -new -key ./private/"+user.name+".key -passin pass:"+user.password+" -out ./requests/"+user.name+".csr -config openssl.cnf -subj \"/C=BA/ST=RS/O=Elektrotehnicki fakultet/OU=ETF/CN="+user.name+ "\"";

        //String command="openssl req -new -key ./private/"+user.name+".key -out ./requests/"+user.name+".csr -config openssl.cnf -subj \"/C=BA/ST=RS/O=Elektrotehnicki fakultet/OU=ETF/CN="+user.name+ "\"";
        CommandExecutor.executeCommand(command);
        System.out.println("Generisan zahtjev za sertifikatom na lokaciji: requests/"+user.name+".csr");

        //ca tijelo potpisuje sertifikat
        command="openssl ca -in ./requests/"+user.name+".csr -config ./openssl.cnf -out ./certs/"+user.name+"Cert.crt -batch -keyfile private/private.key ";
        CommandExecutor.executeCommand(command);
        System.out.println("Generisan sertifikat na lokaciji: certs/"+user.name+"Cert.crt");
    }
    public void generateRSA(User user)
    {
        String command = "openssl genrsa -aes256 -passout pass:" + user.password + " -out ./private/" + user.name + ".key 4096";
        //String command="openssl genrsa -out ./private/"+user.name+".key 4096";
        CommandExecutor.executeCommand(command);
        System.out.println("Lokacija RSA kljuca:  private/"+user.name+".key");
    }
    public void serializeUsers()
    {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./Users/UsersHashMap.ser"))) {
            outputStream.writeObject(this.Users);
        } catch (IOException e) {
            System.err.println("Error occurred during serialization: " + e.getMessage());
        }
    }
    public void deserializeUsers()
    {
        String path="./Users/UsersHashMap.ser";
        File file=new File(path);
        if(file.exists())
        {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))) {
                this.Users = (Map<String, User>) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error occurred during deserialization: " + e.getMessage());
            }
        }
    }
    public void printUsers()
    {
        for (User usr : this.Users.values()) {
            System.out.println(usr);
        }
    }
    public X509Certificate findCertificate(String nameOfCert)
    {
        X509Certificate certificate=null;
        File directory = new File("./certs");
        if (directory.isDirectory())
        {
            File[] files = directory.listFiles();
            for (File file : files)
            {
                if (file.isFile() && file.getName().endsWith(nameOfCert +".crt"))
                {
                    certificate=readCertificate(file.toString());
                    break;
                }
            }
        } else {
            System.out.println("Greska!");
        }
        return certificate;
    }
    public X509Certificate readCertificate(String path)
    {
        X509Certificate certificate=null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return certificate;
    }
    public boolean validateCertificate(User user,X509Certificate certificate)
    {
        X500Principal subjectPrincipal = certificate.getSubjectX500Principal();
        String certInfo= subjectPrincipal.getName(X500Principal.RFC2253);
        String[] parts= certInfo.split(",");
        String commonName=parts[0].substring(3);

        if (!commonName.equals(user.name)) {
            return false;
        }

        Date validTo = certificate.getNotAfter();
        Date currentDate=new Date();
        if (currentDate.after(validTo)) {
            System.out.println("Sertifikat je istekao");
            return false;
        }
        return true;
    }

    public void systemMenu()
    {
        String choise="";
        System.out.println("CryptoSystem:");
        System.out.println("    1.Registracija");
        System.out.println("    2.Prijava");
        System.out.print("Vas izbor: ");
        choise=scan.nextLine();
        if("1".equals(choise))
        {
            registerUser();
        } else if ("2".equals(choise))
        {
            loginUser();
        }
        else {
            System.out.println("Greska!");
        }
    }
    public void loggedInOptions(User user)
    {
        String choise="";
        while(!"3".equals(choise))
        {
            System.out.println();
            System.out.println("Opcije: ");
            System.out.println("    1.Enkriptuj novi tekst ");
            System.out.println("    2.Pregled istorije enkripcije ");
            System.out.println("    3.Izlaz ");
            System.out.print("Vas izbor: ");
            choise=scan.nextLine();
            if("1".equals(choise))
            {
                System.out.println("Algoritmi enkripcije:");
                System.out.println("    1.Rail fence");
                System.out.println("    2.Myszkowski");
                System.out.println("    3.Playfair");
                System.out.print("Vas izbor: ");
                choise=scan.nextLine();

                String text;
                String key;
                int numOfTracks=0;
                String encryptedText;
                String result;

                System.out.print("Tekst(100 karaktera max): ");
                text=scan.nextLine();
                if(text.length()<100)
                {
                    if("1".equals(choise))
                    {
                        System.out.print("Broj kolosijeka: ");
                        numOfTracks=Integer.parseInt(scan.nextLine());
                        encryptedText=RailFence.encrypt(text,numOfTracks);
                        result=text+"|"+numOfTracks+"|"+encryptedText;
                        writeToFolder(user,result);
                    } else if ("2".equals(choise))
                    {
                        System.out.print("Kljuc: ");
                        key=scan.nextLine();
                        encryptedText=Myszkowski.encrypt(text,key);
                        result=text+"|"+key+"|"+encryptedText;
                        writeToFolder(user,result);
                    }else if ("3".equals(choise))
                    {
                        System.out.print("Kljuc: ");
                        key=scan.nextLine();
                        encryptedText=Playfair.encrypt(text,key);
                        result=text+"|"+key+"|"+encryptedText;
                        writeToFolder(user,result);
                    }
                    else {
                        System.out.println("Greska!");
                    }
                }
                else {
                    System.out.println("Greska! Unesite kraci tekst.");
                }
            } else if ("2".equals(choise))
            {
                viewEncryptionHistory(user);
            }
        }
    }

    public void writeToFolder(User user,String result)
    {
        File folderPath =new File("./Users/"+user.name);
        if (!folderPath.exists())
        {
            folderPath.mkdir();
        }

        String tempFile=folderPath +"/SimulationResult.txt";
        decryptFileToTxt(user);//prvo dekriptujemo ako nesto postoji pa dodamo novo na to

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(tempFile, true)))) {
            pw.println(result);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        String command="openssl aes-128-cbc -in "+folderPath+"/SimulationResult.txt -out "+folderPath+"/SimulationResult.dat -k "+user.password;
        CommandExecutor.executeCommand(command);

        File fileToDelete=new File(tempFile);
        if(fileToDelete.exists())
        {
            fileToDelete.delete();
        }
        signFile(user);
    }
    public void viewEncryptionHistory(User user)
    {
        if(verifySignature(user))
        {
            File folderPath =new File("./Users/"+user.name);
            String command="openssl aes-128-cbc -d -in "+folderPath+"/SimulationResult.dat -k "+user.password;
            CommandExecutor.executeCommand(command);
        }
        else {
            System.out.println("Fajl je neovlasteno modifikovan!");
        }
    }
    public void decryptFileToTxt(User user)
    {
        //ova metoda dektriptuje u tekstualni fajl koji ce kasnije biti izbrisan
        //ako fajl ne postoij prosto ga nece dekriptovati, pokriveno je sve sto se tice toga
        File folderPath =new File("./Users/"+user.name);
        String command="openssl aes-128-cbc -d -in "+folderPath+"/SimulationResult.dat -out "+folderPath+"/SimulationResult.txt -k "+user.password;

        CommandExecutor.executeCommand(command);
    }

    public void signFile(User user)
    {
        //-passin pass:" + user.password
        String command="openssl dgst -sign ./private/"+user.name+".key -passin pass:"+user.password+" -out ./Users/"+user.name+"/digitalSignature.pem ./Users/"+user.name+"/SimulationResult.dat";

        //String command="openssl dgst -sign ./private/"+user.name+".key -out ./Users/"+user.name+"/digitalSignature.pem ./Users/"+user.name+"/SimulationResult.dat";
        CommandExecutor.executeCommand(command);
    }

    public boolean verifySignature(User user)
    {
        String command="openssl dgst -prverify ./private/"+user.name+".key -passin pass:"+user.password+" -signature ./Users/"+user.name+"/digitalSignature.pem ./Users/"+user.name+"/SimulationResult.dat";

        //String command="openssl dgst -prverify ./private/"+user.name+".key  -signature ./Users/"+user.name+"/digitalSignature.pem ./Users/"+user.name+"/SimulationResult.dat";
        String result=CommandExecutor.executeCommand(command);
        return "Verified OK".equals(result);
    }
}
