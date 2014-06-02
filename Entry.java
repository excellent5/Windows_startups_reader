package read_test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

public class Entry {
	
	public static void loadDLL(String file){
		try {
			InputStream in =Entry.class.getResourceAsStream("ICE_JNIRegistry.dll");
			File dll=new File(System.getenv("temp")+"\\ICE_JNIRegistry_tmp.dll");
			FileOutputStream out = new FileOutputStream(dll);
			int i;
		    byte [] buf = new byte[1024];
		    while((i=in.read(buf))!=-1) {
		    	out.write(buf,0,i);
		    }
		    in.close();
		    out.close();
		    dll.deleteOnExit();
		    System.load(dll.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException{
		if(System.getProperty("os.arch").equals("amd64")){
			loadDLL("ICE_JNIRegistry.dll");
			new GUI().init();
		}
		else{
			JOptionPane.showMessageDialog(null, "ϵͳ��32λ�ģ���ʹ�ñ����32λ�汾��"
					, "��֧�ֵ�ǰϵͳ�汾", JOptionPane.ERROR_MESSAGE);
		}
	}
}
