package read_test;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class getWinsock extends ReadRegistry implements Runnable {
	DefaultTableModel tablemodels;
	
	public getWinsock(DefaultTableModel tablemodels){
		this.tablemodels=tablemodels;
	}
	
	public void ReadWinsock(RegistryKey rk) throws RegistryException{
		@SuppressWarnings("unchecked")
		Enumeration<String> subkeys=rk.keyElements();
		while(subkeys.hasMoreElements()){
			String key=subkeys.nextElement();
			Vector<String> row=new Vector<String>();
			row.add(rk.openSubKey(key).getStringValue("ProtocolName"));
			String path=getCanonicalPath(rk.openSubKey(key).getStringValue("PackedCatalogItem"));
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
			ReadWinsock(Registry.HKEY_LOCAL_MACHINE.openSubKey("System").openSubKey("CurrentControlSet").openSubKey("Services")
					.openSubKey("WinSock2").openSubKey("Parameters").openSubKey("Protocol_Catalog9").openSubKey("Catalog_Entries"));
		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tablemodels.fireTableDataChanged();
	}

}
