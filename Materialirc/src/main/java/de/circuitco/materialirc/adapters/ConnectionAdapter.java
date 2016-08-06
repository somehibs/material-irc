package de.circuitco.materialirc.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.SortedSet;

import de.circuitco.materialirc.R;

/**
 * Created by alex on 06/08/16.
 */
public class ConnectionAdapter extends RecyclerView.Adapter<SidebarItem> {
    public List<String> connections;

    public ConnectionAdapter(List<String> connections) {
        this.connections = connections;
    }

    @Override
    public SidebarItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SidebarItem(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.sidebar_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(SidebarItem holder, int position) {
        holder.title = connections.get(position);
        holder.unread = 0;
        holder.refresh();
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public void addAll(List<String> names) {
        for (String n : names) {
            if ( !connections.contains(n) ) {
                connections.add(n);
            }
        }
    }
}
