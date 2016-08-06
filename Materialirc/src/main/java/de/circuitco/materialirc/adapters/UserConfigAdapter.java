package de.circuitco.materialirc.adapters;

import android.support.design.widget.TextInputLayout;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
    private Map<String, String> userConfigChanges;

    // list of user config db names and hints
    public UserConfigAdapter(UserConfig configToModify, List<Pair<String, String>> configValues) {
        config = configToModify;
        this.configValues = configValues;
        userConfigChanges = new HashMap<>();
    }

    public void refreshConfig() {
        // populate the config with our data
        for ( Map.Entry<String, String> item : userConfigChanges.entrySet() ) {
            config.putString(item.getKey(), item.getValue());
        }
    }

    public static class StringConfigItem extends RecyclerView.ViewHolder {
        public TextInputLayout text = null;
        public SavingTextWatcher textWatcher = null;
        public StringConfigItem(View itemView, SavingTextWatcher textWatcher) {
            super(itemView);
            this.textWatcher = textWatcher;
            text = (TextInputLayout)itemView;
            text.getEditText().addTextChangedListener(textWatcher);
        }

        public void updatePositon(String key, String maybeValue) {
            textWatcher.key = key;
            if ( maybeValue != null ) {
                text.getEditText().setText(maybeValue);
            } else {git stat
                text.getEditText().setText("");
            }
        }
    }

    public class SavingTextWatcher implements TextWatcher {
        public String key = null;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ( key != null ) {
                userConfigChanges.put(key, s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

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
                holder = new StringConfigItem(v, new SavingTextWatcher());
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_header, parent, false);
                holder = new HeaderItem(v);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if ( configValues.get(position).first.equals("HEADER" ) ) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return configValues.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder abstractHolder, int position) {
        if ( getItemViewType(position) == 1 ) {
            HeaderItem header = (HeaderItem)abstractHolder;
            header.header.setText(configValues.get(position).second);
        } else {
            StringConfigItem holder = (StringConfigItem) abstractHolder;
            Pair<String, String> entry = configValues.get(position);
            // Bind this view.
            holder.text.setHint(entry.second);
            if (config != null) {
                String value = config.getString(entry.first);
                if (holder.text.getEditText() != null &&
                        holder.text.getEditText().getText().length() == 0
                        && value != null) {
                    holder.text.getEditText().setText(value);
                }
            }
            holder.updatePositon(entry.first, userConfigChanges.get(entry.first));
        }
    }

    private class HeaderItem extends RecyclerView.ViewHolder {
        public final TextView header;

        public HeaderItem(View v) {
            super(v);
            header = (TextView) v.findViewById(R.id.header);

        }
    }
}
