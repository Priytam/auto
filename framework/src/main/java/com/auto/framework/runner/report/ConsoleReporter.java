package com.auto.framework.runner.report;

import com.auto.framework.runner.console.ConsoleStyle;
import com.auto.framework.runner.data.ExecutionResult;
import com.auto.framework.runner.job.TestJobResult;
import dnl.utils.text.table.TextTable;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class ConsoleReporter extends AbstractReporter {

    @Override
    public void accept(ExecutionResult result) {
        try {
            System.out.println();
            System.out.println();
            System.out.println(ConsoleStyle.YELLOW + ConsoleStyle.BOLD + "Detail report" + ConsoleStyle.RESET);
            printTestDetail(result);
            System.out.println();
            System.out.println();
            System.out.println(ConsoleStyle.YELLOW + ConsoleStyle.BOLD + "Test Summary" + ConsoleStyle.RESET);
            printSummary(result);
            System.out.println();
            System.out.println();
        } catch (Exception e) {
        }
    }

    private void printSummary(ExecutionResult result) {
        Object[][] summaryData = new Object[1][3];
        summaryData[0][0] = result.getSummary().getPass();
        summaryData[0][1] = result.getSummary().getFatal();
        summaryData[0][2] = result.getSummary().getFail();
        TextTable summary = new TextTable(SUMMARY_COLUMN_NAMES, summaryData);
        summary.printTable();
    }

    private void printTestDetail(ExecutionResult result) {
        Object[][] data = new Object[result.getJobResults().size()][DETAIL_COLUMN_NAMES.length];
        for (int i = 0; i < result.getJobResults().size(); i++) {
            for (int j = 0; j < DETAIL_COLUMN_NAMES.length; j++) {
                if (DETAIL_COLUMN_NAMES[j].equals("Class")) {
                    data[i][j] = result.getJobResults().get(i).getClassName();
                } else if (DETAIL_COLUMN_NAMES[j].equals("Test")) {
                    data[i][j] = result.getJobResults().get(i).getName();
                } else if (DETAIL_COLUMN_NAMES[j].equals("Status")) {
                    data[i][j] = getStatus(result.getJobResults().get(i));
                } else if (DETAIL_COLUMN_NAMES[j].equals("Message")) {
                    data[i][j] = result.getJobResults().get(i).getErrorMessage();
                }
            }
        }
        TextTable tt = new TextTable(DETAIL_COLUMN_NAMES, data);
        tt.printTable();
    }

    private String getStatus(TestJobResult result) {
        return result.isPass() ? ConsoleStyle.TICK : ConsoleStyle.CROSS;
    }
}
