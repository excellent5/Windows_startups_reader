package read_test;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class getLogon extends ReadRegistry implements Runnable{
	DefaultTableModel tablemodels;
	
	public getLogon(DefaultTableModel tablemodels){
		this.tablemodels=tablemodels;
	}
	
	public void ReadWinlogon(String name) throws NoSuchKeyException, RegistryException, IOException{
		String path="";
		if(name.contains("\\"))
			path=getCanonicalPath(name);
		if(new File(path).exists()){
			Vector<String> row=new Vector<String>();
			row.add(name);
			path=getCanonicalPath(path);
			String[] infos=getInfo(path);
			row.add(infos[0]);
			row.add(infos[1]);
			row.add(infos[2]);
			tablemodels.addRow(row);
		}
		
		else{
			for(String systempath:System.getenv("path").split(";")){
				if(!name.endsWith(".exe")){
					name+=".exe";
				}
				path=systempath+"\\"+name;
				if(new File(path).exists()){
					Vector<String> row=new Vector<String>();
					row.add(name);
					path=getCanonicalPath(path);
					String[] infos=getInfo(path);
					row.add(infos[0]);
					row.add(infos[1]);
					row.add(infos[2]);
					tablemodels.addRow(row);
				}
			}
		}
	}
	
	public void ReadRun(RegistryKey rk) throws NoSuchKeyException, RegistryException, IOException{
		@SuppressWarnings("unchecked")
		Enumeration<String> valueNames=rk.valueElements();
		while(valueNames.hasMoreElements()){
			String key=valueNames.nextElement();
			String path=rk.getStringValue(key);
			Vector<String> row=new Vector<String>();
			row.add(key);
			path=getCanonicalPath(path);
			String[] infos=getInfo(path);
			row.add(infos[0]);
			row.add(infos[1]);
			row.add(infos[2]);
			tablemodels.addRow(row);
			
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ReadWinlogon(Registry.HKEY_LOCAL_MACHINE.openSubKey("System").openSubKey("CurrentControlSet").openSubKey("Control").
					openSubKey("Terminal Server").openSubKey("Wds").openSubKey("rdpwd").getStringValue("StartupPrograms"));
			
			ReadWinlogon(Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE").openSubKey("Microsoft").openSubKey("Windows NT")
					.openSubKey("CurrentVersion").openSubKey("Winlogon").getStringValue("Userinit"));
			
			ReadWinlogon(Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE").openSubKey("Microsoft").openSubKey("Windows NT")
					.openSubKey("CurrentVersion").openSubKey("Winlogon").getStringValue("shell"));
			
			ReadRun(Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE").openSubKey("Microsoft").openSubKey("Windows")
					.openSubKey("CurrentVersion").openSubKey("Run"));
			
			ReadRun(Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE").openSubKey("Wow6432Node").openSubKey("Microsoft")
					.openSubKey("Windows").openSubKey("CurrentVersion").openSubKey("Run"));
			
			ReadRun(Registry.HKEY_CURRENT_USER.openSubKey("Software").openSubKey("Microsoft").openSubKey("Windows")
					.openSubKey("CurrentVersion").openSubKey("Run"));
			
			File dir=new File(System.getenv("USERPROFILE")+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup");
			for(File f:dir.listFiles()){
				if(f.getName().endsWith(".lnk")){
					Vector<String> row=new Vector<String>();
					row.add(f.getName());
					String path=new ParseLinkFile(f).getRealFilename();
					int index=path.indexOf("\\Users");
					path=System.getenv("systemdrive")+path.substring(index);
					String[] infos=getInfo(path);
					row.add(infos[0]);
					row.add(infos[1]);
					row.add(infos[2]);
					tablemodels.addRow(row);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tablemodels.fireTableDataChanged();
	}


}
