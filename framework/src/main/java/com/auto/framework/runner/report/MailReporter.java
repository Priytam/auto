package com.auto.framework.runner.report;

import com.auto.framework.env.TestEnvironment;
import com.auto.framework.reporter.data.TestDataReporterItem;
import com.auto.framework.runner.data.ExecutionResult;
import com.auto.framework.runner.data.ExecutionSummary;
import com.auto.framework.runner.job.TestJobResult;
import com.auto.framework.runner.mail.MailConfig;
import com.auto.framework.runner.mail.MailSender;
import com.auto.framework.runner.mail.MailTemplateBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.mail.MessagingException;
import java.util.ArrayList;
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
public class MailReporter extends AbstractReporter {
    private List<MailTemplateBuilder.TableBuilder> tableBuilder;
    boolean isMailSent = false;
    private MailConfig mailConfig;

    public MailReporter(MailConfig mailConfig, List<MailTemplateBuilder.TableBuilder> tableBuilders) {
        this.mailConfig = mailConfig;
        this.tableBuilder = tableBuilders;
    }

    public MailReporter(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    @Override
    public void accept(ExecutionResult result) {
        try {
            if (mailConfig.getEnabled()) {
                String sHtml = buildTemplate(result);
                isMailSent = sendReport(sHtml);
            } else {
                isMailSent = false;
            }
        } catch (Exception e) {
            isMailSent = true;
        }
    }

    private String buildTemplate(ExecutionResult result) {
        MailTemplateBuilder mailTemplateBuilder = new MailTemplateBuilder()
                .withTable(buildSummaryTable(result.getSummary()))
                .withTable(buildDetailReport(result.getJobResults()));
        if (null != tableBuilder) {
            tableBuilder.forEach(builder -> mailTemplateBuilder.withTable(builder.build()));
        }
        return mailTemplateBuilder.build();
    }

    private String buildDetailReport(List<TestJobResult> jobResults) {
        String[] dynamicColumns = jobResults.stream()
                .flatMap(jobResult -> jobResult.getCustomTestData().stream())
                .filter(TestDataReporterItem::shouldBeReported)
                .map(item -> StringUtils.capitalize(item.getKey()))
                .distinct()
                .toArray(String[]::new);
        String[] headerColumns = Stream.of(DETAIL_COLUMN_NAMES, dynamicColumns).flatMap(Arrays::stream).toArray(String[]::new);

        List<String[]> rows = new ArrayList<>();
        for (TestJobResult result : jobResults) {
            String[] row = new String[headerColumns.length];
            for (int i = 0; i < headerColumns.length; i++) {
                String columnName = headerColumns[i];
                if (columnName.equals("Class")) {
                    row[i] = result.getClassName();
                } else if (columnName.equals("Test")) {
                    row[i] = result.getName();
                } else if (columnName.equals("Status")) {
                    row[i] = result.isPass() ? "Pass" : "Fail";
                } else if (columnName.equals("Message")) {
                    row[i] = result.getErrorMessage();
                } else if (CollectionUtils.isNotEmpty(result.getCustomTestData())) {
                    Optional<TestDataReporterItem> any = result
                            .getCustomTestData()
                            .stream()
                            .filter(a -> columnName.equalsIgnoreCase(a.getKey()))
                            .findAny();
                    if (any.isPresent()) {
                        row[i] = any.get().getValue().toString();
                    } else {
                        row[i] = StringUtils.EMPTY;
                    }
                }
            }
            rows.add(row);
        }
        return new MailTemplateBuilder.TableBuilder()
                .withTitle("Regression detail report")
                .withHeaders(headerColumns)
                .withRows(rows)
                .build();
    }

    private String buildSummaryTable(ExecutionSummary summary) {
        String[] strings = {String.valueOf(summary.getPass()), String.valueOf(summary.getFatal()), String.valueOf(summary.getFail())};
        List<String[]> rows = new ArrayList<>();
        rows.add(strings);
        return new MailTemplateBuilder.TableBuilder()
                .withTitle("Regression summary")
                .withHeaders(SUMMARY_COLUMN_NAMES)
                .withRows(rows)
                .build();
    }


    private boolean sendReport(String sMailTemplate) throws MessagingException {
        String host = mailConfig.getHost();
        if (StringUtils.isEmpty(host)) {
            return false;
        }
        String port = mailConfig.getPort();
        if (StringUtils.isEmpty(port)) {
            return false;
        }
        String to = mailConfig.getTo();
        if (StringUtils.isEmpty(to)) {
            return false;
        }
        String from = mailConfig.getFrom();
        if (StringUtils.isEmpty(from)) {
            return false;
        }
        String fromName = mailConfig.getFromName();
        if (StringUtils.isEmpty(fromName)) {
            fromName = "Priytam Jee Pandey";
        }
        String user = mailConfig.getUser();
        String password = mailConfig.getFrom();
        String subjectMessage = "(Automated mail) " + TestEnvironment.getCurrentApplication() + " automation report";
        MailSender mailSender = new MailSender(host, port, user, password);
        mailSender.sendEmail(to, from, subjectMessage, sMailTemplate, fromName, "text/html");
        return true;
    }

    public boolean isMailSent() {
        return isMailSent;
    }
}
