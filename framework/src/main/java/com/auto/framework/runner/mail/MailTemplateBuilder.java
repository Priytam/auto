package com.auto.framework.runner.mail;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class MailTemplateBuilder {

    private final String sPrefix = "<!doctype html><html><head> <meta name=\"viewport\" content=\"width=device-width\"> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title>Simple Transactional Email</title> <style> table.emailTables { font-family: \"Comic Sans MS\", cursive, sans-serif; border: 0px solid #948473; background-color: #FFE3C6; width: 100%; height: 100%; text-align: center; border-collapse: collapse; } table.emailTables td, table.emailTables th { border: 1px solid #948473; padding: 4px 0px; } table.emailTables tbody td { font-size: 13px; } table.emailTables td:nth-child(even) { background: #D0E4F5; } table.emailTables thead { background: #948473; background: -moz-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%); background: -webkit-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%); background: linear-gradient(to bottom, #afa396 0%, #9e9081 66%, #948473 100%); } table.emailTables thead th { font-size: 17px; font-weight: bold; color: #F0F0F0; text-align: center; border-left: 2px solid #948473; } table.emailTables thead th:first-child { border-left: none; } table.emailTables tfoot td { font-size: 16px; } </style></head><body>";
    private final String sPostfix = "<body></html>";
    private static final List<String> tables = Lists.newArrayList();

    public static class TableBuilder {
        private String tableHeader = "";
        private String tableRow = "";
        private String tableHeading = "";

        public TableBuilder withHeaders(String[] headers) {
            StringBuilder stringBuilder = new StringBuilder();
            String sHeading = "";
            stringBuilder.append(sHeading).append("<thead><tr>");
            for (String header : headers) {
                stringBuilder.append(sHeading).append("<th>" + header + "</th>");
            }
            tableHeader = stringBuilder.append(sHeading).append("</tr></thead>").toString();
            return this;
        }

        public TableBuilder withRows(List<String[]> rows) {
            StringBuilder stringBuilder = new StringBuilder();
            String sRow = "";
            stringBuilder.append(sRow).append("<tbody>");
            for (String[] row : rows) {
                stringBuilder.append(sRow).append("<tr>");
                for (String col : row) {
                    stringBuilder.append(sRow).append("<td>").append(col).append("</td>");
                }
                stringBuilder.append(sRow).append("</tr>");
            }
            tableRow = stringBuilder.append(sRow).append("</tbody>").toString();
            return this;
        }

        public TableBuilder withTitle(String heading) {
            tableHeading = heading;
            return this;
        }

        public String build() {
            return "<h2>" + tableHeading + "</h2><table class=\"emailTables\">" + tableHeader + tableRow + "</table>";
        }
    }

    public MailTemplateBuilder withTable(String sTableTemplate) {
        tables.add(sTableTemplate);
        return this;
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sPrefix);
        for (String table : tables) {
            stringBuilder.append(table);
        }
        return stringBuilder.append(sPostfix).toString();
    }
}
