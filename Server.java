package com.company;


import java.net.*;
import java.io.*;
import java.util.*;


public class Server extends Thread
{
    public static void main(String args[]) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(1000);
        System.out.println("Serwer uruchomiony!");
        while(true)
        {
            Socket connSocket = null;
            DataOutputStream output = null;
            DataInputStream input = null;
            try
            {
                connSocket = serverSocket.accept();
                System.out.println("Połączono nowego klienta: " + connSocket.getRemoteSocketAddress());
                output = new DataOutputStream(connSocket.getOutputStream());
                input = new DataInputStream(connSocket.getInputStream());

                output.writeUTF("Co oczekujesz od serwera?");

                System.out.println("Dołączam nowego klienta do osobnego wątku!");

                Thread th = new ClientHandler(connSocket, input, output);
                th.start();
            }
            catch(Exception ex)
            {
                connSocket.close();
                ex.printStackTrace();
            }
        }
    }

}
