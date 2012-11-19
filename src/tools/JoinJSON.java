package tools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import net.sf.json.JSONSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JoinJSON {
	
	
	public static final String DICIONARIO_PATH = "E:\\Faculdade\\5º ano\\1º Semestre\\DAPI\\dicionario.txt";
	public static final String FINALFILE_PATH = "E:\\Faculdade\\5º ano\\1º Semestre\\DAPI\\subfile.txt";
	public static final String MM_PATH = "E:\\Faculdade\\5º ano\\1º Semestre\\DAPI\\mxm_dataset_train.txt";
	public static final String LASTFM_PATH = "E:\\Faculdade\\5º ano\\1º Semestre\\DAPI\\lastfm_subset\\A\\A";
	public static boolean READ = false;
        
	public static void main(String[] args) {



		/*
		 * Cria��o do Objetos JSONObject
		 */
		
		
		try {
			int c=1;
			//JSONObject global = new JSONObject();
			System.out.println(LASTFM_PATH);
			FileWriter file;
			//JSONArray objects = new JSONArray()
			file = new FileWriter(FINALFILE_PATH);
			verify(LASTFM_PATH,c, file);
			

			file.flush();
			file.close();
			System.out.println("done");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
        
	//Função que verifica se a música tem uma letra adicionada. No caso da resposta ser afirmativa, adiciona a letra ao ficheiro e guarda. No fim
	//acrescenta tudo ao ficheiro .json definido pela variável FINALFILE_PATH
	public static boolean verify(String path, int count, FileWriter finalfile ) {

		FileInputStream fstream;

		try {

			
			File root = new File( path );
			File[] list = root.listFiles();
			
			
			
			for ( File f : list ) {
				fstream = new FileInputStream(MM_PATH);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
                                
				if ( f.isDirectory() ) {
					System.out.println(f.getAbsolutePath());
					verify(f.getAbsolutePath(), count, finalfile);
				}
				else{
					while ((strLine = br.readLine()) != null){
						if(strLine.charAt(0) == '#'){
							//System.out.println("Comentario\n");//COMENTARIO
						}
						else 
							// Dicionário
							if(strLine.charAt(0) == '%' && READ==false){
								// TODO Por o dicion�rio num txt e acrescentar um bool no fim para assegurar que este processo 
								//não é repetido
								
								READ = true;
								
								//ignora o %
								strLine = strLine.substring(1);
								
								System.out.println("entrou no dicionario");
								
								FileWriter dicionario = new FileWriter(DICIONARIO_PATH);
								
								//poe no dicionario a primeira palavra
								String word = strLine.substring(0,strLine.indexOf(','));
								dicionario.append(word+"\n");
								//Pega no resto da linha de palavras e vai tratar uma a uma e coloc�-la no ficheiro de texto
								strLine = strLine.substring(strLine.indexOf(',')+1);
								
								//Se encontrar um ',' � porque ainda h� palavras para tratar
								while(strLine.indexOf(',') != -1){
									word = strLine.substring(0,strLine.indexOf(','));
									strLine = strLine.substring(strLine.indexOf(',')+1);
									dicionario.append(word+"\n");
								}
								//poe no dicionario a ultima palavra
								dicionario.append(strLine);
								
								System.out.println("saiu o dicionario");
								dicionario.flush();
								dicionario.close();
							}
							else
							{
								//S� entra neste peda�o de c�digo se a linha lida n�o for nem coment�rio, nem dicionario, ou seja, � m�sica
								int ind = strLine.indexOf(',');
								String lyricID = strLine.substring(0, ind);
								String temp = strLine.substring(ind+1);
								String lyrics = temp.substring(temp.indexOf(',')+1);
								
								
								if(lyricID.equals(f.getName().substring(0,f.getName().indexOf(".")))){

									//Este bloco � executado se a musica tiver uma letra. Caso contr�rio � ignorado
									//teste.write(lyricID+"\n");
									Scanner s = new Scanner(new File(f.getAbsoluteFile().toString()));
									String jsonFile = "";
									while(s.hasNextLine())  jsonFile += s.nextLine();

									JSONObject j = new JSONObject(jsonFile);
									
									j.put("lyrics", lyricsToString(lyrics));
									
									//System.out.println("match");
									finalfile.append(j.toString()+",");
									
									break;
								}
							}
					}
				}
				count++;

			}
			
			//teste.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		return true;
	}
	//finalfile.put(jsonOne);
	//finalfile.put(jsonTwo);
	public static String lyricsToString(String original){
				
		//file = new FileWriter(FINALFILE_PATH);
		Scanner s;
		String result = "";
		try {
			
			String word;
			
			//while(s.hasNextLine())  jsonFile += s.nextLine();
			
			while(original.indexOf(',') != -1){
				int index;
				int n_times;
				
				s = new Scanner(new File(DICIONARIO_PATH));
				word = original.substring(0,original.indexOf(','));
				index = Integer.parseInt(word.substring(0,word.indexOf(':')));
				n_times = Integer.parseInt(word.substring(word.indexOf(':')+1));
				
				int cycle_index = 1;
				//String original = "1:17,2:5,3:7,4:8,5:10,6:1,7:9,8:12,9:5,10:6,11:7,12:8,13:1";
				while(s.hasNextLine()){
					String temp_word = s.nextLine();
					
					if(cycle_index == index){
						for(int i=0; i<n_times; i++)
							result += temp_word + " ";
						break;
					}
					cycle_index++;
					
					
					
				}
				original = original.substring(original.indexOf(',')+1);
			}
			
			s = new Scanner(new File(DICIONARIO_PATH));
			word = original;
			int index = Integer.parseInt(word.substring(0,word.indexOf(':')));
			int n_times = Integer.parseInt(word.substring(word.indexOf(':')+1));
			
			int cycle_index = 1;
			//String original = "1:17,2:5,3:7,4:8,5:10,6:1,7:9,8:12,9:5,10:6,11:7,12:8,13:1";
			while(s.hasNextLine()){
				String temp_word = s.nextLine();
				
				if(cycle_index == index){
					for(int i=0; i<n_times; i++)
						result += temp_word + " ";
					break;
				}
				cycle_index++;
				
				
				
			}

			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
} 
