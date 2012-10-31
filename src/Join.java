import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class Join {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Join j = new Join();
		
		/*
		 * 
		FileWriter fstream = null, fstream2 = null;
		try {
			fstream = new FileWriter("songmatches.txt");
			fstream2 = new FileWriter("lyricmatches.txt");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter out = new BufferedWriter(fstream);
		BufferedWriter out2 = new BufferedWriter(fstream2);
		//Close the output stream
		j.grabSongTitle("C:\\Users\\Luis\\Documents\\FEUP\\5 Ano\\DAPI\\LastFM dataset\\lastfm_train",out);

		j.grabLyricTitle("C:\\Users\\Luis\\Documents\\FEUP\\5 Ano\\DAPI\\musicXmatch dataset\\mxm_dataset_test.txt",out2);
		j.grabLyricTitle("C:\\Users\\Luis\\Documents\\FEUP\\5 Ano\\DAPI\\musicXmatch dataset\\mxm_dataset_train.txt",out2);

		try {
			out.close();
			out2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*
		*/
		
		
		int equals = j.compareSongs("lyricmatches.txt", "songmatches.txt");
		System.out.println("EQUALS: "+equals);

	}

	public void grabLyricTitle(String path, BufferedWriter out)
	{

		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				// Print the content on the console

				if(strLine.charAt(0) == '#'){

					System.out.println("Comentario\n");//COMENTARIO
				}
				else 
					if(strLine.charAt(0) == '%'){
						strLine = br.readLine();
						int ind = strLine.indexOf(',');
						String lyricName = strLine.substring(0, ind);
						System.out.println(lyricName);
						try {
							out.append(lyricName);
							out.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						int ind = strLine.indexOf(',');
						String lyricName = strLine.substring(0, ind);
						System.out.println(lyricName);
						try {
							out.append(lyricName);
							out.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

			}
			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}


	}

	public void grabSongTitle( String path, BufferedWriter out ) {

		File root = new File( path );
		File[] list = root.listFiles();

		for ( File f : list ) {
			if ( f.isDirectory() ) {
				grabSongTitle( f.getAbsolutePath(),out );
			}
			else {
				System.out.println(f.getName());
				int ind = f.getName().indexOf('.');
				String s = f.getName().substring(0, ind);
				try {
					out.append(s);
					out.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

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
