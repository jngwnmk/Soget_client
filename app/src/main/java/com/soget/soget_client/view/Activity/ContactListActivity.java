package com.soget.soget_client.view.Activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.soget.soget_client.R;
import com.soget.soget_client.common.StaticValues;
import com.soget.soget_client.model.Contact;
import com.soget.soget_client.view.Adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wonmook on 15. 5. 18..
 */
public class ContactListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CONTACTS_LOADER_ID = 1;
    private ImageButton back_btn = null;
    private ListView contactList = null;
    private ContactAdapter contactAdapter = null;
    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    private int currentInviationNum = 0;
    private String invitationCode = "";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contactlist_layout);

        currentInviationNum = getIntent().getExtras().getInt(StaticValues.INVITATIONNUM);
        invitationCode = getIntent().getExtras().getString(StaticValues.INVITATIONCODE);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading....");
        pDialog.show();
        setLayout();

    }

    private void setLayout(){
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(CONTACTS_LOADER_ID,
                null,
                this);

        contactList = (ListView)findViewById(R.id.contact_list);
        contactAdapter = new ContactAdapter(this, contacts);
        contactList.setAdapter(contactAdapter);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), InvitatonSendActivity.class);
                Bundle extra = new Bundle();
                extra.putInt(StaticValues.INVITATIONNUM,currentInviationNum);
                extra.putString(StaticValues.INVITATIONCODE,invitationCode);
                extra.putString(StaticValues.PHONENUM,contacts.get(position).getPhone());
                intent.putExtras(extra);
                startActivity(intent);
                finish();

            }
        });
        contactAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == CONTACTS_LOADER_ID) {
            return contactsLoader();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
       contacts.clear();
       contacts.addAll(contactsFromCursor(cursor));
       contactAdapter.notifyDataSetChanged();
       pDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private  Loader<Cursor> contactsLoader() {
        Uri contactsUri = ContactsContract.Contacts.CONTENT_URI; // The content URI of the phone contacts

        String[] projection = {                                  // The columns to return for each row
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.HAS_PHONE_NUMBER

        } ;

        String selection = null;                                 //Selection criteria
        String[] selectionArgs = {};                             //Selection criteria
        String sortOrder = null;                                 //The sort order for the returned rows

        return new CursorLoader(
                getApplicationContext(),
                contactsUri,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }

    private ArrayList<Contact> contactsFromCursor(Cursor cursor) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Contact contact = new Contact();
                contact.setName(name);

                if (Integer.parseInt(cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.setPhone(phoneNo);
                        contacts.add(contact);
                    }
                    pCur.close();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return contacts;
    }
}
