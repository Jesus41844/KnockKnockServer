package KnockKnockServer;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class KKS {
    private static final int PORT = 4444;
    private static final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();
    public static void main(String[] args) throws IOException {
        //throws IOException se usa para dar a entender al JVM (Java Virtual Machine) que el metodo podra tener excepciones
        int portNumber = 4444; //Configurar el puerto
        try (ServerSocket serverSocket = new ServerSocket(PORT)){ //Hace un try/catch que intenta hacer la conexion con el puerto
            System.out.println("Servidor Knock Knock iniciado en el puerto: " + portNumber);//Tira un mensaje por consola para confirmar que se inicio el Knock Knock
            //Inicia un bucle para aceptar las solicitudes
            while (true) {
                var clientSocket = serverSocket.accept();
                threadPool.execute(()-> handleClient(clientSocket));
            }
        } catch (IOException e) {
            //Si no se puede hacer la conexion salta el error y te da el error
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clienSocket) {
        //Hace un try/catch para conectarse con el cliente
        try (clienSocket; //Crea un Socket para el cliente
            var out = new PrintWriter(clienSocket.getOutputStream(), true); //Crea un PrintWritter que manda los datos en el switch al cliente inmediatamente
            //Crea un BufferedReader para recibir los datos dados por el cliente
            var in = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()))){
                //Da un mensaje de que el cliente se conecto en x puerto
                System.out.println("Cliente conectado: " + clienSocket.getInetAddress());
                //Se ejecuta el metodo sendJoke
                sendJoke(out, in);
            //Si no se pude salta el error y da el error suscitado
        } catch (IOException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }

    //En base al try/catch anterior inicia el juego del knock knock server
    private static void sendJoke(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Knock Knock");

        String inputLine = in.readLine();
        if (inputLine == null) return;
        //Hace un switch case con los diferentes casos que se pueden dar en el juego
        switch (in.readLine().toLowerCase()) {
            case "who's there?" -> {
                out.println("Java");
                if ("Java who?".equalsIgnoreCase(in.readLine())) {
                    out.println("Java 21 knocking with virtual threads!, bye");
                    return;
                }
                break;
            }
            //Si el cliente manda un mensaje que no esta en el juego salta por default el siguiente mensaje:
            default -> out.println("Debes responder 'Who's there?' para continuar");
        }
    }
}
