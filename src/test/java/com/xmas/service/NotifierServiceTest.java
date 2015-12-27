package com.xmas.service;

import com.xmas.notifiers.chrome.ChromeNotifier;
import com.xmas.notifiers.email.EmailNotifier;
import com.xmas.notifiers.safari.SafariNotifier;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LocalDateTime.class)
public class NotifierServiceTest {

    @Mock
    private ChromeNotifier chromeNotifier;

    @Mock
    SafariNotifier safariNotifier;

    @Mock
    EmailNotifier emailNotifier;

    @Mock
    ScheduledExecutorService executor;
    
    @InjectMocks
    NotifierService notifierService;

    @Before
    public void init() throws Exception {
        mockStatic(LocalDateTime.class);
        //when(LocalDateTime.now()).thenReturn(LocalDateTime.of(2015, 12, 26, 11, 29, 40));
        doReturn(LocalDateTime.of(2015, 12, 26, 11, 29, 40)).when(LocalDateTime.now());
    }
    
    @Test
    @Ignore
    public void testCalculateDelay() throws Exception {
        assertEquals(3600*3, notifierService.calculateDelay(ZonedDateTime.parse("2015-12-26T11:29:40+03:00")));
    }
}