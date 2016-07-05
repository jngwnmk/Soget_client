package com.markin.app;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.connector.bookmark.BookmarkMakeTask;
import com.markin.app.connector.user.UserLoginTask;
import com.markin.app.model.Authorization;
import com.markin.app.model.Bookmark;
import com.markin.app.model.User;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public void loginTest(){
        /*User user = new User();
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
        }, user).execute();*/
    }




}