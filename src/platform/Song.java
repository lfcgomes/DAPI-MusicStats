/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package platform;

/**
 *
 * @author Luis Gomes
 */
public class Song {
    private String artist;
    private String songName;
    
    public void setArtist(String newArtist){
        this.artist = newArtist;
    }
    public void setSongName(String newSongName){
        this.songName = newSongName;
    }
    public String getArtist(){
        return this.artist;
    }
    public String getSongName(){
        return this.songName;
    }
}
