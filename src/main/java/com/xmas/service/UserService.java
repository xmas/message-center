package com.xmas.service;

import com.xmas.dao.UsersRepository;
import com.xmas.entity.Device;
import com.xmas.entity.User;
import com.xmas.exceptions.NoSuchDeviceFound;
import com.xmas.exceptions.NoSuchUserPresentedException;
import com.xmas.exceptions.UserAlreadyPresentedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UsersRepository usersRepository;


    public User getUser(Long GUID){
        return usersRepository.getUserByGUID(GUID).orElseThrow(() ->new NoSuchUserPresentedException(GUID));
    }

    public void addUser(Long GUID){
        if(usersRepository.getUserByGUID(GUID).isPresent()){
            User user = new User();
            user.setGUID(GUID);

            usersRepository.save(user);
        }else {
            throw new UserAlreadyPresentedException(GUID);
        }
    }

    public Device getDevice(Long GUID, Integer deviceId){
        return getUser(GUID).getDevices().stream()
                .filter(d -> d.getId().equals(deviceId))
                .findFirst().orElseThrow(() -> new NoSuchDeviceFound(deviceId));
    }

    public void addDevice(Device device, Long GUID){
        User user = usersRepository.getUserByGUID(GUID)
                .orElseThrow(() -> new NoSuchUserPresentedException(GUID));

        user.getDevices().add(device);

        usersRepository.save(user);
    }

    public void deleteDevice(Long GUID, Long deviceId){
        User user = usersRepository.getUserByGUID(GUID)
                .orElseThrow(() -> new NoSuchUserPresentedException(GUID));

        user.getDevices().stream()
                .filter(d -> d.getId().equals(deviceId)).findFirst()
                .ifPresent(d -> user.getDevices().remove(d));
    }
}
