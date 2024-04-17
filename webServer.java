//------------------------------------------------------------------------------
// Java web server with no external dependencies.
// Philip R Brenan at appaapps dot com, Appa Apps Ltd Inc., 2024
//------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;

class WebServer                                                                 // Web server written in Java with no dependencies.
 {final static int port = 8080;                                                 // The port we are going to listen on

  static String readInput(InputStream inputStream) throws Exception             // Read all the bytes from the browser
   {final StringBuilder B = new StringBuilder();
    while(inputStream.available() > 0) B.append((char)(inputStream.read()));    //Read all the available bytes
    return B.toString();                                                        // Convert to string
   }

  static String getMethod(String request)                                       // Get Url from request
   {final String[] lines = request.split("\\s+");                               // Split into words
    if (lines.length > 0) return lines[0];                                      // Method
    return null;                                                                // Method not found
   }

  static String getUrl(String request)                                          // Get Url from request
   {final String[] lines = request.split("\\s+");                               // Split into words
    if (lines.length > 1) return lines[1];                                      // Url
    return null;                                                                // Url not found
   }

  public static void main(String[] args)                                        // Run the web server
   {try
     {ServerSocket serverSocket = new ServerSocket(port);                       // Create a server socket listening on port 8080
      say("Server started. Listening on port", port);

      while (true)
       {try(final Socket clientSocket = serverSocket.accept())
         {final String request = readInput(clientSocket.getInputStream());      // Request as a string
          final String method  = getMethod(request);
          final String url     = getUrl(request);
          final String[]urls   = url.split("/");

          say("New connection from:", clientSocket.getInetAddress());
          say("Method             :", method);
          say("Url                :", url);

          String response = "HTTP/1.1 200 OK\r\n\r\n";                          // Prepare response
          if (urls.length > 2)
            response += "<html><body><h1>"+urls[1]+"  "+urls[2]+"!</h1></body></html>";
          else
            response += "<html><body><h1> Hello Vanina!</h1></body></html>\n\n";

          try(final OutputStream outputStream = clientSocket.getOutputStream()) // Write response to the client
           {outputStream.write(response.getBytes());
            outputStream.flush();
           }
          catch(Exception e) {e.printStackTrace();}
         }
        catch(Exception e) {e.printStackTrace();}
       }
     }
    catch (Exception e) {e.printStackTrace();}                                  // Read error
   }

  static void say(Object...O)                                                   // Say something
   {final StringBuilder b = new StringBuilder();
    for(Object o: O) {b.append(" "); b.append(o);}
    System.err.println((O.length > 0 ? b.substring(1) : ""));
   }
 }
