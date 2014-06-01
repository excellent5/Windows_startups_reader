package read_test;

import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

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
		@SuppressWarnings("unchecked")
		Vector<Vector<String>>[] rows=new Vector[6];
		
		JPanel[] jpanels=new JPanel[6];
		final JTable[] jtables=new JTable[6];
		for(int i=0;i<6;i++){
			jpanels[i]=new JPanel();
			jpanels[i].setLayout(new BorderLayout());
			jtables[i]=new JTable(rows[i],columnNames);
			jtables[i].setShowGrid(false);
			TableColumnModel tcm=jtables[i].getColumnModel();
			tcm.getColumn(0).setPreferredWidth(80);
			tcm.getColumn(1).setPreferredWidth(200);
			tcm.getColumn(2).setPreferredWidth(200);
			tcm.getColumn(3).setPreferredWidth(270);
			tablemodels[i]=(DefaultTableModel)jtables[i].getModel();
			jtables[i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		
		for(int i=0;i<6;i++){
			jtp.addTab(functions[i], jpanels[i]);
			jtp.setUI(new MetalTabbedPaneUI(){
				@Override
				protected int calculateTabWidth(int arg0, int arg1,
						FontMetrics arg2) {
					// TODO Auto-generated method stub
					return super.calculateTabWidth(arg0, arg1, arg2)+43;
				}
			});
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
				case 0:
					new Thread(new getLogon(tablemodels[0])).start();
					break;
				case 1:
					new Thread(new getIE_BHO(tablemodels[1])).start();
					break;
				case 2:
					new Thread(new getServices(tablemodels[2])).start();
					break;
				case 3: 
					new Thread(new getDrivers(tablemodels[3])).start();
					break;
				case 4:
					new Thread(new getScheduledTasks(tablemodels[4])).start();
					break;
				case 5:
					new Thread(new getKnownDLLs(tablemodels[5])).start();
					break;
				}				
			}
		});
		
		jf.add(jtp);
		jf.setSize(800, 600);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		new Thread(new getLogon(tablemodels[0])).start();
	}
}
