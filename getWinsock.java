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
			String pn=rk.openSubKey(key).getStringValue("ProtocolName");
			if(pn.contains(","))
				row.add(findName(pn));
			else
				row.add(pn);
			String path=getCanonicalPath(rk.openSubKey(key).getStringValue("PackedCatalogItem"));
			String[] infos=getInfo(path);
			row.add(infos[0]);
			row.add(infos[1]);
			row.add(infos[2]);
			tablemodels.addRow(row);
		}
	}
	
	public String findName(String pn){
		String[] words=pn.split(",");
		if(words[0].contains("wshtcpip.dll")){
			switch(Integer.parseInt(words[1])){
				case -60100:
					return "MSAFD Tcpip [TCP/IP]";
				case -60101: 
					return "MSAFD Tcpip [UDP/IP]";
				case -60102: 
					return "MSAFD Tcpip [RAW/IP]";
				case -60103: 
					return "Tcpip";
				default:
					return pn;	
			}
		}
		
		else if(words[0].contains("wshqos.dll")){
			switch(Integer.parseInt(words[1])){
				case -100: return "RSVP TCPv6 Service Provider";
				case -101: return "RSVP TCP Service Provider";
				case -102: return "RSVP UDPv6 Service Provider";
				case -103: return "RSVP UDP Service Provider";
				default:   return pn;	
			}
		}
		
		else if(words[0].contains("wship6.dll")){
			switch(Integer.parseInt(words[1])){
				case -60100:
					return "MSAFD Tcpip [TCP/IPv6]";
				case -60101: 
					return "MSAFD Tcpip [UDP/IPv6]";
				case -60102: 
					return "MSAFD Tcpip [RAW/IPv6]";
				default:
					return pn;	
			}
		}

		else if(words[0].contains("mswsock.dll")){
			switch(Integer.parseInt(words[1])){
				case -60100:
					return "MSAFD Tcpip [TCP/IP]";
				case -60101: 
					return "MSAFD Tcpip [UDP/IP]";
				case -60102: 
					return "MSAFD Tcpip [RAW/IP]";
				case -60103: 
					return "Tcpip";
				case -60200:
					return "MSAFD Tcpip [TCP/IPv6]";
				case -60201: 
					return "MSAFD Tcpip [UDP/IPv6]";
				case -60202: 
					return "MSAFD Tcpip [RAW/IPv6]";
				default:
					return pn;	
			}
		}
		
		return pn;
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
