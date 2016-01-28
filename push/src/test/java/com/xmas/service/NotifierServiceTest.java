package com.xmas.service;

import com.xmas.service.notifiers.safari.SafariNotifier;
import com.xmas.service.notifiers.chrome.ChromeNotifier;
import com.xmas.service.notifiers.email.EmailNotifier;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;
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
        doReturn(LocalDateTime.of(2015, 12, 26, 11, 29, 40)).when(LocalDateTime.now());
    }
    
    @Test
    @Ignore
    public void testCalculateDelay() throws Exception {
        assertEquals(3600*3, notifierService.calculateDelay(LocalDateTime.parse("2015-12-26T11:29:40")));
    }
}