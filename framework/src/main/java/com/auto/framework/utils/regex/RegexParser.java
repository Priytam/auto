package com.auto.framework.utils.regex;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class RegexParser {

    public static RegexParseResult parse(String sRegex, String sTarget) {
        List<String> lstResult = null;
        boolean bMatch = false;
        Pattern pattern = Pattern.compile(sRegex);
        Matcher matcher = pattern.matcher(sTarget);
        if (matcher.matches()) {
            bMatch = true;
            lstResult = Lists.newArrayList();
            for (int i = 1; i <= matcher.groupCount(); i++) {
                lstResult.add(matcher.group(i));
            }

        }
        return new RegexParseResult(lstResult, bMatch);
    }
}
