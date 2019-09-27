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

		EditText codeView;
		View output_layout;
		TextView output;
		TextWatcher textWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if(timer != null){
					timer.cancel();
					timer.start();
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};


		CountDownTimer timer = new CountDownTimer(1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				if(codeView.getSelectionEnd()-codeView.getSelectionEnd() > 0){
					return;
				}
				try {
					String highlighted = highlighter.highlight("py", codeView.getText().toString());
					highlighted = highlighted.replaceAll("\n", "<br>");
					codeView.removeTextChangedListener(textWatcher);
					int pt = codeView.getSelectionEnd();
					codeView.setText(Html.fromHtml(highlighted));
					codeView.setSelection(pt);
					codeView.addTextChangedListener(textWatcher);
				}catch (Exception e){
					e.printStackTrace();
					codeView.removeTextChangedListener(textWatcher);
					codeView.addTextChangedListener(textWatcher);
				}
			}
		};

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			codeView = itemView.findViewById(R.id.code);
			output = itemView.findViewById(R.id.output);
			output_layout = itemView.findViewById(R.id.output_layout);

			codeView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean b) {
					if(view.isFocused()){
						codeView.addTextChangedListener(textWatcher);
					}else{
						codeView.removeTextChangedListener(textWatcher);
					}
				}
			});
		}
	}

}
