package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
            Album album = new Album(title);
            albums.add(album);
            boolean f = false;
            for(Artist a:artists){
                if(a.getName().equals(artistName)){
                    f = true;
                    break;
                }
            }

        Artist artist = createArtist(artistName);;
            if(f){
                if(artistAlbumMap.containsKey(artist)){
                    List<Album> al = artistAlbumMap.get(artist);
                    al.add(album);
                    artistAlbumMap.put(artist,al);
                }else{
                    List<Album> al = new ArrayList<>();
                    al.add(album);
                    artistAlbumMap.put(artist, al);
                }

            }else{
                List<Album> al = new ArrayList<>();
                al.add(album);
                artistAlbumMap.put(artist, al);
            }
            return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song = new Song(title,length);
        songs.add(song);
        Album album = null;
        boolean f = false;
        for(Album a : albums){
            if(a.getTitle().equals(albumName)){
                f = true;
                album = a;
                break;

            }
        }

        if(f){
            if(albumSongMap.containsKey(album)){
                List<Song> songs1 = albumSongMap.get(album);
                songs1.add(song);
                albumSongMap.put(album,songs1);
            }else{
                List<Song> songs1 = new ArrayList<>();
                songs1.add(song);
                albumSongMap.put(album,songs1);
            }
        }else{
            throw new Exception();
        }
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
             Playlist playlist = new Playlist(title);
             playlists.add(playlist);
             List<Song> songsPlalist = new ArrayList<>();
             for(Song song:songs){
                 if(song.getLength() == length){
                     songsPlalist.add(song);
                 }
             }
             playlistSongMap.put(playlist,songsPlalist);
             User user = null;
             for(User u:users){
                 if(u.getMobile().equals(mobile)){
                     user = u;
                     break;
                 }
             }
        creatorPlaylistMap.put(user,playlist);
        List<User> al = new ArrayList<>();
        al.add(user);
        playlistListenerMap.put(playlist,al);
        List<Playlist> listwithuser = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            listwithuser = userPlaylistMap.get(user);
        }
        listwithuser.add(playlist);
        userPlaylistMap.put(user,listwithuser);
        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist = new Playlist(title);
        List<Song> songslist = new ArrayList<>();
        if(playlistSongMap.containsKey(playlist)){
            songslist = playlistSongMap.get(playlist);
        }

        for(String t:songTitles){
            for(Song song:songs){
                if(song.getTitle().equals(t)){
                    songslist.add(song);
                    break;
                }
            }
        }

        User user = null;
        for(User u:users){
            if(u.getMobile().equals(mobile)){
                user = u;
                break;
            }
        }
        creatorPlaylistMap.put(user,playlist);
        List<User> al = new ArrayList<>();
        al.add(user);
        playlistListenerMap.put(playlist,al);

        List<Playlist> listwithuser = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            listwithuser = userPlaylistMap.get(user);
        }
        listwithuser.add(playlist);
        userPlaylistMap.put(user,listwithuser);
        return playlist;

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
            Playlist playlist = null;
            for(Playlist p : playlists){
                if(p.getTitle().equals(playlistTitle)){
                    playlist = p;
                    break;
                }
            }
            User user = null;
            for(User u:users){
                if(u.getMobile().equals(mobile)){
                    user = u;
                    break;
                }
            }
            boolean f = false;
            if(creatorPlaylistMap.get(user).getTitle().equals(playlistTitle)){
                f = true;
            }

        List<User> listners = playlistListenerMap.get(playlist);
            for(User listner:listners){
                if(listner.getMobile().equals(mobile)){
                    f = true;
                    break;
                }
            }

            if(f == false){
                listners.add(user);
                playlistListenerMap.put(playlist,listners);
            }
          return playlist;

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
         Song song = null;
         for(Song s:songs){
             if(s.getTitle().equals(songTitle)){
                 song = s;
                 break;
             }
         }
        List<User> songUsers = songLikeMap.get(song);
         boolean f = false;
         for(User u:songUsers){
             if(u.getMobile().equals(mobile)){
                 f = true;
                 break;
             }
         }
         User user = null;
         for(User u:users){
             if(u.getMobile().equals(mobile)){
                 user = u;
                 break;
             }
         }

         Album album = null;
         for(Album al:albums){
             List<Song> songList = albumSongMap.get(al);
             boolean found = false;
             for(Song s:songList){
                 if(s.getTitle().equals(songTitle)){
                     album = al;
                     break;
                 }
             }
             if(found == true){
                 break;
             }
         }

         Artist artist = null;
         for(Artist ar:artistAlbumMap.keySet()){
             List<Album> albumList = artistAlbumMap.get(ar);
             boolean found = false;
             for(Album al:albumList){
                 if(al.getTitle().equals(album.getTitle())){
                     artist = ar;
                     break;
                 }
             }
             if(found == true){
                 break;
             }
         }
         if(f == false){
             int sl = song.getLikes();
             sl+=1;
             song.setLikes(sl);
             int al = artist.getLikes();
             al += 1;
             artist.setLikes(al);
         }
         return song;
    }

    public String mostPopularArtist() {
        Artist artist = null;
        int l = 0;
        for(Artist ar:artists){
            if(ar.getLikes() > l){
                artist = ar;
                l  = ar.getLikes();
            }
        }
        return artist.getName();
    }

    public String mostPopularSong() {
        int l = 0;
        Song s = null;
        for(Song song:songs){
            if(song.getLikes() > l){
                l = song.getLikes();
                s = song;
            }
        }
        return s.getTitle();
    }
}
