package com.example.amol.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
//commit
public abstract class TabFragment extends Fragment {

   public abstract void switchTab(View switchTo);

   public static final String EMPLOYER_TAB = "employer";
   public static final String ADDRESS_TAB = "address";
   public static final String INFO_TAB = "info";

   public String currentTab;
    
   public TextView txtInfoHeader;
   public TextView txtAddressHeader;
   public TextView txtEmployerHeader;
    
   public void setOnClickListenerToTab(){
       txtInfoHeader.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               handleInfoOnClick();
           }
       });

       txtAddressHeader.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               handleAddressOnClick();
           }
       });

       txtEmployerHeader.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               handleEmployerOnClick();
           }
       });
       
   }

    private void handleEmployerOnClick() {
        if(!currentTab.equals(EMPLOYER_TAB)){
            switchTab(txtEmployerHeader);
        }
    }

    private void handleInfoOnClick() {
        if(!currentTab.equals(INFO_TAB)){
            //setAdapterToSpinnerForDependents();
            //onClick(txtInfoHeader);
            switchTab(txtInfoHeader);
        }
    }

    private void handleAddressOnClick() {
        if(!currentTab.equals(ADDRESS_TAB)){
            switchTab(txtAddressHeader);
        }
    }

}
