package read_test;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;


public class getIE_BHO extends ReadRegistry implements Runnable{
	DefaultTableModel tablemodels;
	
	public getIE_BHO(DefaultTableModel tablemodels){
		this.tablemodels=tablemodels;
	}
	
	public void getBHO(RegistryKey rk) throws RegistryException{
		@SuppressWarnings("unchecked")
		Enumeration<String> keys=rk.keyElements();
		while(keys.hasMoreElements()){
			String key=keys.nextElement();
			findCLSID(key);
		}
	}
	
	public void getIE(RegistryKey rk) throws RegistryException{
		@SuppressWarnings("unchecked")
		Enumeration<String> valueNames=rk.valueElements();
		while(valueNames.hasMoreElements()){
			String valueName=valueNames.nextElement();
			if(!valueName.equals("")){
				findCLSID(valueName);
			}
		}
	}
	
	public void getExtension(RegistryKey rk) throws RegistryException{
		@SuppressWarnings("unchecked")
		Enumeration<String> keys=rk.keyElements();
		while(keys.hasMoreElements()){
			RegistryKey subkey=rk.openSubKey(keys.nextElement());
			findCLSID(subkey.getStringValue("CLSID"));
		}
	}
	
	public void findCLSID(String key) {
		try{
			RegistryKey rk=Registry.HKEY_CLASSES_ROOT.openSubKey("CLSID").openSubKey(key);
			Vector<String> row=new Vector<String>();
			row.add(rk.getStringValue(""));
			String path=rk.openSubKey("InprocServer32").getStringValue("");
			String[] infos=getInfo(path);
			row.add(infos[0]);
			row.add(infos[1]);
			row.add(infos[2]);
			tablemodels.addRow(row);
		}
		catch(RegistryException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			getBHO(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Microsoft").openSubKey("Windows")
					.openSubKey("CurrentVersion").openSubKey("Explorer").openSubKey("Browser Helper Objects"));
			
			getBHO(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Wow6432Node").openSubKey("Microsoft")
					.openSubKey("Windows").openSubKey("CurrentVersion").openSubKey("Explorer").openSubKey("Browser Helper Objects"));
			
			getIE(Registry.HKEY_CURRENT_USER.openSubKey("Software").openSubKey("Microsoft").openSubKey("Internet Explorer")
					.openSubKey("UrlSearchHooks"));
			
			getIE(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Wow6432Node").openSubKey("Microsoft")
					.openSubKey("Internet Explorer").openSubKey("Toolbar"));
			
			getExtension(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Microsoft").openSubKey("Internet Explorer")
					.openSubKey("Extensions"));
			
			getExtension(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Wow6432Node").openSubKey("Microsoft")
					.openSubKey("Internet Explorer").openSubKey("Extensions"));
			
		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tablemodels.fireTableDataChanged();
	}
	

}
