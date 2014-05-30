package read_test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class getServices extends ReadRegistry implements Runnable{
	int index=0;
	DefaultTableModel tablemodels;
	
	public getServices(DefaultTableModel tablemodels, int index){
		this.index=index;
		this.tablemodels=tablemodels;
	}
	
	@Override
	public RegistryKey getRegistryKey() throws NoSuchKeyException, RegistryException {
		// TODO Auto-generated method stub
		RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey("SYSTEM");
		RegistryKey controlset =software.openSubKey("CurrentControlSet");
		RegistryKey services =controlset.openSubKey("Services");
		return services;
	}
	
	
	@SuppressWarnings("unchecked")
	public void getservices() throws IOException{
		RegistryKey services = null;
		try {
			services = getRegistryKey();
		} catch (RegistryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Enumeration<String> service = null;
		try {
			service = services.keyElements();
		} catch (RegistryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(service.hasMoreElements()){
			RegistryKey current = null;
			try {
				current = services.openSubKey(service.nextElement());
				String path=current.getStringValue("ImagePath");
				if(path.contains("svchost.exe")){
					RegistryKey param=current.openSubKey("Parameters");
					path=param.getStringValue("ServiceDll");
				}
				
				if(!path.endsWith(".sys")){
					Vector<String> row=new Vector<String>();
					row.add(current.getName());
					System.out.println("before "+path+"  Name: "+current.getName());
					path=getCanonicalPath(path);
					System.out.println("after "+path+"  Name: "+current.getName());
					String[] infos=getInfo(path);
					row.add(infos[0]);
					row.add(infos[1]);
					row.add(path);
					tablemodels.addRow(row);
					//System.out.println(path+"\t"+infos[0]);
				}				
			} catch (RegistryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error:  "+current.getName());
				continue;
			}		 
		}
	}

	@Override
	public void run() {
		try {
			getservices();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tablemodels.fireTableDataChanged();
		
	}

	

}
