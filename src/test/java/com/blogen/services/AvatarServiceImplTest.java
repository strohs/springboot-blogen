package com.blogen.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Integration test for AvatarService
 *
 * @author Cliff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AvatarServiceImplTest {

    //total number of avatar images in /img directory
    private static final int TOTAL_AVATAR_FILES = 6;

    @Autowired
    private AvatarService avatarService;


    @Test
    public void should_returnAllImageNamesInImgDirectory_when_getAllAvatarImageNames() throws Exception {
        List<String> imageNames = avatarService.getAllAvatarImageNames();
        assertThat( imageNames, is( notNullValue() ) );
        assertThat( imageNames.size(), is(TOTAL_AVATAR_FILES) );
    }
}