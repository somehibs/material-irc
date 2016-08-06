package de.circuitco.materialirc.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.circuitco.materialirc.R;

/**
 * Created by alex on 06/08/16.
 */
public class SidebarItem extends RecyclerView.ViewHolder {
    private final TextView titleView;
//    private final TextView unreadView;
    public String title;
    public int unread;

    public SidebarItem(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.title);
//        unreadView = itemView.findViewById(R.id.unread);
    }

    public void refresh() {
        if ( titleView != null ) {
            titleView.setText(title);
        }
        if ( unread != -1 ) {
//            unreadView.setText(String.valueOf(unread));
        }
    }
}
