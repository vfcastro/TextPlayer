import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;
//import javax.sound.midi.InvalidMidiDataException;
//import javax.sound.midi.Sequence;


public class FileInterface implements ActionListener{

	private MainWindow wnd;
	private JTextArea text;
	
	public FileInterface(MainWindow wnd, JTextArea text) {
		this.wnd = wnd;
		this.text = text;
	}
	
//	@Override
	public void actionPerformed(ActionEvent e) {
		String acao = e.getActionCommand();
		//Debug das acoes escutadas
		System.err.println(acao);
		
		//Botao Abrir eh clicado
		if(acao.equals("Abrir")){
			JFileChooser fc = new JFileChooser();
			//Se escolheu salvar o arquivo
			if(fc.showOpenDialog(wnd) == JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				try {
					Scanner scanner = new Scanner(file);
					String content = scanner.useDelimiter("\\Z").next();
					text.setText(content);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}				
		}
		else if(acao.equals("Salvar")){
			JFileChooser fc = new JFileChooser();
			//Se escolheu salvar o arquivo
			if(fc.showSaveDialog(wnd) == JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();				
				FileWriter fout;
				try {
					fout = new FileWriter(file);
					fout.write(text.getText());
					fout.close();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}				
		}
	}
	
}
