package com.twinstech.expenzo.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import com.twinstech.expenzo.R;
import com.twinstech.expenzo.database.ExpenseDatabaseHelper;
import com.twinstech.expenzo.presenter.ExpensePresenter;
import com.twinstech.expenzo.view.ExpenseView;

public class ExpenseFragment extends Fragment implements ExpenseView, View.OnClickListener {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.new_expense, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    ExpenseDatabaseHelper expenseDatabaseHelper = new ExpenseDatabaseHelper(this.getActivity());
    ExpensePresenter expensePresenter = new ExpensePresenter(expenseDatabaseHelper, this);
    expensePresenter.setExpenseTypes();
    expenseDatabaseHelper.close();

    Button addExpenseButton = (Button) getActivity().findViewById(R.id.add_expense);
    addExpenseButton.setOnClickListener(this);
  }

  @Override
  public String getAmount() {
    TextView view = (TextView) getActivity().findViewById(R.id.amount);
    return view.getText().toString();
  }

  @Override
  public String getType() {
    Spinner spinner = (Spinner) getActivity().findViewById(R.id.expense_type);
    return (String) spinner.getSelectedItem();
  }

  @Override
  public void renderExpenseTypes(List<String> expenseTypes) {
    Spinner spinner = (Spinner) getActivity().findViewById(R.id.expense_type);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, expenseTypes);
    spinner.setAdapter(adapter);
  }

  @Override
  public void displayError() {
    TextView view = (TextView) getActivity().findViewById(R.id.amount);
    view.setError(getActivity().getString(R.string.amount_empty_error));
  }

  @Override
  public void onClick(View view) {
    ExpenseDatabaseHelper expenseDatabaseHelper = new ExpenseDatabaseHelper(this.getActivity());
    ExpensePresenter expensePresenter = new ExpensePresenter(expenseDatabaseHelper, this);
    if(expensePresenter.addExpense()){
      MainActivity activity = (MainActivity) getActivity();
      Toast.makeText(activity, R.string.expense_add_successfully, Toast.LENGTH_LONG).show();
      activity.onExpenseAdded();
    }
    expenseDatabaseHelper.close();
  }
}
