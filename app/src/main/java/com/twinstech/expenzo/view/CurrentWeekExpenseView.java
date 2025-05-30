package com.twinstech.expenzo.view;

import java.util.List;
import java.util.Map;

import com.twinstech.expenzo.model.Expense;

public interface CurrentWeekExpenseView {
  void displayCurrentWeeksExpenses(Map<String, List<Expense>> expensesByDate);

  void displayTotalExpenses(Long totalExpense);
}
