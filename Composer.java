import java.nio.ByteBuffer;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.random;
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
	private static final int BPM_DEFAULT = 120;
	private static final int OCTAVE_INCREASE_FACTOR = 12;
	private static final int OCTAVE_DECREASE_FACTOR = -12;
	private static final double BPM_INCREASE_FACTOR = 1.50;
	private static final double BPM_DECREASE_FACTOR = 0.50;
	private static final int NOTE_DURATION_INCREASE_FACTOR = 1;
	
	private static final String CONSONANTS = "hH jJ kK lL mM nN pP qQ rR sS tT vV wW xX yY zZ";
	private static final String VOGALS = "iI oO uU";
	private static final String EVEN = "0 2 4 6 8";
	private static final String ODD = "1 3 5 7 9";
	private static final String COMMA = ",";
	private static final String SEMICOLON = ";";
	private static final String EXCLAMATION_MARK = "!";
	private static final String QUESTION_MARK = "?";
	private static final String QUOTE = "\"";
	private static final String SINGLE_QUOTE = "\'";
	private static final String DOT = ".";
	private static final String NEW_LINE = System.getProperty("line.separator");
	
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
	private int currentBPM = BPM_DEFAULT;
	
	
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
	
	private MidiEvent createBPMIncreaseEvent(long tick)	{
		return createBPMEvent(BPM_INCREASE_FACTOR,tick);
	}

	private static MidiEvent createBPMDecreaseEvent(long tick){
		return createBPMEvent(BPM_DECREASE_FACTOR,tick);
	}
	
	private static MidiEvent createBPMEvent(double value, long tick){
		MetaMessage message = new MetaMessage();
		
		try {
			message.setMessage(META_EVENT_SET_TEMPO,Double.toString(value).getBytes(),Double.toString(value).getBytes().length);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		MidiEvent	event = new MidiEvent(message,tick);
		return event;
	}
	
	private void restoreDefaults(){
		currentSemitomFactor = SEMITOM_FACTOR_DEFAULT;
		currentOctaveFactor = OCTAVE_FACTOR_DEFAULT;
		currentNoteDurationFactor = NOTE_DURATION_FACTOR_DEFAULT;
		currentTimbre = TIMBRE_DEFAULT;
		currentBPM = BPM_DEFAULT;
	}
	
	public Sequence compose (String text){		

		StringCharacterIterator str = new StringCharacterIterator(text);
		int tick = 0;

		while(str.current() != StringCharacterIterator.DONE){
			char currentChar = str.current();
			
			if(CONSONANTS.contains(Character.toString(currentChar))){
				if(currentSemitomFactor > 0){
					currentSemitomFactor--;
					str.next();
				}
			}
			else 
				if(VOGALS.contains(Character.toString(currentChar))){
					if(currentSemitomFactor < 127){
						currentSemitomFactor++;
						str.next();
					}
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
										currentTimbre = random.nextInt(128);
										str.next();
									}
									else
										if(QUOTE.contains(Character.toString(currentChar)) || SINGLE_QUOTE.contains(Character.toString(currentChar))){
											if(currentNoteDurationFactor == NOTE_DURATION_FACTOR_DEFAULT)
												currentNoteDurationFactor+= NOTE_DURATION_INCREASE_FACTOR;
											else
												currentNoteDurationFactor = NOTE_DURATION_FACTOR_DEFAULT;
										}
										else
											if(DOT.contains(Character.toString(currentChar)) || NEW_LINE.contains(Character.toString(currentChar))){
												currentOctaveFactor-=OCTAVE_DECREASE_FACTOR;
												str.next();
											}
											else
												if(NOTES_MAP.get(str.current()) != null){
													int note =  NOTES_MAP.get(str.current()) + currentSemitomFactor + currentOctaveFactor;
													if(note >= 0 && note <= 127){
														track.add(createNoteOnEvent(note,tick));
														tick++;
														track.add(createNoteOffEvent(note,tick));
														str.next();
													}
													else{
														track.add(createNoteOnEvent(NOTES_MAP.get(str.current()),tick));
														tick++;
														track.add(createNoteOffEvent(NOTES_MAP.get(str.current()),tick));
														str.next();
													}
												}
												else{
													tick++;
													str.next();	
												}
			
		}	
		return sequence;		
	}
}
