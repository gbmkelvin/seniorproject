package cn.project.camt.cookingmethod;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.project.camt_cfc.R;

public class SingleItemViewFragment extends Fragment {

	// Declare Variables

	String cmid;
	String cminstruction;
	ProgressDialog mProgressDialog;
	
	TextView cmidtxt;
	TextView cmsinstruction;
	public SingleItemViewFragment (){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recipecookingmethods_fragment, container,false);
		
		 cmidtxt = (TextView) view.findViewById(R.id.cmidtxt);
		 cmsinstruction = (TextView) view.findViewById(R.id.cminstructiontxt);
		
		Bundle bundle = getArguments();
		if (bundle!=null) {
			String textIdString =bundle.getString("cmid");
			cmidtxt.setText(textIdString);
			String textInstruction = bundle.getString("cminstruction");
			cmsinstruction.setText(textInstruction);
		}
		return view;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}


}
