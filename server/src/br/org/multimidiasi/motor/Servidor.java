package br.org.multimidiasi.motor;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Calendar;

import javax.media.DataSink;
import javax.media.Format;
import javax.media.MediaLocator;
import javax.media.NoDataSinkException;
import javax.media.NoProcessorException;
import javax.media.NotConfiguredError;
import javax.media.NotRealizedError;
import javax.media.Processor;
import javax.media.Manager;
import javax.media.control.FormatControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferDataSource;

public class Servidor {

	private MediaLocator ml;
	private Processor pro;
	private javax.media.protocol.DataSource ds;
	private DataSink dsk; 
	private boolean codificado = false;
	
	public void pausar(long segundos)
	{
		try {
			Thread.currentThread().sleep(segundos*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void iniciarServicoServidor(String end,String ip, int porta)
	{
		try {
			capturarMidia(end);
			criarProcessor();
			configurarProcessor();
			descreverConteudoEnviado();
			formatarEmProtocoloRTP();			
			criarStreaming();
			configurarServidor(ip, porta);
			iniciarServidor();
		} catch (RuntimeException e) {
			System.out.println("Houve um erro em iniciarServicoServidor");
			e.printStackTrace();
		}
	}
	
	public void capturarMidia(String endereco)
	{
		try {
			System.out.println("**************************************************************");
			System.out.println("Iniciando processo de servidor de multimidia em " + Calendar.getInstance().getTime().toString());
			ml = new MediaLocator("file:///" + endereco);
			System.out.println("Midia capturada com sucesso!");
			System.out.println("[" + "file:///" + endereco +"]");
			System.out.println(ml);
						
		} catch (RuntimeException e) {
			System.out.println("Houve um erro em capturarMidia");
			e.printStackTrace();
		}
	}
	
	public void criarProcessor()
	{
		try {
			System.out.println("**************************************************************");
			System.out.println("Criando um Processor.");
			pro = Manager.createProcessor(ml);
			System.out.println("Processor criado com sucesso." + pro);
			System.out.println("Midia com duracao:" + (pro.getDuration().getSeconds()/60));
			
		} catch (NoProcessorException e) {
			System.out.println("Houve um erro em criarProcessor");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Houve um erro em criarProcessor");
			e.printStackTrace();
		}
	}

	public void configurarProcessor()
	{
		try {
			System.out.println("**************************************************************");
			System.out.println("Processor em estado de configuração."); 
			pro.configure();
			pausar(3);
			System.out.println("Processor configurado!");
			System.out.println(pro);
		} catch (RuntimeException e) {
			System.out.println("Houve um erro em configurarProcessor");
			e.printStackTrace();
		}
	}
	
	public void descreverConteudoEnviado()
	{
		try {
			System.out.println("**************************************************************");		
			pro.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW));
			System.out.println("Descritor de conteudo:" + pro.getContentDescriptor().toString());
		} catch (NotConfiguredError e) {
			System.out.println("Houve um erro em descreverConteudoEnviado");
			e.printStackTrace();
		}
	}
	
    	
	public void formatarEmProtocoloRTP()
	{
		System.out.println("**************************************************************");
		try {
			
			System.out.println("Codificar midia para pacote RTP.");
			TrackControl faixa[] = pro.getTrackControls();
			
			System.out.println("Codificando " + faixa.length + " faixa(s).");
			
			
			for (int i = 0; i < faixa.length ; i++)
			{
				
				System.out.println("Codificando " + (i+1) + " de " + faixa.length);
				
				Format formato = faixa[i].getFormat();
				
				System.out.println(formato);
				
				if (!codificado && 
						faixa[i].isEnabled() &&
						formato instanceof AudioFormat)
						
				{
					
					AudioFormat formatoDeAudioRtp = new AudioFormat(AudioFormat.MPEG_RTP);
					faixa[i].setFormat(formatoDeAudioRtp);
					codificado = true;
					System.out.println("Faixa:" + faixa[i] + " codificada em " + faixa[i].getFormat());
						
				}
				else
				{
					System.out.println("Nao foi possivel codificar a faixa :" + (i+1));
					System.out.println("Ja esta codificado ou nao eh uma instancia de FormatControl");
					faixa[i].setEnabled(false);
				}
				}
		} catch (NotConfiguredError e) {
			System.out.println("Houve um erro em formatarMidia");
			e.printStackTrace();
		}
		
		}
	
	public void tocar()
	{
		pro.start();
			
	}
	
	public void criarStreaming()
	{
		
		try {
			System.out.println("**************************************************************");
			if (codificado)
			{
				System.out.println("Midia codificada...");
				System.out.println("Processor entra em estado de realize.");
				pro.realize();
				pausar(3);
				System.out.println("Processor realized.");
				System.out.println("Adquirindo o streaming a ser enviado."); 
				ds = pro.getDataOutput();
				System.out.println("Streaming adquirido pronto a ser enviado.");
				System.out.println(ds);
				
				if (ds instanceof PullBufferDataSource) 
				{
					System.out.println("-->>Midia sob demanda.");
				}
				else
				{
					System.out.println("-->>Midia tipo radio.");
				}
				
			}
		} catch (NotRealizedError e) {
			System.out.println("Houve um erro em criarStreaming");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void configurarServidor(String ip, int porta)
	{
		System.out.println("**************************************************************");
		String url = "rtp://" + ip + ":" + porta + "/audio/rhcp";
		System.out.println("Servidor ira atender em " + url);
		
		
		MediaLocator mml = new MediaLocator(url);
		
		System.out.println("Localizador de midia ja criado");
		try {
			System.out.println("Criando um DataSink a ser enviado.");
			dsk = Manager.createDataSink(ds, mml);
			System.out.println("DataSink criado.");
		} catch (NoDataSinkException e) {
			e.printStackTrace();
		}
		
	}

	public void iniciarServidor()
	{
		try {
	
			
			System.out.println("**************************************************************");
			dsk.open();
			System.out.println("Servidor ligado.");
			dsk.start();
			ds.start();
			pro.start();
			System.out.println("Servidor iniciado.");
			System.out.println("**************************************************************");
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
}

