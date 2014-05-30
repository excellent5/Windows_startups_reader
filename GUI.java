package read_test;

import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class GUI {
	public static void main(String[] args) {
		JFrame jf=new JFrame("Autorun");
		final JTabbedPane jtp=new JTabbedPane();
		jtp.setTabPlacement(JTabbedPane.TOP);
		String[] functions={"Logon","Internet Explorer","Services","Drivers","Scheduled Tasks","Known DLLs"};
		Vector<String> columnNames=new Vector<String>();
		columnNames.add("Entry");
		columnNames.add("Description");
		columnNames.add("Publisher");
		columnNames.add("ImagePath");
		
		final DefaultTableModel tablemodels[]=new DefaultTableModel[6];
		Vector<Vector<String>>[] rows=new Vector[6];
		
		JPanel[] jpanels=new JPanel[6];
		final JTable[] jtables=new JTable[6];
		for(int i=0;i<6;i++){
			jpanels[i]=new JPanel();
			jpanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
			jtables[i]=new JTable(rows[i],columnNames);
			tablemodels[i]=(DefaultTableModel)jtables[i].getModel();
			jtables[i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		
		//new Thread(new getDrivers(tablemodels[0],0)).start();
		
		for(int i=0;i<6;i++){
			jtp.addTab(functions[i], jpanels[i]);
		}
		
		for(int i=0;i<6;i++){
			jpanels[i].add(new JScrollPane(jtables[i]));
		}
		
		
		jtp.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				int index=jtp.getSelectedIndex();
				tablemodels[index].getDataVector().clear();
				switch(index){
				case 3: 
					new Thread(new getDrivers(tablemodels[index],index)).start();
					break;
				case 2:
					new Thread(new getServices(tablemodels[index],index)).start();
					break;
				}				
			}
		});
		
		jf.add(jtp);
		jf.setSize(800, 600);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
