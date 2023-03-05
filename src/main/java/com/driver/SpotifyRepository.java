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
        for(User user:users){
            if(user.getMobile().equals(mobile)){
                return user;
            }
        }
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        for(Artist artist:artists){
            if(artist.getName().equals(name)){
                return artist;
            }
        }
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        for(Album album:albums){
            if(album.getTitle().equals(title)){
                return album;
            }
        }
            Album album = new Album(title);
            albums.add(album);
            Artist artist = createArtist(artistName);;
            List<Album> al = new ArrayList<>();
            if(artistAlbumMap.containsKey(artist)){
                al = artistAlbumMap.get(artist);
            }
            al.add(album);
            artistAlbumMap.put(artist,al);
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
            List<Song> songs1 = new ArrayList<>();
            if(albumSongMap.containsKey(album)){
                songs1 = albumSongMap.get(album);
            }
            songs1.add(song);
            albumSongMap.put(album,songs1);
        }else{
            throw new Exception("Album does not exist");
        }
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
            for(Playlist playlist:playlists){
                if(playlist.getTitle().equals(title)){
                    return playlist;
                }
            }
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
             if(user == null){
                 throw new Exception("User does not exist");
             }
        creatorPlaylistMap.put(user,playlist);

        List<User> listner = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            listner = playlistListenerMap.get(playlist);
        }
        listner.add(user);
        playlistListenerMap.put(playlist,listner);

        List<Playlist> listwithuser = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            listwithuser = userPlaylistMap.get(user);
        }
        listwithuser.add(playlist);
        userPlaylistMap.put(user,listwithuser);
        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist:playlists){
            if(playlist.getTitle().equals(title)){
                return playlist;
            }
        }

        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        List<Song> songslist = new ArrayList<>();
        if(playlistSongMap.containsKey(playlist)){
            songslist = playlistSongMap.get(playlist);
        }

        for(Song song:songs){
            if(songTitles.contains(song.getTitle())){
                songslist.add(song);
            }
        }
        playlistSongMap.put(playlist,songslist);
//        for(String t:songTitles){
//            for(Song song:songs){
//                if(song.getTitle().equals(t)){
//                    songslist.add(song);
//                    break;
//                }
//            }
//        }

        User user = null;
        for(User u:users){
            if(u.getMobile().equals(mobile)){
                user = u;
                break;
            }
        }
        if(user == null){
            throw new Exception("User does not exist");
        }

        List<User> listners = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
           listners = playlistListenerMap.get(playlist);
        }
        listners.add(user);
        playlistListenerMap.put(playlist,listners);
        creatorPlaylistMap.put(user,playlist);

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
            if(playlist == null) {
                throw new Exception("play list does not exist");
            }
            User user = null;
            for(User u:users){
                if(u.getMobile().equals(mobile)){
                    user = u;
                    break;
                }
            }
            if(user == null){
                throw new Exception("User does not exist");
            }
//            boolean f = false;
//            if(creatorPlaylistMap.get(user).getTitle().equals(playlistTitle)){
//                f = true;
//            }

            List<User> listners = new ArrayList<>();
            if(playlistListenerMap.containsKey(playlist)){
                listners = playlistListenerMap.get(playlist);
            }
        if(!listners.contains(user))
            listners.add(user);
        playlistListenerMap.put(playlist,listners);

        if(creatorPlaylistMap.get(user)!=playlist)
            creatorPlaylistMap.put(user,playlist);


        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            userplaylists=userPlaylistMap.get(user);
        }
        if(!userplaylists.contains(playlist)) userplaylists.add(playlist);
        userPlaylistMap.put(user,userplaylists);
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
         if(song == null){
             throw new Exception("Song does not exist");
         }

        User user = null;
        for(User u:users){
            if(u.getMobile().equals(mobile)){
                user = u;
                break;
            }
        }
        if(user == null){
            throw new Exception("User does not exist");
        }



        List<User> songUsers = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            songUsers = songLikeMap.get(song);
        }

        if (!songUsers.contains(user)) {
            songUsers.add(user);
            songLikeMap.put(song, songUsers);
            song.setLikes(song.getLikes() + 1);


//          public HashMap<Album, List<Song>> albumSongMap;
            Album album = new Album();
            for (Album curAlbum : albumSongMap.keySet()) {
                List<Song> temp = albumSongMap.get(curAlbum);
                if (temp.contains(song)) {
                    album = curAlbum;
                    break;
                }
            }

            //            public HashMap<Artist, List<Album>> artistAlbumMap;
            Artist artist = new Artist();
            for (Artist curArtist : artistAlbumMap.keySet()) {
                List<Album> temp = artistAlbumMap.get(curArtist);
                if (temp.contains(album)) {
                    artist = curArtist;
                    break;
                }
            }

            artist.setLikes(artist.getLikes() + 1);
        }

         return song;
    }

    public String mostPopularArtist() {
        String name="";
        int maxLikes = Integer.MIN_VALUE;
        for(Artist art : artists){
            maxLikes= Math.max(maxLikes,art.getLikes());
        }
        for(Artist art : artists){
            if(maxLikes==art.getLikes()){
                name=art.getName();
            }
        }
        return name;
//        Artist artist = null;
//        int l = Integer.MIN_VALUE;;
//        for(Artist ar:artists){
//            if(ar.getLikes() > l){
//                artist = ar;
//                l  = ar.getLikes();
//            }
//        }
//        return artist.getName();
    }

    public String mostPopularSong() {
        String name="";
        int maxLikes = Integer.MIN_VALUE;
        for(Song song : songs){
            maxLikes=Math.max(maxLikes,song.getLikes());
        }
        for(Song song : songs){
            if(maxLikes==song.getLikes())
                name=song.getTitle();
        }
        return name;
//        int l = 0;
//        Song s = null;
//        for(Song song:songs){
//            if(song.getLikes() > l){
//                l = song.getLikes();
//                s = song;
//            }
//        }
//        return s.getTitle();
    }
}
