package com.auto.reqres.tests;

import com.auto.framework.check.Check;
import com.auto.framework.rules.mock.MockRequestResponse;
import com.auto.framework.rules.mock.RequestType;
import com.auto.framework.utils.FileUtil;
import com.auto.reqres.framework.ReqResWithMockServerTestCase;
import com.auto.reqres.model.User;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;

/**
 * User: Priytam Jee Pandey
 * Date: 07/06/20
 * Time: 1:20 pm
 * email: mrpjpandey@gmail.com
 */

public class BasicMockServerTest extends ReqResWithMockServerTestCase {


    @Test
    @MockRequestResponse(type = RequestType.GET, url = "/api/users/2", responsePath = "user2.json")
    public void validMockUser() throws Exception {
        prepareJson();
        User user = getMockedUser(2);
        Check.assertNotNull(user, "User not found");
        Check.assertNotNull(user.getData(), "User data was null");
        Check.assertEquals("janet.weaver@reqres.in", user.getData().getEmail(), "Incorrect email id");
        Check.assertEquals(2, user.getData().getId(), "Id was incorrect");
    }

    private void prepareJson() throws Exception {
        String pathname = "/tmp/user2.json";
        File file = new File(pathname);
        FileUtil.delete(file);
        FileWriter fFileWriter = new FileWriter(file, true);
        fFileWriter.append("{\n" +
                "   \"data\": {\n" +
                "      \"id\": 2,\n" +
                "      \"email\": \"janet.weaver@reqres.in\",\n" +
                "      \"first_name\": \"Janet\",\n" +
                "      \"last_name\": \"Weaver\",\n" +
                "      \"avatar\": \"https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg\"\n" +
                "   },\n" +
                "   \"ad\": {\n" +
                "      \"company\": \"StatusCode Weekly\",\n" +
                "      \"url\": \"http://statuscode.org/\",\n" +
                "      \"text\": \"A weekly newsletter focusing on software development, infrastructure, the server, performance, and the stack end of things.\"\n" +
                "   }\n" +
                "}");
        fFileWriter.close();
    }
}

