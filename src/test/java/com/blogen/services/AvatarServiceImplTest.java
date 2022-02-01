package com.blogen.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration test for AvatarService
 *
 * @author Cliff
 */
@SpringBootTest
public class AvatarServiceImplTest {

    //total number of avatar images in /img directory
    private static final int TOTAL_AVATAR_FILES = 8;

    @Autowired
    private AvatarService avatarService;


    @Test
    public void should_returnAllImageNamesInImgDirectory_when_getAllAvatarImageNames() throws Exception {
        List<String> imageNames = avatarService.getAllAvatarImageNames();
        assertThat( imageNames, is( notNullValue() ) );
        assertThat( imageNames.size(), is(TOTAL_AVATAR_FILES) );
    }
}