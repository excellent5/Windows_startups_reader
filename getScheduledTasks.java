package read_test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

import javax.swing.JOptionPane;
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
	
	public String ParseScheduledTasks(File f){
		RandomAccessFile raf=null;
		String path="";
		try{
			raf=new RandomAccessFile(f, "r");
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Tasks目录拒绝访问，请用管理员权限打开软件查看"
					, "拒绝访问", JOptionPane.ERROR_MESSAGE);
		}
		try {
			raf.seek(72);
			short word=0;
			while((word=raf.readShort())!=0){
				path+=(char)(word>>8);
			}
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			path="can't get path";
		}
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

	
