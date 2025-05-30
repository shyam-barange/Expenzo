package com.twinstech.expenzo.view;

import java.util.List;

import com.twinstech.expenzo.model.Expense;

public interface TodaysExpenseView {
  void displayTotalExpense(Long totalExpense);
  void displayTodaysExpenses(List<Expense> expenses);
}
