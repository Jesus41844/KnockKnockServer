package KnockKnockServer;
//Importacion de librerias
import java.io.*; //Para entrada y salida de datos
import java.net.*; //Operaciones de red
import java.util.*; //Para Scanner y otras utilidades

//Define la clase principal del cliente
public class Clientekks {
    //Constante para la configuracion del server, host y puerto
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4444;

    //Metodo principal
    public static void main(String[] args) {
        System.out.println("Conectando al KKS...");
        
        // Try-with-resources para manejar recursos (se cierran automáticamente)
        try (var socket = new Socket(SERVER_HOST, SERVER_PORT); //Socket para conectarse al servidor
             var out = new PrintWriter(socket.getOutputStream(), true); //Stream de salida al servidor
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Entrada al servidor
             var userInput = new Scanner(System.in)) { //Lee la entrada del servidor
                System.out.println("Conexion establecida con el kks...");
                boolean jokeComplete = false; //Booleano para controlar el fin del chiste

                //Bucle mientras no se complete el chiste
                while (!jokeComplete) {
                    //Si no hay datos para leer, espera a que haya
                    if (!in.ready()) {
                        Thread.onSpinWait(); //Optimiza la espera activa
                        continue;
                    }

                    //Lee el mensaje dado por el servidor
                    String serverMesagge = in.readLine();
                    //Si el mensaje es nulo termina la conexion
                    if (serverMesagge == null) break;

                    //Escribe lo que diga el servidor
                    System.out.println("Servidor: " + serverMesagge);

                    //Si el mensaje del servidor contiene virtual threads culmina la conexion
                    if (serverMesagge.contains("virtual threads")) {
                        System.out.println("Chiste completado. Cerrando conexion...");
                        jokeComplete = true;
                        break;
                    }

                    //hace un if para que se escriba lo que corresponde a cada parte de la broma y la envia al servidor
                    if ((serverMesagge.contains("Knock knock") || 
                         serverMesagge.contains("Java")) || 
                         serverMesagge.endsWith("?")) {
                            System.out.print("Tu: ");
                            String response = userInput.nextLine();
                        out.println(response);
                    }
                }
        //Da los errores que podrian suscitarse
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + SERVER_HOST);
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
    } 
}
