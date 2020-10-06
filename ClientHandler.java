package com.company;

import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.*;
import java.time.temporal.ChronoUnit;


public class ClientHandler extends Thread
{
    private final DataInputStream inStr;
    private final DataOutputStream outStr;
    private final Socket socket;
    public String rec_mess;
    public String mess;
    public SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");


    public ClientHandler(Socket sock, DataInputStream in, DataOutputStream out)
    {
        this.socket = sock;
        this.inStr = in;
        this.outStr = out;
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                rec_mess = inStr.readUTF();

                if(rec_mess.equals("Exit"))
                {
                    System.out.println("Klient: " + socket.getRemoteSocketAddress() + " wyszedł. Zamykam połączenie!");
                    break;
                }

                String time_s = inStr.readUTF(); //czas od clienta
                if(time_s.equals("Exception"))
                {
                    System.out.println("Wystąpił problem u klienta! Kończę połączenie!");
                    break;
                }


                LocalTime t1 = LocalTime.parse(time_s);
                LocalTime t2 = LocalTime.now();

                long timeDiff = 1;
                if(t2.isBefore(t1))
                    timeDiff = t2.until(t1, ChronoUnit.SECONDS);

                Thread.sleep(timeDiff * 1000);

                outStr.writeUTF( "Wiadomość: " + rec_mess + " zwrócona o: " + time_s + ". Mineło: " + timeDiff +"sekund.");

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }


        try
        {
            this.inStr.close();
            this.outStr.close();
            this.socket.close();
            System.out.println("Połączenie zamknięte!");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

    }
}
