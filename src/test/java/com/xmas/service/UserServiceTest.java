package com.xmas.service;

import com.xmas.dao.DeviceRepository;
import com.xmas.dao.UsersRepository;
import com.xmas.entity.push.Device;
import com.xmas.entity.push.Medium;
import com.xmas.entity.push.User;
import com.xmas.location.provider.IPLocationProvider;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UsersRepository usersRepository;
    @Mock
    DeviceRepository deviceRepository;
    @Mock
    IPLocationProvider locationProvider;

    @InjectMocks
    UserService userService;

    Medium chrome;
    Medium safari;

    User chromeUser;
    User safariUser;
    User allDevicesUser;

    Device chromeDevice;
    Device safariDevice;
    Device newChromeDevice;

    public static final String CHROME_TOKEN = "asdfgsdfgsdfhrtyjfghdfgdfghdfg";
    public static final String NEW_CHROME_TOKEN = "asdfgsdfgsdfhrtyjf78963543218ghdfgdfghdfg";
    public static final String SAFARI_TOKEN = "asdfgsdfgssdfghsdfgdfhrtyjfghdfgdfghdfg";

    public static final String IP = "192.168.0.1";

    public static final Long CHROME_USER_GUID = 123456L;
    public static final Long SAFARI_USER_GUID = 123456789L;
    public static final Long ALL_DEVICES_USER_GUID = 12345601246L;
    public static final Long UNUSED_GUID = 1111111L;

    @Before
    public void setUp() throws Exception {
        chrome = new Medium(Medium.CHROME);
        safari = new Medium(Medium.SAFARI);

        /*
         * Set up devices
         */
        chromeDevice = new Device();
        chromeDevice.setMedium(chrome);
        chromeDevice.setToken(CHROME_TOKEN);

        newChromeDevice = new Device();
        newChromeDevice.setMedium(chrome);
        newChromeDevice.setToken(NEW_CHROME_TOKEN);

        safariDevice = new Device();
        safariDevice.setMedium(safari);
        safariDevice.setToken(SAFARI_TOKEN);



        /*
         * Set up users
         */
        chromeUser = new User();
        chromeUser.setGuid(CHROME_USER_GUID);
        chromeUser.setDevices(new ArrayList<Device>() {{
            add(chromeDevice);
        }});
        chromeDevice.setUser(chromeUser);

        safariUser = new User();
        safariUser.setGuid(SAFARI_USER_GUID);
        safariUser.setDevices(new ArrayList<Device>() {{
            add(safariDevice);
        }});
        safariDevice.setUser(safariUser);

        allDevicesUser = new User();
        allDevicesUser.setGuid(ALL_DEVICES_USER_GUID);
        allDevicesUser.setDevices(new ArrayList<Device>() {{
            add(safariDevice);
            add(chromeDevice);
        }});


        when(usersRepository.getUserByGUID(anyLong())).thenReturn(Optional.<User>empty());
        when(usersRepository.getUserByGUID(CHROME_USER_GUID)).thenReturn(Optional.of(chromeUser));
        when(usersRepository.getUserByGUID(SAFARI_USER_GUID)).thenReturn(Optional.of(safariUser));
        when(usersRepository.getUserByGUID(ALL_DEVICES_USER_GUID)).thenReturn(Optional.of(allDevicesUser));

        when(usersRepository.save(chromeUser)).thenReturn(chromeUser);
        when(usersRepository.save(safariUser)).thenReturn(safariUser);
        when(usersRepository.save(allDevicesUser)).thenReturn(allDevicesUser);

        when(deviceRepository.getAll()).thenReturn(new HashSet<Device>() {{
            add(chromeDevice);
            add(safariDevice);
        }});

        when(deviceRepository.save(safariDevice)).thenThrow(ConstraintViolationException.class);

        when(locationProvider.getLocation(IP)).thenReturn(Optional.<String>empty());
    }

    @Test
    public void testAddDeviceIfNoSuchDevices() throws Exception {
        userService.addDevice(newChromeDevice, CHROME_USER_GUID, IP);

        verify(deviceRepository, times(1)).save(newChromeDevice);
        verify(locationProvider, times(1)).getLocation(IP);
        verify(usersRepository, times(1)).save(chromeUser);

        assertTrue(chromeUser.getDevices().contains(newChromeDevice));
        assertTrue(chromeUser.getDevices().contains(chromeDevice));
        assertEquals(2, chromeUser.getDevices().size());
    }

    @Test
    public void testAddDeviceIfThisUserAlreadyHasSuchDevice() throws Exception {
        userService.addDevice(chromeDevice, CHROME_USER_GUID, IP);

        verify(deviceRepository, times(0)).getAll();

        verify(deviceRepository, times(0)).save(newChromeDevice);
        verify(locationProvider, times(0)).getLocation(IP);
        verify(usersRepository, times(0)).save(chromeUser);

        assertTrue(chromeUser.getDevices().contains(chromeDevice));
        assertEquals(1, chromeUser.getDevices().size());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAddDeviceIfOtherUserAlreadyHasSuchDevice() throws Exception {
        userService.addDevice(safariDevice, CHROME_USER_GUID, IP);
    }

    @Test
    public void testDeleteDeviceIfNoSuchUser() throws Exception {
        userService.deleteDevice(UNUSED_GUID, CHROME_TOKEN);

        assertTrue(chromeUser.getDevices().contains(chromeDevice));
        verifyZeroInteractions(deviceRepository);
    }

    @Test
    public void testDeleteDeviceIfNoSuchDevice() throws Exception {
        userService.deleteDevice(CHROME_USER_GUID, SAFARI_TOKEN);

        assertTrue(chromeUser.getDevices().contains(chromeDevice));
        verifyZeroInteractions(deviceRepository);
    }

    @Test
    public void testDeleteDevice() throws Exception {
        userService.deleteDevice(CHROME_USER_GUID, CHROME_TOKEN);

        assertTrue(!chromeUser.getDevices().contains(chromeDevice));
        assertEquals(0, chromeUser.getDevices().size());
        verify(deviceRepository, times(1)).delete(chromeDevice);
        verify(usersRepository, times(1)).save(chromeUser);
    }
}