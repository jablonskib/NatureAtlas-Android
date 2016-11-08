package com.example.brandan.natureatlas;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class Drafts extends AppCompatActivity {

    ListView draftListView;
    String[] snaps;
    private ActionMode mActionMode;
    ArrayAdapter<String> draftList;
    SelectableAdapter sa;
    ArrayList<Snapshot> cachedSnapshots = new ArrayList<>();
    ArrayList<String> snapNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);



        draftListView = (ListView)findViewById(R.id.list);
        draftListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                onListItemCheck(position);
                return true;
            }
        });

        String fileName = this.getFilesDir().getPath() + "/snapshots.txt";
        try
        {
            FileInputStream inStream = new FileInputStream(fileName);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);
            int count = objectInStream.readInt();
            Log.d("count",Integer.toString(count));

            for(int c = 0; c < count; c++)
            {
                cachedSnapshots.add((Snapshot)objectInStream.readObject());
                snapNames.add(cachedSnapshots.get(c).species);
            }


            snaps = snapNames.toArray(new String[snapNames.size()]);
            sa = new SelectableAdapter(this,snaps);

            draftListView.setAdapter(sa);

            draftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mActionMode == null) {
                        Globals g = Globals.getInstance();
                        g.SetSnapshotInstance(cachedSnapshots.get(i));
                        Intent newView = new Intent(getApplicationContext(), SightingSubmit.class);
                        newView.putExtra("snapshot", "yes");
                        startActivity(newView);
                    }
                }
            });



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Log.d("cachedSnapSize", Integer.toString(cachedSnapshots.size()));

        for(int i = 0; i < cachedSnapshots.size(); i++)
        {
            Log.d("CachedSnap" , cachedSnapshots.get(i).organism);
        }
    }



    public void SaveDrafts()
    {






        String fileName = this.getFilesDir().getPath() + "/snapshots.txt";









        try
        {


            FileOutputStream outStream = new FileOutputStream(this.getFilesDir().getPath() + "/snapshots.txt");
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
            objectOutStream.writeInt(cachedSnapshots.size()); // Save size first
            Log.d("FilePath: ", this.getFilesDir().getPath() + "/snapshots.txt");


            Log.d("snapSize", Integer.toString(cachedSnapshots.size()));





            for(Snapshot s:cachedSnapshots)
                objectOutStream.writeObject(s);
            objectOutStream.close();



        }
        catch(FileNotFoundException f)
        {
            f.printStackTrace();
        }
        catch(StreamCorruptedException sc)
        {
            sc.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        getMenuInflater().inflate(R.menu.draftchoices, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        return true;
    }



    // The code from here on was found at
    // https://github.com/deniskratinov/selectablelistview/blob/master/src/com/androidperspective/examples/selectablelistview/ListViewActivity.java



    private void onListItemCheck(int position) {

        sa.toggleSelection(position);
        boolean hasCheckedItems = sa.getSelectedCount() > 0;

        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = startActionMode(new ActionModeCallback());
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();


        if(mActionMode != null)
            mActionMode.setTitle(String.valueOf(sa.getSelectedCount()) + " selected");
    }

    private class SelectableAdapter extends ArrayAdapter<String>{

        private SparseBooleanArray mSelectedItemsIds;


        public SelectableAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mSelectedItemsIds = new SparseBooleanArray();
        }



        public void toggleSelection(int position)
        {
            selectView(position, !mSelectedItemsIds.get(position));
        }

        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }

        public void selectView(int position, boolean value)
        {
            if(value)
                mSelectedItemsIds.put(position, value);
            else
                mSelectedItemsIds.delete(position);

            notifyDataSetChanged();
        }

        public int getSelectedCount() {
            return mSelectedItemsIds.size();// mSelectedCount;
        }

        public SparseBooleanArray getSelectedIds() {
            return mSelectedItemsIds;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            }
            ((TextView) convertView).setText(getItem(position));
            //change background color if list item is selected
            convertView.setBackgroundColor(mSelectedItemsIds.get(position)? 0x9934B5E4: Color.TRANSPARENT);

            return convertView;
        }

    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // inflate contextual menu
            mode.getMenuInflater().inflate(R.menu.draftchoices, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // retrieve selected items and print them out

            switch (item.getItemId()) {
                case R.id.submit_item:
                    DataSender ds;
                    SharedPreferences prefs = getSharedPreferences("userName", MODE_PRIVATE);
                    for (int i = 0; i <= sa.getSelectedCount(); i++) {
                        if (sa.mSelectedItemsIds.get(i)) {
                            ds = new DataSender();
                            ds.SetAddress("http://natureatlas.org/appServices/newSubmission.php");
                            try {
                                Log.d("SnapData" ,prefs.getString("email", "")+ prefs.getString("usersName", "") + cachedSnapshots.get(i).lat+
                                        cachedSnapshots.get(i).lon+  cachedSnapshots.get(i).wild +cachedSnapshots.get(i).nation +
                                        cachedSnapshots.get(i).species+ cachedSnapshots.get(i).organism+ cachedSnapshots.get(i).comm+
                                        cachedSnapshots.get(i).abund+ cachedSnapshots.get(i).ac);
                                ds.SendData(prefs.getString("email", ""), prefs.getString("usersName", ""), cachedSnapshots.get(i).lat,
                                        cachedSnapshots.get(i).lon, cachedSnapshots.get(i).wild, cachedSnapshots.get(i).nation,
                                        cachedSnapshots.get(i).species, cachedSnapshots.get(i).organism, cachedSnapshots.get(i).comm,
                                        cachedSnapshots.get(i).abund, cachedSnapshots.get(i).ac, "", "true", "", "", "", "", "", "", "", getApplicationContext());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    Log.d("Selected Count", Integer.toString(sa.getSelectedCount()));
                    for (int i = 0; i <= sa.getSelectedCount(); i++) {
                        if (sa.mSelectedItemsIds.get(i)) {
                            Log.d("get is true", "yes");
                            sa.removeSelection();
                            cachedSnapshots.remove(i);
                            snapNames.remove(i);
                        }
                    }
                    Log.d("snapnames", Integer.toString(snapNames.size()));
                    Log.d("cachedSnapshots", Integer.toString(cachedSnapshots.size()));
                    SaveDrafts();

                    String[] snapsS = snapNames.toArray(new String[snapNames.size()]);
                    sa = new SelectableAdapter(getApplicationContext(), snapsS);
                    draftListView.setAdapter(sa);
                    mActionMode.finish();



                    return true;

                case R.id.delete_item:

                    Log.d("Deleted Selected", "yes");
                    if(sa.getSelectedIds().size() > 0)
                    {
                        Log.d("Selected Count", Integer.toString(sa.getSelectedCount()));
                        for(int i = 0; i <= sa.getSelectedCount(); i++)
                        {
                            if(sa.mSelectedItemsIds.get(i))
                            {
                                Log.d("get is true", "yes");
                                sa.removeSelection();
                                cachedSnapshots.remove(i);
                                snapNames.remove(i);
                            }
                        }
                        Log.d("snapnames", Integer.toString(snapNames.size()));
                        Log.d("cachedSnapshots", Integer.toString(cachedSnapshots.size()));
                        SaveDrafts();

                        String[] snaps = snapNames.toArray(new String[snapNames.size()]);
                        sa = new SelectableAdapter(getApplicationContext(), snaps );
                        draftListView.setAdapter(sa);
                        mActionMode.finish();
                    }
                    return true;

            }
            SparseBooleanArray selected = sa.getSelectedIds();
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < selected.size(); i++){
                if (selected.valueAt(i)) {
                    String selectedItem = sa.getItem(selected.keyAt(i));
                    message.append(selectedItem + "\n");
                }
            }


            // close action mode
            mode.finish();
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // remove selection

            sa.removeSelection();
            mActionMode = null;
        }

    }




}
