package net.zephyr.fnafur.util.Computer;

import net.zephyr.fnafur.blocks.computer.ComputerData;

import java.util.ArrayList;
import java.util.List;

public class ComputerPlaylist {
    private final String name;
    private final List<ComputerSong> playlist;
    public ComputerPlaylist(String name, String... songNames){
        this.name = name;
        playlist = new ArrayList<>();
        for (String songName : songNames) {
            if (ComputerData.getSong(songName) == null) continue;
            playlist.add(ComputerData.getSong(songName));
        }
    }
    public String getName(){
        return this.name;
    }
    public void addSong(ComputerSong song){
        playlist.add(song);
    }
    public void addSong(String song){
        playlist.add(ComputerData.getSong(song));
    }
    public List<ComputerSong> getList() {
        return playlist;
    }
}
