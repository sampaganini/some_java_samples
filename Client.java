package com.company;

import javax.xml.validation.Validator;
import java.net.*;
import java.io.*;
import java.text.ParseException;
import java.time.LocalTime;


public class Client
{
    public static void main(String args[]) throws IOException
    {
        String serverName = "localhost";
        int port = 1000;

        String mess = "";
        String time = "";
        DataOutputStream outStr = null;
        DataInputStream inStr = null;
        Socket client = null;

        try
        {
            System.out.println("Łączenie z '" + serverName + "' na porcie: " + port);
            client = new Socket(serverName,port);
            System.out.println("Połączono z '" + serverName + "'.");

            outStr = new DataOutputStream(client.getOutputStream());
            inStr = new DataInputStream(client.getInputStream());
            System.out.println(inStr.readUTF());

            while(true)
            {

                System.out.println("Podaj wiadomosc lub 'Exit' aby zakończyć: ");
                BufferedReader buff = new BufferedReader( new InputStreamReader(System.in));
                mess = buff.readLine();

                outStr.writeUTF(mess);

                if(mess.equals("Exit"))
                {
                    System.out.println("Kończę połączenie!");
                    break;
                }

                while(true)
                {
                    System.out.println("Podaj czas zwrotu wiadomosci:");
                    //jesli wszystko ok podaj czas oczekiwania;
                    time = buff.readLine();
                    ValidateTime(time);

                    if( LocalTime.now().isBefore(LocalTime.parse(time)))
                        break;
                    System.out.println("Nie można wysłać wiadomości w przeszłości!");
                }


                outStr.writeUTF(time);

                System.out.println("Serwer: " + inStr.readUTF());
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        catch (NiepoprawnyFormatGodzinyException ex)
        {
            ex.printStackTrace();
            outStr.writeUTF("Exception");
        }
        finally
        {
            outStr.close();
            inStr.close();
            client.close();
            System.out.println("Połączenie zakończone!");
        }
    }

    public static void ValidateTime(String time) throws NiepoprawnyFormatGodzinyException
    {
        try
        {
            LocalTime tmp = LocalTime.parse(time);
        }
        catch(Exception e)
        {
            throw new NiepoprawnyFormatGodzinyException();
        }
    }
}
