package read_test;

import java.io.IOException;

import org.boris.pecoff4j.PE;
import org.boris.pecoff4j.ResourceDirectory;
import org.boris.pecoff4j.ResourceEntry;
import org.boris.pecoff4j.constant.ResourceType;
import org.boris.pecoff4j.io.PEParser;
import org.boris.pecoff4j.io.ResourceParser;
import org.boris.pecoff4j.resources.StringFileInfo;
import org.boris.pecoff4j.resources.StringTable;
import org.boris.pecoff4j.resources.VersionInfo;
import org.boris.pecoff4j.util.ResourceHelper;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public abstract class ReadRegistry {
	public abstract RegistryKey getRegistryKey() throws NoSuchKeyException, RegistryException;
	
	public String getCanonicalPath(String path){
		String cpath="";
		int start_index=0;
		
		if(path.contains(".exe")){
			int index=path.indexOf(".exe");
			path=path.substring(0, index)+".exe";
		}
		
		if(path.charAt(0)=='\\'){
			start_index=1;
		}
		
		int aim_index=path.indexOf("\\", start_index);
		String prefix=path.substring(start_index, aim_index).toLowerCase();
		if(prefix.contains("systemroot")||prefix.contains("windir")){
			cpath="C:\\Windows"+path.substring(aim_index, path.length());		
		}
		else if(prefix.contains("system32")){
			cpath="C:\\Windows\\System32"+path.substring(aim_index, path.length());
		}
		else
			cpath=path;
		return cpath.replaceAll("[^a-zA-Z0-9()\\\\:_\\s.-]", "");
	}
	
	public String[] getInfo(String path) throws IOException{
		String[] infos=new String[2];
		PE pe = PEParser.parse(path);
        ResourceDirectory rd = pe.getImageData().getResourceTable();

        ResourceEntry[] entries = ResourceHelper.findResources(rd, ResourceType.VERSION_INFO);
        byte[] data = entries[0].getData();
        VersionInfo version = ResourceParser.readVersionInfo(data);

        StringFileInfo strings = version.getStringFileInfo();
        StringTable table = strings.getTable(0);
        for (int j = 0; j < table.getCount(); j++) {
            String key = table.getString(j).getKey();
            if(key.equals("CompanyName"))
            	infos[1]=table.getString(j).getValue();
            else if(key.equals("FileDescription"))
            	infos[0]=table.getString(j).getValue();
        }
        return infos;
	}
	
}
