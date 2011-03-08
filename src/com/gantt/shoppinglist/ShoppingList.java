package com.gantt.shoppinglist;

import android.app.Dialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ShoppingList extends ListActivity {
    SimpleCursorAdapter adapter = null;
	/** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   
    	
        final DataHelper dataHelper = new DataHelper(this);
        ListView lv = (ListView) findViewById(android.R.id.list);
        final SQLiteDatabase db = dataHelper.selectAll();
        final Cursor c = db.rawQuery("SELECT DISTINCT oid as _id,name FROM table1 ORDER BY name", null); // Asks the database to select items from the table, ordered by name
        if (c.moveToFirst()) {
        	String[] columnNames = new String[]{"name"}; // Defines where to get the items
        	int[] ctv = new int[]{R.id.checkedtext}; // Defines where to put the items (our CheckedTextView)
        	adapter = new SimpleCursorAdapter(this, R.layout.rowlayout, c, columnNames, ctv); // Creates a SimpleCursorAdapter, which takes the data from the SQLite Database and puts it in the ListView
        }
        lv.setAdapter(adapter); // Sets the adapter for the ListView
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // Tells the ListView how to be able to choose multiple items
        
        Button button1main = (Button) findViewById(android.R.id.button1);
        button1main.setOnClickListener(new OnClickListener()  { //Sets an OnClickListener to handle a button click
        	@Override
        	public void onClick(View v)  {
        		createDialog();
        	}
        	private void createDialog() { // Defines how to create our Dialog	
        	final Dialog additem = new Dialog(ShoppingList.this);
        	additem.setContentView(R.layout.maindialog);
        	final EditText et = (EditText)additem.findViewById(android.R.id.edit);
        	additem.setTitle("Type your item");
        	additem.setCancelable(true); // Sets if we can cancel the Dialog or not
        	et.setHint("Type the name of an item...");
       		Button button = (Button) additem.findViewById(android.R.id.button3);
       		button.setOnClickListener(new OnClickListener()  {
       			@Override
       			public void onClick(View v)  {
       				additem.dismiss();
       			}
       		});
       		additem.show();
       		
       		Button ok = (Button) additem.findViewById(android.R.id.button2);
        		ok.setOnClickListener(new OnClickListener() {
        			@Override
        			public void onClick(View v) {
        				addItems();
        			}
        			private void addItems() { // Adds our items to the SQLiteDatabase and the ListView
        				String item = et.getText().toString(); // Takes the text from our EditText
        				dataHelper.insert(item); // And inserts it into the SQLiteDatabase
        				c.requery(); // Refreshes the ListView
        				additem.dismiss();
        				et.setText(""); // Resets the EditText
        			}
        		});    
        	}
        });
    }
}