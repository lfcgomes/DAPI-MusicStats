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

	public static void main(String[] args) {



		/*
		 * Criação do Objeto JSONObject
		 */
		int c = 0;
		//System.out.println(verify("E:\\Faculdade\\5º Ano\\1º Semestre\\DAPI\\lastfm_subset", c));
		System.out.println(verify("E:\\Faculdade\\5º Ano\\1º Semestre\\DAPI\\lastfm_subset\\B\\I\\J",c));
	}

	public static int verify(String path, int count) {

		FileInputStream fstream;

		try {

			
			

			FileWriter file = new FileWriter("E:\\test.json");
			FileWriter teste = new FileWriter("E:\\test.txt");
			
			
			File root = new File( path );
			File[] list = root.listFiles();
			
			for ( File f : list ) {
				fstream = new FileInputStream("E:\\Faculdade\\5º Ano\\1º Semestre\\DAPI\\mxm_dataset_train.txt");
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				System.out.println(f.getName());
				if ( f.isDirectory() ) {
					count = verify(f.getAbsolutePath(), count);
				}
				else{
					while ((strLine = br.readLine()) != null){
						if(strLine.charAt(0) == '#'){
							//System.out.println("Comentario\n");//COMENTARIO
						}
						else 
							// Dicionário

							if(strLine.charAt(0) == '%'){
								// TODO Por o dicionário num txt em separado e acrescentar um bool no fim para assegurar que este processo 
								//não é repetido
							}
							else
							{
								int ind = strLine.indexOf(',');
								String lyricID = strLine.substring(0, ind);
								String temp = strLine.substring(ind+1);
								String lyrics = temp.substring(temp.indexOf(',')+1);

								Musica m = new Musica();

								if(lyricID.equals(f.getName().substring(0,f.getName().indexOf(".")))){

									
									teste.write(lyricID);
									Scanner s = new Scanner(new File(f.getAbsoluteFile().toString()));
									String jsonFile = "";
									while(s.hasNextLine())  jsonFile += s.nextLine();

									JSONObject j = new JSONObject(jsonFile);
									j.put("lyrics", lyrics);
								
									System.out.println("match");


									file.write(j.toString());

								}

							}
					}
				}
				count++;

			}
			file.flush();
			file.close();
			teste.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		return count;
	}
	//finalfile.put(jsonOne);
	//finalfile.put(jsonTwo);


	public int compareSongs(String lyricPath, String songPath){

		int n=0;
		try{

			FileInputStream fstream = new FileInputStream(songPath);
			FileInputStream fstream2 = new FileInputStream(lyricPath);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			DataInputStream in2 = new DataInputStream(fstream2);

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
			String strLine, //LE AS LINHAS DAS MUSICAS
			strLine2;//LE AS LINHAS DAS LETRAS

			int found = 0;
			int n_lines = 0;
			//Read File Line By Line

			//ciclo para contar o numero de letras de músicas
			while ((strLine2 = br2.readLine()) != null){
				n_lines++;
			}
			//TODO ver se dá para eliminar/saltar linhas do buffer

			if(n < n_lines) //Para garantir que o ciclo não continua depois de achadas todas as correspondências
				while ((strLine = br.readLine()) != null)   {
					found = 0;
					while ((strLine2 = br2.readLine()) != null && found == 0){
						//System.out.println(strLine + " | " + strLine2);

						if (strLine.contains(strLine2))
						{
							n++;
							found = 1;
							//if(n%1000 ==0){
							System.out.println("N: "+n); System.out.println(strLine + " = " + strLine2);
							//}

						}
					}
					//RESET
					fstream2 = new FileInputStream(lyricPath);
					in2 = new DataInputStream(fstream2);
					br2 = new BufferedReader(new InputStreamReader(in2));
					strLine2 = br2.readLine();
				}

			in.close();
			in2.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		System.out.println(n);
		return n;

	}
} 
