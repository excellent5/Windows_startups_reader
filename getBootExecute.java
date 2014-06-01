package read_test;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class getBootExecute extends ReadRegistry implements Runnable {
	DefaultTableModel tablemodels;
	
	public getBootExecute(DefaultTableModel tablemodels){
		this.tablemodels=tablemodels;
	}
	
	public void ReadBootExecute(RegistryKey rk) throws RegistryException{
		String value=rk.getStringValue("BootExecute");
		String path="C:\\Windows\\System32\\"+value.split(" ")[1]+".exe";
		Vector<String> row=new Vector<String>();
		row.add(value);
		String[] infos=getInfo(path);
		row.add(infos[0]);
		row.add(infos[1]);
		row.add(infos[2]);
		tablemodels.addRow(row);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ReadBootExecute(Registry.HKEY_LOCAL_MACHINE.openSubKey("System").openSubKey("CurrentControlSet").openSubKey("Control")
					.openSubKey("Session Manager"));
		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tablemodels.fireTableDataChanged();
	}
}
