package read_test;

import javax.swing.table.DefaultTableModel;

public class getWinsock extends ReadRegistry implements Runnable {
	DefaultTableModel tablemodels;
	
	public getWinsock(DefaultTableModel tablemodels){
		this.tablemodels=tablemodels;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
