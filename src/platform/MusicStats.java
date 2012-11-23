/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package platform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author Luis Gomes
 */
public class MusicStats {
    
    public static final String finalFile_PATH = "filesToIndex\\subfile.json";
    public static final boolean create = false;
    
       
    public static void main(String[] args)  {
        
        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        GUI g = new GUI();
        g.show();
    }
    public static ArrayList<String> search(String query) throws IOException, ParseException {
        // query
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        Directory dir = FSDirectory.open(new File("index"));
        //String querystr = "love";
        ArrayList<String> results = new ArrayList<String>();
        
        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = new QueryParser(Version.LUCENE_40, "lyrics", analyzer).parse(query);
        
        // 3. search
        int hitsPerPage = 48000;
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        
        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title"));
            results.add((i + 1) + ". " + d.get("title"));
        }
        
        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
        return results;
    }
    
    static void indexDocs(IndexWriter writer, File file) throws IOException {
        // do not try to index files that cannot be read
        
        if (file.canRead()) {
            
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException fnfe) {
                // at least on windows, some temporary files raise this exception with an "access denied" message
                // checking if the file can be read doesn't help
                return;
            }
            
            try {
                
                System.out.println("entrou");
                
                try {
                    JSONObject json = (JSONObject)new JSONParser().parse(new FileReader(file));
                    JSONArray objects = (JSONArray) json.get("musica");
                    
                    for(int i=0; i<objects.size(); i++){
                        JSONObject musica = (JSONObject) objects.get(i);
                        System.out.println(musica.get("title"));
                        // make a new, empty document
                        Document doc = new Document();
                        
                        // Add the contents of the file to a field named "contents".  Specify a Reader,
                        // so that the text of the file is tokenized and indexed, but not stored.
                        // Note that FileReader expects the file to be in UTF-8 encoding.
                        // If that's not the case searching for special characters will fail.
                        //doc.add(new TextField("title", new BufferedReader(new InputStreamReader((FileInputStream)musica.get("title"), "UTF-8"))));
                        doc.add(new TextField("lyrics", musica.get("lyrics").toString(), Field.Store.YES));
                        doc.add(new TextField("title", musica.get("title").toString(), Field.Store.YES));
                        
                        
                        System.out.println(doc);
                        
                        if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                            // New index, so we just add the document (no old document can be there):
                            System.out.println("adding " + file);
                            
                        } else {
                            // Existing index (an old copy of this document may have been indexed) so
                            // we use updateDocument instead to replace the old one matching the exact
                            // path, if present:
                            System.out.println("updating " + file);
                            writer.updateDocument(new Term("path", file.getPath()), doc);
                        }
                    }
                    
                } catch (Exception ex) {
                    Logger.getLogger(MusicStats.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
                
            } finally {
                fis.close();
            }
            
        }
    }
    public static void Index() throws IOException{
        // 1. create the index
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        Directory dir = FSDirectory.open(new File("index"));
        final File doc = new File(finalFile_PATH);
        
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        
        if (create) {
            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(OpenMode.CREATE);
        } else {
            // Add new documents to an existing index:
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        }
        
        IndexWriter writer = new IndexWriter(dir, iwc);
        
        indexDocs(writer, doc);
        
        writer.close();
    }
    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        
        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
}
