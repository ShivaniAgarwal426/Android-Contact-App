package com.example.contactandroidapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.contactandroidapp.CallMessageActivity;
import com.example.contactandroidapp.ContactTable.Model.ContactEntity;
import com.example.contactandroidapp.MainActivity;
import com.example.contactandroidapp.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context context;
    private List<ContactEntity>  contactList;
    LayoutInflater inflater;

    public ContactAdapter(MainActivity context, List<ContactEntity> allContactList) {
        this.context = context;
        this.contactList = allContactList;
        this.inflater = LayoutInflater.from(context);           // dont know why
    }

    // below method is use for filtering data in our array list
    public void filterList(ArrayList<ContactEntity> filterllist) {
        // on below line we are passing filtered
        // array list in our original array list
        contactList = filterllist;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        // get the data from model entity table by position
        final ContactEntity currentContact = contactList.get(position);

//        for initials
        if (!currentContact.getFirstname().isEmpty() && !currentContact.getLastname().isEmpty()){
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getRandomColor();
            // below text drawable is a circular.
            TextDrawable drawable = TextDrawable.builder().beginConfig()
                    .width(100)  // width in px
                    .height(100) // height in px
                    .endConfig()
// as we are building a circular drawable we are calling a build round method. in that method we are passing our text and color.
/**               .buildRound(currentContact.getFirstname().substring(0, 1).toUpperCase(), color);   // for round
                  .buildRect(currentContact.getFirstname().substring(0, 1).toUpperCase(), color);       // for rect
 **/
                    .buildRoundRect(currentContact.getFirstname().substring(0, 1).toUpperCase(), color,10);  // for round rect
            // setting image to our image view on below line.
            holder.initial.setImageDrawable(drawable);
        }
        holder.contact_name.setText(currentContact.getFirstname()+" "+currentContact.getLastname());
        holder.contact_number.setText(currentContact.getPhone());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CallMessageActivity.class).putExtra("id", currentContact.getId());
                context.startActivity(intent);
                /** tab and color change of layout **/
//                holder.layout.
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView initial;
        TextView contact_name, contact_number;
        RelativeLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initial = itemView.findViewById(R.id.initial);
            contact_name = itemView.findViewById(R.id.Contact_Name);
            contact_number = itemView.findViewById(R.id.Contact_Number);
            layout = itemView.findViewById(R.id.each_contact_layout);
        }
    }
/** seach filter **/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // in this on create options menu we are calling
//        // a menu inflater and inflating our menu file.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu);
//        // on below line we are getting our menu item as search view item
//        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
//        // on below line we are creating a variable for our search view.
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
//        // on below line we are setting on query text listener for our search view.
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // on query submit we are clearing the focus for our search view.
//                searchView.clearFocus();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // on changing the text in our search view we are calling
//                // a filter method to filter our array list.
//                filter(newText.toLowerCase());
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    private void filter(String text) {
//        // in this method we are filtering our array list.
//        // on below line we are creating a new filtered array list.
//        ArrayList<ContactsModal> filteredlist = new ArrayList<>();
//        // on below line we are running a loop for checking if the item is present in array list.
//        for (ContactsModal item : contactsModalArrayList) {
//            if (item.getUserName().toLowerCase().contains(text.toLowerCase())) {
//                // on below line we are adding item to our filtered array list.
//                filteredlist.add(item);
//            }
//        }
//        // on below line we are checking if the filtered list is empty or not.
//        if (filteredlist.isEmpty()) {
//            Toast.makeText(this, "No Contact Found", Toast.LENGTH_SHORT).show();
//        } else {
//            // passing this filtered list to our adapter with filter list method.
//            contactRVAdapter.filterList(filteredlist);
//        }
//    }
    /**
//    firebase is services gives/ provide sdk for accessing remote database -->> can fetch data,crud by using functions of firebase
//    api call n interact with database
//    remote databse : how to interact -- by api(data acess class)
     **/
}
