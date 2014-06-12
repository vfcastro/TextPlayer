import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.swing.JTextArea;


public class PlayerInterface implements ActionListener{

	private MainWindow wnd;
	private JTextArea text;
	private int instrument;
	private int bpm;
	private Sequence seq;
	private AudioPlayer player;
	private Composer composer;
	
	public PlayerInterface(MainWindow wnd, JTextArea text) {
		this.wnd = wnd;
		this.text = text;
		this.instrument = 1;
		this.bpm = 60;

		this.createPlayer();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String acao = e.getActionCommand();
		//Debug das acoes escutadas
		System.err.println(acao);		
		
		//Botao Tocar eh clicado
		if(acao.equals("Tocar")){
			
			// Verifica se o player esta em execucao
			if(!player.isAlive()){
				this.createPlayer();
				this.player.start();
			}
		}
		
		//Botão Parar eh clicado
		else if(acao.equals("Parar")){
			player.interrupt();
			
		}

	}
	
	private void setText(){
		this.text = this.wnd.getComposer();
		this.seq = composer.compose(this.text.getText()+" ", instrument);
	}
	
	private void createPlayer() {
		try{
			this.composer = new Composer();
			this.setText();
		
			this.player = new AudioPlayer(seq, bpm);
		}
		catch (InvalidMidiDataException e2){
			e2.printStackTrace();
		}
		
	}
	
}
