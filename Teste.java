import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;


public class Teste {

	/**
	 * @param args
	 * @throws InvalidMidiDataException 
	 * @throws MidiUnavailableException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException, InterruptedException {
		// TODO Auto-generated method stub

		Composer comp = new Composer();
		Sequence seq = comp.compose("cc",55);
		AudioPlayer player = new AudioPlayer(seq,180);
		player.start();

		//Thread.sleep(3000);
		
		//player.interrupt();
		
		
	}

}
