package com.xmas.service;

import com.xmas.dao.DeviceRepository;
import com.xmas.dao.UsersRepository;
import com.xmas.entity.Device;
import com.xmas.entity.User;
import com.xmas.exceptions.DeviceAlreadyPresentedException;
import com.xmas.exceptions.NoSuchDeviceFound;
import com.xmas.exceptions.NoSuchUserFoundException;
import com.xmas.exceptions.UserAlreadyPresentedException;
import com.xmas.location.provider.IPLocationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    IPLocationProvider locationProvider;

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        usersRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUser(Long GUID) {
        return usersRepository.getUserByGUID(GUID).orElseThrow(() -> new NoSuchUserFoundException(GUID));
    }

    public User getUser(String userName) {
        return usersRepository.getUserByName(userName).orElseThrow(() -> new NoSuchUserFoundException(userName));
    }

    public void addUser(Long GUID) {
        if (!usersRepository.getUserByGUID(GUID).isPresent()) {
            User user = new User();
            user.setGuid(GUID);

            usersRepository.save(user);
        } else {
            throw new UserAlreadyPresentedException(GUID);
        }
    }

    public void deleteUser(Long GUID) {
        if (!usersRepository.getUserByGUID(GUID).isPresent()) {
            User user = new User();
            user.setGuid(GUID);

            usersRepository.delete(user);
        } else {
            throw new NoSuchUserFoundException(GUID);
        }
    }

    public Iterable<Device> getDevices(Long GUID) {
        return getUser(GUID).getDevices();
    }

    public Device getDevice(Long GUID, Integer deviceId) {
        return getUser(GUID).getDevices().stream()
                .filter(d -> d.getId().equals(deviceId))
                .findFirst().orElseThrow(() -> new NoSuchDeviceFound(deviceId));
    }

    public void addDevice(Device device, Long GUID, String ip) {
        User user = usersRepository.getUserByGUID(GUID)
                .orElseThrow(() -> new NoSuchUserFoundException(GUID));

        //Do nothing if there already presented device with same token and medium
        if (user.getDevices().contains(device)) return;

        //throw exception if such device is owned by other user
        deviceRepository.getAll().stream()
                .filter(d -> d.equals(device))
                .map(Device::getUser)
                .map(User::getGuid)
                .findAny()
                .ifPresent(id -> {
                    throw new DeviceAlreadyPresentedException();
                });

        locationProvider.getLocation(ip).ifPresent(location -> {
            device.setIp(ip);
            device.setLocation(location);
        });

        device.setUser(user);
        deviceRepository.save(device);

        user.getDevices().add(device);
        usersRepository.save(user);
    }

    public void deleteDevice(Long GUID, String token) {
        usersRepository.getUserByGUID(GUID)
                .ifPresent(user -> user.getDevices().stream()
                        .filter(d -> d.getToken().equals(token))
                        .findAny()
                        .ifPresent(d -> {
                            user.getDevices().remove(d);
                            deviceRepository.delete(d);
                            usersRepository.save(user);
                        }));
    }
}
