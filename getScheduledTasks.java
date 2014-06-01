package read_test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class getScheduledTasks extends ReadRegistry implements Runnable{
	DefaultTableModel tablemodels;
	
	public getScheduledTasks(DefaultTableModel tablemodels){
		this.tablemodels=tablemodels;
	}
	
	public void ReadScheduledTasks() throws IOException{
		File dir=new File("C:\\Windows\\Tasks");
		File[] jobs=dir.listFiles(new FilenameFilter() {		
			@Override
			public boolean accept(File arg0, String arg1) {
				// TODO Auto-generated method stub
				if(arg1.endsWith(".job"))
					return true;
				return false;
			}
		});
		for(File f:jobs){
			Vector<String> row=new Vector<String>();
			row.add(f.getName());
			String path=ParseScheduledTasks(f);
			String[] infos=getInfo(path);
			row.add(infos[0]);
			row.add(infos[1]);
			row.add(infos[2]);
			tablemodels.addRow(row);
		}
	}
	
	public String ParseScheduledTasks(File f) throws IOException{
		RandomAccessFile raf=new RandomAccessFile(f, "r");
		raf.seek(72);
		String path="";
		short word=0;
		while((word=raf.readShort())!=0){
			path+=(char)(word>>8);
		}
		raf.close();
		return path;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ReadScheduledTasks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tablemodels.fireTableDataChanged();
	}

}

	
