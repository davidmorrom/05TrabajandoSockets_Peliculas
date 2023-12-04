package servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SocketServidorHilo {
	public static final int PUERTO = 2019;
	private static int idActual = 0;
	private static List<Pelicula> peliculas = new ArrayList<>();
	private static Pelicula peliculaFallo = new Pelicula(0, "No se ha encontrado la pelicula", "", 0.0);

	public static void main(String[] args) {
		System.out.println("      APLICACIÓN DE SERVIDOR CON HILOS     ");
		System.out.println("-------------------------------------------");
		peliculas.add(new Pelicula("Batman", "Nolan", 7.5));
		peliculas.add(new Pelicula("Oppenheimer", "Nolan", 8.5));
		peliculas.add(new Pelicula("Fast&Furious", "Lin", 6.5));
		peliculas.add(new Pelicula("Napoleón", "R.Scott", 9.5));
		peliculas.add(new Pelicula("Barbie", "G.Gerwig", 8.5));

		try (ServerSocket servidor = new ServerSocket()) {
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
		return peliculaFallo;
	}

	public synchronized static Pelicula getPeliculaByTitulo(String titulo) {
		for (Pelicula pelicula : peliculas) {
			if (pelicula.getTitulo().equalsIgnoreCase(titulo)) {
				return pelicula;
			}
		}
		return peliculaFallo;
	}

	public synchronized static String getPeliculasByDirector(String director) {
		String pelis = "";
		List<Pelicula> peliculasDirector = new ArrayList<>();
		for (Pelicula pelicula : peliculas) {
			if (pelicula.getDirector().trim().equalsIgnoreCase(director)) {
				peliculasDirector.add(pelicula);
			}
		}
		for (Pelicula pelicula : peliculasDirector) {
			pelis+=pelicula.toString()+", ";
		}
		return pelis;
	}
}
