package com.soget.soget_client.view.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soget.soget_client.R;
import com.soget.soget_client.common.SogetUtil;
import com.soget.soget_client.model.Comment;
import com.soget.soget_client.model.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by wonmook on 15. 5. 18..
 */
public class ContactAdapter extends BaseAdapter {

    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    private Context mContext;
    private LayoutInflater inflater;

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        this.mContext = context;
        this.contacts = contacts;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ContactWrapper contactWrapper = null;
        if (contactWrapper == null) {
            row = inflater.inflate(R.layout.contact_list_row, null);
            contactWrapper = new ContactWrapper(row);
            row.setTag(contactWrapper);
        } else {
            contactWrapper = (ContactWrapper) row.getTag();

        }
        //Set title
        Contact item = (Contact) getItem(position);
        contactWrapper.getNameTv().setText(item.getName());
        contactWrapper.getPhoneTv().setText(item.getPhone());

        return row;
    }

    private class ContactWrapper {
        private View base;
        private TextView nameTv;
        private TextView phoneTv;

        public ContactWrapper(View base) {
            this.base = base;
        }

        public TextView getNameTv() {
            if (nameTv == null) {
                nameTv = (TextView) base.findViewById(R.id.contact_name);
            }
            return nameTv;
        }

        public TextView getPhoneTv(){
            if(phoneTv==null){
                phoneTv = (TextView) base.findViewById(R.id.contact_phone);
            }
            return phoneTv;
        }

    }
}
