package com.auto.framework.utils.regex;

import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class RegexParseResult {
    private List<String> lstParsedItems;
    private boolean match;

    public RegexParseResult(List<String> lstItems, boolean bMatch) {
        lstParsedItems = lstItems;
        match = bMatch;
    }

    public boolean isMatch() {
        return match;
    }

    public List<String> getParsedItems() {
        return lstParsedItems;
    }
}
