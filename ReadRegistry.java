package read_test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

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

public class ReadRegistry {
	
	public String getCanonicalPath(String path){
		String cpath="";
		int start_index=0;
		
		//把exe后面的参数给过滤掉
		if(path.contains(".exe")){
			int index=path.indexOf(".exe");
			path=path.substring(0, index)+".exe";
		}	
		
		//把路径开头的\给过滤掉
		if(path.charAt(0)=='\\'){
			start_index=1;
		}
		
		
		int aim_index=path.indexOf("\\", start_index);
		String prefix=path.substring(start_index, aim_index).toLowerCase();
		
		//将systemroot和system32转为完整路径，因为有的路径直接就是system32/... 没有%%括起来
		if(prefix.contains("systemroot")||prefix.contains("windir")){
			cpath="C:\\Windows"+path.substring(aim_index, path.length());		
		}
		else if(prefix.contains("system32")){
			cpath="C:\\Windows\\System32"+path.substring(aim_index, path.length());
		}
		else {
			cpath=path;
			//将%%之间的内容取出找系统变量替换
			if(Pattern.matches("%[a-zA-Z0-9\\s()]+%.*", cpath)){
				int first_index=cpath.indexOf("%");
				int end_index=cpath.lastIndexOf("%");
				String systempath=System.getenv(cpath.substring(first_index+1, end_index));
				cpath=systempath+cpath.substring(end_index+1);
			}
		}
		//将其他字符比如" ,之类的过滤掉
		return cpath.replaceAll("[^a-zA-Z0-9()\\\\:_\\s.-]", "");
	}
	
	public String[] getInfo(String path){
		String[] infos=new String[3];
		try{
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
	        infos[2]=path;
	        if(infos[0]==null)
	        	infos[0]="N/A";
	        if(infos[1]==null)
	        	infos[1]="N/A";
	        return infos;
		}
		catch(FileNotFoundException e){
			//e.printStackTrace();
			return new String[]{"File Not found","File Not found","File Not found: "+path};
		}
		catch(IOException | IndexOutOfBoundsException ee){
			//ee.printStackTrace();
			return new String[]{"N/A","N/A",path};
		}
	}
	
}
