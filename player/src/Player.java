import java.io.IOException;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;

public class Player {

	public static void main(String[] args) {
		String s1 = "";
		
		if (args.length==0)
		{
			System.out.println("Sintaxe: java -jar Xxxxxxx.jar protocolo://endereco");
			System.exit(-1);
		}
		
		s1 = args[0];
		
		//ou seja se ninguem passar nada uso a de sempre se passar uso a que passarem.
		String enderecoMidia = s1 ;

		System.out.println("");
		System.out.println("*******************************"); 
		System.out.println("*******************************");
		System.out.println("*******************************");
		System.out.println("");
		MediaLocator localizadorDeMidia = new MediaLocator(enderecoMidia);
		System.out.println("Criando o localizador de midia");
		//vamos imprimir uma saida no console para visualizar
		System.out.println(localizadorDeMidia);
		try {
			System.out.println("Criando um player.");
			javax.media.Player pl = Manager.createPlayer(localizadorDeMidia);
			System.out.println(pl);
			System.out.println("Iniciando a midia");
			pl.start();
			System.out.println("");	
			System.out.println("*******************************");
			System.out.println("*******************************");
			System.out.println("*******************************");
			System.out.println("");
		} catch (NoPlayerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

