package com.stacks.jupyternotebookreader.adapter;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stacks.jupyternotebookreader.PrettifyHighlighter;
import com.stacks.jupyternotebookreader.R;

import org.json.JSONArray;

public class NotebookReaderAdapter extends RecyclerView.Adapter<NotebookReaderAdapter.ViewHolder> {

	private JSONArray dataset;
	PrettifyHighlighter highlighter = new PrettifyHighlighter();

	public NotebookReaderAdapter(JSONArray dataset) {
		this.dataset = dataset;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.code_cell, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		JSONArray source_lines = dataset.optJSONObject(position).optJSONArray("source");
		String source = "";
		for (int i = 0; i < source_lines.length(); i++) {
			source += source_lines.optString(i, "");
		}
		String highlighted = highlighter.highlight("py", source);
		highlighted = highlighted.replaceAll("\n", "<br>");
		holder.codeView.setText(Html.fromHtml(highlighted));


		JSONArray output_lines = dataset.optJSONObject(position).optJSONArray("outputs");

		if(output_lines.length() != 0){
			String output = "";
			for (int i = 0; i < output_lines.length(); i++) {
				output += output_lines.optString(i);
			}
			holder.output.setText(output);
		}else{
			holder.output_layout.setVisibility(View.GONE);
		}

	}

	@Override
	public int getItemCount() {
		return dataset.length();
	}

	public class ViewHolder extends RecyclerView.ViewHolder{

		TextView codeView;
		View output_layout;
		TextView output;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			codeView = itemView.findViewById(R.id.code);
			output = itemView.findViewById(R.id.output);
			output_layout = itemView.findViewById(R.id.output_layout);
		}
	}

}
