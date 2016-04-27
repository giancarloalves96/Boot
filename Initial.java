import javax.swing.*;

import java.text.ParseException;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;

public class Initial extends JFrame implements ActionListener, WindowListener{
	
	// Instance attributes used in this example
	private	JPanel topPanel;
	private	JList listbox;
	private JButton nameFilter;
	private JButton tagFilter;
	private JTextField nameText;
	private JTextField tagText;
	
	private MenuBar menuBar = new MenuBar(); // first, create a MenuBar item
	
	private Menu file = new Menu(); 
	private Menu view = new Menu(); 
	private Menu filter = new Menu();
	
	private MenuItem newFile = new MenuItem();
	private MenuItem delete = new MenuItem();
	private MenuItem edit = new MenuItem();
	private MenuItem open = new MenuItem();
	private MenuItem close = new MenuItem();
	
	private MenuItem orderName = new MenuItem();
	private MenuItem orderDate = new MenuItem();
	
	private MenuItem all = new MenuItem();
	
	private ArrayList<Note> Notes=new ArrayList<Note>();
	private ArrayList<Note> CurrentNotes=new ArrayList<Note>();
	
	// Constructor of main frame
	public Initial() throws IOException, ParseException
	{
		// Set the frame characteristics
		setTitle( "Boot" );
		setSize( 500, 300 );
		setBackground( Color.white );

		this.setMenuBar(this.menuBar);
		this.menuBar.add(this.file);
		this.menuBar.add(this.view);
		this.menuBar.add(this.filter);
 
		this.file.setLabel("File");
		this.view.setLabel("View");
		this.filter.setLabel("Filter");
 
		this.newFile.setLabel("New"); // set the label of the menu item
		this.newFile.addActionListener(this); // add an action listener (so we know when it's been clicked
		this.newFile.setShortcut(new MenuShortcut(KeyEvent.VK_N, false)); // set a keyboard shortcut
		this.file.add(this.newFile); // add it to the "File" menu
		
		this.delete.setLabel("Delete");
		this.delete.setShortcut(new MenuShortcut(KeyEvent.VK_D, false));
		this.delete.addActionListener(this);
		this.file.add(this.delete);
		
		this.edit.setLabel("Edit Name and Tags");
		this.edit.setShortcut(new MenuShortcut(KeyEvent.VK_E, false));
		this.edit.addActionListener(this);
		this.file.add(this.edit);
		
		this.open.setLabel("Open");
		this.open.setShortcut(new MenuShortcut(KeyEvent.VK_A, false));
		this.open.addActionListener(this);
		this.file.add(this.open);
		
		this.close.setLabel("Close");
		this.close.setShortcut(new MenuShortcut(KeyEvent.VK_F4, false));
		this.close.addActionListener(this);
		this.file.add(this.close);
 
		this.orderName.setLabel("Order by Name");
		this.orderName.addActionListener(this);
		this.orderName.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
		this.view.add(this.orderName);
		
		this.orderDate.setLabel("Order by Date");
		this.orderDate.addActionListener(this);
		this.orderDate.setShortcut(new MenuShortcut(KeyEvent.VK_P, false));
		this.view.add(this.orderDate);
		
		this.all.setLabel("Reset");
		this.all.addActionListener(this);
		this.all.setShortcut(new MenuShortcut(KeyEvent.VK_R, false));
		this.filter.add(this.all);

		// Create a panel to hold all other components
		topPanel = new JPanel();
		topPanel.setLayout( new FlowLayout() );
		getContentPane().add( topPanel );
		
		nameText=new JTextField(20);
		topPanel.add(nameText);
		
		nameFilter=new JButton("Add Name Filter");
		nameFilter.addActionListener(this);
		topPanel.add(nameFilter);
		
		tagText=new JTextField(20);
		topPanel.add(tagText);
		
		tagFilter=new JButton("Add Tag Filter");
		tagFilter.addActionListener(this);
		topPanel.add(tagFilter);
		
		listbox=new JList();
		topPanel.add( listbox, BorderLayout.PAGE_END );
		Load();
	}
	
	private void Load() throws ParseException{
		try{
			Notes.clear();
			File[] files = new File("C:\\Users\\Giancarlo\\Desktop\\Notes\\").listFiles();
			Note note = null;
			BufferedReader br = null;
			for(File file : files){
				br = null;
				br = new BufferedReader(new FileReader(file));
				String name = br.readLine();
				String date = br.readLine();
				ArrayList<String> tags = new ArrayList<String>();
				String line = br.readLine();
				while(!line.equals("********************") && line!=null){
					tags.add(line);
					line=br.readLine();
				}
				String text="";
				line=br.readLine();
				while(line!=null){
					text+=line;
					text+="\n";
					line=br.readLine();
				}
				note=new Note(name, date, tags, text, file.getAbsolutePath());
				Notes.add(note);
				br.close();
			}
			setCurrent();
			refreshList();	
		}
		catch(IOException e1){
			System.out.println("Exceção IO em Load: "+e1.toString());
		}
	}
	
	private void setCurrent(){
		CurrentNotes.clear();
		for(Note note:Notes){
			CurrentNotes.add(note);
		}
	}
	
	private void refreshList(){
		DefaultListModel DLM = new DefaultListModel();
		for(Note note:CurrentNotes){
			DLM.addElement(note.elementString());
		}
		listbox.setModel(DLM);
	}
	
	public void actionPerformed (ActionEvent e){
		
		if (e.getSource() == this.close)
			this.dispose();
		
		if(e.getSource()==this.delete){
			if(!listbox.isSelectionEmpty()){
				int index = listbox.getSelectedIndex();
				try {
					CurrentNotes.get(index).Delete();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					Load();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					System.out.println("Exception: "+e1.toString());
				}
			}
		}
		
		if(e.getSource()==this.open){
			if(!listbox.isSelectionEmpty()){
				int index = listbox.getSelectedIndex();
				Note note = CurrentNotes.get(index);
				Notepad pad = new Notepad(note);
				pad.addWindowListener(this);
				pad.setVisible(true);
			}
		}
		
		if(e.getSource()==this.edit){
			if(!listbox.isSelectionEmpty()){
				int index = listbox.getSelectedIndex();
				Note note = CurrentNotes.get(index);
				Filters fil = new Filters(note);
				fil.addWindowListener(this);
				fil.setVisible(true);
			}
		}
		
		if(e.getSource()==this.newFile){
			String newpath = "C:\\Users\\Giancarlo\\Desktop\\Notes\\"+UUID.randomUUID().toString()+".txt";
			Note note = new Note("", new Date(), new ArrayList<String>(), "", newpath);
			Filters fil = new Filters(note);
			fil.addWindowListener(this);
			fil.setVisible(true);
			Notepad pad = new Notepad(note);
			pad.addWindowListener(this);
			pad.setVisible(true);
		}
		
		if(e.getSource()==this.orderName){
			Collections.sort(CurrentNotes, Note.NameComparator);
			refreshList();
		}
		
		if(e.getSource()==this.orderDate){
			Collections.sort(CurrentNotes, Note.DateComparator);
			refreshList();
		}
		
		if(e.getSource()==this.all){
			setCurrent();
			refreshList();
		}
		
		if(e.getSource()==this.nameFilter){
			String filter = this.nameText.getText();
			CurrentNotes.clear();
			for(Note note:Notes){
				if(filter.equals(note.get_Name()))
					CurrentNotes.add(note);
			}
			refreshList();
		}
		
		if(e.getSource()==this.tagFilter){
			String filter = this.tagText.getText();
			ArrayList<Note> Temp = new ArrayList<Note>();
			for(Note note:CurrentNotes){
				Temp.add(note);
			}
			CurrentNotes.clear();
			for(Note note:Temp){
				if(note.ContainsTag(filter))
					CurrentNotes.add(note);
			}
			refreshList();
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
			try {
				Load();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
