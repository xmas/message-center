package com.xmas.service;

import com.xmas.dao.MediumsRepository;
import com.xmas.dao.MessageRepository;
import com.xmas.dao.UserMessageRepository;
import com.xmas.entity.push.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessagesServiceTest {

    @Mock
    UserService userService;

    @Mock
    NotifierService notifierService;

    @Mock
    MediumsRepository mediumsRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    UserMessageRepository userMessageRepository;

    @InjectMocks
    MessagesService messagesService;

    User chromeUser;
    User safariUser;
    User allDevicesUser;

    UserMessage chromeUserAllDevMessage;
    UserMessage safariUserAllDevMessage;

    Device chromeDevice;
    Device safariDevice;

    Message messageForChromeUserForChromeDevice;
    Message messageForChromeUserForSafariDevice;
    Message messageForAllUsersForChromeDevice;
    Message messageForAllUsersForAllDevices;

    Medium chrome;
    Medium safari;

    public static final String CHROME_TOKEN = "asdfgsdfgsdfhrtyjfghdfgdfghdfg";
    public static final String SAFARI_TOKEN = "asdfgsdfgssdfghsdfgdfhrtyjfghdfgdfghdfg";

    public static final Long CHROME_USER_GUID = 123456L;
    public static final Long SAFARI_USER_GUID = 123456789L;
    public static final Long ALL_DEVICES_USER_GUID = 12345601246L;

    @org.junit.Before
    public void setUp() throws Exception {

        chrome = new Medium(Medium.CHROME);
        safari = new Medium(Medium.SAFARI);

        /* Set up devices*/
        chromeDevice = new Device();
        chromeDevice.setMedium(chrome);
        chromeDevice.setToken(CHROME_TOKEN);

        safariDevice = new Device();
        safariDevice.setMedium(safari);
        safariDevice.setToken(SAFARI_TOKEN);

        /* Set up users*/
        chromeUser = new User();
        chromeUser.setGuid(CHROME_USER_GUID);
        chromeUser.setDevices(new ArrayList<Device>() {{
            add(chromeDevice);
        }});

        safariUser = new User();
        safariUser.setGuid(SAFARI_USER_GUID);
        safariUser.setDevices(new ArrayList<Device>() {{
            add(safariDevice);
        }});

        allDevicesUser = new User();
        allDevicesUser.setGuid(ALL_DEVICES_USER_GUID);
        allDevicesUser.setDevices(new ArrayList<Device>() {{
            add(safariDevice);
            add(chromeDevice);
        }});

        chromeUserAllDevMessage = new UserMessage();
        chromeUserAllDevMessage.setUser(chromeUser);

        safariUserAllDevMessage = new UserMessage();
        safariUserAllDevMessage.setUser(safariUser);


        UserMessage allUserMessage = new UserMessage();
        allUserMessage.setUser(safariUser);


        /* Set up messages*/
        messageForChromeUserForChromeDevice = new Message();
        messageForChromeUserForChromeDevice.setTitle("HELLO!");
        messageForChromeUserForChromeDevice.setMessage("hello world!");
        messageForChromeUserForChromeDevice.setUsers(new ArrayList<UserMessage>() {{
            add(chromeUserAllDevMessage);
        }});
        messageForChromeUserForChromeDevice.setMediums(new HashSet<Medium>() {{
            add(chrome);
        }});

        messageForChromeUserForSafariDevice = new Message();
        messageForChromeUserForSafariDevice.setTitle("HELLO!");
        messageForChromeUserForSafariDevice.setMessage("hello world!");
        messageForChromeUserForSafariDevice.setUsers(new ArrayList<UserMessage>() {{
            add(chromeUserAllDevMessage);
        }});
        messageForChromeUserForSafariDevice.setMediums(new HashSet<Medium>() {{
            add(safari);
        }});

        messageForAllUsersForChromeDevice = new Message();
        messageForAllUsersForChromeDevice.setTitle("HELLO!");
        messageForAllUsersForChromeDevice.setMessage("hello world!");
        messageForAllUsersForChromeDevice.setUsers(new ArrayList<>());
        messageForAllUsersForChromeDevice.setMediums(new HashSet<Medium>() {{
            add(chrome);
        }});

        messageForAllUsersForAllDevices = new Message();
        messageForAllUsersForAllDevices.setTitle("HELLO!");
        messageForAllUsersForAllDevices.setMessage("hello world!");
        messageForAllUsersForAllDevices.setUsers(new ArrayList<>());
        messageForAllUsersForAllDevices.setMediums(new HashSet<>());

        /* Set up mocks*/
        when(userService.getUser(CHROME_USER_GUID)).thenReturn(chromeUser);
        when(userService.getUser(SAFARI_USER_GUID)).thenReturn(safariUser);
        when(userService.getAll()).thenReturn(new ArrayList<User>() {{
            add(chromeUser);
            add(safariUser);
        }});

        when(mediumsRepository.getAll()).thenReturn(new HashSet<Medium>() {{
            add(chrome);
            add(safari);
        }});
    }

    @Test
    public void addMessagesTestChromeUserChromeDevice() {
        messagesService.addMessage(messageForChromeUserForChromeDevice);

        verify(userMessageRepository, times(1)).save(any(UserMessage.class));
        verify(messageRepository, times(1)).save(messageForChromeUserForChromeDevice);
        verify(notifierService, times(1)).push(chrome, messageForChromeUserForChromeDevice, new ArrayList<String>() {{
            add(CHROME_TOKEN);
        }});
    }


    @Test
    public void addMessagesTestForChromeUserSafariDevice() {
        messagesService.addMessage(messageForChromeUserForSafariDevice);

        verify(userMessageRepository, times(1)).save(any(UserMessage.class));
        verify(messageRepository, times(1)).save(any(Message.class));
        verifyZeroInteractions(notifierService);

        assertTrue(chromeUser.getUserMessages() == null);
    }

    @Test
    public void addMessagesTestForAllUsersChromeDevice() {
        messagesService.addMessage(messageForAllUsersForChromeDevice);

        verify(userMessageRepository, times(2)).save(any(UserMessage.class));
        verify(messageRepository, times(1)).save(messageForAllUsersForChromeDevice);
        verify(notifierService, times(1)).push(chrome, messageForAllUsersForChromeDevice, new ArrayList<String>() {{
            add(CHROME_TOKEN);
        }});
    }
    /*
     * Message should be added for both chrome and safari users
     * NotifierService should be invoked twice, once for chrome, and once for safari
     */
    @Test
    public void addMessagesTestForAllUsersAllDevices() {
        messagesService.addMessage(messageForAllUsersForAllDevices);

        verify(userMessageRepository, times(2)).save(any(UserMessage.class));

        verify(messageRepository, times(1)).save(messageForAllUsersForAllDevices);

        verify(notifierService, times(1)).push(chrome, messageForAllUsersForAllDevices, new ArrayList<String>() {{
            add(CHROME_TOKEN);
        }});
        verify(notifierService, times(1)).push(safari, messageForAllUsersForAllDevices, new ArrayList<String>() {{
            add(SAFARI_TOKEN);
        }});
    }


}