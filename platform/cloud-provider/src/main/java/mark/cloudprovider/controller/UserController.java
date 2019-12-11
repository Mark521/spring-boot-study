package mark.cloudprovider.controller;

import mark.cloudprovider.entity.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController  {

    @GetMapping("/{id}")
    public UserInfo findByID(@PathVariable Long id){
        UserInfo u = new UserInfo();

        if(id == 1){
            u.setId(1L);
            u.setName("mark");
            u.setUserName("LiangMark");
            u.setAge(20);
            u.setGender(false);
        }else{
            u.setId(2L);
            u.setName("Fark");
            u.setUserName("YouFark");
            u.setAge(18);
            u.setGender(true);
        }
        return u;
    }

}
