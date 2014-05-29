package read_test;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class getDrivers implements Runnable{
	int index=0;
	DefaultTableModel tablemodels;
	
	public getDrivers(DefaultTableModel tablemodels, int index){
		this.index=index;
		this.tablemodels=tablemodels;
	}
	
	public void getRegistry() throws IOException, RegistryException {
		RegistryKey software = Registry.HKEY_LOCAL_MACHINE.openSubKey("SYSTEM");
		RegistryKey controlset =software.openSubKey("CurrentControlSet");
		RegistryKey services =controlset.openSubKey("Services");
		@SuppressWarnings("unchecked")
		Enumeration<String> service=services.keyElements();
		FileWriter fw=new FileWriter("C:\\Users\\zy\\Desktop\\registry.txt");
		getdrivers(services,service,fw);
		fw.close();
	}
	
	public void getdrivers(RegistryKey services, Enumeration<String> service, FileWriter fw){
		while(service.hasMoreElements()){
			RegistryKey current = null;
			try {
				current = services.openSubKey(service.nextElement());
				String path=current.getStringValue("ImagePath");
				if(path.endsWith(".sys")){
					fw.append(current.getName()+"\t\t"+path+"\n");
					Vector<String> row=new Vector<String>();
					row.add(current.getName());
					row.add("N/A");
					row.add("N/A");
					row.add(path);
					tablemodels.addRow(row);
				}				
			} catch (RegistryException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(current.getName());
				continue;
			}		 
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			getRegistry();
		} catch (IOException | RegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tablemodels.fireTableDataChanged();
		
	}

}
