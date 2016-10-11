package com.example.user.ling;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.StringBuilderPrinter;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.ling.orm2.Configure;
import com.example.user.ling.orm2.MDictionary;
import com.example.user.ling.tranlate.Language;
import com.example.user.ling.tranlate.Translate;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {


    private static final int DEFINITION = 333434 ;
    private LinearLayout searsh_text;
    private  boolean isText;
    public static List<MDictionary> mDictionaryList;
    private LinearLayout panelText;
    private TextView textCore;
    private View parentView;
    private MyArrayAdapterWord adapter=null;
    private RelativeLayout relativeLayout;
    private LinearLayout panelButton;
    private ListView listView;
    private EditText editText;
   private  static String[] words;
    private android.view.ActionMode mActionMode;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        editText= (EditText) findViewById(R.id.find_word);
        listView= (ListView) findViewById(R.id.list_view);
        relativeLayout= (RelativeLayout) findViewById(R.id.relative_text);
        panelButton= (LinearLayout) findViewById(R.id.panel_buton);
        panelText= (LinearLayout) findViewById(R.id.text_panel);
        textCore= (TextView) findViewById(R.id.text_core);
        textCore.setCustomSelectionActionModeCallback(new android.view.ActionMode.Callback() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Remove the "select all" option
                menu.removeItem(android.R.id.selectAll);
                // Remove the "cut" option
                menu.removeItem(android.R.id.cut);
                // Remove the "copy all" option
                menu.removeItem(android.R.id.copy);
                return true;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Called when action mode is first created. The menu supplied
                // will be used to generate action buttons for the action mode

                // Here is an example MenuItem
                menu.add(0, DEFINITION, 0, "Definition").setIcon(R.drawable.ic_translator);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Called when an action mode is about to be exited and
                // destroyed
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case DEFINITION:
                        int min = 0;
                        int max = textCore.getText().length();
                        if (textCore.isFocused()) {
                            final int selStart = textCore.getSelectionStart();
                            final int selEnd = textCore.getSelectionEnd();

                            min = Math.max(0, Math.min(selStart, selEnd));
                            max = Math.max(0, Math.max(selStart, selEnd));
                        }
                        // Perform your definition lookup with the selected text
                        final String selectedText = textCore.getText().subSequence(min, max).toString();
                        tranlateCore(selectedText);
                        // Finish and close the ActionMode
                        mode.finish();
                        return true;
                    default:
                        break;
                }
                return false;
            }

        });

        textCore.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MainActivity.this.startActionMode(new android.view.ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
                        menu.add(0, DEFINITION, 0, "Definition").setIcon(R.drawable.ic_translator);
                        mActionMode=actionMode;
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
                        return true;
                    }


                    @Override
                    public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case DEFINITION:
                                int min = 0;
                                int max =  textCore.getText().length();
                                if (textCore.isFocused()) {
                                    final int selStart = textCore.getSelectionStart();
                                    final int selEnd = textCore.getSelectionEnd();
                                    min = Math.max(0, Math.min(selStart, selEnd));
                                    max = Math.max(0, Math.max(selStart, selEnd));
                                }
                                // Perform your definition lookup with the selected text
                                final String selectedText = textCore.getText().subSequence(min, max).toString();
                                tranlateCore(selectedText);


                                // Finish and close the ActionMode
                                actionMode.finish();
                                return true;
                            default:
                                break;
                        }
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(android.view.ActionMode actionMode) {
                       mActionMode=null;
                    }
                });
                return false;
            }
        });

        textCore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mActionMode!=null){
                    mActionMode.finish();
                    mActionMode=null;
                }
            }
        });

        searsh_text= (LinearLayout)  findViewById(R.id.searsh_text);

        parentView=findViewById(R.id.activity_main);

        findViewById(R.id.image_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] array = new String[Utils.getSelectWordses().size()];
                for(int i=0;i<Utils.getSelectWordses().size();i++){
                    array[i]=Utils.getSelectWordses().get(i).text;
                }
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setStrings(array);
                selectText.notShowMenu();
                selectText.show(getSupportFragmentManager(),"skdsjf");

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
                                editText.setText("");
                                if(isText){

                                    searsh_text.setVisibility(View.VISIBLE);
                                }else{
                                    searsh_text.setVisibility(View.GONE);
                                }

                            }
                        });
            }
        });

        findViewById(R.id.button_text_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String string = editText.getText().toString();
                if(string.trim().length()==0) return;
                List<String> list= new ArrayList<>();
                for (String word : words) {
                    if(word.contains(editText.getText())){
                        list.add(word);
                    }
                }
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setStrings(list.toArray(new String[list.size()]));
                selectText.show(getSupportFragmentManager(),"skdsjf");


            }
        });

        mDictionaryList=Configure.getSession().getList(MDictionary.class," 1=1 ORDER BY keyWord ASC  ");
        words = new String[mDictionaryList.size()];
        for (int i = 0; i < mDictionaryList.size(); i++) {
            words[i]=mDictionaryList.get(i).valueWord;
        }

        findViewById(R.id.bt_random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogWordRandom dialogWord=new DialogWordRandom();
                String dd=words[new Random().nextInt(words.length)-1];
                if(!Utils.containsWord(dd)){
                    dialogWord.setWord(dd);
                }

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

                    if(!isText){
                        listActivate(array);
                    }
                }else{
                    if(!isText){
                        listActivate(words);
                    }
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

        findViewById(R.id.image_delete_select_word).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.messageBox(getString(R.string.warning), getString(R.string.error1), MainActivity.this, new IAction() {
                    @Override
                    public void action(Object o) {
                        for (SelectWords selectWords : Utils.getSelectWordses()) {
                            Utils.removeSelect(selectWords,null);
                            Toast.makeText(MainActivity.this, R.string.all_removed, Toast.LENGTH_SHORT).show();

                        }
                        if(adapter==null&& !isText){
                            activateText(null,false);
                            adapter=null;
                            String[] array = new String[Utils.getSelectWordses().size()];
                            for(int i=0;i<Utils.getSelectWordses().size();i++){
                                array[i]=Utils.getSelectWordses().get(i).text;
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, array);
                            listView.setAdapter(adapter);
                        }
                    }
                });
            }
        });
    }

    private void tranlateCore(String selectedText) {
        if(selectedText.trim().length()>0){

            List<String> strings= new ArrayList<>();
            for (MDictionary mDictionary : mDictionaryList) {
                if(mDictionary.valueWord.contains(selectedText)){
                    strings.add(mDictionary.valueWord);
                }
            }
            if(strings.size()>0){

            }else{
                String translatedText="";
                try {
                    Translate.setKey("trnsl.1.1.20161006T095643Z.9887c471401acf62.edb45ac820c51c1dc67ee16076cb0390c4806133");
                    translatedText = Translate.execute(selectedText, Language.ENGLISH, Language.RUSSIAN,MainActivity.this);
                    } catch (Exception e) {
                       e.printStackTrace();
                }
                try{
                    Gson sd3 = new Gson();
                    TempSender res = sd3.fromJson(translatedText, TempSender.class);
                    StringBuilder stringBuilder = new StringBuilder(selectedText+" - ");
                    for (String string : res.text) {
                        stringBuilder.append(string).append("\n");
                    }
                    strings.add(stringBuilder.toString());
                }catch (Exception ex){
                    strings.add(translatedText);
                }

            }
            DialogSearshWord selectText=new DialogSearshWord();
            selectText.setStrings(strings.toArray(new String[strings.size()]));
            selectText.show(getSupportFragmentManager(),"skdsjf");

        }
    }

    void activateText(String text,boolean isShow){
        isText=isShow;
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
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;

        if(adapter!=null){
            final String string = adapter.getItem(position);
            menu.add(R.string.add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                   if(Utils.addSelectWord(string,MainActivity.this)){
                      for(int i = 0;i<listView.getChildCount();i++){
                        String string1 = (String) listView.getChildAt(i).getTag();
                          if(string1.equals(string)){
                              listView.getChildAt(i).findViewById(R.id.red_star).setVisibility(View.VISIBLE);
                          }
                      }
                   }
                    return false;
                }
            });
            assert string != null;
            final List<SelectWords> list = Configure.getSession().getList(SelectWords.class," hash = "+ String.valueOf(string.hashCode()));
            if(list.size()==1){
                menu.add(R.string.remove).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Utils.removeSelect(list.get(0),MainActivity.this);

                        for(int i = 0;i<listView.getChildCount();i++){
                            String string1 = (String) listView.getChildAt(i).getTag();
                            if(string1.equals(string)){
                                listView.getChildAt(i).findViewById(R.id.red_star).setVisibility(View.INVISIBLE);
                            }
                        }
                        return false;
                    }
                });
            }
        }else{
            if(isText) return;
            String string=((TextView)aMenuInfo.targetView).getText().toString();
            final List<SelectWords> list = Configure.getSession().getList(SelectWords.class," hash = "+ String.valueOf(string.hashCode()));
            menu.add(R.string.remove).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    for (SelectWords selectWords : list) {
                        Utils.removeSelect(selectWords,MainActivity.this);
                    }
                    ////////////////////////
                    activateText(null,false);
                    adapter=null;
                    String[] array = new String[Utils.getSelectWordses().size()];
                    for(int i=0;i<Utils.getSelectWordses().size();i++){
                        array[i]=Utils.getSelectWordses().get(i).text;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, array);
                    listView.setAdapter(adapter);
                    //////////////////////////
                    return false;
                }
            });
        }
    }
}


