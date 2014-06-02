package read_test;

import java.io.UnsupportedEncodingException;
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
	
	public void getBHO(RegistryKey rk, boolean is32) throws RegistryException{
		@SuppressWarnings("unchecked")
		Enumeration<String> keys=rk.keyElements();
		while(keys.hasMoreElements()){
			String key=keys.nextElement();
			findCLSID(key, is32);
		}
	}
	
	public void getIE(RegistryKey rk, boolean is32) throws RegistryException{
		@SuppressWarnings("unchecked")
		Enumeration<String> valueNames=rk.valueElements();
		while(valueNames.hasMoreElements()){
			String valueName=valueNames.nextElement();
			if(!valueName.equals("")){
				findCLSID(valueName, is32);
			}
		}
	}
	
	public void getExtension(RegistryKey rk, boolean is32) throws RegistryException{
		@SuppressWarnings("unchecked")
		Enumeration<String> keys=rk.keyElements();
		while(keys.hasMoreElements()){
			RegistryKey subkey=rk.openSubKey(keys.nextElement());
			findCLSID(subkey.getStringValue("CLSID"), is32);
		}
	}
	
	public void findCLSID(String key, boolean is32) {
		try{
			RegistryKey rk=null;
			if(is32){
				rk=Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE").openSubKey("Classes")
					.openSubKey("CLSID").openSubKey(key);
			}
			else{
				rk=Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE").openSubKey("Wow6432Node").openSubKey("Classes")
						.openSubKey("CLSID").openSubKey(key);
			}
			Vector<String> row=new Vector<String>();
			row.add(decode(rk.getStringValue("")));
			String path=rk.openSubKey("InprocServer32").getStringValue("");
			String[] infos=getInfo(path);
			row.add(infos[0]);
			row.add(infos[1]);
			row.add(infos[2]);
			tablemodels.addRow(row);
		}
		catch(RegistryException e){
			//e.printStackTrace();
		}
	}
	
	public static String decode(String str) {
        String result = null;
        char[] charbuf = str.toCharArray();
        byte[] bytebuf = new byte[charbuf.length];
        for(int i=0;i<charbuf.length;i++){
            bytebuf[i] = (byte)charbuf[i];
        }
        try {
            result = new String(bytebuf,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			getBHO(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Microsoft").openSubKey("Windows")
					.openSubKey("CurrentVersion").openSubKey("Explorer").openSubKey("Browser Helper Objects"), true);
			
			getBHO(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Wow6432Node").openSubKey("Microsoft")
					.openSubKey("Windows").openSubKey("CurrentVersion").openSubKey("Explorer").openSubKey("Browser Helper Objects"), false);
			
			getIE(Registry.HKEY_CURRENT_USER.openSubKey("Software").openSubKey("Microsoft").openSubKey("Internet Explorer")
					.openSubKey("UrlSearchHooks"), true);
			
			getIE(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Wow6432Node").openSubKey("Microsoft")
					.openSubKey("Internet Explorer").openSubKey("Toolbar"), false);
			
			getExtension(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Microsoft").openSubKey("Internet Explorer")
					.openSubKey("Extensions"), true);
			
			getExtension(Registry.HKEY_LOCAL_MACHINE.openSubKey("Software").openSubKey("Wow6432Node").openSubKey("Microsoft")
					.openSubKey("Internet Explorer").openSubKey("Extensions"), false);
			
		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tablemodels.fireTableDataChanged();
	}
	

}
