package services.data;

import com.mongodb.WriteResult;
import models.BusinessUserProfile;
import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;


public class DBProfileServiceTest {

    static play.test.FakeApplication application;

    @BeforeClass
    public static void setUpClass() {
        application = fakeApplication();
    }
    static String correctUserId = "5554f84952423afe1e6ebdcf";


    @Test
    public void addReadRemoveProfileTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                DBProfileService dbProfileService = DBServicesProvider.getDbProfileService();
                String testName = "Klub Studio";
                BusinessUserProfile newProfile = new BusinessUserProfile();
                newProfile.setName(testName);
                dbProfileService.save(newProfile);
                //Class<String> stringClass = String.class;
                ObjectId newProfileId = newProfile.getId();
                BusinessUserProfile receivedProfile = dbProfileService.get(newProfileId);
                assertNotNull("Entity of this id: " + newProfile + " not found", receivedProfile);

                assertEquals("Not adding/read profile to/from db failed", testName, receivedProfile.getName());

                WriteResult writeResult = dbProfileService.deleteById(newProfileId);
                assertEquals("Deleting object failed", 1, writeResult.getN());
            }
        });
    }

    @Test
    public void findingEntityByQueryTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                DBProfileService dbProfileService = DBServicesProvider.getDbProfileService();
                Query<BusinessUserProfile> query = dbProfileService.createQuery().field(Mapper.ID_KEY).equal(new ObjectId(correctUserId));

                BusinessUserProfile userProfile = dbProfileService.find(query).get();
                assertNotNull("When trying to find entity of existing user by querry with id", userProfile);
                System.out.println(userProfile);
            }
        });
    }




}