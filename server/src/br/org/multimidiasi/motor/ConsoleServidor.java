package br.org.multimidiasi.motor;

import java.io.IOException;
import javax.media.DataSink;
import javax.media.MediaLocator;
import javax.media.NoDataSinkException;
import javax.media.NoPlayerException;
import javax.media.NoProcessorException;
import javax.media.NotConfiguredError;
import javax.media.NotRealizedError;
import javax.media.Player;
import javax.media.Processor;
import javax.media.Manager;
import javax.media.control.FormatControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;

public class ConsoleServidor {
	public static void main(String[] args) {
		if (args.length!=3)
		{
			System.out.println("Sintaxe: java -jar NomePacoteServidor.jar [IP] 182.168.1.20 [Porta]22000 [Local onde se localiza a midia]c:\\midiadistribuida\\radio.mp2");
			System.exit(-1);
		}
		
		int porta = 0;
		String ip = null;
		String end = null;
		
		try {
			porta = Integer.parseInt(args[1]);
			ip = args[0];
			end = args[2];
		} catch (NumberFormatException e1) {
			System.out.println("Houve um erro com os parametros passados.");
			System.exit(-1);
		}
		
		Servidor svr = new Servidor();
		try {
			svr.iniciarServicoServidor(end, ip, porta);
		} catch (RuntimeException e) {
			System.out.println("Houve um erro.");
			e.printStackTrace();
		}
		
	}

}

