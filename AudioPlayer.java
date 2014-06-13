import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;


public class AudioPlayer extends Thread
{

	private static Sequencer	sm_sequencer;
	private Sequence seq;
	private int BPM;
	

	public AudioPlayer(Sequence inSeq){
		this.seq = inSeq;
	}
	
	public void run()
	{
		try {
			sm_sequencer = MidiSystem.getSequencer();			
			sm_sequencer.addMetaEventListener(new MetaEventListener()
			{
				public void meta(MetaMessage event)
				{
					if (event.getType() == 47){
						sm_sequencer.close();
					}
					if(event.getType() == 51){
						sm_sequencer.setTempoInBPM(getBPMFromEvent(event.getData()));
					}
				}
			});
			sm_sequencer.open();
			sm_sequencer.setSequence(seq);
			sm_sequencer.setTempoInBPM(BPM);
			sm_sequencer.start();
			
			while(sm_sequencer.isRunning()){
				if(Thread.interrupted()){
					sm_sequencer.stop();
				} 
			}
			
		} catch (MidiUnavailableException | InvalidMidiDataException e) {
			e.printStackTrace();
		}	
	}
	
	private float getBPMFromEvent(byte[] data){
		return Float.parseFloat(data.toString()) * sm_sequencer.getTempoInBPM();
	}
}