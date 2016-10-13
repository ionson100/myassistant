package com.example.user.ling;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ling.orm2.Configure;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;



public class MainActivity extends AppCompatActivity {


    interface IKeyboardVisibilityListener {
        void onVisibilityChanged(boolean visible);
    }



    private static final int DEFINITION = 333434 ;
    private LinearLayout searsh_text;
    private  boolean isText;
    public static List<MDictionary> mDictionaryList=new ArrayList<>();
    private LinearLayout panelText;
    private TextView textCore;
    private View parentView;
    private MyArrayAdapterWord adapter=null;
    private RelativeLayout relativeLayout;
    private LinearLayout panelButton;
    private ListView listView;
    private EditText editText;
    private LinearLayout panelAbc;


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
        panelAbc= (LinearLayout) findViewById(R.id.panel_abc);


        textCore.setCustomSelectionActionModeCallback(new android.view.ActionMode.Callback() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                menu.removeItem(android.R.id.selectAll);
                menu.removeItem(android.R.id.cut);
                menu.removeItem(android.R.id.copy);
                return true;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                menu.add(0, DEFINITION, 0, R.string.kahskjas).setIcon(R.drawable.ic_translator);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {}

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
                        final String selectedText = textCore.getText().subSequence(min, max).toString();
                        translateCore(selectedText);
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
                                translateCore(selectedText);


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


                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(Utils.getSelectWordses());
               // selectText.notShowMenu();
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
                                    searsh_text.setVisibility(View.VISIBLE);
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
                List<MDictionary> list= new ArrayList<>();
                for (MDictionary word : mDictionaryList) {
                    if(word.keyWord.toUpperCase().contains(editText.getText().toString().toUpperCase())){
                        list.add(word);
                    }
                }
                if(list.size()==0){
                    Utils.SenderYandex(string,list,MainActivity.this);
                }
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(list);
                selectText.show(getSupportFragmentManager(),"skdsjf");
            }
        });

        //mDictionaryList=Configure.getSession().getList(MDictionary.class," 1=1 ORDER BY keyWord ASC  ");
        new PreviewTask().execute();

        Utils.indexSurogat=0;
        for (MDictionary mDictionary : mDictionaryList) {
            if(mDictionary.index>Utils.indexSurogat){
                Utils.indexSurogat=mDictionary.index;
            }
        }





        findViewById(R.id.bt_random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogWordRandom dialogWord=new DialogWordRandom();
                int index=new Random().nextInt(mDictionaryList.size())-1;
                dialogWord.setDictionary(mDictionaryList.get(index));
                dialogWord.show(getSupportFragmentManager(),"sdsd");
            }
        });

        findViewById(R.id.bt_show_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateText(null,false);
                listActivate(mDictionaryList);
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>0){
                    List<MDictionary> listTemp=new ArrayList<>();
                    for (MDictionary word : mDictionaryList) {
                        if(word.keyWord.toUpperCase().contains(charSequence.toString().toUpperCase())){
                            listTemp.add(word);
                        }
                    }

                    if(!isText){
                        listActivate(listTemp);
                    }
                }else{
                    if(!isText){
                        listActivate(mDictionaryList);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        listView.setOnCreateContextMenuListener(this);


       // listActivate(mDictionaryList);


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

                        for (MDictionary mDictionary : mDictionaryList) {
                           if(mDictionary.isSelect){
                               mDictionary.isSelect=false;
                               Configure.getSession().update(mDictionary);
                           }
                        }

                        Toast.makeText(MainActivity.this, R.string.all_removed, Toast.LENGTH_SHORT).show();
                       if(!isText){
                           activateText(null,false);
                           listActivate(mDictionaryList);
                       }


                    }
                });
            }
        });
    }

    private void translateCore(String selectedText) {
        if(selectedText.trim().length()>0){

            List<MDictionary> dictionaryArrayList= new ArrayList<>();
            for (MDictionary mDictionary : mDictionaryList) {
                if(mDictionary.valueWord.toUpperCase().contains(selectedText.toUpperCase())){
                    dictionaryArrayList.add(mDictionary);
                }
            }
            if(dictionaryArrayList.size()>0){

            }else{
               Utils.SenderYandex(selectedText, dictionaryArrayList,MainActivity.this);

            }
            DialogSearshWord selectText=new DialogSearshWord();
            selectText.setDictionary(dictionaryArrayList);
            selectText.show(getSupportFragmentManager(),"skdsjf");

        }
    }




    void activateText(String text,boolean isShow){
        isText=isShow;
        if(isShow){
            textCore.setText(text);
            panelText.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            panelAbc.setVisibility(View.GONE);
        }else {
            panelText.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            panelAbc.setVisibility(View.VISIBLE);
        }
    }

    void listActivate(List<MDictionary> words){
        adapter = new MyArrayAdapterWord(this, R.layout.simple_list_item_1, new ArrayList<>(words));
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


    public  void notifyE() {
        if(adapter!=null){
           // activateText(null,false);
           // listActivate(mDictionaryList);
            adapter.notifyDataSetChanged();
        }
    }


    public  void listRefrach() {
        if(adapter!=null){
             activateText(null,false);
             listActivate(mDictionaryList);
            //adapter.notifyDataSetChanged();
        }
    }



    private void createListABC() {
        ListView listViewE = (ListView) findViewById(R.id.listView_alphavit);

        List<MDictionary> mD=new ArrayList<>(Utils.listABC.length);
        for (String s : Utils.listABC) {
            MDictionary d=new MDictionary();
            d.valueWord=s;
            mD.add(d);
        }


        MyArrayAdapterWord aa = new MyArrayAdapterWord(this, R.layout.simple_list_item_2, new ArrayList<>(mD));
        listViewE.setAdapter(aa);
        listViewE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = Utils.listABC[position];
                List<MDictionary> dictionaryList=new ArrayList<>();
                for (MDictionary dictionary : mDictionaryList) {
                    char d=dictionary.keyWord.trim().toUpperCase().charAt(0);
                    char d2=string.charAt(0);
                    if(d==d2){
                        dictionaryList.add(dictionary);
                    }
                }
                listActivate(dictionaryList);
            }
        });
    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;

        if(adapter!=null){
            final MDictionary mDictionary = adapter.getItem(position);
            if(!mDictionary.isSelect){
                menu.add(R.string.add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        mDictionary.isSelect=true;
                        mDictionary.index=++Utils.indexSurogat;
                        Configure.getSession().update(mDictionary);
                        for(int i = 0;i<listView.getChildCount();i++){
                            MDictionary dictionary = ((MDictionary) listView.getChildAt(i).getTag());
                            if(dictionary.id==mDictionary.id){
                                listView.getChildAt(i).findViewById(R.id.red_star).setVisibility(View.VISIBLE);
                            }
                        }
                        Toast.makeText(MainActivity.this, R.string.add, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }


            if(mDictionary.isSelect){
                menu.add(R.string.remove).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        mDictionary.isSelect=false;
                        mDictionary.index=0;
                        Configure.getSession().update(mDictionary);
                        for(int i = 0;i<listView.getChildCount();i++){
                            MDictionary dd = ((MDictionary) listView.getChildAt(i).getTag());
                            if(dd.id==mDictionary.id){
                                listView.getChildAt(i).findViewById(R.id.red_star).setVisibility(View.INVISIBLE);
                            }
                        }
                        Toast.makeText(MainActivity.this, R.string.removed, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }

            menu.add(R.string.edit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    DialogEditWord editWord=new DialogEditWord();
                    editWord.setWord(mDictionary);
                    editWord.setIAction(new IAction() {
                        @Override
                        public void action(Object o) {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    editWord.show(getSupportFragmentManager(),"ada");
                    Toast.makeText(MainActivity.this, R.string.edited, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            menu.add(R.string.addWord).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    DialogAddWord editWord=new DialogAddWord();
                    editWord.setIAction(new IAction() {
                        @Override
                        public void action(Object o) {
                            Configure.getSession().insert(o);
                            mDictionaryList.add((MDictionary) o);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, R.string.addnew, Toast.LENGTH_SHORT).show();
                        }
                    });
                    editWord.show(getSupportFragmentManager(),"ada");
                    return false;
                }
            });

            menu.add(R.string.delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Utils.messageBox(getString(R.string.asddd), getString(R.string.dasffsf), MainActivity.this, new IAction() {
                        @Override
                        public void action(Object o) {
                            mDictionaryList.remove(mDictionary);
                            Configure.getSession().delete(mDictionary);
                            activateText(null,false);
                            listActivate(mDictionaryList);
                            Toast.makeText(MainActivity.this, R.string.removed_permanent , Toast.LENGTH_SHORT).show();
                        }
                    });
                        return false;
                }
            });

        }else{
            if(isText) return;
            String string=((TextView)aMenuInfo.targetView).getText().toString();
            final List<MDictionary> list = Configure.getSession().getList(MDictionary.class," is_select = 1 and valueWord = '"+ string+"'");
            menu.add(R.string.remove).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    for (MDictionary dictionary : list) {
                        dictionary.isSelect=false;
                        Configure.getSession().update(dictionary);
                    }
                    ////////////////////////
                    activateText(null,false);
                    adapter=null;
                    String[] array = new String[Utils.getSelectWordses().size()];
                    for(int i=0;i<Utils.getSelectWordses().size();i++){
                        array[i]=Utils.getSelectWordses().get(i).valueWord;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, array);
                    listView.setAdapter(adapter);
                    //////////////////////////
                    Toast.makeText(MainActivity.this, R.string.removed, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    int index=0;
    @Override
    public void onBackPressed() {
        if(++index<2){
            Toast.makeText(this, R.string.eqek, Toast.LENGTH_SHORT).show();
        }else {
            index=0;
            finish();

        }
    }



    class PreviewTask extends AsyncTask<Void, Void, List<MDictionary>> {



        @Override
        protected void onPostExecute(List<MDictionary> mDictionaries) {
                mDictionaryList=mDictionaries;
            listActivate(mDictionaries);
            createListABC();
            MainActivity.this.findViewById(R.id.panel_buton_top).setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MDictionary> doInBackground(Void... voids) {
           List<MDictionary> sd=Configure.getSession().getList(MDictionary.class," 1=1 ORDER BY keyWord ASC  ");
            return sd;
        }
    }
}


