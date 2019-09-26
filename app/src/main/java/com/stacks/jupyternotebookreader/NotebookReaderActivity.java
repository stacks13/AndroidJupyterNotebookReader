package com.stacks.jupyternotebookreader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.stacks.jupyternotebookreader.adapter.NotebookReaderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class NotebookReaderActivity extends AppCompatActivity {

	RecyclerView notebookView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notebook_reader);

		notebookView = findViewById(R.id.notebookView);
		notebookView.setLayoutManager(new LinearLayoutManager(this));
		JSONObject file = new JSONObject();
		try {
			file = new JSONObject(AssetJSONFile("Test1.ipynb"));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONArray cells = file.optJSONArray("cells");

		JSONArray code_cells = new JSONArray();
		for (int i = 0; i < cells.length(); i++) {
			JSONObject cell = cells.optJSONObject(i);
			if(cell.optString("cell_type").equals("code")){
				code_cells.put(cell);
			}
		}

		notebookView.setAdapter(new NotebookReaderAdapter(code_cells));

	}


	public String AssetJSONFile (String filename) throws IOException {
		AssetManager manager = getAssets();
		InputStream file = manager.open(filename);
		byte[] formArray = new byte[file.available()];
		file.read(formArray);
		file.close();

		return new String(formArray);
	}
}
