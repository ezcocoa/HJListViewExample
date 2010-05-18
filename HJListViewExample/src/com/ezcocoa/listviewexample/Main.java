package com.ezcocoa.listviewexample;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

public class Main extends ListActivity {
    /** Called when the activity is first created. */
    
	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Order> m_orders = null;
	private OrderAdapter m_adapter;
	private Runnable viewOrders;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        m_orders = new ArrayList<Order>();
        this.m_adapter = new OrderAdapter(this, R.layout.row, m_orders);
        setListAdapter(this.m_adapter);
        getOrders();
        
        viewOrders = new Runnable() {

			@Override
			public void run() {
				getOrders();
			}

        };
        
        Thread thread = new Thread(null, viewOrders, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(Main.this, "Please wait..", "Retreving data..", true);
    }
	
	private Runnable returnRes = new Runnable() {
		@Override
        public void run() {
            if(m_orders != null && m_orders.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_orders.size();i++)
                m_adapter.add(m_orders.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
		
	};
	
	private void getOrders() {
		try {
			m_orders = new ArrayList<Order>();
			Order o1 = new Order();
			o1.setOrderName("Order Name");
			o1.setOrderStatus("Pending");
			Order o2 = new Order();
			o2.setOrderName("Oder Discription");
			o2.setOrderStatus("Completed");
			m_orders.add(o1);
			m_orders.add(o2);
			Thread.sleep(5000);
			Log.i("ARRAY",""+m_orders.size());
			for (int i = 0; i < m_orders.size(); i++) {
				m_adapter.add(m_orders.get(i));
				m_adapter.notifyDataSetChanged();
			}
			
		} catch (Exception e) {
			Log.i("BACKGROUND_PROC",e.getMessage());
		}
		runOnUiThread(returnRes);
	}
}