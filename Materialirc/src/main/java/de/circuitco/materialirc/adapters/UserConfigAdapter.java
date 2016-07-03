package de.circuitco.materialirc.adapters;

import android.support.design.widget.TextInputLayout;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.circuitco.materialirc.R;
import de.circuitco.materialirc.config.UserConfig;

/**
 * Created by alex on 03/07/16.
 */
public class UserConfigAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public UserConfig config;

    private List<Pair<String, String>> configValues;
    // map of db names to StringConfigItem bindings
    private Map<String, StringConfigItem> configItemMap;

    // list of user config db names and hints
    public UserConfigAdapter(UserConfig configToModify, List<Pair<String, String>> configValues) {
        config = configToModify;
        this.configValues = configValues;
        configItemMap = new HashMap<>();
    }

    public void refreshConfig() {
        // populate the config with our data
        for ( Map.Entry<String, StringConfigItem> item : configItemMap.entrySet() ) {
            config.putString(item.getKey(), item.getValue().text.getEditText().getText().toString());
        }
    }

    public static class StringConfigItem extends RecyclerView.ViewHolder {
        public TextInputLayout text = null;
        public String configItemKey = null;
        public StringConfigItem(View itemView) {
            super(itemView);
            text = (TextInputLayout)itemView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        RecyclerView.ViewHolder holder = null;
        switch ( viewType ) {
            case 0:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_string_config, parent, false);
                holder = new StringConfigItem(v);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return configValues.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder abstractHolder, int position) {
        StringConfigItem holder = (StringConfigItem)abstractHolder;
        Pair<String,String> entry = configValues.get(position);
        configItemMap.put(entry.first, holder);
        // Bind this view.
        holder.configItemKey = entry.first;
        holder.text.setHint(entry.second);
        if ( config != null ) {
            String value = config.getString(entry.first);
            if (holder.text.getEditText() != null &&
                    holder.text.getEditText().getText().length() == 0
                    && value != null) {
                holder.text.getEditText().setText(value);
            }
        }
    }
}
