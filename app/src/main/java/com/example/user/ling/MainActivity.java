package com.example.user.ling;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ling.orm2.Configure;
import com.example.user.ling.orm2.ISession;
import com.example.user.ling.tranlate.Language;
import com.example.user.ling.tranlate.Translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    private LinearLayout panelText;
    private TextView textCore;
    private View parentView;

    private MyArrayAdapterWord adapter=null;
    private RelativeLayout relativeLayout;
    private LinearLayout panelButton;
    private ListView listView;
    private EditText editText;
   private  static String[] words;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String translatedText="";
//        try {
//            Translate.setKey("trnsl.1.1.20161006T095643Z.9887c471401acf62.edb45ac820c51c1dc67ee16076cb0390c4806133");
//            translatedText = Translate.execute("is", Language.ENGLISH, Language.RUSSIAN);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        setContentView(R.layout.activity_main);
        ImageButton random= (ImageButton) findViewById(R.id.bt_random);

        editText= (EditText) findViewById(R.id.find_word);
        listView= (ListView) findViewById(R.id.list_view);
        relativeLayout= (RelativeLayout) findViewById(R.id.relative_text);
        panelButton= (LinearLayout) findViewById(R.id.panel_buton);
        panelText= (LinearLayout) findViewById(R.id.text_panel);
        textCore= (TextView) findViewById(R.id.text_core);


        parentView=findViewById(R.id.activity_main);

        findViewById(R.id.image_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DialogTranslate dialogTranslate=new DialogTranslate();
//                dialogTranslate.show(getSupportFragmentManager(),"dsd");
                activateText(null,false);
                adapter=null;
                String[] array = new String[Utils.getSelectWordses().size()];
                for(int i=0;i<Utils.getSelectWordses().size();i++){
                    array[i]=Utils.getSelectWordses().get(i).text;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, array);
                listView.setAdapter(adapter);

            }
        });




        findViewById(R.id.image_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.VISIBLE);
                panelButton.setVisibility(View.GONE);
                Handler mHandler = new Handler();
                mHandler.post(
                        new Runnable() {
                            public void run() {
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.toggleSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                editText.requestFocus();
                            }
                        });
            }
        });

        AssetManager am = getAssets();
        InputStream is = null;
        try {
            is = am.open("word.txt");
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        BufferedReader r = null;
        if (is != null) {
            r = new BufferedReader(new InputStreamReader(is));
        }
        StringBuilder total = new StringBuilder();
        String line;
        try {
            if (r != null) {
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        words = total.toString().split("\n");
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogWord dialogWord=new DialogWord();
                dialogWord.setWord(words[new Random().nextInt(words.length)-1]);
                dialogWord.show(getSupportFragmentManager(),"sdsd");
            }
        });

        findViewById(R.id.bt_show_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateText(null,false);
                listActivate(words);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>0){
                    List<String> strings=new ArrayList<>();
                    for (String word : words) {
                        if(word.contains(charSequence)){
                            strings.add(word);
                        }
                    }
                    String[] array = strings.toArray(new String[strings.size()]);
                    listActivate(array);
                }else{
                    listActivate(words);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        listView.setOnCreateContextMenuListener(this);
        listActivate(words);
        setListenerToRootView(new IKeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                if(!visible){
                    panelButton.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.image_text_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file=new File(Application.path2);
                if(!file.exists()) return;
                File[] files=file.listFiles();
                DialogSelectText dialog=new DialogSelectText();
                dialog.setData(new DialogSelectText.ISelectText() {
                    @Override
                    public void activate(File file) {

                        activateText(Utils.readFile(file.getPath()),true);

                    }
                },files);
                dialog.show(getSupportFragmentManager(),"dsdd");
            }
        });

    }

    void activateText(String text,boolean isShow){
        if(isShow){
            textCore.setText(text);
            panelText.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else {
            panelText.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    void listActivate(String[] words){
        adapter = new MyArrayAdapterWord(this,
                R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(words)));
        listView.setAdapter(adapter);
    }


    public void setListenerToRootView(final IKeyboardVisibilityListener listener) {


        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {

                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    return;
                }

                alreadyOpen = isShown;
                listener.onVisibilityChanged(isShown);
            }
        });
    }

    interface IKeyboardVisibilityListener {
        void onVisibilityChanged(boolean visible);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(adapter!=null){
            AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            final int position = aMenuInfo.position;
            final String string = adapter.getItem(position);
            menu.add("Добавить в избранное?").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                   if(Utils.addSelectWord(string)){
                      listView.getChildAt(position).findViewById(R.id.red_star).setVisibility(View.VISIBLE);
                   }

                    return false;
                }
            });
            final List<SelectWords> list = Configure.getSession().getList(SelectWords.class," hash = "+ String.valueOf(string.hashCode()));
            if(list.size()==1){
                menu.add("Удалить из избранных").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Utils.removeSelect(list.get(0));

                        listView.getChildAt(position).findViewById(R.id.red_star).setVisibility(View.INVISIBLE);
                        return false;
                    }
                });
            }
        }

    }
}

