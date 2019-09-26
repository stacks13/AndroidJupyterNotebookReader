package com.stacks.jupyternotebookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.security.spec.ECField;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends AppCompatActivity {

	CountDownTimer timer;
	TextWatcher textWatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EditText ed = findViewById(R.id.codeEditor);
		PrettifyHighlighter highlighter = new PrettifyHighlighter();


		String highlighted = highlighter.highlight("py", getString(R.string.listing_py));


		highlighted = highlighted.replaceAll("\n", "<br>");

		ed.setText(Html.fromHtml(highlighted));

		textWatcher = new TextWatcher() {
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


		ed.addTextChangedListener(textWatcher);


		timer = new CountDownTimer(1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				if(ed.getSelectionEnd()-ed.getSelectionEnd() > 0){
					return;
				}
				try {
					String highlighted = highlighter.highlight("py", ed.getText().toString());
					highlighted = highlighted.replaceAll("\n", "<br>");
					ed.removeTextChangedListener(textWatcher);
					int pt = ed.getSelectionEnd();
					ed.setText(Html.fromHtml(highlighted));
					ed.setSelection(pt);
					ed.addTextChangedListener(textWatcher);
				}catch (Exception e){
					e.printStackTrace();
					ed.removeTextChangedListener(textWatcher);
					ed.addTextChangedListener(textWatcher);
				}
			}
		};


	}


}
