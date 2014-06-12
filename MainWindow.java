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
	
	private String instrucoes = 
			  "<html><h2>LEGENDA</h2>"
			+ "<table><tr>"
			+ "<td><h3>Notas Principais</h3></td>"
			+ "<td><h3>Demais Instrucoes</h3></td></tr>"
			+ "<tr><td><b>A/a</b> - Acorde  L&aacute;<br>"
			+ "<b>B/b</b> - Acorde Si<br>"
			+ "<b>C/c</b> - Acorde D&oacute;<br>"
			+ "<b>D/d</b> - Acorde R&eacute;<br>"
			+ "<b>E/e</b> - Acorde Mi<br>"
			+ "<b>F/f</b> - Acorde F&aacute;<br>"
			+ "Demais vogais ap&oacute;s notas - Aumentam um semitom<br>"
			+ "Demais consoantes ap&oacute;s notas - Diminuem um semitom<br></td>"
			+ "<td>Exclama&ccedil;&atilde;o (!) - Repete tudo desde a &uacute;ltima !<br>"
			+ "D&iacute;gito par - Uma oitava acima <br>"
			+ "D&iacute;gito &iacute;mpar - Uma oitava abaixo <br>"
			+ "? ou . - Aumenta 3 vezes a dura&ccedil;&atilde;o da nota que est&aacute; tocando<br>"
			+ "Nova Linha - Volta a tocar com a oitava original<br>"
			+ "; - Baixa o BPM<br>"
			+ ", - Aumenta o BPM<br>"
			+ "</td></tr></table></html>";
	
	
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
		this.setTitle("Compositor MIDI");
		
		this.setMinimumSize(new Dimension(600, 400));
		this.contentPane = new JPanel(new BorderLayout());		
		this.contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setContentPane(contentPane);
		
		//Configs da area de composicao
		this.composerTextArea = new JTextArea(100,10);		
		this.composerTextArea.setWrapStyleWord(true);		
		this.composerTextArea.setMinimumSize(new Dimension(320, 240));
		this.composerTextArea.setSize(320, 480);
		this.composerTextArea.setPreferredSize(new Dimension(320, 280));
		
		this.composerScroll = new JScrollPane(composerTextArea);
		this.composerScroll.setViewportView(composerTextArea);
		this.composerScroll.setMinimumSize(new Dimension(320, 240));
		this.composerScroll.setSize(320, 480);
		this.composerScroll.setPreferredSize(new Dimension(320, 280));
		this.add(composerScroll);
		
		//Configs das instrucoes
		this.instructionsLabel = new JLabel(instrucoes);	
		this.add(instructionsLabel, BorderLayout.NORTH);
		
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
