package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class HiloPeliculas implements Runnable{

	private Thread hilo;
	private static int numCliente = 0;
	private Socket socketAlCliente;	
	
	public HiloPeliculas(Socket socketAlCliente) {
		numCliente++;
		hilo = new Thread(this, "Cliente_"+numCliente);
		this.socketAlCliente = socketAlCliente;
		hilo.start();
	}
	
	@Override
	public void run() {
		System.out.println("Estableciendo comunicacion con " + hilo.getName());
		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBuffer = null;
		
		try {
			salida = new PrintStream(socketAlCliente.getOutputStream());
			entrada = new InputStreamReader(socketAlCliente.getInputStream());
			entradaBuffer = new BufferedReader(entrada);
			
			String texto = "";
			boolean continuar = true;
			
			while (continuar) {
				texto = entradaBuffer.readLine();
				if (texto.trim().equalsIgnoreCase("FIN")) {
					salida.println("OK");
					System.out.println(hilo.getName() + " ha cerrado la comunicacion");
					continuar = false;
				} else {
					String[] datos = texto.split("#");
					if (datos.length == 2) {
						String operacion = datos[0];
						String[] datosPelicula = datos[1].split(";");
						if (datosPelicula.length == 3) {
							String titulo = datosPelicula[0];
							String director = datosPelicula[1];
							double precio = Double.parseDouble(datosPelicula[2]);
							switch (operacion) {
							case "ADD":
								SocketServidorHilo.addPelicula(new Pelicula(titulo, director, precio));
								salida.println("OK");
								break;
							case "GETID":
								Pelicula pelicula = SocketServidorHilo.getPeliculaById(Integer.parseInt(titulo));
								if (pelicula != null) {
									salida.println(pelicula.toString());
								} else {
									salida.println("ERRORid");
								}
								break;
							case "GETTITLE":
								pelicula = SocketServidorHilo.getPeliculaByTitulo(titulo);
								if (pelicula != null) {
									salida.println(pelicula.toString());
								} else {
									salida.println("ERRORti");
								}
								break;
							case "GETDIRECTOR":
								for (Pelicula p : SocketServidorHilo.getPeliculasByDirector(director)) {
									salida.println(p.toString());
								}
								salida.println("OK");
								break;
							default:
								salida.println("ERROR1");
								break;
							}
						} else {
							salida.println("ERROR2");
						}
					} else {
						salida.println("ERROR3");
					}
				}
			}
			socketAlCliente.close();
		} catch (IOException e) {
			System.err.println("HiloManejadorPeliculas: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("HiloManejadorPeliculas: Error");
			e.printStackTrace();
		}
	}
	
}
