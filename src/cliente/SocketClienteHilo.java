package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClienteHilo {
	public static final int PUERTO = 2019;
	public static final String IP_SERVER = "localhost";

	public static void main(String[] args) {
		System.out.println("        APLICACI�N CLIENTE         ");
		System.out.println("-----------------------------------");

		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);

		try (Scanner sc = new Scanner(System.in)) {

			System.out.println("CLIENTE: Esperando a que el servidor acepte la conexi�n");
			Socket socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);
			System.out.println("CLIENTE: Conexion establecida... a " + IP_SERVER + " por el puerto " + PUERTO);

			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader entradaBuffer = new BufferedReader(entrada);

			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());

			String texto = "";
			boolean continuar = true;
			do {
				Scanner sc2 = new Scanner(System.in);
				System.out.println("1. Consultar pelicula por id");
				System.out.println("2. Consultar pelicula por titulo");
				System.out.println("3. Consultar pelicula por director");
				System.out.println("4. Agregar pelicula");
				System.out.println("5. Salir");
				int eleccion = sc2.nextInt();
				switch (eleccion) {
				case 1:
					System.out.println("Introduce el id de la pelicula");
					int id = sc2.nextInt();
					salida.println("GETID#" + id + ";0;0.0");
					texto = entradaBuffer.readLine();
					System.out.println(texto);
					break;
				case 2:
					System.out.println("Introduce el titulo de la pelicula");
					String titulo = sc2.next();
					salida.println("GETTITLE#" + titulo + ";0;0.0");
					texto = entradaBuffer.readLine();
					System.out.println(texto);
					break;
				case 3:
					System.out.println("Introduce el director de la pelicula");
					String director = sc2.next();
					salida.println("GETDIRECTOR#0;" + director + ";0.0");
					texto = entradaBuffer.readLine();
					System.out.println(texto);
					break;
				case 4:
					System.out.println("Introduce el titulo de la pelicula");
					String titulo2 = sc2.nextLine();
					System.out.println("Introduce el director de la pelicula");
					String director2 = sc2.next();
					System.out.println("Introduce el precio de la pelicula");
					double precio = sc2.nextDouble();
					salida.println("ADD#" + titulo2 + ";" + director2 + ";" + precio);
					texto = entradaBuffer.readLine();
					System.out.println(texto);
					break;
				case 5:
					continuar = false;
					salida.println("FIN");
					texto = entradaBuffer.readLine();
					System.out.println(texto);
					break;
				default:
					continuar = false;
					salida.println("FIN");
					texto = entradaBuffer.readLine();
					System.out.println(texto);
					break;
				}

			} while (continuar);
			socketAlServidor.close();
		} catch (UnknownHostException e) {
			System.err.println("CLIENTE: No encuentro el servidor en la direcci�n" + IP_SERVER);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("CLIENTE: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}

		System.out.println("CLIENTE: Fin del programa");
	}
}
