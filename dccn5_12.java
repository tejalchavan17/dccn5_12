import java.net.*;
import java.io.*;
import java.util.*;
 
public class Sender
{
    int n,i,sum=0;
    int data[]=new int[50];
    Scanner sc=new Scanner(System.in);
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       =  null;
    private DataOutputStream out     = null;
    public Sender(int port)
    {
        try
        {
            server = new ServerSocket(port);
	    System.out.println("--------------Sender--------------");
 
            System.out.println("Waiting...");
 
            socket = server.accept();
            System.out.println("Connection accepted");
 
            in = new DataInputStream(socket.getInputStream());

	    out    = new DataOutputStream(socket.getOutputStream());
 
            while (true)
            {
                try
                {

		    System.out.println("Enter frame size:");
		    n=sc.nextInt();
		    System.out.println("Enter bits:");
		    for(i=0;i<n;i++)
			data[i]=sc.nextInt();
		    out.writeInt(n);
		    for(i=0;i<n;i++)
			out.writeInt(data[i]);
                    if (in.readUTF().equals("frame received")) 
                    {   
			System.out.println("Acknowledge from receiver:");
                	System.out.println("frame received Successfully!");
                	break;
            	    }

                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            socket.close();
            in.close();
	    out.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        Sender sender = new Sender(5000);
    }
}


import java.net.*;
import java.io.*;
import java.util.*;
 
public class Receiver
{

    int n,i,c=0,t=0,e=0;
    int data[]=new int[50];
    int stuffed[]=new int[50];
    int flag[]=new int[]{0,1,1,1,1,1,1,0};
    Scanner sc=new Scanner(System.in);
    private Socket socket=null;
    private DataInputStream in=null;
    private DataOutputStream out=null;
 
    public Receiver(String address, int port)
    {
        try
        {
	    System.out.println("--------------Receiver--------------");
	    socket = new Socket(address, port);
            System.out.println("Connected");

	    in = new DataInputStream(socket.getInputStream());		
 
            out    = new DataOutputStream(socket.getOutputStream());
 
            while (true)
            {
            	try
            	{
			n=in.readInt();
			for(i=0;i<n;i++)
			{
				data[i]=in.readInt();
			}
			for(i=0;i<n;i++)
			{
				if(data[i]==1)
				{
					c++;
					stuffed[t]=data[i];
					t++;
					if(c==5)
					{
						stuffed[t]=0;
						e++;
						t++;
						c=0;
					}
				}
				else
				{
					c=0;
					stuffed[t]=data[i];
					t++;
				}
				
			}
			System.out.println("Stuffed frame:");
			for(i=0;i<flag.length;i++)
			{
				System.out.print(flag[i]+" ");
			}
			for(i=0;i<(n+e);i++)
			{
				System.out.print(stuffed[i]+" ");
			}
			for(i=0;i<flag.length;i++)
			{
				System.out.print(flag[i]+" ");
			}
			c=0;
			for(i=0;i<(n+e);i++)
			{
				if(stuffed[i]==1)
				{
					c++;
					if(c==5)
					{
						i++;
						stuffed[i]=-1;
						c=0;
					}
				}
				else
				{
					c=0;
				}
			}
			System.out.println("");
			System.out.println("Original frame:");
			for(i=0;i<(n+e);i++)
			{
				if(stuffed[i]!=-1)
				{
					System.out.print(stuffed[i]+" ");
				}
			}
			out.writeUTF("frame received");
				break;
			
            	}
            	catch(IOException i)
            	{
                	System.out.println(i);
            	}
       	    }
 
            in.close();
            out.close();
            socket.close();
	}
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        Receiver receiver = new Receiver("127.0.0.1", 5000);
    }
}

/*Output
students@CE-Lab3-603-U19:~/Desktop$ javac Sender.java
students@CE-Lab3-603-U19:~/Desktop$ java Sender
--------------Sender--------------
Waiting...
Connection accepted
Enter frame size:
15
Enter bits:
1 0 1 1 1 1 1 0 1 1 1 1 1 1 0
Acknowledge from receiver:
frame received Successfully!
students@CE-Lab3-603-U19:~/Desktop$ 

students@CE-Lab3-603-U19:~/Desktop$ javac Receiver.java
students@CE-Lab3-603-U19:~/Desktop$ java Receiver
--------------Receiver--------------
Connected
Stuffed frame:
0 1 1 1 1 1 1 0 1 0 1 1 1 1 1 0 0 1 1 1 1 1 0 1 0 0 1 1 1 1 1 1 0 
Original frame:
1 0 1 1 1 1 1 0 1 1 1 1 1 1 0 students@CE-Lab3-603-U19:~/Desktop$ */
