package read_test;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class getKnownDLLs extends ReadRegistry implements Runnable{
	DefaultTableModel tablemodels;
	
	public getKnownDLLs(DefaultTableModel tablemodels){
		this.tablemodels=tablemodels;
	}
	
	public void ReadKnownDLLs() throws NoSuchKeyException, RegistryException{
		RegistryKey rk=Registry.HKEY_LOCAL_MACHINE.openSubKey("System").openSubKey("CurrentControlSet").openSubKey("Control")
				.openSubKey("Session Manager").openSubKey("KnownDlls");
		@SuppressWarnings("unchecked")
		Enumeration<String> valueNames=rk.valueElements();
		while(valueNames.hasMoreElements()){
			String key=valueNames.nextElement();
			String path=rk.getStringValue(key);
			Vector<String> row=new Vector<String>();
			row.add(key);
			String[] infos=getInfo("C:\\Windows\\System32\\"+path);
			row.add(infos[0]);
			row.add(infos[1]);
			row.add(infos[2]);
			tablemodels.addRow(row);
			
			Vector<String> row2=new Vector<String>();
			row2.add(key);
			String[] infos2=getInfo("C:\\Windows\\Syswow64\\"+path);
			row2.add(infos2[0]);
			row2.add(infos2[1]);
			row2.add(infos2[2]);
			tablemodels.addRow(row2);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ReadKnownDLLs();
		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tablemodels.fireTableDataChanged();
	}

}
