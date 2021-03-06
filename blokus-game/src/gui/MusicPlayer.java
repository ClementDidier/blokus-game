package gui;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import program.Program;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class MusicPlayer {
	private String			musicName;
	private Clip			clip;
	private FloatControl	gainControl;

	public MusicPlayer() {
		this.musicName = "null";
		this.clip = null;
		this.gainControl = null;
	}

	/**
	 * Change la musique courante
	 * 
	 * @param musicName
	 *            Nom du fichier(sans extension) de la nouvelle musique
	 */
	public void changeMusic(String newMusicName) {
		if (this.clip==null || !this.musicName.equals(newMusicName)) {
			this.musicName = newMusicName;
			if (this.clip != null) {
				this.clip.stop();
				this.clip.close();
				this.clip = null;
			}

			try {
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/music/"+musicName+".wav"));
				this.clip = AudioSystem.getClip();
				this.clip.open(audioIn);
				this.gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
				if(musicName.startsWith("effect"))
					this.setVolume(Program.optionConfiguration.getVolumeSFX());
				else
					this.setVolume(Program.optionConfiguration.getVolumeMusic());
			}
			catch (LineUnavailableException | IOException e) {
				e.printStackTrace();
			}
			catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Joue la musique en boucle
	 */
	public void playContinuously() {
		if (this.clip != null && !this.clip.isRunning()) {
			this.clip.loop(Clip.LOOP_CONTINUOUSLY);
			this.clip.start();
		}
	}

	/**
	 * Joue la musique(par défaut en boucle)
	 */
	public void playSound() {
		this.playContinuously();
	}

	/**
	 * Joue la musique une fois
	 */
	public void playOnce() {
		if (this.clip != null) {
			this.clip.start();
			this.clip.addLineListener(new LineListener() {
				
				@Override
				public void update(LineEvent e) {
					if (e.getType() == LineEvent.Type.STOP) {
						e.getLine().close();
					}
				}
			});
			this.clip = null;
		}
	}

	/**
	 * Stoppe la musique
	 */
	public void stopSound() {
		if (this.clip != null) {
			this.clip.stop();
		}
	}

	/**
	 * Permet de connaitre l'état du player(en marche, à l'arrêt)
	 * 
	 * @return true si une musique est actuellement jouée, false sinon
	 */
	public boolean isRunning() {
		return (this.clip != null && this.clip.isRunning());
	}

	/**
	 * Change le volume de sortie de la musique
	 * 
	 * @param gain
	 *            Nouveau volume, entre 0.0f et 1.0f
	 */
	public void setVolume(float gain) {
		int i = Math.round(gain/0.05f);
		gain = i*0.05f;
		float dB = (float) (Math.log(gain/10f) / Math.log(10.0) * 20.0);
		this.gainControl.setValue(dB);
	}
}
