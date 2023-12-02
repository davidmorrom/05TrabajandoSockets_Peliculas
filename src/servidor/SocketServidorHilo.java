package servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServidorHilo {
	public static final int PUERTO = 2019;
	private static int idActual = 1;
	private static List<Pelicula> peliculas = new ArrayList<>();
	
	public static void main(String[] args) {
		System.out.println("      APLICACIÓN DE SERVIDOR CON HILOS     ");
		System.out.println("-------------------------------------------");		
		peliculas.add(new Pelicula("El padrino", "Francis Ford Coppola", 7.5));
		peliculas.add(new Pelicula("Oppenheimer", "Christopher Nolan", 8.5));
		peliculas.add(new Pelicula("Fast and Furious", "Justin Lin", 6.5));
		peliculas.add(new Pelicula("Napoléon", "Ridley Scott", 9.5));
		peliculas.add(new Pelicula("El señor de los anillos", "Peter Jackson", 8.5));

		try (ServerSocket servidor = new ServerSocket()){			
			InetSocketAddress direccion = new InetSocketAddress(PUERTO);
			servidor.bind(direccion);

			System.out.println("SERVIDOR: Esperando peticion por el puerto " + PUERTO);
			
			while (true) {
				Socket socketAlCliente = servidor.accept();
				System.out.println("SERVIDOR: peticion numero " + ++idActual + " recibida");
				new HiloPeliculas(socketAlCliente);
			}			
		} catch (IOException e) {
			System.err.println("SERVIDOR: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error");
			e.printStackTrace();
		}
	}
	
	public synchronized static List<Pelicula> getPeliculas() {
		return peliculas;
	}

	public synchronized static void addPelicula(Pelicula pelicula) {
		peliculas.add(pelicula);
	}
	
	public synchronized static Pelicula getPeliculaById(int id) {
		for (Pelicula pelicula : peliculas) {
			if (pelicula.getId() == id) {
				return pelicula;
			}
		}
		return null;
	}
	
	public synchronized static Pelicula getPeliculaByTitulo(String titulo) {
		for (Pelicula pelicula : peliculas) {
			if (pelicula.getTitulo().trim().equalsIgnoreCase(titulo)) {
				return pelicula;
			}
		}
		return null;
	}
	
	public synchronized static List<Pelicula> getPeliculasByDirector(String director) {
		List<Pelicula> peliculasDirector = new ArrayList<>();
		for (Pelicula pelicula : peliculas) {
			if (pelicula.getDirector().trim().equalsIgnoreCase(director)) {
				peliculasDirector.add(pelicula);
			}
		}
		return peliculasDirector;
	}
}
