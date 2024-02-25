package cn.com.twoke.game.spider_solitaire.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {

    public static int FIRE = 0;
    
    public static int FIREWORK = 0;
    public static int LAUNCH = 1;
    public static int NO_TIP = 2;
    public static int PICKUP = 3;
    public static int PUTDOWN = 4;
    public static int SUCCESS = 5;
    public static int TIP = 6;

    private int currentSongId;
    private Clip[] songs, effects;
    private float volume = 1f;

    public AudioPlayer() {
        loadSongs();
        loadEffects();
    }

    private void loadSongs() {
        String[] names = {"fire"};
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }

    }


    private void loadEffects() {
        String[] names = {"firework", "launch", "no_tip", "pickup", "putdown", "success", "tip"};
        effects = new Clip[names.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(names[i]);
        }
        updateEffectsVolume();
    }


    private Clip getClip(String name) {
        URL url = getClass().getResource("/sound/"+name+".wav");
        AudioInputStream audio = null;
        Clip c = null;
        try {
            audio = AudioSystem.getAudioInputStream(url);
            c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }


  
    private void updateEffectsVolume() {
        for (Clip effect : effects) {
            FloatControl gainControl = (FloatControl) effect.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
    
    public void stopSong() {
        if (songs[currentSongId].isActive()) {
            songs[currentSongId].stop();
        }
    }

    public void playEffect(int effectId) {
        effects[effectId].setMicrosecondPosition(0);
        effects[effectId].start();
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (this.volume < 0) {
            this.volume = 0;
        } else if (this.volume > 1) {
            this.volume = 1;
        }
        updateEffectsVolume();
    }
    
    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    public void playSong(int songId) {
        stopSong();
        currentSongId = songId;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY); // 循环播放
    }


}