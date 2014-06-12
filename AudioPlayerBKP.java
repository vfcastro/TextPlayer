import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;


public class AudioPlayerBKP
{

	private static Sequencer	sm_sequencer;

	public AudioPlayerBKP() throws MidiUnavailableException
	{
		sm_sequencer = MidiSystem.getSequencer();		
		sm_sequencer.addMetaEventListener(new MetaEventListener()
		{
			public void meta(MetaMessage event)
			{
				if (event.getType() == 47)
				{
					sm_sequencer.close();
				}
			}
		});
	}
	

	public void startPlay(Sequence seq, float BPM) throws MidiUnavailableException, InvalidMidiDataException
	{
		sm_sequencer.open();
		sm_sequencer.setSequence(seq);
		sm_sequencer.setTempoInBPM(BPM);
		sm_sequencer.start();
	}
	
	public void stopPlay()
	{
		if(sm_sequencer.isRunning())
			sm_sequencer.stop();
	}
	
}