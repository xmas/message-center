package com.xmas.service;

import com.xmas.dao.UsersRepository;
import com.xmas.entity.Device;
import com.xmas.entity.User;
import com.xmas.exceptions.NoSuchDeviceFound;
import com.xmas.exceptions.NoSuchUserFoundException;
import com.xmas.exceptions.UserAlreadyPresentedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UsersRepository usersRepository;


    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        usersRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUser(Long GUID){
        return usersRepository.getUserByGUID(GUID).orElseThrow(() ->new NoSuchUserFoundException(GUID));
    }

    public User getUser(String userName){
        return usersRepository.getUserByName(userName).orElseThrow(() -> new NoSuchUserFoundException(userName));
    }

    public void addUser(Long GUID){
        if(! usersRepository.getUserByGUID(GUID).isPresent()){
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
                .orElseThrow(() -> new NoSuchUserFoundException(GUID));

        device.setUser(user);
        user.getDevices().add(device);

        usersRepository.save(user);
    }

    public void deleteDevice(Long GUID, Long deviceId){
        User user = usersRepository.getUserByGUID(GUID)
                .orElseThrow(() -> new NoSuchUserFoundException(GUID));

        user.getDevices().stream()
                .filter(d -> d.getId().equals(deviceId)).findFirst()
                .ifPresent(d -> user.getDevices().remove(d));
    }
}
