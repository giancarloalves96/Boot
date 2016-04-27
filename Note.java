import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Note {
	private String Name;
	private ArrayList<String> Tags;
	private Date Datetime; 
	private String Text;
	private String path;
	
	public Note(String name, Date date, ArrayList<String> tags, String text, String path){
		this.Name=name;
		this.Datetime=date;
		this.Tags = tags;
		this.Text=text;
		this.path=path;
	}
	
	public Note(String name, String date, ArrayList<String> tags, String text, String path){
		Date datetime;
		try{
			datetime = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(date);
		}
		catch(ParseException e){
			datetime = new Date(0);
		}
		this.Name=name;
		this.Datetime=datetime;
		this.Tags = tags;
		this.Text=text;
		this.path=path;
	}
	
	public Boolean ContainsTag(String tag){
		Boolean contains = false;
		for(String t:this.Tags){
			if(t.equals(tag))
				contains=true;
		}
		return contains;
	}
	
	public void Delete() throws IOException{
		Files.deleteIfExists(Paths.get(this.path));
	}
	
	public String elementString(){
		String element="Nome: "+this.Name+" :: ";
		
		String tags="";
		for(int i=0; i<this.Tags.size()-1;i++){
			tags+=this.Tags.get(i);
			tags+=",";
		}
		if(this.Tags.size()>0)
			tags+=this.Tags.get(this.Tags.size()-1);
		element+="Tags: "+tags+" :: ";
		
		String date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(this.Datetime);
		element+="Data: "+date;
		
		return element;
	}
	
	public String get_Name(){
		return this.Name;
	}
	
	public ArrayList<String> get_Tags(){
		return this.Tags;
	}
	
	public Date get_Date(){
		return this.Datetime;
	}
	
	public String get_Text(){
		return this.Text;
	}
	
	public String get_Path(){
		return this.path;
	}
	
	public void set_Name(String Name){
		this.Name=Name;
	}
	
	public void add_Tag(String Tag){
		Tags.add(Tag);
	}
	
	public void reset_Tags(){
		Tags.clear();
	}
	
	public static Comparator<Note> NameComparator = new Comparator<Note>(){
		public int compare(Note n1, Note n2){
			return n1.get_Name().compareTo(n2.get_Name());
		}
	};
	
	public static Comparator<Note> DateComparator = new Comparator<Note>(){
		public int compare(Note n1, Note n2){
			return n1.get_Date().compareTo(n2.get_Date());
		}
	};
} 
