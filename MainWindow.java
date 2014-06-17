import java.awt.*;

import javax.swing.*;

public class MainWindow extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -501712301015461626L;
	private JTextArea composerTextArea;	
	private JLabel instructionsLabel;
	private JScrollPane composerScroll;
	private JPanel buttonsPanel;
	private JPanel contentPane;
	private JButton playButton;
	private JButton stopButton;
	private JButton openButton;
	private JButton saveButton;
	private PlayerInterface playerInterface;
	private FileInterface fileInterface;
	
	private String instrucoes = "<html><h3>Legenda</h3>"
			+ "<table><tr><td>"
			+ "<b>Letra A</b> - Nota  L&aacute;<br>"
			+ "<b>Letra B</b> - Nota Si<br>"
			+ "<b>Letra C</b> - Nota D&oacute;<br>"
			+ "<b>Letra D</b> - Nota R&eacute;<br>"
			+ "<b>Letra E</b> - Nota Mi<br>"
			+ "<b>Letra F</b> - Nota F&aacute;<br>"
			+ "<b>Letra G</b> - Nota Sol<br></td>"
			+ "<td><b>Demais vogais</b> - Aumentam um semitom<br>"
			+ "<b>Demais consoantes</b> - Diminuem um semitom<br>"
			+ "<b>D&iacute;gito par</b> - Aumenta BPM <br>"
			+ "<b>D&iacute;gito &iacute;mpar</b> - Diminui BPM <br>"
			+ "<b>Exclama&ccedil;&atilde;o (!) ou Interroga&ccedil;&atilde;o (?)</b> - Gera timbre<br>aleatorio de instrumento<br>"
			+ "<b>Ponto e vírgula (;)</b> - Sobe uma oitava<br>"
			+ "<b>Vírgula (,)</b> - Diminui uma oitava<br>"
			+ "<b>Nova linha ou ponto final (.)</b> - retorna à configuração padrão<br>"
			+ "</td></tr></table>"
			+ "<h3>Compositor</h3></html>";
	
	
	public MainWindow() {
		//Configs da janela principal
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setTitle("Compositor");
		
		this.setMinimumSize(new Dimension(600, 400));
		this.contentPane = new JPanel(new BorderLayout());		
		this.contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setContentPane(contentPane);
		
		//Configs das instrucoes
		this.instructionsLabel = new JLabel(instrucoes);	
		this.add(instructionsLabel, BorderLayout.NORTH);
		
		//Configs da area de composicao
		this.composerTextArea = new JTextArea(100,10);		 
		this.composerTextArea.setWrapStyleWord(true);		
		this.composerTextArea.setMinimumSize(new Dimension(320, 300));
		this.composerTextArea.setSize(300, 500);
		this.composerTextArea.setPreferredSize(new Dimension(320, 300));
		
		this.composerScroll = new JScrollPane(composerTextArea);
		this.composerScroll.setViewportView(composerTextArea);
		this.composerScroll.setMinimumSize(new Dimension(320, 300));
		this.composerScroll.setSize(300, 500);
		this.composerScroll.setPreferredSize(new Dimension(320, 300));
		this.add(composerScroll);
		
		//Config Painel dos botoes
		this.buttonsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
		this.buttonsPanel.setSize(100, 100);
		this.add(buttonsPanel, BorderLayout.SOUTH);

		//Configs dos botões
		this.playButton = new JButton("Tocar", new ImageIcon ("icons/play.png"));
		this.buttonsPanel.add(playButton);
		
		this.stopButton = new JButton("Parar", new ImageIcon ("icons/stop.png"));
		this.buttonsPanel.add(stopButton);
		
		this.openButton = new JButton("Abrir");
		this.buttonsPanel.add(openButton);
		
		this.saveButton = new JButton("Salvar");
		this.buttonsPanel.add(saveButton);
		
		//Configs finais da MainWindow
		this.playerInterface = new PlayerInterface(this,this.composerTextArea);
		this.playButton.addActionListener(this.playerInterface);
		this.stopButton.addActionListener(this.playerInterface);
		
		this.fileInterface = new FileInterface(this, this.composerTextArea);
		this.openButton.addActionListener(this.fileInterface);
		this.saveButton.addActionListener(this.fileInterface);
		
		this.setVisible(true);		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);				
	}
	
	public JTextArea getComposer(){
		return this.composerTextArea;
	}
	
}
