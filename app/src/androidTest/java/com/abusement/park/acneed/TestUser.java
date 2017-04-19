package com.abusement.park.acneed;


import com.abusement.park.acneed.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TestUser {

    @Test
    public void testUserCreatedFromJson()throws Exception {
        String json = "{\"email\":\"jbarkley3@gatech.edu\",\"images\":[{\"uploadDate\":1492224533886," +
                "\"uri\":\"content://com.android.providers.media.documents/document/image%3A387061\"}," +
                "{\"uploadDate\":1492228366268," +
                "\"uri\":\"file:///storage/emulated/0/Acneed/ACNEED20170414_235237-1450574735.jpg\"}," +
                "{\"uploadDate\":1492228412081," +
                "\"uri\":\"file:///storage/emulated/0/Acneed/ACNEED20170414_235310188516535.jpg\"}," +
                "{\"uploadDate\":1492281775199," +
                "\"uri\":\"file:///storage/emulated/0/Acneed/ACNEED20170415_144244-1450574735.jpg\"}," +
                "{\"uploadDate\":1492281785897," +
                "\"uri\":\"file:///storage/emulated/0/Acneed/ACNEED20170415_144258188516535.jpg\"}," +
                "{\"uploadDate\":1492281799597," +
                "\"uri\":\"file:///storage/emulated/0/Acneed/ACNEED20170415_1443091012477031.jpg\"}," +
                "{\"uploadDate\":1492329778556," +
                "\"uri\":\"file:///storage/emulated/0/Acneed/ACNEED20170416_040233-1450574735.jpg\"}]," +
                "\"password\":\"jbarkley3@gatech.edu\",\"settings\":{\"alertFrequency\":7}," +
                "\"suggestions\":[{\"creationDate\":1492490468660,\"details\":\"like the title says. Matt Ryan the " +
                "goat. nothing left to debate tbh\",\"downVotes\":0," +
                "\"id\":\"149249046866015pP7bjrgKea3QsG0qFiBAXcllz1\",\"title\":\"Matt Ryan the Goat\",\"upVotes\":0," +
                "\"user\":\"15pP7bjrgKea3QsG0qFiBAXcllz1\",\"totalVotes\":0},{\"creationDate\":1492490583178," +
                "\"details\":\"$500,000 for it. fwm\\n\\n\\nlol Nah I'm jk. \\n\\ngn\",\"downVotes\":0," +
                "\"id\":\"149249058317815pP7bjrgKea3QsG0qFiBAXcllz1\",\"title\":\"Isotretinoin\",\"upVotes\":0," +
                "\"user\":\"15pP7bjrgKea3QsG0qFiBAXcllz1\",\"totalVotes\":0}]," +
                "\"uid\":\"15pP7bjrgKea3QsG0qFiBAXcllz1\"," +
                "\"videos\":[{\"filePath\":\"/storage/emulated/0/Acneed/MyJourney1012477031.mp4\"," +
                "\"uploadDate\":1492314142254,\"uri\":\"file:///storage/emulated/0/Acneed/MyJourney1012477031.mp4\"}," +
                "{\"filePath\":\"/storage/emulated/0/Acneed/MyJourney188516535.mp4\",\"uploadDate\":1492329828043," +
                "\"uri\":\"file:///storage/emulated/0/Acneed/MyJourney188516535.mp4\"}]}";
        ObjectMapper objectMapper = new ObjectMapper();
        User user  = objectMapper.readValue(json, User.class);
        assertNotNull(user);
    }

}
