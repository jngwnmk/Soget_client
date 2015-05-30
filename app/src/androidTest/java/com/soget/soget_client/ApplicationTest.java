package com.soget.soget_client;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.connector.bookmark.BookmarkMakeTask;
import com.soget.soget_client.connector.user.UserLoginTask;
import com.soget.soget_client.model.Authorization;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.model.User;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public void loginTest(){
        User user = new User();
        user.setUserId("admin");
        user.setPassword("admin");
        new UserLoginTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                assertNotNull(object);
                String token = ((Authorization)object).getAccess_token();
                final Bookmark bookmark = new Bookmark();
                bookmark.setUrl("http://www.ytn.co.kr/_sn/0117_201505151413059846_005");
                bookmark.setInitUserId("admin");
                bookmark.setPrivacy(false);

                new BookmarkMakeTask(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        assertEquals("admin",((Bookmark)object).getInitUserId());
                    }
                }, bookmark, token).execute();

            }
        }, user).execute();
    }




}