package ir.mhdr.bmi.lib;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import ir.mhdr.bmi.NewEditProfileActivity;
import ir.mhdr.bmi.R;
import ir.mhdr.bmi.blDao.HistoryBL;
import ir.mhdr.bmi.blDao.PrivateSettingBL;
import ir.mhdr.bmi.blDao.UserBL;
import ir.mhdr.bmi.dao.User;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private Context context;
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        this.notifyDataSetChanged();
    }

    public void addUser(User user) {
        this.userList.add(0, user);
        this.notifyItemInserted(0);
    }

    public ProfileAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.profile_row, parent, false);

        ProfileViewHolder viewHolder = new ProfileViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {

        final User user = userList.get(position);
        holder.textViewProfile.setText(user.getName());

        if (user.isActive()) {
            holder.imageViewActiveProfileStar.setVisibility(View.VISIBLE);
        }
        else {
            holder.imageViewActiveProfileStar.setVisibility(View.INVISIBLE);
        }

        final AppCompatImageButton button = holder.buttonProfileOptions;
        final ProfileViewHolder viewHolder = holder;

        holder.relativeLayoutProfileRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserBL userBL = new UserBL();
                int itemPosition = viewHolder.getAdapterPosition();
                User userToEdit = userList.get(itemPosition);
                userBL.SwitchActiveUser(userToEdit);
                userList = userBL.getUsers();
                notifyDataSetChanged();

                Toast.makeText(context, context.getResources().getString(R.string.change_profile_successful), Toast.LENGTH_LONG).show();
            }
        });

        holder.buttonProfileOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListPopupWindow listPopupWindow = new ListPopupWindow(context);

                String[] menuItems = context.getResources().getStringArray(R.array.profile_menu);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.profile_menu,
                        R.id.textViewProfileMenuItem, menuItems);

                listPopupWindow.setAdapter(adapter);
                listPopupWindow.setAnchorView(button);
                listPopupWindow.setWidth(420);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHorizontalOffset(-380);
                listPopupWindow.setVerticalOffset(-50);

                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final int itemPosition = viewHolder.getAdapterPosition();

                        if (position == 0) {
                            // edit

                            User userToEdit = userList.get(itemPosition);
                            Intent intent = new Intent(context, NewEditProfileActivity.class);
                            intent.putExtra("userId", userToEdit.getId());
                            context.startActivity(intent);
                            listPopupWindow.dismiss();
                        } else if (position == 1) {
                            // delete

                            if (userList.size() == 1) {
                                Toast.makeText(context, context.getResources().getString(R.string.atleast_one_profile_is_needed), Toast.LENGTH_LONG).show();
                                return;
                            }

                            UserBL userBL = new UserBL();
                            HistoryBL historyBL = new HistoryBL();

                            User userToDelete = userList.get(itemPosition);

                            historyBL.deleteAllByUser(userToDelete);
                            userBL.delete(userToDelete);

                            User nextUser = userBL.getUsers().get(0);

                            PrivateSettingBL privateSettingBL=new PrivateSettingBL();
                            privateSettingBL.setActiveUser(nextUser);

                            userBL.update(nextUser);

                            Toast.makeText(context, context.getResources().getString(R.string.user_removed), Toast.LENGTH_LONG).show();

                            userList=userBL.getUsers();
                            notifyDataSetChanged();

                            listPopupWindow.dismiss();
                        }
                    }
                });

                listPopupWindow.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewProfile;
        public AppCompatImageButton buttonProfileOptions;
        public AppCompatImageView imageViewActiveProfileStar;
        public RelativeLayout relativeLayoutProfileRow;

        public ProfileViewHolder(View itemView) {
            super(itemView);

            textViewProfile = (AppCompatTextView) itemView.findViewById(R.id.textViewProfile);
            buttonProfileOptions = (AppCompatImageButton) itemView.findViewById(R.id.buttonProfileOptions);
            imageViewActiveProfileStar = (AppCompatImageView) itemView.findViewById(R.id.imageViewActiveProfileStar);
            relativeLayoutProfileRow = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutProfileRow);
        }
    }

}

