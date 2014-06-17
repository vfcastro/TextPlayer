import java.nio.ByteBuffer;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class Composer {
	// Constantes utilizadas do padrão MIDI
	private static final int	VELOCITY = 64;
	private static final float	TIMING_TYPE = Sequence.PPQ;
	private static final int	TIMING_RESOLUTION = 1;
	private static final int CHANNEL = 0;
	private static final int META_EVENT_SET_TEMPO = 51;
	
	private static final int SEMITOM_FACTOR_DEFAULT = 0;
	private static final int OCTAVE_FACTOR_DEFAULT = 0;
	private static final int NOTE_DURATION_FACTOR_DEFAULT = 0;
	private static final int TIMBRE_DEFAULT = 0;
	private static final float BPM_DEFAULT = 120f;
	private static final int OCTAVE_INCREASE_FACTOR = 12;
	private static final int OCTAVE_DECREASE_FACTOR = -12;
	private static final float BPM_INCREASE_FACTOR = 1.50f;
	private static final float BPM_DECREASE_FACTOR = 0.50f;
	private static final int NOTE_DURATION_INCREASE_FACTOR = 1;
	
	private static final String CONSONANTS = "hHjJkKlLmMnNpPqQrRsStTvVwWxXyYzZ";
	private static final String VOGALS = "iIoOuU";
	private static final String EVEN = "02468";
	private static final String ODD = "13579";
	private static final String COMMA = ",";
	private static final String SEMICOLON = ";";
	private static final String EXCLAMATION_MARK = "!";
	private static final String QUESTION_MARK = "?";
	private static final String QUOTE = "\"";
	private static final String SINGLE_QUOTE = "\'";
	private static final String DOT = ".";
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String SPACE = " ";
	
	private static final Map<Character, Integer> NOTES_MAP;
    static
    {
    	NOTES_MAP = new HashMap<Character, Integer>();
    	NOTES_MAP.put('a', 69);
    	NOTES_MAP.put('b', 71);
    	NOTES_MAP.put('c', 60);
    	NOTES_MAP.put('d', 62);
    	NOTES_MAP.put('e', 64);
    	NOTES_MAP.put('f', 65);
    	NOTES_MAP.put('g', 67);
    	NOTES_MAP.put('A', 69);
    	NOTES_MAP.put('B', 71);
    	NOTES_MAP.put('C', 60);
    	NOTES_MAP.put('D', 62);
    	NOTES_MAP.put('E', 64);
    	NOTES_MAP.put('F', 65);
    	NOTES_MAP.put('G', 67);
    }
	
	private Sequence	sequence;
	private Track track;
	private int currentSemitomFactor = SEMITOM_FACTOR_DEFAULT;
	private int currentOctaveFactor = OCTAVE_FACTOR_DEFAULT;
	private int currentNoteDurationFactor = NOTE_DURATION_FACTOR_DEFAULT;
	private int  currentTimbre = TIMBRE_DEFAULT;
	private float currentBPM = BPM_DEFAULT;
	
	public Composer() throws InvalidMidiDataException{
		sequence = new Sequence(TIMING_TYPE, TIMING_RESOLUTION);
		track = sequence.createTrack();
	}
	
	private void setInstrument (int instrument, long tick) throws InvalidMidiDataException{
		track.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE,CHANNEL,instrument,0),tick));
	}
	
	private MidiEvent createNoteOnEvent(int note, long tick)	{
		return createNoteEvent(ShortMessage.NOTE_ON,note,VELOCITY,tick);
	}

	private MidiEvent createNoteOffEvent(int note, long tick){
		return createNoteEvent(ShortMessage.NOTE_OFF,note,0,tick);
	}

	private MidiEvent createNoteEvent(int command, int note, int velocity, long tick){
		ShortMessage message = new ShortMessage();
		
		try {
			message.setMessage(command,CHANNEL,note,velocity);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		MidiEvent	event = new MidiEvent(message,tick);
		return event;
	}
	
	private MidiEvent createBPMIncreaseEvent(long tick)	{
		currentBPM = currentBPM * BPM_INCREASE_FACTOR;
		return createBPMEvent(currentBPM,tick);
	}

	private MidiEvent createBPMDecreaseEvent(long tick){
		currentBPM = currentBPM * BPM_DECREASE_FACTOR;
		return createBPMEvent(currentBPM,tick);
	}
	
	private MidiEvent createBPMEvent(float value, long tick){
		MetaMessage message = new MetaMessage();
		
		try {
			message.setMessage(META_EVENT_SET_TEMPO,ByteBuffer.allocate(Float.SIZE).putFloat(value).array(),Float.SIZE);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		MidiEvent	event = new MidiEvent(message,tick);
		return event;
	}
	
	private void restoreDefaults(long tick) throws InvalidMidiDataException{
		currentSemitomFactor = SEMITOM_FACTOR_DEFAULT;
		currentOctaveFactor = OCTAVE_FACTOR_DEFAULT;
		currentNoteDurationFactor = NOTE_DURATION_FACTOR_DEFAULT;
		currentTimbre = TIMBRE_DEFAULT;
		currentBPM = BPM_DEFAULT;
		setInstrument(TIMBRE_DEFAULT,tick);
		createBPMEvent(BPM_DEFAULT,tick);
		
	}
	
	public Sequence compose (String text) throws InvalidMidiDataException{		

		StringCharacterIterator str = new StringCharacterIterator(text);
		long tick = 0;

		while(str.current() != StringCharacterIterator.DONE){
			char currentChar = str.current();
			
			if(CONSONANTS.contains(Character.toString(currentChar))){
				currentSemitomFactor--;
				str.next();
			}
			else 
				if(VOGALS.contains(Character.toString(currentChar))){
					currentSemitomFactor++;
					str.next();
				}
				else
					if(EVEN.contains(Character.toString(currentChar))){
						track.add(createBPMIncreaseEvent(tick));
						str.next();
					}
					else
						if(ODD.contains(Character.toString(currentChar))){
							track.add(createBPMDecreaseEvent(tick));
							str.next();
						}
						else
							if(COMMA.contains(Character.toString(currentChar))){
								currentOctaveFactor+=OCTAVE_INCREASE_FACTOR;
								str.next();
							}
							else
								if(SEMICOLON.contains(Character.toString(currentChar))){
									currentOctaveFactor-=OCTAVE_DECREASE_FACTOR;
									str.next();
								}
								else
									if(EXCLAMATION_MARK.contains(Character.toString(currentChar)) || QUESTION_MARK.contains(Character.toString(currentChar))){
										currentTimbre = (new Random()).nextInt(128);
										setInstrument(currentTimbre,tick);
										str.next();
									}
									else
										if(QUOTE.contains(Character.toString(currentChar)) || SINGLE_QUOTE.contains(Character.toString(currentChar))){
											if(currentNoteDurationFactor == NOTE_DURATION_FACTOR_DEFAULT)
												currentNoteDurationFactor+= NOTE_DURATION_INCREASE_FACTOR;
											else
												currentNoteDurationFactor = NOTE_DURATION_FACTOR_DEFAULT;
											str.next();
										}
										else
											if(DOT.contains(Character.toString(currentChar)) || NEW_LINE.contains(Character.toString(currentChar))){
												restoreDefaults(tick);
												str.next();
											}
											else
												if(NOTES_MAP.get(str.current()) != null){
													int note =  NOTES_MAP.get(str.current()) + currentSemitomFactor + currentOctaveFactor;
													if(note >= 0 && note <= 127){
														track.add(createNoteOnEvent(note,tick));
														tick+=currentNoteDurationFactor + 1;
														track.add(createNoteOffEvent(note,tick));
														str.next();
													}
													else{
														track.add(createNoteOnEvent(NOTES_MAP.get(str.current()),tick));
														tick+=currentNoteDurationFactor + 1;
														track.add(createNoteOffEvent(NOTES_MAP.get(str.current()),tick));
														str.next();
													}
												}
												else{
													if(SPACE.contains(Character.toString(currentChar))){
														tick+=currentNoteDurationFactor + 1;
														str.next();
													}
													else
														str.next();	
												}
		}	
		return sequence;		
	}
}
