package com.xmas.service.push;

import com.xmas.dao.push.DeviceRepository;
import com.xmas.dao.push.UsersRepository;
import com.xmas.entity.push.Device;
import com.xmas.entity.push.User;
import com.xmas.exceptions.push.NoSuchDeviceFound;
import com.xmas.exceptions.push.NoSuchUserFoundException;
import com.xmas.exceptions.push.UserAlreadyPresentedException;
import com.xmas.service.push.locationprovider.IPLocationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
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
            usersRepository.delete(GUID);
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
        usersRepository.getUserByGUID(GUID)
                .map(user -> {
                    if (user.getDevices().contains(device)) return user;

                    device.setIp(ip);
                    locationProvider.getLocation(ip).ifPresent(device::setLocation);

                    device.setUser(user);
                    deviceRepository.save(device);

                    user.getDevices().add(device);
                    return usersRepository.save(user);
                }).orElseThrow(() -> new NoSuchUserFoundException(GUID));
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
