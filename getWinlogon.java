package read_test;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class getWinlogon extends ReadRegistry implements Runnable {
	DefaultTableModel tablemodels;
	
	public getWinlogon(DefaultTableModel tablemodels){
		this.tablemodels=tablemodels;
	}
	
	public void ReadWinlogon(RegistryKey rk) throws RegistryException{
		@SuppressWarnings("unchecked")
		Enumeration<String> keys=rk.keyElements();
		while(keys.hasMoreElements()){
			String name=keys.nextElement();
			String dllname=rk.openSubKey(name).getStringValue("DLLName");
			for(String systempath:System.getenv("path").split(";")){
				String path=systempath+"\\"+dllname;
				if(new File(path).exists()){
					Vector<String> row=new Vector<String>();
					row.add(name);
					String[] infos=getInfo(path);
					row.add(infos[0]);
					row.add(infos[1]);
					row.add(infos[2]);
					tablemodels.addRow(row);
				}
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ReadWinlogon(Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE").openSubKey("Microsoft").openSubKey("Windows NT")
					.openSubKey("CurrentVersion").openSubKey("Winlogon").openSubKey("Notify"));
		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
