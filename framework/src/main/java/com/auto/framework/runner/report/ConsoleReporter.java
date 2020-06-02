package com.auto.framework.runner.report;

import com.auto.framework.reporter.data.TestDataReporterItem;
import com.auto.framework.runner.console.ConsoleStyle;
import com.auto.framework.runner.data.ExecutionResult;
import com.auto.framework.runner.job.TestJobResult;
import dnl.utils.text.table.TextTable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
        List<TestJobResult> jobResults = result.getJobResults();
        String[] dynamicColumns = jobResults.stream()
                .flatMap(jobResult -> jobResult.getCustomTestData().stream())
                .filter(TestDataReporterItem::shouldBeReported)
                .map(item -> StringUtils.capitalize(item.getKey()))
                .distinct()
                .toArray(String[]::new);
        String[] headerColumns = Stream.of(DETAIL_COLUMN_NAMES, dynamicColumns).flatMap(Arrays::stream).toArray(String[]::new);

        Object[][] data = new Object[jobResults.size()][headerColumns.length];
        for (int i = 0; i < jobResults.size(); i++) {
            for (int j = 0; j < headerColumns.length; j++) {
                String currentHeader = headerColumns[j];
                if (currentHeader.equals("Class")) {
                    data[i][j] = jobResults.get(i).getClassName();
                } else if (currentHeader.equals("Test")) {
                    data[i][j] = jobResults.get(i).getName();
                } else if (currentHeader.equals("Status")) {
                    data[i][j] = getStatus(jobResults.get(i));
                } else if (currentHeader.equals("Message")) {
                    data[i][j] = jobResults.get(i).getErrorMessage();
                } else if (CollectionUtils.isNotEmpty(jobResults.get(i).getCustomTestData())) {
                    Optional<TestDataReporterItem> any = jobResults.get(i).getCustomTestData()
                            .stream()
                            .filter(a -> currentHeader.equalsIgnoreCase(a.getKey()))
                            .findAny();
                    if (any.isPresent()) {
                        data[i][j] = any.get().getValue();
                    } else {
                        data[i][j] = StringUtils.EMPTY;
                    }
                }
            }
        }
        TextTable tt = new TextTable(headerColumns, data);
        tt.printTable();
    }

    private String getStatus(TestJobResult result) {
        return result.isPass() ? ConsoleStyle.TICK : ConsoleStyle.CROSS;
    }
}
