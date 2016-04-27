import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.io.*;
 
public class Filters extends JFrame implements ActionListener {
	private Note note;
	
	private JTextField name = new JTextField(20);
	private JTextField tag = new JTextField(20);
	private JButton bname = new JButton("Add Name");
	private JButton btag = new JButton("Add Tag");
	
	private MenuBar menuBar = new MenuBar();
	private Menu file = new Menu();
	private MenuItem saveFile = new MenuItem(); 
	private MenuItem close = new MenuItem();
	
  	public Filters(Note note) {
  		this.note=note;
  		note.reset_Tags();
  		
		this.setSize(350, 150); // set the initial size of the window
		this.setTitle("Name and Tag"); // set the title of the window
		setDefaultCloseOperation(EXIT_ON_CLOSE); // set the default close operation (exit when it gets closed)
		this.setLayout(new FlowLayout()); // the BorderLayout bit makes it fill it automatically
		
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
		
		bname.addActionListener(this);
		btag.addActionListener(this);
		
		add(name);
		add(bname);
		add(tag);
		add(btag);
	}
  	
  	private String txtFromNote(){
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
		txt+=note.get_Text();
		return txt;
	}
 
	public void actionPerformed (ActionEvent e) {
		if (e.getSource() == this.close)
			this.dispose();
		if (e.getSource() == this.saveFile) {
			try {
				String path;
				
				path=this.note.get_Path();
				
				BufferedWriter out = new BufferedWriter(new FileWriter(path));
				out.write(txtFromNote());
				out.close();
				
			} catch (Exception ex) { 
				System.out.println(ex.getMessage());
			}
		}
		if(e.getSource()==this.bname){
			note.set_Name(name.getText());
		}
		if(e.getSource()==this.btag){
			note.add_Tag(tag.getText());
		}
	}
}
