import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class Composer {
	
	private static final int	VELOCITY = 64;
	private static final float	TIMING_TYPE = Sequence.PPQ;
	private static final int	TIMING_RESOLUTION = 1;
	private static final int CHANNEL = 0;
	private static final Map<Character, Integer> notesMap;
    static
    {
    	notesMap = new HashMap<Character, Integer>();
    	notesMap.put('a', 69);
    	notesMap.put('b', 71);
    	notesMap.put('c', 60);
    	notesMap.put('d', 62);
    	notesMap.put('e', 64);
    	notesMap.put('f', 65);
    	notesMap.put('g', 67);
    }
	
	private Sequence	sequence;
	private Track track;
	
	public Composer() throws InvalidMidiDataException{
		sequence = new Sequence(TIMING_TYPE, TIMING_RESOLUTION);
		track = sequence.createTrack();
	}
	
	private void setInstrument (int instrument) throws InvalidMidiDataException{
		track.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE,CHANNEL,instrument,0),0));
	}
	
	private MidiEvent createNoteOnEvent(int note, long tick)	{
		return createNoteEvent(ShortMessage.NOTE_ON,note,VELOCITY,tick);
	}

	private static MidiEvent createNoteOffEvent(int note, long tick){
		return createNoteEvent(ShortMessage.NOTE_OFF,note,0,tick);
	}

	private static MidiEvent createNoteEvent(int command, int note, int velocity, long tick){
		ShortMessage message = new ShortMessage();
		
		try {
			message.setMessage(command,CHANNEL,note,velocity);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		MidiEvent	event = new MidiEvent(message,tick);
		return event;
	}
	
	public Sequence compose (String text, int instrument){
		try {
			setInstrument(instrument);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}

		StringCharacterIterator str = new StringCharacterIterator(text);
		int tick = 0;

		while(str.current() != StringCharacterIterator.DONE){
			if(notesMap.get(str.current()) == null){
				tick++;
				str.next();			
			}
			else{
				track.add(createNoteOnEvent(notesMap.get(str.current()),tick));
				tick++;
				track.add(createNoteOffEvent(notesMap.get(str.current()),tick));
				str.next();
			}
		}
				
		return sequence;		
