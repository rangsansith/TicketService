package korntip.skyict.co.th.ticketservice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import korntip.skyict.co.th.ticketservice.MainActivity;
import korntip.skyict.co.th.ticketservice.R;
import korntip.skyict.co.th.ticketservice.ServiceActivity;
import korntip.skyict.co.th.ticketservice.utility.MyAlertDialog;
import korntip.skyict.co.th.ticketservice.utility.ReadAllData;

public class MainFragment extends Fragment{

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();

//        Submit Controller
        submitController();

    } // Main Method

    private void submitController() {
        Button button = getView().findViewById(R.id.btnSubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userEditText = getView().findViewById(R.id.edtUser);
                EditText passEditText = getView().findViewById(R.id.edtPassword);

                String userString = userEditText.getText().toString().trim();
                String passwordString = passEditText.getText().toString().trim();

//                Check Space
                if (userString.isEmpty() || passwordString.isEmpty()) {

//                    Have Space
                    MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());
                    myAlertDialog.normalDialog(getString(R.string.title_have_space),
                            getString(R.string.message_have_space));

                } else {

//                    No Space

                    try {

                        boolean userBool = true;
                        String truePasswordString = null, nameString = null, idString = null;
                        String urlJSON = "http://androidthai.in.th/gate/getAllUserKet.php";
                        ReadAllData readAllData = new ReadAllData(getActivity());
                        readAllData.execute(urlJSON);
                        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());

                        String resultJSON = readAllData.get();
                        Log.d("28MayV1", "JSON ==> "+ resultJSON);

                        JSONArray jsonArray = new JSONArray(resultJSON);
                        for (int i=0; i<jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (userString.equals(jsonObject.getString("User"))) {
                                userBool = false;
                                truePasswordString = jsonObject.getString("Password");
                                nameString = jsonObject.getString("Name");
                                idString = jsonObject.getString("id");
                            } else if (passwordString.equals(truePasswordString)) {
                                Toast.makeText(getActivity(), "Welcome " + nameString,
                                        Toast.LENGTH_SHORT).show();

//                                Intent to ServiceActivity
                                intentToService(nameString, idString);

                            } else {
                                myAlertDialog.normalDialog("Password False",
                                        "Please Try Password False");
                            }

                        }

                        if (userBool) {
//                            User False
                            myAlertDialog.normalDialog("User False",
                                    "No This User in my Database");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }   // if


            }// onClick
        });
    }

    private void intentToService(String nameString, String idString) {
        Intent intent = new Intent(getActivity(), ServiceActivity.class);
        intent.putExtra("id", idString);
        intent.putExtra("Name", nameString);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemSetting) {

//            Replace Fragment
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentMainFrgment, new SettingFragment())
                    .addToBackStack(null)
                    .commit();


            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main, menu);

    }

    private void createToolbar() {

        Toolbar toolbar = getView().findViewById(R.id.toolbarMain);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

//        Setup Title
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Welcome");

        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        return view;
    }
}
