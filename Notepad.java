import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
 
public class Notepad extends JFrame implements ActionListener {
	private Note note;
	
	private TextArea textArea = new TextArea("", 0,0, TextArea.SCROLLBARS_VERTICAL_ONLY);
  	private MenuBar menuBar = new MenuBar(); 
	private Menu file = new Menu(); 
	private MenuItem saveFile = new MenuItem(); 
	private MenuItem close = new MenuItem(); 
 
	public Notepad(Note note) {
		this.note=note;
		
		this.setSize(500, 300);
		this.setTitle(note.get_Name());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.textArea.setFont(new Font("Century Gothic", Font.BOLD, 12));
		this.textArea.setText(note.get_Text());
		this.textArea.setEditable(true);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(textArea);
 
		this.setMenuBar(this.menuBar);
		this.menuBar.add(this.file);
 
		this.file.setLabel("File");
		
		this.saveFile.setLabel("Save");
		this.saveFile.addActionListener(this);
		this.saveFile.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
		this.file.add(this.saveFile);
 
		this.close.setLabel("Close");
		this.close.setShortcut(new MenuShortcut(KeyEvent.VK_F4, false));
		this.close.addActionListener(this);
		this.file.add(this.close);
	}
	
	private String txtFromNote(String text){
		String txt="";
		txt+=this.note.get_Name();
		txt+="\n";
		txt+=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(this.note.get_Date());
		txt+="\n";
		for(String s:this.note.get_Tags()){
			txt+=s;
			txt+="\n";
		}
		txt+="********************\n";
		txt+=text;
		return txt;
	}
 
	public void actionPerformed (ActionEvent e) {
		
		if (e.getSource() == this.close)
			this.dispose(); 
 
		else if (e.getSource() == this.saveFile) {
			try {
				String path;
				
				path=this.note.get_Path();
				
				BufferedWriter out = new BufferedWriter(new FileWriter(path));
				out.write(txtFromNote(this.textArea.getText()));
				out.close();
				
			} catch (Exception ex) { 
				System.out.println(ex.getMessage());
			}
		}
	}
}